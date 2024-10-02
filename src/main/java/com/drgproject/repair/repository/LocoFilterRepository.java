package com.drgproject.repair.repository;

import com.drgproject.repair.entity.LocoFilter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocoFilterRepository extends JpaRepository<LocoFilter, Long> {

    Optional<LocoFilter> findByHomeRegionAndHomeDepotAndLocoTypeAndSectionNumber(String homeRegion, String homeDepot, String locoType, String sectionNumber);

    List<LocoFilter> findByHomeRegionAndHomeDepotAndLocoTypeAndFreeSection(String homeRegion, String homeDepot, String locoType, String freeSection);
}
