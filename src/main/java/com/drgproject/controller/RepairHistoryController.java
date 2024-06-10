package com.drgproject.controller;

import com.drgproject.dto.RepairHistoryDto;
import com.drgproject.exception.ResourceNotFoundException;
import com.drgproject.service.RepairHistoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/repair-history")
public class RepairHistoryController {

    private final RepairHistoryService repairHistoryService;

    public RepairHistoryController(RepairHistoryService repairHistoryService) {
        this.repairHistoryService = repairHistoryService;
    }

    @PostMapping
    public ResponseEntity<RepairHistoryDto> createRepairHistory(@RequestBody RepairHistoryDto repairHistoryDto) {
        RepairHistoryDto createdRepairHistory = repairHistoryService.createRepairHistory(repairHistoryDto);
        return new ResponseEntity<>(createdRepairHistory, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RepairHistoryDto> getRepairHistoryById(@PathVariable Long id) {
        RepairHistoryDto repairHistory = repairHistoryService.getRepairHistoryById(id);
        if (repairHistory == null) {
            throw new ResourceNotFoundException("RepairHistory not found with id: " + id);
        }
        return new ResponseEntity<>(repairHistory, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<RepairHistoryDto>> getAllRepairHistories() {
        List<RepairHistoryDto> repairHistories = repairHistoryService.getAllRepairHistories();
        return new ResponseEntity<>(repairHistories, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RepairHistoryDto> updateRepairHistory(@PathVariable Long id,
                                                                @RequestBody RepairHistoryDto repairHistoryDto) {
        RepairHistoryDto updatedRepairHistory = repairHistoryService.updateRepairHistory(id, repairHistoryDto);
        if (updatedRepairHistory == null) {
            throw new ResourceNotFoundException("RepairHistory not found with id: " + id);
        }
        return new ResponseEntity<>(updatedRepairHistory, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRepairHistory(@PathVariable Long id) {
        repairHistoryService.deleteRepairHistory(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
