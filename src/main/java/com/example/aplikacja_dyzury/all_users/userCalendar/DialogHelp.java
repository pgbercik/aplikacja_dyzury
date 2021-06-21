package com.example.aplikacja_dyzury.all_users.userCalendar;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class DialogHelp extends Dialog {

    public DialogHelp() {
        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);
        setWidth("800px");


        VerticalLayout layout = new VerticalLayout();
        layout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.STRETCH);
//        layout.setSizeFull();
        layout.add(new H3("Pomoc"));
        add(layout);
        Span span = new Span("Dyżury, na które użytkownik jest zarejestrowany oznaczono kolorem niebieskim.\n" +
                "Kolorem szarym oznaczono te, na które użytkownik się jeszcze nie zarejestrował." );
        Span span1 = new Span("");
        Span span2 = new Span("Przycisk Eksport umożliwia eksportowanie dyżurów do pliku CSV, który można następnie zaimportować do kalendarza firmy Google. Użytkownik definiuje okres czasu. Przeszukiwane są dyżury ze wszystkich oddziałów i wybierane są te, na które użytkownik się zapisał.");
        add(span,span1,span2);


        Button btnCloseWindow = new Button("Zamknij", e -> {
            close();
        });
        btnCloseWindow.getElement().getThemeList().add("tertiary");
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(btnCloseWindow);
        add(horizontalLayout);
    }
}
