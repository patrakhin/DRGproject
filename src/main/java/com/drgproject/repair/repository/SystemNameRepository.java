package com.drgproject.repair.repository;

import com.drgproject.repair.entity.SystemName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemNameRepository extends JpaRepository<SystemName, Long> {
    SystemName findBySysName(String systemName);
}
