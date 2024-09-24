package com.drgproject.repair.repository;

import com.drgproject.repair.entity.AllTypeLocoUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AllTypeLocoUnitRepository extends JpaRepository<AllTypeLocoUnit, Long> {
}
