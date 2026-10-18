# Pruebas del laboratorio TableView CRUD

- **Autor:** Carlos Rafael Umaña Vásquez
- **Fecha:** 17/10/2026
- **Base:** `papeleria_campus`, la local de prueba. Antes de cada bloque, restaurar los 12 productos con `sql/datos.sql`.
- **Estado:** `Pasa`, `Falla`, `Pendiente` (falta hacerla) o `Repetir` (hay que sacarla de nuevo). Si alguna falla, dejarla anotada igual con lo que salio.

"Resultado obtenido" es lo que muestra la app en pantalla. Las capturas estan en la carpeta `evidencias/`.

## Busqueda y filtros (B1 a B7)

Cada busqueda consulta PostgreSQL, no filtra solo la lista que ya estaba cargada.

| Caso | Accion | Resultado esperado | Resultado obtenido | Estado | Evidencia |
|---|---|---|---|---|---|
| Carga | Abrir la app | 12 filas ordenadas por codigo, igual que `SELECT * FROM catalogo.producto ORDER BY codigo` | | Pendiente | [imagen-tableview](evidencias/imagen-tableview.png) |
| B1 | Escribir `CUADERNO` y presionar Buscar | P001 y P002, contador 2 | Aparecen P001 y P002 | Pasa | [caso-b1](evidencias/filtros/caso-b1.png) |
| B2 | Escribir `T00` y presionar Buscar | T001 a T004, contador 4 | Aparecen T001, T002, T003 y T004 | Pasa | [caso-b2](evidencias/filtros/caso-b2.png) |
| B3 | Dejar el texto vacio y elegir Papelería | P001 a P004, contador 4 | Aparecen P001, P002, P003 y P004 | Pasa | [caso-b3](evidencias/filtros/caso-b3.png) |
| B4 | Marcar "Solo con existencias" con Todas | 9 productos, sin P004, T004 ni A003 | Aparecen 9 y no estan P004, T004 ni A003 | Pasa | [caso-b4](evidencias/filtros/caso-b4.png) |
| B5 | Escribir `USB`, elegir Tecnología y marcar "Solo con existencias" | T001 y T003, contador 2 | Aparecen T001 y T003 | Pasa | [caso-b5](evidencias/filtros/caso-b5.png) |
| B6 | Escribir `ZZZ999` y presionar Buscar | 0 filas, contador 0 y aviso de que no hay coincidencias | La tabla queda vacia y sale el aviso "No existen productos que coincidan con los filtros aplicados." | Pasa | [caso-b6](evidencias/filtros/caso-b6.png) |
| B7 | Despues de B5, presionar Limpiar filtros | Texto vacio, Todas, casilla desmarcada y 12 filas | Se limpian los filtros y vuelven las 12 filas | Pasa | [caso-b7](evidencias/filtros/caso-b7.png) |
| Recargar | Insertar una fila desde pgAdmin y presionar Recargar | La fila nueva aparece; despues borrarla y vuelven 12 | La fila "Fila externa" aparece al presionar Recargar | Pasa | [recargar](evidencias/filtros/recargar.png) |

## Ciclo CRUD

Hacerlo con los filtros limpios y revisar en pgAdmin despues de cada paso.

| Paso | Accion | Resultado esperado | SELECT para revisar | Resultado obtenido | Estado | Evidencia |
|---|---|---|---|---|---|---|
| Crear | Guardar X001, Producto de prueba, Accesorios, 10.00, stock 1 | Mensaje de guardado y 13 filas | `SELECT * FROM catalogo.producto WHERE codigo = 'X001';` | Sale "El producto fue registrado correctamente." y X001 aparece en la tabla. La captura tiene 14 filas porque todavia estaba A005 de la prueba V8, por eso hay que repetirla | Repetir | [x001-guardar](evidencias/ciclocrud/x001-guardar.png) |
| Reiniciar | Cerrar la app y volver a abrirla | X001 sigue ahi | | X001 sigue en la tabla | Repetir | [x001-persistencia](evidencias/ciclocrud/x001-persistencia.png) |
| Actualizar | Cambiar X001 a precio 15.50 y stock 3 | Solo cambia X001 y siguen 13 filas | el mismo SELECT | Sale "Los cambios se guardaron correctamente."; X001 queda con 15.5 y 3 y los demas no cambian | Repetir | [x001-actualizar](evidencias/ciclocrud/x001-actualizar.png), [vista](evidencias/ciclocrud/x001-actualizar-view.png) |
| Eliminar y cancelar | Presionar Eliminar con X001 y despues Cancel | X001 sigue y siguen 13 filas | el mismo SELECT (1 fila) | Pregunta "¿Eliminar Producto de prueba (X001)?" y con Cancel X001 se queda | Repetir | [x001-eliminar-cancelar](evidencias/ciclocrud/x001-eliminar-cancelar.png) |
| Eliminar y aceptar | Presionar Eliminar con X001 y despues OK | X001 se va y quedan 12 filas | el mismo SELECT (0 filas) | Sale "El producto se eliminó de la base de datos." y X001 ya no esta | Repetir | [x001-eliminar](evidencias/ciclocrud/x001-eliminar.png) |
| Reiniciar | Cerrar la app y volver a abrirla | 12 filas y sin X001 | `SELECT COUNT(*) FROM catalogo.producto;` | X001 ya no aparece | Repetir | [eliminar-persistencia](evidencias/ciclocrud/eliminar-persistencia.png) |
| Filtros puestos | Con "Solo con existencias", poner stock 0 a A004 y presionar Actualizar | A004 se va de la lista (8 filas); despues dejarlo como estaba | | | Pendiente | |

