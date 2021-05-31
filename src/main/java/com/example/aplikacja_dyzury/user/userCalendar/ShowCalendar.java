package com.example.aplikacja_dyzury.user.userCalendar;

import com.example.aplikacja_dyzury.DataModelAndRepo.*;
import com.example.aplikacja_dyzury.FindUserData;
import com.example.aplikacja_dyzury.navAndThemes.RegisteredMenuBar;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.SessionDestroyEvent;
import com.vaadin.flow.server.SessionDestroyListener;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.stefan.fullcalendar.CalendarViewImpl;
import org.vaadin.stefan.fullcalendar.Entry;
import org.vaadin.stefan.fullcalendar.FullCalendar;
import org.vaadin.stefan.fullcalendar.FullCalendarBuilder;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import static java.time.temporal.ChronoUnit.DAYS;

@PageTitle("Kalendarz")
@Route(value = "calendar", layout = RegisteredMenuBar.class)
public class ShowCalendar extends VerticalLayout implements SessionDestroyListener {

    //referencja na obiekt z wątkiem aktualizującym zawartość kalendarza w tle
    //reference to an object with a thread that updates a calendar in the background
    private BackgroundCalendarUpdaterThread thread;

    private LocalDate chosenDateTime = LocalDate.now();
    private Long monthDiff = 0L;
    H4 currentlyChosenTimeSpan;
    private Long hospitalId = null;
    private Long hospitalIdDept = null;

    //tu zapisujemy czy wybrano miesiąc czy tydzień w widoku
    // here we define whether use chose month or week view
    private String chosenView = "month";

    private final EntryDyzurDbRepo entryDyzurDbRepo;
    private final UserRepository userRepository;
    private final CalendarDataProvider calendarDataProvider;
//    private final FindUserData findUserData;
    private final FullCalendar calendar;

    private String email;
    private boolean autoRefresh = true;


    @Override
    public void sessionDestroy(SessionDestroyEvent sessionDestroyEvent) {
        cleanUpAfterThread();
        UI.getCurrent().getPage().setLocation("/loggedOutMainPage");
    }

