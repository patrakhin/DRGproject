package com.drgproject.repair.repository;

import com.drgproject.repair.entity.BlockRemoval;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface BlockRemovalRepository extends JpaRepository<BlockRemoval, Long> {
    Optional<List<BlockRemoval>> findBlockRemovalByRegion(String region);
    Page<BlockRemoval> findBlockRemovalByRegionAndHomeDepot(String region, String homeDepot, Pageable pageable);
    Optional<List<BlockRemoval>> findBlockRemovalByTypeLocoAndLocoNumber(String typeLoco, String locoNumber);
    Optional<BlockRemoval> findBlockRemovalBySystemTypeAndBlockNameAndBlockNumber(String systemType, String blockName, String blockNumber);
}
