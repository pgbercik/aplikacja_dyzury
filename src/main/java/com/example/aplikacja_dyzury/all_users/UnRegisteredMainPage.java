package com.example.aplikacja_dyzury.all_users;


import com.example.aplikacja_dyzury.nav_and_themes.NonRegisteredMenuBar;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;


@PageTitle("Strona główna")
@Route(value = "loggedOutMainPage", layout = NonRegisteredMenuBar.class)

public class UnRegisteredMainPage extends VerticalLayout {


    @Autowired
    public UnRegisteredMainPage() {
        H1 h1 = new H1("Witamy na stronie E-terminarz");
//        H3 hh = new H3("Dane logowania admina: admin@gmail.com, AdminPass1");
        H3 h32 = new H3("Nowych użytkowników prosimy o utworzenie konta w zakładce Rejestracja. ");
        H3 h31 = new H3("Zarejestrowanych użytkowników zapraszamy do skorzystania z zakładki Logowanie.");

        add(h1,h31,h32);

    }


}
