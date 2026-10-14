package com.eg.yatc.auth.util;



import com.eg.yatc.auth.entity.AppIdentity;
import com.eg.yatc.auth.repo.AppIdentityRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AppIdentityRepo appIdentityRepo;

    public CustomUserDetailsService(AppIdentityRepo appIdentityRepo) {
        this.appIdentityRepo = appIdentityRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        AppIdentity appIdentity = appIdentityRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        System.out.println("DB PASSWORD = [" + appIdentity.getPassword() + "]");
        System.out.println("LENGTH = " + appIdentity.getPassword().length());



        return new CustomUserDetails(appIdentity);
    }
}