-- Revisar restricciones
--1. UNIQUE codigo
INSERT INTO catalogo.producto (codigo, nombre, categoria, precio, stock)
VALUES ('P001', 'Producto repetido', 'Papelería', 10.00, 1);

-- 2. CHECK código: minúsculas, espacios y vacio
INSERT INTO catalogo.producto (codigo, nombre, categoria, precio, stock)
VALUES (' p009 ', 'Código sin normalizar', 'Papelería', 10.00, 1);

INSERT INTO catalogo.producto (codigo, nombre, categoria, precio, stock)
VALUES ('', 'Código vacío', 'Papelería', 10.00, 1);

-- 3. CHECK nombre: menos de 3 caracteres
INSERT INTO catalogo.producto (codigo, nombre, categoria, precio, stock)
VALUES ('X100', '  AB  ', 'Papelería', 10.00, 1);

-- 4. CHECK categoría: valor fuera del catalogo
INSERT INTO catalogo.producto (codigo, nombre, categoria, precio, stock)
VALUES ('X101', 'Camiseta', 'Ropa', 10.00, 1);

-- 5. CHECK precio: cero o negativo
INSERT INTO catalogo.producto (codigo, nombre, categoria, precio, stock)
VALUES ('X102', 'Precio cero', 'Papelería', 0, 1);

-- 6. CHECK stock: negativo
INSERT INTO catalogo.producto (codigo, nombre, categoria, precio, stock)
VALUES ('X103', 'Stock negativo', 'Papelería', 10.00, -1);

-- 7. NOT NULL: falta el nombre
INSERT INTO catalogo.producto (codigo, nombre, categoria, precio, stock)
VALUES ('X104', NULL, 'Papelería', 10.00, 1);