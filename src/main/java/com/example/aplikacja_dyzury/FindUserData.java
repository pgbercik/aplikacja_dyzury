package com.example.aplikacja_dyzury;


import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FindUserData {

    /**
     * Wynikowo znajduje adres email danego użytkownika. Ten adres jest unikalny dla każdego usera.
     * We're finding an email address of a declared user. Email is unique fore every user.*/
    public String findCurrentlyLoggedInUser() {
        String username="";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            username= currentUserName;
        }
        return username;
    }

    public Collection<? extends GrantedAuthority> findUserRoles() {
        Collection<? extends GrantedAuthority> grantedAuthorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        grantedAuthorities.forEach(System.out::println);
        return grantedAuthorities;

    }

    public String findFirstUserRoleString() {
        List<String> userRoleList = new ArrayList<>();
        Collection<? extends GrantedAuthority> grantedAuthorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        grantedAuthorities.forEach(o -> userRoleList.add(o.toString()));


        return userRoleList.get(0);

    }
}
