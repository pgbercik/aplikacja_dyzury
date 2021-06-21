package com.example.aplikacja_dyzury.nav_and_themes;


import com.example.aplikacja_dyzury.data_model.User;
import com.example.aplikacja_dyzury.repository.UserRepository;
import com.example.aplikacja_dyzury.FindUserData;
import com.example.aplikacja_dyzury.admin.adminForms.FormAddHospital;
import com.example.aplikacja_dyzury.admin.adminTables.HospitalTable;
import com.example.aplikacja_dyzury.all_users.MainPage;

import com.example.aplikacja_dyzury.all_users.userCalendar.SentRequestsTable;
import com.example.aplikacja_dyzury.all_users.userCalendar.ShowCalendar;
import com.github.appreciated.app.layout.addons.notification.DefaultNotificationHolder;
import com.github.appreciated.app.layout.component.applayout.LeftLayouts;
import com.github.appreciated.app.layout.component.builder.AppLayoutBuilder;
import com.github.appreciated.app.layout.component.menu.left.builder.LeftAppMenuBuilder;
import com.github.appreciated.app.layout.component.menu.left.items.LeftClickableItem;
import com.github.appreciated.app.layout.component.menu.left.items.LeftNavigationItem;
import com.github.appreciated.app.layout.component.router.AppLayoutRouterLayout;
import com.github.appreciated.app.layout.entity.DefaultBadgeHolder;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.github.appreciated.app.layout.entity.Section.FOOTER;

@Push
@Viewport("width=device-width, minimum-scale=1.0, initial-scale=1.0, user-scalable=yes")
@Theme(value = Lumo.class,variant = Lumo.DARK)
@Component @UIScope // optional but useful; allows access to this instance from views, see View1.
public class RegisteredMenuBar extends AppLayoutRouterLayout<LeftLayouts.LeftResponsive> {
    private DefaultNotificationHolder notifications = new DefaultNotificationHolder();
    private DefaultBadgeHolder badge = new DefaultBadgeHolder(5);

    @Autowired
    public RegisteredMenuBar(UserRepository userRepository) {
//        FindUserData findUserData = new FindUserData();
        System.out.println(FindUserData.findFirstUserRoleString() +"rola w registered menu bar");
        User loggedInUserDetails =  userRepository.findByEmail(FindUserData.findCurrentlyLoggedInUser());

        if (FindUserData.findFirstUserRoleString().equals("ROLE_USER")) {
            init(AppLayoutBuilder.get(LeftLayouts.LeftResponsive.class)
                    .withTitle("E-terminarz"+" - zalogowano jako "
                            + loggedInUserDetails.getDoctorTitle().getType() +" "
                            +loggedInUserDetails.getFirstName()+" "+loggedInUserDetails.getLastName())
                    .withAppMenu(LeftAppMenuBuilder.get()
                            .add(
                                    new LeftNavigationItem("Zgłoszenia przychodzące", VaadinIcon.HOME.create(), MainPage.class),
                                    new LeftNavigationItem("Zgłoszenia wysłane", VaadinIcon.TABLE.create(), SentRequestsTable.class),
                                    new LeftNavigationItem("Zaplanowane dyżury", VaadinIcon.CALENDAR_USER.create(), ShowCalendar.class)

//                                new LeftNavigationItem("Kalendarz", VaadinIcon.CALENDAR_USER.create(), ShowCalendar.class)

                            )
//                            .addTozSection(FOOTER, new LeftClickableItem("Zmień motyw", VaadinIcon.ADJUST.create(),
//                                    clickEvent -> {
//                                        ThemeChanger themeChanger = new ThemeChanger();
//                                        themeChanger.changeTheme();
//                                    }
//                            ))
                            .addToSection(FOOTER, new LeftClickableItem("Wyloguj", VaadinIcon.EXIT.create(),
                                    clickEvent -> UI.getCurrent().getPage().setLocation("logout")
                            ))
                            .build())
                    .build());

        }

        if (FindUserData.findFirstUserRoleString().equals("ROLE_ADMIN")) {
            init(AppLayoutBuilder.get(LeftLayouts.LeftResponsive.class)
                    .withTitle("E-terminarz"+" - zalogowano jako administrator")
                    .withAppMenu(LeftAppMenuBuilder.get()
                            .add(
                                    new LeftNavigationItem("Strona główna", VaadinIcon.HOME.create(), MainPage.class),
                                    new LeftNavigationItem("Dodawanie szpitali", VaadinIcon.PLUS.create(), FormAddHospital.class),
                                    new LeftNavigationItem("Utworzone szpitale", VaadinIcon.TABLE.create(), HospitalTable.class),
                                    new LeftNavigationItem("Zaplanowane dyżury", VaadinIcon.CALENDAR_USER.create(), ShowCalendar.class)
//                                    new LeftNavigationItem("Tabela lekarze", VaadinIcon.TABLE.create(), DoctorTable.class)
//                                new LeftNavigationItem("Kalendarz", VaadinIcon.CALENDAR_USER.create(), ShowCalendar.class)

                            )
//                            .addToSection(FOOTER, new LeftClickableItem("Zmień motyw", VaadinIcon.ADJUST.create(),
//                                    clickEvent -> {
//                                        ThemeChanger themeChanger = new ThemeChanger();
//                                        themeChanger.changeTheme();
//                                    }
//                            ))
                            .addToSection(FOOTER, new LeftClickableItem("Wyloguj", VaadinIcon.EXIT.create(),
                                    clickEvent -> UI.getCurrent().getPage().setLocation("logout")
                            ))
                            .build())
                    .build());

        }

    }


}
