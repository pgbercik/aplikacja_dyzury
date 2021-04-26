package com.example.aplikacja_dyzury.DataModelAndRepo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RequestStatusRepo extends CrudRepository<RequestStatus,Long> {
    List<RequestStatus> findAll();

//    @Query(value = "SELECT * FROM dyzury.request_status where id =:id;",nativeQuery = true)

}
