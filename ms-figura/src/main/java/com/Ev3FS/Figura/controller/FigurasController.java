package com.Ev3FS.Figura.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Ev3FS.Figura.DTO.FigurasDTO;
import com.Ev3FS.Figura.service.FigurasService;

@RestController
@RequestMapping("/api/v1/figuras")
public class FigurasController {

    @Autowired
    private FigurasService figurasService;

    @GetMapping
    public ResponseEntity<List<FigurasDTO>> obtenerFiguras() {
        return ResponseEntity.ok(figurasService.obtenerTodos());
    }

    @PostMapping
    public ResponseEntity<FigurasDTO> crearFiguras(@RequestBody FigurasDTO dto) {
        return ResponseEntity.ok(figurasService.guardarFigura(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FigurasDTO> actualizarFiguras(@PathVariable("id") Integer id, @RequestBody FigurasDTO dto) {
        return ResponseEntity.ok(figurasService.actualizarFigura(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarFiguras(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(figurasService.eliminarFigura(id));
    }
}
