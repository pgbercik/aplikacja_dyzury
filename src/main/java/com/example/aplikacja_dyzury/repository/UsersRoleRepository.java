package com.example.aplikacja_dyzury.repository;


import com.example.aplikacja_dyzury.data_model.UsersRole;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UsersRoleRepository extends JpaRepository<UsersRole, Long> {
    UsersRole findByRole(String role);
}
