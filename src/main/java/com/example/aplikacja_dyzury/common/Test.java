package com.example.aplikacja_dyzury.common;

import com.example.aplikacja_dyzury.DataModelAndRepo.*;
import com.example.aplikacja_dyzury.navAndThemes.NonRegisteredMenuBar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Route(value = "testPage",layout = NonRegisteredMenuBar.class)
public class Test extends VerticalLayout {

    @Autowired
    public Test(UserRepository userRepository, RequestsRepo requestsRepo) {

        add(new H4("ID: "+userRepository.findByEmail("johndoe@gmail.com")));

//        Button btn =new Button("ddd");
//        btn.addClickListener(event -> {
//            User user = null;
//            List<User> userList = userRepository.findAll();
//            for (User user1 :userList ) if (user1.getEmail().equals("johndoe@gmail.com")) user=user1;
//            System.out.println(user);
//            Requests requests = new Requests("ddd",LocalDateTime.now(),1,true,user);
//            requestsRepo.save(requests);
//        });
//        add(btn);

        DatePicker datePicker = new DatePicker("Drugi dyżur");
        add(datePicker);
        datePicker.addValueChangeListener(event -> {
            Notification.show(datePicker.getValue().toString(),1000, Notification.Position.MIDDLE);
        });
        makeDatePickerPolish(datePicker);

    }
    private void makeDatePickerPolish(DatePicker datePicker) {
        datePicker.setI18n(
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
    }
}
