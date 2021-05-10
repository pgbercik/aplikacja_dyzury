package com.example.aplikacja_dyzury.user.userCalendar;

import com.example.aplikacja_dyzury.DataModelAndRepo.EntryDyzurDb;
import com.example.aplikacja_dyzury.DataModelAndRepo.EntryDyzurDbRepo;
import com.example.aplikacja_dyzury.DataModelAndRepo.User;
import com.example.aplikacja_dyzury.FindUserData;
import com.vaadin.flow.component.html.H4;
import org.vaadin.stefan.fullcalendar.Entry;
import org.vaadin.stefan.fullcalendar.FullCalendar;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

public class CalendarDataProvider {

    /**
     * Generujemy dane do exportu do kalendarza Google - tutaj user wybiera z jakiego okresu importować.
     * We are generating data that will be exported to Google Calendar - here user chooses a range of duties to import.
     * */
    public List<EntryDyzurDb> getDataForGoogleCalendar(EntryDyzurDbRepo entryDyzurDbRepo, LocalDate startDate,LocalDate endDate, String mode,
                                                       Long hospitalId, Long deptID) {

        LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.MIN);
        LocalDateTime endDateTime = LocalDateTime.of(endDate,LocalTime.MAX);

        System.out.println("start date: "+startDateTime);
        System.out.println("end date: "+endDateTime);


        List<EntryDyzurDb> entriesMatching= new ArrayList<>();


        if (mode.equals("wszystkie oddziały")) entriesMatching = entryDyzurDbRepo.findAllByStartTimeAndUserIdLike(startDateTime,endDateTime);
        if (mode.equals("wybrany oddział")) entriesMatching = entryDyzurDbRepo.findAllByStartTimeAndUserIdLike(startDateTime,endDateTime,hospitalId,deptID);

        System.out.println("ZNALEZIONE WPISY Z CAŁEGO OKRESU");
            entriesMatching.forEach(entryDyzurDb -> System.out.println(entryDyzurDb.getId()+" | "+entryDyzurDb.getStartTime()+" | "+entryDyzurDb.getEndTime()));


        return entriesMatching;

    }


    /**
     * Ta metoda pobiera z bazy i wyświetla dyżury z miesiąca poprzedniego, bieżącego i przyszłego zawierające dany szpital i oddział. Następnie wyświetla je w kalendarzu.
     *
     * This method gets duties from a previous, current, and following month with specified hospital and department. After that it displays them in the calendar.
     * */
    public void addEntriesFromDBWithHospitalNameAndDept(FullCalendar calendar, EntryDyzurDbRepo entryDyzurDbRepo, LocalDate localDate, String chosenView,
                                 H4 currentlyChosenTimeSpan, Long hospitalId, Long hospitalIdDept, String email) {
        LocalDateTime localDateTimeNextMonth = null;
        LocalDateTime localDateTimePreviousMonth = null;


        if (chosenView.equals("month")) {
            localDateTimeNextMonth = LocalDateTime.of(localDate.plusMonths(1L).with(lastDayOfMonth()), LocalTime.MAX);
            localDateTimePreviousMonth = LocalDateTime.of(localDate.minusMonths(1L).with(firstDayOfMonth()), LocalTime.MIN);

            currentlyChosenTimeSpan.setText(localDate.format(DateTimeFormatter.ofPattern("  MM/yyyy",new Locale("pl","PL"))));

        }

        if (chosenView.equals("week")) {
            localDateTimeNextMonth = LocalDateTime.of(localDate.plusDays(7L), LocalTime.MAX);
            localDateTimePreviousMonth = LocalDateTime.of(localDate.minusDays(7L), LocalTime.MIN);
//            System.out.println("-----------------------------------------------");
            currentlyChosenTimeSpan.setText(localDate.format(DateTimeFormatter.ofPattern("  MM/yyyy",new Locale("pl","PL"))));
        }
        try {
            calendar.removeAllEntries();

            List<EntryDyzurDb> entriesMatching = new ArrayList<>();


            FindUserData findUserData = new FindUserData();
//            Long userId = userRepository.findByEmail(findUserData.findCurrentlyLoggedInUser()).getId();

            if (hospitalId==null || hospitalIdDept==null) {
                entriesMatching = entryDyzurDbRepo.findAllByStartTimeAndUserIdLike(localDateTimePreviousMonth,localDateTimeNextMonth);
//                System.out.println("hospitalId "+hospitalId+" | hospitalDept "+hospitalIdDept);
            }
            if (hospitalId!=null && hospitalIdDept==null) {
                entriesMatching = entryDyzurDbRepo.findAllByStartTimeAndUserIdLike(localDateTimePreviousMonth,localDateTimeNextMonth,hospitalId);
//                System.out.println("hospitalId "+hospitalId+" | hospitalDept "+hospitalIdDept);
            }
            if (hospitalId!=null && hospitalIdDept!=null) {
                entriesMatching = entryDyzurDbRepo.findAllByStartTimeAndUserIdLike(localDateTimePreviousMonth,localDateTimeNextMonth,hospitalId,hospitalIdDept);
//                System.out.println("hospitalId "+hospitalId+" | hospitalDept "+hospitalIdDept);
            }


            for (EntryDyzurDb entryDyzurDb : entriesMatching) {

                Instant start = entryDyzurDb.getStartTime().toInstant(ZoneOffset.UTC);
                Instant end = entryDyzurDb.getEndTime().toInstant(ZoneOffset.UTC);

                boolean userIsRegistered=false;
                Entry entry;
                Set<User> foundUsers = entryDyzurDb.getUsers();
                if (email.isEmpty()) email = findUserData.findCurrentlyLoggedInUser();
                String finalEmail = email;
                if (foundUsers.stream().anyMatch(user -> user.getEmail().equals(finalEmail))) userIsRegistered=true;
//                for (User user1 : foundUsers) { if (user1.getEmail().equals(email)) userIsRegistered=true; }

                if (userIsRegistered) {
                    entry = new Entry(true,entryDyzurDb.getTitle(),start,end,
                            entryDyzurDb.isAllDay(), "MediumBlue",entryDyzurDb.getDescription(),
                            entryDyzurDb.getHospital(),entryDyzurDb.getHospitalDepartment(),entryDyzurDb.getUsers());
                } else {
                    entry = new Entry(true,entryDyzurDb.getTitle(),start,end,
                            entryDyzurDb.isAllDay(), "Gray",entryDyzurDb.getDescription(),
                            entryDyzurDb.getHospital(),entryDyzurDb.getHospitalDepartment(),entryDyzurDb.getUsers());
                }


                calendar.addEntry(entry);

            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
