package com.drgproject.repair.service;

import com.drgproject.repair.dto.BlockNameDTO;
import com.drgproject.repair.entity.BlockName;
import com.drgproject.repair.entity.SystemName;
import com.drgproject.repair.repository.BlockNameRepository;
import com.drgproject.repair.repository.SystemNameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BlockNameService {

    private BlockNameRepository blockNameRepository;
    private SystemNameRepository systemNameRepository;

    public BlockNameService(BlockNameRepository blockNameRepository, SystemNameRepository systemNameRepository) {
        this.blockNameRepository = blockNameRepository;
        this.systemNameRepository = systemNameRepository;
    }

    @Transactional
    public BlockNameDTO createBlockName(BlockNameDTO blockNameDTO) {
        SystemName systemName = systemNameRepository.findById(blockNameDTO.getSystemNameId())
                .orElseThrow(() -> new IllegalArgumentException("Неверное systemNameId"));

        BlockName blockName = new BlockName(blockNameDTO.getBlockName(), systemName);
        blockName = blockNameRepository.save(blockName);

        return convertToDTO(blockName);
    }

    @Transactional(readOnly = true)
    public BlockNameDTO getBlockNameById(Long id) {
        BlockName blockName = blockNameRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Блок с таким наименованием не найден"));
        return convertToDTO(blockName);
    }

    @Transactional(readOnly = true)
    public List<BlockNameDTO> getAllBlockNames() {
        return blockNameRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public BlockNameDTO updateBlockName(Long id, BlockNameDTO blockNameDTO) {
        BlockName blockName = blockNameRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Блок с таким наименованием не найден"));

        SystemName systemName = systemNameRepository.findById(blockNameDTO.getSystemNameId())
                .orElseThrow(() -> new IllegalArgumentException("Неверное systemNameId"));

        blockName.setBlockName(blockNameDTO.getBlockName());
        blockName.setSystemName(systemName);

        blockName = blockNameRepository.save(blockName);

        return convertToDTO(blockName);
    }

    @Transactional
    public boolean deleteBlockName(Long id) {
        if (blockNameRepository.existsById(id)){
            blockNameRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private BlockNameDTO convertToDTO(BlockName blockName) {
        return new BlockNameDTO(
                blockName.getId(),
                blockName.getBlockName(),
                blockName.getSystemName().getId()
        );
    }
}
