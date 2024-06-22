package com.drgproject.repair.service;

import com.drgproject.repair.dto.BlockOnLocoDTO;
import com.drgproject.repair.entity.BlockOnLoco;
import com.drgproject.repair.entity.LocoList;
import com.drgproject.repair.repository.BlockOnLocoRepository;
import com.drgproject.repair.repository.LocoListRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BlockOnLocoService {

    private final BlockOnLocoRepository blockOnLocoRepository;
    private final LocoListRepository locoListRepository;

    public BlockOnLocoService(BlockOnLocoRepository blockOnLocoRepository, LocoListRepository locoListRepository) {
        this.blockOnLocoRepository = blockOnLocoRepository;
        this.locoListRepository = locoListRepository;
    }

    public List<BlockOnLocoDTO> getAllBlockOnLocos() {
        return blockOnLocoRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public BlockOnLocoDTO getBlockOnLocoById(Long id) {
        Optional<BlockOnLoco> blockOnLoco = blockOnLocoRepository.findById(id);
        return blockOnLoco.map(this::convertToDTO).orElse(null);
    }

    public BlockOnLocoDTO createBlockOnLoco(BlockOnLocoDTO blockOnLocoDTO) {
        Optional<LocoList> locoList = locoListRepository.findById(blockOnLocoDTO.getLocoListId());
        if (locoList.isPresent()) {
            BlockOnLoco blockOnLoco = new BlockOnLoco(blockOnLocoDTO.getBlockName(), blockOnLocoDTO.getBlockNumber());
            blockOnLoco.setLocoList(locoList.get());
            blockOnLoco = blockOnLocoRepository.save(blockOnLoco);
            return convertToDTO(blockOnLoco);
        }
        return null;
    }

    public BlockOnLocoDTO updateBlockOnLoco(Long id, BlockOnLocoDTO blockOnLocoDTO) {
        Optional<BlockOnLoco> optionalBlockOnLoco = blockOnLocoRepository.findById(id);
        Optional<LocoList> locoList = locoListRepository.findById(blockOnLocoDTO.getLocoListId());
        if (optionalBlockOnLoco.isPresent() && locoList.isPresent()) {
            BlockOnLoco blockOnLoco = optionalBlockOnLoco.get();
            blockOnLoco.setBlockName(blockOnLocoDTO.getBlockName());
            blockOnLoco.setBlockNumber(blockOnLocoDTO.getBlockNumber());
            blockOnLoco.setLocoList(locoList.get());
            blockOnLoco = blockOnLocoRepository.save(blockOnLoco);
            return convertToDTO(blockOnLoco);
        }
        return null;
    }

    public boolean deleteBlockOnLoco(Long id) {
        if (blockOnLocoRepository.existsById(id)) {
            blockOnLocoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public BlockOnLocoDTO convertToDTO(BlockOnLoco blockOnLoco) {
        return new BlockOnLocoDTO(blockOnLoco.getId(), blockOnLoco.getBlockName(), blockOnLoco.getBlockNumber(), blockOnLoco.getLocoList().getId());
    }

    public BlockOnLoco convertToEntity(BlockOnLocoDTO blockOnLocoDTO) {
        BlockOnLoco blockOnLoco = new BlockOnLoco(blockOnLocoDTO.getBlockName(), blockOnLocoDTO.getBlockNumber());
        Optional<LocoList> locoList = locoListRepository.findById(blockOnLocoDTO.getLocoListId());
        locoList.ifPresent(blockOnLoco::setLocoList);
        return blockOnLoco;
    }
}
