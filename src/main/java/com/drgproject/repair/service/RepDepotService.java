package com.drgproject.repair.service;

import com.drgproject.repair.dto.RepDepotDTO;
import com.drgproject.repair.entity.RepDepot;
import com.drgproject.repair.entity.Region;
import com.drgproject.repair.repository.RepDepotRepository;
import com.drgproject.repair.repository.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RepDepotService {

    private final RepDepotRepository repDepotRepository;
    private final RegionRepository regionRepository;

    @Autowired
    public RepDepotService(RepDepotRepository repDepotRepository, RegionRepository regionRepository) {
        this.repDepotRepository = repDepotRepository;
        this.regionRepository = regionRepository;
    }

    // Получить все депо ремонта для конкретного региона по его названию (DTO)
    public List<RepDepotDTO> getDepotsByRegionName(String regionName) {
        Region region = regionRepository.findRegionByName(regionName)
                .orElseThrow(() -> new IllegalArgumentException("Регион с именем " + regionName + " не найден"));
        List<RepDepot> depots = repDepotRepository.findByRegion(region);

        return depots.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Получаем депо по ID
    public RepDepotDTO getRepairDepotById(Long id) {
        return repDepotRepository.findById(id)
                .map(this::convertToDTO) // Преобразуем сущность в DTO
                .orElse(null); // Возвращаем null, если объект не найден
    }

    // Добавить новое депо ремонта (DTO)
    public RepDepotDTO addRepairDepot(String depotName, String regionName) {
        Region region = regionRepository.findRegionByName(regionName)
                .orElseThrow(() -> new IllegalArgumentException("Регион с именем " + regionName + " не найден"));

        // Проверка на существование одноименного депо в данном регионе
        Optional<RepDepot> existingDepot = repDepotRepository.findByNameAndRegion(depotName, region);
        if (existingDepot.isPresent()) {
            throw new IllegalArgumentException("Депо ремонта с именем " + depotName + " уже существует в регионе " + regionName);
        }

        RepDepot newDepot = new RepDepot(depotName, region);
        RepDepot savedDepot = repDepotRepository.save(newDepot);
        return convertToDTO(savedDepot);
    }

    // Обновить депо ремонта (DTO)
    public RepDepotDTO updateRepairDepot(Long depotId, String newName, String regionName) {
        RepDepot existingDepot = repDepotRepository.findById(depotId)
                .orElseThrow(() -> new IllegalArgumentException("Депо ремонта с ID " + depotId + " не найдено"));

        Region region = regionRepository.findRegionByName(regionName)
                .orElseThrow(() -> new IllegalArgumentException("Регион с именем " + regionName + " не найден"));

        // Проверка на уникальность нового имени в данном регионе
        Optional<RepDepot> sameNameDepot = repDepotRepository.findByNameAndRegion(newName, region);
        if (sameNameDepot.isPresent() && !sameNameDepot.get().getId().equals(depotId)) {
            throw new IllegalArgumentException("Депо ремонта с именем " + newName + " уже существует в регионе " + regionName);
        }

        existingDepot.setName(newName);
        existingDepot.setRegion(region);
        RepDepot updatedDepot = repDepotRepository.save(existingDepot);
        return convertToDTO(updatedDepot);
    }

    // Удалить депо ремонта
    public void deleteRepairDepot(Long depotId) {
        if (!repDepotRepository.existsById(depotId)) {
            throw new IllegalArgumentException("Депо ремонта с ID " + depotId + " не найдено");
        }
        repDepotRepository.deleteById(depotId);
    }

    public void deleteDepotByNameAndRegion(String name, String region1){
        Region region = regionRepository.findRegionByName(region1).orElseThrow();
        RepDepot depot = repDepotRepository.findByNameAndRegion(name, region).orElse(null);
        if (depot != null) {
            repDepotRepository.delete(depot);
        } else {
            throw new IllegalArgumentException("Депо с именем " + name + " в регионе " + region1 + " не найдено.");
        }
    }

    public boolean existsByDepotAndRegion(String depot, String regionName) {
        Region region = regionRepository.findRegionByName(regionName)
                .orElseThrow(() -> new IllegalArgumentException("Регион с именем " + regionName + " не найден"));
        return repDepotRepository.findByNameAndRegion(depot, region).isPresent();
    }

    public RepDepotDTO addRepairDepotFromFile(String depotName, String regionName) {
        Region region = regionRepository.findRegionByName(regionName)
                .orElseThrow(() -> new IllegalArgumentException("Регион с именем " + regionName + " не найден"));

        // Проверка на существование депо в регионе
        Optional<RepDepot> existingDepot = repDepotRepository.findByNameAndRegion(depotName, region);
        if (existingDepot.isPresent()) {
            throw new IllegalArgumentException("Депо ремонта с именем " + depotName + " уже существует в регионе " + regionName);
        }

        RepDepot newDepot = new RepDepot(depotName, region);
        RepDepot savedDepot = repDepotRepository.save(newDepot);
        return convertToDTO(savedDepot);
    }

    // Конвертация из DTO в сущность
    private RepDepot convertToEntity(RepDepotDTO dto) {
        Region region = regionRepository.findRegionByName(dto.getRegionName())
                .orElseThrow(() -> new IllegalArgumentException("Регион с именем " + dto.getRegionName() + " не найден"));
        RepDepot depot = new RepDepot();
        depot.setId(dto.getId());
        depot.setName(dto.getName());
        depot.setRegion(region);
        return depot;
    }

    // Конвертация из сущности в DTO
    private RepDepotDTO convertToDTO(RepDepot depot) {
        return new RepDepotDTO(depot.getId(), depot.getName(), depot.getRegion().getName());
    }
}
