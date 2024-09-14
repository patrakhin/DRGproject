package com.drgproject.repair.repository;

import com.drgproject.repair.entity.RepDepot;
import com.drgproject.repair.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RepDepotRepository extends JpaRepository<RepDepot, Long> {

    // Найти все депо ремонта в конкретном регионе
    List<RepDepot> findByRegion(Region region);

    // Найти депо ремонта по имени и региону
    Optional<RepDepot> findByNameAndRegion(String name, Region region);

    // Найти депо ремонта по имени и имени региона

}
