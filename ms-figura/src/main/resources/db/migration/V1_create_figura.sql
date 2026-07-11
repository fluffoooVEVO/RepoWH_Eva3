CREATE TABLE Figura (
    id_figura INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(60) NOT NULL,
    descripcion VARCHAR(255) NOT NULL,
    url VARCHAR(255),
    id_edicion INT NOT NULL
);