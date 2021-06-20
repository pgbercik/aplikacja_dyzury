package com.example.aplikacja_dyzury.repository;

import com.example.aplikacja_dyzury.data_model.DoctorTitle;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DoctorTitleRepo extends CrudRepository<DoctorTitle,Long> {


    @Query("SELECT d.type FROM DoctorTitle d where d.id=?1")
    String findDoctorTypeById(Long doctorType);

    List<DoctorTitle> findAll();

    @Query("SELECT d.id FROM DoctorTitle d")
    List<String> findDoctorIdForCombobox();

    @Query(value = "SELECT * FROM doctor_title",nativeQuery = true)
    List<DoctorTitle> findTitleROW();

}
