package com.drgproject.repair.controller;

import com.drgproject.repair.dto.RepDepotDTO;
import com.drgproject.repair.dto.RegionDTO;
import com.drgproject.repair.service.RepDepotService;
import com.drgproject.repair.service.RegionService;
import jakarta.servlet.http.HttpSession;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Controller
@RequestMapping("/repair-depots")
public class RepDepotController {

    private final RepDepotService repDepotService;
    private final RegionService regionService;

    public RepDepotController(RepDepotService repDepotService, RegionService regionService) {
        this.repDepotService = repDepotService;
        this.regionService = regionService;
    }

    // Главная страница депо ремонта
    @GetMapping
    public String getRepairDepotsPage() {
        return "repair_depot_1_main";  // Название шаблона главной страницы
    }

    // Страница с списком всех депо ремонта
    @GetMapping("/all")
    public String getAllRepairDepotsPage(Model model, HttpSession session) {
        String post = (String) session.getAttribute("post");

        if ("Администратор".equals(post)) {
            // Для администратора получаем список всех регионов и передаем в модель
            List<String> regions = regionService.getAllRegions().stream()
                    .map(RegionDTO::getName)
                    .toList();
            model.addAttribute("regions", regions);
            model.addAttribute("depots", Collections.emptyList()); // Изначально список депо пуст
            model.addAttribute("isAdmin", true); // Указываем, что это администратор
        } else if ("Регионал".equals(post)) {
            // Для регионала отображаем депо только для его региона
            String userRegion = (String) session.getAttribute("region");
            List<RepDepotDTO> depots = repDepotService.getDepotsByRegionName(userRegion);
            model.addAttribute("depots", depots);
            model.addAttribute("isAdmin", false); // Указываем, что это не администратор
        }

        return "repair_depot_2_list"; // Название шаблона со списком депо
    }

    // Получить депо ремонта по ID (REST)
    @GetMapping("/{id}")
    public ResponseEntity<RepDepotDTO> getRepairDepotById(@PathVariable Long id) {
        RepDepotDTO repDepotDTO = repDepotService.getRepairDepotById(id); // Используем метод сервиса для поиска по ID
        if (repDepotDTO != null) {
            return ResponseEntity.ok(repDepotDTO); // Если объект найден, возвращаем его
        } else {
            return ResponseEntity.notFound().build(); // Если объект не найден, возвращаем 404
        }
    }

    // Страница для создания нового депо ремонта (форма)
    @GetMapping("/create")
    public String showCreateDepotForm(Model model, HttpSession session) {
        String post = (String) session.getAttribute("post");

        if ("Администратор".equals(post)) {
            // Для администратора получаем список всех регионов и передаем в модель
            List<String> regions = regionService.getAllRegions().stream()
                    .map(RegionDTO::getName)
                    .toList();
            model.addAttribute("regions", regions);  // Добавляем список регионов для выбора
            model.addAttribute("repairDepot", new RepDepotDTO());
            model.addAttribute("isAdmin", true); // Флаг для различения роли администратора
        } else if ("Регионал".equals(post)) {
            // Для регионала автоматически выбираем его регион из сессии
            String userRegion = (String) session.getAttribute("region");
            RepDepotDTO repDepotDTO = new RepDepotDTO();
            repDepotDTO.setRegionName(userRegion); // Устанавливаем регион для регионала

            model.addAttribute("repairDepot", repDepotDTO);
            model.addAttribute("isAdmin", false); // Флаг для различения роли регионала
        }

        return "repair_depot_3_add"; // Название шаблона для формы добавления
    }


    // Обработчик создания нового депо ремонта
    @PostMapping("/create")
    public String createRepairDepot(@ModelAttribute RepDepotDTO repDepotDTO, Model model) {
        RepDepotDTO createdRepairDepot = repDepotService.addRepairDepot(repDepotDTO.getName(), repDepotDTO.getRegionName());
        if (createdRepairDepot != null) {
            model.addAttribute("createdRepairDepot", createdRepairDepot);
            model.addAttribute("regionName", createdRepairDepot.getRegionName());
            return "repair_depot_4_add_success"; // Шаблон для успешного добавления
        } else {
            model.addAttribute("errorMessage", "Ошибка при создании депо");
            return "repair_depot_3_add"; // Вернуться к форме в случае ошибки
        }
    }

    // Страница для редактирования депо ремонта (форма)
    @GetMapping("/edit")
    public String showEditDepotForm(@RequestParam long id, Model model, HttpSession session) {
        String post = (String) session.getAttribute("post");
        RepDepotDTO repDepotDTO = repDepotService.getRepairDepotById(id);

        if (repDepotDTO != null) {
            if ("Администратор".equals(post)) {
                // Для администратора получаем список всех регионов
                List<String> regions = regionService.getAllRegions().stream()
                        .map(RegionDTO::getName)
                        .toList();
                model.addAttribute("regions", regions);
                model.addAttribute("isAdmin", true); // Указываем, что это администратор
            } else if ("Регионал".equals(post)) {
                // Для регионала отображаем его регион
                String userRegion = (String) session.getAttribute("region");
                repDepotDTO.setRegionName(userRegion); // Устанавливаем регион для регионала
                model.addAttribute("isAdmin", false); // Указываем, что это не администратор
            }

            model.addAttribute("repairDepot", repDepotDTO);
            return "repair_depot_5_edit"; // Шаблон формы редактирования
        } else {
            model.addAttribute("errorMessage", "Депо с таким ID не найдено");
            return "repair_depot_5_edit"; // Вернуться к форме в случае ошибки
        }
    }

