package com.eg.yatc.core.util;

import com.eg.yatc.core.req.CreateTweetReq;
import com.eg.yatc.core.servicereq.CreateTweetServiceReq;
import com.eg.yatc.core.servicereq.DeleteTweetServiceReq;
import com.eg.yatc.core.servicereq.LikeTweetServiceReq;
import com.eg.yatc.core.servicereq.ReadTimelineServiceReq;
import org.springframework.stereotype.Component;

@Component
public class Req2ServiceReqMapper {

    private final ActiveUserResolver activeUserResolver;

    public Req2ServiceReqMapper(ActiveUserResolver activeUserResolver) {
        this.activeUserResolver = activeUserResolver;
    }

    public CreateTweetServiceReq createTweetReq2CreateTweetServiceReq(CreateTweetReq req) {
        Long id = activeUserResolver.getUserId();
        CreateTweetServiceReq serviceReq = new CreateTweetServiceReq(id, req.content);

        return  serviceReq;
    }

    public LikeTweetServiceReq likeTweet2LikeTweetServiceReq(long tweetId) {
        Long userId = activeUserResolver.getUserId();
        LikeTweetServiceReq serviceReq = new LikeTweetServiceReq(tweetId, userId);

        return serviceReq;
    }

    public DeleteTweetServiceReq deleteTweet2DeleteTweetServiceReq(long tweetId) {
        Long userId = activeUserResolver.getUserId();
        DeleteTweetServiceReq serviceReq = new DeleteTweetServiceReq(tweetId, userId);

        return serviceReq;
    }

    public ReadTimelineServiceReq readTimeLineReq2readTimelineServiceReq(Long tweetId) {
        Long userId = activeUserResolver.getUserId();
        ReadTimelineServiceReq serviceReq = new ReadTimelineServiceReq(tweetId, userId);

        return serviceReq;
    }
}
