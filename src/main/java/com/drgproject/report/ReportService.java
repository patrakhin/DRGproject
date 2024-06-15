package com.drgproject.report;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ReportService {
    private final RestTemplate restTemplate;

    public ReportService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public List<StorageDto> getAllStorages() {
        String url = "http://localhost:8080/storages";
        ResponseEntity<List<StorageDto>> response = restTemplate.exchange(
                url, HttpMethod.GET, null, new ParameterizedTypeReference<List<StorageDto>>() {});
        return response.getBody();
    }

    public List<LocoBlockDto> getAllLocoBlocks() {
        String url = "http://localhost:8080/loco_blocks";
        ResponseEntity<List<LocoBlockDto>> response = restTemplate.exchange(
                url, HttpMethod.GET, null, new ParameterizedTypeReference<List<LocoBlockDto>>() {});
        return response.getBody();
    }

    public List<TransactionDto> getReceiptTransactions() {
        String url = "http://localhost:8080/receipt";
        ResponseEntity<List<TransactionDto>> response = restTemplate.exchange(
                url, HttpMethod.GET, null, new ParameterizedTypeReference<List<TransactionDto>>() {});
        return response.getBody();
    }

    public List<TransactionDto> getShipmentTransactions() {
        String url = "http://localhost:8080/shipment";
        ResponseEntity<List<TransactionDto>> response = restTemplate.exchange(
                url, HttpMethod.GET, null, new ParameterizedTypeReference<List<TransactionDto>>() {});
        return response.getBody();
    }
}
