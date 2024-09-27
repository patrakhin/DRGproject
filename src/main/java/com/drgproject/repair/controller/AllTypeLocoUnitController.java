package com.drgproject.repair.controller;

import com.drgproject.repair.dto.AllTypeLocoUnitDTO;
import com.drgproject.repair.dto.TypeLocoDTO;
import com.drgproject.repair.service.AllTypeLocoUnitService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Controller
@RequestMapping("/all-type-unit")
public class AllTypeLocoUnitController {
    private final AllTypeLocoUnitService allTypeLocoUnitService;

    public AllTypeLocoUnitController(AllTypeLocoUnitService allTypeLocoUnitService){
        this.allTypeLocoUnitService = allTypeLocoUnitService;
    }

    @GetMapping
    public String getAllTypeLoco(Model model) {
        List<AllTypeLocoUnitDTO> allTypeLocoUnitDTOS = allTypeLocoUnitService.getAllTypeLocoUnit();
        model.addAttribute("allTypeLoco", allTypeLocoUnitDTOS);
        return "all_type_loco_1_main";
    }

    @GetMapping("/create")
    public String showCreateAllTypeLoco(Model model) {
        model.addAttribute("allTypeLoco", new AllTypeLocoUnitDTO());
        return "all_type_loco_2_add";
    }

    @PostMapping("/create")
    public String createSystemName(@ModelAttribute AllTypeLocoUnitDTO allTypeLocoUnitDTO, Model model) {
        AllTypeLocoUnitDTO createdTypeLoco = allTypeLocoUnitService.createAllTypeLocoUnit(allTypeLocoUnitDTO);
        model.addAttribute("createdTypeLoco", createdTypeLoco);
        return "all_type_loco_2_add_success";
    }

    @GetMapping("/edit")
    public String showEditTypeLocoForm(@RequestParam long id, Model model) {
        AllTypeLocoUnitDTO allTypeLocoUnitDTO = allTypeLocoUnitService.getAllTypeLocoUnitById(id);
        if (allTypeLocoUnitDTO != null) {
            model.addAttribute("allTypeLocoUnitDTO", allTypeLocoUnitDTO);
        } else {
            model.addAttribute("errorMessage", "Серия локомотива с таким ID не найдена");
        }
        return "all_type_loco_3_update";
    }

    @PostMapping("/edit/{id}")
    public String updateTypeLoco(@PathVariable long id, @ModelAttribute AllTypeLocoUnitDTO allTypeLoco, Model model) {
        AllTypeLocoUnitDTO allTypeLocoUnit = allTypeLocoUnitService.updateAllTypeLocoUnit(id, allTypeLoco);
        if (allTypeLocoUnit != null) {
            model.addAttribute("allTypeLocoUnit", allTypeLocoUnit);
            return "all_type_loco_3_update_success";
        } else {
            model.addAttribute("errorMessage", "Не удалось обновить серию локомотива");
            return "all_type_loco_3_update";
        }
    }

    @GetMapping("/delete")
    public String showDeleteTypeLoco(Model model) {
        return "all_type_loco_4_delete";
    }

    @PostMapping("/deleteById")
    public String deleteSystemName(@RequestParam long id, Model model) {
        boolean isDeleted = allTypeLocoUnitService.deleteAllTypeLocoUnit(id);
        if (isDeleted) {
            model.addAttribute("successMessage", "Серия успешно удалена");
        } else {
            model.addAttribute("errorMessage", "Ошибка при удалении серии локомотива");
        }
        return "all_type_loco_4_delete";
    }

    // Метод GET для отображения формы загрузки файла
    @GetMapping("/upload-type-locoUnits")
    public String showUploadForm(Model model) {
        return "all_type_loco_5_upload"; // Возвращаем название шаблона Thymeleaf
    }

    //Загрузка дорог из Excel
    @PostMapping("/upload-type-locoUnit")
    public String uploadExcelFile(@RequestParam("fileExcel") MultipartFile fileExcel, Model model) {

        try {
            // Проверяем, что файл не пуст
            if (fileExcel.isEmpty()) {
                model.addAttribute("message", "Пожалуйста, выберите файл для загрузки");
                return "all_type_loco_5_upload"; // Возвращаемся к форме загрузки
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
                        String typeLocoUnit = getCellValueAsString(row.getCell(0));
                        // Проверяем, если все ключевые ячейки пусты, прекращаем чтение
                        if (isRowEmpty(typeLocoUnit)) {
                            break; // Останавливаем обработку, так как встретили пустую строку
                        }

                        // Проверка на существование дороги
                        boolean typeSLocoUnitExists = allTypeLocoUnitService.existTypeLocoUnit(typeLocoUnit);
                        if (typeSLocoUnitExists) {
                            // Добавляем сообщение о пропуске уже существующей дороги
                            message.append("Серия локомотива").append(typeLocoUnit).append(" уже присутствует. Пропускаем.<br/>");
                            continue; // Пропускаем создание этой дороги и переходим к следующей
                        }

                        // Создаем новую серию ТПС
                        AllTypeLocoUnitDTO allTypeLocoUnitDTO = new AllTypeLocoUnitDTO();
                        allTypeLocoUnitDTO.setTypeLocoUnit(typeLocoUnit);
                        allTypeLocoUnitService.createAllTypeLocoUnit(allTypeLocoUnitDTO);

                        // Добавляем сообщение о создании новой серии
                        message.append("Серия локомотива ").append(typeLocoUnit).append(" успешно добавлена.<br/>");
                    }
                }

                model.addAttribute("message", message.toString());
            }
        } catch (Exception e) {
            model.addAttribute("message", "Ошибка при обработке файла: " + e.getMessage());
            e.printStackTrace();
        }
        return "all_type_loco_5_upload"; // возвращаем ту же форму с сообщением
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
    private boolean isRowEmpty(String typeLocoUnit) {
        return typeLocoUnit.isEmpty();
    }
}
