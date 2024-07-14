package com.drgproject.service;

import com.drgproject.dto.LocoBlockDto;
import com.drgproject.entity.LocoBlock;
import com.drgproject.repository.LocoBlockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class LocoBlockService {
    private final LocoBlockUniqNumService locoBlockUniqNumService;
    LocoBlockRepository locoBlockRepository;

    public LocoBlockService(LocoBlockRepository locoBlockRepository, LocoBlockUniqNumService locoBlockUniqNumService) {
        this.locoBlockRepository = locoBlockRepository;
        this.locoBlockUniqNumService = locoBlockUniqNumService;
    }

    @Transactional
    public List<LocoBlockDto> getAllLocoBlocks(){
        return locoBlockRepository.findAll().stream().map(this::convertToDTO).toList();
    }

    @Transactional
    public List<LocoBlockDto> getLocoBlocksByRegion (String region){
        return locoBlockRepository.findAll().stream()
                .filter(regions -> Objects.equals(regions.getRegion(),region))
                .map(this::convertToDTO)
                .toList();
    }

    @Transactional
    public List<LocoBlockDto> getLocoBlockByName (String name){
        return locoBlockRepository.findAll().stream()
                .filter(names -> Objects.equals(names.getBlockName(),name))
                .map(this::convertToDTO)
                .toList();
    }

    @Transactional
    public List<LocoBlockDto> getLocoBlockByRegionAndName (String region, String name){
        return locoBlockRepository.findLocoBlockByRegionAndBlockName(region, name).stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Transactional
    public LocoBlockDto getLocoBlockById(Long id) {
        return locoBlockRepository.findById(id).map(this::convertToDTO).orElse(null);
    }

    @Transactional
    public LocoBlockDto getLocoBlockByBlockNumber(String blockNumber){
        return locoBlockRepository.findLocoBlockByBlockNumber(blockNumber).map(this::convertToDTO).orElse(null);
    }

    public LocoBlockDto getLocoBlockByUniqueId(Long uniqueId) {
        return locoBlockRepository.findLocoBlockByUniqueId(uniqueId).map(this::convertToDTO).orElse(null);
    }

    @Transactional
    public LocoBlockDto createLocoBlock(LocoBlockDto locoBlockDto){
        locoBlockDto.setUniqueId(locoBlockUniqNumService.generateUniqueId(locoBlockDto.getSystemType(),
                locoBlockDto.getBlockName(), locoBlockDto.getBlockNumber()));
        LocoBlock locoBlock = convertToEntity(locoBlockDto);
        locoBlockRepository.save(locoBlock);
        return convertToDTO(locoBlock);
    }

    @Transactional
    public LocoBlockDto updateLocoBlock(Long id, LocoBlockDto locoBlockDto){
        Optional<LocoBlock> optionalLocoBlock = locoBlockRepository.findById(id);
        if(optionalLocoBlock.isPresent()){
            LocoBlock locoBlock = optionalLocoBlock.get();
            locoBlock.setSystemType(locoBlockDto.getSystemType());
            locoBlock.setBlockName(locoBlockDto.getBlockName());
            locoBlock.setBlockNumber(locoBlockDto.getBlockNumber());
            locoBlock.setRegion(locoBlockDto.getRegion());
            locoBlock.setUniqueId(locoBlockUniqNumService.generateUniqueId(locoBlock.getSystemType(),
                    locoBlock.getBlockName(), locoBlock.getBlockNumber()));
            locoBlock = locoBlockRepository.save(locoBlock);
            return convertToDTO(locoBlock);
        }
        return null;
    }

    @Transactional
    public void deleteLocoBlockByBlockNumber(String blockNumber){
        locoBlockRepository.deleteLocoBlockByBlockNumber(blockNumber);
    }

    @Transactional
    public void deleteLocoBlock(Long id){
        locoBlockRepository.deleteById(id);
    }


    private LocoBlockDto convertToDTO(LocoBlock locoBlock) {
        LocoBlockDto locoBlockDto = new LocoBlockDto(
                locoBlock.getSystemType(),
                locoBlock.getBlockName(),
                locoBlock.getBlockNumber(),
                locoBlock.getUniqueId(),
                locoBlock.getRegion()
        );
        locoBlockDto.setId(locoBlock.getId());
        return locoBlockDto;
    }

    private LocoBlock convertToEntity(LocoBlockDto locoBlockDto) {
        return new LocoBlock(
                locoBlockDto.getSystemType(),
                locoBlockDto.getBlockName(),
                locoBlockDto.getBlockNumber(),
                locoBlockDto.getUniqueId(),
                locoBlockDto.getRegion()
        );
    }
}
