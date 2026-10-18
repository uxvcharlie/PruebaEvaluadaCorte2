package com.example.practicaevaluadacorte2.controller;

import com.example.practicaevaluadacorte2.dao.DatosException;
import com.example.practicaevaluadacorte2.dao.ProductoDAO;
import com.example.practicaevaluadacorte2.model.Producto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.Optional;

public class ProductosController {

    /*Gestion*/
    @FXML
    private TextField txtCodigo;

    @FXML
    private TextField txtNombre;

    @FXML
    private ComboBox<String> cmbCategoria;

    @FXML
    private TextField txtPrecio;

    @FXML
    private TextField txtStock;

    @FXML
    private TextField txtId;


    /*Busqueda*/

    @FXML
    private TextField txtBusqueda;

    @FXML
    private ComboBox<String> cmbCategoriaFiltro;

    @FXML
    private CheckBox chkSoloExistencia;

    @FXML
    private TableView<Producto> tblProducto;

    @FXML
    private TableColumn<Producto, Integer> colId;

    @FXML
    private TableColumn<Producto, String> colCodigo;

    @FXML
    private TableColumn<Producto, String> colNombre;

    @FXML
    private TableColumn<Producto, String> colCategoria;

    @FXML
    private TableColumn<Producto, Double> colPrecio;

    @FXML
    private TableColumn<Producto, Integer> colStock;

    @FXML
    private Label lblContador;


    /*Observable list*/
    private final ObservableList<Producto> productos = FXCollections.observableArrayList();



    @FXML
    private void initialize() {
        configurarTabla();
        configurarSeleccionTabla();
        cargarCategorias();
        cargarProductos();
    }

    /*
     * Todo el SQL esta en el DAO: el controlador solo le pide las cosas
     * */
    private final ProductoDAO productoDAO = new ProductoDAO();

    private Producto productoSeleccionado;


    /*Se crean para poder seleecionar todas las categorias en el filtros y tambien poner limite al precio*/
    private static final String TODAS = "Todas";
    private static final List<String> CATEGORIAS = List.of("Papelería", "Tecnología", "Accesorios");

    private static final double PRECIO_MAXIMO = 99999999.99;


    /*
     * Recibe como parametro el producto seleccionado del TableView
     * */
    private void cargarProductoEnFormulario(Producto p) {
        txtId.setText(String.valueOf(p.getIdProducto()));
        txtCodigo.setText(p.getCodigo());
        txtNombre.setText(p.getNombre());
        cmbCategoria.setValue(p.getCategoria());
        txtPrecio.setText(String.valueOf(p.getPrecio()));
        txtStock.setText(String.valueOf(p.getStock()));
    }


