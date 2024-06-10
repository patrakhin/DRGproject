package com.drgproject.service;

import com.drgproject.dto.BlockInLocoDto;
import com.drgproject.entity.BlockInLoco;
import com.drgproject.repository.BlockInLocoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BlockInLockService {

    private final BlockInLocoRepository blockInLocoRepository;

    public BlockInLockService(BlockInLocoRepository blockInLocoRepository) {
        this.blockInLocoRepository = blockInLocoRepository;
    }

    @Transactional
    public List<BlockInLocoDto> getAllBlockInLoco(){
        return blockInLocoRepository.findAll().stream().map(this::convertToDto).toList();
    }

    @Transactional
    public BlockInLocoDto getBlockInLocoById(long id){
        return blockInLocoRepository.findById(id).map(this::convertToDto).orElse(null);
    }

    @Transactional
    public BlockInLocoDto getBlockInLocoByNumber(String blockNumber){
        return blockInLocoRepository.findBlockInLocoByBlockNumber(blockNumber).map(this::convertToDto).orElse(null);
    }

    @Transactional
    public List<BlockInLocoDto> getBlockInLocoByName(String blockName){
        return blockInLocoRepository.findBlockInLocoByBlockName(blockName).stream().map(this::convertToDto).toList();
    }

    @Transactional
    public List<BlockInLocoDto> getBlockInLocoByStatus(String blockStatus){
        return blockInLocoRepository.findBlockInLocoByBlockStatus(blockStatus).stream().map(this::convertToDto).toList();
    }

    @Transactional
    public BlockInLocoDto createBlockInLoco(BlockInLocoDto blockInLocoDto){
        BlockInLoco blockInLoco = convertToEntity(blockInLocoDto);
        blockInLoco = blockInLocoRepository.save(blockInLoco);
        return convertToDto(blockInLoco);
    }

    @Transactional
    public BlockInLocoDto updateBlockInLoco(long id, BlockInLocoDto blockInLocoDto){
        Optional<BlockInLoco> blockInLocoOptional = blockInLocoRepository.findById(id);
        if(blockInLocoOptional.isPresent()){
            BlockInLoco blockInLoco = blockInLocoOptional.get();
            blockInLoco.setBlockNumber(blockInLocoDto.getBlockNumber());
            blockInLoco.setBlockName(blockInLocoDto.getBlockName());
            blockInLoco.setBlockStatus(blockInLocoDto.getBlockStatus());
            blockInLoco = blockInLocoRepository.save(blockInLoco);
            return convertToDto(blockInLoco);
        }
        return null;
    }

    @Transactional
    public void deleteBlockInLocoById(long id){
        blockInLocoRepository.deleteById(id);
    }

    @Transactional
    public void deleteBlockInLocoByBlockNumber(String blockNumber){
        blockInLocoRepository.deleteById(getBlockInLocoByNumber(blockNumber).getId());
    }

    private BlockInLocoDto convertToDto(BlockInLoco blockInLoco){
        BlockInLocoDto blockInLocoDto = new BlockInLocoDto();
        blockInLocoDto.setId(blockInLoco.getId());
        blockInLocoDto.setBlockNumber(blockInLoco.getBlockNumber());
        blockInLocoDto.setBlockName(blockInLoco.getBlockName());
        blockInLocoDto.setBlockStatus(blockInLoco.getBlockStatus());
        blockInLocoDto.setDateCreate(blockInLoco.getDateCreate());
        blockInLocoDto.setLocomotive(blockInLoco.getLocomotive());
        return blockInLocoDto;
    }

    private BlockInLoco convertToEntity(BlockInLocoDto blockInLocoDto){
        BlockInLoco blockInLoco = new BlockInLoco();
        blockInLoco.setBlockNumber(blockInLocoDto.getBlockNumber());
        blockInLoco.setBlockName(blockInLocoDto.getBlockName());
        blockInLoco.setBlockStatus(blockInLocoDto.getBlockStatus());
        return blockInLoco;
    }

}
