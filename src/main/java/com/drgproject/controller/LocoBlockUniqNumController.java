package com.drgproject.controller;

import com.drgproject.dto.LocoBlockNumGenerationDto;
import com.drgproject.dto.LocoBlockNumGetterDto;
import com.drgproject.dto.LocoBlockNumParseDto;
import com.drgproject.service.LocoBlockUniqNumService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/locoblock_unique")
public class LocoBlockUniqNumController {
    private final LocoBlockUniqNumService locoBlockUniqNumService;

    public LocoBlockUniqNumController(LocoBlockUniqNumService locoBlockUniqNumService) {
        this.locoBlockUniqNumService = locoBlockUniqNumService;
    }

    @PostMapping("/generate-id")
    public ResponseEntity<LocoBlockNumGetterDto> generateUniqueId(@RequestBody LocoBlockNumGenerationDto requestDto) {
        Long uniqueId = locoBlockUniqNumService.generateUniqueId(requestDto.getBlockType(),
                requestDto.getBlockName(), requestDto.getBlockNumber());
        return ResponseEntity.ok(new LocoBlockNumGetterDto(uniqueId));
    }

    @PostMapping("/parse-id")
    public ResponseEntity<LocoBlockNumParseDto> parseUniqueId(@RequestBody LocoBlockNumGetterDto responseDto) {
        List<String> details = locoBlockUniqNumService.parseUniqueId(responseDto.getUniqueId());
        return ResponseEntity.ok(new LocoBlockNumParseDto(details));
    }
}
