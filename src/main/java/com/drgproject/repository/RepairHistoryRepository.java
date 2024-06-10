package com.drgproject.repository;

import com.drgproject.entity.RepairHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepairHistoryRepository extends JpaRepository<RepairHistory, Long> {
}
