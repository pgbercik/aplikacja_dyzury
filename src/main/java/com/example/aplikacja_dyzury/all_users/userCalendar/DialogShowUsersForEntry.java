package com.example.aplikacja_dyzury.all_users.userCalendar;

import com.example.aplikacja_dyzury.data_model.Users;
import com.example.aplikacja_dyzury.data_model.EntryDyzurDb;
import com.example.aplikacja_dyzury.repository.EntryDyzurDbRepo;
import com.example.aplikacja_dyzury.data_model.custom_pojo.CustomAddedUsersForEntry;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DialogShowUsersForEntry extends Dialog {

    public DialogShowUsersForEntry(String clickedEntryId,EntryDyzurDbRepo entryDyzurDbRepo, String id) {
        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);
        setWidth("800px");


        VerticalLayout layout = new VerticalLayout();
        layout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.STRETCH);
//        layout.setSizeFull();
        layout.add(new H3("Lekarze zapisani na dyżur"));
        add(layout);

        EntryDyzurDb entryDyzurDb = entryDyzurDbRepo.findByID(id);
        Set<Users> foundUsers = entryDyzurDb.getUsers();
//        System.out.println("found users: "+ foundUsers);

        if (!foundUsers.isEmpty()) {

            List<CustomAddedUsersForEntry> list = new ArrayList<>();
            for (Users users1 : foundUsers) {
                list.add(new CustomAddedUsersForEntry(users1.getUsersId(), users1.getDoctorTitle().getType(),
                        users1.getFirstName(), users1.getLastName()));
            }

            Grid<CustomAddedUsersForEntry> userGrid = new Grid<>();
            userGrid.setItems(list);

            userGrid.addColumn(CustomAddedUsersForEntry::getProfTitle).setHeader("Tytuł").setAutoWidth(true);
            userGrid.addColumn(CustomAddedUsersForEntry::getFirstName).setHeader("Imię").setAutoWidth(true);
            userGrid.addColumn(CustomAddedUsersForEntry::getLastName).setHeader("Nazwisko").setAutoWidth(true);

            add(userGrid);
        } else {
            add(new Label("    Nikt jeszcze się nie zapisał"));
        }


        Button btnCloseWindow = new Button("Zamknij onko", e->{
            close();
        });
        btnCloseWindow.getElement().getThemeList().add("tertiary");
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(btnCloseWindow);
        add(horizontalLayout);
    }
}
