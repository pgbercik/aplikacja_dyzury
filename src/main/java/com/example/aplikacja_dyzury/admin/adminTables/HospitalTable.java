package com.example.aplikacja_dyzury.admin.adminTables;


import com.example.aplikacja_dyzury.data_model.Hospital;
import com.example.aplikacja_dyzury.repository.HospitalDepartmentRepo;
import com.example.aplikacja_dyzury.repository.HospitalRepo;

import com.example.aplikacja_dyzury.nav_and_themes.RegisteredMenuBar;
import com.example.aplikacja_dyzury.nav_and_themes.ThemeChanger;
import com.example.aplikacja_dyzury.admin.adminDialogs.AddDepartmentDialog;
import com.example.aplikacja_dyzury.admin.adminDialogs.EditHospitalDataDialog;
import com.example.aplikacja_dyzury.admin.adminDialogs.ShowEditDepartmentsDialog;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.annotation.Secured;


import java.util.ArrayList;
import java.util.List;

@Secured("ROLE_ADMIN")
@PageTitle("Szpitale")
@Route(value = "hospitalTable",layout = RegisteredMenuBar.class)

public class HospitalTable extends VerticalLayout {

    private HospitalRepo hospitalRepo;
    private ThemeChanger themeChanger;
    private Button btnNextPage, btnPreviousPage;
    private  int page=0;
    private int size=5;
    private Div buttonsDiv;
    private int totalPages=0;


    public HospitalTable(HospitalRepo hospitalRepo, ThemeChanger themeChanger, HospitalDepartmentRepo hospitalDepartmentRepo) {
        Label currentPage = new Label();


        this.hospitalRepo = hospitalRepo;
        this.themeChanger = themeChanger;

        add(new H1("Utworzone szpitale"),new H4("Kliknij wiersz tabeli, aby edytować."));
        btnNextPage = new Button("Następna strona");
        btnPreviousPage = new Button("Poprzednia strona");


        btnNextPage.addClickListener(event -> {
            if (page<totalPages-1) addTableWithPagination(page+=1,size,hospitalDepartmentRepo);
            currentPage.setText(page+1+" z "+totalPages);
        });
        btnPreviousPage.addClickListener(event -> {

            if (page>0) addTableWithPagination(page-=1,size,hospitalDepartmentRepo);

            currentPage.setText(page+1+" z "+totalPages);
        });
        buttonsDiv = new Div();
        buttonsDiv.addClassName("buttonsDiv");



        buttonsDiv.add(btnPreviousPage,currentPage,btnNextPage);
        addTableWithPagination(page,size,hospitalDepartmentRepo);
        currentPage.setText(page+1+" z "+totalPages);





    }

    private void addTableWithPagination(int page, int size, HospitalDepartmentRepo hospitalDepartmentRepo) {
        removeAll();
        add(new H1("Utworzone szpitale"),new H4("Kliknij wiersz tabeli, aby edytować."));
        add(buttonsDiv);

        List<Hospital> hospitals = new ArrayList<>();

        Page<Hospital> allProducts = hospitalRepo.findAll(PageRequest.of(page, size));
        totalPages=allProducts.getTotalPages();
        hospitals.clear();
        for (Hospital hospital : allProducts) {
            hospitals.add(hospital);
        }


        if (!hospitals.isEmpty()) {

            Grid<Hospital> grid = new Grid<>();
            grid.setItems(hospitals);
            grid.setColumnReorderingAllowed(true);
            grid.addColumn(Hospital::getName).setHeader("Nazwa").setAutoWidth(true);
            grid.addColumn(Hospital::getAddress).setHeader("Adres").setAutoWidth(true);
            grid.addColumn(Hospital::getCity).setHeader("Miasto").setAutoWidth(true);



            add(grid);

            grid.setItemDetailsRenderer(new ComponentRenderer<>(hospital -> {
                Div buttonsInsideTableRow = new Div();
                buttonsInsideTableRow.addClassName("buttonsDiv");

                Button btnEditDepartments = new Button("Pokaż/edytuj nazwy oddziałów");
                btnEditDepartments.addClickListener(event -> {
                    new ShowEditDepartmentsDialog(hospitalDepartmentRepo,hospital.getId()).open();
                });
                Button btnAddDepartment = new Button("Dodaj oddział");
                btnAddDepartment.addClickListener(event -> {
                    AddDepartmentDialog addDepartmentDialog = new AddDepartmentDialog(hospital,hospitalRepo,hospitalDepartmentRepo);
                    addDepartmentDialog.open();
                    addDepartmentDialog.addDialogCloseActionListener(event2 -> {
                        addDepartmentDialog.close();
                        addTableWithPagination(page,size,hospitalDepartmentRepo);
                    } );

                });
                Button btn = new Button("Edytuj dane teleadresowe");
                btn.addClickListener(event -> {
                    EditHospitalDataDialog editHospitalDataDialog = new EditHospitalDataDialog(hospital,hospitalRepo);
                    editHospitalDataDialog.open();
                    editHospitalDataDialog.addDialogCloseActionListener(event1 -> {
                        editHospitalDataDialog.close();
                        addTableWithPagination(page,size,hospitalDepartmentRepo);
                    });
                });

                buttonsInsideTableRow.add(btnAddDepartment,btnEditDepartments,btn);
                return  buttonsInsideTableRow;

            }));



        } else { add(new H1("Nie dodano jeszcze żadnych szpitali.")); }
    }
}
