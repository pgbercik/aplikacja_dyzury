package com.example.aplikacja_dyzury.repository;


import com.example.aplikacja_dyzury.data_model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface UsersRepository extends JpaRepository<Users, Long> {

    @Query(value = "SELECT u from Users u LEFT JOIN FETCH u.roles WHERE u.email=:email")
    Users findByEmail(String email);

    List<Users> findByEmailAndFirstNameAndLastName(String email, String firstName, String lastName);

}