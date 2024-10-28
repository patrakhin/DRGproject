package com.drgproject.completed_work_reports;

import org.springframework.stereotype.Controller;
import com.drgproject.repair.dto.*;
import com.drgproject.repair.service.*;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Controller
@RequestMapping("/completed_work")
public class CompletedWorkController {

    private static final String REPAIRED_LOCOS = "repairedLocos";
    private static final String TIMES_NEW_ROMAN = "Times New Roman";
    private static final String APPLICATION_VND = "application/vnd.ms-excel";
    private static final String CONTENT_DIS = "Content-Disposition";
    private static final String REP_LOC = "Repaired Locos";
    private final RepairHistoryService repairHistoryService;
    private final RegionService regionService;

    public CompletedWorkController(RepairHistoryService repairHistoryService,
                                   RegionService regionService) {
        this.repairHistoryService = repairHistoryService;
        this.regionService = regionService;
    }

    // Главная страница "Отчеты выполненных работ"
    @GetMapping("/reports")
    public String showReportsSelection() {
        return "repair_history_13_reports_selection";
    }

    //Формирование отчета - Общий отчет по сети дорог
    @GetMapping("/repaired-locos")
    public String getRepairedLocos(Model model) {
        List<RepairedLocoDTO> repairedLocos = repairHistoryService.getRepairedLocos();
        model.addAttribute(REPAIRED_LOCOS, repairedLocos);
        return "repair_history_11_repaired-locos";
    }

    // Метод для генерации отчета - Сформировать отчет
    @GetMapping("/generate-report")
    public void generateReport(HttpServletResponse response) throws IOException {
        List<RepairedLocoDTO> repairedLocos = repairHistoryService.getRepairedLocos();

        response.setContentType(APPLICATION_VND);
        response.setHeader(CONTENT_DIS, "attachment; filename=repaired-locos-report-basic.xlsx");

        Workbook workbook;
        workbook = new XSSFWorkbook();
        Sheet sheet;
        sheet = workbook.createSheet(REP_LOC);

        // Создание стилей
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(createFont(workbook, TIMES_NEW_ROMAN, (short) 8, true));
        headerStyle.setAlignment(HorizontalAlignment.CENTER); // Центрирование текста
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER); // Центрирование текста по вертикали
        headerStyle.setWrapText(true); // Перенос текста в заголовках

        CellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setFont(createFont(workbook, TIMES_NEW_ROMAN, (short) 14, true));
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        CellStyle dateStyle = workbook.createCellStyle();
        dateStyle.setFont(createFont(workbook, TIMES_NEW_ROMAN, (short) 10, true));
        dateStyle.setAlignment(HorizontalAlignment.LEFT);
        dateStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        CellStyle textStyle = workbook.createCellStyle();
        textStyle.setFont(createFont(workbook, TIMES_NEW_ROMAN, (short) 7, false));
        textStyle.setAlignment(HorizontalAlignment.CENTER);
        textStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        textStyle.setWrapText(true); // Перенос текста в данных

