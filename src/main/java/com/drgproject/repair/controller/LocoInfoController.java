package com.drgproject.repair.controller;

import com.drgproject.repair.dto.*;
import com.drgproject.repair.entity.LocoInfo;
import com.drgproject.repair.service.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.sql.JDBCType.BOOLEAN;
import static java.sql.JDBCType.NUMERIC;
import static javax.management.openmbean.SimpleType.STRING;
import static org.apache.poi.ss.formula.DataValidationEvaluator.ValidationEnum.FORMULA;

@Controller
@RequestMapping("/loco_info")
public class LocoInfoController {

    private final LocoInfoService locoInfoService;
    private final TypeLocoService typeLocoService;
    private final RegionService regionService;
    private final HomeDepotService homeDepotService;
    private final BlockOnLocoService blockOnLocoService;
    private final LocoListService locoListService;
    private final LocoFilterService locoFilterService;
    private final AllTypeLocoUnitService allTypeLocoUnitService;


    public LocoInfoController(LocoInfoService locoInfoService,
                              TypeLocoService typeLocoService,
                              RegionService regionService,
                              HomeDepotService homeDepotService,
                              BlockOnLocoService blockOnLocoService,
                              LocoListService locoListService,
                              LocoFilterService locoFilterService,
                              AllTypeLocoUnitService allTypeLocoUnitService) {
        this.locoInfoService = locoInfoService;
        this.typeLocoService = typeLocoService;
        this.regionService = regionService;
        this.homeDepotService = homeDepotService;
        this.blockOnLocoService = blockOnLocoService;
        this.locoListService = locoListService;
        this.locoFilterService = locoFilterService;
        this.allTypeLocoUnitService = allTypeLocoUnitService;
    }

    @GetMapping("/manage")
    public String managePanel(){
        return "loco_info_1_manage";
    }

    @GetMapping("/create")
    public String showCreateLocoInfoForm( @RequestParam(required = false) String successMessage,
                                          @RequestParam(required = false) String errorMessage,
                                          Model model) {
        List<TypeLocoDTO> typeLocos = typeLocoService.getAllTypeLocos();
        model.addAttribute("locoInfo", new LocoInfoDTO());
        model.addAttribute("typeLocos", typeLocos);
        // Получение всех регионов и добавление в модель
        List<RegionDTO> regions = regionService.getAllRegions();
        model.addAttribute("regions",regions);

        // Получение списка свободных секций и добавление в модель
        /*List<LocoListDTO> allSections = locoListService.getAllLocoLists();
        List<LocoFilterDTO> allSectionsIntoLoco = locoFilterService.getAllLocoFilters();
        List<String> freeSections = locoListService.getSortedFreeSections(allSections, allSectionsIntoLoco);
        model.addAttribute("freeSections",freeSections);*/

        // Добавление параметров в модель, если они присутствуют
        if (successMessage != null) {
            model.addAttribute("successMessage", successMessage);
        }
        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
        }

