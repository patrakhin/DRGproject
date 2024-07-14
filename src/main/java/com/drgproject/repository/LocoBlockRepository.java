package com.drgproject.repository;

import com.drgproject.entity.LocoBlock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocoBlockRepository extends JpaRepository<LocoBlock, Long> {

    Optional<LocoBlock> findByBlockNumber(String blockNumber);
    Optional<LocoBlock> findLocoBlockByBlockNameAndBlockNumberAndSystemType(String blockName, String blockNumber, String systemType);
    Optional<LocoBlock> findLocoBlockByUniqueId(Long uniqueId);
    Optional<LocoBlock> findLocoBlockByRegionAndBlockName(String region, String name);
    Optional<LocoBlock> findLocoBlockByBlockNumber(String blockNumber);
    void deleteLocoBlockByBlockNumber(String blockNumber);
}