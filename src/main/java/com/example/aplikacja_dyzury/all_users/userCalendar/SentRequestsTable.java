package com.example.aplikacja_dyzury.all_users.userCalendar;


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
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
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
@Route(value = "oldSentRequests", layout = RegisteredMenuBar.class)

public class SentRequestsTable extends VerticalLayout {

    private List<CustomRequestView> customRequestViews;
    private Grid<CustomRequestView> grid;
    private Button btnNextPage, btnPreviousPage;
    private  int page=0;
    private int size=5;
    private HorizontalLayout horizontalLayout;
    private int totalPages=0;
    private User loggedInUserDetails;
    private DateTimeFormatter formatter;

//    FindUserData findUserData = new FindUserData();


    @Autowired
    public SentRequestsTable(UserRepository userRepository, RequestsRepo requestsRepo, RequestStatusRepo requestStatusRepo, EntryDyzurDbRepo entryDyzurDbRepo) {
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        loggedInUserDetails = userRepository.findByEmail(FindUserData.findCurrentlyLoggedInUser());
        H5 currentPage = new H5();


        add(new H2("Wysłane propozycje zamiany dyżurów"));
        btnNextPage = new Button("Następna strona");
        btnPreviousPage = new Button("Poprzednia strona");

        btnNextPage.addClickListener(event -> {

            if (page<totalPages-1) addTableWithPagination(page+=1,size,requestsRepo,requestStatusRepo);

            currentPage.setText(page+1+" z "+totalPages);
        });
        btnPreviousPage.addClickListener(event -> {

            if (page>0) addTableWithPagination(page-=1,size,requestsRepo,requestStatusRepo);

            currentPage.setText(page+1+" z "+totalPages);
        });
        horizontalLayout = new HorizontalLayout();



        horizontalLayout.add(btnPreviousPage,currentPage,btnNextPage);
        addTableWithPagination(page,size,requestsRepo,requestStatusRepo);
        currentPage.setText(page+1+" z "+totalPages);







    }
    public void addTableWithPagination(int page,int size,RequestsRepo requestsRepo, RequestStatusRepo requestStatusRepo) {
        removeAll();
        add(new H2("Wysłane propozycje zamiany dyżurów"));
        add(horizontalLayout);



        Page<Requests> requestsFound = requestsRepo.findAllSent(loggedInUserDetails.getId(),PageRequest.of(page, size));
        totalPages=requestsFound.getTotalPages();


        List<RequestStatus> requestStatuses = requestStatusRepo.findAll();


        customRequestViews = new ArrayList<>();
        customRequestViews.clear();
        for (Requests requests1 : requestsFound) {
            customRequestViews.add(new CustomRequestView(requests1.getRequestId(), requests1.getDescription(), requests1.getRequestTime(),
                    requestStatuses.get(requests1.getStatus() - 1).getStateName(), requests1.getUserInit().getFirstName() + " " + requests1.getUserInit().getLastName(),
                    requests1.getUserTarget().getFirstName() + " " + requests1.getUserTarget().getLastName(), requests1.getEntryTarget().getTitle(),
                    requests1.getEntryTarget().getStartTime(), requests1.getEntryInitial(), requests1.getEntryTarget(), requests1.getUserInit(), requests1.getUserTarget()
            ));
        }
        if (!customRequestViews.isEmpty()) {

            grid = new Grid<>();
            grid.setItems(customRequestViews);
            grid.setColumnReorderingAllowed(true);

            grid.addColumn(CustomRequestView::getRequestTime).setHeader("Czas zgłoszenia").setAutoWidth(true);
            grid.addColumn(CustomRequestView::getStatus).setHeader("Status").setAutoWidth(true);
            grid.addColumn(CustomRequestView::getTargetUserName).setHeader("Wysłano do").setAutoWidth(true);
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

        } else add(new H3("Brak wpisów"));
    }
}
