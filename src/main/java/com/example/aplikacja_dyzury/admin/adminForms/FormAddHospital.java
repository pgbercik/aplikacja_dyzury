package com.example.aplikacja_dyzury.admin.adminForms;

import com.example.aplikacja_dyzury.DataModelAndRepo.Hospital;
import com.example.aplikacja_dyzury.DataModelAndRepo.HospitalRepo;
import com.example.aplikacja_dyzury.navAndThemes.RegisteredMenuBar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.security.access.annotation.Secured;

import java.util.List;

@Secured("ROLE_ADMIN")
@PageTitle("Dodaj szpital")

@Route(value = "hospitalform", layout = RegisteredMenuBar.class)
public class FormAddHospital extends VerticalLayout {
    private Binder<Hospital> binder;
    private TextField name,address,city;
    private Button btnSave;
    private Button btnClearForm;

    private HospitalRepo hospitalRepo;

    public FormAddHospital(HospitalRepo hospitalRepo) {
        this.hospitalRepo = hospitalRepo;

        VerticalLayout layout = new VerticalLayout();
        layout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.STRETCH);
        layout.setSizeFull();

        //okienka do wpisywania
        name = new TextField("Nazwa");
        address = new TextField("Adres");
        city = new TextField("Miasto");
        layout.add(name,address,city);

        btnSave = new Button("Dodaj szpital");
        btnSave.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
//        btnClearForm = new Button("Wyczyść formularz");
//        btnClearForm.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        HorizontalLayout buttons = new HorizontalLayout(btnSave);
        layout.add(buttons);
        add(layout);

        binder = new Binder<>(Hospital.class);
        Hospital hospital = new Hospital();
        defineFormValidation(hospital);


        btnSave.addClickListener(buttonClickEvent -> {
            //walidacja po kliknięciu przycisku
            binder.validate();
            List<Hospital> alreadyExistingHospitals = hospitalRepo.findExistingHospitals(address.getValue(),city
            .getValue(),name.getValue());
            if (binder.isValid()) {


                if (alreadyExistingHospitals.isEmpty()) {
                    System.out.println(hospital.toString());

                    try {
                        hospital.setActive(false);
                        hospitalRepo.save(hospital);
                        Notification.show("Szpital dodany!", 1000, Notification.Position.MIDDLE);

                    } catch (Exception e) {
    //                    e.printStackTrace();
                        Notification.show("Wystąpił błąd przy dodawaniu szpitala. Spróbuj ponownie.", 1000, Notification.Position.MIDDLE);
                    }
                }
                else {
                    Notification.show("Taki szpital już istnieje!",1000, Notification.Position.MIDDLE);
                }


            }

        });

//        btnClearForm.addClickListener(buttonClickEvent -> {
//            name.clear();
//            address.clear();
//            city.clear();
//        });



//        save.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
//            @Override
//            public void onComponentEvent(ClickEvent<Button> event) {
//                try {
//
//                    Hospital hospital = new Hospital(name.getValue().trim(),address.getValue().trim(),city.getValue().trim());
//                    hospitalRepo.save(hospital);
//
//                    save.getUI().ifPresent(ui -> ui.navigate("hospitalTable")); // przejście do tabelki
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    Notification.show("Podaj wszystkie wartości!!!", 2000, Notification.Position.MIDDLE);
//                }
//            }
//        });
    }

    private void defineFormValidation(Hospital hospital) {
        //minimum 1 cyfra, minimum 1 małą litera, minimum 1 duża, minimum 1 znak specjalny,bez pustych znaków, minimum 8 znaków
//        String patternPassword = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        //wersja bez znaków specjalnych
        String patternPassword = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$";
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
