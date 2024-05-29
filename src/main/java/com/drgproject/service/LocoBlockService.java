package com.drgproject.service;

import com.drgproject.dto.LocoBlockDto;
import com.drgproject.entity.LocoBlock;
import com.drgproject.repository.LocoBlockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class LocoBlockService {
    LocoBlockRepository locoBlockRepository;

    public LocoBlockService(LocoBlockRepository locoBlockRepository) {
        this.locoBlockRepository = locoBlockRepository;
    }

    @Transactional
    public List<LocoBlockDto> getAllLocoBlocks(){
        return locoBlockRepository.findAll().stream().map(this::convertToDTO).toList();
    }

    @Transactional
    public LocoBlockDto getLocoBlockById(Long id) {
       return locoBlockRepository.findById(id).map(this::convertToDTO).orElse(null);
    }

    @Transactional
    public LocoBlockDto createLocoBlock(LocoBlockDto locoBlockDto){
        LocoBlock locoBlock = convertToEntity(locoBlockDto);
        locoBlockRepository.save(locoBlock);
        return convertToDTO(locoBlock);
    }

    @Transactional
    public LocoBlockDto updateLocoBlock(Long id, LocoBlockDto locoBlockDto){
        Optional<LocoBlock> optionalLocoBlock = locoBlockRepository.findById(id);
        if(optionalLocoBlock.isPresent()){
            LocoBlock locoBlock = optionalLocoBlock.get();
            locoBlock.setStorage(locoBlockDto.getStorage());
            locoBlock.setSystemType(locoBlockDto.getSystemType());
            locoBlock.setBlockName(locoBlockDto.getBlockName());
            locoBlock.setBlockNumber(locoBlockDto.getBlockNumber());
            locoBlock.setDateCreate(locoBlockDto.getDateCreate());
            locoBlock = locoBlockRepository.save(locoBlock);
            return convertToDTO(locoBlock);
        }
        return null;
    }

    @Transactional
    public void deleteLocoBlock(Long id){
        locoBlockRepository.deleteById(id);
    }


    private LocoBlockDto convertToDTO(LocoBlock locoBlock) {
        return new LocoBlockDto(
                locoBlock.getStorage(),
                locoBlock.getSystemType(),
                locoBlock.getBlockName(),
                locoBlock.getBlockNumber()
        );
    }

    private LocoBlock convertToEntity(LocoBlockDto locoBlockDto) {
        return new LocoBlock(
                locoBlockDto.getStorage(),
                locoBlockDto.getSystemType(),
                locoBlockDto.getBlockName(),
                locoBlockDto.getBlockNumber(),
                locoBlockDto.getDateCreate()
        );
    }
}
