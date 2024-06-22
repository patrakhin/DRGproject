package com.drgproject.repair.repository;

import com.drgproject.repair.entity.RepairHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RepairHistoryRepository extends JpaRepository<RepairHistory, Long> {
    Optional<RepairHistory> findRepairHistoriesByLocoNumber(String locoNumber);
}
