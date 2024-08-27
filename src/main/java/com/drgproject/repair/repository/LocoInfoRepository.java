package com.drgproject.repair.repository;

import com.drgproject.repair.entity.LocoInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocoInfoRepository extends JpaRepository<LocoInfo, Long> {

    // Поиск LocoInfo по locoUnit
    Optional<LocoInfo> findByLocoUnit(String locoUnit);

    // Удаление LocoInfo по locoUnit
    void deleteByLocoUnitAndLocoType(String locoUnit, String locoType);

    //Получение LocoInfo по Региону и серии локомотива
    Page<LocoInfo> findByRegionAndLocoType(String region, String locoType, Pageable pageable);

    //Получение LocoInfo по региону, депо приписки, номеру локомотива
    Optional<LocoInfo> findByRegionAndHomeDepotAndLocoUnit(String region, String homeDepot, String locoUnit);

    // Проверка на существование уже созданного локомотива
    Optional<LocoInfo> findByRegionAndHomeDepotAndLocoTypeAndLocoSection1(String region, String homeDepot, String locoType, String locoSection1);
}

