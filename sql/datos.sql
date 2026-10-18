-- Insertar los productos
INSERT INTO catalogo.producto(codigo, nombre, categoria, precio, stock)
VALUES
    ('P001', 'Cuaderno universitario', 'Papelería', 65.00, 25),
    ('P002', 'Cuaderno cuadriculado', 'Papelería', 72.00, 12),
    ('P003', 'Lapicero azul', 'Papelería', 12.00, 80),
    ('P004', 'Resma de papel carta', 'Papelería',185.00, 0),
    ('T001', 'Memoria USB 32 GB', 'Tecnología', 210.00, 8),
    ('T002', 'Mouse inalámbrico', 'Tecnología', 320, 6),
    ('T003', 'Teclado USB', 'Tecnología',180.00, 4),
    ('T004', 'Cable HDMI', 'Tecnología', 180.00, 0),
    ('A001', 'Mochila escolar', 'Accesorios', 850.00, 5),
    ('A002', 'Estuche de lápices', 'Accesorios', 95.00, 18),
    ('A003', 'Botella reutilizable', 'Accesorios', 240.00, 0),
    ('A004', 'Regla metalica', 'Accesorios', 45.00, 20);


-- Arreglar columnas mal integradas
UPDATE catalogo.producto SET precio=72.50 WHERE codigo= 'P002';
UPDATE catalogo.producto SET precio=450.00 WHERE codigo='T003';

--Revisar
SELECT * FROM catalogo.producto ORDER BY id_producto;


-- Revisar restricciones
INSERT INTO catalogo.producto(codigo, nombre, categoria, precio, stock)
VALUES ('P31231', 'Cuaderno rayado', 'Cuaderno', 150.023, 0);

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

-- 8. IDENTITY
INSERT INTO catalogo.producto (id_producto, codigo, nombre, categoria, precio, stock)
VALUES (99, 'X105', 'Id a mano', 'Papelería', 10.00, 1);