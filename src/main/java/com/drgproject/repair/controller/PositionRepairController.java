package com.drgproject.repair.controller;

import com.drgproject.repair.dto.PositionRepairDTO;
import com.drgproject.repair.service.PositionRepairService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/position-repairs")
public class PositionRepairController {


    private final PositionRepairService positionRepairService;

    public PositionRepairController(PositionRepairService positionRepairService) {
        this.positionRepairService = positionRepairService;
    }

    @GetMapping
    public List<PositionRepairDTO> getAllPositionRepairs() {
        return positionRepairService.getAllPositionRepairs();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PositionRepairDTO> getPositionRepairById(@PathVariable Long id) {
        PositionRepairDTO positionRepairDTO = positionRepairService.getPositionRepairById(id);
        if (positionRepairDTO != null) {
            return ResponseEntity.ok(positionRepairDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<PositionRepairDTO> createPositionRepair(@RequestBody PositionRepairDTO positionRepairDTO) {
        PositionRepairDTO createdPositionRepair = positionRepairService.createPositionRepair(positionRepairDTO);
        return ResponseEntity.ok(createdPositionRepair);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PositionRepairDTO> updatePositionRepair(@PathVariable Long id, @RequestBody PositionRepairDTO positionRepairDTO) {
        PositionRepairDTO updatedPositionRepair = positionRepairService.updatePositionRepair(id, positionRepairDTO);
        if (updatedPositionRepair != null) {
            return ResponseEntity.ok(updatedPositionRepair);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePositionRepair(@PathVariable Long id) {
        boolean isDeleted = positionRepairService.deletePositionRepair(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
