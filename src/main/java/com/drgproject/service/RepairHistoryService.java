package com.drgproject.service;

import com.drgproject.dto.RepairHistoryDto;
import com.drgproject.entity.RepairHistory;
import com.drgproject.repository.RepairHistoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RepairHistoryService {

    private final RepairHistoryRepository repairHistoryRepository;

    public RepairHistoryService(RepairHistoryRepository repairHistoryRepository) {
        this.repairHistoryRepository = repairHistoryRepository;
    }

    @Transactional
    public RepairHistoryDto createRepairHistory(RepairHistoryDto repairHistoryDto) {
        RepairHistory repairHistory = convertToEntity(repairHistoryDto);
        RepairHistory savedRepairHistory = repairHistoryRepository.save(repairHistory);
        return convertToDto(savedRepairHistory);
    }

    @Transactional(readOnly = true)
    public RepairHistoryDto getRepairHistoryById(Long id) {
        Optional<RepairHistory> repairHistory = repairHistoryRepository.findById(id);
        return repairHistory.map(this::convertToDto).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<RepairHistoryDto> getAllRepairHistories() {
        return repairHistoryRepository.findAll().stream()
                .map(this::convertToDto)
                .toList();
    }

    @Transactional
    public RepairHistoryDto updateRepairHistory(Long id, RepairHistoryDto repairHistoryDto) {
        Optional<RepairHistory> optionalRepairHistory = repairHistoryRepository.findById(id);
        if (optionalRepairHistory.isPresent()) {
            RepairHistory repairHistory = optionalRepairHistory.get();
            repairHistory.setRepairType(repairHistoryDto.getRepairType());
            repairHistory.setEmployeeNumber(repairHistoryDto.getEmployeeNumber());
            repairHistory.setDateCreate(repairHistoryDto.getDateCreate());
            repairHistory.setTransactionLogs(repairHistoryDto.getTransactionLogs());
            repairHistory.setLocomotive(repairHistoryDto.getLocomotive());
            RepairHistory updatedRepairHistory = repairHistoryRepository.save(repairHistory);
            return convertToDto(updatedRepairHistory);
        }
        return null;
    }

    @Transactional
    public void deleteRepairHistory(Long id) {
        repairHistoryRepository.deleteById(id);
    }

    private RepairHistory convertToEntity(RepairHistoryDto repairHistoryDto) {
        RepairHistory repairHistory = new RepairHistory();
        repairHistory.setId(repairHistoryDto.getId());
        repairHistory.setRepairType(repairHistoryDto.getRepairType());
        repairHistory.setEmployeeNumber(repairHistoryDto.getEmployeeNumber());
        repairHistory.setDateCreate(repairHistoryDto.getDateCreate());
        repairHistory.setTransactionLogs(repairHistoryDto.getTransactionLogs());
        repairHistory.setLocomotive(repairHistoryDto.getLocomotive());
        return repairHistory;
    }

    private RepairHistoryDto convertToDto(RepairHistory repairHistory) {
        RepairHistoryDto dto = new RepairHistoryDto();
        dto.setId(repairHistory.getId());
        dto.setRepairType(repairHistory.getRepairType());
        dto.setEmployeeNumber(repairHistory.getEmployeeNumber());
        dto.setDateCreate(repairHistory.getDateCreate());
        dto.setTransactionLogs(repairHistory.getTransactionLogs());
        dto.setLocomotive(repairHistory.getLocomotive());
        return dto;
    }
}
