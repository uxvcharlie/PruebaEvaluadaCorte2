package com.example.practicaevaluadacorte2.dao;

import com.example.practicaevaluadacorte2.dao.DatosException;
import com.example.practicaevaluadacorte2.dao.ProductoDAO;
import com.example.practicaevaluadacorte2.model.Producto;

/*
 * Prueba E2: llama al DAO directamente, SIN pasar por las validaciones
 * de la ventana, para ver que la base rechaza los datos malos y que el
 * DAO maneja la SQLException. No es parte de la interfaz: se corre aparte
 * (clic derecho > Run 'PruebasDAO.main()').
 * */


public class PruebasDAO {

    // Una "accion" que puede lanzar DatosException (para no repetir el try/catch)
    interface Accion {
        void ejecutar() throws DatosException;
    }

    public static void main(String[] args) throws DatosException {
        ProductoDAO dao = new ProductoDAO();
        int antes = dao.buscar("", "", false).size();

        probar("Codigo duplicado (P001)", () ->
                dao.insertar(new Producto(0, "P001", "Producto repetido", "Papelería", 10.00, 1)));

        probar("CHECK precio (0)", () ->
                dao.insertar(new Producto(0, "X900", "Precio cero", "Papelería", 0.0, 1)));

        probar("CHECK categoria (Ropa)", () ->
                dao.insertar(new Producto(0, "X901", "Camiseta", "Ropa", 10.00, 1)));

        probar("CHECK stock (-5)", () ->
                dao.insertar(new Producto(0, "X902", "Stock negativo", "Papelería", 10.00, -5)));

        int despues = dao.buscar("", "", false).size();
        System.out.println("Filas antes: " + antes + " | despues: " + despues
                + (antes == despues ? "  -> OK, la base no cambio" : "  -> MAL, la base cambio"));
    }

    private static void probar(String nombre, Accion accion) {
        try {
            accion.ejecutar();
            System.out.println("[MAL] " + nombre + ": la base lo acepto");
        } catch (DatosException e) {
            System.out.println("[OK]  " + nombre + ": rechazado. SQLState=" + e.getSqlState()
                    + " | mensaje al usuario: " + e.getMessage());
        }
    }
}