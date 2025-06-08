package org.example.excepciones;

public class StockInsuficienteExcepcion extends Exception {
    public StockInsuficienteExcepcion(String mensaje) {
        super(mensaje);
    }
}
