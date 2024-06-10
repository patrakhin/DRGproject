package com.drgproject.controller;

import com.drgproject.dto.LocoBlockTransactionDto;
import com.drgproject.dto.LocoBlockTransactionRequestDto;
import com.drgproject.service.LocoBlockTransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class LocoBlockTransactionController {

    private final LocoBlockTransactionService transactionService;

    public LocoBlockTransactionController(LocoBlockTransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * Получение списка всех транзакций LocoBlock.
     *
     * @return список всех транзакций
     */
    @GetMapping
    public ResponseEntity<List<LocoBlockTransactionDto>> getAllTransactions() {
        List<LocoBlockTransactionDto> transactions = transactionService.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }

    /**
     * Получение транзакции LocoBlock по её ID.
     *
     * @param id идентификатор транзакции
     * @return найденная транзакция или статус 404, если не найдена
     */
    @GetMapping("/{id}")
    public ResponseEntity<LocoBlockTransactionDto> getTransactionById(@PathVariable Long id) {
        LocoBlockTransactionDto transaction = transactionService.getTransactionById(id);
        if (transaction != null) {
            return ResponseEntity.ok(transaction);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Создание новой транзакции LocoBlock.
     *
     * @param transactionDto DTO с данными для новой транзакции
     * @return созданная транзакция
     */
    @PostMapping
    public ResponseEntity<LocoBlockTransactionDto> createTransaction(@RequestBody LocoBlockTransactionDto transactionDto) {
        LocoBlockTransactionDto newTransaction = transactionService.createTransaction(transactionDto);
        return ResponseEntity.ok(newTransaction);
    }

    /**
     * Обновление существующей транзакции LocoBlock.
     *
     * @param id идентификатор обновляемой транзакции
     * @param transactionDto DTO с новыми данными для транзакции
     * @return обновленная транзакция или статус 404, если не найдена
     */
    @PutMapping("/{id}")
    public ResponseEntity<LocoBlockTransactionDto> updateTransaction(@PathVariable Long id, @RequestBody LocoBlockTransactionDto transactionDto) {
        LocoBlockTransactionDto updatedTransaction = transactionService.updateTransaction(id, transactionDto);
        if (updatedTransaction != null) {
            return ResponseEntity.ok(updatedTransaction);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Удаление транзакции LocoBlock по её ID.
     *
     * @param id идентификатор удаляемой транзакции
     * @return статус 204 (No Content), если удаление прошло успешно
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Добавление LocoBlock на склад.
     *
     * @param requestDto DTO с данными для новой транзакции
     * @return созданная транзакция
     */
    @PostMapping("/add-to-storage")
    public ResponseEntity<LocoBlockTransactionDto> addLocoBlockToStorage(
            @RequestBody LocoBlockTransactionRequestDto requestDto) {
        try {
            LocoBlockTransactionDto newTransaction = transactionService.addLocoBlockToStorage(
                    requestDto.getStorageName(),
                    requestDto.getNameBlock(),
                    requestDto.getSystemType(),
                    requestDto.getBlockNumber(),
                    requestDto.getNumberTable());
            return ResponseEntity.ok(newTransaction);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }


    /**
     * Удаление LocoBlock со склада.
     *
     * @param storageName  наименование склада
     * @param nameBlock наименование блока
     * @param systemType  тип системы
     * @param blockNumber номер блока
     * @param numberTable  табельный номер сотрудника
     * @return созданная транзакция
     */
    @PostMapping("/remove-from-storage")
    public ResponseEntity<LocoBlockTransactionDto> removeLocoBlockFromStorage(
            @RequestParam String storageName,
            @RequestParam String nameBlock,
            @RequestParam String systemType,
            @RequestParam String blockNumber,
            @RequestParam String numberTable) {
        try {
            LocoBlockTransactionDto newTransaction = transactionService.removeLocoBlockFromStorage(storageName, nameBlock,
                    systemType, blockNumber, numberTable);
            return ResponseEntity.ok(newTransaction);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}