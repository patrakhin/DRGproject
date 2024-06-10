package com.drgproject.controller;

import com.drgproject.dto.LocomotiveDto;
import com.drgproject.exception.InvalidDataException;
import com.drgproject.exception.ResourceNotFoundException;
import com.drgproject.service.LocomotiveService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locomotives")
public class LocomotiveController {

    private final LocomotiveService locomotiveService;

    public LocomotiveController(LocomotiveService locomotiveService) {
        this.locomotiveService = locomotiveService;
    }

    @GetMapping
    public List<LocomotiveDto> getAllLocomotives() {
        return locomotiveService.getAllLocomotive();
    }

    @GetMapping("/{id}")
    public LocomotiveDto getLocomotiveById(@PathVariable long id) {
        LocomotiveDto locomotive = locomotiveService.getLocomotiveById(id);
        if (locomotive == null) {
            throw new ResourceNotFoundException("Locomotive not found with id: " + id);
        }
        return locomotive;
    }

    @GetMapping("/number/{locomotiveNumber}")
    public List<LocomotiveDto> getLocomotiveByNumber(@PathVariable String locomotiveNumber) {
        return locomotiveService.getLocomotiveByNumber(locomotiveNumber);
    }

    @GetMapping("/number/{locomotiveNumber}/section/{section}")
    public LocomotiveDto getLocomotiveByNumberAndSection(@PathVariable String locomotiveNumber, @PathVariable String section) {
        LocomotiveDto locomotive = locomotiveService.getLocomotiveByNumberAndSection(locomotiveNumber, section);
        if (locomotive == null) {
            throw new ResourceNotFoundException("Locomotive not found with number: " + locomotiveNumber + " and section: " + section);
        }
        return locomotive;
    }

    @GetMapping("/systemType/{systemType}")
    public List<LocomotiveDto> getLocomotiveBySystemType(@PathVariable String systemType) {
        return locomotiveService.getLocomotiveBySystemType(systemType);
    }

    @GetMapping("/homeDepot/{homeDepot}")
    public List<LocomotiveDto> getLocomotiveByHomeDepot(@PathVariable String homeDepot) {
        return locomotiveService.getLocomotiveByHomeDepot(homeDepot);
    }

    @PostMapping
    public LocomotiveDto createLocomotive(@Valid @RequestBody LocomotiveDto locomotiveDto) {
        if (locomotiveDto.getId() != null) {
            throw new InvalidDataException("New locomotive cannot have an ID");
        }
        return locomotiveService.createLocomotive(locomotiveDto);
    }

    @PutMapping("/{id}")
    public LocomotiveDto updateLocomotive(@PathVariable long id, @Valid @RequestBody LocomotiveDto locomotiveDto) {
        LocomotiveDto updatedLocomotive = locomotiveService.updateLocomotive(id, locomotiveDto);
        if (updatedLocomotive == null) {
            throw new ResourceNotFoundException("Locomotive not found with id: " + id);
        }
        return updatedLocomotive;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocomotive(@PathVariable long id) {
        locomotiveService.deleteLocomotive(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/number/{locomotiveNumber}/section/{section}")
    public ResponseEntity<Void> deleteLocomotiveByNumberAndSection(@PathVariable String locomotiveNumber, @PathVariable String section) {
        LocomotiveDto locomotive = locomotiveService.getLocomotiveByNumberAndSection(locomotiveNumber, section);
        if (locomotive == null) {
            throw new ResourceNotFoundException("Locomotive not found with number: " + locomotiveNumber + " and section: " + section);
        }
        locomotiveService.deleteLocomotiveByNumberAndSection(locomotiveNumber, section);
        return ResponseEntity.noContent().build();
    }
}
