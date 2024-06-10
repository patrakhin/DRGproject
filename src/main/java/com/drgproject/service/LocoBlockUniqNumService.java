package com.drgproject.service;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class LocoBlockUniqNumService {
    /**
     * Генерация уникального номера записи на основе типа блока, наименования блока и номера блока.
     *
     * @param blockType   тип блока
     * @param blockName   наименование блока
     * @param blockNumber номер блока
     * @return уникальный номер записи
     */
    public Long generateUniqueId(String blockType, String blockName, String blockNumber) {
        // Конкатенируем входные данные с разделителями
        String combinedString = blockType + "|" + blockName + "|" + blockNumber;
        // Генерируем хешкод строки
        return (long) combinedString.hashCode();
    }

    /**
     * Получение исходных данных (тип блока, наименование блока и номер блока) по уникальному номеру.
     *
     * @param uniqueId уникальный номер записи
     * @return список с исходными данными в порядке: тип блока, наименование блока, номер блока
     */
    public List<String> parseUniqueId(Long uniqueId) {
        // Преобразуем уникальный номер обратно в строку (имитация хеширования)
        String combinedString = String.valueOf(uniqueId);
        // Разбиваем строку на части
        String[] parts = combinedString.split("\\|");
        return Arrays.asList(parts);
    }
}