    /*
     * Enlaza cada columna con el getter de Producto que le corresponde
     * (PropertyValueFactory("codigo") llama a getCodigo())
     * */
    private void configurarTabla() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idProducto"));
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));

        tblProducto.setItems(productos);
    }


    /*
     * Al seleccionar una fila se guarda el producto y se pasa al formulario
     * para poder actualizarlo o eliminarlo
     * */
    private void configurarSeleccionTabla() {
        tblProducto.getSelectionModel().selectedItemProperty().addListener((obs, anterior, nuevo) -> {
            productoSeleccionado = nuevo;
            if (nuevo != null) {
                cargarProductoEnFormulario(nuevo);
            }
        });
    }


    /*
     * Las categorias son fijas (las mismas del CHECK de la tabla).
     * El ComboBox del filtro lleva todas las categorias; el del formulario no, porque es donde se agrega
     * */
    private void cargarCategorias() {
        cmbCategoria.setItems(FXCollections.observableArrayList(CATEGORIAS));

        cmbCategoriaFiltro.setItems(FXCollections.observableArrayList(TODAS));
        cmbCategoriaFiltro.getItems().addAll(CATEGORIAS);
        cmbCategoriaFiltro.setValue(TODAS);
    }


    /*
     * Consulta PostgreSQL con los filtros que esten puestos y llena el TableView.
     * La usan la carga inicial, Buscar, Recargar, Limpiar filtros y el CRUD.
     * Devuelve false si la consulta fallo
     * */
    private boolean cargarProductos() {
        String texto = txtBusqueda.getText().trim();
        String categoria = cmbCategoriaFiltro.getValue();

        if (categoria == null || categoria.equals(TODAS)) {
            categoria = "";
        }

        try {
            productos.setAll(productoDAO.buscar(texto, categoria, chkSoloExistencia.isSelected()));
            lblContador.setText("Registros visibles: " + productos.size());
            return true;
        } catch (DatosException e) {
            /*
             * si falla la conexion NO se muestra con 0 registros, porque parecería una búsqueda que salió bien y no encontró nada
             * */
            productos.clear();
            lblContador.setText("Registros visibles: - error al consultar la base de datos.");
            mostrarErrorBaseDatos("No se pudieron cargar los productos.", e);
            return false;
        }
    }

    @FXML
    private void buscarProductos() {
        if (cargarProductos() && productos.isEmpty()) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Sin coincidencias",
                    "No existen productos que coincidan con los filtros aplicados.");
        }
    }

    @FXML
    private void recargar() {
        cargarProductos();
    }


    /*
     * Deja los filtros de busqueda en su estado inicial (no toca el formulario)
     * */
    @FXML
    private void limpiar() {
        txtBusqueda.clear();
        cmbCategoriaFiltro.setValue(TODAS);
        chkSoloExistencia.setSelected(false);

        cargarProductos();
    }

    @FXML
    private void nuevo() {
        limpiarFormulario();
    }

    /*
     * Deja el formulario vacío para registrar un nuevo producto
     * */
    private void limpiarFormulario() {
        txtId.clear();
        txtCodigo.clear();
        txtNombre.clear();
        cmbCategoria.setValue(null);
        txtPrecio.clear();
        txtStock.clear();
        tblProducto.getSelectionModel().clearSelection();
        productoSeleccionado = null;
        txtCodigo.requestFocus();
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }


    /*
     * El DAO ya dejo el SQLState en la consola (E4).
     * Aqui solo se muestra el mensaje seguro, sin contraseñas ni trazas
     * */
    private void mostrarErrorBaseDatos(String mensaje, DatosException e) {
        mostrarAlerta(Alert.AlertType.ERROR, "Error de base de datos",
                mensaje + "\n\nDetalle: " + e.getMessage());
    }


    /*
     * Valida los campos del formulario antes de guardar o actualizar (V1 a V8).
     * Primero normaliza: quita los espacios de los lados y pone el codigo en mayusculas
     * */
    private boolean validarFormulario() {
        txtCodigo.setText(txtCodigo.getText().trim().toUpperCase());
        txtNombre.setText(txtNombre.getText().trim());

        String codigo = txtCodigo.getText();
        String nombre = txtNombre.getText();

        if (codigo.isEmpty() || codigo.length() > 10) {
            mostrarAlerta(Alert.AlertType.WARNING, "Código inválido",
                    "El código debe tener de 1 a 10 caracteres (sin contar los espacios de los lados).");
            txtCodigo.requestFocus();
            return false;
        }


        if (nombre.length() < 3 || nombre.length() > 80) {
            mostrarAlerta(Alert.AlertType.WARNING, "Nombre inválido",
                    "El nombre debe tener de 3 a 80 caracteres (sin contar los espacios de los lados).");
            txtNombre.requestFocus();
            return false;
        }

        if (cmbCategoria.getValue() == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campo requerido",
                    "Seleccione una categoría: Papelería, Tecnología o Accesorios.");
            cmbCategoria.requestFocus();
            return false;
        }

        String textoPrecio = txtPrecio.getText().trim();

        try {
            double precio = Double.parseDouble(textoPrecio);

            if (precio <= 0) {
                mostrarAlerta(Alert.AlertType.WARNING, "Precio inválido", "El precio debe ser mayor que cero.");
                txtPrecio.requestFocus();
                return false;
            }

            if (precio > PRECIO_MAXIMO) {
                mostrarAlerta(Alert.AlertType.WARNING, "Precio inválido",
                        "El precio máximo permitido es 99999999.99.");
                txtPrecio.requestFocus();
                return false;
            }

            /*
             * Un double no recuerda cuantos decimales escribiste (12.345 se guarda igual),
             * por eso los decimales se revisan en el TEXTO: digitos y, si hay punto, 1 o 2 decimales
             * */
            if (!textoPrecio.matches("\\d+(\\.\\d{1,2})?")) {
                mostrarAlerta(Alert.AlertType.WARNING, "Precio inválido",
                        "El precio admite como máximo 2 decimales, por ejemplo 65.00");
                txtPrecio.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.WARNING, "Precio inválido",
                    "Ingrese un precio numérico con punto decimal, por ejemplo 65.00");
            txtPrecio.requestFocus();
            return false;
        }

        try {
            int stock = Integer.parseInt(txtStock.getText().trim());

            if (stock < 0) {
                mostrarAlerta(Alert.AlertType.WARNING, "Stock inválido", "El stock no puede ser negativo.");
                txtStock.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.WARNING, "Stock inválido",
                    "Ingrese un stock entero de 0 a 2147483647, por ejemplo 10");
            txtStock.requestFocus();
            return false;
        }

        return true;
    }

    /*
     * Arma el Producto con lo que hay en el formulario (ya validado y normalizado).
     * El id va en 0: el verdadero lo pone PostgreSQL al insertar
     * */
    private Producto crearProductoDesdeFormulario() {
        return new Producto(
                0,
                txtCodigo.getText(),
                txtNombre.getText(),
                cmbCategoria.getValue(),
                Double.parseDouble(txtPrecio.getText().trim()),
                Integer.parseInt(txtStock.getText().trim())
        );
    }

    @FXML
    private void guardar() {
        if (!validarFormulario()) {
            return;
        }

        Producto producto = crearProductoDesdeFormulario();

        try {
            /*
             * Validación preventiva de UNIQUE
             */
            if (productoDAO.existeCodigo(producto.getCodigo(), null)) {
                mostrarAlerta(Alert.AlertType.WARNING, "Código duplicado",
                        "El código " + producto.getCodigo() + " ya pertenece a otro producto.");
                txtCodigo.requestFocus();
                return;
            }

            int filasAfectadas = productoDAO.insertar(producto);

            if (filasAfectadas > 0) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Registro guardado",
                        "El producto fue registrado correctamente.");
                limpiarFormulario();
                cargarProductos();
            }
        } catch (DatosException e) {
            mostrarErrorBaseDatos("No se pudo guardar el producto.", e);
        }
    }

    @FXML
    private void actualizar() {
        if (productoSeleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Sin selección",
                    "Selecciona un producto de la tabla para actualizarlo.");
            return;
        }

        if (!validarFormulario()) {
            return;
        }

        Producto producto = crearProductoDesdeFormulario();
        producto.setIdProducto(productoSeleccionado.getIdProducto());

        try {
            /*
             * excluye su propio id, para que P001 pueda conservar su codigo
             */
            if (productoDAO.existeCodigo(producto.getCodigo(), producto.getIdProducto())) {
                mostrarAlerta(Alert.AlertType.WARNING, "Código duplicado",
                        "El código " + producto.getCodigo() + " ya pertenece a otro producto.");
                txtCodigo.requestFocus();
                return;
            }

            int filasAfectadas = productoDAO.actualizar(producto);

            /*
             * no hay = alguien borro el producto mientras estaba seleccionado
             */
            if (filasAfectadas == 0) {
                avisarQueYaNoExiste();
                return;
            }

            mostrarAlerta(Alert.AlertType.INFORMATION, "Registro actualizado",
                    "Los cambios se guardaron correctamente.");
            limpiarFormulario();
            cargarProductos();
        } catch (DatosException e) {
            mostrarErrorBaseDatos("No se pudo actualizar el producto.", e);
        }
    }

    @FXML
    private void eliminar() {
        // V10
        if (productoSeleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Sin selección",
                    "Selecciona un producto de la tabla para eliminarlo.");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Eliminar producto");
        confirmacion.setHeaderText(null);
        confirmacion.setContentText("¿Eliminar " + productoSeleccionado.getNombre()
                + " (" + productoSeleccionado.getCodigo() + ")?\nEsta acción no se puede deshacer.");

        Optional<ButtonType> respuesta = confirmacion.showAndWait();
        if (respuesta.isEmpty() || respuesta.get() != ButtonType.OK) {
            return;
        }

        try {
            int filasAfectadas = productoDAO.eliminar(productoSeleccionado.getIdProducto());

            // E3
            if (filasAfectadas == 0) {
                avisarQueYaNoExiste();
                return;
            }

            mostrarAlerta(Alert.AlertType.INFORMATION, "Registro eliminado",
                    "El producto se eliminó de la base de datos.");
            limpiarFormulario();
            cargarProductos();
        } catch (DatosException e) {
            mostrarErrorBaseDatos("No se pudo eliminar el producto.", e);
        }
    }

    /*
     * E3: el producto seleccionado ya no esta en la base (lo borraron desde pgAdmin u otra ventana). Se avisa, se limpia el formulario y se recarga la tabla para que deje de aparecer
     * */
    private void avisarQueYaNoExiste() {
        mostrarAlerta(Alert.AlertType.WARNING, "Producto no encontrado",
                "El producto seleccionado ya no existe en la base de datos. Se recargará la tabla.");
        limpiarFormulario();
        cargarProductos();
    }
}
