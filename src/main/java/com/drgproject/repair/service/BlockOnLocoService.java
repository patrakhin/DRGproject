package com.drgproject.repair.service;

import com.drgproject.repair.dto.BlockOnLocoDTO;
import com.drgproject.repair.entity.BlockOnLoco;
import com.drgproject.repair.entity.LocoList;
import com.drgproject.repair.repository.BlockOnLocoRepository;
import com.drgproject.repair.repository.LocoListRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


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
                .toList();
    }

    public BlockOnLocoDTO getBlockOnLocoById(Long id){
        return blockOnLocoRepository.findById(id).map(this::convertToDTO).orElse(null);
    }

    public BlockOnLocoDTO getBlockOnLocoByLocoNumberAndTypeLoco(String locoNumber, String typeLoco) {
        Optional<LocoList> locoList = locoListRepository.findLocoListByLocoNumberAndTypeLoco(locoNumber, typeLoco);
        LocoList locoList1 = locoList.orElse(null);
        if (locoList1 == null) {
            throw new IllegalArgumentException("Локомотив с  № " + locoNumber + " не найден");
        }
        Long locoListId = locoList1.getId();
        Optional<BlockOnLoco> blockOnLoco = blockOnLocoRepository.findById(locoListId);
        return blockOnLoco.map(this::convertToDTO).orElse(null);
    }

    public List<BlockOnLocoDTO> getAllBlockOnLocoByLocoNumberAndTypeLoco(String locoNumber, String typeLoco){
        Optional<LocoList> locoList = locoListRepository.findLocoListByLocoNumberAndTypeLoco(locoNumber, typeLoco);
        LocoList locoList1 = locoList.orElse(null);
        if (locoList1 == null) {
            throw new IllegalArgumentException("Локомотив с  № " + locoNumber + " не найден");
        }
        Long locoListId = locoList1.getId();
        Optional<List<BlockOnLoco>> blockOnLocoByListLocoId = blockOnLocoRepository.findAllByLocoList_Id(locoListId);
        return blockOnLocoByListLocoId.map(blockOnLocos -> blockOnLocos.stream()
                .map(this::convertToDTO)
                .toList()).orElseGet(List::of);
    }

    //Подготовка ДТО к созданию блока на локомотиве
    public BlockOnLocoDTO prepareToCreateBlockOnLocoByLocoNumberAndTypeLoco(String locoNumber, String typeLoco){
        Optional<LocoList> locoList = locoListRepository.findLocoListByLocoNumberAndTypeLoco(locoNumber, typeLoco);
        LocoList locoList1 = locoList.orElse(null);
        if (locoList1 == null) {
            throw new IllegalArgumentException("Локомотив " + typeLoco + " с  № " + locoNumber + " не найден");
        }
        Long locoListId = locoList1.getId();
        BlockOnLocoDTO blockOnLocoDTO = new BlockOnLocoDTO();
        blockOnLocoDTO.setLocoListId(locoListId);
        return  blockOnLocoDTO;
    }

    //Создание нового блока на локомотиве
    public BlockOnLocoDTO createBlockOnLoco(BlockOnLocoDTO blockOnLocoDTO) {
        Optional<LocoList> locoList = locoListRepository.findLocoListByLocoNumberAndTypeLoco(blockOnLocoDTO.getLocoNumber(), blockOnLocoDTO.getTypeLoco());
        if (locoList.isEmpty()) {
            throw new IllegalArgumentException("Локомотив " + blockOnLocoDTO.getTypeLoco() + " с № " + blockOnLocoDTO.getLocoNumber() + " не найден");
        }
        BlockOnLoco blockOnLoco = new BlockOnLoco(blockOnLocoDTO.getBlockName(), blockOnLocoDTO.getBlockNumber());
        blockOnLoco.setLocoList(locoList.get());
        blockOnLoco = blockOnLocoRepository.save(blockOnLoco);
        return convertToDTO(blockOnLoco);
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

    public void deleteBlockOnLocoByBlockNameAneBlockNumber(String blockName, String blockNumber) {
        Optional<BlockOnLoco> blockOnLoco = blockOnLocoRepository.findBlockOnLocoByBlockNameAndBlockNumber(blockName, blockNumber);
        BlockOnLoco blockOnLoco1 = blockOnLoco.orElse(null);
        if (blockOnLoco1 == null) {
            throw new IllegalArgumentException("Блок с  наименованием " + blockName + "и таким № " + blockNumber + " не найден");
        }
        Long blockOnLocoId = blockOnLoco1.getId();
        blockOnLocoRepository.deleteById(blockOnLocoId);
    }

    public BlockOnLocoDTO convertToDTO(BlockOnLoco blockOnLoco) {
        Long locoId = blockOnLoco.getLocoList().getId();
        LocoList locoList = locoListRepository.findById(locoId).orElse(null);
        if (locoList == null) {
            throw new IllegalArgumentException("Локомотив с  id " + locoId + " не найден");
        }
        String typeLoco = locoList.getTypeLoco();
        String locoNumber = locoList.getLocoNumber();
        return new BlockOnLocoDTO(blockOnLoco.getId(), blockOnLoco.getBlockName(), blockOnLoco.getBlockNumber(),
                blockOnLoco.getLocoList().getId(), typeLoco, locoNumber);
    }

    public BlockOnLoco convertToEntity(BlockOnLocoDTO blockOnLocoDTO) {
        BlockOnLoco blockOnLoco = new BlockOnLoco(blockOnLocoDTO.getBlockName(), blockOnLocoDTO.getBlockNumber());
        Optional<LocoList> locoList = locoListRepository.findById(blockOnLocoDTO.getLocoListId());
        locoList.ifPresent(blockOnLoco::setLocoList);
        return blockOnLoco;
    }
}
