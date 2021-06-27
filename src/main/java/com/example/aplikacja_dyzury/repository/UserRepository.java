package com.example.aplikacja_dyzury.repository;


import com.example.aplikacja_dyzury.data_model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    List<User> findByEmailAndFirstNameAndLastName(String email, String firstName, String lastName);

}