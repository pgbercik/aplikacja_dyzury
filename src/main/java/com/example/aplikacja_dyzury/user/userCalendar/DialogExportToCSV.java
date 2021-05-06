package com.example.aplikacja_dyzury.user.userCalendar;

import com.example.aplikacja_dyzury.DataModelAndRepo.EntryDyzurDb;
import com.example.aplikacja_dyzury.DataModelAndRepo.EntryDyzurDbRepo;
import com.example.aplikacja_dyzury.DataModelAndRepo.User;
import com.example.aplikacja_dyzury.FindUserData;
import com.example.aplikacja_dyzury.common.user_registration_ui.GoogleCalendarPoJo;
import com.example.aplikacja_dyzury.user.userCalendar.csv.CsvUtils;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import org.vaadin.olli.FileDownloadWrapper;

import java.io.File;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static java.time.temporal.ChronoUnit.DAYS;

public class DialogExportToCSV extends Dialog {
    private DatePicker dateStart;
    private DatePicker dateEnd;
    private Button btnCancel;
    private Button btnExport;
    private int exportedEntriesCounter=0;
    private RadioButtonGroup<String> deptModeButton;
    private Long hospitalId,deptId;

    public DialogExportToCSV(CalendarDataProvider calendarDataProvider, EntryDyzurDbRepo entryDyzurDbRepo,
                             FindUserData findUserData, Long hospitalId, Long deptId) {
        this.hospitalId=hospitalId;
        this.deptId=deptId;


        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);
//        setWidth("800px");


