package com.example.aplikacja_dyzury.repository;

import com.example.aplikacja_dyzury.data_model.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestStatusRepo extends JpaRepository<RequestStatus,Long> {
}
