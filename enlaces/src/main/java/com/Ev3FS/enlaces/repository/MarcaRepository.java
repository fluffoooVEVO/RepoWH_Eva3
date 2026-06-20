package com.Ev3FS.enlaces.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.Ev3FS.enlaces.model.Marca;

@Repository
public interface MarcaRepository extends JpaRepository<Marca, Integer> {}