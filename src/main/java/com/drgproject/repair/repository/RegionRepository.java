package com.drgproject.repair.repository;

import com.drgproject.repair.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {
    Optional<Region> findRegionByName(String name);
    void deleteRegionByName(String name);
}
