package com.drgproject.repair.controller;

import com.drgproject.repair.dto.LocoListDTO;
import com.drgproject.repair.service.LocoListService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/locos")
public class LocoListController {

    private final LocoListService locoListService;

    public LocoListController(LocoListService locoListService) {
        this.locoListService = locoListService;
    }

    @GetMapping
    public List<LocoListDTO> getAllLocos() {
        return locoListService.getAllLocoLists();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocoListDTO> getLocoById(@PathVariable Long id) {
        LocoListDTO locoListDTO = locoListService.getLocoListById(id);
        if (locoListDTO != null) {
            return ResponseEntity.ok(locoListDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<LocoListDTO> createLoco(@RequestBody LocoListDTO locoListDTO) {
        LocoListDTO createdLoco = locoListService.createLocoList(locoListDTO);
        return ResponseEntity.ok(createdLoco);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LocoListDTO> updateLoco(@PathVariable Long id, @RequestBody LocoListDTO locoListDTO) {
        LocoListDTO updatedLoco = locoListService.updateLocoList(id, locoListDTO);
        if (updatedLoco != null) {
            return ResponseEntity.ok(updatedLoco);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLoco(@PathVariable Long id) {
        locoListService.deleteLocoList(id);
        return ResponseEntity.noContent().build();
    }
}
