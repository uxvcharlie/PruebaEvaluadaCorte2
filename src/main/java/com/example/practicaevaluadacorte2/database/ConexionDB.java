package com.example.practicaevaluadacorte2.database;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConexionDB {

    private static final String archivo = System.getProperty("db.conf", "db.properties");

    public ConexionDB() {
    }

    public static Connection getConexion() throws SQLException {

        Properties config = new Properties();


        /*Crea el lector que va a acceder a las credenciales de bd.properties*/
        try (Reader lector = Files.newBufferedReader(Path.of(archivo))) {
            config.load(lector);
        } catch (IOException e) {
            // 08001 = "no se pudo establecer la conexion"
            throw new SQLException("No se encontró el archivo de configuración " + archivo, "08001", e);
        }

        String url = config.getProperty("db.url");
        String user = config.getProperty("db.usuario");
        String password = config.getProperty("db.contra");

        return DriverManager.getConnection(url, user, password);
    }


}

