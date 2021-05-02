package com.example.aplikacja_dyzury.user.userCalendar;


import com.example.aplikacja_dyzury.DataModelAndRepo.EntryDyzurDbRepo;
import com.example.aplikacja_dyzury.DataModelAndRepo.UserRepository;
import com.vaadin.flow.component.UI;
import org.vaadin.stefan.fullcalendar.FullCalendar;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Klasa z wątkiem, któy w tle aktualizuje dyżury w kalendarzu. Dzięki temu jeśli inny user doda dyżur to pozostali będą go widzieli bez konieczności odświeżania strony.
 */

public class BackgroundCalendarUpdaterThread extends Thread {
    private final UI ui;
    private final ShowCalendar showCalendar;
    private CalendarDataProvider calendarDataProvider;
    private FullCalendar calendar;
    private EntryDyzurDbRepo entryDyzurDbRepo;
    private UserRepository userRepository;
    private String email;


    public BackgroundCalendarUpdaterThread(UI ui, ShowCalendar showCalendar, CalendarDataProvider calendarDataProvider, FullCalendar calendar, EntryDyzurDbRepo entryDyzurDbRepo, UserRepository userRepository, String email) {
        this.ui = ui;
        this.showCalendar = showCalendar;
        this.calendarDataProvider = calendarDataProvider;
        this.calendar = calendar;
        this.entryDyzurDbRepo = entryDyzurDbRepo;
        this.userRepository = userRepository;
        this.email = email;
    }

    @Override
    public void run() {
        try {
            while (true) {
                // co sekundę coś robimy
                Thread.sleep(1000);

                ui.access(() -> {
                    String prettyTimeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                    //wywołujemy metodę s
                    calendarDataProvider.addEntriesFromDBWithHospitalNameAndDept(calendar, entryDyzurDbRepo, showCalendar.getChosenDateTime(),
                            showCalendar.getChosenView(), showCalendar.getCurrentlyChosenTimeSpan(),
                            showCalendar.getHospitalId(), showCalendar.getHospitalIdDept(), email);
                    System.out.println(prettyTimeStamp);

                });
            }
        } catch (InterruptedException ignored) {
        }
    }
}


