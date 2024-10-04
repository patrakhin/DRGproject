package com.drgproject.repair;

import java.util.Comparator;

public class SectionNumberComparator implements Comparator<String> {

    @Override
    public int compare(String s1, String s2) {
        // Разделение строк на числовую часть и букву (если есть)
        String[] parts1 = splitSectionNumber(s1);
        String[] parts2 = splitSectionNumber(s2);

        // Сравнение числовой части
        int numCompare = Integer.compare(Integer.parseInt(parts1[0]), Integer.parseInt(parts2[0]));
        if (numCompare != 0) {
            return numCompare;  // Если числовая часть различается, возвращаем результат
        }

        // Сравнение буквенной части (если она есть)
        return parts1[1].compareTo(parts2[1]);
    }

    // Вспомогательный метод для разделения строки на числовую и буквенную часть
    private String[] splitSectionNumber(String sectionNumber) {
        // Используем регулярное выражение, чтобы разделить строку на цифры и буквы
        String numericPart = sectionNumber.replaceAll("[^0-9]", ""); // Оставляем только цифры
        String letterPart = sectionNumber.replaceAll("[0-9]", "");   // Оставляем только буквы (если есть)

        return new String[] { numericPart, letterPart.isEmpty() ? "" : letterPart };
    }
}
