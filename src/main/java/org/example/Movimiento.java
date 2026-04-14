package org.example;

// Guarda un movimiento para la operación deshacer.
public class Movimiento {
    private Casilla casilla1;
    private Casilla casilla2;
    private int puntosAntes;
    private int encontradasAntes;
    private int pendientesAntes;
    private int pistasAntes;

    public Movimiento(Casilla casilla1, Casilla casilla2, int puntosAntes, int encontradasAntes, int pendientesAntes, int pistasAntes) {
        this.casilla1 = casilla1;
        this.casilla2 = casilla2;
        this.puntosAntes = puntosAntes;
        this.encontradasAntes = encontradasAntes;
        this.pendientesAntes = pendientesAntes;
        this.pistasAntes = pistasAntes;
    }

    public Casilla getCasilla1() {
        return casilla1;
    }

    public Casilla getCasilla2() {
        return casilla2;
    }

    public int getPuntosAntes() {
        return puntosAntes;
    }

    public int getEncontradasAntes() {
        return encontradasAntes;
    }

    public int getPendientesAntes() {
        return pendientesAntes;
    }

    public int getPistasAntes() {
        return pistasAntes;
    }
}