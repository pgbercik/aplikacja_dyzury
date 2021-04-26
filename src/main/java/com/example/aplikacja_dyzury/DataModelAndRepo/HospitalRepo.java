package com.example.aplikacja_dyzury.DataModelAndRepo;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface HospitalRepo  extends PagingAndSortingRepository<Hospital,Long> {
    List<Hospital> findAll();

    /**
     * Ta metoda  sprawdza czy istnieje szpital o podanej nazwie, mieście i adresie - to jest używane przy dodawaniu szpitali w celu uniknięcia duplikatów.
     * */
    @Query(value = "SELECT * FROM hospital \n" +
            "where address=:address \n" +
            "AND city=:city \n" +
            "AND name=:name ",nativeQuery = true)
    List<Hospital> findExistingHospitals(String address, String city, String name);

    List<Hospital> findAllByCity(String city, Pageable pageable);
}
