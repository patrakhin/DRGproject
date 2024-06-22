package com.drgproject.repair.repository;

import com.drgproject.repair.entity.BlockOnLoco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlockOnLocoRepository extends JpaRepository<BlockOnLoco, Long> {
}
