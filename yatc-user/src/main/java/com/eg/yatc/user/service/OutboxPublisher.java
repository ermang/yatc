package com.eg.yatc.user.service;

import com.eg.yatc.user.constant.OutboxStatus;
import com.eg.yatc.user.entity.UserOutbox;
import com.eg.yatc.user.repo.UserOutboxRepo;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OutboxPublisher {

    private final UserOutboxRepo userOutboxRepo;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public OutboxPublisher(UserOutboxRepo userOutboxRepo, KafkaTemplate<String, String> kafkaTemplate) {
        this.userOutboxRepo = userOutboxRepo;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Scheduled(fixedDelay = 3000)
    public void publishPendingEvents() {
        List<UserOutbox> events = userOutboxRepo.findTop50ByStatusNotAndRetryCountLessThanOrderByIdAsc(OutboxStatus.SUCCESS, 4);

        for(UserOutbox event : events) {
            try {
                kafkaTemplate.send(event.getTopicName(), event.getEvent()).get();

                event.setStatus(OutboxStatus.SUCCESS);
                //event.setPublishedAt(Instant.now());
                userOutboxRepo.save(event);
            } catch (Exception ex) {
                event.setRetryCount(event.getRetryCount() + 1);
                userOutboxRepo.save(event);
            }
        }
    }

}
