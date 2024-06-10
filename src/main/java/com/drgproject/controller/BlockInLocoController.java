package com.drgproject.controller;

import com.drgproject.dto.BlockInLocoDto;
import com.drgproject.exception.ResourceNotFoundException;
import com.drgproject.service.BlockInLockService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/block-in-loco")
public class BlockInLocoController {

    private final BlockInLockService blockInLockService;

    public BlockInLocoController(BlockInLockService blockInLockService) {
        this.blockInLockService = blockInLockService;
    }

    @GetMapping
    public ResponseEntity<List<BlockInLocoDto>> getAllBlocks() {
        List<BlockInLocoDto> blocks = blockInLockService.getAllBlockInLoco();
        return ResponseEntity.ok(blocks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BlockInLocoDto> getBlockById(@PathVariable Long id) {
        BlockInLocoDto block = blockInLockService.getBlockInLocoById(id);
        if (block == null) {
            throw new ResourceNotFoundException("Block not found with id " + id);
        }
        return ResponseEntity.ok(block);
    }

    @GetMapping("/number/{blockNumber}")
    public ResponseEntity<BlockInLocoDto> getBlockByNumber(@PathVariable String blockNumber) {
        BlockInLocoDto block = blockInLockService.getBlockInLocoByNumber(blockNumber);
        if (block == null) {
            throw new ResourceNotFoundException("Block not found with number " + blockNumber);
        }
        return ResponseEntity.ok(block);
    }

    @GetMapping("/name/{blockName}")
    public ResponseEntity<List<BlockInLocoDto>> getBlocksByName(@PathVariable String blockName) {
        List<BlockInLocoDto> blocks = blockInLockService.getBlockInLocoByName(blockName);
        return ResponseEntity.ok(blocks);
    }

    @GetMapping("/status/{blockStatus}")
    public ResponseEntity<List<BlockInLocoDto>> getBlocksByStatus(@PathVariable String blockStatus) {
        List<BlockInLocoDto> blocks = blockInLockService.getBlockInLocoByStatus(blockStatus);
        return ResponseEntity.ok(blocks);
    }

    @PostMapping
    public ResponseEntity<BlockInLocoDto> createBlock(@Validated @RequestBody BlockInLocoDto blockInLocoDto) {
        BlockInLocoDto createdBlock = blockInLockService.createBlockInLoco(blockInLocoDto);
        return new ResponseEntity<>(createdBlock, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BlockInLocoDto> updateBlock(@PathVariable Long id, @Validated @RequestBody BlockInLocoDto blockInLocoDto) {
        BlockInLocoDto updatedBlock = blockInLockService.updateBlockInLoco(id, blockInLocoDto);
        if (updatedBlock == null) {
            throw new ResourceNotFoundException("Block not found with id " + id);
        }
        return ResponseEntity.ok(updatedBlock);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBlock(@PathVariable Long id) {
        blockInLockService.deleteBlockInLocoById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/number/{blockNumber}")
    public ResponseEntity<Void> deleteBlockByNumber(@PathVariable String blockNumber) {
        BlockInLocoDto block = blockInLockService.getBlockInLocoByNumber(blockNumber);
        if (block == null) {
            throw new ResourceNotFoundException("Block not found with number " + blockNumber);
        }
        blockInLockService.deleteBlockInLocoByBlockNumber(blockNumber);
        return ResponseEntity.noContent().build();
    }
}