## Validaciones (V1 a V10)

Probar cada una al guardar y al actualizar (con una fila seleccionada), menos V10, que es solo con Actualizar y Eliminar. Cuando la app rechaza un dato, la base queda igual.

| Grupo | Accion | Resultado esperado | Resultado obtenido | Estado | Evidencia |
|---|---|---|---|---|---|
| V1 | Poner el codigo vacio, solo espacios y `ABCDEFGHIJK` (11) | Rechazarlo, de 1 a 10 caracteres | Sale "El código debe tener de 1 a 10 caracteres (sin contar los espacios de los lados)." | Pasa | [v1](evidencias/validaciones/v1.png) |
| V2 | Poner el nombre solo con espacios, `AB` y uno de 81 | Rechazarlo, de 3 a 80 caracteres | Sale "El nombre debe tener de 3 a 80 caracteres (sin contar los espacios de los lados)." | Pasa | [v2](evidencias/validaciones/v2.png) |
| V3 | No elegir categoria (el ComboBox no deja escribir otra) | Pedir la categoria | Sale "Seleccione una categoría: Papelería, Tecnología o Accesorios." | Pasa | [v3](evidencias/validaciones/v3.png) |
| V4 | Poner precio `abc` | Rechazarlo sin cerrar la app | Sale "Ingrese un precio numérico con punto decimal, por ejemplo 65.00" y la app sigue abierta | Pasa | [v4](evidencias/validaciones/v4.png) |
| V5 | Poner precio `0` y `-1` | Rechazar los dos | Con los dos sale "El precio debe ser mayor que cero." | Pasa | [v5.1](evidencias/validaciones/v5.1.png), [v5.2](evidencias/validaciones/v5.2.png) |
| V6 | Poner precio `12.345` y `100000000.00` | No aceptar mas de 2 decimales ni pasarse del maximo, sin redondear | Sale "El precio admite como máximo 2 decimales, por ejemplo 65.00" y "El precio máximo permitido es 99999999.99." | Pasa | [v6.1](evidencias/validaciones/v6.1.png), [v6.2](evidencias/validaciones/v6.2.png) |
| V7 | Poner stock `abc` y `2.5` | Aceptar solo enteros | Con los dos sale "Ingrese un stock entero de 0 a 2147483647, por ejemplo 10" | Pasa | [v7.1](evidencias/validaciones/v7.1.png), [v7.2](evidencias/validaciones/v7.2.png) |
| V8 | Poner stock `-1`, `2147483648` y despues `0` | Rechazar los dos primeros y aceptar el 0 | Con `-1` sale "El stock no puede ser negativo.", con `2147483648` sale "Ingrese un stock entero de 0 a 2147483647…" y con `0` lo guarda: "El producto fue registrado correctamente." | Pasa | [v8.1](evidencias/validaciones/v8.1.png), [v8.2](evidencias/validaciones/v8.2.png), [v8.3](evidencias/validaciones/v8.3.png) |
| V9 | Guardar otro con `" p001 "`, cambiar P002 a `P001` y editar P001 dejandole su mismo codigo | No dejar repetir el codigo, pero si dejar editar P001 con su codigo | Al repetirlo sale "El código P001 ya pertenece a otro producto." y al editar P001 con su mismo codigo sale "Los cambios se guardaron correctamente." | Pasa | [v9.1](evidencias/validaciones/v9.1.png), [v9.2](evidencias/validaciones/v9.2.png) |
| V10 | Presionar Actualizar y Eliminar sin elegir ninguna fila | No hacer nada y pedir que se elija un producto | En los dos sale "Selecciona un producto de la tabla para…" y no cambia nada | Pasa | [v10.1](evidencias/validaciones/v10.1.png), [v10.2](evidencias/validaciones/v10.2.png) |

