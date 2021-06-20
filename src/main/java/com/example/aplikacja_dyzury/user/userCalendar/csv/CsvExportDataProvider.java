package com.example.aplikacja_dyzury.user.userCalendar.csv;

import com.example.aplikacja_dyzury.data_model.EntryDyzurDb;
import com.example.aplikacja_dyzury.repository.EntryDyzurDbRepo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class CsvExportDataProvider {

    private final EntryDyzurDbRepo entryDyzurDbRepo;

    public CsvExportDataProvider(EntryDyzurDbRepo entryDyzurDbRepo) {
        this.entryDyzurDbRepo = entryDyzurDbRepo;
    }

    /**
     * Generujemy dane do exportu do kalendarza Google - tutaj user wybiera z jakiego okresu importować.
     * We are generating data that will be exported to Google Calendar - here user chooses a range of duties to import.
     */
    public List<EntryDyzurDb> getDataForGoogleCalendar(LocalDate startDate, LocalDate endDate, String mode,
                                                       Long hospitalId, Long deptID) {

        LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.MIN);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.MAX);

        System.out.println("start date: " + startDateTime);
        System.out.println("end date: " + endDateTime);


        List<EntryDyzurDb> entriesMatching = new ArrayList<>();


        if (mode.equals("wszystkie oddziały"))
            entriesMatching = entryDyzurDbRepo.findAllByStartTimeAndUserIdLike(startDateTime, endDateTime);
        if (mode.equals("wybrany oddział"))
            entriesMatching = entryDyzurDbRepo.findAllByStartTimeAndUserIdLike(startDateTime, endDateTime, hospitalId, deptID);

//        System.out.println("ZNALEZIONE WPISY Z CAŁEGO OKRESU");
//            entriesMatching.forEach(entryDyzurDb -> System.out.println(entryDyzurDb.getId()+" | "+entryDyzurDb.getStartTime()+" | "+entryDyzurDb.getEndTime()));


        return entriesMatching;

    }
}
