package com.drgproject.repository;

import com.drgproject.entity.LocoBlockTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LocoBlockTransactionRepository extends JpaRepository<LocoBlockTransaction, Long> {
    Optional<LocoBlockTransaction> findLocoBlockTransactionByTransactionType(String transactionType);
    @Query("from LocoBlockTransaction where storageId = :storageId and locoBlockId = :lockblockID and employeeId = :employeeId")
    List<LocoBlockTransaction> findByLockoblockStorageEmployee(@Param("storageId") Long storageId,
                                                               @Param("lockblockID") Long lockblockID,
                                                               @Param("employeeId") Long employeeId);
}