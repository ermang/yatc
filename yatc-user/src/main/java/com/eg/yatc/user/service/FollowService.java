package com.eg.yatc.user.service;

import com.eg.yatc.user.constant.OutboxStatus;
import com.eg.yatc.user.entity.AppUser;
import com.eg.yatc.user.entity.Follow;
import com.eg.yatc.user.entity.OutboxEvent;
import com.eg.yatc.user.event.FollowEvent;
import com.eg.yatc.user.repo.AppUserRepo;
import com.eg.yatc.user.repo.FollowRepo;
import com.eg.yatc.user.repo.OutboxRepo;
import com.eg.yatc.user.resp.ReadFollowerListResp;
import com.eg.yatc.user.resp.ReadFollowerResp;
import com.eg.yatc.user.servicereq.FollowServiceReq;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityExistsException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Transactional
@Service
public class FollowService {

    private final AppUserRepo appUserRepo;
    private final FollowRepo followRepo;
    private final OutboxRepo outboxRepo;
    private final KafkaTemplate kafkaTemplate;
    private final ObjectMapper objectMapper;

    public FollowService(AppUserRepo appUserRepo, FollowRepo followRepo, OutboxRepo outboxRepo, KafkaTemplate kafkaTemplate, ObjectMapper objectMapper) {
        this.appUserRepo = appUserRepo;
        this.followRepo = followRepo;
        this.outboxRepo = outboxRepo;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
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

        OutboxEvent o = new OutboxEvent();
        o.setTopicName("follow-t");
        try {
            o.setEvent(objectMapper.writeValueAsString(new FollowEvent(follower.getId(), followee.getId())));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        outboxRepo.save(o);

//        kafkaTemplate.send("follow-t", new FollowEvent(follower.getId(), followee.getId()));
    }

    public ReadFollowerListResp readFollowers(long followeeId) {

        List<ReadFollowerResp> readFollowerRespList =  followRepo.findAllByFolloweeId(followeeId);
        ReadFollowerListResp resp = new ReadFollowerListResp(readFollowerRespList);

        return resp;
    }
}
