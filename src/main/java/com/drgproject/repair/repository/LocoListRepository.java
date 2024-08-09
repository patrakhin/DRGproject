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
}
