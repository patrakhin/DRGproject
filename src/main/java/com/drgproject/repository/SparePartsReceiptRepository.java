package com.drgproject.repository;

import com.drgproject.entity.SparePartsReceipt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SparePartsReceiptRepository extends JpaRepository<SparePartsReceipt, Long> {
    Optional<List<SparePartsReceipt>> findSparePartsReceiptBySparePartNameAndSparePartNumber(String sparePartsName, Long sparePartsNumber);
    SparePartsReceipt findSparePartsReceiptBySparePartNameAndSparePartNumberAndTransactionType(String sparePartsName, Long sparePartsNumber, String transactionType);

}
