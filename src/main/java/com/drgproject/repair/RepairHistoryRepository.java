package com.drgproject.repair;

import com.drgproject.repair.entiny.RepairHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepairHistoryRepository extends JpaRepository<RepairHistory, Long> {
}
