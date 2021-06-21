package com.example.aplikacja_dyzury.all_users.userCalendar;


import com.example.aplikacja_dyzury.repository.EntryDyzurDbRepo;
import com.example.aplikacja_dyzury.repository.UserRepository;
import com.vaadin.flow.component.UI;
import org.vaadin.stefan.fullcalendar.FullCalendar;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Klasa z wątkiem, któy w tle aktualizuje dyżury w kalendarzu. Dzięki temu jeśli inny user doda dyżur to pozostali będą go widzieli bez konieczności odświeżania strony ręcznie.
 * A class with a thred that updates duties shown in calendar window. If a different user  adds a new duty, the our current user will be able to see changes without reloading the whole page manually.
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
                // usypiamy wątek na sekundę - we make the thread sleep for one second
                Thread.sleep(1000);

                ui.access(() -> {
                    String prettyTimeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                    //wywołujemy metodę aktualizującą dane wyświetlane w kalendarzu
                    // we are running a method that updates data shown in calendar
                    calendarDataProvider.addEntriesFromDBWithHospitalNameAndDept(calendar, showCalendar.getChosenDateTime(),
                            showCalendar.getChosenView(), showCalendar.getCurrentlyChosenTimeSpan(),
                            showCalendar.getHospitalId(), showCalendar.getHospitalIdDept(), email);
                    System.out.println(prettyTimeStamp);

                });
            }
        } catch (InterruptedException ignored) {
        }
    }
}


