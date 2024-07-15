package com.drgproject.repair.service;

import com.drgproject.repair.dto.BlockRemovalDto;
import com.drgproject.repair.entity.BlockOnLoco;
import com.drgproject.repair.entity.BlockRemoval;
import com.drgproject.repair.entity.LocoList;
import com.drgproject.repair.repository.BlockOnLocoRepository;
import com.drgproject.repair.repository.BlockRemovalRepository;
import com.drgproject.repair.repository.LocoListRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class BlockRemovalService {

    private final BlockRemovalRepository blockRemovalRepository;
    private final BlockOnLocoRepository blockOnLocoRepository;
    private final LocoListRepository locoListRepository;

    public BlockRemovalService(BlockRemovalRepository blockRemovalRepository, BlockOnLocoRepository blockOnLocoRepository,
                               LocoListRepository locoListRepository) {
        this.blockRemovalRepository = blockRemovalRepository;
        this.blockOnLocoRepository = blockOnLocoRepository;
        this.locoListRepository = locoListRepository;
    }

    public List<BlockRemovalDto> getAllBlockRemoval(){
        return blockRemovalRepository.findAll().stream().map(this::convertToDto).toList();
    }

    public BlockRemovalDto getBlockRemovalById(Long id){
        return blockRemovalRepository.findById(id).map(this::convertToDto).orElse(null);
    }

    public List<BlockRemovalDto> getBlockRemovalByRegion(String region){
        List <BlockRemoval> blockRemovals = blockRemovalRepository.
                findBlockRemovalByRegion(region).orElseThrow(()-> new IllegalArgumentException("Регион не найден"));
        return blockRemovals.stream().map(this::convertToDto).toList();
    }

    public Page<BlockRemovalDto> getBlockRemovalByRegionAndHomeDepot(String region, String homeDepot, Pageable pageable) {
        return blockRemovalRepository.findBlockRemovalByRegionAndHomeDepot(region, homeDepot, pageable)
                .map(this::convertToDto);
    }

    public List<BlockRemovalDto> getBlockRemovalByTypeLocoAndNumberLoco(String typeLoco, String numberLoco){
        List <BlockRemoval> blockRemovals = blockRemovalRepository.
                findBlockRemovalByTypeLocoAndLocoNumber(typeLoco, numberLoco)
                .orElseThrow(()-> new IllegalArgumentException("Локомотив " + typeLoco + " и №" + numberLoco + " не найден"));
        return blockRemovals.stream().map(this::convertToDto).toList();
    }

    public BlockRemovalDto getBlockRemovalBySystemTypeAndBlockNameAndBlockNumber(String systemType, String blockName, String blockNumber){
        BlockRemoval blockRemoval = blockRemovalRepository
                .findBlockRemovalBySystemTypeAndBlockNameAndBlockNumber(systemType, blockName, blockNumber)
                .orElseThrow(()-> new IllegalArgumentException("Блок не найден"));
        return convertToDto(blockRemoval);
    }

    //Создание демонтажа блока
    public BlockRemovalDto createBlockRemoval(BlockRemovalDto blockRemovalDto){
        BlockRemoval blockRemoval = new BlockRemoval();
        blockRemoval.setTypeLoco(blockRemovalDto.getTypeLoco());
        blockRemoval.setLocoNumber(blockRemovalDto.getBlockNumber());
        blockRemoval.setRegion(blockRemovalDto.getRegion());
        blockRemoval.setHomeDepot(blockRemovalDto.getHomeDepot());
        blockRemoval.setSystemType(blockRemovalDto.getSystemType());
        blockRemoval.setBlockName(blockRemovalDto.getBlockName());
        blockRemoval.setBlockNumber(blockRemovalDto.getBlockNumber());
        blockRemoval.setPosition(blockRemovalDto.getPosition());
        blockRemoval.setNumberTable(blockRemovalDto.getNumberTable());
        blockRemoval = blockRemovalRepository.save(blockRemoval);
        return convertToDto(blockRemoval);
    }

    //демонтаж блока из локомотива
    public BlockRemovalDto blockRemovalFromLoco(BlockRemovalDto blockRemovalDto){
        Optional<BlockOnLoco> blockOnLoco = blockOnLocoRepository
                .findBlockOnLocoByBlockNameAndBlockNumber(blockRemovalDto.getBlockName(), blockRemovalDto.getBlockNumber());
        BlockOnLoco blockOnLoco1 = blockOnLoco.orElse(null);
        if (blockOnLoco1 == null) {
            throw new IllegalArgumentException("Блок с  наименованием " + blockRemovalDto.getBlockName()
                    + "и таким № " + blockRemovalDto.getBlockNumber() + " не найден");
        }
        Long blockOnLocoId = blockOnLoco1.getId();
        blockOnLocoRepository.deleteById(blockOnLocoId);
        return createBlockRemoval(blockRemovalDto);
    }

    //отмена демонтажа блока из локомотива
    public void cancelBlockRemovalFromLoco(BlockRemovalDto blockRemovalDto){
        Optional<LocoList> locoList = locoListRepository
                .findLocoListByLocoNumberAndTypeLoco(blockRemovalDto.getLocoNumber(), blockRemovalDto.getTypeLoco());
        if (locoList.isEmpty()) {
            throw new IllegalArgumentException("Локомотив " + blockRemovalDto.getTypeLoco() + " с № "
                    + blockRemovalDto.getLocoNumber() + " не найден");
        }
        BlockOnLoco blockOnLoco = new BlockOnLoco(blockRemovalDto.getBlockName(), blockRemovalDto.getBlockNumber());
        blockOnLoco.setLocoList(locoList.get());
        blockOnLocoRepository.save(blockOnLoco);
        Optional<BlockRemoval> blockRemoval = blockRemovalRepository
                .findBlockRemovalBySystemTypeAndBlockNameAndBlockNumber(blockRemovalDto.getSystemType(), blockRemovalDto.getBlockName(), blockRemovalDto.getBlockNumber());
        if (blockRemoval.isEmpty()){
            throw new IllegalArgumentException("Блок с  наименованием " + blockRemovalDto.getBlockName()
                    + "и таким № " + blockRemovalDto.getBlockNumber() + " не найден");
        }
        blockRemovalRepository.deleteById(blockRemoval.get().getId());
    }

    private BlockRemovalDto convertToDto(BlockRemoval blockRemoval){
        BlockRemovalDto blockRemovalDto = new BlockRemovalDto();
        blockRemovalDto.setId(blockRemoval.getId());
        blockRemovalDto.setLocoNumber(blockRemoval.getLocoNumber());
        blockRemovalDto.setRegion(blockRemoval.getRegion());
        blockRemovalDto.setHomeDepot(blockRemoval.getHomeDepot());
        blockRemovalDto.setSystemType(blockRemoval.getSystemType());
        blockRemovalDto.setBlockName(blockRemoval.getBlockName());
        blockRemovalDto.setBlockNumber(blockRemoval.getBlockNumber());
        blockRemovalDto.setPosition(blockRemoval.getPosition());
        blockRemovalDto.setNumberTable(blockRemoval.getNumberTable());
        blockRemovalDto.setDateCreate(blockRemoval.getDateCreate());
        return blockRemovalDto;
    }

    private BlockRemoval convertToEntity(BlockRemovalDto blockRemovalDto){
        BlockRemoval blockRemoval = new BlockRemoval();
        blockRemoval.setTypeLoco(blockRemovalDto.getTypeLoco());
        blockRemoval.setLocoNumber(blockRemovalDto.getBlockNumber());
        blockRemoval.setRegion(blockRemovalDto.getRegion());
        blockRemoval.setHomeDepot(blockRemovalDto.getHomeDepot());
        blockRemoval.setSystemType(blockRemovalDto.getSystemType());
        blockRemoval.setBlockName(blockRemovalDto.getBlockName());
        blockRemoval.setBlockNumber(blockRemovalDto.getBlockNumber());
        blockRemoval.setPosition(blockRemovalDto.getPosition());
        blockRemoval.setNumberTable(blockRemovalDto.getNumberTable());
        return blockRemoval;
    }
}
