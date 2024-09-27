package com.drgproject.repair.repository;

import com.drgproject.repair.entity.HomeDepot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HomeDepotRepository extends JpaRepository<HomeDepot, Long> {
    List<HomeDepot> findByRegionId(Long regionId);
    boolean existsByDepotAndRegionId(String depot, Long regionId);
}