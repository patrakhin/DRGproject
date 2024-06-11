package com.drgproject.service;

import com.drgproject.dto.ReceiptBlockDto;
import com.drgproject.entity.Employee;
import com.drgproject.entity.LocoBlock;
import com.drgproject.entity.ReceiptBlock;
import com.drgproject.entity.Storage;
import com.drgproject.repository.EmployeeRepository;
import com.drgproject.repository.LocoBlockRepository;
import com.drgproject.repository.ReceiptBlockRepository;
import com.drgproject.repository.StorageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ReceiptBlockService {

    private final ReceiptBlockRepository receiptBlockRepository;
    private final LocoBlockRepository locoBlockRepository;
    private final StorageRepository storageRepository;
    private final EmployeeRepository employeeRepository;
    private final LocoBlockUniqNumService locoBlockUniqNumService;

    public ReceiptBlockService(ReceiptBlockRepository receiptBlockRepository,
                               LocoBlockRepository locoBlockRepository,
                               StorageRepository storageRepository,
                               EmployeeRepository employeeRepository,
                               LocoBlockUniqNumService locoBlockUniqNumService) {
        this.receiptBlockRepository = receiptBlockRepository;
        this.locoBlockRepository = locoBlockRepository;
        this.storageRepository = storageRepository;
        this.employeeRepository = employeeRepository;
        this.locoBlockUniqNumService = locoBlockUniqNumService;
    }

    @Transactional
    public List<ReceiptBlockDto> getAllReceiptBlock() {
        return receiptBlockRepository.findAll().stream().map(this::convertToDTO).toList();
    }

    @Transactional
    public ReceiptBlockDto getReceiptBlockById(Long id) {
        return receiptBlockRepository.findById(id).map(this::convertToDTO).orElse(null);
    }

    @Transactional
    public void deleteReceiptBlock(Long id) {
        receiptBlockRepository.deleteById(id);
    }

    @Transactional
    public ReceiptBlockDto addLocoBlockToReceipt(String storageName, String numberTable,
                                                 String systemType, String nameBlock, String blockNumber) {
        Optional<Storage> storage = storageRepository.findStorageByStorageName(storageName);
        Long getUniqueId = locoBlockUniqNumService.generateUniqueId(systemType, nameBlock, blockNumber);
        Optional<LocoBlock> locoBlockByUniqueId = locoBlockRepository.findLocoBlockByUniqueId(getUniqueId);

        Employee employee = employeeRepository.findEmployeeByNumberTable(numberTable).orElse(null);
        if (employee == null){
            throw new IllegalArgumentException("Employee with number " + numberTable + " not found");
        }

        if (storage.isPresent() && locoBlockByUniqueId.isPresent()) {
            ReceiptBlock receiptBlock = new ReceiptBlock();
            receiptBlock.setStorageName(storage.get().getStorageName());
            receiptBlock.setEmployeeNumber(employee.getNumberTable()); // Set employeeId after implementing Employee entity
            receiptBlock.setLocoBlockUniqueId(getUniqueId);
            receiptBlock.setTransactionType("на складе");
            receiptBlock.setQuantity(1);
            receiptBlock = receiptBlockRepository.save(receiptBlock);
            return convertToDTO(receiptBlock);
        }
        throw new IllegalArgumentException("Storage with name " + storageName +
                " LockBlock with unique number " + getUniqueId +  " not found");
    }

    private ReceiptBlockDto convertToDTO(ReceiptBlock receiptBlock) {
        ReceiptBlockDto receiptBlockDto =  new ReceiptBlockDto(
                receiptBlock.getStorageName(),
                receiptBlock.getEmployeeNumber(),
                receiptBlock.getLocoBlockUniqueId(),
                receiptBlock.getTransactionType(),
                receiptBlock.getQuantity()
        );
        receiptBlockDto.setId(receiptBlock.getId());
        receiptBlockDto.setDateCreate(receiptBlock.getDateCreate());
        return receiptBlockDto;
    }

    private ReceiptBlock convertToEntity(ReceiptBlockDto receiptBlockDto) {
        ReceiptBlock receiptBlock = new ReceiptBlock();
        receiptBlock.setStorageName(receiptBlockDto.getStorageName());
        receiptBlock.setEmployeeNumber(receiptBlockDto.getEmployeeNumber());
        receiptBlock.setLocoBlockUniqueId(receiptBlockDto.getLocoBlockUniqueId());
        receiptBlock.setTransactionType(receiptBlockDto.getTransactionType());
        receiptBlock.setQuantity(receiptBlockDto.getQuantity());
        return receiptBlock;
    }
}
