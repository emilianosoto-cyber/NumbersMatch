package org.example;

import java.util.Random;

/**
 * Representa el tablero del juego.
 * Las casillas se almacenan en forma lineal.
 */
public class Tablero {
    private int filas;
    private int columnas;
    private ListaSimple<Casilla> casillas;

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

    /**
     * Agrega al final del tablero las casillas activas.
     * Se acomodan en nuevos renglones.
     */
    public void agregarNumerosActivosAlFinal() {
        ListaSimple<Integer> valores = new ListaSimple<>();

        for (int i = 0; i < casillas.tamanio(); i++) {
            Casilla c = casillas.obtener(i);
            if (c != null && c.isActiva()) {
                valores.insertarFinal(c.getValor());
            }
        }

        if (valores.estaVacia()) {
            return;
        }

        int columna = 0;
        int filaNueva = filas;

        for (int i = 0; i < valores.tamanio(); i++) {
            int valor = valores.obtener(i);
            casillas.insertarFinal(new Casilla(filaNueva, columna, valor));
            columna++;
            if (columna == columnas) {
                columna = 0;
                filaNueva++;
            }
        }

        if (columna != 0) {
            while (columna < columnas) {
                Casilla vacia = new Casilla(filaNueva, columna, 0);
                vacia.setActiva(false);
                casillas.insertarFinal(vacia);
                columna++;
            }
            filaNueva++;
        }

        filas = filaNueva;
    }
}