package org.example;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.Optional;

// Interfaz gráfica del juego.
// Aquí solo se pinta y se atienden eventos.
public class VistaNumberMatch {
    private JuegoNumberMatch juego;
    private GridPane panelTablero;
    private ScrollPane contenedorTablero;
    private Label lblPuntos;
    private Label lblEncontradas;
    private Label lblPendientes;
    private Label lblPistas;
    private Button[][] botones;
    private Casilla primeraSeleccionada;

    public void iniciar(Stage stage) {
        int filas = pedirNumero("Ingresa renglones (mínimo 4):", 4, 10);
        int columnas = pedirNumero("Ingresa columnas (mínimo 10):", 10, 12);

        juego = new JuegoNumberMatch(filas, columnas);

        BorderPane raiz = new BorderPane();
        raiz.setStyle("-fx-background-color: white;");
        raiz.setPadding(new Insets(15));

        Button btnCerrar = new Button("←");
        btnCerrar.setStyle("-fx-background-color: #5f6368; -fx-text-fill: white; -fx-font-size: 20px; -fx-background-radius: 50%; -fx-min-width: 42px; -fx-min-height: 42px; -fx-max-width: 42px; -fx-max-height: 42px;");
        btnCerrar.setOnAction(e -> stage.close());

        Label iconoTrofeo = new Label("🏆");
        iconoTrofeo.setStyle("-fx-font-size: 22px;");

        lblPuntos = new Label("0");
        lblPuntos.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        lblEncontradas = new Label("Encontradas: 0");
        lblEncontradas.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #4f5b62;");

        lblPendientes = new Label("Pendientes: 0");
        lblPendientes.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #4f5b62;");

        lblPistas = new Label("Pistas: 5");
        lblPistas.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #4f5b62;");

        HBox filaPuntos = new HBox(6, iconoTrofeo, lblPuntos);
        filaPuntos.setAlignment(Pos.CENTER);

        VBox panelInfo = new VBox(6, filaPuntos, lblEncontradas, lblPendientes, lblPistas);
        panelInfo.setAlignment(Pos.CENTER);

        Region relleno = new Region();
        relleno.setMinWidth(42);

        BorderPane panelSuperior = new BorderPane();
        panelSuperior.setLeft(btnCerrar);
        panelSuperior.setCenter(panelInfo);
        panelSuperior.setRight(relleno);
        panelSuperior.setPadding(new Insets(0, 0, 15, 0));

        panelTablero = new GridPane();
        panelTablero.setAlignment(Pos.TOP_CENTER);
        panelTablero.setHgap(0);
        panelTablero.setVgap(0);

        contenedorTablero = new ScrollPane(panelTablero);
        contenedorTablero.setFitToWidth(true);
        contenedorTablero.setPannable(true);
        contenedorTablero.setStyle("-fx-background: white; -fx-background-color: white;");
        contenedorTablero.setPrefViewportHeight(330);
        contenedorTablero.setMinViewportHeight(330);
        contenedorTablero.setMaxHeight(360);

        Button btnAgregar = new Button("+");
        btnAgregar.setStyle("-fx-background-color: #1e88e5; -fx-text-fill: white; -fx-font-size: 34px; -fx-font-weight: bold; -fx-background-radius: 50%; -fx-min-width: 70px; -fx-min-height: 70px; -fx-max-width: 70px; -fx-max-height: 70px;");

        Button btnPista = new Button("💡");
        btnPista.setStyle("-fx-background-color: #4f5b62; -fx-text-fill: white; -fx-font-size: 22px; -fx-background-radius: 50%; -fx-min-width: 62px; -fx-min-height: 62px; -fx-max-width: 62px; -fx-max-height: 62px;");

        Button btnDeshacer = new Button("↶");
        btnDeshacer.setStyle("-fx-background-color: #ff9800; -fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold; -fx-background-radius: 50%; -fx-min-width: 62px; -fx-min-height: 62px; -fx-max-width: 62px; -fx-max-height: 62px;");

        btnAgregar.setOnAction(e -> {
            boolean agregado = juego.agregarNumeros();
            if (!agregado) {
                mostrarMensaje("Tablero lleno", "Ya no se pueden agregar más filas. Límite: " + juego.getTablero().getMaxFilas() + " filas.");
            }
            reconstruirTablero();
            actualizarVista();
        });

        btnPista.setOnAction(e -> mostrarPista());

        btnDeshacer.setOnAction(e -> {
            if (!juego.deshacer()) {
                mostrarMensaje("Deshacer", "No hay movimientos para deshacer.");
            }
            actualizarVista();
        });

        HBox panelInferior = new HBox(30, btnAgregar, btnPista, btnDeshacer);
        panelInferior.setAlignment(Pos.CENTER);
        panelInferior.setPadding(new Insets(15, 0, 10, 0));

        VBox centro = new VBox(15, contenedorTablero, panelInferior);
        centro.setAlignment(Pos.TOP_CENTER);

        raiz.setTop(panelSuperior);
        raiz.setCenter(centro);

        reconstruirTablero();
        actualizarVista();

        Scene escena = new Scene(raiz, 760, 620);
        stage.setTitle("Number Match");
        stage.setScene(escena);
        stage.setMinWidth(720);
        stage.setMinHeight(620);
        stage.show();
    }

