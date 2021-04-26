package com.example.aplikacja_dyzury.user.userCalendar;

import com.example.aplikacja_dyzury.DataModelAndRepo.*;
import com.example.aplikacja_dyzury.FindUserData;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.dom.ThemeList;

import org.vaadin.stefan.fullcalendar.Entry;
import org.vaadin.stefan.fullcalendar.FullCalendar;
import org.vaadin.stefan.fullcalendar.Timezone;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DialogAddEditEvent extends Dialog {
    private Binder<Entry> binder;
    private EntryDyzurDbRepo entryDyzurDbRepo;




    //tymczasowo przechowuje wartości pól formularza z momentu odpalenia okna bo po edycji pól binder automatycznie aktualizuje entry
    EntryDyzurDb entryDyzurDbTemporaryForEditONly;

    //uuid entry potrzebne przy aktualizacji
    private String id;



//    private static final String[] COLORS = {"red", "orange", "blue", "green", "gray"};
//    private static final String[] COLORSPL = {"czerwony", "pomarańczowy", "niebieski", "zielony", "szary"};


    //draggedDroppedEvent = true jeśli przeciągamy myszką wydarzenie, w przeciwnym wypadku draggedDroppedEvent=false
    public DialogAddEditEvent(FullCalendar calendar, Entry entry, boolean newInstance, EntryDyzurDbRepo entryDyzurDbRepo, String id,
                              HospitalRepo hospitalRepo, HospitalDepartmentRepo hospitalDepartmentRepo, UserRepository userRepository,
                              String chosenView, H4 currentlyChosenTimeSpan,Long hospitalId,Long hospitalIdDept,
                              LocalDate chosenDateTime, CalendarDataProvider calendarDataProvider,RequestsRepo requestsRepo) {
        this.entryDyzurDbRepo = entryDyzurDbRepo;
        this.id=id;

        //pobieramy wartości pól formularza przed modyfikacją

        this.entryDyzurDbTemporaryForEditONly = new EntryDyzurDb(entry.getId(),entry.getTitle(),
                entry.getStart(),entry.getEnd(),entry.isAllDay(),/*entry.getColor(),*/entry.getDescription(),
                entry.isEditable(),entry.getHospital(),entry.getHospitalDepartment(),entry.getUsers());

        FindUserData findUserData11 = new FindUserData();

        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);
        setWidth("1100px");
//        setHeight("750px");

        VerticalLayout layout = new VerticalLayout();
        layout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.STRETCH);
        layout.setSizeFull();

        TextField fieldTitle = new TextField("Nazwa wydarzenia");
//        fieldTitle.setWidth("650px");
        fieldTitle.focus();


        //w klasie Entry w setColor (od lini 364.) jest kowersja z polskich nazw na angielskie
//        ComboBox<String> fieldColor = new ComboBox<>("Kolor zaznaczenia",COLORSPL);

        TextArea fieldDescription = new TextArea("Opis");

//        HorizontalLayout horizontalLayout = new HorizontalLayout();
//        horizontalLayout.add(fieldTitle/*,fieldColor*/);

        layout.add(fieldTitle, fieldDescription);

        CustomDateTimePicker fieldStart = new CustomDateTimePicker("Początek");
        CustomDateTimePicker fieldEnd = new CustomDateTimePicker("Koniec");


        ComboBox<Hospital> fieldHospitalName = new ComboBox<>("Nazwa szpitala");
        fieldHospitalName.setItems(hospitalRepo.findAll());
        fieldHospitalName.setItemLabelGenerator(Hospital::getName);


        ComboBox<HospitalDepartment> fieldHospitalDepartment = new ComboBox<>("Oddział");
        fieldHospitalDepartment.setItemLabelGenerator(HospitalDepartment::getDepartment);
        fieldHospitalDepartment.setEnabled(false);


        fieldHospitalName.addValueChangeListener(event -> {
            List<HospitalDepartment> itemList = hospitalDepartmentRepo.findDepartmentByHospitalId(event.getValue().getId());
            fieldHospitalDepartment.setItems(itemList);
            fieldHospitalDepartment.setEnabled(true);
        });

//        fieldHospitalName.setItems("szpital1","szpital2");

