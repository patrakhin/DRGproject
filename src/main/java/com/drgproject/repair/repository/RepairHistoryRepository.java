package com.drgproject.repair.repository;

import com.drgproject.repair.entity.RepairHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RepairHistoryRepository extends JpaRepository<RepairHistory, Long> {

    Optional<List<RepairHistory>> findRepairHistoriesByTypeLocoAndLocoNumber(String typeLoco, String locoNumber);
    Optional<RepairHistory> findRepairHistoriesByTypeLocoAndLocoNumberAndRepairDate(String typeLoco, String locoNumber, LocalDate repairDate);
    void deleteRepairHistoryByTypeLocoAndLocoNumberAndRepairDate(String typeLoco, String locoNumber, LocalDate repairDate);
}
