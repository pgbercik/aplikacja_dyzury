package com.example.aplikacja_dyzury.repository;

import com.example.aplikacja_dyzury.data_model.EntryDyzurDb;
import com.example.aplikacja_dyzury.data_model.Hospital;
import com.example.aplikacja_dyzury.data_model.HospitalDepartment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface EntryDyzurDbRepo extends CrudRepository<EntryDyzurDb, Long>, JpaRepositoryImplementation<EntryDyzurDb, Long> {

    /** To wyciąga dyżury z wybranego zakresu. W ShowCalendar jest użyte przy pokazywaniu dyżurów z poszczególnych miesięcy i tygodni.
     * This request finds dutoes from a chosen range. In ShowCalendar it is used in order to show duties from a chosen week or month.
     */
    @Query(value = "SELECT * FROM dyzur  where start_time >=:previousMonth \n" +
            "AND start_time <= :nextMonth ",nativeQuery = true)
    List<EntryDyzurDb> findAllByStartTimeAndUserIdLike(LocalDateTime previousMonth,LocalDateTime nextMonth);

    /** To wyciąga dyżury z wybranego zakresu dat i danym szpitalem i oddziałem. W ShowCalendar jest użyte przy pokazywaniu dyżurów z poszczególnych miesięcy i tygodni.
     * This query gets duties from a chosen range of dates and with specific hospital and department. In ShowCalendar it is used in order to show duties from a chosen week or month.
     */
    @Query(value = "SELECT * FROM dyzur  where \n" +
            "start_time >=:previousMonth \n" +
            "AND start_time <=:nextMonth \n" +
            "AND hospital_hospital_id=:hospitalId \n" +
            "AND hospital_department_hospital_dept_id=:hospitalDeptId ",nativeQuery = true)
    List<EntryDyzurDb> findAllByStartTimeAndUserIdLike(LocalDateTime previousMonth, LocalDateTime nextMonth,Long hospitalId,Long hospitalDeptId);


    /** To wyciąga dyżury z wybranym zakresem dat i danym szpitalem. W ShowCalendar jest użyte przy pokazywaniu dyżurów z poszczególnych miesięcy i tygodni.
     * This query gets duties from a chosen range of dates and with specific hospital. In ShowCalendar it is used in order to show duties from a chosen week or month.
     */
    @Query(value = "SELECT * FROM dyzur  where \n" +
            "start_time >=:previousMonth \n" +
            "AND start_time <=:nextMonth \n" +
            "AND hospital_hospital_id=:hospitalId ",nativeQuery = true)
    List<EntryDyzurDb> findAllByStartTimeAndUserIdLike(LocalDateTime previousMonth, LocalDateTime nextMonth,Long hospitalId);



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
     * Wybiera dyżury, w których data (yyyy-MM-DD) jest równa dacie dyżuru dodawanego i szpital wraz z wydziałem się zgadza. W tym zakresie wyszukujemy wydarzeń, któe się nakładają z podanym przez nas wydarzeniem
     * Chooses a duties in which a date (yyyy-MM-DD) is equal to a date of an added duty, and hospitall and hospital dept id also match.
     * I this range of duties we search for duties which overlap with the duty chosen by us.
     * */
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
     * Znajduje dyżury zaczynające się danego dnia i mające chociaż jednego zapisanego usera.
     * It finds duties that start at the same date as the delared one and which have at least one user signed.
     * */
    @Query(value = "SELECT distinct dyzur.* FROM dyzur,dyzur_users " +
            "where dyzur.id=dyzur_users.entry_dyzur_db_id\n" +
            "AND date (start_time)=:startDate",nativeQuery = true)
    List<EntryDyzurDb> findAllWithStartDateGiven(LocalDate startDate);






}