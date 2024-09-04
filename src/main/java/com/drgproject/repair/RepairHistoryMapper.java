package com.drgproject.repair;

import com.drgproject.repair.dto.RepairHistoryDto;
import com.drgproject.repair.entity.RepairHistory;

import java.util.List;

public class RepairHistoryMapper {

    public static RepairHistoryDto toDto(RepairHistory repairHistory) {
        RepairHistoryDto dto = new RepairHistoryDto();
        dto.setId(repairHistory.getId());
        dto.setRepairDate(repairHistory.getRepairDate());
        dto.setHomeDepot(repairHistory.getHomeDepot());
        dto.setTypeLoco(repairHistory.getTypeLoco());
        dto.setLocoNumber(repairHistory.getLocoNumber());
        dto.setPositionRepair(repairHistory.getPositionRepair());
        dto.setTypeSystem(repairHistory.getTypeSystem());
        dto.setEmployee(repairHistory.getEmployee());
        dto.setInspectionResult(repairHistory.getInspectionResult());
        dto.setWorkResult(repairHistory.getWorkResult());
        dto.setBlock1Seal(repairHistory.getBlock1Seal());
        dto.setBlock2Seal(repairHistory.getBlock2Seal());
        dto.setBlock3Seal(repairHistory.getBlock3Seal());
        dto.setBlock4Seal(repairHistory.getBlock4Seal());
        dto.setBlock5Seal(repairHistory.getBlock5Seal());
        dto.setBlock6Seal(repairHistory.getBlock6Seal());
        dto.setBlock7Seal(repairHistory.getBlock7Seal());
        dto.setBlock8Seal(repairHistory.getBlock8Seal());
        dto.setBlock9Seal(repairHistory.getBlock9Seal());
        dto.setBlock10Seal(repairHistory.getBlock10Seal());
        dto.setRepairDepot(repairHistory.getRepairDepot());
        return dto;
    }

    public static RepairHistory toEntity(RepairHistoryDto dto) {
        RepairHistory repairHistory = new RepairHistory();
        repairHistory.setId(dto.getId());
        repairHistory.setRepairDate(dto.getRepairDate());
        repairHistory.setHomeDepot(dto.getHomeDepot());
        repairHistory.setTypeLoco(dto.getTypeLoco());
        repairHistory.setLocoNumber(dto.getLocoNumber());
        repairHistory.setPositionRepair(dto.getPositionRepair());
        repairHistory.setTypeSystem(dto.getTypeSystem());
        repairHistory.setEmployee(dto.getEmployee());
        repairHistory.setInspectionResult(dto.getInspectionResult());
        repairHistory.setWorkResult(dto.getWorkResult());
        repairHistory.setBlock1Seal(dto.getBlock1Seal());
        repairHistory.setBlock2Seal(dto.getBlock2Seal());
        repairHistory.setBlock3Seal(dto.getBlock3Seal());
        repairHistory.setBlock4Seal(dto.getBlock4Seal());
        repairHistory.setBlock5Seal(dto.getBlock5Seal());
        repairHistory.setBlock6Seal(dto.getBlock6Seal());
        repairHistory.setBlock7Seal(dto.getBlock7Seal());
        repairHistory.setBlock8Seal(dto.getBlock8Seal());
        repairHistory.setBlock9Seal(dto.getBlock9Seal());
        repairHistory.setBlock10Seal(dto.getBlock10Seal());
        repairHistory.setRepairDepot(dto.getRepairDepot());
        return repairHistory;
    }

    public static List<RepairHistoryDto> toDtoList(List<RepairHistory> repairHistories) {
        return repairHistories.stream()
                .map(RepairHistoryMapper::toDto)
                .toList();
    }

    public static List<RepairHistory> toEntityList(List<RepairHistoryDto> dtoList) {
        return dtoList.stream()
                .map(RepairHistoryMapper::toEntity)
                .toList();
    }
}