## Excepciones (E1 a E4)

| Caso | Accion | Resultado esperado | Resultado obtenido | Estado | Evidencia |
|---|---|---|---|---|---|
| E1 | Crear otra Run configuration con `-Ddb.conf=db-prueba-e1.properties`, que tiene el puerto malo, y abrir la app | Un aviso que se entienda, que la app no se cierre y que no diga que hay 0 productos | Sale "No se pudieron cargar los productos. Detalle: No se pudo conectar con la base de datos…" y la app no se cierra | Pasa | [E1](evidencias/excepciones/E1.png) |
| E2 | Correr `PruebasDAO`, que llama al DAO directo sin la ventana: codigo P001 repetido, precio 0, categoria Ropa y stock -5 | Que PostgreSQL los rechace, que el DAO maneje la SQLException y que la base no cambie | Rechaza los 4: 23505 por el codigo repetido y 23514 por el precio, la categoria y el stock. Quedan las mismas filas que antes | Pasa | [E2](evidencias/excepciones/E2.png) |
| E3 | Seleccionar X001, borrarlo desde pgAdmin y despues presionar Actualizar en la app | Avisar que ya no existe y no decir que se guardo | Sale "El producto seleccionado ya no existe en la base de datos. Se recargará la tabla." y no dice que se guardo | Pasa | [E3](evidencias/excepciones/E3.png) |
| E4 | Revisar la consola despues de E1 y E2 | Anotar la operacion y el SQLState, sin mostrarle al usuario la contraseña ni el error tecnico | En la consola sale `[ERROR BD] operacion=buscar SQLState=08001 …` y en E2 `operacion=insertar SQLState=23505/23514`. En la alerta no sale la contraseña ni la traza | Pasa | [E4](evidencias/excepciones/E4.png), [E2](evidencias/excepciones/E2.png) |

## Restricciones de la base (sin la app)

Correr `sql/prueba-restricciones.sql` en pgAdmin, un INSERT a la vez, para comprobar que la base sola tambien rechaza los datos malos.

| Restriccion | INSERT de prueba | Error esperado | Resultado obtenido | Estado | Evidencia |
|---|---|---|---|---|---|
| UNIQUE del codigo | Insertar `P001` repetido | 23505 por el UNIQUE del codigo | Sale `duplicate key value violates unique constraint "producto_codigo_key"`, SQL state 23505 | Pasa | [1_unique-codigo](evidencias/restricciones-sql/1_unique-codigo.png) |
| CHECK del codigo (minusculas y espacios) | Insertar `' p009 '` | 23514 por `chk_producto_codigo` | La captura que hay tiene seleccionado el INSERT del codigo vacio, no el de `' p009 '`. Hay que sacarla de nuevo | Repetir | [2.1_check-codigo-minusculas-espacios](evidencias/restricciones-sql/2.1_check-codigo-minusculas-espacios.png) |
| CHECK del codigo (vacio) | Insertar `''` | 23514 por `chk_producto_codigo` | Sale `violates check constraint "chk_producto_codigo"`, SQL state 23514 | Pasa | [2.2_check-codigo-vacios](evidencias/restricciones-sql/2.2_check-codigo-vacios.png) |
| CHECK del nombre | Insertar `'  AB  '` | 23514 por el CHECK del nombre | Sale `violates check constraint "chk_nombre_longitud"`, SQL state 23514 | Pasa | [3_check-nombre-min3caracteres](evidencias/restricciones-sql/3_check-nombre-min3caracteres.png) |
| CHECK de la categoria | Insertar `'Ropa'` | 23514 por el CHECK de la categoria | Sale `violates check constraint "chk_categoria_permitida"`, SQL state 23514 | Pasa | [4_fuera-del-catalogo](evidencias/restricciones-sql/4_fuera-del-catalogo.png) |
| CHECK del precio | Insertar precio `0` | 23514 por `chk_producto_precio` | Sale `violates check constraint "chk_producto_precio"`, SQL state 23514 | Pasa | [5_check-precio-cero-negativo](evidencias/restricciones-sql/5_check-precio-cero-negativo.png) |
| CHECK del stock | Insertar stock `-1` | 23514 por `chk_producto_stock` | Sale `violates check constraint "chk_producto_stock"`, SQL state 23514 | Pasa | [6_check-stock-negativo](evidencias/restricciones-sql/6_check-stock-negativo.png) |
| NOT NULL | Insertar nombre `NULL` | 23502 | Sale `null value in column "nombre" ... violates not-null constraint`, SQL state 23502 | Pasa | [7_not-null-falta-nombre](evidencias/restricciones-sql/7_not-null-falta-nombre.png) |
