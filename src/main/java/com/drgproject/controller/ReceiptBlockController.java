package com.drgproject.controller;

import com.drgproject.dto.ReceiptBlockDto;
import com.drgproject.dto.ReceiptBlockRequestDto;
import com.drgproject.service.ReceiptBlockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/receipt")
public class ReceiptBlockController {

    private final ReceiptBlockService receiptBlockService;

    public ReceiptBlockController(ReceiptBlockService receiptBlockService) {
        this.receiptBlockService = receiptBlockService;
    }

    /**
     * Получение списка всех записей поступления LocoBlock.
     *
     * @return список всех записей поступления
     */
    @GetMapping
    public ResponseEntity<List<ReceiptBlockDto>> getAllReceiptBlock() {
        List<ReceiptBlockDto> receiptBlock = receiptBlockService.getAllReceiptBlock();
        return ResponseEntity.ok(receiptBlock);
    }

    /**
     * Получение записи поступления LocoBlock по его ID.
     *
     * @param id идентификатор записи
     * @return найденная запись или статус 404, если не найдена
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReceiptBlockDto> getReceiptBlockById(@PathVariable Long id) {
        ReceiptBlockDto receiptBlockById = receiptBlockService.getReceiptBlockById(id);
        if (receiptBlockById != null) {
            return ResponseEntity.ok(receiptBlockById);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Удаление записи LocoBlock по её ID при ошибочном добавлении на склад
     *
     * @param id идентификатор удаляемой транзакции
     * @return статус 204 (No Content), если удаление прошло успешно
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        receiptBlockService.deleteReceiptBlock(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Добавление записи о поступлении LocoBlock на склад.
     *
     * @param receiptBlockRequestDto DTO с данными для новой записи
     * @return созданная запись
     */
    @PostMapping("/add-to-storage")
    public ResponseEntity<ReceiptBlockDto> addLocoBlockToStorage(
            @RequestBody ReceiptBlockRequestDto receiptBlockRequestDto) {
        try {
            ReceiptBlockDto receiptBlockDto = receiptBlockService.addLocoBlockToReceipt(
                    receiptBlockRequestDto.getStorageName(),
                    receiptBlockRequestDto.getNumberTable(),
                    receiptBlockRequestDto.getSystemType(),
                    receiptBlockRequestDto.getNameBlock(),
                    receiptBlockRequestDto.getBlockNumber());
            return ResponseEntity.ok(receiptBlockDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}