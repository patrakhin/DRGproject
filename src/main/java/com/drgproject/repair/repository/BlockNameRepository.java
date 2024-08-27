package com.drgproject.repair.repository;

import com.drgproject.repair.entity.BlockName;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BlockNameRepository extends JpaRepository<BlockName, Long> {

    List<BlockName> findBySystemNameId(Long systemNameId);
}
