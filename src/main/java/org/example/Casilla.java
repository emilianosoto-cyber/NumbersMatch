package org.example;

/**
 * Representa una casilla del tablero.
 */
public class Casilla implements Comparable<Casilla> {
    private int fila;
    private int columna;
    private int valor;
    private boolean activa;
    private boolean seleccionada;

    public Casilla(int fila, int columna, int valor) {
        this.fila = fila;
        this.columna = columna;
        this.valor = valor;
        this.activa = true;
        this.seleccionada = false;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    public int getValor() {
        return valor;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    public boolean isSeleccionada() {
        return seleccionada;
    }

    public void setSeleccionada(boolean seleccionada) {
        this.seleccionada = seleccionada;
    }

    /**
     * Dos casillas concuerdan si son iguales
     * o si su suma es 10.
     */
    @Override
    public int compareTo(Casilla otra) {
        if (otra == null) {
            return -1;
        }
        if (valor == otra.valor || valor + otra.valor == 10) {
            return 0;
        }
        return Integer.compare(valor, otra.valor);
    }
}