package com.eg.yatc.user.service;

import com.eg.yatc.user.entity.AppUser;
import com.eg.yatc.user.entity.Follow;
import com.eg.yatc.user.repo.AppUserRepo;
import com.eg.yatc.user.repo.FollowRepo;
import com.eg.yatc.user.resp.ReadFollowerListResp;
import com.eg.yatc.user.resp.ReadFollowerResp;
import com.eg.yatc.user.servicereq.FollowServiceReq;
import jakarta.persistence.EntityExistsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Transactional
@Service
public class FollowService {

    private final AppUserRepo appUserRepo;
    private final FollowRepo followRepo;

    public FollowService(AppUserRepo appUserRepo, FollowRepo followRepo) {
        this.appUserRepo = appUserRepo;
        this.followRepo = followRepo;
    }

    public void follow(FollowServiceReq serviceReq) {

        boolean alreadyFollowed = followRepo.existsByFollowerIdAndFolloweeId(serviceReq.followerId(), serviceReq.followeeId());

        if (alreadyFollowed)
            throw new EntityExistsException("service.validation.user.already.follows");

        AppUser follower = appUserRepo.findById(serviceReq.followerId()).orElseThrow(() -> new NoSuchElementException("service.validation.no.such.user"));
        AppUser followee = appUserRepo.findById(serviceReq.followeeId()).orElseThrow(() -> new NoSuchElementException("service.validation.no.such.user"));

        Follow f = new Follow();
        f.setFollower(follower);
        f.setFollowee(followee);

        followRepo.save(f);
    }

    public ReadFollowerListResp readFollowers(long followeeId) {

        List<ReadFollowerResp> readFollowerRespList =  followRepo.findAllByFolloweeId(followeeId);
        ReadFollowerListResp resp = new ReadFollowerListResp(readFollowerRespList);

        return resp;
    }
}
