package com.example.practicaevaluadacorte2.controller;

import com.example.practicaevaluadacorte2.model.Producto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;

public class ProductosController {

    /*Gestion*/
    @FXML
    private TextField txtCodigo;

    @FXML
    private TextField txtNombre;

    @FXML
    private ComboBox<Producto> cmbCategoria;

    @FXML
    private TextField txtPrecio;

    @FXML
    private TextField txtStock;

    /*Busqueda*/

    @FXML
    private TextField txtBusqueda;

    @FXML
    private ComboBox<Producto> cmbCategoriaFiltro;

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


    /*Observable list*/
    private final ObservableList<Producto> entidades = FXCollections.observableArrayList();


}
