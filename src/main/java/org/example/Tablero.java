package org.example;

import java.util.Random;

// Representa el tablero del juego.
// Las casillas se almacenan en forma lineal usando lista simple.
public class Tablero {
    private int filas;
    private int columnas;
    private ListaSimple<Casilla> casillas;
    private static final int MAX_FILAS = 18;

    public Tablero(int filas, int columnas) {
        this.filas = filas;
        this.columnas = columnas;
        this.casillas = new ListaSimple<>();
        generarInicial();
    }

    private void generarInicial() {
        Random random = new Random();
        for (int fila = 0; fila < filas; fila++) {
            for (int columna = 0; columna < columnas; columna++) {
                casillas.insertarFinal(new Casilla(fila, columna, random.nextInt(9) + 1));
            }
        }
    }

    public Casilla getCasilla(int fila, int columna) {
        if (fila < 0 || fila >= filas || columna < 0 || columna >= columnas) {
            return null;
        }
        int indice = fila * columnas + columna;
        return casillas.obtener(indice);
    }

    public ListaSimple<Casilla> getCasillas() {
        return casillas;
    }

    public int getFilas() {
        return filas;
    }

    public int getColumnas() {
        return columnas;
    }

    public int getTotalCasillas() {
        return casillas.tamanio();
    }

    public int getMaxFilas() {
        return MAX_FILAS;
    }

    public boolean puedeCrecer() {
        return filas < MAX_FILAS;
    }

    // Agrega al final del tablero las casillas activas actuales.
    // Si el tablero ya está al límite, no agrega más.
    public boolean agregarNumerosActivosAlFinal() {
        if (!puedeCrecer()) {
            return false;
        }

        ListaSimple<Integer> valores = new ListaSimple<>();

        for (int i = 0; i < casillas.tamanio(); i++) {
            Casilla c = casillas.obtener(i);
            if (c != null && c.isActiva()) {
                valores.insertarFinal(c.getValor());
            }
        }

        if (valores.estaVacia()) {
            return false;
        }

        int espaciosDisponibles = (MAX_FILAS - filas) * columnas;
        int totalAInsertar = valores.tamanio();

        if (totalAInsertar > espaciosDisponibles) {
            totalAInsertar = espaciosDisponibles;
        }

        int filaNueva = filas;
        int columnaNueva = 0;

        for (int i = 0; i < totalAInsertar; i++) {
            int valor = valores.obtener(i);
            casillas.insertarFinal(new Casilla(filaNueva, columnaNueva, valor));
            columnaNueva++;
            if (columnaNueva == columnas) {
                columnaNueva = 0;
                filaNueva++;
            }
        }

        // Si quedó un renglón incompleto, se rellena con casillas inactivas.
        if (columnaNueva != 0 && filaNueva < MAX_FILAS) {
            while (columnaNueva < columnas) {
                Casilla vacia = new Casilla(filaNueva, columnaNueva, 0);
                vacia.setActiva(false);
                casillas.insertarFinal(vacia);
                columnaNueva++;
            }
            filaNueva++;
        }

        filas = filaNueva;
        return true;
    }
}