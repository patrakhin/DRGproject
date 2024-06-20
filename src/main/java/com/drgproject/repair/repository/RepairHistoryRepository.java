package com.drgproject.repair.repository;

import com.drgproject.repair.entity.RepairHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepairHistoryRepository extends JpaRepository<RepairHistory, Long> {
}
