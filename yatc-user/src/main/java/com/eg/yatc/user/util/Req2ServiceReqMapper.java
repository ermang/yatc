package com.eg.yatc.user.util;


import com.eg.yatc.user.servicereq.FollowServiceReq;
import org.springframework.stereotype.Component;

@Component
public class Req2ServiceReqMapper {

    private final ActiveUserResolver activeUserResolver;

    public Req2ServiceReqMapper(ActiveUserResolver activeUserResolver) {
        this.activeUserResolver = activeUserResolver;
    }

    public FollowServiceReq followReq2FollowServiceReq(long followeeId) {
        long followerId = activeUserResolver.getUserId();

        FollowServiceReq serviceReq = new FollowServiceReq(followerId, followeeId);

        return serviceReq;
    }

}
