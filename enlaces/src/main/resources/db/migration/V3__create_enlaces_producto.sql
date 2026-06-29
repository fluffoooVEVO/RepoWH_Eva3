CREATE TABLE Enlaces (
    id_enlace_producto INT AUTO_INCREMENT PRIMARY KEY,
    id_enlace INT NOT NULL,
    id_producto INT NOT NULL,
    CONSTRAINT fk_enlace FOREIGN KEY (id_enlace) REFERENCES Enlace(id_enlace)
);