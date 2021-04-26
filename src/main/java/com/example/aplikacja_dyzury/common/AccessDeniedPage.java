package com.example.aplikacja_dyzury.common;


import com.example.aplikacja_dyzury.navAndThemes.RegisteredMenuBar;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;


@Route(value = "accessDenied",layout = RegisteredMenuBar.class)
public class AccessDeniedPage extends VerticalLayout {

    public AccessDeniedPage() {

        Icon logo = new Icon(VaadinIcon.EXCLAMATION_CIRCLE);
        logo.setSize("300px");
        logo.setColor("red");

        add(logo);

        H1 h1 = new H1("ACCESS DENIED!");


        add(h1);

        setAlignItems(Alignment.CENTER);

    }
}
