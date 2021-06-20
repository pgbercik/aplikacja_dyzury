package com.example.aplikacja_dyzury.repository;


import com.example.aplikacja_dyzury.data_model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    UserRole findByRole(String role);
}
