package com.drgproject.repository;

import com.drgproject.entity.LocoBlock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocoBlockRepository extends JpaRepository<LocoBlock, Long> {

    Optional<LocoBlock> findByBlockNumber(String blockNumber);
}