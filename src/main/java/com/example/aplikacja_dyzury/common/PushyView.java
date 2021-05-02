//package com.example.aplikacja_dyzury.common;
//
//import com.example.aplikacja_dyzury.navAndThemes.NonRegisteredMenuBar;
//import com.vaadin.flow.component.AttachEvent;
//import com.vaadin.flow.component.ClientCallable;
//import com.vaadin.flow.component.DetachEvent;
//import com.vaadin.flow.component.UI;
//import com.vaadin.flow.component.orderedlayout.VerticalLayout;
//import com.vaadin.flow.component.textfield.TextField;
//import com.vaadin.flow.router.PageTitle;
//import com.vaadin.flow.router.Route;
//import com.vaadin.flow.server.VaadinSession;
//
//
//@PageTitle("Pushy View")
//@Route(value = "eeeeeeeeeeeee", layout = NonRegisteredMenuBar.class)
//public class PushyView extends VerticalLayout {
//
////    public PushyView() {}
//    private BackgroundUpdaterThread thread;
//
//    public TextField textField;
//
//    public PushyView() {
//
//
//        textField = new TextField("");
//        add(textField);
////        Button btn = new Button("zamknij sesję");
////        add(btn);
////        btn.addClickListener(buttonClickEvent -> {
////            VaadinSession.getCurrent().close();
////            Notification.show("AAA", 1000, Notification.Position.MIDDLE);
////        });
//
//        //czas trwania sesji ustawiamy na 60 sekund
//        VaadinSession.getCurrent().getSession().setMaxInactiveInterval(60);
//
//        //skrypt JS sprawdzający czy user nie zmienił zakładki
//        UI.getCurrent().getPage().executeJs("function closeListener() { $0.$server.windowClosed(); } " +
//                "window.addEventListener('beforeunload', closeListener); " +
//                "window.addEventListener('unload', closeListener);", getElement());
//    }
//
//    //listener od skryputu sprawdzającego czy user nie zmienił zakładki
//    @ClientCallable
//    public void windowClosed() {
//        cleanUpAfterThread();
//        System.out.println("Sesja wygasła");
//    }
//
//    @Override
//    protected void onAttach(AttachEvent attachEvent) {
//
//        // urchamiamy wątek
//        thread = new BackgroundUpdaterThread(attachEvent.getUI(), this);
//        thread.start();
//    }
//
//    @Override
//    protected void onDetach(DetachEvent detachEvent) {
//        cleanUpAfterThread();
//    }
//
//    /**
//     * sprzątamy po wątku.
//     */
//    public void cleanUpAfterThread() {
//        if (thread != null) {
//            if (thread.isAlive()) {
//                thread.interrupt();
//                thread = null;
//                System.out.println("Wątek w tle się zakończył");
//            }
//        }
//    }
//
//
//}