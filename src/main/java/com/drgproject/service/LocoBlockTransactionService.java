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
            transaction.setStorage(transactionDto.getStorage());
            transaction.setLocoBlock(transactionDto.getLocoBlock());
            transaction.setEmployee(transactionDto.getEmployee());
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
    public LocoBlockTransactionDto addLocoBlockToStorage(Long storageId, Long locoBlockId, Long employeeId) {
        Optional<Storage> storage = storageRepository.findById(storageId);
        Optional<LocoBlock> locoBlock = locoBlockRepository.findById(locoBlockId);

        Employee employee = employeeRepository.findById(employeeId).orElse(null);
        if (employee == null){
            throw new IllegalArgumentException("Employee with number " + employeeId + " not found");
        }

        if (storage.isPresent() && locoBlock.isPresent()) {
            LocoBlockTransaction transaction = new LocoBlockTransaction();
            transaction.setStorage(storage.get());
            transaction.setLocoBlock(locoBlock.get());
            transaction.setEmployee(employee); // Set employeeId after implementing Employee entity
            transaction.setTransactionType("на складе");
            transaction.setQuantity(1);
            transaction = locoBlockTransactionRepository.save(transaction);
            return convertToDTO(transaction);
        }
        throw new IllegalArgumentException("Storage with number " + storageId +
                " LockBlock with number " + locoBlockId +  " not found");
    }

    @Transactional
    public LocoBlockTransactionDto removeLocoBlockFromStorage(Long storageId, String blockNumber, Long employeeId) {
        List<LocoBlock> locoBlocks = locoBlockRepository.findByBlockNumber(blockNumber).stream().toList();

        if (locoBlocks.isEmpty()) {
            throw new IllegalArgumentException("LocoBlock with number " + blockNumber + " not found.");
        }

        Employee employee = employeeRepository.findById(employeeId).orElse(null);
        if (employee == null){
            throw new IllegalArgumentException("User with numberTable " + employeeId + " not found");
        }
        for (LocoBlock locoBlock : locoBlocks) {
            if (locoBlock.getStorage().getId().equals(storageId)) {
                LocoBlockTransaction transaction = new LocoBlockTransaction();
                transaction.setStorage(locoBlock.getStorage());
                transaction.setLocoBlock(locoBlock);
                transaction.setEmployee(employee); // Set employeeId after implementing Employee entity
                transaction.setTransactionType("отгружен");
                transaction.setQuantity(0);
                locoBlockTransactionRepository.save(transaction);
                return convertToDTO(transaction);
            }
        }
        throw new IllegalArgumentException("LocoBlock with number " + blockNumber + " is not in the specified storage.");
    }

    private LocoBlockTransactionDto convertToDTO(LocoBlockTransaction transaction) {
        LocoBlockTransactionDto transactionDto = new LocoBlockTransactionDto(
                transaction.getStorage(),
                transaction.getLocoBlock(),
                transaction.getEmployee(),
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
        transaction.setStorage(transactionDto.getStorage());
        transaction.setLocoBlock(transactionDto.getLocoBlock());
        transaction.setEmployee(transactionDto.getEmployee());
        transaction.setTransactionType(transactionDto.getTransactionType());
        transaction.setQuantity(transactionDto.getQuantity());
        transaction.setDateCreate(transactionDto.getDateCreate());
        return transaction;
    }
}
