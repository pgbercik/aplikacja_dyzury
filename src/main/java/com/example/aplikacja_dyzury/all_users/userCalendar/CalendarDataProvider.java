package com.example.aplikacja_dyzury.all_users.userCalendar;

import com.example.aplikacja_dyzury.data_model.Users;
import com.example.aplikacja_dyzury.data_model.EntryDyzurDb;
import com.example.aplikacja_dyzury.repository.EntryDyzurDbRepo;
import com.example.aplikacja_dyzury.FindUserData;
import com.vaadin.flow.component.html.Label;
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

    private final EntryDyzurDbRepo entryDyzurDbRepo;

    public CalendarDataProvider(EntryDyzurDbRepo entryDyzurDbRepo) {
        this.entryDyzurDbRepo = entryDyzurDbRepo;
    }




    /**
     * Ta metoda pobiera z bazy i wyświetla dyżury z miesiąca poprzedniego, bieżącego i przyszłego zawierające dany szpital i oddział. Następnie wyświetla je w kalendarzu.
     * <p>
     * This method gets duties from a previous, current, and following month with specified hospital and department. After that it displays them in the calendar.
     */
    public void addEntriesFromDBWithHospitalNameAndDept(FullCalendar calendar, LocalDate localDate, String chosenView,
                                                        Label currentlyChosenTimeSpan, Long hospitalId, Long hospitalIdDept, String email) {

//        LocalDateTime nextTimeSpan = null;
        LocalDateTime previousTimeSpan = getPreviousTimeFrame(chosenView, localDate);

        //getting next week or next month time frame to query the DB
        LocalDateTime nextTimeSpan = getNextTimeFrame(chosenView, localDate);

        //set currently chosen time span which is shown in calendar widget
        currentlyChosenTimeSpan.setText(createCurrentTimeSpanText(localDate));

        try {
            //clean remove all entries from calendar widget
            calendar.removeAllEntries();

            //entries from DB matching hospital, hospitalDept and timestamps
            List<EntryDyzurDb> entriesMatching = getEntriesMatching(hospitalId, hospitalIdDept,
                    nextTimeSpan, previousTimeSpan);

            calendar.addEntries(prepareEntriesToAddToCalendar(entriesMatching, email));

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    /**
     * Konwertujemy dyżury do takiego formatu jakiego wymaga widget kalendarza. Ustawiamy kolory itd.
     * We convert duties to a format required by calendar widget. We  set colors, etc.
     */
    private List<Entry> prepareEntriesToAddToCalendar(List<EntryDyzurDb> entriesMatching, String email) {
        List<Entry> entryList = new ArrayList<>();
        for (EntryDyzurDb entryDyzurDb : entriesMatching) {

            Instant start = entryDyzurDb.getStartTime().toInstant(ZoneOffset.UTC);
            Instant end = entryDyzurDb.getEndTime().toInstant(ZoneOffset.UTC);

            boolean userIsRegistered = false;
            Entry entry;
            Set<Users> foundUsers = entryDyzurDb.getUsers();
            if (email.isEmpty()) email = FindUserData.findCurrentlyLoggedInUser();
            String finalEmail = email;
            if (foundUsers.stream().anyMatch(user -> user.getEmail().equals(finalEmail))) userIsRegistered = true;

            if (userIsRegistered) {
                entry = new Entry(true, entryDyzurDb.getTitle(), start, end,
                        entryDyzurDb.isAllDay(), "MediumBlue", entryDyzurDb.getDescription(),
                        entryDyzurDb.getHospital(), entryDyzurDb.getHospitalDepartment(), entryDyzurDb.getUsers());
            } else {
                entry = new Entry(true, entryDyzurDb.getTitle(), start, end,
                        entryDyzurDb.isAllDay(), "Gray", entryDyzurDb.getDescription(),
                        entryDyzurDb.getHospital(), entryDyzurDb.getHospitalDepartment(), entryDyzurDb.getUsers());
            }

            entryList.add(entry);

        }
        return entryList;
    }

    /**
     * Znajdujemy dyżury w zależności czy podano szpital, oddział, któreś z nich czy żadne z nich.
     * <p>
     * We're fetching duties based on whether hospital and dept were provided.
     */
    private List<EntryDyzurDb> getEntriesMatching(Long hospitalId, Long hospitalIdDept, LocalDateTime nextTimeSpan,
                                                  LocalDateTime previousTimeSpan) {
        List<EntryDyzurDb> entriesMatching = new ArrayList<>();

        if (hospitalId == null || hospitalIdDept == null) {
            entriesMatching = entryDyzurDbRepo.findAllByStartTimeAndUserIdLike(previousTimeSpan, nextTimeSpan);
        }
        if (hospitalId != null && hospitalIdDept == null) {
            entriesMatching = entryDyzurDbRepo.findAllByStartTimeAndUserIdLike(previousTimeSpan, nextTimeSpan, hospitalId);
        }
        if (hospitalId != null && hospitalIdDept != null) {
            entriesMatching = entryDyzurDbRepo.findAllByStartTimeAndUserIdLike(previousTimeSpan, nextTimeSpan, hospitalId, hospitalIdDept);
        }
        return entriesMatching;
    }

    /**
     * Finding following time frame. It is needed when querying DB for entries from current time frame,
     * because the calendar view sometimes shows a few days from previous and following month.
     */
    private LocalDateTime getNextTimeFrame(String chosenView, LocalDate localDate) {
        LocalDateTime nextTimeSpan = null;
        switch (chosenView) {
            case "month":
                nextTimeSpan = LocalDateTime.of(localDate.plusMonths(1L).with(lastDayOfMonth()), LocalTime.MAX);
                break;
            case "week":
                nextTimeSpan = LocalDateTime.of(localDate.plusDays(7L), LocalTime.MAX);
                break;
        }

        return nextTimeSpan;
    }

    /**
     * Finding previous time frame. It is needed when querying DB for entries from current time frame,
     * because the calendar view sometimes shows a few days from previous and following month.
     */
    private LocalDateTime getPreviousTimeFrame(String chosenView, LocalDate localDate) {
        LocalDateTime previousTimeSpan = null;
        switch (chosenView) {
            case "month":
                previousTimeSpan = LocalDateTime.of(localDate.minusMonths(1L).with(firstDayOfMonth()), LocalTime.MIN);
                break;
            case "week":
                previousTimeSpan = LocalDateTime.of(localDate.minusDays(7L), LocalTime.MIN);
                break;
        }
        return previousTimeSpan;
    }

    /**
     * Text that shows currently chosen month and year MM/yyyy.
     */
    private String createCurrentTimeSpanText(LocalDate localDate) {
        return
                localDate.format(DateTimeFormatter.ofPattern("  MM/yyyy", new Locale("pl", "PL")));
    }
}
