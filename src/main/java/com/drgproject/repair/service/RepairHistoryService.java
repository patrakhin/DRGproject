package com.drgproject.repair.service;

import com.drgproject.repair.RepairHistoryMapper;
import com.drgproject.repair.repository.RepairHistoryRepository;
import com.drgproject.repair.dto.RepairHistoryDto;
import com.drgproject.repair.entity.RepairHistory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RepairHistoryService {


    private final RepairHistoryRepository repairHistoryRepository;

    public RepairHistoryService(RepairHistoryRepository repairHistoryRepository) {
        this.repairHistoryRepository = repairHistoryRepository;
    }

    public List<RepairHistoryDto> findAll() {
        List<RepairHistory> repairHistories = repairHistoryRepository.findAll();
        return RepairHistoryMapper.toDtoList(repairHistories);
    }

    public Optional<RepairHistoryDto> findById(Long id) {
        Optional<RepairHistory> repairHistory = repairHistoryRepository.findById(id);
        return repairHistory.map(RepairHistoryMapper::toDto);
    }

    public Optional<RepairHistoryDto> findByNumberLoco(String numberLoco) {
        Optional<RepairHistory> repairHistory = repairHistoryRepository.findRepairHistoriesByLocoNumber(numberLoco);
        return repairHistory.map(RepairHistoryMapper::toDto);
    }

    public RepairHistoryDto save(RepairHistoryDto repairHistoryDTO) {
        RepairHistory repairHistory = RepairHistoryMapper.toEntity(repairHistoryDTO);
        RepairHistory savedRepairHistory = repairHistoryRepository.save(repairHistory);
        return RepairHistoryMapper.toDto(savedRepairHistory);
    }

    public void deleteById(Long id) {
        repairHistoryRepository.deleteById(id);
    }
}

