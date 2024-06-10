package com.drgproject.service;

import com.drgproject.dto.LocoBlockTransactionDto;
import com.drgproject.entity.Employee;
import com.drgproject.entity.LocoBlock;
import com.drgproject.entity.LocoBlockTransaction;
import com.drgproject.entity.Storage;
import com.drgproject.repository.EmployeeRepository;
import com.drgproject.repository.LocoBlockRepository;
import com.drgproject.repository.LocoBlockTransactionRepository;
import com.drgproject.repository.StorageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class LocoBlockTransactionService {

    private final LocoBlockTransactionRepository locoBlockTransactionRepository;
    private final LocoBlockRepository locoBlockRepository;
    private final StorageRepository storageRepository;
    private final EmployeeRepository employeeRepository;

    public LocoBlockTransactionService(LocoBlockTransactionRepository locoBlockTransactionRepository,
                                       LocoBlockRepository locoBlockRepository,
                                       StorageRepository storageRepository, EmployeeRepository employeeRepository) {
        this.locoBlockTransactionRepository = locoBlockTransactionRepository;
        this.locoBlockRepository = locoBlockRepository;
        this.storageRepository = storageRepository;
        this.employeeRepository = employeeRepository;
    }

    @Transactional
    public List<LocoBlockTransactionDto> getAllTransactions() {
        return locoBlockTransactionRepository.findAll().stream().map(this::convertToDTO).toList();
    }

    @Transactional
    public LocoBlockTransactionDto getTransactionById(Long id) {
        return locoBlockTransactionRepository.findById(id).map(this::convertToDTO).orElse(null);
    }

    @Transactional
    public LocoBlockTransactionDto createTransaction(LocoBlockTransactionDto transactionDto) {
        LocoBlockTransaction locoBlockTransaction = convertToEntity(transactionDto);
        locoBlockTransaction = locoBlockTransactionRepository.save(locoBlockTransaction);
        return convertToDTO(locoBlockTransaction);
    }

    @Transactional
    public LocoBlockTransactionDto updateTransaction(Long id, LocoBlockTransactionDto transactionDto) {
        Optional<LocoBlockTransaction> optionalTransaction = locoBlockTransactionRepository.findById(id);
        if (optionalTransaction.isPresent()) {
            LocoBlockTransaction transaction = optionalTransaction.get();
            /*transaction.setStorage(transactionDto.getStorage());
            transaction.setLocoBlock(transactionDto.getLocoBlocks());
            transaction.setEmployee(transactionDto.getEmployee());*/
            transaction.setTransactionType(transactionDto.getTransactionType());
            transaction.setQuantity(transactionDto.getQuantity());
            transaction = locoBlockTransactionRepository.save(transaction);
            return convertToDTO(transaction);
        }
        return null;
    }

    @Transactional
    public void deleteTransaction(Long id) {
        locoBlockTransactionRepository.deleteById(id);
    }

    @Transactional
    public LocoBlockTransactionDto addLocoBlockToStorage(String storageName, String nameBlock, String systemType,
                                                         String blockNumber, String numberTable) {
        Optional<Storage> storage = storageRepository.findStorageByStorageName(storageName);
        Optional<LocoBlock> locoBlock = locoBlockRepository.findLocoBlockByBlockNameAndBlockNumberAndSystemType(nameBlock,
                blockNumber, systemType);

        Employee employee = employeeRepository.findEmployeeByNumberTable(numberTable).orElse(null);
        if (employee == null){
            throw new IllegalArgumentException("Employee with number " + numberTable + " not found");
        }

        if (storage.isPresent() && locoBlock.isPresent()) {
            LocoBlockTransaction transaction = new LocoBlockTransaction();
            transaction.setStorageId(storage.get().getId());
            transaction.setLocoBlockId(locoBlock.get().getId());
            transaction.setEmployeeId(employee.getId()); // Set employeeId after implementing Employee entity
            transaction.setTransactionType("на складе");
            transaction.setQuantity(1);
            transaction = locoBlockTransactionRepository.save(transaction);
            return convertToDTO(transaction);
        }
        throw new IllegalArgumentException("Storage with name " + storageName +
                " LockBlock with number " + blockNumber +  " not found");
    }

    @Transactional
    public LocoBlockTransactionDto removeLocoBlockFromStorage(String storageName, String nameBlock, String systemType,
                                                              String blockNumber, String numberTable) {
        Optional<Storage> storage = storageRepository.findStorageByStorageName(storageName);
        if(storage.isEmpty()){
            throw new IllegalArgumentException("Storage with name " + storageName + " not found.");
        }
        Long storageIdFromStorage = storage.get().getId();

        Optional<LocoBlock> locoBlock = locoBlockRepository.findLocoBlockByBlockNameAndBlockNumberAndSystemType(nameBlock,
                blockNumber,systemType);
        if (locoBlock.isEmpty()){
            throw new IllegalArgumentException("Block with number " + blockNumber + " not found.");
        }
        Long locoBlockFromSearch = locoBlock.get().getId();

        List <LocoBlockTransaction> locoBlockTransaction = locoBlockTransactionRepository
                .findLocoBlockTransactionByTransactionType("на складе")
                .stream().toList();
        if (locoBlockTransaction.isEmpty()) {
            throw new IllegalArgumentException("LocoBlock with " + "на складе" + " not found.");
        }

        LocoBlockTransaction locoBlockTransactionOne = locoBlockTransaction.stream()
                .filter(logTransaction -> Objects.equals(logTransaction.getStorageId(), storageIdFromStorage))
                .filter(logTransaction -> Objects.equals(logTransaction.getLocoBlockId(), locoBlockFromSearch))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "LocoBlockTransaction with storageId " + storageIdFromStorage +
                                " and locoBlockId " + locoBlockFromSearch + " not found."));


        Employee employee = employeeRepository.findEmployeeByNumberTable(numberTable).orElse(null);
        if (employee == null){
            throw new IllegalArgumentException("User with numberTable " + numberTable + " not found");
        }

        locoBlockTransactionOne.setTransactionType("отгружен");
        locoBlockTransactionOne.setQuantity(0);
        /*locoBlockTransactionOne.setEmployee(employee);*/
        locoBlockTransactionRepository.save(locoBlockTransactionOne);
        return convertToDTO(locoBlockTransactionOne);
    }

    private LocoBlockTransactionDto convertToDTO(LocoBlockTransaction transaction) {
        LocoBlockTransactionDto transactionDto = new LocoBlockTransactionDto(
                transaction.getTransactionType(),
                transaction.getQuantity()
        );
        transactionDto.setId(transaction.getId());
        transactionDto.setDateCreate(transaction.getDateCreate());
        return transactionDto;
    }

    private LocoBlockTransaction convertToEntity(LocoBlockTransactionDto transactionDto) {
        LocoBlockTransaction transaction = new LocoBlockTransaction();
        transaction.setId(transactionDto.getId());
        transaction.setTransactionType(transactionDto.getTransactionType());
        transaction.setQuantity(transactionDto.getQuantity());
        transaction.setDateCreate(transactionDto.getDateCreate());
        return transaction;
    }
}
