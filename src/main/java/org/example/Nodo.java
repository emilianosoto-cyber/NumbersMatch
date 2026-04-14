package org.example;

/**
 * Nodo simple genérico.
 * Sirve como base para la lista simple.
 */
public class Nodo<T> {
    private T info;
    private Nodo<T> sig;

    public Nodo(T info) {
        this.info = info;
        this.sig = null;
    }

    public T getInfo() {
        return info;
    }

    public void setInfo(T info) {
        this.info = info;
    }

    public Nodo<T> getSig() {
        return sig;
    }

    public void setSig(Nodo<T> sig) {
        this.sig = sig;
    }
}