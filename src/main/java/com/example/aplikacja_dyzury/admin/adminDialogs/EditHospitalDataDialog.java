package com.example.aplikacja_dyzury.admin.adminDialogs;

import com.example.aplikacja_dyzury.DataModelAndRepo.Hospital;
import com.example.aplikacja_dyzury.DataModelAndRepo.HospitalRepo;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationResult;
import org.springframework.security.access.annotation.Secured;

@Secured("ROLE_ADMIN")
public class EditHospitalDataDialog extends Dialog {
    private TextField name,address,city;
    private Button btnSaveChanges;
    private Binder<Hospital> binder;

    public EditHospitalDataDialog(Hospital hospital, HospitalRepo hospitalRepo) {
        setCloseOnEsc(true);
        setCloseOnOutsideClick(true);
        setWidth("900px");
        setHeightFull();

       VerticalLayout verticalLayout = new VerticalLayout();
       verticalLayout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.STRETCH);
        verticalLayout.setSizeFull();

        //textfields for name,address and city
        name = new TextField("Nazwa");
        address = new TextField("Adres");
        city = new TextField("Miasto");

        binder = new Binder<>(Hospital.class);
        defineFormValidation(hospital);

        btnSaveChanges = new Button("Zapisz zmiany");
        btnSaveChanges.addClickListener(event -> {

            binder.validate();
            if (binder.isValid()) {
                hospital.setName(name.getValue());
                hospital.setAddress(address.getValue());
                hospital.setCity(city.getValue());
                hospitalRepo.save(hospital);
                Notification.show("Wprowadzono zmiany",1000, Notification.Position.MIDDLE);
                close();
            }

        });

        verticalLayout.add(name,address,city,btnSaveChanges);
        add(verticalLayout);
    }

    private void defineFormValidation(Hospital hospital) {
        // imię nie może zawierać cyfr, name cannot contain numbers
        String patternNames = "^[^\\d]+$";



        binder.forField(name)
                .asRequired("Pole wymagane.")
                .bind(Hospital::getName,Hospital::setName);

        binder.forField(address)
                .asRequired("Pole wymagane.")
                .bind(Hospital::getAddress,Hospital::setAddress);

        binder.forField(city)
                .asRequired("Pole wymagane.")
                .withValidator((value, context) -> {
                    if (!value.matches(patternNames)) return ValidationResult.error(
                            "Pole nie może zawierać cyfr");
                    else return ValidationResult.ok();
                })
                .asRequired("").asRequired("Pole wymagane.")
                .bind(Hospital::getCity,Hospital::setCity);

        binder.setBean(hospital);
    }
}
