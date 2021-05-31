package com.example.aplikacja_dyzury.user.userCalendar;

import com.example.aplikacja_dyzury.DataModelAndRepo.EntryDyzurDb;
import com.example.aplikacja_dyzury.DataModelAndRepo.EntryDyzurDbRepo;
import com.example.aplikacja_dyzury.DataModelAndRepo.User;
import com.example.aplikacja_dyzury.FindUserData;
import com.example.aplikacja_dyzury.common.user_registration_ui.GoogleCalendarPoJo;
import com.example.aplikacja_dyzury.user.userCalendar.csv.CsvExportDataProvider;
import com.example.aplikacja_dyzury.user.userCalendar.csv.CsvFileWriter;
import com.example.aplikacja_dyzury.user.userCalendar.custom_vaadin_time_date_pickers.TimeDateTranslation;
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

    public DialogExportToCSV(EntryDyzurDbRepo entryDyzurDbRepo, Long hospitalId, Long deptId) {
        this.hospitalId=hospitalId;
        this.deptId=deptId;


        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);
//        setWidth("800px");


        VerticalLayout layout = new VerticalLayout();
        layout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.STRETCH);
//        layout.setSizeFull();

        dateStart = new DatePicker("Początek");
        dateEnd = new DatePicker("Koniec");

        TimeDateTranslation.makeDatePickerPolish(dateStart);
        TimeDateTranslation.makeDatePickerPolish(dateEnd);

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
                        exportToCSV(entryDyzurDbRepo);
                        add(verticalLayout);
                        numberOfFoundEntries.setText("Znalezione wydarzenia: "+exportedEntriesCounter);
                    }
                    if (deptModeButton.getValue().equals("wybrany oddział") && hospitalId!=null && deptId!=null) {
                        exportToCSV(entryDyzurDbRepo);
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

    private void exportToCSV(EntryDyzurDbRepo entryDyzurDbRepo) {
        exportedEntriesCounter=0;

        if (dateStart.getValue()!=null && dateEnd.getValue()!=null) {
            CsvExportDataProvider csvExportDataProvider = new CsvExportDataProvider(entryDyzurDbRepo);
            List<EntryDyzurDb> entriesMatching = csvExportDataProvider.getDataForGoogleCalendar(dateStart.getValue(),dateEnd.getValue(),deptModeButton.getValue(),hospitalId,deptId);
            if (!entriesMatching.isEmpty()) {
                List<GoogleCalendarPoJo> googleCalendarPoJos = new ArrayList<>();
                for (EntryDyzurDb entryDyzurDb1 : entriesMatching) {
                    boolean isAddedToEntry=false;
                    Set<User> foundUsers = entryDyzurDb1.getUsers();
                    for (User user1 : foundUsers) {
                        if (user1.getEmail().equals(FindUserData.findCurrentlyLoggedInUser())) isAddedToEntry=true;
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
                CsvFileWriter.writeDataLineByLine("calendar.csv",googleCalendarPoJos);

            }
        }
    }
}
