package org.example.servicios;

import org.example.productos.Producto;

import java.util.ArrayList;
import java.util.List;


public class ProductoService {
    private List<Producto> productos = new ArrayList<>();

    public void agregarProducto(Producto producto) {
        productos.add(producto);
    }

    public List<Producto> obtenerTodos() {
        return productos;
    }

    public Producto buscarPorId(int id) {
        for (Producto p : productos) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    public Producto buscarPorNombre(String nombre) {
        for (Producto p : productos) {
            if (p.getNombre().equalsIgnoreCase(nombre)) {
                return p;
            }
        }
        return null;
    }

    public boolean eliminarProducto(int id) {
        return productos.removeIf(p -> p.getId() == id);
    }
}
