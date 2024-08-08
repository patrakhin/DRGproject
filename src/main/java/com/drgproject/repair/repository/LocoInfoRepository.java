package com.drgproject.repair.repository;

import com.drgproject.repair.entity.LocoInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface LocoInfoRepository extends JpaRepository<LocoInfo, Long> {

    // Поиск LocoInfo по locoUnit
    Optional<LocoInfo> findByLocoUnit(String locoUnit);

    // Удаление LocoInfo по locoUnit
    void deleteByLocoUnitAndLocoType(String locoUnit, String locoType);
}

