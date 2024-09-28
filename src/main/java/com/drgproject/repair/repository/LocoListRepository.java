package com.drgproject.repair.repository;

import com.drgproject.repair.entity.LocoList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocoListRepository extends JpaRepository<LocoList, Long> {
    Optional<LocoList> findLocoListByLocoNumber(String locoNumber);
    Optional<LocoList> findLocoListByLocoNumberAndTypeLoco(String locoNumber, String typeLoco);
    // Метод для поиска номеров локомотивов, начинающихся с указанного префикса
    List<LocoList> findByLocoNumberStartingWith(String prefix);
    //Проверка на дублирование
    Optional<LocoList> findLocoListByHomeRegionAndHomeDepotAndLocoNumber(String homeRegion, String homeDepot, String locoNumber);

    //Проверка на дублирование при загрузке из файла
    Optional<LocoList> findLocoListByHomeRegionAndHomeDepotAndTypeLocoAndLocoNumber(String homeRegion, String homeDepot, String typeLoco, String locoNumber);

    //ПРоверка на существование (для создания блока на секции)
    Optional<LocoList> findLocoListByTypeLocoAndLocoNumber(String typeLoco, String locoNumber);

    List<LocoList> findAllByHomeRegion(String homeRegion);

    // Получаем список список всех номеров секций по Region, homeDepot, typeLoco
    List<LocoList> findAllByHomeRegionAndHomeDepotAndTypeLoco(String homeRegion, String homeDepot, String typeLoco);

    // Приверка на сущ по дороге депо прип серии секци и номеру секции
    boolean existsByHomeRegionAndHomeDepotAndTypeLocoAndLocoNumber(String homeRegion, String homeDepot, String typeLoco, String locoNumber);
}