    @Autowired
    public ShowCalendar(EntryDyzurDbRepo entryDyzurDbRepo, HospitalRepo hospitalRepo,
                        HospitalDepartmentRepo hospitalDepartmentRepo, UserRepository userRepository, RequestsRepo requestsRepo) {

        this.entryDyzurDbRepo = entryDyzurDbRepo;
        this.userRepository = userRepository;
        calendarDataProvider = new CalendarDataProvider(entryDyzurDbRepo);
//        findUserData = new FindUserData();

        email = FindUserData.findCurrentlyLoggedInUser();


        FindUserData.findUserRoles();


        // Create a new calendar instance and attach it to our layout
        calendar = FullCalendarBuilder.create().build();


        //czas trwania sesji ustawiamy na 300 sekund, po trochę ponad ponad 300 sekundach braku aktywności
        // ( i przy jednoczesnym zamknięciu okna przeglądarki) sesja się kończy i przy okazji też wątki działające w tle
        //we set session interval to 300 seconds, and after a bit more than that of no activity
        // (and closing browser tabs or window) the sessino ends together with background threads
        VaadinSession.getCurrent().getSession().setMaxInactiveInterval(300);

        //skrypt JS sprawdzający czy user nie zmienił zakładki
        //a JS scriptr checking if the user changed tabs
        UI.getCurrent().getPage().executeJs("function closeListener() { $0.$server.windowClosed(); } " +
                "window.addEventListener('beforeunload', closeListener); " +
                "window.addEventListener('unload', closeListener);", getElement());


        //pokazywanie numerów dni tygodnia
        //showing weekday numbers in calendar
        calendar.setWeekNumbersVisible(true);
        //pierwszym dniem tygodnia jest poniedziałek - first day of week is monday
        calendar.setFirstDay(DayOfWeek.MONDAY);
        //polskie dni tygodnia - polish names for weekdays
        calendar.setLocale(Locale.forLanguageTag("pl-PL"));
        calendar.setHeight(490);

        calendar.setTimeslotsSelectable(true);
        calendar.setNumberClickable(false);

        Button buttonPrevious = new Button("Wcześniej ", VaadinIcon.ANGLE_LEFT.create(), e -> {

            if (chosenView.equals("month")) {

                monthDiff = 1L;
                chosenDateTime = chosenDateTime.minusMonths(monthDiff);
                calendarDataProvider.addEntriesFromDBWithHospitalNameAndDept(calendar, chosenDateTime,
                         chosenView, currentlyChosenTimeSpan, hospitalId, hospitalIdDept, "");

                calendar.previous();
            }
            if (chosenView.equals("week")) {

                chosenDateTime = chosenDateTime.minusDays(7L);
                calendarDataProvider.addEntriesFromDBWithHospitalNameAndDept(calendar, chosenDateTime,
                         chosenView, currentlyChosenTimeSpan, hospitalId, hospitalIdDept, "");

                calendar.previous();
            }
        });
        Button buttonNext = new Button("Później", VaadinIcon.ANGLE_RIGHT.create(), e -> {

            if (chosenView.equals("month")) {

                monthDiff = 1L;
                chosenDateTime = chosenDateTime.plusMonths(monthDiff);
                calendarDataProvider.addEntriesFromDBWithHospitalNameAndDept(calendar, chosenDateTime,
                         chosenView, currentlyChosenTimeSpan, hospitalId, hospitalIdDept, "");

                calendar.next();
            }
            if (chosenView.equals("week")) {

                chosenDateTime = chosenDateTime.plusDays(7L);
                calendarDataProvider.addEntriesFromDBWithHospitalNameAndDept(calendar, chosenDateTime,
                         chosenView, currentlyChosenTimeSpan, hospitalId, hospitalIdDept, "");

                calendar.next();
            }

        });
        Button btnExport = new Button("Export do kalendarza Google", VaadinIcon.CALENDAR_O.create());

        btnExport.addClickListener(event -> new DialogExportToCSV(entryDyzurDbRepo, hospitalId, hospitalIdDept).open());




        Button btnMonth = new Button("Miesiąc", VaadinIcon.CALENDAR.create(), e -> {
            chosenView = "month";
            chosenDateTime = LocalDate.now();
            calendarDataProvider.addEntriesFromDBWithHospitalNameAndDept(calendar, chosenDateTime,
                     chosenView, currentlyChosenTimeSpan, hospitalId, hospitalIdDept, "");
            calendar.changeView(CalendarViewImpl.DAY_GRID_MONTH);
            calendar.today();
        });
        Button btnWeek = new Button("Tydzień", VaadinIcon.CALENDAR.create(), e -> {
            chosenView = "week";
            chosenDateTime = LocalDate.now();
            calendarDataProvider.addEntriesFromDBWithHospitalNameAndDept(calendar, chosenDateTime,
                     chosenView, currentlyChosenTimeSpan, hospitalId, hospitalIdDept, "");
            calendar.changeView(CalendarViewImpl.DAY_GRID_WEEK);
            calendar.today();
        });

        currentlyChosenTimeSpan = new H4();


        Button btnHelp = new Button("Pomoc", VaadinIcon.QUESTION.create());
        btnHelp.addClickListener(event -> new DialogHelp().open());


        ComboBox<Hospital> fieldHospitalName = new ComboBox<>();
        fieldHospitalName.setItems(hospitalRepo.findAll());
        fieldHospitalName.setItemLabelGenerator(Hospital::getName);
        fieldHospitalName.setRequired(true);
        fieldHospitalName.setWidth("450px");
        fieldHospitalName.setClearButtonVisible(true);

        ComboBox<HospitalDepartment> fieldHospitalDepartment = new ComboBox<>();
        fieldHospitalDepartment.setItemLabelGenerator(HospitalDepartment::getDepartment);
        fieldHospitalDepartment.setRequired(true);
        fieldHospitalDepartment.setEnabled(false);
        fieldHospitalDepartment.setWidth("450px");
        fieldHospitalDepartment.setClearButtonVisible(true);

        fieldHospitalDepartment.addValueChangeListener(event -> {
            if (fieldHospitalName.getValue() == null) fieldHospitalDepartment.clear();
        });

        fieldHospitalName.addValueChangeListener(event -> {
            if (fieldHospitalName.getValue() != null) {
                List<HospitalDepartment> itemList = hospitalDepartmentRepo.findDepartmentByHospitalId(event.getValue().getId());
                fieldHospitalDepartment.setItems(itemList);
                fieldHospitalDepartment.setEnabled(true);
            } else fieldHospitalDepartment.clear();

        });


        Button btnSort = new Button("Filtruj dyżury", event -> {
            if (fieldHospitalName.getValue() == null) hospitalId = null;
            else hospitalId = fieldHospitalName.getValue().getId();
            if (fieldHospitalDepartment.getValue() == null) hospitalIdDept = null;
            else hospitalIdDept = fieldHospitalDepartment.getValue().getId();
            calendarDataProvider.addEntriesFromDBWithHospitalNameAndDept(calendar, chosenDateTime,
                     chosenView, currentlyChosenTimeSpan, hospitalId, hospitalIdDept, "");
        });


        HorizontalLayout buttons1 = new HorizontalLayout();
        HorizontalLayout buttons2 = new HorizontalLayout();
        if (FindUserData.findFirstUserRoleString().equals("ROLE_USER")) {
            buttons1.add(buttonPrevious, currentlyChosenTimeSpan, buttonNext, btnMonth, btnWeek, btnExport);
        } else buttons1.add(buttonPrevious, currentlyChosenTimeSpan, buttonNext, btnMonth, btnWeek);


        buttons1.add(btnHelp);
        buttons2.add(btnSort, fieldHospitalName, fieldHospitalDepartment);

        //event od przeciągania entries na inne dni itd.
        calendar.addEntryDroppedListener(entryDroppedEvent -> {




            Entry entry = entryDroppedEvent.getEntry();



            String entryToEditID = entryDyzurDbRepo.findEntryToEdit(/*entryDroppedEvent.getEntry().getColor(),*/
                    entryDroppedEvent.getEntry().getDescription(), entryDroppedEvent.getEntry().getStart(),
                    entryDroppedEvent.getEntry().getEnd(), entryDroppedEvent.getEntry().getHospital(),
                    entryDroppedEvent.getEntry().getTitle());

            //po przesunięciu na kratkę z nowym dniem zczytuje z nowego dnia datę i zapisuje zmiany w Entry (tylko warstwa wizualna)
            entryDroppedEvent.applyChangesOnEntry();





            new DialogAddEditEvent(calendar, entry, false, entryDyzurDbRepo, entryToEditID,
                    hospitalRepo, hospitalDepartmentRepo, userRepository, chosenView, currentlyChosenTimeSpan, hospitalId, hospitalIdDept,
                    chosenDateTime, calendarDataProvider, requestsRepo).open();

        });


        add(buttons1, buttons2, calendar);


        // akcja po kliknięciu na istniejące wydarzenie
        //what happens after clicking on an existing duty
        calendar.addEntryClickedListener(event -> {
            String entryToEditID = entryDyzurDbRepo.findEntryToEdit(/*event.getEntry().getColor(),*/
                    event.getEntry().getDescription(), event.getEntry().getStart(), event.getEntry().getEnd(),
                    event.getEntry().getHospital(), event.getEntry().getTitle());


            Entry entry = event.getEntry();

            new DialogAddEditEvent(calendar, entry, false, entryDyzurDbRepo, entryToEditID,
                    hospitalRepo, hospitalDepartmentRepo, userRepository, chosenView, currentlyChosenTimeSpan, hospitalId, hospitalIdDept,
                    chosenDateTime, calendarDataProvider, requestsRepo).open();

        });

        //akcja po kliknięciu na pusty obszar - dodawanie wydarzenia
        //what happens after clicking on a blank area - adding a new duty
        calendar.addTimeslotsSelectedListener((event) -> {

            Long diffInDays = DAYS.between(LocalDate.now(), event.getStartDateTime());

            if (diffInDays >= -14) {
                Entry entry;
                entry = new Entry();
                entry.setStart(event.getStartDateTime());
                entry.setEnd(event.getStartDateTime());


                String entryToEditID = "";

                new DialogAddEditEvent(calendar, entry, true, entryDyzurDbRepo, entryToEditID,
                        hospitalRepo, hospitalDepartmentRepo, userRepository, chosenView, currentlyChosenTimeSpan, hospitalId, hospitalIdDept,
                        chosenDateTime, calendarDataProvider, requestsRepo).open();
            } else {
                Notification.show("Data rozpoczęcia dyżuru jest wcześniejsza o ponad 14 dni od daty dzisiejszej", 3000, Notification.Position.MIDDLE);
            }

        });

        //pobiera z bazy i wyświetla dyżury z miesiąca poprzedniego, bieżącego i przyszłego
        //fetches duties from a monrh before, current and following
        calendarDataProvider.addEntriesFromDBWithHospitalNameAndDept(calendar,
                LocalDate.now(), chosenView, currentlyChosenTimeSpan, hospitalId, hospitalIdDept, "");
    }

    //listener od skryputu sprawdzającego czy user nie zmienił zakładki
    //listener for a script that checks wheter user changed the tab
    @ClientCallable
    public void windowClosed() {
        cleanUpAfterThread();
        System.out.println("Sesja wygasła");
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {

        // uruchamiamy wątek
        //we're creating and starting a thread
        thread = new BackgroundCalendarUpdaterThread(attachEvent.getUI(), this, calendarDataProvider,
                calendar, entryDyzurDbRepo, userRepository, email);
        thread.start();
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        cleanUpAfterThread();
    }

    /**
     * sprzątamy po wątku - cleanup after the thread.
     */
    public void cleanUpAfterThread() {
        if (thread != null) {
            if (thread.isAlive()) {
                thread.interrupt();
                thread = null;
                System.out.println("Background thread has now ended");
            }
        }
    }

    public LocalDate getChosenDateTime() {
        return chosenDateTime;
    }

    public String getChosenView() {
        return chosenView;
    }

    public H4 getCurrentlyChosenTimeSpan() {
        return currentlyChosenTimeSpan;
    }

    public Long getHospitalId() {
        return hospitalId;
    }

    public Long getHospitalIdDept() {
        return hospitalIdDept;
    }
}
