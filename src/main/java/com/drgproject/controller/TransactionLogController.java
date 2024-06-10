package com.drgproject.controller;
import com.drgproject.dto.TransactionLogDto;
import com.drgproject.exception.InvalidDataException;
import com.drgproject.exception.ResourceNotFoundException;
import com.drgproject.service.TransactionLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/transactionLogs")
public class TransactionLogController {

    private final TransactionLogService transactionLogService;

    public TransactionLogController(TransactionLogService transactionLogService) {
        this.transactionLogService = transactionLogService;
    }

    @GetMapping
    public List<TransactionLogDto> getAllTransactionLogs() {
        return transactionLogService.getAllTransactionLogs();
    }

    @GetMapping("/{id}")
    public TransactionLogDto getTransactionLogById(@PathVariable Long id) {
        TransactionLogDto transactionLog = transactionLogService.getTransactionLogById(id);
        if (transactionLog == null) {
            throw new ResourceNotFoundException("TransactionLog not found with id: " + id);
        }
        return transactionLog;
    }

    @PostMapping
    public TransactionLogDto createTransactionLog(@Valid @RequestBody TransactionLogDto transactionLogDto) {
        if (transactionLogDto.getId() != null) {
            throw new InvalidDataException("New transaction log cannot have an ID");
        }
        return transactionLogService.createTransactionLog(transactionLogDto);
    }

    @PutMapping("/{id}")
    public TransactionLogDto updateTransactionLog(@PathVariable Long id, @Valid @RequestBody TransactionLogDto transactionLogDto) {
        TransactionLogDto updatedTransactionLog = transactionLogService.updateTransactionLog(id, transactionLogDto);
        if (updatedTransactionLog == null) {
            throw new ResourceNotFoundException("TransactionLog not found with id: " + id);
        }
        return updatedTransactionLog;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransactionLog(@PathVariable Long id) {
        transactionLogService.deleteTransactionLog(id);
        return ResponseEntity.noContent().build();
    }
}