        VerticalLayout layout = new VerticalLayout();
        layout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.STRETCH);
        layout.setSizeFull();

        dateStart = new DatePicker("Początek");
        dateEnd = new DatePicker("Koniec");

        dateStart.setI18n(
                new DatePicker.DatePickerI18n().setWeek("tydzień").setCalendar("kalendarz")
                        .setClear("Wyczyść").setToday("dzisiaj")
                        .setCancel("Anuluj").setFirstDayOfWeek(1)
                        .setMonthNames(Arrays.asList("styczeń", "luty",
                                "marzec", "kwiecień", "maj", "czerwiec",
                                "lipiec", "sierpień", "wrzesień", "październik",
                                "listopad", "grudzień")).setWeekdays(
                        Arrays.asList("niedziela", "poniedziałek", "wtorek",
                                "środa", "czwartek", "piątek",
                                "sobota")).setWeekdaysShort(
                        Arrays.asList("nd.", "pon.", "wt.", "śr.", "czw.", "pt.",
                                "sob.")));

        dateEnd.setI18n(dateStart.getI18n());
        layout.add(dateStart,dateEnd);
        deptModeButton = new RadioButtonGroup<>();
        deptModeButton.setItems("wszystkie oddziały", "wybrany oddział");
        deptModeButton.setValue("wszystkie oddziały");
        add(deptModeButton);


        btnExport = new Button("Zdefiniuj okres");
        btnExport.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        btnCancel = new Button("Zamknij okno");
        btnCancel.addClickListener(event -> close());
        btnCancel.addThemeVariants(ButtonVariant.LUMO_CONTRAST);

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(btnExport,btnCancel);
        layout.add(horizontalLayout);
        add(layout);

        H5 numberOfFoundEntries = new H5("Znalezione wydarzenia: ");
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(numberOfFoundEntries);


        Button button = new Button("Pobierz plik CSV");
        FileDownloadWrapper buttonWrapper = new FileDownloadWrapper("calendar.csv",new File("calendar.csv"));
        buttonWrapper.wrapComponent(button);



        btnExport.addClickListener(event -> {


            if (dateStart.getValue()!=null && dateEnd.getValue()!=null) {
                long daysBetween = DAYS.between(dateStart.getValue(), dateEnd.getValue());

                if (daysBetween>=0) {
                    System.out.println("szpital i oddział "+hospitalId+" | "+deptId);

                    if (deptModeButton.getValue().equals("wszystkie oddziały")) {
                        exportToCSV(calendarDataProvider,entryDyzurDbRepo, findUserData);
                        add(verticalLayout);
                        numberOfFoundEntries.setText("Znalezione wydarzenia: "+exportedEntriesCounter);
                    }
                    if (deptModeButton.getValue().equals("wybrany oddział") && hospitalId!=null && deptId!=null) {
                        exportToCSV(calendarDataProvider,entryDyzurDbRepo, findUserData);
                        add(verticalLayout);
                        numberOfFoundEntries.setText("Znalezione wydarzenia: "+exportedEntriesCounter);
                    }
                    if (deptModeButton.getValue().equals("wybrany oddział") && hospitalId==null && deptId==null) {
                        Notification.show("Nie podano szpitala i oddziału",2000, Notification.Position.MIDDLE);
                    }
                    if (exportedEntriesCounter>=1) add(buttonWrapper);
                } else {
                    Notification.show("Data końcowa nie może być wcześniejsza od początkowej",2000, Notification.Position.MIDDLE);
                }
            } else {
                Notification.show("Nie podano zakresu wyszukiwania",2000, Notification.Position.MIDDLE);
            }

        });


    }

    private void exportToCSV(CalendarDataProvider calendarDataProvider, EntryDyzurDbRepo entryDyzurDbRepo,
                             FindUserData findUserData) {
        exportedEntriesCounter=0;

        if (dateStart.getValue()!=null && dateEnd.getValue()!=null) {

            List<EntryDyzurDb> entriesdMatching = calendarDataProvider.getDataForGoogleCalendar(entryDyzurDbRepo,
                    dateStart.getValue(),dateEnd.getValue(),deptModeButton.getValue(),hospitalId,deptId);
            if (!entriesdMatching.isEmpty()) {
                List<GoogleCalendarPoJo> googleCalendarPoJos = new ArrayList<>();
                for (EntryDyzurDb entryDyzurDb1 : entriesdMatching) {
                    boolean isAddedToEntry=false;
                    Set<User> foundUsers = entryDyzurDb1.getUsers();
                    for (User user1 : foundUsers) {
                        if (user1.getEmail().equals(findUserData.findCurrentlyLoggedInUser())) isAddedToEntry=true;
                    }
                    if (isAddedToEntry) {
                        String title = entryDyzurDb1.getTitle();
                        String startDate = entryDyzurDb1.getStartTime().toLocalDate().format(DateTimeFormatter.ofPattern("MM/dd/yy"));
                        String startTime = entryDyzurDb1.getStartTime().toLocalTime().format(DateTimeFormatter.ofPattern("hh:mm a"));
                        String endDate = entryDyzurDb1.getEndTime().toLocalDate().format(DateTimeFormatter.ofPattern("MM/dd/yy"));
                        String endTime = entryDyzurDb1.getEndTime().toLocalTime().format(DateTimeFormatter.ofPattern("hh:mm a"));
                        String description = entryDyzurDb1.getDescription();
                        String location = "Dyżur w szpitalu: "+entryDyzurDb1.getHospital().getName()+
                                " na oddziale: "+entryDyzurDb1.getHospitalDepartment().getDepartment();

                        System.out.println(title+","+startDate+","+startTime+","+endDate+","+endTime+","+description+","+location);
                        googleCalendarPoJos.add(new GoogleCalendarPoJo(title,startDate,startTime,endDate,endTime,description,location));
                    }
                }


                for (GoogleCalendarPoJo googleCalendarPoJo :googleCalendarPoJos) {
                    System.out.println("GOOGLE POJO"+googleCalendarPoJo.getSubjectTitle()+" | " +googleCalendarPoJo.getStartDate()
                            +googleCalendarPoJo.getStartTime()+" | "+googleCalendarPoJo.getEndDate()+googleCalendarPoJo.getEndTime());
                    exportedEntriesCounter+=1;
                }
                CsvUtils.writeDataLineByLine("calendar.csv",googleCalendarPoJos);

            }
        }
    }
}
