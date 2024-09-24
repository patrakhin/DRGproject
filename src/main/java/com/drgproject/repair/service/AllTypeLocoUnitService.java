package com.drgproject.repair.service;

import com.drgproject.repair.dto.AllTypeLocoUnitDTO;
import com.drgproject.repair.entity.AllTypeLocoUnit;
import com.drgproject.repair.repository.AllTypeLocoUnitRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AllTypeLocoUnitService {
    private final AllTypeLocoUnitRepository allTypeLocoUnitRepository;

    public AllTypeLocoUnitService(AllTypeLocoUnitRepository aAllTypeLocoUnitRepository){
        this.allTypeLocoUnitRepository = aAllTypeLocoUnitRepository;
    }

    public List<AllTypeLocoUnitDTO> getAllTypeLocoUnit(){
        return allTypeLocoUnitRepository.findAll().stream()
                .map(this::convertToDTO)
                .toList();
    }

    public AllTypeLocoUnitDTO getAllTypeLocoUnitById(Long id){
        Optional<AllTypeLocoUnit> allTypeLocoUnit = allTypeLocoUnitRepository.findById(id);
        return allTypeLocoUnit.map(this::convertToDTO).orElse(null);
    }

    public AllTypeLocoUnitDTO createAllTypeLocoUnit(AllTypeLocoUnitDTO allTypeLocoUnitDTO){
        AllTypeLocoUnit allTypeLocoUnit = new AllTypeLocoUnit(allTypeLocoUnitDTO.getTypeLocoUnit());
        allTypeLocoUnit = allTypeLocoUnitRepository.save(allTypeLocoUnit);
        return convertToDTO(allTypeLocoUnit);
    }

    public AllTypeLocoUnitDTO updateAllTypeLocoUnit(Long id, AllTypeLocoUnitDTO allTypeLocoUnitDTO){
        Optional<AllTypeLocoUnit> allTypeLocoUnit = allTypeLocoUnitRepository.findById(id);
        if(allTypeLocoUnit.isPresent()){
            AllTypeLocoUnit allTypeLocoUnit1 = allTypeLocoUnit.get();
            allTypeLocoUnit1.setTypeLocoUnit(allTypeLocoUnitDTO.getTypeLocoUnit());
            allTypeLocoUnit1 = allTypeLocoUnitRepository.save(allTypeLocoUnit1);
            return convertToDTO(allTypeLocoUnit1);
        }
        return null;
    }

    public boolean deleteAllTypeLocoUnit(Long id){
        if (allTypeLocoUnitRepository.existsById(id)){
            allTypeLocoUnitRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private AllTypeLocoUnitDTO convertToDTO(AllTypeLocoUnit allTypeLocoUnit) {

        return new AllTypeLocoUnitDTO(allTypeLocoUnit.getId(), allTypeLocoUnit.getTypeLocoUnit());
    }
}
