package com.example.aplikacja_dyzury.user.userCalendar;

import com.example.aplikacja_dyzury.DataModelAndRepo.*;
import com.example.aplikacja_dyzury.navAndThemes.RegisteredMenuBar;

import com.example.aplikacja_dyzury.user.userCalendar.custom_vaadin_time_date_pickers.TimeDateTranslation;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

@Route(value = "entryExchangeForm",layout = RegisteredMenuBar.class)
public class DialogEntryExchange extends Dialog {
    private String width="400px";
    private Requests requests;

    public DialogEntryExchange(EntryDyzurDb entryDyzurDbClickedFromCalendar, User userInit, EntryDyzurDbRepo entryDyzurDbRepo, RequestsRepo requestsRepo) {



        VerticalLayout verticalLayout = new VerticalLayout();


        DatePicker datePicker = new DatePicker("Drugi dyżur");
        verticalLayout.add(datePicker);

        ComboBox<EntryDyzurDb> entryToExchageFromDialog = new ComboBox<>("Nazwa dyżuru do wymiany");
        ComboBox<User> existingUsersList = new ComboBox<>("Użytkownik do zamiany");

        Button btnSendExchangeRequest = new Button("Wyślij prośbę o zamianę");
        btnSendExchangeRequest.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Button btnCancel = new Button("Anuluj");
        btnCancel.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        btnCancel.addClickListener(event -> {
            close();
        });


        verticalLayout.add(entryToExchageFromDialog);
        verticalLayout.add(existingUsersList);

        entryToExchageFromDialog.setEnabled(false);
        existingUsersList.setEnabled(false);

        entryToExchageFromDialog.setWidth(width);
        existingUsersList.setWidth(width);
        datePicker.setWidth(width);




//
        datePicker.addValueChangeListener(event -> {
            List<EntryDyzurDb> entryDyzurDbs = entryDyzurDbRepo.findAllWithStartDateGiven(datePicker.getValue());




            Long diffInDays = DAYS.between(LocalDate.now(),datePicker.getValue());


            if (diffInDays>=0) {
                if (!entryDyzurDbs.isEmpty()) {

                    entryToExchageFromDialog.setItems(entryDyzurDbs);
                    entryToExchageFromDialog.setItemLabelGenerator(EntryDyzurDb::getTitle);
                    entryToExchageFromDialog.setEnabled(true);



                    entryToExchageFromDialog.addValueChangeListener(event1 -> {

                        EntryDyzurDb chosenSecondEntry = entryToExchageFromDialog.getValue();
                        datePicker.setEnabled(false);

                        existingUsersList.setItems(chosenSecondEntry.getUsers());
                        existingUsersList.setItemLabelGenerator(User::getCustomUserNameAndSurname);
                        existingUsersList.setEnabled(true);
                        existingUsersList.clear();
                        existingUsersList.addValueChangeListener(event2 -> {

                            entryToExchageFromDialog.setEnabled(false);

                            requests=null;
                            requests = new Requests("prośba o zamianę dyżuru",LocalDateTime.now(),
                                    1,true,entryDyzurDbClickedFromCalendar,entryToExchageFromDialog.getValue(),
                                    userInit,existingUsersList.getValue());





                            add(btnSendExchangeRequest,btnCancel);


                        });
                    });


                } else {
                    Notification.show("W wybranym dniu nie dodano dyżurów lub nikt z personelu się nie zarejestrował",3000, Notification.Position.MIDDLE);
                }
            } else {
                Notification.show("Nie można dokonywać zamiany dyżurów, które trwają lub już się zakończyły.",3000, Notification.Position.MIDDLE);
            }
        });
        btnSendExchangeRequest.addClickListener(event3 -> {
            if (requests!=null) {
                requestsRepo.save(requests);

                Notification.show("Wysłano prośbę o zamianę.",2000, Notification.Position.MIDDLE);
                close();
            }
            else Notification.show("Uzupełnij dane",2000, Notification.Position.MIDDLE);
        });
        add(verticalLayout);
        TimeDateTranslation.makeDatePickerPolish(datePicker);


    }
}
