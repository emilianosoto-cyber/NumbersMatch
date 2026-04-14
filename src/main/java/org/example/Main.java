package org.example;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Punto de entrada del programa.
 */
public class Main extends Application {
    @Override
    public void start(Stage stage) {
        VistaNumberMatch vista = new VistaNumberMatch();
        vista.iniciar(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}