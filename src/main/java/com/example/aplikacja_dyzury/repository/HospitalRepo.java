package com.example.aplikacja_dyzury.repository;

import com.example.aplikacja_dyzury.data_model.Hospital;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface HospitalRepo  extends PagingAndSortingRepository<Hospital,Long> {
    List<Hospital> findAll();

    /**
     * Ta metoda  sprawdza czy istnieje szpital o podanej nazwie, mieście i adresie - to jest używane przy dodawaniu szpitali w celu uniknięcia duplikatów.
     * This method check whether a database already contains a hospital with declared address, city and name - it is used in order to prevent adding duplicate hospitals.
     * */
        @Query(value = "SELECT h from Hospital h " +
                "WHERE h.address=:address" +
                " AND h.city=:city" +
                " AND h.name=:name")
    List<Hospital> findExistingHospitals(String address, String city, String name);

}
