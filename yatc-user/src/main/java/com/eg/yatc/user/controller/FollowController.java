package com.eg.yatc.user.controller;

import com.eg.yatc.user.resp.ReadFollowerListResp;
import com.eg.yatc.user.service.FollowService;
import com.eg.yatc.user.servicereq.FollowServiceReq;
import com.eg.yatc.user.util.Req2ServiceReqMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FollowController {

    private final Req2ServiceReqMapper req2ServiceReqMapper;
    private final FollowService followService;

    public FollowController(Req2ServiceReqMapper req2ServiceReqMapper, FollowService followService) {
        this.req2ServiceReqMapper = req2ServiceReqMapper;
        this.followService = followService;
    }

    @PostMapping("/follow/{followeeId}")
    public void followUser(@PathVariable long followeeId) {
        FollowServiceReq serviceReq = req2ServiceReqMapper.followReq2FollowServiceReq(followeeId);

        followService.follow(serviceReq);
    }

    @GetMapping("/follower-list/{followeeId}")
    public ReadFollowerListResp readFollowers(@PathVariable long followeeId) {
        //FollowServiceReq serviceReq = req2ServiceReqMapper.followReq2FollowServiceReq(followeeId);

        ReadFollowerListResp resp = followService.readFollowers(followeeId);

        return resp;
    }
}
