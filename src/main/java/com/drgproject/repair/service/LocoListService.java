package com.drgproject.repair.service;

import com.drgproject.repair.dto.LocoListDTO;
import com.drgproject.repair.entity.LocoList;
import com.drgproject.repair.repository.LocoListRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LocoListService {

    private final LocoListRepository locoListRepository;

    public LocoListService(LocoListRepository locoListRepository) {
        this.locoListRepository = locoListRepository;
    }

    public List<LocoListDTO> getAllLocoLists() {
        return locoListRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public LocoListDTO getLocoListById(Long id) {
        Optional<LocoList> locoList = locoListRepository.findById(id);
        return locoList.map(this::convertToDTO).orElse(null);
    }

    public LocoListDTO createLocoList(LocoListDTO locoListDTO) {
        LocoList locoList = convertToEntity(locoListDTO);
        locoList = locoListRepository.save(locoList);
        return convertToDTO(locoList);
    }

    public LocoListDTO updateLocoList(Long id, LocoListDTO locoListDTO) {
        Optional<LocoList> locoListOptional = locoListRepository.findById(id);
        if (locoListOptional.isPresent()) {
            LocoList locoList = locoListOptional.get();
            locoList.setContractNumber(locoListDTO.getContractNumber());
            locoList.setTypeLoco(locoListDTO.getTypeLoco());
            locoList.setTypeSystem(locoListDTO.getTypeSystem());
            locoList.setLocoNumber(locoListDTO.getLocoNumber());
            locoList.setComment(locoListDTO.getComment());
            locoListRepository.save(locoList);
            return convertToDTO(locoList);
        }
        return null;
    }

    public void deleteLocoList(Long id) {
        locoListRepository.deleteById(id);
    }

    private LocoListDTO convertToDTO(LocoList locoList) {
        return new LocoListDTO(locoList.getId(), locoList.getContractNumber(), locoList.getTypeLoco(), locoList.getTypeSystem(), locoList.getLocoNumber(), locoList.getComment());
    }

    private LocoList convertToEntity(LocoListDTO locoListDTO) {
        return new LocoList(locoListDTO.getContractNumber(), locoListDTO.getTypeLoco(), locoListDTO.getTypeSystem(), locoListDTO.getLocoNumber(), locoListDTO.getComment());
    }
}
