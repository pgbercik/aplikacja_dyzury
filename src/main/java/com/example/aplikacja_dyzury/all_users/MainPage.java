package com.example.aplikacja_dyzury.all_users;


import com.example.aplikacja_dyzury.data_model.*;
import com.example.aplikacja_dyzury.data_model.custom_pojo.CustomRequestView;
import com.example.aplikacja_dyzury.FindUserData;
import com.example.aplikacja_dyzury.nav_and_themes.RegisteredMenuBar;
import com.example.aplikacja_dyzury.repository.EntryDyzurDbRepo;
import com.example.aplikacja_dyzury.repository.RequestStatusRepo;
import com.example.aplikacja_dyzury.repository.RequestsRepo;
import com.example.aplikacja_dyzury.repository.UserRepository;
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

@Secured({"ROLE_ADMIN", "ROLE_USER"})
@PageTitle("Strona główna")
@Route(value = "", layout = RegisteredMenuBar.class)

public class MainPage extends VerticalLayout {
    private RadioButtonGroup<String> currentOrHistory;
    private List<CustomRequestView> customRequestViews;
    private Grid<CustomRequestView> grid;
    private  int page=0;
    private final int size=5;
    private HorizontalLayout horizontalLayout;
    private int totalPages=0;
    private final User loggedInUserDetails;
    private final DateTimeFormatter formatter;
    private final H5 currentPage;

    private final UserRepository userRepository;
    private final RequestsRepo requestsRepo;
    private final RequestStatusRepo requestStatusRepo;
    private final EntryDyzurDbRepo entryDyzurDbRepo;




