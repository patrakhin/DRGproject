package com.drgproject.repair.controller;

import com.drgproject.repair.dto.AllTypeLocoUnitDTO;
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
    public static final String ERROR_MESSAGE = "errorMessage";
    public static final String ALL_TYPE_LOCO_5_UPLOAD = "all_type_loco_5_upload";
    public static final String MESSAGE = "message";
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
            model.addAttribute(ERROR_MESSAGE, "Серия локомотива с таким ID не найдена");
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
            model.addAttribute(ERROR_MESSAGE, "Не удалось обновить серию локомотива");
            return "all_type_loco_3_update";
        }
    }

    @GetMapping("/delete")
    public String showDeleteTypeLoco() {
        return "all_type_loco_4_delete";
    }

    @PostMapping("/deleteById")
    public String deleteSystemName(@RequestParam long id, Model model) {
        boolean isDeleted = allTypeLocoUnitService.deleteAllTypeLocoUnit(id);
        if (isDeleted) {
            model.addAttribute("successMessage", "Серия успешно удалена");
        } else {
            model.addAttribute(ERROR_MESSAGE, "Ошибка при удалении серии локомотива");
        }
        return "all_type_loco_4_delete";
    }

    // Метод GET для отображения формы загрузки файла
    @GetMapping("/upload-type-locoUnits")
    public String showUploadForm() {
        return ALL_TYPE_LOCO_5_UPLOAD; // Возвращаем название шаблона Thymeleaf
    }

    //Загрузка дорог из Excel
    @PostMapping("/upload-type-locoUnit")
    public String uploadExcelFile(@RequestParam("fileExcel") MultipartFile fileExcel, Model model) {

        try {
            // Проверяем, что файл не пуст
            if (fileExcel.isEmpty()) {
                model.addAttribute(MESSAGE, "Пожалуйста, выберите файл для загрузки");
                return ALL_TYPE_LOCO_5_UPLOAD; // Возвращаемся к форме загрузки
            }

            StringBuilder message = new StringBuilder(); // Для вывода всех сообщений
            // Открываем файл с помощью Apache POI
            try (Workbook workbook = new XSSFWorkbook(fileExcel.getInputStream())) {
                Sheet sheet = workbook.getSheetAt(0); // Получаем первый лист

                // Проходим по строкам начиная с 2-й строки (индекс 1)
                for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                    Row row = sheet.getRow(rowIndex);

                    // Проверяем, что строка не пустая и ячейка не пуста
                    String typeLocoUnit = (row != null) ? getCellValueAsString(row.getCell(0)) : null;

                    if (row != null && !isRowEmpty(typeLocoUnit)) {
                        // Проверка на существование серии локомотива
                        boolean typeSLocoUnitExists = allTypeLocoUnitService.existTypeLocoUnit(typeLocoUnit);
                        if (typeSLocoUnitExists) {
                            // Добавляем сообщение о пропуске уже существующей серии
                            message.append("Серия локомотива ").append(typeLocoUnit).append(" уже присутствует. Пропускаем.<br/>");
                            continue; // Пропускаем создание этой серии и переходим к следующей строке
                        }
                        // Создаем новую серию ТПС
                        AllTypeLocoUnitDTO allTypeLocoUnitDTO = new AllTypeLocoUnitDTO();
                        allTypeLocoUnitDTO.setTypeLocoUnit(typeLocoUnit);
                        allTypeLocoUnitService.createAllTypeLocoUnit(allTypeLocoUnitDTO);
                        // Добавляем сообщение о создании новой серии
                        message.append("Серия локомотива ").append(typeLocoUnit).append(" успешно добавлена.<br/>");
                    }
                }

                model.addAttribute(MESSAGE, message.toString());
            }
        } catch (Exception e) {
            model.addAttribute(MESSAGE, "Ошибка при обработке файла: " + e.getMessage());
        }
        return ALL_TYPE_LOCO_5_UPLOAD; // возвращаем ту же форму с сообщением
    }

    // Метод для безопасного чтения значений из ячейки
    private String getCellValueAsString(Cell cell) {
        return getString(cell);
    }

    static String getString(Cell cell) {
        return HomeDepotController.getString(cell);
    }

    // Проверяем, пустая ли строка (все ключевые значения пустые)
    private boolean isRowEmpty(String typeLocoUnit) {
        return typeLocoUnit.isEmpty();
    }
}
