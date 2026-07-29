package com.eg.yatc.user.service;


import com.eg.yatc.user.entity.AppUser;
import com.eg.yatc.user.entity.OutboxEvent;
import com.eg.yatc.user.event.UserCreatedEvent;
import com.eg.yatc.user.repo.AppUserRepo;
import com.eg.yatc.user.repo.OutboxRepo;
import com.eg.yatc.user.servicereq.CreateUserServiceReq;
import com.eg.yatc.user.util.Constant;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class UserCommandService {
    private final AppUserRepo appUserRepo;
    private final OutboxRepo outboxRepo;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;

    public UserCommandService(AppUserRepo appUserRepo, OutboxRepo outboxRepo, PasswordEncoder passwordEncoder, ObjectMapper objectMapper) {
        this.appUserRepo = appUserRepo;
        this.outboxRepo = outboxRepo;
        this.passwordEncoder = passwordEncoder;
        this.objectMapper = objectMapper;
    }

    public void createUser(CreateUserServiceReq createUserServiceReq) {

        AppUser appUser = new AppUser();
        appUser.setUsername(createUserServiceReq.username());
        appUser.setPassword(passwordEncoder.encode(createUserServiceReq.password()));
        appUser.setRole(Constant.ROLE_USER);

        appUser = appUserRepo.save(appUser);

        UserCreatedEvent uce = new UserCreatedEvent(appUser.getId(), appUser.getUsername());

        String uceAsString = null;
        try {
            uceAsString = objectMapper.writeValueAsString(uce);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        OutboxEvent oe = new OutboxEvent();
        oe.setTopicName("user-created-t");
        oe.setEvent(uceAsString);
        outboxRepo.save(oe);
    }
}
