package com.eg.yatc.core.resp;

import com.eg.yatc.core.projection.ReadTweet;

import java.util.List;

public class ReadTimeline {

    public final List<ReadTweet> readTweetList;

    public ReadTimeline(List<ReadTweet> readTweetList) {
        this.readTweetList = readTweetList;
    }
}