    // Обработчик обновления депо ремонта
    @PostMapping("/edit/{id}")
    public String updateRepairDepot(@PathVariable long id, @ModelAttribute RepDepotDTO repDepotDTO, Model model) {
        RepDepotDTO updatedRepairDepot = repDepotService.updateRepairDepot(id, repDepotDTO.getName(), repDepotDTO.getRegionName());
        if (updatedRepairDepot != null) {
            model.addAttribute("updatedRepairDepot", updatedRepairDepot);
            return "repair_depot_6_edit_success"; // Шаблон для успешного редактирования
        } else {
            model.addAttribute("errorMessage", "Не удалось обновить данные депо");
            return "repair_depot_5_edit"; // Вернуться к форме в случае ошибки
        }
    }

    // Страница для удаления депо ремонта (форма)
    @GetMapping("/delete")
    public String showDeleteDepotForm(@RequestParam long id, @RequestParam String name, Model model) {
        model.addAttribute("id", id);
        model.addAttribute("name", name);
        return "repair_depot_7_delete"; // Шаблон формы удаления
    }

    // Обработчик удаления депо ремонта
    @PostMapping("/deleteById")
    public String deleteRepairDepot(@RequestParam long id, Model model) {
        try {
            repDepotService.deleteRepairDepot(id);
            model.addAttribute("successMessage", "Депо успешно удалено");
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", "Ошибка при удалении депо: " + e.getMessage());
        }
        return "repair_depot_7_delete"; // Вернуться к форме удаления
    }


    // Метод для удаления депо по имени и региону
    @PostMapping("/delete")
    public String deleteDepotByNameAndRegion(@RequestParam String name, @RequestParam String region, Model model) {
        try {
            repDepotService.deleteDepotByNameAndRegion(name, region);
            model.addAttribute("successMessage", "Депо успешно удалено.");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Ошибка при удалении депо: " + e.getMessage());
        }

        // Перенаправляем обратно на страницу со списком депо
        return "redirect:/repair-depots/all";
    }

    //Список депо по имени Региона
    @GetMapping("/region")
    @ResponseBody
    public List<RepDepotDTO> getDepotsByRegion(@RequestParam String region) {
        return repDepotService.getDepotsByRegionName(region);
    }

    @GetMapping("/upload-rep_depots")
    public String showUploadForm(Model model) {
        return "repair_depot_7_upload"; // Возвращаем название шаблона Thymeleaf
    }

    @PostMapping("/upload-rep_depot")
    public String uploadRepairDepotExcelFile(@RequestParam("fileExcel") MultipartFile fileExcel, Model model) {

        try {
            // Проверяем, что файл не пуст
            if (fileExcel.isEmpty()) {
                model.addAttribute("message", "Пожалуйста, выберите файл для загрузки");
                return "repair_depot_7_upload"; // Возвращаемся к форме загрузки депо ремонта
            }

            StringBuilder message = new StringBuilder(); // Для вывода всех сообщений

            // Открываем файл с помощью Apache POI
            try (Workbook workbook = new XSSFWorkbook(fileExcel.getInputStream())) {
                Sheet sheet = workbook.getSheetAt(0); // Получаем первый лист

                // Проходим по строкам начиная с 2-й строки (индекс 1)
                for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                    Row row = sheet.getRow(rowIndex);

                    if (row != null) {
                        // Чтение данных из ячеек (колонка 0 - homeRegion, колонка 1 - repDepot)
                        String homeRegion = getCellValueAsString(row.getCell(0));
                        String repDepot = getCellValueAsString(row.getCell(1));

                        // Проверяем, если все ключевые ячейки пусты, прекращаем чтение
                        if (isRowEmpty(homeRegion) || isRowEmpty(repDepot)) {
                            continue; // Пропускаем пустую строку
                        }

                        // Проверка на существование региона
                        boolean regionExist = regionService.existByName(homeRegion);
                        if (!regionExist) {
                            // Если регион не найден, выводим сообщение и пропускаем
                            message.append("Регион ").append(homeRegion).append(" не найден. Пропускаем депо ").append(repDepot).append(".<br/>");
                            continue;
                        }

                        // Проверка на существование депо ремонта в регионе
                        boolean depotExists = repDepotService.existsByDepotAndRegion(repDepot, homeRegion);
                        if (depotExists) {
                            // Если депо ремонта уже существует в данном регионе, пропускаем его
                            message.append("Депо ремонта ").append(repDepot).append(" уже существует в регионе ").append(homeRegion).append(". Пропускаем.<br/>");
                            continue;
                        }

                        // Создаем новое депо ремонта
                        // Сохраняем депо ремонта
                        repDepotService.addRepairDepotFromFile(repDepot, homeRegion);

                        // Добавляем сообщение о создании нового депо ремонта
                        message.append("Депо ремонта ").append(repDepot).append(" успешно добавлено в регион ").append(homeRegion).append(".<br/>");
                    }
                }

                model.addAttribute("message", message.toString()); // Отправляем все сообщения на фронт
            }
        } catch (Exception e) {
            model.addAttribute("message", "Ошибка при обработке файла: " + e.getMessage());
            e.printStackTrace();
        }

        return "repair_depot_7_upload"; // Возвращаемся к той же форме загрузки с результатами
    }

    // Метод для безопасного чтения значений из ячейки
    private String getCellValueAsString(Cell cell) {
        return getString(cell);
    }

    // Проверяем, пустая ли строка (все ключевые значения пустые)
    private boolean isRowEmpty(String homeRegion) {
        return homeRegion.isEmpty();
    }
}
