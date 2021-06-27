package com.example.aplikacja_dyzury.all_users;

import com.example.aplikacja_dyzury.nav_and_themes.NonRegisteredMenuBar;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.Route;

@Route(value = "example", layout = NonRegisteredMenuBar.class)
@CssImport("./styles/shared-styles.css")
public class ExamplePage extends VerticalLayout {

    public ExamplePage() {
        addClassName("main-container");
        Div div = new Div();
        div.addClassName("buttonsDiv");

        String aaa = " QW";
        Button b1 = new Button("Button1" + aaa);
        Button b2 = new Button("Button2" + aaa);
        Button b3 = new Button("Button3" + aaa);
        Button b4 = new Button("Button4" + aaa);
        Button b5 = new Button("Button5" + aaa);
        Button b6 = new Button("Button6" + aaa);
        Button b7 = new Button("Button7" + aaa);
        Button b8 = new Button("Button8" + aaa);

        div.add(b1, b2, b3, b4, b5, b6, b7, b8);
        add(div);
        TextArea textArea = new TextArea("big text area");
        textArea.setWidthFull();
        textArea.setHeightFull();
        textArea.setValue("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin eget turpis eleifend, blandit felis eu, volutpat est. Vivamus lobortis sit amet felis id lacinia. Aliquam vel gravida libero. Morbi porta consequat eros ac efficitur. Aenean urna justo, lacinia eget lectus sit amet, cursus porta turpis. In hac habitasse platea dictumst. Aenean volutpat dolor et ornare hendrerit. Donec tincidunt vitae felis eget maximus. Duis malesuada, ipsum a finibus tristique, nulla enim sodales odio, ut condimentum erat velit eu turpis. Duis dui tortor, hendrerit quis sagittis eu, tincidunt eu enim. Maecenas a finibus ex. Nulla dolor elit, porta ut venenatis egestas, sagittis at libero. Morbi vel urna ipsum. Ut iaculis nibh ac finibus vulputate.");
        textArea.addClassName("textArea");
        add(textArea);
    }
}
