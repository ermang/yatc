package com.eg.yatc.user.entity;

import jakarta.persistence.*;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"follower_id", "followee_id"}))
public class Follow extends BaseEntity{

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private AppUser follower;

    @ManyToOne(optional = false)
    private AppUser followee;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


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
