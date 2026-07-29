package com.eg.yatc.user.util;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ActiveUserResolver {

    public CustomUserDetails getActiveUser() {
        CustomUserDetails userDetails =  (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //if (userDetails instanceof C)
        //CustomPrincipal customPrincipal =  (CustomPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

       // return customPrincipal;

        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof CustomPrincipal)
            System.out.println("*****CUSTOMPRINCIPAL*****");
        else
            System.out.println("*****NO_CUSTOM*****");

        return (CustomUserDetails)userDetails;
    }

    public Long getUserId() {

        Long userId = getActiveUser().getUserId();

        return userId;
    }
}