    private int pedirNumero(String mensaje, int minimo, int maximo) {
        while (true) {
            TextInputDialog dialogo = new TextInputDialog();
            dialogo.setTitle("Configuración");
            dialogo.setHeaderText(null);
            dialogo.setContentText(mensaje + " Rango: " + minimo + " a " + maximo);

            Optional<String> resultado = dialogo.showAndWait();
            if (resultado.isEmpty()) {
                System.exit(0);
            }

            try {
                int valor = Integer.parseInt(resultado.get());
                if (valor >= minimo && valor <= maximo) {
                    return valor;
                }
                mostrarMensaje("Dato inválido", "Ingresa un valor entre " + minimo + " y " + maximo + ".");
            } catch (NumberFormatException e) {
                mostrarMensaje("Dato inválido", "Debes escribir un número entero.");
            }
        }
    }

    private void reconstruirTablero() {
        int filas = juego.getTablero().getFilas();
        int columnas = juego.getTablero().getColumnas();

        panelTablero.getChildren().clear();
        botones = new Button[filas][columnas];

        for (int fila = 0; fila < filas; fila++) {
            for (int columna = 0; columna < columnas; columna++) {
                Button boton = new Button();
                boton.setPrefSize(56, 56);
                boton.setMinSize(56, 56);
                boton.setMaxSize(56, 56);
                boton.setStyle("-fx-background-color: white; -fx-border-color: #ebebeb; -fx-border-width: 1; -fx-font-size: 20px; -fx-text-fill: black;");

                final int f = fila;
                final int c = columna;
                boton.setOnAction(e -> manejarSeleccion(f, c));

                botones[fila][columna] = boton;
                panelTablero.add(boton, columna, fila);
            }
        }
    }

    private void manejarSeleccion(int fila, int columna) {
        Casilla actual = juego.getTablero().getCasilla(fila, columna);

        if (actual == null || !actual.isActiva()) {
            return;
        }

        if (primeraSeleccionada == null) {
            primeraSeleccionada = actual;
            actual.setSeleccionada(true);
            actualizarVista();
            return;
        }

        if (primeraSeleccionada == actual) {
            actual.setSeleccionada(false);
            primeraSeleccionada = null;
            actualizarVista();
            return;
        }

        boolean exito = juego.seleccionarPar(primeraSeleccionada, actual);

        primeraSeleccionada.setSeleccionada(false);
        actual.setSeleccionada(false);
        primeraSeleccionada = null;

        actualizarVista();

        if (!exito) {
            mostrarMensaje("Movimiento inválido", "Las casillas seleccionadas no forman una concordancia válida.");
        }

        if (juego.juegoTerminado()) {
            mostrarMensaje("Fin del juego", "Ya no hay más movimientos posibles.");
        }
    }

    private void mostrarPista() {
        Casilla[] pista = juego.darPista();
        actualizarVista();

        if (pista == null) {
            if (juego.getPistasRestantes() <= 0) {
                mostrarMensaje("Pistas", "Ya no quedan pistas disponibles.");
            } else {
                mostrarMensaje("Pistas", "No se encontró una jugada válida.");
            }
            return;
        }

        botones[pista[0].getFila()][pista[0].getColumna()]
                .setStyle("-fx-background-color: #42a5f5; -fx-border-color: #ebebeb; -fx-border-width: 1; -fx-font-size: 20px; -fx-text-fill: white;");
        botones[pista[1].getFila()][pista[1].getColumna()]
                .setStyle("-fx-background-color: #42a5f5; -fx-border-color: #ebebeb; -fx-border-width: 1; -fx-font-size: 20px; -fx-text-fill: white;");
    }

    private void actualizarVista() {
        Tablero tablero = juego.getTablero();

        for (int fila = 0; fila < tablero.getFilas(); fila++) {
            for (int columna = 0; columna < tablero.getColumnas(); columna++) {
                Casilla casilla = tablero.getCasilla(fila, columna);
                Button boton = botones[fila][columna];

                if (casilla == null) {
                    continue;
                }

                if (casilla.isActiva()) {
                    boton.setDisable(false);
                    boton.setText(String.valueOf(casilla.getValor()));

                    if (casilla.isSeleccionada()) {
                        boton.setStyle("-fx-background-color: #42a5f5; -fx-border-color: #ebebeb; -fx-border-width: 1; -fx-font-size: 20px; -fx-text-fill: white;");
                    } else {
                        boton.setStyle("-fx-background-color: white; -fx-border-color: #ebebeb; -fx-border-width: 1; -fx-font-size: 20px; -fx-text-fill: black;");
                    }
                } else {
                    if (casilla.getValor() == 0) {
                        boton.setText("");
                    } else {
                        boton.setText(String.valueOf(casilla.getValor()));
                    }
                    boton.setDisable(true);
                    boton.setStyle("-fx-background-color: white; -fx-border-color: #ebebeb; -fx-border-width: 1; -fx-font-size: 20px; -fx-text-fill: #d9d9d9;");
                }
            }
        }

        lblPuntos.setText(String.valueOf(juego.getPuntos()));
        lblEncontradas.setText("Encontradas: " + juego.getConcordanciasEncontradas());
        lblPendientes.setText("Pendientes: " + juego.getConcordanciasPendientes());
        lblPistas.setText("Pistas: " + juego.getPistasRestantes());
    }

    private void mostrarMensaje(String titulo, String contenido) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }
}