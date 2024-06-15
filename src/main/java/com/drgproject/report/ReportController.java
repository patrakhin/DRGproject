package com.drgproject.report;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/report")
public class ReportController {
    private final ReportService reportService;
    private final ExcelReportGenerator excelReportGenerator;

    public ReportController(ReportService reportService, ExcelReportGenerator excelReportGenerator) {
        this.reportService = reportService;
        this.excelReportGenerator = excelReportGenerator;
    }

    @GetMapping("/stock")
    public ResponseEntity<InputStreamResource> generateStockReport() {
        List<StorageDto> storages = reportService.getAllStorages();
        List<LocoBlockDto> locoBlocks = reportService.getAllLocoBlocks();
        List<TransactionDto> receipts = reportService.getReceiptTransactions();
        List<TransactionDto> shipments = reportService.getShipmentTransactions();

        ByteArrayInputStream bis = excelReportGenerator.generateReport(storages, locoBlocks, receipts, shipments);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=stock_report.xlsx");

        return ResponseEntity.ok().headers(headers).body(new InputStreamResource(bis));
    }
}
