package com.drgproject.repository;

import com.drgproject.entity.LocoBlock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocoBlockRepository extends JpaRepository<LocoBlock, Long> {
  Long countByStorageId(Long storageId);
}