package com.drgproject.repository;

import com.drgproject.entity.TransactionLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionLogRepository extends JpaRepository<TransactionLog,Long> {
}
