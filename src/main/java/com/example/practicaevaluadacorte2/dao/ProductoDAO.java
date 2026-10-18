package com.example.practicaevaluadacorte2.dao;

import com.example.practicaevaluadacorte2.database.ConexionDB;
import com.example.practicaevaluadacorte2.model.Producto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    /*
     * Un solo SELECT para la carga inicial, Buscar, Recargar y Limpiar filtros.
     * Cada filtro no pasa solo cuando viene vacio:
     *   texto vacio = ILIKE '%%' deja pasar todo
     *   categoria '' = (? = '') da verdadero
     *   false = (? = FALSE) da verdadero
     * Los tres se combinan con AND.
     * */
    private static final String SQL_BUSCAR = """
            SELECT id_producto, codigo, nombre, categoria, precio, stock
            FROM catalogo.producto
            WHERE (nombre ILIKE ? OR codigo ILIKE ?)
            AND (? = '' OR categoria = ?)
            AND (? = FALSE OR stock > 0)
            ORDER BY codigo """;



    public List<Producto> buscar(String texto, String categoria, boolean soloConStock) throws DatosException {
        List<Producto> lista = new ArrayList<>();
        String patron = "%" + texto + "%";

        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(SQL_BUSCAR)) {

            ps.setString(1, patron);
            ps.setString(2, patron);
            ps.setString(3, categoria);
            ps.setString(4, categoria);
            ps.setBoolean(5, soloConStock);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Producto producto = new Producto(
                            rs.getInt("id_producto"),
                            rs.getString("codigo"),
                            rs.getString("nombre"),
                            rs.getString("categoria"),
                            rs.getDouble("precio"),
                            rs.getInt("stock")
                    );
                    lista.add(producto);
                }
            }
        } catch (SQLException e) {
            throw error("buscar", e);
        }
        return lista;
    }

    /*
     * Verifica si el codigo ya pertenece a otro producto (V9). @param idProductoExcluir, es el id del producto que se esta actualizando
     * para no compararlo consigo mismo. Al guardar uno nuevo se envia un null
     * */
    public boolean existeCodigo(String codigo, Integer idProductoExcluir) throws DatosException {
        String sql = "SELECT COUNT(*) FROM catalogo.producto WHERE codigo = ?";

        if (idProductoExcluir != null) {
            sql += " AND id_producto <> ?";
        }

        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, codigo);

            if (idProductoExcluir != null) {
                ps.setInt(2, idProductoExcluir);
            }

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw error("existeCodigo", e);
        }
    }

    /*
     * Devuelve las filas afectadas (1 si se guardo)
     * */
    public int insertar(Producto producto) throws DatosException {
        String sql = """
            INSERT INTO catalogo.producto (codigo, nombre, categoria, precio, stock)
            VALUES (?, ?, ?, ?, ?)
            """;

        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            asignarDatos(ps, producto);
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw error("insertar", e);
        }
    }

    /*
     * Los 5 datos del formulario, en el mismo orden de los ? del INSERT y del UPDATE
     * */
    private void asignarDatos(PreparedStatement ps, Producto producto) throws SQLException {
        ps.setString(1, producto.getCodigo());
        ps.setString(2, producto.getNombre());
        ps.setString(3, producto.getCategoria());
        ps.setDouble(4, producto.getPrecio());
        ps.setInt(5, producto.getStock());
    }

    /*
     * deja en la consola la operacion y el SQLState (para el programador) y arma un mensaje seguro para el usuario sin clave.
     * */
    private DatosException error(String operacion, SQLException e) {
        System.err.println(LocalDateTime.now().withNano(0)
                + " [ERROR BD] operacion=" + operacion
                + " SQLState=" + e.getSQLState()
                + " detalle=" + e.getMessage());

        return new DatosException(mensajeParaUsuario(e.getSQLState()), e.getSQLState(), e);
    }

    private String mensajeParaUsuario(String sqlState) {
        if (sqlState == null) {
            return "Ocurrió un error con la base de datos. Revisa la consola.";
        }
        if (sqlState.startsWith("08")) {
            return "No se pudo conectar con la base de datos. "
                    + "Revisa que PostgreSQL esté encendido y los datos de db.properties.";
        }
        switch (sqlState) {
            case "28P01":
            case "28000":

                return "PostgreSQL rechazó el usuario o la contraseña de db.properties.";
            case "3D000":
                return "La base de datos que indica db.properties no existe.";
            case "42P01":

                return "No existe la tabla catalogo.producto.";
            case "23505":
                return "Ya existe un producto con ese código.";
            case "23514":
                return "La base de datos rechazó el producto: un dato no cumple sus reglas (CHECK).";
            case "23502":
                return "Falta un dato obligatorio.";
            case "22001":
                return "Un texto es más largo de lo permitido.";
            case "22003":
                return "Un número está fuera del rango permitido.";
            default:
                return "Ocurrió un error con la base de datos. Revisa la consola.";
        }
    }


    /*
     * UPDATE por id. Devuelve las filas afectadas: 0 si el producto ya no existe (E3)
     * */
    public int actualizar(Producto producto) throws DatosException {
        String sql = """
            UPDATE catalogo.producto
            SET codigo = ?, nombre = ?, categoria = ?, precio = ?, stock = ?
            WHERE id_producto = ?
            """;

        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            asignarDatos(ps, producto);
            ps.setInt(6, producto.getIdProducto());
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw error("actualizar id=" + producto.getIdProducto(), e);
        }
    }


    /*
     * DELETE por id. Devuelve las filas afectadas: 0 si el producto ya no existe (E3)
     * */
    public int eliminar(int idProducto) throws DatosException {
        String sql = "DELETE FROM catalogo.producto WHERE id_producto = ?";

        try (Connection conexion = ConexionDB.getConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idProducto);
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw error("eliminar id=" + idProducto, e);
        }
    }
}
