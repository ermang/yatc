package com.eg.yatc.auth.service;



import com.eg.yatc.auth.entity.AppIdentity;
import com.eg.yatc.auth.entity.AuthOutbox;
import com.eg.yatc.auth.event.UserCreatedEvent;
import com.eg.yatc.auth.repo.AppIdentityRepo;
import com.eg.yatc.auth.repo.AuthOutboxRepo;
import com.eg.yatc.auth.servicereq.CreateUserServiceReq;
import com.eg.yatc.auth.util.Constant;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class UserCommandService {
    private final AppIdentityRepo appIdentityRepo;
    private final AuthOutboxRepo authOutboxRepo;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;

    public UserCommandService(AppIdentityRepo appIdentityRepo, AuthOutboxRepo authOutboxRepo, PasswordEncoder passwordEncoder, ObjectMapper objectMapper) {
        this.appIdentityRepo = appIdentityRepo;
        this.authOutboxRepo = authOutboxRepo;
        this.passwordEncoder = passwordEncoder;
        this.objectMapper = objectMapper;
    }

    public void createUser(CreateUserServiceReq createUserServiceReq) {

        AppIdentity appIdentity = new AppIdentity();
        appIdentity.setUsername(createUserServiceReq.username());
        appIdentity.setPassword(passwordEncoder.encode(createUserServiceReq.password()));
        appIdentity.setRole(Constant.ROLE_USER);

        appIdentity = appIdentityRepo.save(appIdentity);

        UserCreatedEvent uce = new UserCreatedEvent(appIdentity.getId(), appIdentity.getUsername());

        String uceAsString = null;
        try {
            uceAsString = objectMapper.writeValueAsString(uce);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        AuthOutbox oe = new AuthOutbox();
        oe.setTopicName("user-created-t");
        oe.setEvent(uceAsString);
        authOutboxRepo.save(oe);
    }
}
