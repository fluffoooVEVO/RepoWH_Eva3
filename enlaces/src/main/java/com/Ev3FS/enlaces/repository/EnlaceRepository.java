package com.Ev3FS.enlaces.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.Ev3FS.enlaces.model.Enlace;

@Repository
public interface EnlaceRepository extends JpaRepository<Enlace, Integer> {}