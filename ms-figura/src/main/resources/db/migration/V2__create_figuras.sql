CREATE TABLE Figuras (
    id_producto_figura INT AUTO_INCREMENT PRIMARY KEY,
    id_figura INT NOT NULL,
    id_producto INT NOT NULL,
    CONSTRAINT fk_figura FOREIGN KEY (id_figura) REFERENCES Figura(id_figura)
);
