package com.eg.yatc.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"follower_id", "followee_id"}))
public class Follow extends BaseEntity{

    @ManyToOne(optional = false)
    private AppUser follower;

    @ManyToOne(optional = false)
    private AppUser followee;

    public AppUser getFollower() {
        return follower;
    }

    public void setFollower(AppUser follower) {
        this.follower = follower;
    }

    public AppUser getFollowee() {
        return followee;
    }

    public void setFollowee(AppUser followee) {
        this.followee = followee;
    }
}
