package com.drgproject.repair.service;

import com.drgproject.repair.dto.LocoListDTO;
import com.drgproject.repair.entity.BlockOnLoco;
import com.drgproject.repair.entity.LocoList;
import com.drgproject.repair.repository.BlockOnLocoRepository;
import com.drgproject.repair.repository.LocoListRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LocoListService {

    private final LocoListRepository locoListRepository;
    private final BlockOnLocoRepository blockOnLocoRepository;

    public LocoListService(LocoListRepository locoListRepository, BlockOnLocoRepository blockOnLocoRepository) {
        this.locoListRepository = locoListRepository;
        this.blockOnLocoRepository = blockOnLocoRepository;
    }

    public List<LocoListDTO> getAllLocoLists() {
        return locoListRepository.findAll().stream().map(this::convertToDTO).toList();
    }

    public LocoListDTO getLocoListById(Long id) {
        Optional<LocoList> locoList = locoListRepository.findById(id);
        return locoList.map(this::convertToDTO).orElse(null);
    }

    public LocoListDTO getLocoListByNumberLoco(String numberLoco) {
        Optional<LocoList> locoList = locoListRepository.findLocoListByLocoNumber(numberLoco);
        return locoList.map(this::convertToDTO).orElse(null);
    }

    public LocoListDTO createLocoList(LocoListDTO locoListDTO) {
        LocoList locoList = new LocoList();
        locoList.setContractNumber(locoListDTO.getContractNumber());
        locoList.setTypeLoco(locoListDTO.getTypeLoco());
        locoList.setTypeSystem(locoListDTO.getTypeSystem());
        locoList.setLocoNumber(locoListDTO.getLocoNumber());
        locoList.setComment(locoListDTO.getComment());

        List<BlockOnLoco> blockOnLocos = locoListDTO.getBlockOnLocos().stream()
                .map(blockId -> blockOnLocoRepository.findById(blockId)
                        .orElseThrow(() -> new RuntimeException("BlockOnLoco not found: " + blockId)))
                .collect(Collectors.toList());

        locoList.setBlockOnLocos(blockOnLocos);

        LocoList savedLocoList = locoListRepository.save(locoList);
        return convertToDTO(savedLocoList);
    }

    public LocoListDTO updateLocoList(Long id, LocoListDTO locoListDTO) {
        Optional<LocoList> optionalLocoList = locoListRepository.findById(id);
        if (optionalLocoList.isPresent()) {
            LocoList locoList = optionalLocoList.get();
            locoList.setContractNumber(locoListDTO.getContractNumber());
            locoList.setTypeLoco(locoListDTO.getTypeLoco());
            locoList.setTypeSystem(locoListDTO.getTypeSystem());
            locoList.setLocoNumber(locoListDTO.getLocoNumber());
            locoList.setComment(locoListDTO.getComment());

            List<BlockOnLoco> blockOnLocos = locoListDTO.getBlockOnLocos().stream()
                    .map(blockId -> blockOnLocoRepository.findById(blockId)
                            .orElseThrow(() -> new RuntimeException("BlockOnLoco not found: " + blockId)))
                    .collect(Collectors.toList());

            locoList.setBlockOnLocos(blockOnLocos);

            LocoList updatedLocoList = locoListRepository.save(locoList);
            return convertToDTO(updatedLocoList);
        }
        return null;
    }

    public boolean deleteLocoList(Long id) {
        if (locoListRepository.existsById(id)) {
            locoListRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private LocoListDTO convertToDTO(LocoList locoList) {
        List<Long> blockOnLocosIds = locoList.getBlockOnLocos().stream()
                .map(BlockOnLoco::getId)
                .toList();

        return new LocoListDTO(locoList.getId(), locoList.getContractNumber(), locoList.getTypeLoco(),
                locoList.getTypeSystem(), locoList.getLocoNumber(), locoList.getComment(), blockOnLocosIds);
    }
}
