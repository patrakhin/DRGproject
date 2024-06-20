package com.drgproject.repair.controller;

import com.drgproject.repair.dto.HomeDepotDTO;
import com.drgproject.repair.service.HomeDepotService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/home-depots")
public class HomeDepotController {

    private final HomeDepotService homeDepotService;

    public HomeDepotController(HomeDepotService homeDepotService) {
        this.homeDepotService = homeDepotService;
    }

    @GetMapping
    public List<HomeDepotDTO> getAllHomeDepots() {
        return homeDepotService.getAllHomeDepots();
    }

    @GetMapping("/{id}")
    public ResponseEntity<HomeDepotDTO> getHomeDepotById(@PathVariable Long id) {
        HomeDepotDTO homeDepotDTO = homeDepotService.getHomeDepotById(id);
        if (homeDepotDTO != null) {
            return ResponseEntity.ok(homeDepotDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/region/{regionId}")
    public ResponseEntity<List<HomeDepotDTO>> getDepotsByRegionId(@PathVariable Long regionId) {
        List<HomeDepotDTO> depots = homeDepotService.getDepotsByRegionId(regionId);
        return ResponseEntity.ok(depots);
    }

    @PostMapping
    public ResponseEntity<HomeDepotDTO> createHomeDepot(@RequestBody HomeDepotDTO homeDepotDTO) {
        HomeDepotDTO createdHomeDepot = homeDepotService.createHomeDepot(homeDepotDTO);
        if (createdHomeDepot != null) {
            return ResponseEntity.ok(createdHomeDepot);
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<HomeDepotDTO> updateHomeDepot(@PathVariable Long id, @RequestBody HomeDepotDTO homeDepotDTO) {
        HomeDepotDTO updatedHomeDepot = homeDepotService.updateHomeDepot(id, homeDepotDTO);
        if (updatedHomeDepot != null) {
            return ResponseEntity.ok(updatedHomeDepot);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHomeDepot(@PathVariable Long id) {
        boolean isDeleted = homeDepotService.deleteHomeDepot(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
