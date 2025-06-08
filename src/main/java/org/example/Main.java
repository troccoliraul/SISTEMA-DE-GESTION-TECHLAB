package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.example.productos.*;
import org.example.pedidos.*;
import org.example.excepciones.StockInsuficienteExcepcion;
import org.example.servicios.*;
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    private static ProductoService productoService = new ProductoService();
    private static List<Pedido> pedidos = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean salir = false;

        while (!salir) {
            mostrarMenu();
            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    agregarProducto();
                    break;
                case "2":
                    listarProductos();
                    break;
                case "3":
                    buscarActualizarProducto();
                    break;
                case "4":
                    eliminarProducto();
                    break;
                case "5":
                    crearPedido();
                    break;
                case "6":
                    listarPedidos();
                    break;
                case "7":
                    salir = true;
                    System.out.println("¡Hasta luego!");
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        }
    }

    private static void mostrarMenu() {
        System.out.println("\n=== SISTEMA DE GESTIÓN - TECHLAB ===");
        System.out.println("1) Agregar producto");
        System.out.println("2) Listar productos");
        System.out.println("3) Buscar/Actualizar producto");
        System.out.println("4) Eliminar producto");
        System.out.println("5) Crear un pedido");
        System.out.println("6) Listar pedidos");
        System.out.println("7) Salir");
        System.out.print("Elija una opción: ");
    }

    private static void agregarProducto() {
        System.out.print("¿Es una bebida? (s/n): ");
        String tipo = scanner.nextLine();

        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();

        double precio = 0;
        int stock = 0;

        try {
            System.out.print("Precio: ");
            precio = Double.parseDouble(scanner.nextLine().replace(',', '.'));

            System.out.print("Stock: ");
            stock = Integer.parseInt(scanner.nextLine());

            if (tipo.equalsIgnoreCase("s")) {
                System.out.print("Volumen en litros: ");
                double volumen = Double.parseDouble(scanner.nextLine().replace(',', '.'));
                productoService.agregarProducto(new Bebida(nombre, precio, stock, volumen));
            } else {
                productoService.agregarProducto(new Producto(nombre, precio, stock));
            }

            System.out.println("Producto agregado.");
        } catch (NumberFormatException e) {
            System.out.println("Error: Ingresaste un número inválido. El producto no se pudo agregar.");
        }
    }

    private static void listarProductos() {
        List<Producto> productos = productoService.obtenerTodos();
        if (productos.isEmpty()) {
            System.out.println("No hay productos.");
        } else {
            for (Producto p : productos) {
                System.out.println(p);
            }
        }
    }

    private static void buscarActualizarProducto() {
        System.out.print("Ingrese el ID del producto: ");
        int id = Integer.parseInt(scanner.nextLine());
        Producto p = productoService.buscarPorId(id);
        if (p != null) {
            System.out.println("Producto encontrado: " + p);
            System.out.print("¿Desea actualizar precio o stock? (p/s/n): ");
            String op = scanner.nextLine();
            if (op.equalsIgnoreCase("p")) {
                System.out.print("Nuevo precio: ");
                double nuevoPrecio = Double.parseDouble(scanner.nextLine());
                p.setPrecio(nuevoPrecio);
                System.out.println("Precio actualizado.");
            } else if (op.equalsIgnoreCase("s")) {
                System.out.print("Nuevo stock: ");
                int nuevoStock = Integer.parseInt(scanner.nextLine());
                p.setStock(nuevoStock);
                System.out.println("Stock actualizado.");
            }
        } else {
            System.out.println("Producto no encontrado.");
        }
    }

    private static void eliminarProducto() {
        System.out.print("Ingrese el ID del producto a eliminar: ");
        int id = Integer.parseInt(scanner.nextLine());
        boolean eliminado = productoService.eliminarProducto(id);
        System.out.println(eliminado ? "Producto eliminado." : "Producto no encontrado.");
    }

    private static void crearPedido() {
        Pedido pedido = new Pedido();
        boolean seguir = true;

        while (seguir) {
            System.out.print("ID del producto: ");
            int id = Integer.parseInt(scanner.nextLine());
            Producto producto = productoService.buscarPorId(id);

            if (producto != null) {
                System.out.print("Cantidad: ");
                int cantidad = Integer.parseInt(scanner.nextLine());
                try {
                    if (producto.getStock() < cantidad) {
                        throw new StockInsuficienteExcepcion("No hay stock suficiente.");
                    }
                    pedido.agregarLinea(new LineaPedido(producto, cantidad));
                    producto.setStock(producto.getStock() - cantidad);
                    System.out.print("¿Agregar otro producto al pedido? (s/n): ");
                    seguir = scanner.nextLine().equalsIgnoreCase("s");
                } catch (StockInsuficienteExcepcion e) {
                    System.out.println(e.getMessage());
                }
            } else {
                System.out.println("Producto no encontrado.");
            }
        }

        pedidos.add(pedido);
        System.out.println("Pedido creado:\n" + pedido);
    }

    private static void listarPedidos() {
        if (pedidos.isEmpty()) {
            System.out.println("No hay pedidos.");
        } else {
            for (Pedido p : pedidos) {
                System.out.println(p);
                System.out.println("------------------");
            }
        }
    }
}