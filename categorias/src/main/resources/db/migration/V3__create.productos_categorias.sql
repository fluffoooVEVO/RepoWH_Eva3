CREATE TABLE productos_categorias (
    id_producto_categoria INT AUTO_INCREMENT PRIMARY KEY,
    id_producto INT NOT NULL,
    id_categoria INT NOT NULL,
    CONSTRAINT fk_categoria FOREIGN KEY (id_categoria) REFERENCES categoria(id_categoria)
);