package com.example.aplikacja_dyzury.admin.adminDialogs;

import com.example.aplikacja_dyzury.DataModelAndRepo.HospitalDepartment;
import com.example.aplikacja_dyzury.DataModelAndRepo.HospitalDepartmentRepo;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import org.springframework.security.access.annotation.Secured;

import java.util.List;

@Secured("ROLE_ADMIN")

public class ShowEditDepartmentsDialog extends Dialog {

    public ShowEditDepartmentsDialog(HospitalDepartmentRepo hospitalDepartmentRepo, Long hospitalId) {
        setCloseOnEsc(true);
        setCloseOnOutsideClick(true);
        setWidth("900px");
        setHeight("650px");

        VerticalLayout layout = new VerticalLayout();
        layout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.STRETCH);
        layout.setSizeFull();


        System.out.println("id szpitala : "+hospitalId);
        List<HospitalDepartment> hospitalDepartments = hospitalDepartmentRepo.findDepartmentByHospitalId(hospitalId);
        if (!hospitalDepartments.isEmpty()) {


            Grid<HospitalDepartment> grid = new Grid<>();
            grid.setItems(hospitalDepartments);
            grid.setColumnReorderingAllowed(true);


            grid.addComponentColumn(hospitalDepartment -> {
                Button button = new Button("Edytuj nazwę");
                TextField textField = new TextField();
                textField.setWidth("650px");
                textField.setValue(hospitalDepartment.getDepartment());

                HorizontalLayout horizontalLayout = new HorizontalLayout();
                horizontalLayout.add(textField,button);
                button.addClickListener(click ->{

                    hospitalDepartment.setDepartment(textField.getValue());
                    hospitalDepartmentRepo.save(hospitalDepartment);
                    Notification.show("Zmieniono nazwę odziału",500, Notification.Position.MIDDLE);
                });
                return horizontalLayout;
            }).setHeader("Oddziały").setAutoWidth(true);


            grid.addItemClickListener(event -> {

                Notification.show(event.getItem().getDepartment(),1000, Notification.Position.MIDDLE);
                UI.getCurrent().getPage().reload();
            });

            layout.add(grid);
        }
        else layout.add(new Label("Nie dodano jeszcze oddziałów"));
        add(layout);
    }
}
