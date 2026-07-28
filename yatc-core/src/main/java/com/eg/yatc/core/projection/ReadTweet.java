package com.eg.yatc.core.projection;

public record ReadTweet(
        long id,
        String content,
        long userId,
        String username
)
{}
