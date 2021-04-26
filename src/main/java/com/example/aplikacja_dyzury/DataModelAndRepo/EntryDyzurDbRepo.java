package com.example.aplikacja_dyzury.DataModelAndRepo;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.CrudRepository;
import org.vaadin.stefan.fullcalendar.Entry;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface EntryDyzurDbRepo extends CrudRepository<EntryDyzurDb, Long>, JpaRepositoryImplementation<EntryDyzurDb, Long> {
    List<EntryDyzurDb> findAll();

    /** To wyciąga dyżury z wybranego zakresu. W ShowCalendar jest użyte przy pokazywaniu dyżurów z poszczególnych miesięcy i tygodni.
     */
    @Query(value = "SELECT * FROM dyzur  where start_time >=:previousMonth \n" +
            "AND start_time <= :nextMonth ",nativeQuery = true)
    List<EntryDyzurDb> findAllByStartTimeAndUserIdLike(LocalDateTime previousMonth,LocalDateTime nextMonth);

    /** To wyciąga dyżury z wybranego zakresu dat i danym szpitalem i oddziałem. W ShowCalendar jest użyte przy pokazywaniu dyżurów z poszczególnych miesięcy i tygodni.
     */
    @Query(value = "SELECT * FROM dyzur  where \n" +
            "start_time >=:previousMonth \n" +
            "AND start_time <=:nextMonth \n" +
            "AND hospital_hospital_id=:hospitalId \n" +
            "AND hospital_department_hospital_dept_id=:hospitalDeptId ",nativeQuery = true)
    List<EntryDyzurDb> findAllByStartTimeAndUserIdLike(LocalDateTime previousMonth, LocalDateTime nextMonth,Long hospitalId,Long hospitalDeptId);


    /** To wyciąga dyżury z wybranym zakresem dat i danym szpitalem. W ShowCalendar jest użyte przy pokazywaniu dyżurów z poszczególnych miesięcy i tygodni.
     */
    @Query(value = "SELECT * FROM dyzur  where \n" +
            "start_time >=:previousMonth \n" +
            "AND start_time <=:nextMonth \n" +
            "AND hospital_hospital_id=:hospitalId ",nativeQuery = true)
    List<EntryDyzurDb> findAllByStartTimeAndUserIdLike(LocalDateTime previousMonth, LocalDateTime nextMonth,Long hospitalId);



//    /**
//     * to będzie użyte do eksportu wydarzeń do csv - to ma wyciągnąć wydarzenia od początku aktualnego dnia do końca następnego miesiąca.*/
//
//    @Query(value = "SELECT * FROM dyzury.dyzur  where \n" +
//            "start_time >=:mindightOfPreviousDay \n" +
//            "AND start_time <=:lastDayOfFollowingMonth \n" ,nativeQuery = true)
//    List<EntryDyzurDb> findAllFutureEntriesForGoogleCalendar(LocalDateTime mindightOfPreviousDay, LocalDateTime lastDayOfFollowingMonth);


//    /** To wyciąga dyżury importowane do kalendarza Google.
//     */
//    @Query(value = "SELECT * FROM dyzury.dyzur  where \n" +
//            "start_time >=:previousMonth \n" +
//            "AND start_time <=:nextMonth \n" +
//            "AND hospital_hospital_id=:hospitalId \n" +
//            "AND hospital_department_hospital_dept_id=:hospitalDeptId ",nativeQuery = true)
//    List<EntryDyzurDb> findEntriesForGoogleCalendar(LocalDateTime previousMonth, LocalDateTime nextMonth,Long hospitalId,Long hospitalDeptId);

    @Query("SELECT e.id FROM EntryDyzurDb e WHERE e.description=?1 AND e.startTime=?2 AND e.endTime=?3 AND e.hospital=?4 AND title=?5")
    String findEntryToEdit(String description, LocalDateTime startTime, LocalDateTime endTime, Hospital hospital, String title);


    @Query(value = "SELECT * FROM dyzur WHERE id=:id",nativeQuery = true)
    EntryDyzurDb findByID(String id);

    @Query(value = "SELECT * FROM dyzur  where start_time=:startTime \n" +
            "AND end_name=:endTime\n" +
            "AND hospital_hospital_id=:hospital\n" +
            "AND hospital_department_hospital_dept_id=:hospitalDepartment",nativeQuery = true)
    List<EntryDyzurDb> findAllMatchingStartEndHospitalHospitalDepartment(LocalDateTime startTime, LocalDateTime endTime,
                                                                         Hospital hospital, HospitalDepartment hospitalDepartment);


    /**
     * Wybiera dyżury, w których data (YYYY-MM-DD) jest równa dacie dyżuru dodawanego i szpital wraz z wydziałem się zgadza. W tym zakresie wyszukujemy wydarzeń, któe się nakładają z podanym przez nas wydarzeniem*/
    @Query(value = "select * from dyzur where \n" +
            "(DATE(start_time) =:localDate " +
            "OR DATE(end_name) =:localDate ) \n" +
            "AND hospital_hospital_id=:hospital \n" +
            "AND hospital_department_hospital_dept_id=:hospitalDepartment " +
            "AND start_time!=:startDateTimeFromEntry \n" +
            "AND end_name!=:endDateTimeFromEntry ",nativeQuery = true)
    List<EntryDyzurDb> findEntriesForOverlappingCheck(LocalDate localDate, Hospital hospital, HospitalDepartment hospitalDepartment,
                                                      LocalDateTime startDateTimeFromEntry, LocalDateTime endDateTimeFromEntry);

    /**
     * znajduje dyżury zaczynające się danego dnia i mające chociaż jednego zapisanego usera*/
    @Query(value = "SELECT distinct dyzur.* FROM dyzur,dyzur_users " +
            "where dyzur.id=dyzur_users.entry_dyzur_db_id\n" +
            "AND date (start_time)=:startDate",nativeQuery = true)
    List<EntryDyzurDb> findAllWithStartDateGiven(LocalDate startDate);






}