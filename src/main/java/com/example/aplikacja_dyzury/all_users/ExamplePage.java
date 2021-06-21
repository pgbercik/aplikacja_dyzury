package com.example.aplikacja_dyzury.all_users;

import com.example.aplikacja_dyzury.nav_and_themes.NonRegisteredMenuBar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "example", layout = NonRegisteredMenuBar.class)
@CssImport("./styles/shared-styles.css")
public class ExamplePage extends HorizontalLayout {

    public ExamplePage() {
        addClassName("content");

        String aaa = " QW";
        Button b1 = new Button("Button1"+aaa);
        Button b2 = new Button("Button2"+aaa);
        Button b3 = new Button("Button3"+aaa);
        Button b4 = new Button("Button4"+aaa);
        Button b5 = new Button("Button5"+aaa);
        Button b6 = new Button("Button6"+aaa);
        Button b7 = new Button("Button7"+aaa);
        Button b8 = new Button("Button8"+aaa);
        Button b9 = new Button("Button9"+aaa);

        add(b1, b2, b3, b4, b5, b6, b7, b8, b9);
        HorizontalLayout h = new HorizontalLayout();
    }
}
