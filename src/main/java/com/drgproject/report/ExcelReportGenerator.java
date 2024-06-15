package com.drgproject.report;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ExcelReportGenerator {
    public ByteArrayInputStream generateReport(List<StorageDto> storages,
                                               List<LocoBlockDto> locoBlocks,
                                               List<TransactionDto> receipts,
                                               List<TransactionDto> shipments) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Отчет");

            int rowNum = 0;
            Row headerRow = sheet.createRow(rowNum++);
            headerRow.createCell(0).setCellValue("Склад");
            headerRow.createCell(1).setCellValue("Тип блока");
            headerRow.createCell(2).setCellValue("Наименование блока");
            headerRow.createCell(3).setCellValue("Номер блока");
            headerRow.createCell(4).setCellValue("Количество");

            Map<Long, String> blockIdToName = locoBlocks.stream()
                    .collect(Collectors.toMap(LocoBlockDto::getUniqueId,
                            LocoBlockDto::getBlockName));

            Map<Long, Integer> storageCounts = receipts.stream()
                    .collect(Collectors.groupingBy(TransactionDto::getLocoBlockUniqueId,
                            Collectors.summingInt(TransactionDto::getQuantity)));

            Map<Long, Integer> shipmentCounts = shipments.stream()
                    .collect(Collectors.groupingBy(TransactionDto::getLocoBlockUniqueId,
                            Collectors.summingInt(TransactionDto::getQuantity)));

            for (TransactionDto receipt : receipts) {
                int quantityInStock = storageCounts.getOrDefault(receipt.getLocoBlockUniqueId(), 0)
                        - shipmentCounts.getOrDefault(receipt.getLocoBlockUniqueId(), 0);
                if (quantityInStock > 0) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(receipt.getStorageName());
                    row.createCell(1).setCellValue(locoBlocks.stream()
                            .filter(block -> block.getUniqueId().equals(receipt.getLocoBlockUniqueId()))
                            .findFirst().orElse(new LocoBlockDto()).getSystemType());
                    row.createCell(2).setCellValue(blockIdToName.get(receipt.getLocoBlockUniqueId()));
                    row.createCell(3).setCellValue(receipt.getLocoBlockUniqueId().toString());
                    row.createCell(4).setCellValue(quantityInStock);
                }
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при создании Excel файла", e);
        }
    }
}
