package com.eg.yatc.core.service;

import com.eg.yatc.core.entity.Tweet;
import com.eg.yatc.core.entity.UserProjection;
import com.eg.yatc.core.entity.UserTweetLikeRel;
import com.eg.yatc.core.projection.ReadTweet;
import com.eg.yatc.core.repo.TweetRepo;
import com.eg.yatc.core.repo.UserProjectionRepo;
import com.eg.yatc.core.repo.UserTweetLikeRelRepo;
import com.eg.yatc.core.servicereq.CreateTweetServiceReq;
import com.eg.yatc.core.servicereq.DeleteTweetServiceReq;
import com.eg.yatc.core.servicereq.LikeTweetServiceReq;
import com.eg.yatc.core.util.ActiveUserResolver;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

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

        Optional<UserProjection> userProjectionOptional= userProjectionRepo.findById(serviceReq.appUserId());

        if(userProjectionOptional.isPresent()) {
            Tweet t = new Tweet();
            t.setContent(serviceReq.content());
            t.setUserProjection(userProjectionOptional.get());

            tweetRepo.save(t);
        } else {
            UserProjection up = new UserProjection();
            up.setId(serviceReq.appUserId());
            up.setUsername(activeUserResolver.getActiveUser().getUsername());

            up = userProjectionRepo.save(up);

            Tweet t = new Tweet();
            t.setContent(serviceReq.content());
            t.setUserProjection(up);

            tweetRepo.save(t);
        }

    }

    public ReadTweet readTweet(long tweetId) {
        ReadTweet rt = tweetRepo.findOneByIdRO(tweetId);

        return rt;
    }

    public void deleteTweet(DeleteTweetServiceReq serviceReq) {
        Tweet t = tweetRepo.findById(serviceReq.tweetId()).orElseThrow(
                () -> new NoSuchElementException("service.validation.tweet.not.exists"));

        if (!t.getUserProjection().getId().equals(serviceReq.userId())) {
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

        boolean alreadyLiked = userTweetLikeRelRepo.existsByTweetIdAndUserProjectionId(serviceReq.tweetId(), serviceReq.userId());

        if (alreadyLiked)
            throw new UnsupportedOperationException("service.validation.tweet.already.liked");

        t.setLikeCount(t.getLikeCount() + 1);

        tweetRepo.save(t);

        UserTweetLikeRel likeRel = new UserTweetLikeRel();
        likeRel.setTweet(t);
        likeRel.setUserProjection(t.getUserProjection());

        userTweetLikeRelRepo.save(likeRel);
    }
}
