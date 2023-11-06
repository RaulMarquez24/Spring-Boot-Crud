USE db_springboot;

-- Crear tabla usuarios
CREATE TABLE users (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    enabled TINYINT(1) NOT NULL DEFAULT 1,
    admin TINYINT(1) NOT NULL DEFAULT 0,
    UNIQUE KEY unique_username (username)
);

-- Crear tabla authorities
CREATE TABLE authorities (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    authority VARCHAR(255) NOT NULL,
    UNIQUE INDEX user_id_authority_unique (user_id ASC, authority ASC),
    CONSTRAINT fk_authorities_users
        FOREIGN KEY (user_id)
        REFERENCES users (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- Insertar el usuario con rol ROLE_ADMIN
INSERT INTO users (username, password, enabled) VALUES ('admin', '$2a$10$t2dQcpC6wsexSCbqQb0Zq.G1oNQU5WOAhyUM70MarllcydaM1IfIa', 1, 1);

-- Asignar el rol ROLE_ADMIN al usuario
INSERT INTO authorities (user_id, authority) SELECT id, 'ROLE_ADMIN' FROM users WHERE username = 'admin';
INSERT INTO authorities (user_id, authority) SELECT id, 'ROLE_USER' FROM users WHERE username = 'admin';

-- Insertar el usuario con rol ROLE_USER
INSERT INTO users (username, password, enabled) VALUES ('user', '$2a$10$wt.Os6CDsRqhzXQhKB6NGO6J4rGncoI68EEmqNAEiblZtHHE/a2T.', 1, 1);

-- Asignar el rol ROLE_USER al usuario
INSERT INTO authorities (user_id, authority) SELECT id, 'ROLE_USER' FROM users WHERE username = 'user';

-- Insertar 50 registros de ejemplo en la tabla 'clientes' sin ID
INSERT INTO clientes (nombre, apellido, email, create_at, foto) VALUES
('Alejandro', 'Gómez', 'alejandro@example.com', NOW(), ''),
('Laura', 'Fernández', 'laura@example.com', NOW(), ''),
('José', 'López', 'jose@example.com', NOW(), ''),
('María', 'Pérez', 'maria@example.com', NOW(), ''),
('David', 'Martínez', 'david@example.com', NOW(), ''),
('Ana', 'Rodríguez', 'ana@example.com', NOW(), ''),
('Carlos', 'García', 'carlos@example.com', NOW(), ''),
('Sara', 'Sánchez', 'sara@example.com', NOW(), ''),
('Pedro', 'González', 'pedro@example.com', NOW(), ''),
('Carmen', 'Torres', 'carmen@example.com', NOW(), ''),
('Francisco', 'Ruiz', 'francisco@example.com', NOW(), ''),
('Elena', 'Díaz', 'elena@example.com', NOW(), ''),
('Antonio', 'Vargas', 'antonio@example.com', NOW(), ''),
('Isabel', 'Ortega', 'isabel@example.com', NOW(), ''),
('Miguel', 'Jiménez', 'miguel@example.com', NOW(), ''),
('Lucía', 'Hernández', 'lucia@example.com', NOW(), ''),
('Raúl', 'Flores', 'raul@example.com', NOW(), ''),
('Paula', 'Molina', 'paula@example.com', NOW(), ''),
('Javier', 'Santos', 'javier@example.com', NOW(), ''),
('Teresa', 'Castro', 'teresa@example.com', NOW(), ''),
('Pablo', 'Ramos', 'pablo@example.com', NOW(), ''),
('Eva', 'Guerrero', 'eva@example.com', NOW(), ''),
('Manuel', 'Cabrera', 'manuel@example.com', NOW(), ''),
('Marina', 'Fuentes', 'marina@example.com', NOW(), ''),
('Alberto', 'Núñez', 'alberto@example.com', NOW(), ''),
('Luis', 'Iglesias', 'luis@example.com', NOW(), ''),
('Natalia', 'Serrano', 'natalia@example.com', NOW(), ''),
('Sergio', 'Reyes', 'sergio@example.com', NOW(), ''),
('Cristina', 'Medina', 'cristina@example.com', NOW(), ''),
('Andrés', 'Herrera', 'andres@example.com', NOW(), ''),
('Beatriz', 'Giménez', 'beatriz@example.com', NOW(), ''),
('Fernando', 'Marín', 'fernando@example.com', NOW(), ''),
('Olga', 'Silva', 'olga@example.com', NOW(), ''),
('José Luis', 'Rojas', 'joseluis@example.com', NOW(), ''),
('Martha', 'Navarro', 'martha@example.com', NOW(), ''),
('Roberto', 'Peña', 'roberto@example.com', NOW(), ''),
('Silvia', 'Cortés', 'silvia@example.com', NOW(), ''),
('Ángel', 'León', 'angel@example.com', NOW(), ''),
('Lorena', 'Mendez', 'lorena@example.com', NOW(), ''),
('Joaquín', 'Hidalgo', 'joaquin@example.com', NOW(), ''),
('Sandra', 'Paredes', 'sandra@example.com', NOW(), ''),
('Jorge', 'Cruz', 'jorge@example.com', NOW(), ''),
('Nuria', 'Soto', 'nuria@example.com', NOW(), ''),
('Diego', 'Villa', 'diego@example.com', NOW(), ''),
('Rosa', 'Peinado', 'rosa@example.com', NOW(), ''),
('Manuela', 'Gallardo', 'manuela@example.com', NOW(), ''),
('Luisa', 'Sáenz', 'luisa@example.com', NOW(), ''),
('Ricardo', 'Castaño', 'ricardo@example.com', NOW(), ''),
('Verónica', 'Quintero', 'veronica@example.com', NOW(), ''),
('Guillermo', 'Escobar', 'guillermo@example.com', NOW(), '');

-- Insertar 15 registros de productos con nombres reales y la fecha actual
INSERT INTO productos (nombre, precio, create_at) VALUES
('Camiseta de algodón', 19.99, NOW()),
('Zapatillas deportivas', 59.99, NOW()),
('Pantalones vaqueros', 29.99, NOW()),
('Chaqueta de cuero', 89.99, NOW()),
('Bolso de mano', 39.99, NOW()),
('Reloj de pulsera', 79.99, NOW()),
('Gafas de sol', 14.99, NOW()),
('Sombrero de verano', 9.99, NOW()),
('Zapatos de vestir', 49.99, NOW()),
('Bicicleta de montaña', 349.99, NOW()),
('Mesa de comedor', 149.99, NOW()),
('Sofá de tres plazas', 299.99, NOW()),
('Smartphone Android', 349.99, NOW()),
('Portátil de 15 pulgadas', 599.99, NOW()),
('Televisor LED de 55 pulgadas', 799.99, NOW());

-- Crear una factura para el Cliente 1 con algunos productos
INSERT INTO facturas (descripcion, observacion, cliente_id, create_at) VALUES ('Factura para equipos de oficina', null, 1, NOW());
INSERT INTO facturas_items (cantidad, factura_id, producto_id) VALUES (1, 1, 1);
INSERT INTO facturas_items (cantidad, factura_id, producto_id) VALUES (2, 1, 4);
INSERT INTO facturas_items (cantidad, factura_id, producto_id) VALUES (1, 1, 5);

-- Crear una factura para el Cliente 2 con otros productos
INSERT INTO facturas (descripcion, observacion, cliente_id, create_at) VALUES ('Factura para suministros de oficina', null, 2, NOW());
INSERT INTO facturas_items (cantidad, factura_id, producto_id) VALUES (1, 2, 2);
INSERT INTO facturas_items (cantidad, factura_id, producto_id) VALUES (3, 2, 6);
INSERT INTO facturas_items (cantidad, factura_id, producto_id) VALUES (2, 2, 8);

-- Crear una factura para el Cliente 3 con más productos
INSERT INTO facturas (descripcion, observacion, cliente_id, create_at) VALUES ('Factura para muebles de oficina', null, 3, NOW());
INSERT INTO facturas_items (cantidad, factura_id, producto_id) VALUES (2, 3, 3);
INSERT INTO facturas_items (cantidad, factura_id, producto_id) VALUES (1, 3, 7);
INSERT INTO facturas_items (cantidad, factura_id, producto_id) VALUES (3, 3, 9);


