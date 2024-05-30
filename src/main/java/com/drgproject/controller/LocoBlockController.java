package com.drgproject.controller;

import com.drgproject.dto.LocoBlockDto;
import com.drgproject.service.LocoBlockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/locoblocks")
public class LocoBlockController {

    private final LocoBlockService locoBlockService;

    public LocoBlockController(LocoBlockService locoBlockService) {
        this.locoBlockService = locoBlockService;
    }

    /**
     * Получение списка всех LocoBlock.
     *
     * @return список всех LocoBlock
     */
    @GetMapping
    public ResponseEntity<List<LocoBlockDto>> getAllLocoBlocks() {
        List<LocoBlockDto> locoBlocks = locoBlockService.getAllLocoBlocks();
        return ResponseEntity.ok(locoBlocks);
    }

    /**
     * Получение LocoBlock по его ID.
     *
     * @param id идентификатор LocoBlock
     * @return найденный LocoBlock или статус 404, если не найден
     */
    @GetMapping("/{id}")
    public ResponseEntity<LocoBlockDto> getLocoBlockById(@PathVariable Long id) {
        LocoBlockDto locoBlock = locoBlockService.getLocoBlockById(id);
        if (locoBlock != null) {
            return ResponseEntity.ok(locoBlock);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Создание нового LocoBlock.
     *
     * @param locoBlockDto DTO с данными для нового LocoBlock
     * @return созданный LocoBlock
     */
    @PostMapping
    public ResponseEntity<LocoBlockDto> createLocoBlock(@RequestBody LocoBlockDto locoBlockDto) {
        LocoBlockDto newLocoBlock = locoBlockService.createLocoBlock(locoBlockDto);
        return ResponseEntity.ok(newLocoBlock);
    }

    /**
     * Обновление существующего LocoBlock.
     *
     * @param id идентификатор обновляемого LocoBlock
     * @param locoBlockDto DTO с новыми данными для LocoBlock
     * @return обновленный LocoBlock или статус 404, если не найден
     */
    @PutMapping("/{id}")
    public ResponseEntity<LocoBlockDto> updateLocoBlock(@PathVariable Long id, @RequestBody LocoBlockDto locoBlockDto) {
        LocoBlockDto updatedLocoBlock = locoBlockService.updateLocoBlock(id, locoBlockDto);
        if (updatedLocoBlock != null) {
            return ResponseEntity.ok(updatedLocoBlock);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Удаление LocoBlock по его ID.
     *
     * @param id идентификатор удаляемого LocoBlock
     * @return статус 204 (No Content), если удаление прошло успешно
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocoBlock(@PathVariable Long id) {
        locoBlockService.deleteLocoBlock(id);
        return ResponseEntity.noContent().build();
    }
}
