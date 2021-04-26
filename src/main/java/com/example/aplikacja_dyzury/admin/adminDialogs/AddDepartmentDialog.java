package com.example.aplikacja_dyzury.admin.adminDialogs;

import com.example.aplikacja_dyzury.DataModelAndRepo.Hospital;
import com.example.aplikacja_dyzury.DataModelAndRepo.HospitalDepartment;
import com.example.aplikacja_dyzury.DataModelAndRepo.HospitalDepartmentRepo;
import com.example.aplikacja_dyzury.DataModelAndRepo.HospitalRepo;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import org.springframework.security.access.annotation.Secured;

import java.util.List;

@Secured("ROLE_ADMIN")

public class AddDepartmentDialog extends Dialog {
//    private ComboBox<Hospital> chooseHospitalComboBox;
    private Hospital chosenHospital;
    private Binder<HospitalDepartment> binder;
    private TextField departmentName;
    private Button btnAddDepartment;

    public AddDepartmentDialog(Hospital hospital,HospitalRepo hospitalRepo, HospitalDepartmentRepo hospitalDepartmentRepo) {
        chosenHospital=hospital;
        setCloseOnEsc(true);
        setCloseOnOutsideClick(true);
        setWidth("900px");
        setHeightFull();


        departmentName = new TextField("Nazwa oddziału");
        HospitalDepartment hospitalDepartment = new HospitalDepartment();
        binder = new Binder<>(HospitalDepartment.class);
        defineFormValidation(hospitalDepartment);


        btnAddDepartment = new Button("Dodaj oddział");
        btnAddDepartment.addClickListener(buttonClickEvent -> {
            //walidacja po kliknięciu przycisku
            binder.validate();
            if (binder.isValid()) {
                List<HospitalDepartment> existingDepartments =
                        hospitalDepartmentRepo.findAlreadyExistingDepartment(departmentName.getValue(),hospital.getId());

                if (existingDepartments.isEmpty()) {
                    try {
                        hospitalDepartment.setActive(true);
                        System.out.println(hospitalDepartment.toString() +" wartość do dodania");
                        hospitalDepartmentRepo.save(hospitalDepartment);
                        chosenHospital.getHospitalDepartments().add(hospitalDepartment);
                        chosenHospital.setActive(true);
                        hospitalRepo.save(chosenHospital);
                        close();
                        Notification.show("Dodano oddział: "+hospitalDepartment.getDepartment(),1000, Notification.Position.MIDDLE);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Notification.show("Wystąpił błąd przy dodawaniu oddziału. "+hospitalDepartment.getDepartment(),1000, Notification.Position.MIDDLE);
                    }
                }
                else Notification.show("Oddział o takiej nazwie już istnieje!",1000, Notification.Position.MIDDLE);


            }
        });

        VerticalLayout layout = new VerticalLayout();
        layout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.STRETCH);
        layout.setSizeFull();
        layout.add(/*chooseHospitalComboBox,*/departmentName,btnAddDepartment);
        add(layout);

    }

    private void defineFormValidation(HospitalDepartment hospitalDepartment) {
        binder.forField(departmentName)
                .asRequired("Pole wymagane.")
                .bind(HospitalDepartment::getDepartment,HospitalDepartment::setDepartment);

        binder.setBean(hospitalDepartment);
    }

}
