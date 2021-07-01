package com.example.aplikacja_dyzury.repository;

import com.example.aplikacja_dyzury.data_model.Requests;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RequestsRepo extends JpaRepository<Requests,Long> {

//    @Query(value = "select * from requests where is_active=:isActive \n" +
//            "AND user_target_id=:targetId ORDER BY request_time DESC",
//            countQuery = "select count(*) from requests where is_active=:isActive \n" +
//            "AND user_target_id=:targetId ORDER BY request_time DESC",
//            nativeQuery = true)
@Query(value = "select r from Requests r where r.isActive=:isActive \n" +
        "AND r.userTarget.id=:targetId ORDER BY r.requestTime DESC",
        countQuery = "select count(r) from Requests r where r.isActive=:isActive \n" +
                "AND r.userTarget.id=:targetId ORDER BY r.requestTime DESC")
    Page<Requests> findAllReceived(Boolean isActive,Long targetId,Pageable pageable);

//    @Query(value = "select * from requests where  user_init_id=:initialId ORDER BY request_time DESC",
//            countQuery = "select count(*) from requests where  user_init_id=:initialId ORDER BY request_time DESC"
//            ,nativeQuery = true)
@Query(value = "select r from Requests r where  r.userInit.id=:initialId ORDER BY r.requestTime DESC",
        countQuery = "select count(r) from Requests r where  r.userInit.id=:initialId ORDER BY r.requestTime DESC"
        )
    Page<Requests> findAllSent(Long initialId, Pageable pageable);

//    @Query(value = "select * from requests where request_id=:id",nativeQuery = true)
    @Query(value = "select r from Requests r where r.requestId=:id")
    Requests findWithId(Long id);

//    @Query(value = "SELECT * FROM requests where entry_initial_id=:entryId \n" +
//            "OR entry_target_id=:entryId ",nativeQuery = true)
@Query(value = "SELECT r FROM Requests r where r.entryInitial =:entryId \n" +
        "OR r.entryTarget=:entryId ")
    List<Requests> findRequestsForEntry(String entryId);




}
