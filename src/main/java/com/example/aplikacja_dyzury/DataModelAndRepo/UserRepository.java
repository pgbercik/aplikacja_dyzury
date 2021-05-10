package com.example.aplikacja_dyzury.DataModelAndRepo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    @Query(value = "select * from user_table where email=:email AND first_name=:firstName AND last_name=:lastName ",
            nativeQuery = true)
    List<User> findAlreadyExistingUsers(String email, String firstName, String lastName);

}