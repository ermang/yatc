package com.eg.yatc.user.service;


import com.eg.yatc.user.entity.AppUser;
import com.eg.yatc.user.repo.AppUserRepo;
import com.eg.yatc.user.servicereq.CreateUserServiceReq;
import com.eg.yatc.user.util.Constant;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class UserCommandService {
    private final AppUserRepo appUserRepo;
    private final PasswordEncoder passwordEncoder;

    public UserCommandService(AppUserRepo appUserRepo, PasswordEncoder passwordEncoder) {
        this.appUserRepo = appUserRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public void createUser(CreateUserServiceReq createUserServiceReq) {

        AppUser appUser = new AppUser();
        appUser.setUsername(createUserServiceReq.username());
        appUser.setPassword(passwordEncoder.encode(createUserServiceReq.password()));
        appUser.setRole(Constant.ROLE_USER);

        appUserRepo.save(appUser);
    }
}