    @Autowired
    public MainPage(UserRepository userRepository, RequestsRepo requestsRepo, RequestStatusRepo requestStatusRepo,EntryDyzurDbRepo entryDyzurDbRepo) {

        this.userRepository = userRepository;
        this. requestsRepo = requestsRepo;
        this.requestStatusRepo = requestStatusRepo;
        this.entryDyzurDbRepo = entryDyzurDbRepo;

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

            Button btnNextPage = new Button("Następna strona");
            Button btnPreviousPage = new Button("Poprzednia strona");
            btnNextPage.addClickListener(event -> {

                if (page < totalPages - 1) {
                    if (currentOrHistory.getValue().equals("do rozpatrzenia")) {
                        showRequestsToDecide(page += 1, size);
                    }
                    if (currentOrHistory.getValue().equals("rozpatrzone")) {
                        showRequestsDecided(page += 1, size);
                    }
                }

                currentPage.setText(page + 1 + " z " + totalPages);
            });
            btnPreviousPage.addClickListener(event -> {

                if (page > 0) {
                    if (currentOrHistory.getValue().equals("do rozpatrzenia")) {
                        showRequestsToDecide(page -= 1, size);
                    }
                    if (currentOrHistory.getValue().equals("rozpatrzone")) {
                        showRequestsDecided(page -= 1, size);
                    }
                }

                currentPage.setText(page + 1 + " z " + totalPages);
            });
            horizontalLayout = new HorizontalLayout();



            horizontalLayout.add(btnPreviousPage,currentPage, btnNextPage);

            //wstępnie wyświetlamy te do rozpatrzenia
            //we're showing duty swap requests that have not been accepted or declined yet
            showRequestsToDecide(page,size);




            currentOrHistory.addValueChangeListener(event -> {

                if (event.getValue().equals("do rozpatrzenia")) {
                    showRequestsToDecide(page,size);
                }
                if (event.getValue().equals("rozpatrzone")) {
                    showRequestsDecided(page,size);
                }
            });







        }
    }
    public void showRequestsToDecide(int page, int size) {

        clearUI();

        Page<Requests> requestsFound = requestsRepo.findAllReceived(true, loggedInUserDetails.getId(), PageRequest.of(page, size));
        totalPages=requestsFound.getTotalPages();
                System.out.println("znalezione requesty " + totalPages);
        currentPage.setText(page + 1 + " z " + totalPages);
        List<RequestStatus> requestStatuses = requestStatusRepo.findAll();


        customRequestViews = new ArrayList<>();
        addEntriesToTable(requestsFound, requestStatuses);
        if (!customRequestViews.isEmpty()) {

            addSharedGridConfig();
            grid.addComponentColumn(this::requestAcceptAction);
            grid.addComponentColumn(this::requestDeclineAction);
            grid.setItemDetailsRenderer(new ComponentRenderer<>(this::generateDutyDetailsText));

            add(grid);

        }
        else add(new H3("Brak wpisów"));
    }

    private void addEntriesToTable(Page<Requests> requestsFound, List<RequestStatus> requestStatuses) {
        for (Requests r1 : requestsFound) {
            customRequestViews.add(new CustomRequestView(r1.getRequestId(), r1.getDescription(), r1.getRequestTime(),
                    requestStatuses.get(r1.getStatus() - 1).getStateName(), r1.getUserInit().getFirstName()
                    + " " + r1.getUserInit().getLastName(), r1.getUserTarget().getFirstName()
                    + " " + r1.getUserTarget().getLastName(), r1.getEntryTarget().getTitle(),
                    r1.getEntryTarget().getStartTime(), r1.getEntryInitial(), r1.getEntryTarget(), r1.getUserInit(),
                    r1.getUserTarget()
            ));
        }
    }

    public void showRequestsDecided(int page, int size) {

        clearUI();

        Page<Requests> requestsFound = requestsRepo.findAllReceived(false, loggedInUserDetails.getId(),PageRequest.of(page, size));
        totalPages=requestsFound.getTotalPages();
        System.out.println("znalezione requesty " + totalPages);
        currentPage.setText(page + 1 + " z " + totalPages);
        List<RequestStatus> requestStatuses = requestStatusRepo.findAll();


        customRequestViews = new ArrayList<>();
        addEntriesToTable(requestsFound, requestStatuses);
        if (!customRequestViews.isEmpty()) {

            addSharedGridConfig();
            grid.setItemDetailsRenderer(new ComponentRenderer<>(this::generateDutyDetailsText));
            add(grid);

        }
        else add(new H3("Brak wpisów"));
    }

    private Button requestDeclineAction(CustomRequestView customRequestView) {
        Button button = new Button("Odmów");
        button.addClickListener(event2 -> {

            Requests requestsToUpdate = requestsRepo.findWithId(customRequestView.getRequestId());
            requestsToUpdate.setActive(false);
            requestsToUpdate.setStatus(3);
            requestsRepo.save(requestsToUpdate);

            currentOrHistory.setValue("rozpatrzone");

        });

        return button;
    }

    private Button requestAcceptAction(CustomRequestView customRequestView) {
        Button button = new Button("Akceptuj");
        button.addClickListener(event1 -> {
            EntryDyzurDb initEntry = entryDyzurDbRepo.findByID(customRequestView.getInitEntry().getId());

            EntryDyzurDb targetEntry = entryDyzurDbRepo.findByID(customRequestView.getTargetEntry().getId());
            User initUser = userRepository.findByEmail(customRequestView.getInitUser().getEmail());
            User targetUser = userRepository.findByEmail(customRequestView.getTargetUser().getEmail());
            System.out.println(initUser);
            System.out.println(targetUser);

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
    }




    private VerticalLayout generateDutyDetailsText(CustomRequestView view) {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(new H5("Nazwa dyżuru nadawcy:    " + view.getInitEntry().getTitle()));
        verticalLayout.add(new H5("Data rozpoczęcia:    " + view.getInitEntry().getStartTime().format(formatter)));
        verticalLayout.add(new H5("Data zakończenia:    " + view.getInitEntry().getEndTime().format(formatter)));
        verticalLayout.add(new H5("Miejsce dyżuru:    " + view.getInitEntry().getHospital().getName()));
        verticalLayout.add(new H5("Oddział:    " + view.getInitEntry().getHospitalDepartment().getDepartment()));
        return verticalLayout;
    }

    /**
     * We delete old table and UI elements and add new ones.
     * */
    private void clearUI() {
        removeAll();
        add(new H2("Przychodzące propozycje zamiany dyżurów"));
        add(currentOrHistory);
        add(horizontalLayout);
    }
    /**
     * A part of grid configuration shared between decided requests and not decided requests.
     * */
    private void addSharedGridConfig() {
        grid = new Grid<>();
        grid.setItems(customRequestViews);
        grid.setColumnReorderingAllowed(true);

        grid.addColumn(CustomRequestView::getRequestTime).setHeader("Czas zgłoszenia").setAutoWidth(true);
        grid.addColumn(CustomRequestView::getStatus).setHeader("Status").setAutoWidth(true);
        grid.addColumn(CustomRequestView::getInitUserName).setHeader("Zgłoszenie wysłał(a)").setAutoWidth(true);
        grid.addColumn(CustomRequestView::getTargetEntryName).setHeader("Dotyczy dyżuru").setAutoWidth(true);
        grid.addColumn(CustomRequestView::getTargetEntryTime).setHeader("Odbywającego się").setAutoWidth(true);

        grid.setHeight("490px");
    }
}
