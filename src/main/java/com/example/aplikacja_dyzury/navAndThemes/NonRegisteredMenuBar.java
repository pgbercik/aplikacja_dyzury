package com.example.aplikacja_dyzury.navAndThemes;

import com.example.aplikacja_dyzury.common.LoginView;
import com.example.aplikacja_dyzury.common.UnRegisteredMainPage;
import com.example.aplikacja_dyzury.common.user_registration_ui.FormAddUser;
import com.github.appreciated.app.layout.addons.notification.DefaultNotificationHolder;
import com.github.appreciated.app.layout.component.applayout.LeftLayouts;
import com.github.appreciated.app.layout.component.builder.AppLayoutBuilder;
import com.github.appreciated.app.layout.component.menu.left.builder.LeftAppMenuBuilder;
import com.github.appreciated.app.layout.component.menu.left.items.LeftNavigationItem;
import com.github.appreciated.app.layout.component.router.AppLayoutRouterLayout;
import com.github.appreciated.app.layout.entity.DefaultBadgeHolder;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.stereotype.Component;

import static com.github.appreciated.app.layout.entity.Section.FOOTER;

@Push
@Viewport("width=device-width, minimum-scale=1.0, initial-scale=1.0, user-scalable=yes")
@Theme(value = Lumo.class,variant = Lumo.DARK)
@Component @UIScope
public class NonRegisteredMenuBar extends AppLayoutRouterLayout<LeftLayouts.LeftResponsive> {
    private DefaultNotificationHolder notifications = new DefaultNotificationHolder();
    private DefaultBadgeHolder badge = new DefaultBadgeHolder(5);

    public NonRegisteredMenuBar() {
        init(AppLayoutBuilder.get(LeftLayouts.LeftResponsive.class)
                .withTitle("E-terminarz")
                .withAppMenu(LeftAppMenuBuilder.get()
                        .add(
                                new LeftNavigationItem("Strona główna", VaadinIcon.HOME.create(), UnRegisteredMainPage.class),
                                new LeftNavigationItem("Logowanie", VaadinIcon.USER.create(), LoginView.class),
                                new LeftNavigationItem("Rejestracja", VaadinIcon.PLUS.create(), FormAddUser.class)
//                                new LeftNavigationItem("PushyView",VaadinIcon.INBOX.create(), PushyView.class)
//                                new LeftNavigationItem("Kalendarz", VaadinIcon.CALENDAR_USER.create(), ShowCalendar.class)
//                                new LeftNavigationItem("Kalendarz", VaadinIcon.ANCHOR.create(), Demo.class)
                        )
//                        .addToSection(FOOTER, new LeftClickableItem("Zmień motyw", VaadinIcon.ADJUST.create(),
//                                clickEvent -> {
//                                    ThemeChanger themeChanger = new ThemeChanger();
//                                    themeChanger.changeTheme();
//                                }
//                        ))
                        .build())
                .build());
    }


}
