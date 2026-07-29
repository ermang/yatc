package com.eg.yatc.core.consumer;

import com.eg.yatc.core.entity.UserProjection;
import com.eg.yatc.core.event.UserCreatedEvent;
import com.eg.yatc.core.repo.UserProjectionRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class UserCreatedEventConsumer {

    private final ObjectMapper objectMapper;
    private final UserProjectionRepo userProjectionRepo;

    public UserCreatedEventConsumer(ObjectMapper objectMapper, UserProjectionRepo userProjectionRepo) {
        this.objectMapper = objectMapper;
        this.userProjectionRepo = userProjectionRepo;
    }

    @KafkaListener(topics = "${yatc.kafka.usercreatedevent.topic}", groupId = "${yatc.kafka.usercreatedevent.group}")
    public void listen(String message, Acknowledgment ack) {

        UserCreatedEvent userCreatedEvent = null;

        try {
            userCreatedEvent = objectMapper.readValue(message, UserCreatedEvent.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);

            //TODO: something something DLT stuff
        }

        boolean alreadyExists = userProjectionRepo.existsById(userCreatedEvent.userId());

        if (alreadyExists) {
            ack.acknowledge();
            return;
        }

        UserProjection userProjection = new UserProjection();
        userProjection.setId(userCreatedEvent.userId());
        userProjection.setUsername(userCreatedEvent.username());

        userProjectionRepo.save(userProjection);
        ack.acknowledge();
    }

}
