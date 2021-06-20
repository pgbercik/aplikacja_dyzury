package com.example.aplikacja_dyzury.repository;

import com.example.aplikacja_dyzury.data_model.HospitalDepartment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface HospitalDepartmentRepo extends CrudRepository<HospitalDepartment,Long> {
    List<HospitalDepartment> findAll();

    @Query(value = "SELECT * FROM hospital_department\n" +
            "WHERE referenced_hospital_id=:hospitalId",nativeQuery = true)
    List<HospitalDepartment> findDepartmentByHospitalId(Long hospitalId);

    @Query(value = "SELECT * FROM hospital_department " +
            "where department=:department " +
            "AND referenced_hospital_id=:hospitalId ;",nativeQuery = true)
    List<HospitalDepartment> findAlreadyExistingDepartment(String department, Long hospitalId);
}
