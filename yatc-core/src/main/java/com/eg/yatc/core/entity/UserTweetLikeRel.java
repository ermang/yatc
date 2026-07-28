package com.eg.yatc.core.entity;

import jakarta.persistence.*;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"user_projection_id", "tweet_id"}))
public class UserTweetLikeRel extends BaseEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private UserProjection userProjection;

    @ManyToOne(optional = false)
    private Tweet tweet;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserProjection getUserProjection() {
        return userProjection;
    }

    public void setUserProjection(UserProjection userProjection) {
        this.userProjection = userProjection;
    }

    public Tweet getTweet() {
        return tweet;
    }

    public void setTweet(Tweet tweet) {
        this.tweet = tweet;
    }
}
