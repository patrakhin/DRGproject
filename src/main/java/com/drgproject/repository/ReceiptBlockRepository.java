package com.drgproject.repository;

import com.drgproject.entity.ReceiptBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ReceiptBlockRepository extends JpaRepository<ReceiptBlock, Long> {
    Optional<ReceiptBlock> findReceiptBlockByLocoBlockUniqueId(Long uniqueId);
}