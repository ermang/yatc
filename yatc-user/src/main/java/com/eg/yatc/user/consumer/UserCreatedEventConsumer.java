package com.eg.yatc.user.consumer;

import com.eg.yatc.user.entity.AppUser;
import com.eg.yatc.user.event.UserCreatedEvent;
import com.eg.yatc.user.repo.AppUserRepo;
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
    private final AppUserRepo appUserRepo;

    public UserCreatedEventConsumer(ObjectMapper objectMapper, AppUserRepo appUserRepo) {
        this.objectMapper = objectMapper;
        this.appUserRepo = appUserRepo;
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

        boolean alreadyExists = appUserRepo.existsById(userCreatedEvent.userId());

        if (alreadyExists) {
            ack.acknowledge();
            return;
        }

        AppUser appUser = new AppUser();
        appUser.setId(userCreatedEvent.userId());
        appUser.setUsername(userCreatedEvent.username());

        appUserRepo.save(appUser);
        ack.acknowledge();
    }

}