        // Добавление заголовка и даты
        int rowNum = 0;
        Row titleRow = sheet.createRow(rowNum++);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("Отчет выполненных работ ООО \"ДРГ-Сервис\" №____");
        titleCell.setCellStyle(titleStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 8)); // Объединение ячеек для заголовка

        // Установка высоты строки для заголовка
        titleRow.setHeightInPoints(14.25f);

        Row dateRow = sheet.createRow(rowNum++);
        Cell dateCell = dateRow.createCell(0);
        String currentDate = new SimpleDateFormat("dd.MM.yyyy").format(new Date());
        dateCell.setCellValue("Дата: " + currentDate);
        dateCell.setCellStyle(dateStyle);
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 8)); // Объединение ячеек для даты

        // Установка высоты строки для даты
        dateRow.setHeightInPoints(14.25f);

        // Создание строки заголовков
        Row headerRow = sheet.createRow(rowNum++);
        headerRow.setHeightInPoints(34.5f); // Высота строки заголовков

        // Заголовки
        String[] headers = {"№ п/п", "Дата ремонта", "Депо приписки", "Тип системы", "Серия локомотива", "Номер локомотива",
                "Наименование работ", "Количество выполненных работ", "Фамилия исполнителя"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Установка ширины столбцов
        sheet.setColumnWidth(0, 6 * 256); // Ширина столбца для "№ п/п"
        sheet.setColumnWidth(1, 15 * 256); // Ширина столбца для "Дата ремонта"
        sheet.setColumnWidth(2, 15 * 256); // Ширина столбца для "Депо приписки"
        sheet.setColumnWidth(3, 15 * 256); // Ширина столбца для "Тип системы"
        sheet.setColumnWidth(4, 15 * 256); // Ширина столбца для "Серия локомотива"
        sheet.setColumnWidth(5, 15 * 256); // Ширина столбца для "Номер локомотива"
        sheet.setColumnWidth(6, 15 * 256); // Ширина столбца для "Наименование работ"
        sheet.setColumnWidth(7, (int)(9.43 * 256)); // Ширина столбца для "Количество выполненных работ"
        sheet.setColumnWidth(8, 20 * 256); // Ширина столбца для "Фамилия исполнителя"

        // Заполнение данными
        int index = 1; // Порядковый номер
        for (RepairedLocoDTO loco : repairedLocos) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(index++); // Порядковый номер
            row.createCell(1).setCellValue(loco.getRepairDate().toString()); // Преобразование LocalDate в строку
            row.createCell(2).setCellValue(loco.getHomeDepot());
            row.createCell(3).setCellValue(loco.getTypeSystem());
            row.createCell(4).setCellValue(loco.getTypeLoco());
            row.createCell(5).setCellValue(loco.getLocoUnit());
            row.createCell(6).setCellValue(loco.getPositionRepair());
            row.createCell(7).setCellValue(loco.getWorkCount()); // Значение workCount можно оставить числом
            row.createCell(8).setCellValue(loco.getEmployee().toString()); // Метод getEmployee() уже возвращает строку

            // Применение стиля к ячейкам с данными
            for (int i = 0; i < 9; i++) {
                row.getCell(i).setCellStyle(textStyle);
            }
        }

        // Запись данных в поток ответа
        workbook.write(response.getOutputStream());
        workbook.close();
    }

    // Метод для создания стиля шрифта
    private Font createFont(Workbook workbook, String fontName, short fontSize, boolean bold) {
        Font font = workbook.createFont();
        font.setFontName(fontName);
        font.setFontHeightInPoints(fontSize);
        font.setBold(bold);
        return font;
    }

    // Отчет по депо ремонта СПСТ/без СПСТ
    @GetMapping("/select-parameters")
    public String showSelectParametersForm(Model model) {
        List<RegionDTO> regions = regionService.getAllRegions();
        model.addAttribute("regions", regions);
        model.addAttribute("depots", new ArrayList<HomeDepotDTO>());
        return "repair_history_12_select_parameters";
    }

    @GetMapping("/filter-report")
    public String filterReport(@RequestParam String region, @RequestParam String depot, @RequestParam(required = false) boolean includeSPS, Model model) {
        List<RepairedLocoDTO> repairedLocos;
        if (includeSPS) {
            repairedLocos = repairHistoryService.getRepairedLocosWithTypeSPS(depot);
        } else {
            repairedLocos = repairHistoryService.getRepairedLocosWithoutTypeSPS(depot);
        }
        model.addAttribute(REPAIRED_LOCOS, repairedLocos);
        model.addAttribute("depotName", depot); // Добавляем наименование депо в модель
        return "repair_history_11_repaired-locos";
    }

    // Выбор параметров для Отчет по депо ремонта СПСТ/без СПСТ
    @GetMapping("/filter-report-sps")
    public String filterReportSps(@RequestParam String region, @RequestParam String depot, @RequestParam(required = false) boolean includeSPS, Model model) {
        List<RepairedLocoDTO> repairedLocos;
        if (includeSPS) {
            repairedLocos = repairHistoryService.getRepairedLocosWithTypeSPS(depot);
        } else {
            repairedLocos = repairHistoryService.getRepairedLocosWithoutTypeSPS(depot);
        }
        model.addAttribute(REPAIRED_LOCOS, repairedLocos);
        model.addAttribute("depotName", depot);
        model.addAttribute("includeSPS", includeSPS); // Добавляем параметр includeSPS в модель
        return "repair_history_14_spst";
    }

    @GetMapping("/generate-report-sps")
    public void generateReportSps(
            @RequestParam String depot,
            @RequestParam(required = false) String includeSPS,
            HttpServletResponse response) throws IOException {

        boolean includeSPSFlag;

        // Обработка значений "да" и "нет"
        if ("да".equalsIgnoreCase(includeSPS)) {
            includeSPSFlag = true;
        } else if ("нет".equalsIgnoreCase(includeSPS)) {
            includeSPSFlag = false;
        } else {
            includeSPSFlag = false; // значение по умолчанию, если параметр отсутствует или некорректен
        }

        List<RepairedLocoDTO> repairedLocos;
        if (includeSPSFlag) {
            repairedLocos = repairHistoryService.getRepairedLocosWithTypeSPS(depot);
        } else {
            repairedLocos = repairHistoryService.getRepairedLocosWithoutTypeSPS(depot);
        }

        response.setContentType(APPLICATION_VND);
        response.setHeader(CONTENT_DIS, "attachment; filename=repaired-locos-report-with-sps.xlsx");

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(REP_LOC);

        // Создание стилей
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(createFont(workbook, TIMES_NEW_ROMAN, (short) 8, true));
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerStyle.setWrapText(true);

        CellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setFont(createFont(workbook, TIMES_NEW_ROMAN, (short) 14, true));
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        CellStyle dateStyle = workbook.createCellStyle();
        dateStyle.setFont(createFont(workbook, TIMES_NEW_ROMAN, (short) 10, true));
        dateStyle.setAlignment(HorizontalAlignment.LEFT);
        dateStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        CellStyle textStyle = workbook.createCellStyle();
        textStyle.setFont(createFont(workbook, TIMES_NEW_ROMAN, (short) 7, false));
        textStyle.setAlignment(HorizontalAlignment.CENTER);
        textStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        textStyle.setWrapText(true);

        // Добавление заголовка и даты
        int rowNum = 0;
        Row titleRow = sheet.createRow(rowNum++);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("Отчет выполненных работ ООО \"ДРГ-Сервис\" №____");
        titleCell.setCellStyle(titleStyle);

        // Устанавливаем ширину первой ячейки для заголовка
        sheet.setColumnWidth(0, 20 * 256);

        titleRow.setHeightInPoints(14.25f);

        Row dateRow = sheet.createRow(rowNum++);
        Cell dateCell = dateRow.createCell(0);
        String currentDate = new SimpleDateFormat("dd.MM.yyyy").format(new Date());
        dateCell.setCellValue("Дата: " + currentDate);
        dateCell.setCellStyle(dateStyle);

        // Добавление ячейки для депо (вместо объединения ячеек, увеличиваем ширину)
        Cell depotCell = dateRow.createCell(8);
        depotCell.setCellValue("Депо ремонта: " + depot);
        depotCell.setCellStyle(dateStyle);

        // Устанавливаем ширину столбца под depot
        sheet.setColumnWidth(8, 30 * 256);

        dateRow.setHeightInPoints(14.25f);

        // Создание строки заголовков
        Row headerRow = sheet.createRow(rowNum++);
        headerRow.setHeightInPoints(34.5f);

        String[] headers = {"№ п/п", "Дата ремонта", "Депо приписки", "Тип системы", "Серия локомотива", "Номер локомотива",
                "Наименование работ", "Количество выполненных работ", "Фамилия исполнителя"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        sheet.setColumnWidth(0, 6 * 256);
        sheet.setColumnWidth(1, 15 * 256);
        sheet.setColumnWidth(2, 15 * 256);
        sheet.setColumnWidth(3, 15 * 256);
        sheet.setColumnWidth(4, 15 * 256);
        sheet.setColumnWidth(5, 15 * 256);
        sheet.setColumnWidth(6, 15 * 256);
        sheet.setColumnWidth(7, (int) (9.43 * 256));
        sheet.setColumnWidth(8, 20 * 256);

        int index = 1;
        for (RepairedLocoDTO loco : repairedLocos) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(index++);
            row.createCell(1).setCellValue(loco.getRepairDate().toString());
            row.createCell(2).setCellValue(loco.getHomeDepot());
            row.createCell(3).setCellValue(loco.getTypeSystem());
            row.createCell(4).setCellValue(loco.getTypeLoco());
            row.createCell(5).setCellValue(loco.getLocoUnit());
            row.createCell(6).setCellValue(loco.getPositionRepair());
            row.createCell(7).setCellValue(loco.getWorkCount());
            row.createCell(8).setCellValue(loco.getEmployee().toString());

            for (int i = 0; i < 9; i++) {
                row.getCell(i).setCellStyle(textStyle);
            }
        }

        workbook.write(response.getOutputStream());
        workbook.close();
    }

    // Метод для отображения формы выбора депо и дат  СПСТ по дате и депо - Отчет по депо ремонта СПСТ на дату
    @GetMapping("/filt-report-sps")
    public String showFilterForm(Model model) {
        List<RegionDTO> regions = regionService.getAllRegions();
        model.addAttribute("regions", regions);
        model.addAttribute("depots", new ArrayList<RepDepotDTO>());
        return "repair_history_15_filter-report-sps"; // Имя HTML-формы
    }

    // Метод для обработки запроса и отображения результата СПСТ по дате и депо
    @GetMapping("/gen-report-sps")
    public String generateReportSps(
            @RequestParam String depot,
            @RequestParam String startDate,
            @RequestParam String endDate,
            Model model) {

        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        List<RepairedLocoDTO> repairedLocos = repairHistoryService.getRepairedLocosWithTypeSPS(depot, start, end);
        // Сортировка списка по дате ремонта по возрастанию
        repairedLocos.sort(Comparator.comparing(RepairedLocoDTO::getRepairDate));

        model.addAttribute(REPAIRED_LOCOS, repairedLocos);
        model.addAttribute("depotName", depot);
        model.addAttribute("startDate", startDate);  // Добавляем начальную дату в модель
        model.addAttribute("endDate", endDate);      // Добавляем конечную дату в модель
        return "repair_history_16_report-sps-result";
    }

    // Генерация отчета по СПСТ по дате и депо ремонта
    @GetMapping("/gen-report")
    public void genReport(
            @RequestParam String depot,
            @RequestParam String startDate,
            @RequestParam String endDate,
            HttpServletResponse response) throws IOException {

        // Преобразование строковых дат в LocalDate
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        // Получение данных на основе переданных параметров
        List<RepairedLocoDTO> repairedLocos = repairHistoryService.getRepairedLocosWithTypeSPS(depot, start, end);

        // Сортировка списка по дате ремонта по возрастанию
        repairedLocos.sort(Comparator.comparing(RepairedLocoDTO::getRepairDate));

        // Настройка ответа для скачивания файла
        response.setContentType(APPLICATION_VND);
        response.setHeader(CONTENT_DIS, "attachment; filename=repaired-locos-report-sps-date.xlsx");

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(REP_LOC);

        // Создание стилей
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(createFont(workbook, TIMES_NEW_ROMAN, (short) 8, true));
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerStyle.setWrapText(true);

        CellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setFont(createFont(workbook, TIMES_NEW_ROMAN, (short) 14, true));
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        CellStyle dateStyle = workbook.createCellStyle();
        dateStyle.setFont(createFont(workbook, TIMES_NEW_ROMAN, (short) 10, true));
        dateStyle.setAlignment(HorizontalAlignment.LEFT);
        dateStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        CellStyle textStyle = workbook.createCellStyle();
        textStyle.setFont(createFont(workbook, TIMES_NEW_ROMAN, (short) 7, false));
        textStyle.setAlignment(HorizontalAlignment.CENTER);
        textStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        textStyle.setWrapText(true);

        // Добавление заголовка
        int rowNum = 0;
        Row titleRow = sheet.createRow(rowNum++);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("Отчет выполненных работ ООО \"ДРГ-Сервис\" №____");
        titleCell.setCellStyle(titleStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 8));
        titleRow.setHeightInPoints(14.25f);

        // Добавление строки с датой и депо
        Row dateRow = sheet.createRow(rowNum++);
        dateRow.setHeightInPoints(14.25f);

        // Ячейка с датой слева
        Cell dateCell = dateRow.createCell(0);
        String currentDate = new SimpleDateFormat("dd.MM.yyyy").format(new Date());
        dateCell.setCellValue("Дата: " + currentDate);
        dateCell.setCellStyle(dateStyle);
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 5)); // Объединение ячеек для даты

        // Ячейка с названием депо справа
        Cell depotCell = dateRow.createCell(6);
        depotCell.setCellValue("депо ремонта: " + depot);
        depotCell.setCellStyle(dateStyle);
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 6, 8)); // Объединение ячеек для депо

        // Создание строки заголовков таблицы
        Row headerRow = sheet.createRow(rowNum++);
        headerRow.setHeightInPoints(34.5f);

        // Заголовки
        String[] headers = {"№ п/п", "Дата ремонта", "Депо приписки", "Тип системы", "Серия локомотива", "Номер локомотива",
                "Наименование работ", "Количество выполненных работ", "Фамилия исполнителя"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Установка ширины столбцов
        sheet.setColumnWidth(0, 6 * 256);
        sheet.setColumnWidth(1, 15 * 256);
        sheet.setColumnWidth(2, 15 * 256);
        sheet.setColumnWidth(3, 15 * 256);
        sheet.setColumnWidth(4, 15 * 256);
        sheet.setColumnWidth(5, 15 * 256);
        sheet.setColumnWidth(6, 15 * 256);
        sheet.setColumnWidth(7, (int)(9.43 * 256));
        sheet.setColumnWidth(8, 20 * 256);

        // Заполнение данными
        int index = 1;
        for (RepairedLocoDTO loco : repairedLocos) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(index++);
            row.createCell(1).setCellValue(loco.getRepairDate().toString());
            row.createCell(2).setCellValue(loco.getHomeDepot());
            row.createCell(3).setCellValue(loco.getTypeSystem());
            row.createCell(4).setCellValue(loco.getTypeLoco());
            row.createCell(5).setCellValue(loco.getLocoUnit());
            row.createCell(6).setCellValue(loco.getPositionRepair());
            row.createCell(7).setCellValue(loco.getWorkCount());
            row.createCell(8).setCellValue(loco.getEmployee().toString());

            for (int i = 0; i < 9; i++) {
                row.getCell(i).setCellStyle(textStyle);
            }
        }

        // Запись данных в поток ответа
        workbook.write(response.getOutputStream());
        workbook.close();
    }

    // Метод для отображения формы выбора депо и дат  БЕЗ СПСТ по дате и депо - Отчет по депо ремонта для других систем на дату
    @GetMapping("/filt-rep-sps")
    public String showFilterFormWithout(Model model) {
        List<RegionDTO> regions = regionService.getAllRegions();
        model.addAttribute("regions", regions);
        model.addAttribute("depots", new ArrayList<RepDepotDTO>());
        return "repair_history_17_filter-report-sps"; // Имя HTML-формы
    }

    // Метод для обработки запроса и отображения результата  БЕЗ СПСТ по дате и депо
    @GetMapping("/gen-rep-sps")
    public String generateReportWithoutSps(
            @RequestParam String depot,
            @RequestParam String startDate,
            @RequestParam String endDate,
            Model model) {

        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        List<RepairedLocoDTO> repairedLocos = repairHistoryService.getRepairedLocosWithoutSPS(depot, start, end);
        // Сортировка списка по дате ремонта по возрастанию
        repairedLocos.sort(Comparator.comparing(RepairedLocoDTO::getRepairDate));

        model.addAttribute(REPAIRED_LOCOS, repairedLocos);
        model.addAttribute("depotName", depot);
        model.addAttribute("startDate", startDate);  // Добавляем начальную дату в модель
        model.addAttribute("endDate", endDate);      // Добавляем конечную дату в модель
        return "repair_history_18_report-sps-result";
    }

    // Генерация отчета по СПСТ по дате и депо ремонта
    @GetMapping("/gen-rep")
    public void genReportWithout(
            @RequestParam String depot,
            @RequestParam String startDate,
            @RequestParam String endDate,
            HttpServletResponse response) throws IOException {

        // Преобразование строковых дат в LocalDate
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        // Получение данных на основе переданных параметров
        List<RepairedLocoDTO> repairedLocos = repairHistoryService.getRepairedLocosWithoutSPS(depot, start, end);

        // Сортировка списка по дате ремонта по возрастанию
        repairedLocos.sort(Comparator.comparing(RepairedLocoDTO::getRepairDate));

        // Настройка ответа для скачивания файла
        response.setContentType(APPLICATION_VND);
        response.setHeader(CONTENT_DIS, "attachment; filename=repaired-locos-report-sps-without.xlsx");

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet;
        sheet = workbook.createSheet(REP_LOC);

        // Создание стилей
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(createFont(workbook, TIMES_NEW_ROMAN, (short) 8, true));
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerStyle.setWrapText(true);

        CellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setFont(createFont(workbook, TIMES_NEW_ROMAN, (short) 14, true));
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        CellStyle dateStyle = workbook.createCellStyle();
        dateStyle.setFont(createFont(workbook, TIMES_NEW_ROMAN, (short) 10, true));
        dateStyle.setAlignment(HorizontalAlignment.LEFT);
        dateStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        CellStyle textStyle = workbook.createCellStyle();
        textStyle.setFont(createFont(workbook, TIMES_NEW_ROMAN, (short) 7, false));
        textStyle.setAlignment(HorizontalAlignment.CENTER);
        textStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        textStyle.setWrapText(true);

        // Добавление заголовка
        int rowNum = 0;
        Row titleRow = sheet.createRow(rowNum++);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("Отчет выполненных работ ООО \"ДРГ-Сервис\" №____");
        titleCell.setCellStyle(titleStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 8));
        titleRow.setHeightInPoints(14.25f);

        // Добавление строки с датой и депо
        Row dateRow = sheet.createRow(rowNum++);
        dateRow.setHeightInPoints(14.25f);

        // Ячейка с датой слева
        Cell dateCell = dateRow.createCell(0);
        String currentDate = new SimpleDateFormat("dd.MM.yyyy").format(new Date());
        dateCell.setCellValue("Дата: " + currentDate);
        dateCell.setCellStyle(dateStyle);
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 5)); // Объединение ячеек для даты

        // Ячейка с названием депо справа
        Cell depotCell = dateRow.createCell(6);
        depotCell.setCellValue("депо ремонта: " + depot);
        depotCell.setCellStyle(dateStyle);
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 6, 8)); // Объединение ячеек для депо

        // Создание строки заголовков таблицы
        Row headerRow = sheet.createRow(rowNum++);
        headerRow.setHeightInPoints(34.5f);

        // Заголовки
        String[] headers = {"№ п/п", "Дата ремонта", "Депо приписки", "Тип системы", "Серия локомотива", "Номер локомотива",
                "Наименование работ", "Количество выполненных работ", "Фамилия исполнителя"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Установка ширины столбцов
        sheet.setColumnWidth(0, 6 * 256);
        sheet.setColumnWidth(1, 15 * 256);
        sheet.setColumnWidth(2, 15 * 256);
        sheet.setColumnWidth(3, 15 * 256);
        sheet.setColumnWidth(4, 15 * 256);
        sheet.setColumnWidth(5, 15 * 256);
        sheet.setColumnWidth(6, 15 * 256);
        sheet.setColumnWidth(7, (int)(9.43 * 256));
        sheet.setColumnWidth(8, 20 * 256);

        // Заполнение данными
        int index = 1;
        for (RepairedLocoDTO loco : repairedLocos) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(index++);
            row.createCell(1).setCellValue(loco.getRepairDate().toString());
            row.createCell(2).setCellValue(loco.getHomeDepot());
            row.createCell(3).setCellValue(loco.getTypeSystem());
            row.createCell(4).setCellValue(loco.getTypeLoco());
            row.createCell(5).setCellValue(loco.getLocoUnit());
            row.createCell(6).setCellValue(loco.getPositionRepair());
            row.createCell(7).setCellValue(loco.getWorkCount());
            row.createCell(8).setCellValue(loco.getEmployee().toString());

            for (int i = 0; i < 9; i++) {
                row.getCell(i).setCellStyle(textStyle);
            }
        }
        // Запись данных в поток ответа
        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
