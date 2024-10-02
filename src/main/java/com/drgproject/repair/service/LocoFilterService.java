package com.drgproject.repair.service;

import com.drgproject.repair.dto.LocoFilterDTO;
import com.drgproject.repair.entity.LocoFilter;
import com.drgproject.repair.repository.LocoFilterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LocoFilterService {

    private final LocoFilterRepository locoFilterRepository;

    @Autowired
    public LocoFilterService(LocoFilterRepository locoFilterRepository) {
        this.locoFilterRepository = locoFilterRepository;
    }

    // Создание или обновление записи
    public LocoFilterDTO saveLocoFilter(LocoFilterDTO locoFilterDTO) {
        LocoFilter locoFilter = new LocoFilter(
                locoFilterDTO.getHomeRegion(),
                locoFilterDTO.getHomeDepot(),
                locoFilterDTO.getLocoType(),
                locoFilterDTO.getSectionNumber(),
                locoFilterDTO.getFreeSection(),
                locoFilterDTO.getBusySection()
        );
        locoFilter.setId(locoFilterDTO.getId());

        LocoFilter savedLocoFilter = locoFilterRepository.save(locoFilter);
        return convertToDTO(savedLocoFilter);
    }

    // Получение всех записей
    public List<LocoFilterDTO> getAllLocoFilters() {
        return locoFilterRepository.findAll().stream()
                .map(this::convertToDTO)
                .toList();
    }

    // Получение записи по ID
    public LocoFilterDTO getLocoFilterById(Long id) {
        Optional<LocoFilter> locoFilterOptional = locoFilterRepository.findById(id);
        return locoFilterOptional.map(this::convertToDTO).orElse(null);
    }

    // Определение существование (из нее сформировывается ТПС) секции
    public boolean ifSectionIsExist(String homeRegion, String homeDepot, String locoType, String sectionNumber){
        Optional<LocoFilter> sectionTested = locoFilterRepository.findByHomeRegionAndHomeDepotAndLocoTypeAndSectionNumber(homeRegion, homeDepot, locoType, sectionNumber);
        return sectionTested.isPresent();
    }

    //Проверка на свободность секции
    public boolean ifSectionIsFree(String homeRegion, String homeDepot, String locoType, String sectionNumber){
        Optional<LocoFilter> sectionTested = locoFilterRepository.findByHomeRegionAndHomeDepotAndLocoTypeAndSectionNumber(homeRegion, homeDepot, locoType, sectionNumber);
        LocoFilter section;
        String dfg = homeDepot;
        if(sectionTested.isPresent()){
            section = sectionTested.get();
        }
        if(sectionTested.isPresent() && sectionTested.get().getFreeSection().contains("да")){
            section = sectionTested.get();
            String free = section.getFreeSection();
            return true;
        }
        return false;
    }

    // Устанавливаем секцию не свободной при формировании ТПС
    public void updateSectionNonFree(String oldHomeRegion, String oldHomeDepot, String oldLocoType, String oldSectionNumber) {
        // Находим существующую секцию по идентификатору
        Optional<LocoFilter> existingLoco = locoFilterRepository.findByHomeRegionAndHomeDepotAndLocoTypeAndSectionNumber(oldHomeRegion, oldHomeDepot, oldLocoType, oldSectionNumber);
        LocoFilter endLoco;
        if (existingLoco.isPresent()) {
            endLoco = existingLoco.get();
            // Обновляем поля секции новыми данными
            endLoco.setFreeSection("нет");
            endLoco.setBusySection("да");
        } else {
            throw new IllegalArgumentException("Секция номер: " + oldSectionNumber + " не найдена при установке занятости сервисный слой");
        }
        // Сохраняем обновленную секцию
        locoFilterRepository.save(endLoco);
    }

    // Создание свободной секции (при создании)
    public LocoFilterDTO createFreeSection (String homeRegion, String homeDepot, String locoType, String sectionNumber) {
        String freeSection = "да";
        String busySection = "нет";
        LocoFilter locoFilter = new LocoFilter(
                homeRegion,
                homeDepot,
                locoType,
                sectionNumber,
                freeSection,
                busySection
        );
        LocoFilter savedLocoFilter = locoFilterRepository.save(locoFilter);
        return convertToDTO(savedLocoFilter);
    }

    // Создание свободной секции (при расформировании ТПС)
    public void freeSectionAfterDistMist (String homeRegion, String homeDepot, String locoType, String sectionNumber) {
        String freeSection = "да";
        String busySection = "нет";
        // Находим существующую секцию по идентификатору
        Optional<LocoFilter> existingLocoFilter = locoFilterRepository.findByHomeRegionAndHomeDepotAndLocoTypeAndSectionNumber(homeRegion, homeDepot, locoType, sectionNumber);
        LocoFilter endLocoFilter;
        if (existingLocoFilter.isPresent()) {
            endLocoFilter = existingLocoFilter.get();
            // Обновляем поля секции новыми данными
            endLocoFilter.setFreeSection(freeSection);
            endLocoFilter.setBusySection(busySection);
        } else {
            throw new IllegalArgumentException("Секция номер: " + sectionNumber + " не найдена при расформировании ТПС сервисный слой");
        }
        // Сохраняем обновленную секцию
        locoFilterRepository.save(endLocoFilter);
    }

    //Удаление секции при удалении секции вообще из программы
    public void deleteSection (String homeRegion, String homeDepot, String locoType, String sectionNumber){
      Optional<LocoFilter> delSection = locoFilterRepository.findByHomeRegionAndHomeDepotAndLocoTypeAndSectionNumber(homeRegion, homeDepot, locoType, sectionNumber);
        if(delSection.isPresent()){
            locoFilterRepository.delete(delSection.get());
        } else {
        throw new IllegalArgumentException("Секция номер: " + sectionNumber + " не найдена при удалении сервисный слой");
        }
    }

    // Обновление свободной секции (при редактировании секции при создании)
    public LocoFilterDTO updateSection(String oldHomeRegion, String oldHomeDepot, String oldLocoType, String oldSectionNumber,
            String homeRegion, String homeDepot, String locoType, String sectionNumber) {
        // Находим существующую секцию по идентификатору
        Optional<LocoFilter> existingLocoFilter = locoFilterRepository.findByHomeRegionAndHomeDepotAndLocoTypeAndSectionNumber(oldHomeRegion, oldHomeDepot, oldLocoType, oldSectionNumber);
        LocoFilter endLocoFilter;
        if (existingLocoFilter.isPresent()) {
           endLocoFilter = existingLocoFilter.get();
            // Обновляем поля секции новыми данными
            endLocoFilter.setHomeRegion(homeRegion);
            endLocoFilter.setHomeDepot(homeDepot);
            endLocoFilter.setLocoType(locoType);
            endLocoFilter.setSectionNumber(sectionNumber);
        } else {
            throw new IllegalArgumentException("Секция номер: " + sectionNumber + " не найдена при обновлении сервисный слой");
        }
        // Сохраняем обновленную секцию
        LocoFilter updatedLocoFilter = locoFilterRepository.save(endLocoFilter);
        // Конвертируем и возвращаем DTO
        return convertToDTO(updatedLocoFilter);
    }

    // Получение списка наименований блоков по типу системы на секции


    // Получение списка свободных секций
    public List<LocoFilterDTO> getAllFreeSect(String homeRegion, String homeDepot, String locoType, String freeSection) {
        return locoFilterRepository.findByHomeRegionAndHomeDepotAndLocoTypeAndFreeSection(homeRegion, homeDepot, locoType, freeSection).stream()
                .map(this::convertToDTO)
                .toList();
    }

    // Удаление записи по ID
    public void deleteLocoFilterById(Long id) {
        locoFilterRepository.deleteById(id);
    }

    // Удаление записи при удалении секции
    public void deleteLocoFilterByHomeRegion(String homeRegion, String homeDepot, String locoType, String sectionNumber) {
        Optional<LocoFilter> middleLocoFilter = locoFilterRepository.findByHomeRegionAndHomeDepotAndLocoTypeAndSectionNumber(homeRegion, homeDepot, locoType, sectionNumber);
        if (middleLocoFilter.isPresent()){
            Long id = middleLocoFilter.get().getId();
            deleteLocoFilterById(id);
        }else {
            throw new IllegalArgumentException("Секция номер: " + sectionNumber + " не найдена при удалении сервисный слой");
        }
    }

    // Преобразование сущности в DTO
    private LocoFilterDTO convertToDTO(LocoFilter locoFilter) {
        return new LocoFilterDTO(
                locoFilter.getId(),
                locoFilter.getHomeRegion(),
                locoFilter.getHomeDepot(),
                locoFilter.getLocoType(),
                locoFilter.getSectionNumber(),
                locoFilter.getFreeSection(),
                locoFilter.getBusySection()
        );
    }
}
