package com.drgproject.repair.service;

import com.drgproject.repair.RepairHistoryMapper;
import com.drgproject.repair.dto.LocoListDTO;
import com.drgproject.repair.repository.LocoListRepository;
import com.drgproject.repair.repository.RepairHistoryRepository;
import com.drgproject.repair.dto.RepairHistoryDto;
import com.drgproject.repair.entity.RepairHistory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class RepairHistoryService {


    private final RepairHistoryRepository repairHistoryRepository;
    private final LocoListRepository locoListRepository;
    private final LocoListService locoListService;

    public RepairHistoryService(RepairHistoryRepository repairHistoryRepository,
                                LocoListRepository locoListRepository, LocoListService locoListService) {
        this.repairHistoryRepository = repairHistoryRepository;
        this.locoListRepository = locoListRepository;
        this.locoListService = locoListService;
    }

    public List<RepairHistoryDto> findAll() {
        List<RepairHistory> repairHistories = repairHistoryRepository.findAll();
        return RepairHistoryMapper.toDtoList(repairHistories);
    }

    public Optional<RepairHistoryDto> findById(Long id) {
        Optional<RepairHistory> repairHistory = repairHistoryRepository.findById(id);
        return repairHistory.map(RepairHistoryMapper::toDto);
    }


    public List<RepairHistoryDto> findByTypeLocoAndLocoNumber(String typeLoco, String numberLoco) {
       List<RepairHistory> repairHistory = repairHistoryRepository.
               findRepairHistoriesByTypeLocoAndLocoNumber(typeLoco, numberLoco).orElseThrow(() -> new IllegalArgumentException("Исторя для этого локомотива не найдена"));
       return RepairHistoryMapper.toDtoList(repairHistory);
    }

    public Optional<RepairHistoryDto> findByTypeLocoAndLocoNumberAndDate(String typeLoco, String locoNumber, LocalDate repairDate){
        Optional<RepairHistory> repairHistory = repairHistoryRepository.
                findRepairHistoriesByTypeLocoAndLocoNumberAndRepairDate(typeLoco, locoNumber, repairDate);
        if (repairHistory.isEmpty()){
            throw new IllegalArgumentException("История для этого локомотива на эту дату не найдена сервисный слой");
        }
        return repairHistory.map(RepairHistoryMapper::toDto);
    }

    public RepairHistoryDto save(RepairHistoryDto repairHistoryDTO) {
        RepairHistory repairHistory = RepairHistoryMapper.toEntity(repairHistoryDTO);
        RepairHistory savedRepairHistory = repairHistoryRepository.save(repairHistory);
        return RepairHistoryMapper.toDto(savedRepairHistory);
    }

    @Transactional
    public void deleteById(Long id) {
        repairHistoryRepository.deleteById(id);
    }

    @Transactional
    public void deleteByTypeAndNumberAndDate(String typeLoco, String locoNumber, LocalDate repairDate){
        repairHistoryRepository.deleteRepairHistoryByTypeLocoAndLocoNumberAndRepairDate(typeLoco, locoNumber, repairDate);
    }

    public boolean isLastEntry(String typeLoco, String numberLoco) {
        // Выполняем запрос к базе данных, чтобы проверить количество записей для данного локомотива
        int count = repairHistoryRepository.countByTypeLocoAndLocoNumber(typeLoco, numberLoco);
        return count == 0;
    }

    //Перевод полного имени в формат "Фамилия А.Р."
    public String convertToShortName(String fullName) {
        if (fullName == null || fullName.isEmpty()) {
            return fullName; // Возвращаем исходное значение, если оно пустое
        }

        // Разбиваем строку на части
        String[] nameParts = fullName.split(" ");

        // Проверяем, содержит ли строка достаточное количество частей
        if (nameParts.length < 3) {
            throw new IllegalArgumentException("ФИО должно содержать фамилию, имя и отчество.");
        }

        // Извлекаем фамилию и первые буквы имени и отчества
        String lastName = nameParts[0];
        String firstNameInitial = nameParts[1].substring(0, 1);
        String middleNameInitial = nameParts[2].substring(0, 1);

        // Формируем сокращенное имя
        return String.format("%s %s.%s.", lastName, firstNameInitial, middleNameInitial);
    }
}
