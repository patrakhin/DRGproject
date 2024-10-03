package com.drgproject.repair.controller;

import com.drgproject.repair.dto.RegionDTO;
import com.drgproject.repair.dto.TypeLocoDTO;
import com.drgproject.repair.service.TypeLocoService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Controller
@RequestMapping("/type-locos")
public class TypeLocoController {

    private final TypeLocoService typeLocoService;

    public TypeLocoController(TypeLocoService typeLocoService) {
        this.typeLocoService = typeLocoService;
    }

    @GetMapping
    public String getAllTypeLocos(Model model) {
        List<TypeLocoDTO> typeLocos = typeLocoService.getAllTypeLocos();
        model.addAttribute("typeLocos", typeLocos);
        return "type_loco_1_main";
    }

    @GetMapping("/create")
    public String showCreateTypeLocoForm(Model model) {
        model.addAttribute("typeLoco", new TypeLocoDTO());
        return "type_loco_2_add";
    }

    @PostMapping("/create")
    public String createTypeLoco(@ModelAttribute TypeLocoDTO typeLocoDTO, Model model) {
        TypeLocoDTO createdTypeLoco = typeLocoService.createTypeLoco(typeLocoDTO);
        model.addAttribute("createdTypeLoco", createdTypeLoco);
        return "type_loco_2_add_success";
    }

    @GetMapping("/edit")
    public String showEditTypeLocoForm(@RequestParam long id, Model model) {
        TypeLocoDTO typeLocoDTO = typeLocoService.getTypeLocoById(id);
        if (typeLocoDTO != null) {
            model.addAttribute("typeLoco", typeLocoDTO);
            return "type_loco_3_update";
        } else {
            model.addAttribute("errorMessage", "Серия локомотива с таким ID не найдена");
            return "type_loco_3_update";
        }
    }

    @PostMapping("/edit/{id}")
    public String updateTypeLoco(@PathVariable long id, @ModelAttribute TypeLocoDTO typeLocoDTO, Model model) {
        TypeLocoDTO updatedTypeLoco = typeLocoService.updateTypeLoco(id, typeLocoDTO);
        if (updatedTypeLoco != null) {
            model.addAttribute("updatedTypeLoco", updatedTypeLoco);
            return "type_loco_3_update_success";
        } else {
            model.addAttribute("errorMessage", "Не удалось обновить серию локомотива");
            return "type_loco_3_update";
        }
    }

    @GetMapping("/delete")
    public String showDeleteTypeLocoForm(Model model) {
        return "type_loco_4_delete";
    }

    @PostMapping("/deleteById")
    public String deleteTypeLoco(@RequestParam long id, Model model) {
        boolean isDeleted = typeLocoService.deleteTypeLoco(id);
        if (isDeleted) {
            model.addAttribute("successMessage", "Серия локомотива успешно удалена");
        } else {
            model.addAttribute("errorMessage", "Ошибка при удалении серии локомотива");
        }
        return "type_loco_4_delete";
    }

    // Метод GET для отображения формы загрузки файла
    @GetMapping("/upload-type-locos")
    public String showUploadForm(Model model) {
        return "type_loco_5_upload"; // Возвращаем название шаблона Thymeleaf
    }

    //Загрузка дорог из Excel
    @PostMapping("/upload-type-loco")
    public String uploadExcelFile(@RequestParam("fileExcel") MultipartFile fileExcel, Model model) {

        try {
            // Проверяем, что файл не пуст
            if (fileExcel.isEmpty()) {
                model.addAttribute("message", "Пожалуйста, выберите файл для загрузки");
                return "type_loco_5_upload"; // Возвращаемся к форме загрузки
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
                        String typeSection = getCellValueAsString(row.getCell(0));
                        // Проверяем, если все ключевые ячейки пусты, прекращаем чтение
                        if (isRowEmpty(typeSection)) {
                            break; // Останавливаем обработку, так как встретили пустую строку
                        }

                        // Проверка на существование дороги
                        boolean typeSectionExists = typeLocoService.sectionExists(typeSection);
                        if (typeSectionExists) {
                            // Добавляем сообщение о пропуске уже существующей дороги
                            message.append("Серия ").append(typeSection).append(" уже присутствует. Пропускаем.<br/>");
                            continue; // Пропускаем создание этой дороги и переходим к следующей
                        }

                        // Создаем новую дорогу
                        TypeLocoDTO typeLocoDto = new TypeLocoDTO();
                        typeLocoDto.setLocoType(typeSection);
                        typeLocoService.createTypeLoco(typeLocoDto);


                        // Добавляем сообщение о создании новой дороги
                        message.append("Серия секции ").append(typeSection).append(" успешно добавлена.<br/>");
                    }
                }

                model.addAttribute("message", message.toString());
            }
        } catch (Exception e) {
            model.addAttribute("message", "Ошибка при обработке файла: " + e.getMessage());
            e.printStackTrace();
        }
        return "type_loco_5_upload"; // возвращаем ту же форму с сообщением
    }

    // Метод для безопасного чтения значений из ячейки
    private String getCellValueAsString(Cell cell) {
        return getString(cell);
    }

    // Проверяем, пустая ли строка (все ключевые значения пустые)
    private boolean isRowEmpty(String typeSection) {
        return typeSection.isEmpty();
    }
}
