package com.eg.yatc.auth.service;


import com.eg.yatc.auth.constant.OutboxStatus;
import com.eg.yatc.auth.entity.AuthOutbox;
import com.eg.yatc.auth.repo.AuthOutboxRepo;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OutboxPublisher {

    private final AuthOutboxRepo authOutboxRepo;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public OutboxPublisher(AuthOutboxRepo authOutboxRepo, KafkaTemplate<String, String> kafkaTemplate) {
        this.authOutboxRepo = authOutboxRepo;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Scheduled(fixedDelay = 3000)
    public void publishPendingEvents() {
        List<AuthOutbox> events = authOutboxRepo.findTop50ByStatusNotAndRetryCountLessThanOrderByIdAsc(OutboxStatus.SUCCESS, 4);

        for(AuthOutbox event : events) {
            try {
                kafkaTemplate.send(event.getTopicName(), event.getEvent()).get();

                event.setStatus(OutboxStatus.SUCCESS);
                //event.setPublishedAt(Instant.now());
                authOutboxRepo.save(event);
            } catch (Exception ex) {
                event.setRetryCount(event.getRetryCount() + 1);
                authOutboxRepo.save(event);
            }
        }
    }

}