//        boolean allDay = entry.isAllDay();

//        fieldAllDay.addValueChangeListener(event -> {
//            fieldStart.setDateOnly(event.getValue());
//            fieldEnd.setDateOnly(event.getValue());
//
//        });

        HorizontalLayout horizontalLayout1 = new HorizontalLayout();
        horizontalLayout1.add(fieldStart,fieldEnd);

        layout.add(horizontalLayout1,/*fieldAllDay, */fieldHospitalName, fieldHospitalDepartment);

        binder = new Binder<>(Entry.class);
        binder.forField(fieldTitle)
                .asRequired("Pole wymagane")
                .bind(Entry::getTitle, Entry::setTitle);
//        binder.forField(fieldColor)
//                .asRequired("Pole wymagane")
//                .bind( Entry::getColor, Entry::setColor);
        binder.forField(fieldDescription)
                .asRequired("Pole wymagane")
                .bind(Entry::getDescription, Entry::setDescription);
        Timezone timezone = calendar.getTimezone();
        binder.forField(fieldStart)
                .asRequired("Pole wymagane")
                .bind( e -> e.getStart(timezone), (e, start) -> e.setStart(start, timezone));
        binder.forField(fieldEnd)
                .asRequired("Pole wymagane")
                .bind( e -> e.getEnd(timezone), (e, end) -> e.setEnd(end, timezone));
        binder.forField(fieldHospitalName)
                .asRequired("Pole wymagane")
                .bind(Entry::getHospital,Entry::setHospital);
        binder.forField(fieldHospitalDepartment)
                .asRequired("Pole wymagane")
                .bind(Entry::getHospitalDepartment,Entry::setHospitalDepartment);


        binder.setBean(entry);



        HorizontalLayout buttons = new HorizontalLayout();
        Button buttonSave;
        if (newInstance) {
            buttonSave = new Button("Utwórz dyżur", e -> {
                if (binder.validate().isOk()) {



                    EntryDyzurDb entryDyzurDb = new EntryDyzurDb(entry.getId(),entry.getTitle(),entry.getStart(),
                            entry.getEnd(),entry.isAllDay(),/*entry.getColor(),*/entry.getDescription(),entry.isEditable(),
                            entry.getHospital(),entry.getHospitalDepartment(),entry.getUsers());

                    if (!verifyDateTimesOverlapping(entryDyzurDb.getStartTime(), entryDyzurDb.getEndTime(), entry)) {

                        //sprawdzamy czy data dyżur trwa przynamnjmniej 5 minut i czy nie zaczyna się później niż kończy itd.
                        if (verifyDates(entryDyzurDb.getStartTime(),entryDyzurDb.getEndTime(),entry, id)) {
                            System.out.println("result true");
                            entry.setColor("Gray");
                            calendar.addEntry(entry);

                            entryDyzurDbRepo.save(entryDyzurDb);
                            close();
                        }

                    } else {
                        Notification.show("Okres trwania dyżuru pokrywa się z innym dyżurem z tego samego dnia", 3000, Notification.Position.MIDDLE);
                    }




                }
            });
        } else {
            buttonSave = new Button("Zapisz zmiany", e -> {
                binder.validate();
                if (binder.isValid()) {
//                    System.out.println(entry.getId() +"id z bazy pobrane właśnie AAA ");
//                    calendar.updateEntry(entry);
//                    System.out.println(entry.toString());


                    EntryDyzurDb entryDyzurDb = entryDyzurDbRepo.findByID(id);

                    System.out.println("z entrydyzurdb"+entryDyzurDb.getStartTime()+" | "+entryDyzurDb.getEndTime());

                    entryDyzurDb.setTitle(entry.getTitle());
                    entryDyzurDb.setHospital(entry.getHospital());
                    entryDyzurDb.setStartTime(entry.getStart());
                    entryDyzurDb.setEndTime(entry.getEnd());
                    entryDyzurDb.setAllDay(entry.isAllDay());
//                    entryDyzurDb.setColor(entry.getColor());
                    entryDyzurDb.setDescription(entry.getDescription());
                    entryDyzurDb.setEditable(entry.isEditable());
//                    entryDyzurDb.setColor(entry.getColor());
                    entryDyzurDb.setHospitalDepartment(entry.getHospitalDepartment());
                    entryDyzurDb.setUsers(entry.getUsers());

                    System.out.println("z entry"+entry.getStart()+" | "+entry.getEnd());
                    System.out.println("z entrydyzurdb"+entryDyzurDb.getStartTime()+" | "+entryDyzurDb.getEndTime());
                    if (!verifyDateTimesOverlapping(entryDyzurDb.getStartTime(), entryDyzurDb.getEndTime(), entry)) {

                        //sprawdzamy czy data dyżur trwa przynamnjmniej 5 imut i czy nie zaczyna się później niż kończy itd.
                        if (verifyDates(entryDyzurDb.getStartTime(),entryDyzurDb.getEndTime(),entry,id)) {
                            System.out.println("result true");
//                        calendar.addEntry(entry);
                            entryDyzurDbRepo.save(entryDyzurDb);
//                        entryDyzurDb.
                            calendar.updateEntry(entry);
                            close();
                        }

                    } else {
                        Notification.show("Okres trwania dyżuru pokrywa się z innym dyżurem z tego samego dnia", 3000, Notification.Position.MIDDLE);
                    }






                }
            });
        }
