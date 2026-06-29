-- Creamos la tabla principal
CREATE TABLE IF NOT EXISTS producto (
    id_producto INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    descripcion VARCHAR(1000),
    fecha_creacion DATE,
    id_categoria INT NOT NULL
);

-- Insertamos un par de datos de prueba para la presentacion
INSERT INTO producto (nombre, descripcion, fecha_creacion, id_categoria) 
VALUES ('Figura Space Marine', 'Figura de Warhammer 40k pintada a mano', '2026-06-20', 1);

INSERT INTO producto (nombre, descripcion, fecha_creacion, id_categoria) 
VALUES ('Funko Pop Batman', 'Edicion especial aniversario', '2026-06-20', 2);