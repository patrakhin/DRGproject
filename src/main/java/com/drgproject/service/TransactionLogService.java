package com.drgproject.service;

import com.drgproject.dto.TransactionLogDto;
import com.drgproject.entity.TransactionLog;
import com.drgproject.repository.TransactionLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionLogService {

    private final TransactionLogRepository transactionLogRepository;

    public TransactionLogService(TransactionLogRepository transactionLogRepository) {
        this.transactionLogRepository = transactionLogRepository;
    }

    @Transactional(readOnly = true)
    public List<TransactionLogDto> getAllTransactionLogs() {
        return transactionLogRepository.findAll().stream()
                .map(this::convertToDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public TransactionLogDto getTransactionLogById(Long id) {
        Optional<TransactionLog> transactionLog = transactionLogRepository.findById(id);
        return transactionLog.map(this::convertToDto).orElse(null);
    }

    @Transactional
    public TransactionLogDto createTransactionLog(TransactionLogDto transactionLogDto) {
        TransactionLog transactionLog = convertToEntity(transactionLogDto);
        TransactionLog savedTransactionLog = transactionLogRepository.save(transactionLog);
        return convertToDto(savedTransactionLog);
    }

    @Transactional
    public TransactionLogDto updateTransactionLog(Long id, TransactionLogDto transactionLogDto) {
        Optional<TransactionLog> optionalTransactionLog = transactionLogRepository.findById(id);
        if (optionalTransactionLog.isPresent()) {
            TransactionLog transactionLog = optionalTransactionLog.get();
            transactionLog.setSystemType(transactionLogDto.getSystemType());
            transactionLog.setBlockIn(transactionLogDto.getBlockIn());
            transactionLog.setBlockOut(transactionLogDto.getBlockOut());
            transactionLog.setRepairHistory(transactionLogDto.getRepairHistory());
            TransactionLog updatedTransactionLog = transactionLogRepository.save(transactionLog);
            return convertToDto(updatedTransactionLog);
        }
        return null;
    }

    @Transactional
    public void deleteTransactionLog(Long id) {
        transactionLogRepository.deleteById(id);
    }

    private TransactionLog convertToEntity(TransactionLogDto transactionLogDto) {
        TransactionLog transactionLog = new TransactionLog();
        transactionLog.setId(transactionLogDto.getId());
        transactionLog.setSystemType(transactionLogDto.getSystemType());
        transactionLog.setBlockIn(transactionLogDto.getBlockIn());
        transactionLog.setBlockOut(transactionLogDto.getBlockOut());
        transactionLog.setDateCreate(transactionLogDto.getDateCreate());
        transactionLog.setRepairHistory(transactionLogDto.getRepairHistory());
        return transactionLog;
    }

    private TransactionLogDto convertToDto(TransactionLog transactionLog) {
        TransactionLogDto dto = new TransactionLogDto();
        dto.setId(transactionLog.getId());
        dto.setSystemType(transactionLog.getSystemType());
        dto.setBlockIn(transactionLog.getBlockIn());
        dto.setBlockOut(transactionLog.getBlockOut());
        dto.setDateCreate(transactionLog.getDateCreate());
        dto.setRepairHistory(transactionLog.getRepairHistory());
        return dto;
    }
}
