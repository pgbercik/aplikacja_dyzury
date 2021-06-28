package com.example.aplikacja_dyzury.security;


import java.util.HashSet;
import java.util.Set;

import com.example.aplikacja_dyzury.data_model.Users;
import com.example.aplikacja_dyzury.data_model.UsersRole;
import com.example.aplikacja_dyzury.repository.UsersRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.example.aplikacja_dyzury.repository.UsersRepository;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    private UsersRepository usersRepository;
//    private UsersRoleRepository usersRoleRepository;

    @Autowired
    public void setUserRepository(UsersRepository usersRepository/*, UsersRoleRepository usersRoleRepository*/) {
        this.usersRepository = usersRepository;
//        this.usersRoleRepository = usersRoleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users users = usersRepository.findByEmail(email);

        if(users == null)
            throw new UsernameNotFoundException("Users not found");
        else {

            return new org.springframework.security.core.userdetails.User(
                    users.getEmail(),
                    users.getPassword(),
                    convertAuthorities(users.getRoles()));
        }
    }

    private Set<GrantedAuthority> convertAuthorities(Set<UsersRole> usersRoles) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        for(UsersRole ur: usersRoles) {
            authorities.add(new SimpleGrantedAuthority(ur.getRole()));
        }

        return authorities;
    }
}