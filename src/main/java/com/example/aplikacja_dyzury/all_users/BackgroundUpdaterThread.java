//package com.example.aplikacja_dyzury.common;
//
//import com.vaadin.flow.component.UI;
//
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//
///**
// * Klasa z wątkiem, któy w tle aktualizuje dyżury w kalendarzu. Dzięki temu jeśli iny user doda dyżur to pozostali będą go widzieli bez konieczności odświeżania strony.
// * */
//
//public class BackgroundUpdaterThread extends Thread {
//    private final UI ui;
//    private final PushyView view;
//
//
//    public BackgroundUpdaterThread(UI ui, PushyView view) {
//        this.ui = ui;
//        this.view = view;
//    }
//
//    @Override
//    public void run() {
//        try {
//            while (true) {
//                // co sekundę coś robimy
//                Thread.sleep(1000);
//
//                ui.access(() -> {
//                    String prettyTimeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//                    view.textField.setValue(prettyTimeStamp);
//                    System.out.println(prettyTimeStamp);
//
//                });
//            }
//        } catch (InterruptedException ignored) {
//        }
//    }
//}