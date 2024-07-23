package com.drgproject.repair.service;

import com.drgproject.repair.dto.PositionRepairDTO;
import com.drgproject.repair.entity.PositionRepair;
import com.drgproject.repair.repository.PositionRepairRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PositionRepairService {


    private final PositionRepairRepository positionRepairRepository;

    public PositionRepairService(PositionRepairRepository positionRepairRepository) {
        this.positionRepairRepository = positionRepairRepository;
    }

    public List<PositionRepairDTO> getAllPositionRepairs() {
        return positionRepairRepository.findAll().stream().map(this::convertToDTO).toList();
    }

    public PositionRepairDTO getPositionRepairByName(String positionName) {
        Optional<PositionRepair> positionRepair = positionRepairRepository.findByPosRepair(positionName);
        return positionRepair.map(this::convertToDTO).orElse(null);
    }
    public PositionRepairDTO getPositionRepairById(Long id) {
        Optional<PositionRepair> positionRepair = positionRepairRepository.findById(id);
        return positionRepair.map(this::convertToDTO).orElse(null);
    }

    public PositionRepairDTO createPositionRepair(PositionRepairDTO positionRepairDTO) {
        PositionRepair positionRepair = new PositionRepair(positionRepairDTO.getPosRepair());
        positionRepair = positionRepairRepository.save(positionRepair);
        return convertToDTO(positionRepair);
    }

    public PositionRepairDTO updatePositionRepair(Long id, PositionRepairDTO positionRepairDTO) {
        Optional<PositionRepair> optionalPositionRepair = positionRepairRepository.findById(id);
        if (optionalPositionRepair.isPresent()) {
            PositionRepair positionRepair = optionalPositionRepair.get();
            positionRepair.setPosRepair(positionRepairDTO.getPosRepair());
            positionRepair = positionRepairRepository.save(positionRepair);
            return convertToDTO(positionRepair);
        }
        return null;
    }

    public boolean deletePositionRepair(Long id) {
        if (positionRepairRepository.existsById(id)) {
            positionRepairRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private PositionRepairDTO convertToDTO(PositionRepair positionRepair) {
        return new PositionRepairDTO(positionRepair.getId(), positionRepair.getPosRepair());
    }
}
