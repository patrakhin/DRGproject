package com.drgproject.repair.controller;

import com.drgproject.repair.dto.LocoListDTO;
import com.drgproject.repair.service.LocoListService;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<List<LocoListDTO>> getAllLocos() {
        List<LocoListDTO> locoLists = locoListService.getAllLocoLists();
        return new ResponseEntity<>(locoLists, HttpStatus.OK);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<LocoListDTO> getLocoById(@PathVariable Long id) {
        LocoListDTO locoListDTO = locoListService.getLocoListById(id);
        if (locoListDTO != null) {
            return ResponseEntity.ok(locoListDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{number_loco}")
    public ResponseEntity<LocoListDTO> getLocoByNumberLoco(@PathVariable String number_loco) {
        LocoListDTO locoListDTO = locoListService.getLocoListByNumberLoco(number_loco);
        if (locoListDTO != null) {
            return ResponseEntity.ok(locoListDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{number_loco}/{type_loco}")
    public ResponseEntity<LocoListDTO> getLocoByNumberLocoAndType(@PathVariable String number_loco, @PathVariable String type_loco) {
        LocoListDTO locoListDTO = locoListService.getLocoListByNumberLocoAndTypeLoco(number_loco, type_loco);
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
        return locoListService.deleteLocoList(id) ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