//        buttonSave.addClickListener(e -> close());
        buttons.add(buttonSave);

        Button buttonCancel = new Button("Zamknij okno", e -> {
            calendarDataProvider.addEntriesFromDBWithHospitalNameAndDept(calendar,entryDyzurDbRepo,chosenDateTime,
                    userRepository,chosenView,currentlyChosenTimeSpan,hospitalId,hospitalIdDept);
            close();
//            refreshCalendarPage();
        }
        );
        buttonCancel.getElement().getThemeList().add("tertiary");
        buttons.add(buttonCancel);

        if (!newInstance) {

            Button buttonRemove = new Button("Usuń dyżur", e -> {


                EntryDyzurDb entryDyzurDb = entryDyzurDbRepo.findByID(id);
                System.out.println(entryDyzurDb);
                FindUserData findUserData = new FindUserData();
                String currentlyLoggedInUsersEmail = findUserData.findCurrentlyLoggedInUser();
                User userToAdd = userRepository.findByEmail(currentlyLoggedInUsersEmail);
                Set<User> foundUsers = entryDyzurDb.getUsers();
                System.out.println("rozmiar: " + foundUsers.size());
                FindUserData findUserData1 = new FindUserData();
                if (findUserData1.findFirstUserRoleString().equals("ROLE_ADMIN")) {
                    List<Requests> requestsToRemove=requestsRepo.findRequestsForEntry(entryDyzurDb.getId());
                    requestsRepo.deleteAll(requestsToRemove);
                    entryDyzurDb.getUsers().clear();
                    entryDyzurDbRepo.delete(entryDyzurDb);
                    calendar.removeEntry(entry);
                    close();
                } else {
                    boolean usersIsIn=false;
                    User currentlyLoggedIN = userRepository.findByEmail(currentlyLoggedInUsersEmail);
                    for (User user : foundUsers ) {
                        if (user.getId().equals(currentlyLoggedIN.getId())) usersIsIn=true;
                    }
                    if (foundUsers.size()==0 || (foundUsers.size()<2 && usersIsIn) ) {
                        List<Requests> requestsToRemove=requestsRepo.findRequestsForEntry(entryDyzurDb.getId());
                        requestsRepo.deleteAll(requestsToRemove);
                        entryDyzurDbRepo.delete(entryDyzurDb);
                        entryDyzurDb.getUsers().removeAll(entryDyzurDb.getUsers());
                        calendar.removeEntry(entry);
                        close();
                    } else {
                        Notification.show("Na dyżur zapisali się też inni lekarze. " +
                                "W celu usunięcia skontaktuj się z administratorem.", 3000, Notification.Position.MIDDLE);
                    }
                }


            });
            ThemeList themeList = buttonRemove.getElement().getThemeList();
            themeList.add("error");
            themeList.add("tertiary");
            buttons.add(buttonRemove);

            Button btnReplicate = new Button("Powiel dyżur",event -> {
//                EntryDyzurDb entryDyzurDb=null;
                if (binder.validate().isOk()) {

                    EntryDyzurDb entryDyzurDb = new EntryDyzurDb(entry.getId(),entry.getTitle(),entry.getStart(),
                            entry.getEnd(),entry.isAllDay(),entry.getDescription(),entry.isEditable(),
                            entry.getHospital(),entry.getHospitalDepartment(),entry.getUsers());

//                    System.out.println("entryDyzurDbTemporaryForEditONly.getStartTime() "+entryDyzurDbTemporaryForEditONly.getStartTime() );
//                    System.out.println("entryDyzurDb.getStartTime() "+entryDyzurDb.getStartTime());
//                    System.out.println("entryDyzurDb.getEndTime() "+entryDyzurDb.getEndTime());
//                    System.out.println("entryDyzurDbTemporaryForEditONly.getEndTime() "+entryDyzurDbTemporaryForEditONly.getEndTime() );
                    if (!entryDyzurDbTemporaryForEditONly.getStartTime().equals(entryDyzurDb.getStartTime())
                    && !entryDyzurDbTemporaryForEditONly.getEndTime().equals(entryDyzurDb.getEndTime())) {
                        if (!verifyDateTimesOverlapping(entryDyzurDb.getStartTime(), entryDyzurDb.getEndTime(), entry)) {

                            //sprawdzamy czy dyżur trwa przynamnjmniej 5 minut i czy nie zaczyna się później niż kończy itd.
                            if (verifyDates(entryDyzurDb.getStartTime(),entryDyzurDb.getEndTime(),entry, id)) {

                                System.out.println("result true");
                                entry.setColor("Gray");
                                calendar.addEntry(entry);
                                Set<User> emptyUsers = new HashSet<>();
                                entryDyzurDb.setUsers(emptyUsers);
                                entryDyzurDbRepo.save(entryDyzurDb);
                                close();
                                calendarDataProvider.addEntriesFromDBWithHospitalNameAndDept(calendar,entryDyzurDbRepo,chosenDateTime,
                                        userRepository,chosenView,currentlyChosenTimeSpan,hospitalId,hospitalIdDept);
                                Notification.show("Powielono dyżur bez zapisanych lekarzy",2000, Notification.Position.MIDDLE);

                            }

                        } else {
                            Notification.show("Okres trwania dyżuru pokrywa się z innym dyżurem z tego samego dnia", 3000, Notification.Position.MIDDLE);
                        }
//                        Notification.show("git",200, Notification.Position.MIDDLE);
                    } else {
                        Notification.show("Zmień dane dyżuru przed powielaniem. Przynajmniej  godzinę rozpoczęcia oraz zakończenia.",3000, Notification.Position.MIDDLE);
                    }


                }
//                entryDyzurDb=null;

            });

//            if (findUserData11.findFirstUserRoleString().equals("ROLE_USER")) {
                btnReplicate.getElement().getThemeList().add("tertiary");
                buttons.add(btnReplicate);
//            }

            Button buttonSignInToEntry = new Button("Zapisz się na dyżur",e -> {
                boolean isUSerAlreadyAdded = false;
                FindUserData findUserData = new FindUserData();
                String currentlyLoggedInUsersEmail = findUserData.findCurrentlyLoggedInUser();
                User userToAdd = userRepository.findByEmail(currentlyLoggedInUsersEmail);

                EntryDyzurDb entryDyzurDb = entryDyzurDbRepo.findByID(id);

                Set<User> foundUsers = entryDyzurDb.getUsers();

                int amountOfAddedUsers=0;
                for (User user1 : foundUsers) {
                    if (user1.getEmail().equals(currentlyLoggedInUsersEmail)) isUSerAlreadyAdded=true;
                    amountOfAddedUsers+=1;
                }

                if (amountOfAddedUsers<8) {
                    if (isUSerAlreadyAdded ) {
                        Notification.show("Użytkownik jest już zapisany.",2000, Notification.Position.MIDDLE);
                    }
                    else {
                        entryDyzurDb.getUsers().add(userToAdd);
                        entryDyzurDbRepo.save(entryDyzurDb);

                        close();

                        calendarDataProvider.addEntriesFromDBWithHospitalNameAndDept(calendar,entryDyzurDbRepo,chosenDateTime,
                                userRepository,chosenView,currentlyChosenTimeSpan,hospitalId,hospitalIdDept);
                    }
                } else {
                    Notification.show("Na dyżur można zapisać maksymalnie "+amountOfAddedUsers+" użytkowników.",2000, Notification.Position.MIDDLE);
                }


            });

            if (findUserData11.findFirstUserRoleString().equals("ROLE_USER")) {
                buttonSignInToEntry.getElement().getThemeList().add("tertiary");
                buttons.add(buttonSignInToEntry);
            }


            Button buttonRemoveFromEntry = new Button("Wypisz się",e->{
                FindUserData findUserData = new FindUserData();
                String currentlyLoggedInUsersEmail = findUserData.findCurrentlyLoggedInUser();
                User userToRemove = userRepository.findByEmail(currentlyLoggedInUsersEmail);
                System.out.println("user do wycięcia: "+userToRemove);
                EntryDyzurDb entryDyzurDb = entryDyzurDbRepo.findByID(id);
                System.out.println("dyżur edytowany: "+entryDyzurDb);
                System.out.println("user do wywalenia: "+userToRemove);


                entryDyzurDb.getUsers().removeIf(user -> user.getId().equals(userToRemove.getId()));
                entryDyzurDbRepo.save(entryDyzurDb);



                close();
                calendarDataProvider.addEntriesFromDBWithHospitalNameAndDept(calendar,entryDyzurDbRepo,chosenDateTime,
                        userRepository,chosenView,currentlyChosenTimeSpan,hospitalId,hospitalIdDept);

            });
            if (findUserData11.findFirstUserRoleString().equals("ROLE_USER")) {
                buttonRemoveFromEntry.getElement().getThemeList().add("tertiary");
                buttons.add(buttonRemoveFromEntry);
            }

            Button buttonShowAddedUsers = new Button("Zapisani lekarze",e->{

                new DialogShowUsersForEntry(entry.getId(),entryDyzurDbRepo,id).open();
            });
            buttonShowAddedUsers.getElement().getThemeList().add("tertiary");
            buttons.add(buttonShowAddedUsers);

            Button btnExchangeEntries = new Button("Zamiana dyżuru",e->{


                EntryDyzurDb entryDyzurDb = entryDyzurDbRepo.findByID(id);
//                LocalDateTime currentDateTime = LocalDateTime.now();
//                int aaa = entryDyzurDb.getStartTime().compareTo(currentDateTime);

                Duration dur1 = Duration.between(LocalDateTime.now(),entryDyzurDb.getStartTime());
                Long diffInDays = dur1.toDays();
                System.out.println("różnica w dniach między datą dzisiejszą, a starttime" + diffInDays);
//

                if (diffInDays>=0) {
                    FindUserData findUserData = new FindUserData();
                    User userInit = userRepository.findByEmail(findUserData.findCurrentlyLoggedInUser());
                    new DialogEntryExchange(entryDyzurDb, userInit, entryDyzurDbRepo, requestsRepo).open();
                } else {
                    Notification.show("Nie można dokonywać zamiany dyżurów, które trwają lub już się zakończyły.",3000, Notification.Position.MIDDLE);
                }


            });

            if (findUserData11.findFirstUserRoleString().equals("ROLE_USER")) {
                btnExchangeEntries.getElement().getThemeList().add("tertiary");
                buttons.add(btnExchangeEntries);
            }

            FindUserData findUserData = new FindUserData();
            Long userIdToCheck =  userRepository.findByEmail(findUserData.findCurrentlyLoggedInUser()).getId();
            System.out.println("userIdToCheck "+userIdToCheck);
            EntryDyzurDb entryDyzurDbForBtnExchange = entryDyzurDbRepo.findByID(id);
//
            boolean isUserOnTheList=false;
            Set<User> addedUsers = entryDyzurDbForBtnExchange.getUsers();
            System.out.println("addedUsers "+addedUsers);
            for (User user :addedUsers) {
                System.out.println(user.getFirstName()+" | "+user.getLastName());
                if (user.getId().equals(userIdToCheck)){
                    isUserOnTheList=true;
                    break;
                }
            }

//            if (isUserOnTheList)
            System.out.println("czy jest na liście "+isUserOnTheList);

        }

        add(layout, buttons);
    }

    /**Ta metoda sprawdza czy się godziny dyżurów nakładają. Zakładamy, że pojedynczy dyżur może trwać max 24h.
     * */
    public boolean verifyDateTimesOverlapping(LocalDateTime entryStartDateTime, LocalDateTime entryEndDateTime, Entry entry) {


        //        System.out.println(entry.getStart()+" | "+entry.getEnd()+" | "+entry);
        List<EntryDyzurDb> overLappingEntriesList = entryDyzurDbRepo.findEntriesForOverlappingCheck(entryStartDateTime.toLocalDate(),entry.getHospital(),entry.getHospitalDepartment(),
                entryDyzurDbTemporaryForEditONly.getStartTime(),entryDyzurDbTemporaryForEditONly.getEndTime());
        System.out.println("Kandydaci do overlapping");
        for (EntryDyzurDb entryDyzurDb :overLappingEntriesList) {
            System.out.println(entryDyzurDb.getTitle() +" | "+entryDyzurDb.getStartTime()+" | "+entryDyzurDb.getEndTime()+" | "+ entryDyzurDb.getHospital().getId()+" | "+entryDyzurDb.getHospitalDepartment().getId());
        }


        int i=1;
        boolean overlappingOccurs= false;
        for (EntryDyzurDb entryDyzurDb : overLappingEntriesList) {
            if (overlappingOccurs) {
                break;
            } else {
                System.out.println("wykonanie pętli :"+i);
                i+=1;

                boolean condition1 =  entryStartDateTime.compareTo(entryDyzurDb.getStartTime()) <0 && entryEndDateTime.compareTo(entryDyzurDb.getStartTime()) <0;
                boolean condition2 = entryStartDateTime.compareTo(entryDyzurDb.getStartTime()) <0 && entryEndDateTime.compareTo(entryDyzurDb.getStartTime()) ==0;
                boolean condition3 =  entryStartDateTime.compareTo(entryDyzurDb.getEndTime()) >0 && entryEndDateTime.compareTo(entryDyzurDb.getEndTime()) >0;
                boolean condition4 = entryStartDateTime.compareTo(entryDyzurDb.getEndTime()) ==0 && entryEndDateTime.compareTo(entryDyzurDb.getEndTime()) >0;
//            boolean condition5 =  entryStartDateTime.compareTo(entryDyzurDb.getStartTime()) >0 && entryEndDateTime.compareTo(entryDyzurDb.getEndTime()) <0;

                //te 2 warunki są tylko po to, żeby przepuścić przez tę metodę i pozwolić wyświetlić się warunkom z metody verify
                // (exception 1 - start i stop są róne startowi i stopowi innego dyżuru, a exception2 - start dyżuru = stop dyżuru)
                boolean exception = entryStartDateTime.compareTo(entryDyzurDb.getStartTime()) ==0 && entryEndDateTime.compareTo(entryDyzurDb.getEndTime()) ==0;
                boolean exception2 = entryStartDateTime.compareTo(entryEndDateTime) ==0;


                System.out.println("condition 1: "+condition1);
                System.out.println("condition 2: "+condition2);
                System.out.println("condition 3: "+condition3);
                System.out.println("condition 4: "+condition4);
//            System.out.println("condition 5: "+condition5);
                System.out.println("start i end równe tym w bazie: "+exception);
                System.out.println("entry from list"+entryDyzurDb.getStartTime() +" | "+entryDyzurDb.getEndTime());
                System.out.println("");
                System.out.println("---------------------------------------------------");
                System.out.println("");


                if (!condition1 && !condition2 && !condition3 && !condition4 && !exception && !exception2 ) {
                    overlappingOccurs=true;
                    System.out.println("overlappinng date"+entryDyzurDb.getStartTime() +" | "+entryDyzurDb.getEndTime());
                    //                break;
                }
            }


        }


        System.out.println("Overlapping occurs: "+overlappingOccurs);

        return overlappingOccurs;
    }





    /**Ta metoda sprawdza czynie ma wydarzenia o tych samych godzinach i szpitalu i oddziale i czy np. czs dyżuru ni jest ujemny i czy nie jest krótszy od 5 minut.*/
    public boolean verifyDates(LocalDateTime startTime, LocalDateTime endTime, Entry entry, String id) {
        boolean isDurationCorrect=false; // sprawdza czy podany okres jest poprawny, np. czy start nie jest później od końca
        boolean matchingListHasId=false; //sprawdzamy czy w liście wydarzeń z identycznym start time , end time , szpitalem
        // i oddziałem jest entry, które edytujemy - jeśli jest to pozwalamy je edytować
        boolean overlappingOccurs=true; //jeśli dyżury się nie nakłądają to dajemy false, jeśli się chociaż jeden nakłada to true


        List<EntryDyzurDb> matchingList = entryDyzurDbRepo.findAllMatchingStartEndHospitalHospitalDepartment(
                startTime,endTime,entry.getHospital(),entry.getHospitalDepartment());

        for( EntryDyzurDb entryDyzurDb : matchingList) {
            if (entryDyzurDb.getId().equals(id)) matchingListHasId=true;
        }
//        System.out.println(matchingList.toString());

        //w tych trzech linach liczymy odstępy pomiędzy startem i stopem wydarzenia  wykorzystywane do sprawdzenia czy wydarzenie ma czas dodatni i większy lub równy 5 minut
        Duration dur = Duration.between(startTime, endTime);
        Long diff = dur.toMinutes();

        System.out.println("diff in minutes: "+diff);

        Duration dur1 = Duration.between(startTime,LocalDateTime.now());
        Long diffInDays = dur1.toDays();
        System.out.println("różnica w dniach między datą dzisiejszą, a starttime"+diffInDays);


        if (matchingList.isEmpty() || matchingListHasId) {


            if (diffInDays <= 14) {
                if (diff > 0) {
                    if (diff >= 5) {
                        if (diff <= 1440) {
                            System.out.println("ok");
                            isDurationCorrect = true;
                        } else {
                            Notification.show("Dyżur może trwać maksymalnie 24h.", 2000, Notification.Position.MIDDLE);
                        }
                    } else {
                        System.out.println("Dyżur musi trwać nie mniej minż 5 minut.");
                        Notification.show("Dyżur musi trwać minimum 5 minut.", 2000, Notification.Position.MIDDLE);
                        isDurationCorrect = false;
                    }
                } else {
                    System.out.println("Data zakończenia dyżuru musi być późniejsza od daty rozpoczęcia.");
                    Notification.show("Data zakończenia dyżuru musi być późniejsza od daty rozpoczęcia.", 2000, Notification.Position.MIDDLE);
                    isDurationCorrect = false;
                }
            } else {
                Notification.show("Nie wolno wprowadzać dyżurów z datą wcześniejszą o ponad 2 tygodnie od bieżącej.", 3000, Notification.Position.MIDDLE);
            }

        } else {
            isDurationCorrect = false;
            Notification.show("Na tym oddziale istnieje dyżur o podanych ramach czasowych", 2000, Notification.Position.MIDDLE);
        }
        return isDurationCorrect;
    }

    private void refreshCalendarPage() {
        UI.getCurrent().getPage().reload();
    }


    public static class CustomDateTimePicker extends CustomField<LocalDateTime> {

        private final DatePicker datePicker = new DatePicker();
        private final TimePicker timePicker = new TimePicker();
        private boolean dateOnly;

        CustomDateTimePicker(String label) {
            setLabel(label);
            add(datePicker, timePicker);
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
        }

        @Override
        protected LocalDateTime generateModelValue() {
            final LocalDate date = datePicker.getValue();
            final LocalTime time = timePicker.getValue();

            if (date != null) {
                if (dateOnly || time == null) {
                    return date.atStartOfDay();
                }

                return LocalDateTime.of(date, time);
            }

            return null;

        }

        @Override
        protected void setPresentationValue(
                LocalDateTime newPresentationValue) {
            datePicker.setValue(newPresentationValue != null ? newPresentationValue.toLocalDate() : null);

            timePicker.setValue(newPresentationValue != null ? newPresentationValue.toLocalTime() : null);

        }

        public void setDateOnly(boolean dateOnly) {
            this.dateOnly = dateOnly;
            timePicker.setVisible(!dateOnly);
        }

    }

}
