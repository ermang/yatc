package com.eg.yatc.auth.controller;



import com.eg.yatc.auth.req.CreateUserReq;
import com.eg.yatc.auth.req.LoginReq;
import com.eg.yatc.auth.service.UserCommandService;
import com.eg.yatc.auth.servicereq.CreateUserServiceReq;
import com.eg.yatc.auth.util.CustomUserDetails;
import com.eg.yatc.auth.util.CustomUserDetailsService;
import com.eg.yatc.auth.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {


    private final UserCommandService userCommandService;


    private final AuthenticationManager authManager;
    private final JwtUtil jwtService;
    private final CustomUserDetailsService userDetailsService;
    //private final Req2ServiceReq req2ServiceReq;

    public AuthController(UserCommandService userCommandService, AuthenticationManager authManager, JwtUtil jwtService,
                          CustomUserDetailsService userDetailsService) {
        this.userCommandService = userCommandService;
        this.authManager = authManager;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        //this.req2ServiceReq = req2ServiceReq;
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginReq request) {
        System.out.println("RAW PASSWORD = [" + request.password + "]");
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username, request.password));

        CustomUserDetails customUserDetails =
                (CustomUserDetails) auth.getPrincipal();

        return jwtService.generateToken(customUserDetails);
    }
    @PostMapping
    public void createUser(@RequestBody @Valid CreateUserReq createUserReq) {
        CreateUserServiceReq serviceReq = new CreateUserServiceReq(createUserReq.username, createUserReq.password);
        userCommandService.createUser(serviceReq);
    }


}