        return "loco_info_2_create";
    }

    @GetMapping("/search")
    public String locoInfoSearch(Model model){
        //Получение серий локомотива и добавление в модель
        List<AllTypeLocoUnitDTO> typeLocos= allTypeLocoUnitService.getAllTypeLocoUnit();// List<TypeLocoDTO> typeLocos = typeLocoService.getAllTypeLocos();
        model.addAttribute("typeLocos", typeLocos);
        // Получение всех регионов и добавление в модель
        List<RegionDTO> regions = regionService.getAllRegions();
        model.addAttribute("regions",regions);
        // Пустой список депо, так как они будут загружаться динамически
        model.addAttribute("depots", new ArrayList<>());
        return "loco_info_4_search";
    }

    @GetMapping("/search/results")
    public String searchLocoInfo(@RequestParam("region") String region,
                                 @RequestParam("locoType") String locoType,
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size,
                                 Model model) {
        // Проверка валидности значения страницы
        if (page < 0) {
            page = 0; // Устанавливаем минимально допустимое значение
        }
        model.addAttribute("region", region);
        model.addAttribute("locoType", locoType);
        try {
            Page<LocoInfoDTO> locoInfoPage = locoInfoService.getLocoInfoByRegionAndType(region, locoType, page, size);
            model.addAttribute("locoInfoPage", locoInfoPage);
            return "loco_info_5_search_result_region";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "loco_info_5_search_result_region";
        }
    }

    @GetMapping("/search/by-loco-number")
    public String searchByLocoNumberPage(Model model) {
        // Получаем список регионов для отображения на странице
        List<RegionDTO> regions = regionService.getAllRegions();
        model.addAttribute("regions", regions);

        // Получение всех депо и добавление в модель
        List<HomeDepotDTO> homeDepots = homeDepotService.getAllHomeDepots();
        model.addAttribute("homeDepots",homeDepots);
        return "loco_info_4_search_by_number";
    }

    @GetMapping("/search/by-loco-number/results")
    public String searchByLocoNumberResults(@RequestParam("region") String region,
                                            @RequestParam("homeDepot") String homeDepot,
                                            @RequestParam("locoNumber") String locoNumber,
                                            Model model) {
        try {
            // Выполняем поиск локомотива по номеру
            String clearLocoNumber = locoInfoService.convertToCyrillic(locoNumber);
            LocoInfoDTO locoInfo = locoInfoService.getLocoInfoByRegionAndHomeDepotAndLocoNumber(region, homeDepot, clearLocoNumber);

            // Если локомотив найден, добавляем информацию в модель
            model.addAttribute("locoInfo", locoInfo);
        } catch (Exception e) {
            // Если произошла ошибка или локомотив не найден
            model.addAttribute("errorMessage", "Локомотив не найден или произошла ошибка.");
        }

        return "loco_info_5_search_results";
    }

    //Детальные сведения по каждой секции локомотива
    @GetMapping("/locos/details/{id}")
    public String getLocoDetails(@PathVariable Long id, Model model) {
        // Получаем данные локомотива
        LocoInfoDTO locoInfoDTO = locoInfoService.getLocoInfoById(id);
        //Получаем общие данные по секции
        LocoListDTO overviewLoco = locoListService.getLocoListByNumberLoco(locoInfoDTO.getLocoSection1());
        // Получаем список секций и блоков
        List<String> locoSectionNumbers = locoInfoService.getLocoSections(locoInfoDTO);
        String typeLoco = locoInfoService.getTypeLocoListFromLoco(locoInfoDTO.getLocoType());

        // Создаем список секций с блоками
        List<SectionWithBlocksDTO> sections = new ArrayList<>();
        for (String sectionNumber : locoSectionNumbers) {
            if (sectionNumber != null && !sectionNumber.isEmpty() && !sectionNumber.equalsIgnoreCase("нет")) {
                List<BlockOnLocoDTO> blocksOnSection = blockOnLocoService.getAllBlockOnLocoByLocoNumberAndTypeLoco(sectionNumber, typeLoco);
                sections.add(new SectionWithBlocksDTO(sectionNumber, blocksOnSection));
            }
        }

        // Добавляем данные в модель
        model.addAttribute("overviewLoco", overviewLoco);
        model.addAttribute("locoInfoDTO", locoInfoDTO);
        model.addAttribute("sections", sections);

        return "loco_info_6_loco_details";
    }

    @PostMapping("/create/tps")
    public String createLocoInfo(
            @RequestParam(required = false) String region,
            @RequestParam(required = false) String homeDepot,
            @RequestParam(required = false) String typeLoco,
            @RequestParam(required = false) String section1,
            @RequestParam(defaultValue = "false") boolean section1Status,
            @RequestParam(required = false) String section2,
            @RequestParam(defaultValue = "false") boolean section2Status,
            @RequestParam(required = false) String section3,
            @RequestParam(defaultValue = "false") boolean section3Status,
            @RequestParam(required = false) String section4,
            @RequestParam(defaultValue = "false") boolean section4Status,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        List<TypeLocoDTO> typeLocos = typeLocoService.getAllTypeLocos();
        model.addAttribute("typeLocos", typeLocos);

        // Подготовка к проверке на занятость секций
        if (section1.equals("Выберите секцию")){
            section1 = "";
        }
        String sectionLoco1 = section1;
        if (section2.equals("Выберите секцию")){
            section2 = "";
        }
        String sectionLoco2 = section2;
        if (section3.equals("Выберите секцию")){
            section3 = "";
        }
        String sectionLoco3 = section3;
        if (section4.equals("Выберите секцию")){
            section4 = "";
        }
        String sectionLoco4 = section4;

        List<String> sectionsNumber = new ArrayList<>();
        sectionsNumber.add(sectionLoco1);
        sectionsNumber.add(sectionLoco2);
        sectionsNumber.add(sectionLoco3);
        sectionsNumber.add(sectionLoco4);
        //Получаем только те секции которые имеют номер
        List<String> clearNumbers = locoInfoService.getClearLocoSections(sectionsNumber);

        int countSections = clearNumbers.size();
        String typeLocoUnit = locoInfoService.getTypeLocoFromTypeSection(typeLoco, countSections);
        // Проверка на существование локомотива
        boolean locoExists = locoInfoService.ifLociUnitIsExists(region, homeDepot, typeLocoUnit, section1);

        if (locoExists) {
            redirectAttributes.addFlashAttribute("errorMessage", "Локомотив с такими данными уже существует.");
            return "redirect:/loco_info/create"; // Редирект на ту же страницу
        }

        // Проверка на существование секций
        for (String section : clearNumbers) {
            boolean sectionExists = locoFilterService.ifSectionIsExist(region, homeDepot, typeLoco, section);
            if (!sectionExists) {
                redirectAttributes.addFlashAttribute("errorMessage", "Секция номер " + section + " не существует в свободных.");
                return "redirect:/loco_info/create"; // Редирект на ту же страницу
            }
        }
        // Проверка на свободность секций
        for(String section : clearNumbers){
            boolean sectionFree = locoFilterService.ifSectionIsFree(region, homeDepot, typeLoco, section);
            if (!sectionFree) {
                redirectAttributes.addFlashAttribute("errorMessage", "Секция номер " + section + " не свободна.");
                return "redirect:/loco_info/create"; // Редирект на ту же страницу
            }
        }
        try {
            // Заполнение недостающих полей строкой "нет" и установка статуса оборудования
            LocoInfoDTO locoInfoDTO = fillMissingFieldsWithDefault(
                    region, homeDepot, typeLocoUnit, section1, section1Status,
                    section2, section2Status, section3, section3Status,
                    section4, section4Status
            );

            // Делаем секции занятыми
            for(String section : clearNumbers){
                locoFilterService.updateSectionNonFree(region, homeDepot, typeLoco, section);
            }
            // Создание локомотива
            locoInfoService.createLocoInfo(locoInfoDTO);

            model.addAttribute("locoInfo", new LocoInfoDTO()); // Очищаем форму
            model.addAttribute("successMessage", "Локомотив успешно создан");

        } catch (Exception e) {
            model.addAttribute("errorMessage", "Ошибка при создании локомотива: " + e.getMessage());
        }
        return "loco_info_2_create";
    }

    @GetMapping("/delete")
    public String showDeleteDepotForm(Model model) {
        List<AllTypeLocoUnitDTO> typeLocos = allTypeLocoUnitService.getAllTypeLocoUnit(); // было List<TypeLocoDTO> typeLocos = typeLocoService.getAllTypeLocos();
        List<RegionDTO> regions = regionService.getAllRegions();
        model.addAttribute("typeLocos", typeLocos);
        model.addAttribute("regions", regions);
        List<HomeDepotDTO> homeDepots = new ArrayList<>();
        return "loco_info_3_delete";
    }

    @PostMapping("/deleteByName")
    public String deleteHomeDepot(@RequestParam String homeDepot, @RequestParam String locoUnit, @RequestParam String locoType, Model model) {
        //String clearLocoUnit = locoInfoService.c
        LocoInfoDTO locoForCreateSection = locoInfoService.getLocoInfoByHomeDepotAndLocoTypeAndLocoUnit(homeDepot, locoType, locoUnit);
        String homeRegion = locoForCreateSection.getRegion();
        //String homeDepot = locoForCreateSection.getHomeDepot();
        List<String> sectionsNumber = locoInfoService.getLocoSections(locoForCreateSection);
        List<String> clearSectionNumber = locoInfoService.convertAllSectionsToCyrillic(sectionsNumber);
        String locoTypeForSection = locoInfoService.getTypeLocoListFromLoco(locoType);
        boolean isDeleted = locoInfoService.deleteLocoInfoByLocoUnit(locoUnit, locoType);
        if (isDeleted) {
            // Для каждой секции в списке sectionsNumber делаем ее свободной
            for (String sectionNumber : clearSectionNumber) {
                locoFilterService.freeSectionAfterDistMist(homeRegion, homeDepot, locoTypeForSection, sectionNumber);
            }
            model.addAttribute("successMessage", "Локомотив успешно расформирован, секции освободились");
        } else {
            model.addAttribute("errorMessage", "Ошибка при расформировании локомотива");
        }
        return "loco_info_3_delete";
    }

    // Метод для отображения страницы удаления локомотива по ID
    @GetMapping("/deleteById")
    public String showDeleteLocoInfoPage() {
        return "loco_info_3_delete_by_id";
    }

    // Метод для обработки удаления локомотива по ID
    @PostMapping("/deleteById")
    public String deleteLocoInfoById(@RequestParam Long id, Model model) {

        LocoInfoDTO locoForCreateSection = locoInfoService.getLocoInfoById(id);
        String homeRegion = locoForCreateSection.getRegion();
        String homeDepot = locoForCreateSection.getHomeDepot();
        String locoType = locoForCreateSection.getLocoType();
        List<String> sectionsNumber = locoInfoService.getLocoSections(locoForCreateSection);
        String locoTypeForSection = locoInfoService.getTypeLocoListFromLoco(locoType);

        // Для каждой секции в списке sectionsNumber делаем ее свободной
        for (String sectionNumber : sectionsNumber) {
            locoFilterService.freeSectionAfterDistMist(homeRegion, homeDepot, locoTypeForSection, sectionNumber);
        }

        try {
            locoInfoService.deleteLocoInfo(id);
            model.addAttribute("successMessage", "Локомотив с ID " + id + " успешно расформирован, секции освободились.");
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "loco_info_3_delete_by_id";
    }

    public LocoInfoDTO fillMissingFieldsWithDefault(
            String region,
            String homeDepot,
            String locoType,
            String section1,
            boolean section1Status,
            String section2,
            boolean section2Status,
            String section3,
            boolean section3Status,
            String section4,
            boolean section4Status
    ) {
        // Создаем новый объект LocoInfoDTO
        LocoInfoDTO locoInfoDTO = new LocoInfoDTO();

        // Устанавливаем значения региона, депо и типа локомотива
        locoInfoDTO.setRegion(region);
        locoInfoDTO.setHomeDepot(homeDepot);
        locoInfoDTO.setLocoType(locoType);

        // Проверка и установка значения по умолчанию для первой секции
        locoInfoDTO.setLocoSection1(section1 != null && !section1.isEmpty() ? section1 : "нет");
        // Установка значения в LocoFit1 на основе статуса переключателя
        locoInfoDTO.setLocoFit1(section1Status ? "оборудован" : "не оборудован");

        // Проверка и установка значения по умолчанию для второй секции
        locoInfoDTO.setLocoSection2(section2 != null && !section2.isEmpty() ? section2 : "нет");
        // Установка значения в LocoFit2 на основе статуса переключателя
        locoInfoDTO.setLocoFit2(section2Status ? "оборудован" : "не оборудован");

        // Проверка и установка значения по умолчанию для третьей секции
        locoInfoDTO.setLocoSection3(section3 != null && !section3.isEmpty() ? section3 : "нет");
        // Установка значения в LocoFit3 на основе статуса переключателя
        locoInfoDTO.setLocoFit3(section3Status ? "оборудован" : "не оборудован");

        // Проверка и установка значения по умолчанию для четвертой секции
        locoInfoDTO.setLocoSection4(section4 != null && !section4.isEmpty() ? section4 : "нет");
        // Установка значения в LocoFit4 на основе статуса переключателя
        locoInfoDTO.setLocoFit4(section4Status ? "оборудован" : "не оборудован");

        // Устанавливаем значение locoUnit
        locoInfoDTO.setLocoUnit(locoInfoService.calculateLocoUnit(section1, section2, section3, section4));

        return locoInfoDTO;
    }

    // Метод GET для отображения формы загрузки файла
    @GetMapping("/upload-loco")
    public String showUploadForm(Model model) {
        return "loco_info_7_upload-loco"; // Возвращаем название шаблона Thymeleaf
    }

    //Загрузка локомотивов из Excel
    @PostMapping("/upload-loco")
    public String uploadExcelFile(@RequestParam("fileExcel") MultipartFile fileExcel, Model model) {
        String section1 = "";
        String section2 = "";
        String section3 = "";
        String section4 = "";

        try {
            // Проверяем, что файл не пуст
            if (fileExcel.isEmpty()) {
                model.addAttribute("message", "Пожалуйста, выберите файл для загрузки");
                return "locos_9_upload"; // Возвращаемся к форме загрузки
            }

            // Открываем файл с помощью Apache POI
            try (Workbook workbook = new XSSFWorkbook(fileExcel.getInputStream())) {
                Sheet sheet = workbook.getSheetAt(0); // Получаем первый лист

                // Проходим по строкам начиная с 2-й строки (индекс 1)
                for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                    Row row = sheet.getRow(rowIndex);

                    if (row != null) {
                        // Чтение данных из ячеек начиная с A2, B2 и т.д.
                        String homeRegion = getCellValueAsString(row.getCell(0));
                        String homeDepot = getCellValueAsString(row.getCell(1));
                        String typeLoco = getCellValueAsString(row.getCell(2));
                        String locoNumber = getCellValueAsString(row.getCell(3));

                        // Проверяем, если все ключевые ячейки пусты, прекращаем чтение
                        if (isRowEmpty(homeRegion, homeDepot, typeLoco, locoNumber)) {
                            break; // Останавливаем обработку, так как встретили пустую строку
                        }

                        // Оставшийся код для обработки строки
                        List<String> sectionNumbers = locoInfoService.createSections(typeLoco, locoNumber);
                        List<String> clearNumbers = locoInfoService.convertAllSectionsToCyrillic(sectionNumbers);

                        // Создание серии
                        String typeLocoForSections = locoInfoService.getTypeLocoListFromLoco(typeLoco);

                        // Проверка на существование секций
                        for (String section : clearNumbers) {
                            boolean sectionExists = locoListService.existsSectionByRegionAndDepotAndTypeAndNumber(homeRegion, homeDepot, typeLocoForSections, section);
                            if (!sectionExists) {
                                model.addAttribute("message", "Секция номер " + section + " не существует в свободных.");
                                return "loco_info_7_upload-loco"; // Редирект на ту же страницу
                            }
                        }

                        // Проверка на свободность секций
                        for(String section : clearNumbers){
                            boolean sectionFree = locoFilterService.ifSectionIsFree(homeRegion, homeDepot, typeLocoForSections, section);
                            if (!sectionFree) {
                                model.addAttribute("message", "Секция номер " + section + " не свободна. Уже в составе ТПС");
                                return "loco_info_7_upload-loco"; // Редирект на ту же страницу
                            }
                        }

                        List<String> extendedList = new ArrayList<>(clearNumbers);

                        int countSection = extendedList.size();
                        // Добавляем строку "нет", пока размер списка меньше 4
                        while (extendedList.size() < 4) {
                            extendedList.add("нет");
                        }
                        section1 = extendedList.get(0);
                        section2 = extendedList.get(1);
                        section3 = extendedList.get(2);
                        section4 = extendedList.get(3);

                        boolean section1Status = true;
                        boolean section2Status = true;
                        boolean section3Status = true;
                        boolean section4Status = true;

                        if (countSection == 1) {
                            section2Status = false;
                            section3Status = false;
                            section4Status = false;
                        }
                        if (countSection == 2) {
                            section3Status = false;
                            section4Status = false;
                        }
                        if (countSection == 3) {
                            section4Status = false;
                        }

                        LocoInfoDTO locoInfoDTO = fillMissingFieldsWithDefault(
                                homeRegion, homeDepot, typeLoco, section1, section1Status,
                                section2, section2Status, section3, section3Status,
                                section4, section4Status
                        );

                        // Делаем секции занятыми
                        for (String section : clearNumbers) {
                            locoFilterService.updateSectionNonFree(homeRegion, homeDepot, typeLocoForSections, section);
                        }

                        // Создание локомотива
                        locoInfoService.createLocoInfo(locoInfoDTO);
                    }
                }

                model.addAttribute("message", "Файл успешно загружен и обработан.");
            }
        } catch (Exception e) {
            model.addAttribute("message", "Ошибка при обработке файла: " + e.getMessage());
            e.printStackTrace();
        }

        return "loco_info_7_upload-loco"; // возвращаем ту же форму с сообщением
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
    private boolean isRowEmpty(String homeRegion, String homeDepot, String typeLoco, String locoNumber) {
        return homeRegion.isEmpty() && homeDepot.isEmpty() && typeLoco.isEmpty() && locoNumber.isEmpty();
    }

    // Новый метод для обработки AJAX-запроса
    @GetMapping("/depots")
    @ResponseBody
    public List<HomeDepotDTO> getDepotsByRegion(@RequestParam String regionName) {
        return homeDepotService.getDepotsByRegion(regionName);
    }

    // Новый метод для обработки AJAX-запроса (получаем свободные секции для конкретного типа ТПС)
    @GetMapping("/sections")
    @ResponseBody
    public List<String> getFreeSectionsByRegionAndHomeDepotAndTypeLoco(@RequestParam String homeRegion,
                                                                   @RequestParam String homeDepot,
                                                                   @RequestParam String typeLoco) {
        String freeSection = "да";
        //List<LocoListDTO> filteredSection = locoListService.getSectionByRegionAndHomeDepotAndTypeLoco(homeRegion, homeDepot, typeLoco);
        //List<LocoFilterDTO> allSectionsIntoLoco = locoFilterService.getAllLocoFilters();
        List<LocoFilterDTO> filterSectionByFree = locoFilterService.getAllFreeSect(homeRegion, homeDepot, typeLoco, freeSection);
        // Получение списка номеров секций
        return filterSectionByFree.stream()
                .map(LocoFilterDTO::getSectionNumber) // Извлечение номера секции
                .toList();
    }
}
