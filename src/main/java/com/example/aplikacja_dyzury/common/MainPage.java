package com.example.aplikacja_dyzury.common;


import com.example.aplikacja_dyzury.DataModelAndRepo.*;
import com.example.aplikacja_dyzury.DataModelAndRepo.custom_pojo.CustomRequestView;
import com.example.aplikacja_dyzury.FindUserData;
import com.example.aplikacja_dyzury.navAndThemes.RegisteredMenuBar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.annotation.Secured;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Secured({"ROLE_ADMIN", "ROLE_USER"})
@PageTitle("Strona główna")
@Route(value = "", layout = RegisteredMenuBar.class)

public class MainPage extends VerticalLayout {
    private RadioButtonGroup<String> currentOrHistory;
    private List<CustomRequestView> customRequestViews;
    private Grid<CustomRequestView> grid;
    private  int page=0;
    private int size=5;
    private HorizontalLayout horizontalLayout;
    private int totalPages=0;
    private User loggedInUserDetails;
    private DateTimeFormatter formatter;
    private Button btnNextPage,btnPreviousPage;
    private H5 currentPage;




    @Autowired
    public MainPage(UserRepository userRepository, RequestsRepo requestsRepo, RequestStatusRepo requestStatusRepo,EntryDyzurDbRepo entryDyzurDbRepo) {
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        loggedInUserDetails = userRepository.findByEmail(FindUserData.findCurrentlyLoggedInUser());
        currentPage = new H5(page + 1 + " z " + 1);

        if (FindUserData.findFirstUserRoleString().equals("ROLE_ADMIN")) {
            add(new H1("Zalogowano jako " /*+loggedInUserDetails.getFirstName()+" "+loggedInUserDetails.getLastName()*/ + "administrator."));
            add(new H3("Witamy w panelu administratora."));
        }
        if (FindUserData.findFirstUserRoleString().equals("ROLE_USER")) {
            add(new H2("Przychodzące propozycje zamiany dyżurów"));
            add(new H3("Wybierz jedną z opcji"));





            currentOrHistory = new RadioButtonGroup<>();
            currentOrHistory.setItems("do rozpatrzenia", "rozpatrzone");
            currentOrHistory.setValue("do rozpatrzenia");


            add(currentOrHistory);

            btnNextPage = new Button("Następna strona");
            btnPreviousPage = new Button("Poprzednia strona");
            btnNextPage.addClickListener(event -> {

                if (page < totalPages - 1) {
                    if (currentOrHistory.getValue().equals("do rozpatrzenia")) {
                        addTableWithPaginationWithButtons(page += 1, size, requestsRepo, requestStatusRepo, entryDyzurDbRepo, userRepository);
                    }
                    if (currentOrHistory.getValue().equals("rozpatrzone")) {
                        addTableWithPagination(page += 1, size, requestsRepo, requestStatusRepo, entryDyzurDbRepo, userRepository);
                    }
                }

                currentPage.setText(page + 1 + " z " + totalPages);
            });
            btnPreviousPage.addClickListener(event -> {

                if (page > 0) {
                    if (currentOrHistory.getValue().equals("do rozpatrzenia")) {
                        addTableWithPaginationWithButtons(page -= 1, size, requestsRepo, requestStatusRepo, entryDyzurDbRepo, userRepository);
                    }
                    if (currentOrHistory.getValue().equals("rozpatrzone")) {
                        addTableWithPagination(page -= 1, size, requestsRepo, requestStatusRepo, entryDyzurDbRepo, userRepository);
                    }
                }

                currentPage.setText(page + 1 + " z " + totalPages);
            });
            horizontalLayout = new HorizontalLayout();



            horizontalLayout.add(btnPreviousPage,currentPage,btnNextPage);

            //wstępnie wyświetlamy te do rozpatrzenia
            //we're showing duty swap requests that have not been accepted or declined yet
            addTableWithPaginationWithButtons(page,size,requestsRepo,requestStatusRepo,entryDyzurDbRepo,userRepository);




            currentOrHistory.addValueChangeListener(event -> {

                if (event.getValue().equals("do rozpatrzenia")) {
                    addTableWithPaginationWithButtons(page,size,requestsRepo,requestStatusRepo,entryDyzurDbRepo,userRepository);
                }
                if (event.getValue().equals("rozpatrzone")) {
                    addTableWithPagination(page,size,requestsRepo,requestStatusRepo,entryDyzurDbRepo,userRepository);
                }
            });







        }
    }
    public void addTableWithPaginationWithButtons(int page,int size,RequestsRepo requestsRepo,RequestStatusRepo requestStatusRepo,
                                                  EntryDyzurDbRepo entryDyzurDbRepo, UserRepository userRepository) {

        removeAll();
        add(new H2("Przychodzące propozycje zamiany dyżurów"));
        add(currentOrHistory);
        add(horizontalLayout);

        Page<Requests> requestsFound = requestsRepo.findAllReceived(true, loggedInUserDetails.getId(), PageRequest.of(page, size));
        totalPages=requestsFound.getTotalPages();

        currentPage.setText(page + 1 + " z " + totalPages);
        List<RequestStatus> requestStatuses = requestStatusRepo.findAll();


        customRequestViews = new ArrayList<>();
        customRequestViews.clear();
        for (Requests requests1 : requestsFound) {
            customRequestViews.add(new CustomRequestView(requests1.getRequestId(),requests1.getDescription(), requests1.getRequestTime(),
                    requestStatuses.get(requests1.getStatus() - 1).getStateName(), requests1.getUserInit().getFirstName() + " " + requests1.getUserInit().getLastName(),
                    requests1.getUserTarget().getFirstName() + " " + requests1.getUserTarget().getLastName(), requests1.getEntryTarget().getTitle(),
                    requests1.getEntryTarget().getStartTime(), requests1.getEntryInitial(), requests1.getEntryTarget(),requests1.getUserInit(),requests1.getUserTarget()
            ));
        }
        if (!customRequestViews.isEmpty()) {

            grid = new Grid<>();
            grid.setItems(customRequestViews);
            grid.setColumnReorderingAllowed(true);

            grid.addColumn(CustomRequestView::getRequestTime).setHeader("Czas zgłoszenia").setAutoWidth(true);
            grid.addColumn(CustomRequestView::getStatus).setHeader("Status").setAutoWidth(true);
            grid.addColumn(CustomRequestView::getInitUserName).setHeader("Zgłoszenie wysłał").setAutoWidth(true);
            grid.addColumn(CustomRequestView::getTargetEntryName).setHeader("Dotyczy dyżuru").setAutoWidth(true);
            grid.addColumn(CustomRequestView::getTargetEntryTime).setHeader("Odbywającego się").setAutoWidth(true);

            grid.setHeight("490px");
            grid.addComponentColumn(customRequestView -> {
                Button button = new Button("Akceptuj");
                button.addClickListener(event1 -> {
                    EntryDyzurDb initEntry = entryDyzurDbRepo.findByID(customRequestView.getInitEntry().getId());

                    EntryDyzurDb targetEntry = entryDyzurDbRepo.findByID(customRequestView.getTargetEntry().getId());
                                Set<User> targetEntryUsers = targetEntry.getUsers();
                    User initUser = userRepository.findByEmail(customRequestView.getInitUser().getEmail());
                    User targetUser = userRepository.findByEmail(customRequestView.getTargetUser().getEmail());


                    initEntry.getUsers().removeIf(user ->
                            user.getId().equals(initUser.getId())
                    );
                    initEntry.getUsers().add(targetUser);
                    entryDyzurDbRepo.save(initEntry);

                    targetEntry.getUsers().removeIf(user ->
                            user.getId().equals(targetUser.getId())
                    );
                    targetEntry.getUsers().add(initUser);
                    entryDyzurDbRepo.save(targetEntry);



                    Requests requestsToUpdate = requestsRepo.findWithId(customRequestView.getRequestId());
                    requestsToUpdate.setActive(false);
                    requestsToUpdate.setStatus(2);
                    requestsRepo.save(requestsToUpdate);

                    currentOrHistory.setValue("rozpatrzone");


                });

                return button;
            });
            grid.addComponentColumn(customRequestView -> {
                Button button = new Button("Odmów");
                button.addClickListener(event2 -> {

                    Requests requestsToUpdate = requestsRepo.findWithId(customRequestView.getRequestId());
                    requestsToUpdate.setActive(false);
                    requestsToUpdate.setStatus(3);
                    requestsRepo.save(requestsToUpdate);

                    currentOrHistory.setValue("rozpatrzone");

                });

                return button;
            });
            grid.setItemDetailsRenderer(new ComponentRenderer<>(customRequestView -> {
                VerticalLayout verticalLayout = new VerticalLayout();
                verticalLayout.add(new H5("Nazwa dyżuru nadawcy:    " + customRequestView.getInitEntry().getTitle()));
                verticalLayout.add(new H5("Data rozpoczęcia:    " + customRequestView.getInitEntry().getStartTime().format(formatter)));
                verticalLayout.add(new H5("Data zakończenia:    " + customRequestView.getInitEntry().getEndTime().format(formatter)));
                verticalLayout.add(new H5("Miejsce dyżuru:    "+customRequestView.getInitEntry().getHospital().getName()));
                verticalLayout.add(new H5("Oddział:    "+customRequestView.getInitEntry().getHospitalDepartment().getDepartment()));

                return verticalLayout;

            }));


            add(grid);

        }
        else add(new H3("Brak wpisów"));
    }

