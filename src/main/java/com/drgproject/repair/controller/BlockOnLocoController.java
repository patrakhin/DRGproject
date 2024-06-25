package com.drgproject.repair.controller;

import com.drgproject.repair.dto.BlockOnLocoDTO;
import com.drgproject.repair.service.BlockOnLocoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/block-on-locos")
public class BlockOnLocoController {

    private final BlockOnLocoService blockOnLocoService;

    public BlockOnLocoController(BlockOnLocoService blockOnLocoService) {
        this.blockOnLocoService = blockOnLocoService;
    }

    @GetMapping
    public ResponseEntity<List<BlockOnLocoDTO>> getAllBlockOnLocos() {
        List<BlockOnLocoDTO> blockOnLocos = blockOnLocoService.getAllBlockOnLocos();
        return new ResponseEntity<>(blockOnLocos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BlockOnLocoDTO> getBlockOnLocoById(@PathVariable Long id) {
        BlockOnLocoDTO blockOnLocoDTO = blockOnLocoService.getBlockOnLocoById(id);
        return blockOnLocoDTO != null ? new ResponseEntity<>(blockOnLocoDTO, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/id_loco/{loco_id}")
    public ResponseEntity<List<BlockOnLocoDTO>> getAllBlockByListLoco(@PathVariable Long loco_id){
        List<BlockOnLocoDTO> blockOnLocoByIdLocoList = blockOnLocoService.getAllBlockOnLocoByIdLocoList(loco_id);
        return new ResponseEntity<>(blockOnLocoByIdLocoList, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<BlockOnLocoDTO> createBlockOnLoco(@RequestBody BlockOnLocoDTO blockOnLocoDTO) {
        BlockOnLocoDTO newBlockOnLoco = blockOnLocoService.createBlockOnLoco(blockOnLocoDTO);
        return newBlockOnLoco != null ? new ResponseEntity<>(newBlockOnLoco, HttpStatus.CREATED) : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BlockOnLocoDTO> updateBlockOnLoco(@PathVariable Long id, @RequestBody BlockOnLocoDTO blockOnLocoDTO) {
        BlockOnLocoDTO updatedBlockOnLoco = blockOnLocoService.updateBlockOnLoco(id, blockOnLocoDTO);
        return updatedBlockOnLoco != null ? new ResponseEntity<>(updatedBlockOnLoco, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBlockOnLoco(@PathVariable Long id) {
        return blockOnLocoService.deleteBlockOnLoco(id) ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
