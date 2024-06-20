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
        dto.setSystemName(repairHistory.getSystemName());
        dto.setBlockNumber(repairHistory.getBlockNumber());
        dto.setEmployee(repairHistory.getEmployee());
        dto.setInspectionResult(repairHistory.getInspectionResult());
        dto.setWorkResult(repairHistory.getWorkResult());
        dto.setControlBlockSeal(repairHistory.getControlBlockSeal());
        dto.setPowerBlockSeal(repairHistory.getPowerBlockSeal());
        dto.setComBlockSeal(repairHistory.getComBlockSeal());
        dto.setRemoteContrSeal(repairHistory.getRemoteContrSeal());
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
        repairHistory.setSystemName(dto.getSystemName());
        repairHistory.setBlockNumber(dto.getBlockNumber());
        repairHistory.setEmployee(dto.getEmployee());
        repairHistory.setInspectionResult(dto.getInspectionResult());
        repairHistory.setWorkResult(dto.getWorkResult());
        repairHistory.setControlBlockSeal(dto.getControlBlockSeal());
        repairHistory.setPowerBlockSeal(dto.getPowerBlockSeal());
        repairHistory.setComBlockSeal(dto.getComBlockSeal());
        repairHistory.setRemoteContrSeal(dto.getRemoteContrSeal());
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

