CREATE TABLE imagen (
    id_imagen INT AUTO_INCREMENT PRIMARY KEY,
    url VARCHAR(255) NOT NULL,
    orden INT NOT NULL,
    descripcion VARCHAR(255) NOT NULL,
    id_producto INT NOT NULL
);