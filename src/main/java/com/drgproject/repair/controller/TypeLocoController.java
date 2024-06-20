package com.drgproject.repair.controller;

import com.drgproject.repair.dto.TypeLocoDTO;
import com.drgproject.repair.service.TypeLocoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/type-locos")
public class TypeLocoController {

    private final TypeLocoService typeLocoService;

    public TypeLocoController(TypeLocoService typeLocoService) {
        this.typeLocoService = typeLocoService;
    }

    @GetMapping
    public List<TypeLocoDTO> getAllTypeLocos() {
        return typeLocoService.getAllTypeLocos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TypeLocoDTO> getTypeLocoById(@PathVariable Long id) {
        TypeLocoDTO typeLocoDTO = typeLocoService.getTypeLocoById(id);
        if (typeLocoDTO != null) {
            return ResponseEntity.ok(typeLocoDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<TypeLocoDTO> createTypeLoco(@RequestBody TypeLocoDTO typeLocoDTO) {
        TypeLocoDTO createdTypeLoco = typeLocoService.createTypeLoco(typeLocoDTO);
        return ResponseEntity.ok(createdTypeLoco);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TypeLocoDTO> updateTypeLoco(@PathVariable Long id, @RequestBody TypeLocoDTO typeLocoDTO) {
        TypeLocoDTO updatedTypeLoco = typeLocoService.updateTypeLoco(id, typeLocoDTO);
        if (updatedTypeLoco != null) {
            return ResponseEntity.ok(updatedTypeLoco);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTypeLoco(@PathVariable Long id) {
        boolean isDeleted = typeLocoService.deleteTypeLoco(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
