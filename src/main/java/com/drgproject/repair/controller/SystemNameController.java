package com.drgproject.repair.controller;

import com.drgproject.repair.dto.SystemNameDTO;
import com.drgproject.repair.service.SystemNameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/system-names")
public class SystemNameController {

    private final SystemNameService systemNameService;

    public SystemNameController(SystemNameService systemNameService) {
        this.systemNameService = systemNameService;
    }

    @GetMapping
    public List<SystemNameDTO> getAllSystemNames() {
        return systemNameService.getAllSystemNames();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SystemNameDTO> getSystemNameById(@PathVariable Long id) {
        SystemNameDTO systemNameDTO = systemNameService.getSystemNameById(id);
        if (systemNameDTO != null) {
            return ResponseEntity.ok(systemNameDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<SystemNameDTO> createSystemName(@RequestBody SystemNameDTO systemNameDTO) {
        SystemNameDTO createdSystemName = systemNameService.createSystemName(systemNameDTO);
        return ResponseEntity.ok(createdSystemName);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SystemNameDTO> updateSystemName(@PathVariable Long id, @RequestBody SystemNameDTO systemNameDTO) {
        SystemNameDTO updatedSystemName = systemNameService.updateSystemName(id, systemNameDTO);
        if (updatedSystemName != null) {
            return ResponseEntity.ok(updatedSystemName);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSystemName(@PathVariable Long id) {
        boolean isDeleted = systemNameService.deleteSystemName(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
