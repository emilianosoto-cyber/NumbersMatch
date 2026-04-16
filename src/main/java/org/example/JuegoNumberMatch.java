package org.example;

// Contiene toda la lógica del juego.
public class JuegoNumberMatch {
    private Tablero tablero;
    private ListaSimple<Movimiento> historial;
    private int puntos;
    private int concordanciasEncontradas;
    private int concordanciasPendientes;
    private int pistasRestantes;

    public JuegoNumberMatch(int filas, int columnas) {
        tablero = new Tablero(filas, columnas);
        historial = new ListaSimple<>();
        puntos = 0;
        concordanciasEncontradas = 0;
        pistasRestantes = 5;
        concordanciasPendientes = calcularConcordanciasPendientes();
    }

    public Tablero getTablero() {
        return tablero;
    }

    public int getPuntos() {
        return puntos;
    }

    public int getConcordanciasEncontradas() {
        return concordanciasEncontradas;
    }

    public int getConcordanciasPendientes() {
        return concordanciasPendientes;
    }

    public int getPistasRestantes() {
        return pistasRestantes;
    }

    public boolean seleccionarPar(Casilla primera, Casilla segunda) {
        if (primera == null || segunda == null) {
            return false;
        }
        if (primera == segunda) {
            return false;
        }
        if (!primera.isActiva() || !segunda.isActiva()) {
            return false;
        }
        if (primera.compareTo(segunda) != 0) {
            return false;
        }
        if (!esMovimientoValido(primera, segunda)) {
            return false;
        }

        historial.insertarInicio(new Movimiento(primera, segunda, puntos, concordanciasEncontradas, concordanciasPendientes, pistasRestantes));

        primera.setActiva(false);
        segunda.setActiva(false);
        primera.setSeleccionada(false);
        segunda.setSeleccionada(false);

        puntos += primera.getValor() + segunda.getValor();
        concordanciasEncontradas++;
        concordanciasPendientes = calcularConcordanciasPendientes();
        return true;
    }

    // Determina si dos casillas forman un movimiento permitido.
    public boolean esMovimientoValido(Casilla a, Casilla b) {
        if (a == null || b == null) {
            return false;
        }

        if (a.getFila() == b.getFila()) {
            return caminoLibreEnFila(a, b);
        }

        if (a.getColumna() == b.getColumna()) {
            return caminoLibreEnColumna(a, b);
        }

        if (Math.abs(a.getFila() - b.getFila()) == Math.abs(a.getColumna() - b.getColumna())) {
            return caminoLibreEnDiagonal(a, b);
        }

        return caminoLibreLineal(a, b);
    }

    private boolean caminoLibreEnFila(Casilla a, Casilla b) {
        int fila = a.getFila();
        int inicio = Math.min(a.getColumna(), b.getColumna()) + 1;
        int fin = Math.max(a.getColumna(), b.getColumna());

        for (int columna = inicio; columna < fin; columna++) {
            Casilla c = tablero.getCasilla(fila, columna);
            if (c != null && c.isActiva()) {
                return false;
            }
        }
        return true;
    }

    private boolean caminoLibreEnColumna(Casilla a, Casilla b) {
        int columna = a.getColumna();
        int inicio = Math.min(a.getFila(), b.getFila()) + 1;
        int fin = Math.max(a.getFila(), b.getFila());

        for (int fila = inicio; fila < fin; fila++) {
            Casilla c = tablero.getCasilla(fila, columna);
            if (c != null && c.isActiva()) {
                return false;
            }
        }
        return true;
    }

    private boolean caminoLibreEnDiagonal(Casilla a, Casilla b) {
        int pasoFila = a.getFila() < b.getFila() ? 1 : -1;
        int pasoColumna = a.getColumna() < b.getColumna() ? 1 : -1;

        int fila = a.getFila() + pasoFila;
        int columna = a.getColumna() + pasoColumna;

        while (fila != b.getFila() && columna != b.getColumna()) {
            Casilla c = tablero.getCasilla(fila, columna);
            if (c != null && c.isActiva()) {
                return false;
            }
            fila += pasoFila;
            columna += pasoColumna;
        }
        return true;
    }

    // Une las casillas siguiendo el orden lineal del tablero.
    // El final de un renglón con el inicio del siguiente.
    private boolean caminoLibreLineal(Casilla a, Casilla b) {
        int indiceA = getIndiceLineal(a);
        int indiceB = getIndiceLineal(b);

        int inicio = Math.min(indiceA, indiceB);
        int fin = Math.max(indiceA, indiceB);

        for (int i = inicio + 1; i < fin; i++) {
            Casilla c = tablero.getCasillas().obtener(i);
            if (c != null && c.isActiva()) {
                return false;
            }
        }
        return true;
    }

    private int getIndiceLineal(Casilla casilla) {
        return casilla.getFila() * tablero.getColumnas() + casilla.getColumna();
    }

    public Casilla[] darPista() {
        if (pistasRestantes <= 0) {
            return null;
        }

        for (int i = 0; i < tablero.getTotalCasillas(); i++) {
            Casilla a = tablero.getCasillas().obtener(i);
            if (a == null || !a.isActiva()) {
                continue;
            }

            for (int j = i + 1; j < tablero.getTotalCasillas(); j++) {
                Casilla b = tablero.getCasillas().obtener(j);
                if (b == null || !b.isActiva()) {
                    continue;
                }

                if (a.compareTo(b) == 0 && esMovimientoValido(a, b)) {
                    pistasRestantes--;
                    return new Casilla[]{a, b};
                }
            }
        }
        return null;
    }

    public boolean deshacer() {
        Movimiento movimiento = historial.eliminarInicio();
        if (movimiento == null) {
            return false;
        }

        movimiento.getCasilla1().setActiva(true);
        movimiento.getCasilla2().setActiva(true);
        movimiento.getCasilla1().setSeleccionada(false);
        movimiento.getCasilla2().setSeleccionada(false);

        puntos = movimiento.getPuntosAntes();
        concordanciasEncontradas = movimiento.getEncontradasAntes();
        concordanciasPendientes = movimiento.getPendientesAntes();
        pistasRestantes = movimiento.getPistasAntes();
        return true;
    }

    public boolean agregarNumeros() {
        boolean agregado = tablero.agregarNumerosActivosAlFinal();
        concordanciasPendientes = calcularConcordanciasPendientes();
        return agregado;
    }

    public boolean hayMovimientosDisponibles() {
        for (int i = 0; i < tablero.getTotalCasillas(); i++) {
            Casilla a = tablero.getCasillas().obtener(i);
            if (a == null || !a.isActiva()) {
                continue;
            }

            for (int j = i + 1; j < tablero.getTotalCasillas(); j++) {
                Casilla b = tablero.getCasillas().obtener(j);
                if (b == null || !b.isActiva()) {
                    continue;
                }

                if (a.compareTo(b) == 0 && esMovimientoValido(a, b)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean juegoTerminado() {
        return !hayMovimientosDisponibles();
    }

    private int calcularConcordanciasPendientes() {
        int contador = 0;

        for (int i = 0; i < tablero.getTotalCasillas(); i++) {
            Casilla a = tablero.getCasillas().obtener(i);
            if (a == null || !a.isActiva()) {
                continue;
            }

            for (int j = i + 1; j < tablero.getTotalCasillas(); j++) {
                Casilla b = tablero.getCasillas().obtener(j);
                if (b == null || !b.isActiva()) {
                    continue;
                }

                if (a.compareTo(b) == 0 && esMovimientoValido(a, b)) {
                    contador++;
                }
            }
        }

        return contador;
    }
}