    public void addTableWithPagination(int page,int size,RequestsRepo requestsRepo,RequestStatusRepo requestStatusRepo,
                                       EntryDyzurDbRepo entryDyzurDbRepo, UserRepository userRepository) {

        removeAll();
        add(new H2("Przychodzące propozycje zamiany dyżurów"));
        add(currentOrHistory);
        add(horizontalLayout);

        Page<Requests> requestsFound = requestsRepo.findAllReceived(false, loggedInUserDetails.getId(),PageRequest.of(page, size));
        totalPages=requestsFound.getTotalPages();

        currentPage.setText(page + 1 + " z " + totalPages);
        List<RequestStatus> requestStatuses = requestStatusRepo.findAll();


        customRequestViews = new ArrayList<>();
        customRequestViews.clear();
        for (Requests requests1 : requestsFound) {
            customRequestViews.add(new CustomRequestView(requests1.getRequestId(),requests1.getDescription(), requests1.getRequestTime(),
                    requestStatuses.get(requests1.getStatus() - 1).getStateName(), requests1.getUserInit().getFirstName() + " " + requests1.getUserInit().getLastName(),
                    requests1.getUserTarget().getFirstName() + " " + requests1.getUserTarget().getLastName(), requests1.getEntryTarget().getTitle(),
                    requests1.getEntryTarget().getStartTime(), requests1.getEntryInitial(), requests1.getEntryTarget(),requests1.getUserInit(),requests1.getUserTarget()
            ));
        }
        if (!customRequestViews.isEmpty()) {

            grid = new Grid<>();
            grid.setItems(customRequestViews);
            grid.setColumnReorderingAllowed(true);

            grid.addColumn(CustomRequestView::getRequestTime).setHeader("Czas zgłoszenia").setAutoWidth(true);
            grid.addColumn(CustomRequestView::getStatus).setHeader("Status").setAutoWidth(true);
            grid.addColumn(CustomRequestView::getInitUserName).setHeader("Zgłoszenie wysłał(a)").setAutoWidth(true);
            grid.addColumn(CustomRequestView::getTargetEntryName).setHeader("Dotyczy dyżuru").setAutoWidth(true);
            grid.addColumn(CustomRequestView::getTargetEntryTime).setHeader("Odbywającego się").setAutoWidth(true);

            grid.setHeight("490px");

            grid.setItemDetailsRenderer(new ComponentRenderer<>(customRequestView -> {
                VerticalLayout verticalLayout = new VerticalLayout();
                verticalLayout.add(new H5("Nazwa dyżuru nadawcy:    " + customRequestView.getInitEntry().getTitle()));
                verticalLayout.add(new H5("Data rozpoczęcia:    " + customRequestView.getInitEntry().getStartTime().format(formatter)));
                verticalLayout.add(new H5("Data zakończenia:    " + customRequestView.getInitEntry().getEndTime().format(formatter)));
                verticalLayout.add(new H5("Miejsce dyżuru:    "+customRequestView.getInitEntry().getHospital().getName()));
                verticalLayout.add(new H5("Oddział:    "+customRequestView.getInitEntry().getHospitalDepartment().getDepartment()));

                return verticalLayout;

            }));


            add(grid);

        }
        else add(new H3("Brak wpisów"));
    }
}
