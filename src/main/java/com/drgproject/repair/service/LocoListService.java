package com.drgproject.repair.service;

import com.drgproject.repair.dto.LocoListDTO;
import com.drgproject.repair.entity.BlockOnLoco;
import com.drgproject.repair.entity.LocoInfo;
import com.drgproject.repair.entity.LocoList;
import com.drgproject.repair.repository.BlockOnLocoRepository;
import com.drgproject.repair.repository.LocoListRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


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
        List<LocoList> locoLists = locoListRepository.findAll();
        Optional<LocoList> locoListOptional = locoLists.stream()
                .filter(loco -> loco.getLocoNumber().equals(numberLoco))
                .findFirst();
        return locoListOptional.map(this::convertToDTO).orElse(null);
    }

    public LocoListDTO getLocoListByNumberLocoAndTypeLoco(String numberLoco, String typeLoco){
        Optional<LocoList> locoListByType = locoListRepository.findLocoListByLocoNumberAndTypeLoco(numberLoco, typeLoco);
        return locoListByType.map(this::convertToDTO).orElse(null);
    }

    //Проверка на дублирование перед созданием секции
    public boolean ifLociListIsExists(String homeRegion, String homeDepot, String locoNumber){
        Optional<LocoList> locoListIsExist = locoListRepository.findLocoListByHomeRegionAndHomeDepotAndLocoNumber(homeRegion, homeDepot, locoNumber);
        return locoListIsExist.isPresent();
    }

    // Приверка ну сущестование секции (для создания блока на секцию)
    public boolean ifSectionExist (String typeLoco, String sectionNumber){
        Optional<LocoList> sectionIsExist = locoListRepository.findLocoListByTypeLocoAndLocoNumber(typeLoco, sectionNumber);
        return sectionIsExist.isPresent();
    }

    public LocoListDTO createLocoList(LocoListDTO locoListDTO) {
        LocoList locoList = new LocoList();
        locoList.setContractNumber(locoListDTO.getContractNumber());
        locoList.setTypeLoco(locoListDTO.getTypeLoco());
        locoList.setTypeSystem(locoListDTO.getTypeSystem());
        locoList.setLocoNumber(locoListDTO.getLocoNumber());
        locoList.setHomeRegion(locoListDTO.getHomeRegion());
        locoList.setHomeDepot(locoListDTO.getHomeDepot());
        locoList.setComment(locoListDTO.getComment());

        // Пустой список блоков при создании локомотива
        locoList.setBlockOnLocos(new ArrayList<>());

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
            locoList.setHomeRegion(locoListDTO.getHomeRegion());
            locoList.setHomeDepot(locoListDTO.getHomeDepot());
            locoList.setComment(locoListDTO.getComment());
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

    //поиск номера локомотива по первым двум цифрам
    public List<String> findNumbersByPrefix(String prefix) {
        return locoListRepository.findByLocoNumberStartingWith(prefix)
                .stream()
                .map(LocoList::getLocoNumber)
                .toList();
    }

    private LocoListDTO convertToDTO(LocoList locoList) {
        List<Long> blockOnLocosIds = locoList.getBlockOnLocos().stream()
                .map(BlockOnLoco::getId)
                .toList();

        return new LocoListDTO(locoList.getId(), locoList.getContractNumber(), locoList.getTypeLoco(),
                locoList.getTypeSystem(), locoList.getLocoNumber(), locoList.getHomeRegion(),
                locoList.getHomeDepot(), locoList.getComment(), blockOnLocosIds);
    }
}
