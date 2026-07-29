package com.eg.yatc.core.consumer;

import com.eg.yatc.core.entity.FollowProjection;
import com.eg.yatc.core.entity.UserProjection;
import com.eg.yatc.core.event.FollowEvent;
import com.eg.yatc.core.repo.FollowProjectionRepo;
import com.eg.yatc.core.repo.UserProjectionRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class FollowEventConsumer {

    private final ObjectMapper objectMapper;
    private final UserProjectionRepo userProjectionRepo;
    private final FollowProjectionRepo followProjectionRepo;

    public FollowEventConsumer(ObjectMapper objectMapper, UserProjectionRepo userProjectionRepo, FollowProjectionRepo followProjectionRepo) {
        this.objectMapper = objectMapper;
        this.userProjectionRepo = userProjectionRepo;
        this.followProjectionRepo = followProjectionRepo;
    }

    @KafkaListener(topics = "${yatc.kafka.followevent.topic}", groupId = "${yatc.kafka.followevent.group}")
    public void listen(String message, Acknowledgment ack) {

        FollowEvent followEvent = null;

        try {
            followEvent = objectMapper.readValue(message, FollowEvent.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);

            //TODO: something something DLT stuff
        }

        boolean alreadyExists = followProjectionRepo.existsByFollowerIdAndFolloweeId(followEvent.followerId(), followEvent.followeeId());

        if (alreadyExists) {
            ack.acknowledge();
            return;
        }

        FollowProjection followProjection = new FollowProjection();
        followProjection.setFollowerId(followEvent.followerId());
        followProjection.setFolloweeId(followEvent.followeeId());

        followProjectionRepo.save(followProjection);
        ack.acknowledge();
    }

}
