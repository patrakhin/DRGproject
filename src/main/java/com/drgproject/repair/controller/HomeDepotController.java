package com.drgproject.repair.controller;

import com.drgproject.repair.dto.HomeDepotDTO;
import com.drgproject.repair.dto.RegionDTO;
import com.drgproject.repair.service.HomeDepotService;
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
@RequestMapping("/home-depots")
public class HomeDepotController {

    private final HomeDepotService homeDepotService;
    private final RegionService regionService;

    public HomeDepotController(HomeDepotService homeDepotService, RegionService regionService) {
        this.homeDepotService = homeDepotService;
        this.regionService = regionService;
    }

    @GetMapping
    public String getHomeDepotsPage() {
        return "home_depot_1_main";
    }

    @GetMapping("/all")
    public String getAllHomeDepotsPage(Model model, HttpSession session) {
        String post = (String) session.getAttribute("post");
        List<HomeDepotDTO> depots = Collections.emptyList();
        if ("Администратор".equals(post) || "Регионал".equals(post)) {
            depots = homeDepotService.getAllHomeDepots();
        }
        model.addAttribute("depots", depots);
        return "home_depot_2_list";
    }

    @GetMapping("/{id}")
    public ResponseEntity<HomeDepotDTO> getHomeDepotById(@PathVariable Long id) {
        HomeDepotDTO homeDepotDTO = homeDepotService.getHomeDepotById(id);
        if (homeDepotDTO != null) {
            return ResponseEntity.ok(homeDepotDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/region/{regionId}")
    public ResponseEntity<List<HomeDepotDTO>> getDepotsByRegionId(@PathVariable Long regionId) {
        List<HomeDepotDTO> depots = homeDepotService.getDepotsByRegionId(regionId);
        return ResponseEntity.ok(depots);
    }

    @GetMapping("/create")
    public String showCreateDepotForm(Model model) {
        List<RegionDTO> regions = regionService.getAllRegions();
        model.addAttribute("homeDepot", new HomeDepotDTO());
        model.addAttribute("regions", regions);
        return "home_depot_3_add";
    }

    @PostMapping("/create")
    public String createHomeDepot(@ModelAttribute HomeDepotDTO homeDepotDTO, Model model) {
        HomeDepotDTO createdHomeDepot = homeDepotService.createHomeDepot(homeDepotDTO);
        String regionName = homeDepotService.getRegionNameByDepotId(createdHomeDepot.getId());
        Long id = regionService.getRegionByName(regionName).getId();
        createdHomeDepot.setRegionId(id);
        createdHomeDepot.setRegionName(regionName); // Добавление имени региона в DTO
        if (createdHomeDepot != null) {
            model.addAttribute("createdHomeDepot", createdHomeDepot);
            model.addAttribute("regionName", regionName); // Добавление имени региона в модель
            return "home_depot_3_add_success";
        } else {
            model.addAttribute("errorMessage", "Ошибка при создании депо");
            return "home_depot_3_add";
        }
    }

    @GetMapping("/edit")
    public String showEditDepotForm(@RequestParam long id, Model model) {
        HomeDepotDTO homeDepotDTO = homeDepotService.getHomeDepotById(id);
        List<RegionDTO> regions = regionService.getAllRegions();
        if (homeDepotDTO != null) {
            model.addAttribute("homeDepot", homeDepotDTO);
            model.addAttribute("regions", regions);
            return "home_depot_4_update";
        } else {
            model.addAttribute("errorMessage", "Депо с таким ID не найдено");
            return "home_depot_4_update";
        }
    }

    @PostMapping("/edit/{id}")
    public String updateHomeDepot(@PathVariable long id, @ModelAttribute HomeDepotDTO homeDepotDTO, Model model) {
        HomeDepotDTO updatedHomeDepot = homeDepotService.updateHomeDepot(id, homeDepotDTO);
        if (updatedHomeDepot != null) {
            model.addAttribute("updatedHomeDepot", updatedHomeDepot);
            return "home_depot_4_update_success";
        } else {
            model.addAttribute("errorMessage", "Не удалось обновить данные депо");
            return "home_depot_4_update";
        }
    }

    @GetMapping("/delete")
    public String showDeleteDepotForm(Model model) {
        return "home_depot_5_delete";
    }

    @PostMapping("/deleteById")
    public String deleteHomeDepot(@RequestParam long id, Model model) {
        boolean isDeleted = homeDepotService.deleteHomeDepot(id);
        if (isDeleted) {
            model.addAttribute("successMessage", "Депо успешно удалено");
        } else {
            model.addAttribute("errorMessage", "Ошибка при удалении депо");
        }
        return "home_depot_5_delete";
    }

    // Метод GET для отображения формы загрузки файла
    @GetMapping("/upload-home_depots")
    public String showUploadForm(Model model) {
        return "home_depot_6_upload"; // Возвращаем название шаблона Thymeleaf
    }

    //Загрузка дорог из Excel
    @PostMapping("/upload-home-depot")
    public String uploadDepotExcelFile(@RequestParam("fileExcel") MultipartFile fileExcel, Model model) {

        try {
            // Проверяем, что файл не пуст
            if (fileExcel.isEmpty()) {
                model.addAttribute("message", "Пожалуйста, выберите файл для загрузки");
                return "home_depot_6_upload"; // Возвращаемся к форме загрузки депо
            }

            StringBuilder message = new StringBuilder(); // Для вывода всех сообщений

            // Открываем файл с помощью Apache POI
            try (Workbook workbook = new XSSFWorkbook(fileExcel.getInputStream())) {
                Sheet sheet = workbook.getSheetAt(0); // Получаем первый лист

                // Проходим по строкам начиная с 2-й строки (индекс 1)
                for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                    Row row = sheet.getRow(rowIndex);

                    if (row != null) {
                        // Чтение данных из ячеек (колонка 0 - homeRegion, колонка 1 - homeDepot)
                        String homeRegion = getCellValueAsString(row.getCell(0));
                        String homeDepot = getCellValueAsString(row.getCell(1));

                        // Проверяем, если все ключевые ячейки пусты, прекращаем чтение
                        if (isRowEmpty(homeRegion) || isRowEmpty(homeDepot)) {
                            continue; // Пропускаем пустую строку
                        }

                        // Проверка на существование региона
                        RegionDTO region = regionService.getRegionByName(homeRegion);
                        if (region == null) {
                            // Если регион не найден, выводим сообщение и пропускаем
                            message.append("Регион ").append(homeRegion).append(" не найден. Пропускаем депо ").append(homeDepot).append(".<br/>");
                            continue;
                        }

                        // Проверка на существование депо в регионе
                        boolean depotExists = homeDepotService.existsByDepotAndRegionId(homeDepot, region.getId());
                        if (depotExists) {
                            // Если депо уже существует в данном регионе, пропускаем его
                            message.append("Депо ").append(homeDepot).append(" уже существует в регионе ").append(homeRegion).append(". Пропускаем.<br/>");
                            continue;
                        }

                        // Создаем новое депо
                        HomeDepotDTO homeDepotDTO = new HomeDepotDTO();
                        homeDepotDTO.setDepot(homeDepot);
                        homeDepotDTO.setRegionId(region.getId());
                        homeDepotDTO.setRegionName(region.getName());

                        // Сохраняем депо
                        homeDepotService.createHomeDepot(homeDepotDTO);

                        // Добавляем сообщение о создании нового депо
                        message.append("Депо ").append(homeDepot).append(" успешно добавлено в регион ").append(homeRegion).append(".<br/>");
                    }
                }

                model.addAttribute("message", message.toString()); // Отправляем все сообщения на фронт
            }
        } catch (Exception e) {
            model.addAttribute("message", "Ошибка при обработке файла: " + e.getMessage());
            e.printStackTrace();
        }

        return "home_depot_6_upload"; // Возвращаемся к той же форме загрузки с результатами
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
