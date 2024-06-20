package com.drgproject.repair.controller;

import com.drgproject.repair.dto.RepairHistoryDto;
import com.drgproject.repair.service.RepairHistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/repair-history")
public class RepairHistoryController {


    private final RepairHistoryService repairHistoryService;

    public RepairHistoryController(RepairHistoryService repairHistoryService) {
        this.repairHistoryService = repairHistoryService;
    }

    @GetMapping
    public List<RepairHistoryDto> getAllRepairHistories() {
        return repairHistoryService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RepairHistoryDto> getRepairHistoryById(@PathVariable Long id) {
        Optional<RepairHistoryDto> repairHistoryDTO = repairHistoryService.findById(id);
        return repairHistoryDTO.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public RepairHistoryDto createRepairHistory(@RequestBody RepairHistoryDto repairHistoryDTO) {
        return repairHistoryService.save(repairHistoryDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RepairHistoryDto> updateRepairHistory(@PathVariable Long id, @RequestBody RepairHistoryDto repairHistoryDTO) {
        Optional<RepairHistoryDto> existingRepairHistory = repairHistoryService.findById(id);

        if (existingRepairHistory.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        RepairHistoryDto updatedRepairHistoryDTO = repairHistoryService.save(repairHistoryDTO);
        return ResponseEntity.ok(updatedRepairHistoryDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRepairHistory(@PathVariable Long id) {
        Optional<RepairHistoryDto> existingRepairHistory = repairHistoryService.findById(id);

        if (existingRepairHistory.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        repairHistoryService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

