package com.drgproject.repair.repository;

import com.drgproject.repair.entity.TypeLoco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeLocoRepository extends JpaRepository<TypeLoco, Long> {
    boolean existsByLocoType(String locoType);
}
