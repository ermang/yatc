package com.eg.yatc.core.service;

import com.eg.yatc.core.entity.Tweet;
import com.eg.yatc.core.entity.UserTweetLikeRel;
import com.eg.yatc.core.projection.ReadTweet;
import com.eg.yatc.core.repo.TweetRepo;
import com.eg.yatc.core.repo.UserProjectionRepo;
import com.eg.yatc.core.repo.UserTweetLikeRelRepo;
import com.eg.yatc.core.resp.ReadTimeline;
import com.eg.yatc.core.servicereq.CreateTweetServiceReq;
import com.eg.yatc.core.servicereq.DeleteTweetServiceReq;
import com.eg.yatc.core.servicereq.LikeTweetServiceReq;
import com.eg.yatc.core.servicereq.ReadTimelineServiceReq;
import com.eg.yatc.core.util.ActiveUserResolver;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Transactional
@Service
public class TweetService {
    private final ActiveUserResolver activeUserResolver;
    private final TweetRepo tweetRepo;
    private final UserProjectionRepo userProjectionRepo;
    private final UserTweetLikeRelRepo userTweetLikeRelRepo;

    public TweetService(ActiveUserResolver activeUserResolver, TweetRepo tweetRepo, UserProjectionRepo userProjectionRepo, UserTweetLikeRelRepo userTweetLikeRelRepo) {
        this.activeUserResolver = activeUserResolver;
        this.tweetRepo = tweetRepo;
        this.userProjectionRepo = userProjectionRepo;
        this.userTweetLikeRelRepo = userTweetLikeRelRepo;
    }

    public void createTweet(CreateTweetServiceReq serviceReq) {

            Tweet t = new Tweet();
            t.setContent(serviceReq.content());
            t.setUserId(serviceReq.appUserId());

            tweetRepo.save(t);
    }

    public ReadTweet readTweet(long tweetId) {
        ReadTweet rt = tweetRepo.findOneByIdRO(tweetId);

        return rt;
    }

    public void deleteTweet(DeleteTweetServiceReq serviceReq) {
        Tweet t = tweetRepo.findById(serviceReq.tweetId()).orElseThrow(
                () -> new NoSuchElementException("service.validation.tweet.not.exists"));

        if (!t.getUserId().equals(serviceReq.userId())) {
            throw new AuthorizationDeniedException("service.validation.tweet.not.owned.by.user");
        }
        else {
            userTweetLikeRelRepo.deleteAllByTweetId(serviceReq.tweetId());
            tweetRepo.deleteById(serviceReq.tweetId());
        }

    }

    public void likeTweet(LikeTweetServiceReq serviceReq) {
        Tweet t = tweetRepo.findById(serviceReq.tweetId()).orElseThrow(
                () -> new NoSuchElementException("service.validation.tweet.not.exists"));

        boolean alreadyLiked = userTweetLikeRelRepo.existsByTweetIdAndUserId(serviceReq.tweetId(), serviceReq.userId());

        if (alreadyLiked)
            throw new UnsupportedOperationException("service.validation.tweet.already.liked");

        t.setLikeCount(t.getLikeCount() + 1);

        tweetRepo.save(t);

        UserTweetLikeRel likeRel = new UserTweetLikeRel();
        likeRel.setTweet(t);
        likeRel.setUserId(t.getUserId());

        userTweetLikeRelRepo.save(likeRel);
    }

    public ReadTimeline readTimeline(ReadTimelineServiceReq serviceReq) {
        if (serviceReq.tweetId() == null) {
            List<ReadTweet> readTweetList = tweetRepo.getTimeline(serviceReq.userId(), PageRequest.of(0, 20));
            ReadTimeline readTimeline = new ReadTimeline(readTweetList);

            return readTimeline;
        }

        return null;
    }
}
