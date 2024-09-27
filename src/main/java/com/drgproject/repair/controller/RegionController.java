package com.drgproject.repair.controller;

import com.drgproject.repair.dto.LocoInfoDTO;
import com.drgproject.repair.dto.RegionDTO;
import com.drgproject.repair.service.RegionService;
import jakarta.servlet.http.HttpSession;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Controller
@RequestMapping("/regions")
public class RegionController {

    private final RegionService regionService;

    public RegionController(RegionService regionService) {
        this.regionService = regionService;
    }

    @GetMapping
    public String getRegionsPage() {
        return "region_1_main";
    }

    @GetMapping("/all")
    public String getAllRegionsPage(Model model, HttpSession session) {
        String post = (String) session.getAttribute("post");
        List<RegionDTO> regions = Collections.emptyList();
        if ("Администратор".equals(post)) {
            regions = regionService.getAllRegions();
        }
        model.addAttribute("regions", regions);
        return "region_2_list";
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegionDTO> getRegionById(@PathVariable Long id) {
        RegionDTO regionDTO = regionService.getRegionById(id);
        if (regionDTO != null) {
            return ResponseEntity.ok(regionDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    public String showSearchPage() {
        return "region_3_search";
    }

    @GetMapping("/byName")
    public String getRegionByName(@RequestParam String name, Model model, HttpSession session) {
        String post = (String) session.getAttribute("post");
        RegionDTO region = regionService.getRegionByName(name);

        if (region != null && "Администратор".equals(post)) {
            model.addAttribute("region", region);
        } else {
            model.addAttribute("errorMessage", "Регион с таким именем не найден");
        }
        return "region_3_search";
    }

    @GetMapping("/create")
    public String showCreateRegionForm(Model model) {
        model.addAttribute("region", new RegionDTO());
        return "region_4_add";
    }

    @PostMapping("/create")
    public String createRegion(@ModelAttribute RegionDTO regionDTO, Model model) {
        RegionDTO createdRegion = regionService.createRegion(regionDTO);
        model.addAttribute("createdRegion", createdRegion);
        return "region_4_add_success";
    }

    @GetMapping("/edit")
    public String showEditRegionForm(@RequestParam long id, Model model) {
        RegionDTO regionDTO = regionService.getRegionById(id);
        if (regionDTO != null) {
            model.addAttribute("region", regionDTO);
        } else {
            model.addAttribute("errorMessage", "Регион с таким ID не найден");
        }
        return "region_5_update";
    }

    @PostMapping("/edit/{id}")
    public String updateRegion(@PathVariable long id, @ModelAttribute RegionDTO regionDTO, Model model) {
        RegionDTO updatedRegion = regionService.updateRegion(id, regionDTO);
        if (updatedRegion != null) {
            model.addAttribute("updatedRegion", updatedRegion);
            return "region_5_update_success";
        } else {
            model.addAttribute("errorMessage", "Не удалось обновить данные региона");
            return "region_5_update";
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRegion(@PathVariable long id) {
        regionService.deleteRegion(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/delete")
    public String showDeleteRegionForm(Model model, HttpSession session) {
        String post = (String) session.getAttribute("post");
        model.addAttribute("post", post);
        return "region_6_delete";
    }

    @PostMapping("/deleteByName")
    public String deleteRegionByName(@RequestParam String name, Model model) {
        try {
            regionService.deleteRegionByName(name);
            model.addAttribute("successMessage", "Регион успешно удален");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Ошибка при удалении региона");
        }
        return "region_6_delete";
    }

    // Метод GET для отображения формы загрузки файла
    @GetMapping("/upload-regions")
    public String showUploadForm(Model model) {
        return "region_7_upload"; // Возвращаем название шаблона Thymeleaf
    }

    //Загрузка дорог из Excel
    @PostMapping("/upload-region")
    public String uploadExcelFile(@RequestParam("fileExcel") MultipartFile fileExcel, Model model) {

        try {
            // Проверяем, что файл не пуст
            if (fileExcel.isEmpty()) {
                model.addAttribute("message", "Пожалуйста, выберите файл для загрузки");
                return "region_7_upload"; // Возвращаемся к форме загрузки
            }

            StringBuilder message = new StringBuilder(); // Для вывода всех сообщений

            // Открываем файл с помощью Apache POI
            try (Workbook workbook = new XSSFWorkbook(fileExcel.getInputStream())) {
                Sheet sheet = workbook.getSheetAt(0); // Получаем первый лист

                // Проходим по строкам начиная с 2-й строки (индекс 1)
                for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                    Row row = sheet.getRow(rowIndex);

                    if (row != null) {
                        // Чтение данных из ячеек начиная с A2, B2 и т.д.
                        String homeRegion = getCellValueAsString(row.getCell(0));
                        // Проверяем, если все ключевые ячейки пусты, прекращаем чтение
                        if (isRowEmpty(homeRegion)) {
                            break; // Останавливаем обработку, так как встретили пустую строку
                        }

                        // Проверка на существование дороги
                        boolean regionExists = regionService.existByName(homeRegion);
                        if (regionExists) {
                            // Добавляем сообщение о пропуске уже существующей дороги
                            message.append("Дорога ").append(homeRegion).append(" уже присутствует. Пропускаем.<br/>");
                            continue; // Пропускаем создание этой дороги и переходим к следующей
                        }

                        // Создаем новую дорогу
                        RegionDTO regionDTO = new RegionDTO();
                        regionDTO.setName(homeRegion);
                        regionService.createRegion(regionDTO);

                        // Добавляем сообщение о создании новой дороги
                        message.append("Дорога ").append(homeRegion).append(" успешно добавлена.<br/>");
                    }
                }

                model.addAttribute("message", message.toString());
            }
        } catch (Exception e) {
            model.addAttribute("message", "Ошибка при обработке файла: " + e.getMessage());
            e.printStackTrace();
        }
        return "region_7_upload"; // возвращаем ту же форму с сообщением
    }

    // Метод для безопасного чтения значений из ячейки
    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return ""; // Возвращаем пустую строку, если ячейка пустая
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf((int) cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }

    // Проверяем, пустая ли строка (все ключевые значения пустые)
    private boolean isRowEmpty(String homeRegion) {
        return homeRegion.isEmpty();
    }
}
