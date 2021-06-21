package com.example.aplikacja_dyzury.all_users.userCalendar.custom_time_date_pickers;

import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.timepicker.TimePicker;

import java.util.Arrays;
import java.util.Locale;

public final class TimeDateTranslation {

    /**
     * polskie tłumaczenie datepickera i format wyświetlania dat
     * */
    public static void makeDatePickerPolish(DatePicker datePicker) {
        //polskie napisy
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
        //polski układ dat
        datePicker.setLocale(new Locale("pl"));
    }
    /**
     * polski format wyświetlaia godzin
     * */
    public static void makeTimePickerPolish(TimePicker timePicker) {
        //polski układ daty i godziny
        timePicker.setLocale(new Locale("pl"));
    }
}
