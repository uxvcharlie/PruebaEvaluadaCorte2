-- Crear base de datos de la farmacia
CREATE DATABASE papeleria_campus;

-- CREAR ESQUEMA
CREATE SCHEMA catalogo;

-- CREAR TABLA producto
CREATE TABLE catalogo.producto (
id_producto SERIAL PRIMARY KEY,
codigo VARCHAR(10) NOT NULL UNIQUE
CONSTRAINT chk_producto_codigo
CHECK (LENGTH(TRIM(codigo)) >= 1),

nombre VARCHAR(80) NOT NULL,
CONSTRAINT chk_nombre_longitud
CHECK (LENGTH(TRIM(nombre)) BETWEEN 3 AND 80),


categoria VARCHAR(20) NOT NULL,
CONSTRAINT chk_categoria_permitida
CHECK (categoria IN ('Papelería', 'Tecnología', 'Accesorios')),

precio NUMERIC(10,2) NOT NULL,
CONSTRAINT chk_producto_precio
CHECK (precio >= 0.01),

stock INTEGER NOT NULL,
CONSTRAINT chk_producto_stock
CHECK (stock BETWEEN 0 AND 2147483647)
);

