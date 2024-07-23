package com.drgproject.repair.repository;

import com.drgproject.repair.entity.PositionRepair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PositionRepairRepository extends JpaRepository<PositionRepair, Long> {
    Optional<PositionRepair> findByPosRepair(String namePosRepair);
}
