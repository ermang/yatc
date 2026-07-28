package com.eg.yatc.core.controller;

import com.eg.yatc.core.projection.ReadTweet;
import com.eg.yatc.core.req.CreateTweetReq;
import com.eg.yatc.core.service.TweetService;
import com.eg.yatc.core.servicereq.CreateTweetServiceReq;
import com.eg.yatc.core.servicereq.DeleteTweetServiceReq;
import com.eg.yatc.core.servicereq.LikeTweetServiceReq;
import com.eg.yatc.core.util.Req2ServiceReqMapper;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tweet")
public class TweetController {

    private final Req2ServiceReqMapper req2ServiceReqMapper;
    private final TweetService tweetService;

    public TweetController(Req2ServiceReqMapper req2ServiceReqMapper, TweetService tweetService) {
        this.req2ServiceReqMapper = req2ServiceReqMapper;
        this.tweetService = tweetService;
    }

    @PostMapping
    public void createTweet(@RequestBody CreateTweetReq req) {
        CreateTweetServiceReq serviceReq = req2ServiceReqMapper.createTweetReq2CreateTweetServiceReq(req);

        tweetService.createTweet(serviceReq);
    }

    @GetMapping("/{tweetId}")
    public ReadTweet readThread(@PathVariable long tweetId){
        ReadTweet readTweet = tweetService.readTweet(tweetId);

        return readTweet;
    }

    @DeleteMapping("/{tweetId}")
    public void deleteTweet(@PathVariable long tweetId){
        DeleteTweetServiceReq serviceReq = req2ServiceReqMapper.deleteTweet2DeleteTweetServiceReq(tweetId);
        tweetService.deleteTweet(serviceReq);
    }

    @PostMapping("/like/{tweetId}")
    public void likeTweet(@PathVariable long tweetId){
        LikeTweetServiceReq serviceReq = req2ServiceReqMapper.likeTweet2LikeTweetServiceReq(tweetId);
        tweetService.likeTweet(serviceReq);
    }
}
