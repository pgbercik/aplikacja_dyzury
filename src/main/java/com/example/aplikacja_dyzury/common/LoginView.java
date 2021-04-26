package com.example.aplikacja_dyzury.common;


import com.example.aplikacja_dyzury.navAndThemes.NonRegisteredMenuBar;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.Collections;

@Tag("sa-login-view")
@Route(value = LoginView.ROUTE,layout = NonRegisteredMenuBar.class)
@PageTitle("Logowanie do E-terminarz")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

	public static final String ROUTE = "login";

	private LoginForm login = new LoginForm();

	public LoginView() {
		login.setAction("login");
		login.setForgotPasswordButtonVisible(false);

//		dodajemy panel logowania
		getElement().appendChild(login.getElement());

//				polskie napisy
		login.setI18n(createPolishI18n());


	}

	//	private LoginOverlay login = new LoginOverlay();

//	public LoginView(){
//		login.setAction("login");
//		login.setOpened(true);
//		login.setForgotPasswordButtonVisible(false);
//
//
//////		polskie napisy
////		login.setI18n(createPolishI18n());
//
//		//dodajemu panel logowania
//		getElement().appendChild(login.getElement());
//
//	}

//	ta metoda się uruchamia przed wpuszczeniem usera na stronę
	@Override
	public void beforeEnter(BeforeEnterEvent event) {

		// jeśli logowanie się nie powiedzie to login.setError(true); wywołuje  pokazanie tekstu o błędnym logowaniu
		if(!event.getLocation().getQueryParameters().getParameters().getOrDefault("error", Collections.emptyList()).isEmpty()) {
			login.setError(true); //
		}
	}

	//metoda generujące polskie napisy ekranu logowania
	private LoginI18n createPolishI18n() {
		final LoginI18n i18n = LoginI18n.createDefault();

		i18n.setHeader(new LoginI18n.Header());
		i18n.getHeader().setTitle("Panel logowania");
		i18n.getHeader().setDescription("");
		i18n.getForm().setUsername("Adres email");
		i18n.getForm().setTitle("Wprowadź dane logowania");
		i18n.getForm().setSubmit("Zaloguj się");
		i18n.getForm().setPassword("Hasło");
		i18n.getForm().setForgotPassword("Nie pamiętam hasła");
		i18n.getErrorMessage().setTitle("Nazwa użytkownika lub hasło są nieprawidłowe.");
		i18n.getErrorMessage()
				.setMessage("Sprawdż poprawność wprowadzonych danych i spróbuj ponownie.");
		i18n.setAdditionalInformation("");
		return i18n;
	}


}