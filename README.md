# Laboratorio TableView - CRUD (Papelería Campus)

Aplicación de escritorio en JavaFX que guarda el catálogo de productos de la papelería Campus en PostgreSQL y lo muestra en un `TableView`. Permite buscar por nombre o código, filtrar por categoría y existencias, y crear, actualizar y eliminar productos con validaciones.

- **Autor:** Carlos Rafael Umaña Vásquez
- **Asignatura:** Programación de Aplicaciones de Escritorio
## Versiones utilizadas

| Herramienta | Versión |
|---|---|
| JDK | Eclipse Temurin 21.0.12 |
| JavaFX (controls y fxml) | 21.0.6 |
| PostgreSQL | 18.6 |
| Driver JDBC de PostgreSQL | 42.7.13 |
| Maven (wrapper del proyecto) | 3.8.5 |
| IntelliJ IDEA | 2026.2 |
| pgAdmin | 4 |

## Dependencias (pom.xml)

- `org.openjfx:javafx-controls:21.0.6`
- `org.openjfx:javafx-fxml:21.0.6`
- `org.postgresql:postgresql:42.7.13`

Maven las descarga solo al abrir el proyecto.

## Crear la base de datos

1. En pgAdmin, clic derecho en **Databases > Create > Database...** y ponle de nombre `papeleria_campus`. (O en un Query Tool sobre `postgres`: `CREATE DATABASE papeleria_campus;`).
2. Selecciona la base `papeleria_campus` en el árbol y abre **Tools > Query Tool**. El título de la pestaña debe decir `papeleria_campus`.
3. Abre y ejecuta `sql/estructura.sql` (crea el esquema `catalogo`, la tabla `producto` y sus restricciones).
4. Abre y ejecuta `sql/datos.sql` (carga los 12 productos iniciales).

**Restaurar la base de prueba:** ejecuta otra vez `sql/datos.sql`. Vacía la tabla, reinicia el contador del id y vuelve a cargar los 12 productos.

`sql/pruebas-restricciones.sql` trae los INSERT que sirven para probar las restricciones sin la aplicación (todos deben fallar).

## Configuración de la conexión

Los datos de conexión no están en el código. Se leen del archivo `db.properties` en la carpeta raíz del proyecto, que **no se sube a GitHub** (está en `.gitignore`).

1. Copia `db.properties.example` y renómbralo a `db.properties`.
2. Cambia `db.contra` por tu contraseña de PostgreSQL (y la URL o el usuario si son distintos).

```properties
db.url=jdbc:postgresql://localhost:5432/papeleria_campus
db.usuario=postgres
db.contra=CAMBIA_ESTO
```

Para usar otro archivo se arranca con la opción de VM `-Ddb.conf=otro.properties`. El archivo `db-prueba-e1.properties` tiene un puerto incorrecto a propósito, para la prueba E1.

## Ejecución

- **IntelliJ:** abrir la carpeta del proyecto, esperar a que Maven cargue y ejecutar `Launcher` (flecha verde).
- **Prueba E2 del DAO:** ejecutar `pruebas/PruebasDAO` (clic derecho > Run). No usa la ventana.

## Organización de clases

```zsh
src/main/java/com/example/practicaevaluadacorte2/
├── Launcher.java              arranca la aplicación
├── HelloApplication.java      carga papeleria-view.fxml y abre la ventana
├── database/ConexionDB.java   abre la conexión leyendo db.properties
├── model/Producto.java        la entidad (id, código, nombre, categoría, precio, stock)
├── dao/ProductoDAO.java       TODO el SQL: buscar, existeCodigo, insertar, actualizar, eliminar
├── dao/DatosException.java    error de base de datos con un mensaje seguro para el usuario
├── controller/ProductosController.java  eventos de la ventana y validaciones
└── pruebas/PruebasDAO.java    prueba E2 (llama al DAO sin la interfaz)
src/main/resources/com/example/practicaevaluadacorte2/papeleria-view.fxml   la interfaz
sql/        estructura.sql, datos.sql, pruebas-restricciones.sql
evidencias/ capturas de las pruebas
pruebas.md  matriz de pruebas
```

## Flujo de la información

```
Interfaz (FXML)  ->  ProductosController ->  ProductoDAO  ->  PostgreSQL
   botón, campo        valida y normaliza     PreparedStatement   catalogo.producto
                                              executeQuery / executeUpdate
TableView  <-  ObservableList<Producto>  <-  List<Producto>  <-  ResultSet
```

1. El usuario pulsa un botón (Buscar, Guardar, Actualizar, Eliminar...).
2. El controlador lee el formulario y los filtros, aplica `trim` y mayúsculas al código y valida (V1 a V10).
3. El controlador llama al DAO. El DAO abre la conexión con `ConexionDB`, arma un `PreparedStatement` con parámetros y ejecuta `executeQuery` (consultas) o `executeUpdate` (escrituras). Todo se cierra con try-with-resources.
4. Si PostgreSQL da error, el DAO escribe en la consola la operación y el SQLState y lanza `DatosException` con un mensaje seguro.
5. Después de guardar, actualizar o eliminar, el controlador vuelve a consultar con los filtros vigentes: el `ResultSet` se convierte en objetos `Producto`, que pasan a la `ObservableList` del `TableView`, y se actualiza el contador.

## Fuentes y herramientas de apoyo

- pgJDBC: consultas, parámetros y ResultSet. https://jdbc.postgresql.org/documentation/query/
- PostgreSQL: coincidencia de patrones e ILIKE. https://www.postgresql.org/docs/current/functions-matching.html
- OpenJFX: TableView. https://openjfx.io/javadoc/21/javafx.controls/javafx/scene/control/TableView.html
- Oracle: SQLException y try-with-resources. https://docs.oracle.com/javase/tutorial/jdbc/basics/sqlexception.html
- PostgreSQL: restricciones. https://www.postgresql.org/docs/current/ddl-constraints.html
- Herramientas: IntelliJ IDEA, pgAdmin 4, Git y GitHub.