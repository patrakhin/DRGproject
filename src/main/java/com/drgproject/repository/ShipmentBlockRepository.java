package com.drgproject.repository;

import com.drgproject.entity.ShipmentBlock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShipmentBlockRepository extends JpaRepository<ShipmentBlock, Long> {
    Optional<ShipmentBlock> findShipmentBlockByRegion(String region);
    Optional<ShipmentBlock> findShipmentBlockByStorageName(String storageName);
    Optional<ShipmentBlock> findShipmentBlockByLocoBlockUniqueId(Long uniqueId);
}
