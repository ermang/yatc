package com.eg.yatc.user.service;

import com.eg.yatc.user.constant.OutboxStatus;
import com.eg.yatc.user.entity.OutboxEvent;
import com.eg.yatc.user.repo.OutboxRepo;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OutboxPublisher {

    private final OutboxRepo outboxRepo;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public OutboxPublisher(OutboxRepo outboxRepo, KafkaTemplate<String, String> kafkaTemplate) {
        this.outboxRepo = outboxRepo;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Scheduled(fixedDelay = 3000)
    public void publishPendingEvents() {
        List<OutboxEvent> events = outboxRepo.findTop50ByStatusNotAndRetryCountLessThanOrderByIdAsc(OutboxStatus.SUCCESS, 4);

        for(OutboxEvent event : events) {
            try {
                kafkaTemplate.send(event.getTopicName(), event.getEvent()).get();

                event.setStatus(OutboxStatus.SUCCESS);
                //event.setPublishedAt(Instant.now());
                outboxRepo.save(event);
            } catch (Exception ex) {
                event.setRetryCount(event.getRetryCount() + 1);
                outboxRepo.save(event);
            }
        }
    }

}
