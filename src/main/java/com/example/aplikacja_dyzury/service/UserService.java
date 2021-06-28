package com.example.aplikacja_dyzury.service;

import com.example.aplikacja_dyzury.data_model.Users;
import com.example.aplikacja_dyzury.data_model.UsersRole;
import com.example.aplikacja_dyzury.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.aplikacja_dyzury.repository.UsersRoleRepository;

import java.util.Set;


@Service
public class UserService {

    private static final String DEFAULT_ROLE = "ROLE_USER";
    private UsersRepository usersRepository;
    private UsersRoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setUserRepository(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Autowired
    public void setRoleRepository(UsersRoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public void addWithDefaultRole(Users users) {
        UsersRole defaultRole = roleRepository.findByRole(DEFAULT_ROLE);
//        users.getRoles().add(defaultRole);
        users.setRoles(Set.of(defaultRole));
        String passwordHash = passwordEncoder.encode(users.getPassword());
        users.setPassword(passwordHash);
        usersRepository.save(users);
    }
}