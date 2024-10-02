package com.drgproject.repair.controller;

import com.drgproject.dto.ReceiptBlockDto;
import com.drgproject.dto.SparePartsReceiptDto;
import com.drgproject.entity.Members;
import com.drgproject.repair.dto.*;
import com.drgproject.repair.entity.LocoInfo;
import com.drgproject.repair.service.*;
import com.drgproject.repository.MemberRepository;
import com.drgproject.service.*;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.apache.poi.ss.usermodel.*;


import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.IntStream;
import java.text.SimpleDateFormat;


@CrossOrigin(origins = "*", allowedHeaders = "*")
@Controller
@RequestMapping("/repair_history")
public class RepairHistoryControllerTwo {

    private static final String TYPE_LOCO = "typeLoco";

    private final RepairHistoryService repairHistoryService;
    private final LocoListService locoListService;
    private final BlockOnLocoService blockOnLocoService;
    private final ReceiptBlockService receiptBlockService;
    private final BlockRemovalService blockRemovalService;
    private final TypeLocoService typeLocoService;
    private final SparePartsReceiptService sparePartsReceiptService;
    private final PositionRepairService positionRepairService;
    private final MemberRepository userRepository;
    private final ShipmentBlockService shipmentBlockService;
    private final UserService userService;
    private final LocoInfoService locoInfoService;
    private final HomeDepotService homeDepotService;
    private final RegionService regionService;
    private final RepDepotService repDepotService;
    private final AllTypeLocoUnitService allTypeLocoUnitService;

    public RepairHistoryControllerTwo(RepairHistoryService repairHistoryService,
                                      LocoListService locoListService,
                                      BlockOnLocoService blockOnLocoService,
                                      ReceiptBlockService receiptBlockService,
                                      BlockRemovalService blockRemovalService,
                                      TypeLocoService typeLocoService,
                                      SparePartsReceiptService sparePartsReceiptService,
                                      PositionRepairService positionRepairService,
                                      MemberRepository userRepository,
                                      ShipmentBlockService shipmentBlockService,
                                      UserService userService,
                                      LocoInfoService locoInfoService,
                                      HomeDepotService homeDepotService,
                                      RegionService regionService,
                                      RepDepotService repDepotService,
                                      AllTypeLocoUnitService allTypeLocoUnitService) {
        this.repairHistoryService = repairHistoryService;
        this.locoListService = locoListService;
        this.blockOnLocoService = blockOnLocoService;
        this.receiptBlockService = receiptBlockService;
        this.blockRemovalService = blockRemovalService;
        this.typeLocoService = typeLocoService;
        this.sparePartsReceiptService = sparePartsReceiptService;
        this.positionRepairService = positionRepairService;
        this.userRepository = userRepository;
        this.shipmentBlockService = shipmentBlockService;
        this.userService = userService;
        this.locoInfoService = locoInfoService;
        this.homeDepotService = homeDepotService;
        this.regionService = regionService;
        this.repDepotService = repDepotService;
        this.allTypeLocoUnitService = allTypeLocoUnitService;
    }

    // Главная страница
    @GetMapping("/search")
    public String showSearchForm(Model model, HttpSession session) {
        String post = (String) session.getAttribute("post");
        if ("Администратор".equals(post) || "Регионал".equals(post)) {
            model.addAttribute("showNavigationBarLink", true);
        } else {
            model.addAttribute("showNavigationBarLink", false);
        }
        String repairDepot = (String) session.getAttribute("unit");
        String numberTable = (String) session.getAttribute("number_table");
        String fullName = userService.getUserByNumberTable(numberTable).getFio();
        String shortName = repairHistoryService.convertToShortName(fullName);
        model.addAttribute("repairDepot", repairDepot);
        model.addAttribute("shortName", shortName);
        model.addAttribute("data", LocalDate.now());
        model.addAttribute("locoList", new LocoListDTO());
        List<AllTypeLocoUnitDTO> typeLocoUnit = allTypeLocoUnitService.getAllTypeLocoUnit();
        model.addAttribute("typeLocoUnit", typeLocoUnit);
        return "repair_history_1_main";
    }

    //Поиск номера локомотива по первым двум цифрам
/*    @GetMapping("/locomotives")
    @ResponseBody
    public List<String> getLocomotiveNumbers(@RequestParam("term") String term) {
        return locoListService.findNumbersByPrefix(term);
    }*/

    // Новый поиск локомотива 280824
    @GetMapping("/locomotives")
    @ResponseBody
    public List<String> getLocomotiveNumbers(@RequestParam("term") String term) {
        return locoInfoService.getFindNumbersByPrefix(term);
    }

    // Результат поиска локомотива по типу и серии
    @PostMapping("/search")
    public String searchLocoByTypeAndNumber(@RequestParam String typeLocoUnit, @RequestParam String numberLoco, Model model, HttpSession session) {
        LocoInfoDTO locoInfoDTO = locoInfoService.getLocoByNumber(numberLoco, typeLocoUnit);
        // Создание серии для секции
        String typeLoco = locoInfoService.getTypeLocoListFromLoco(typeLocoUnit);
        if (locoInfoDTO == null) {
            model.addAttribute("error", "Локомотив такой серии и номером не найден.");
            return showSearchForm(model, session);
        }

        session.setAttribute(TYPE_LOCO, typeLoco);
        session.setAttribute("numberLoco", numberLoco);

        // Получаем номера секций
        List<String> sectionsNumber = Arrays.asList(
                locoInfoDTO.getLocoSection1(),
                locoInfoDTO.getLocoSection2(),
                locoInfoDTO.getLocoSection3(),
                locoInfoDTO.getLocoSection4()
        );

        // Фильтруем номера секций, чтобы получить только те, которые не пустые
        List<String> clearNumbers = locoInfoService.getClearLocoSections(sectionsNumber);

        // Получаем DTO для каждой секции из списка clearNumbers
        List<LocoListDTO> sections = new ArrayList<>();
        for (String sectionNumber : clearNumbers) {
            LocoListDTO section = locoListService.getLocoListByNumberLocoAndTypeLoco(sectionNumber, typeLoco);
            sections.add(section);
        }

        // Добавляем список DTO в модель
        model.addAttribute("sections", sections);

        model.addAttribute(TYPE_LOCO, typeLoco);
        model.addAttribute("typeLocoUnit", typeLocoUnit);

        model.addAttribute("numberLoco", numberLoco);
        model.addAttribute("locoInfoDTO", locoInfoDTO);
        String homeDepot = locoInfoDTO.getHomeDepot();
        model.addAttribute("homeDepot", homeDepot);

        return "repair_history_2_work_bar";
    }

    // История ремонта
    @PostMapping("/repair_history")
    public String showRepairHistory(@RequestParam String sectionNumber, @RequestParam String typeLocoUnit, Model model, HttpSession session) {

        String typeLoco = (String) session.getAttribute(TYPE_LOCO);
        if(sectionNumber.isEmpty()){
            sectionNumber = (String) session.getAttribute("sectionNumber");
        }
        String numberLoco = (String) session.getAttribute("numberLoco");
        String storageName = (String) session.getAttribute("unit");
        LocoListDTO locoListDTO = locoListService.getLocoListByNumberLocoAndTypeLoco(sectionNumber, typeLoco);
        String systemType = locoListDTO.getTypeSystem(); // Получаем тип системы

        List<BlockOnLocoDTO> blockOnLocoDTOS = blockOnLocoService.getAllBlockOnLocoByLocoNumberAndTypeLoco(sectionNumber, typeLoco);
        List<ReceiptBlockDto> receiptBlockDtos = receiptBlockService.getReceiptBlocksByStorageName(storageName);
        // Получаем список историй ремонта для конкретного локомотива
        List<RepairHistoryDto> repairHistoryDtos = repairHistoryService.findByTypeLocoAndLocoNumber(typeLoco, sectionNumber);

        // Добавляем в историю ремонта только три последние записи
        List<RepairHistoryDto> lastThreeEntries = getLastThreeEntries(repairHistoryDtos);

        LocoInfoDTO locoInfoDTO = locoInfoService.getLocoByNumber(numberLoco, typeLocoUnit);
        // Добавляем список секций
        List<String> sectionsNumber = Arrays.asList(
                locoInfoDTO.getLocoSection1(),
                locoInfoDTO.getLocoSection2(),
                locoInfoDTO.getLocoSection3(),
                locoInfoDTO.getLocoSection4()
        );
        List<String> clearNumbers = locoInfoService.getClearLocoSections(sectionsNumber);

        List<LocoListDTO> sections = new ArrayList<>();
        for (String secNumber : clearNumbers) {
            LocoListDTO section = locoListService.getLocoListByNumberLocoAndTypeLoco(secNumber, typeLoco);
            sections.add(section);
        }

        model.addAttribute("lastThreeEntries", lastThreeEntries);
        model.addAttribute(TYPE_LOCO, typeLoco);
        model.addAttribute("numberLoco", sectionNumber);
        model.addAttribute("firstNumber", numberLoco);
        model.addAttribute("systemType", systemType); // Добавляем systemType в модель
        model.addAttribute("blockOnLocoDTOS", blockOnLocoDTOS);
        model.addAttribute("receiptBlockDtos", receiptBlockDtos);
        model.addAttribute("sections", sections); // Добавляем список секций
        model.addAttribute("typeLocoUnit", typeLocoUnit);
        String homeDepot = locoInfoDTO.getHomeDepot();
        model.addAttribute("homeDepot", homeDepot);
        return "repair_history_3_history_loco";
    }

    // Вспомогательный метод для получения последних трех записей
    private List<RepairHistoryDto> getLastThreeEntries(List<RepairHistoryDto> repairHistoryDtos) {
        int size = repairHistoryDtos.size();
        if (size <= 3) {
            return repairHistoryDtos;
        }
        return repairHistoryDtos.subList(size - 3, size);
    }

    // Удаление истории ремонта на текущую дату
    @PostMapping("/delete")
    public String deleteRepairHistory(@RequestParam String typeLoco,
                                      @RequestParam String numberLoco,
                                      @RequestParam String repairDate,
                                      @RequestParam String typeLocoUnit,
                                      //@RequestParam String firstNumber,
                                      Model model, HttpSession session) {
        LocalDate date = LocalDate.parse(repairDate);
        String firstNumber = locoInfoService.getLocoByFirstNumberSection(numberLoco);
        model.addAttribute("typeLocoUnit", typeLocoUnit);
        if (firstNumber == null || firstNumber.isEmpty()){
            firstNumber = (String) session.getAttribute("numberLoco");
        }
        if (date.equals(LocalDate.now())) {
            // Удаляем запись
            repairHistoryService.deleteByTypeAndNumberAndDate(typeLoco, numberLoco, date);

            // Проверяем, остались ли ещё записи для данного локомотива
            boolean isLastEntry = repairHistoryService.isLastEntry(typeLoco, numberLoco);

            // Если это была последняя запись, перенаправляем на нужную страницу
            if (isLastEntry) {
                return redirectToWorkBar(typeLoco, firstNumber, typeLocoUnit, model, session);
            }
        }

        // Возвращаем на ту же страницу истории, если ещё есть записи
        return redirectToWorkBar(typeLoco, firstNumber, typeLocoUnit, model, session);
    }

    //Получился универсальный метод для всех ссылок "На панель работ" НЕ ЗАБУДЬ!
    //Вспомогательный метод для удаления записи
    private String redirectToWorkBar(String typeLoco, String numberLoco, String typeLocoUnit, Model model, HttpSession session) {
        //LocoInfoDTO locoInfoDTO = locoInfoService.getLocoByNumber(numberLoco, typeLoco);
        LocoInfoDTO locoInfoDTO = locoInfoService.getLocoByNumber(numberLoco, typeLocoUnit);
        if (locoInfoDTO == null) {
            model.addAttribute("error", "Локомотив такой серии и номером не найден.");
            return showSearchForm(model, session); // Метод для отображения формы поиска
        }

        session.setAttribute(TYPE_LOCO, typeLoco);
        session.setAttribute("numberLoco", numberLoco);
        model.addAttribute("typeLocoUnit", typeLocoUnit);
        // Получаем номера секций
        List<String> sectionsNumber = Arrays.asList(
                locoInfoDTO.getLocoSection1(),
                locoInfoDTO.getLocoSection2(),
                locoInfoDTO.getLocoSection3(),
                locoInfoDTO.getLocoSection4()
        );

        // Фильтруем номера секций, чтобы получить только те, которые не пустые
        List<String> clearNumbers = locoInfoService.getClearLocoSections(sectionsNumber);

        // Получаем DTO для каждой секции из списка clearNumbers
        List<LocoListDTO> sections = new ArrayList<>();
        for (String sectionNumber : clearNumbers) {
            LocoListDTO section = locoListService.getLocoListByNumberLocoAndTypeLoco(sectionNumber, typeLoco);
            sections.add(section);
        }

        // Добавляем список DTO в модель
        model.addAttribute("sections", sections);
        model.addAttribute(TYPE_LOCO, typeLoco);
        model.addAttribute("numberLoco", numberLoco);
        model.addAttribute("locoInfoDTO", locoInfoDTO);


        return "repair_history_2_work_bar";
    }

    // Подготовка к добавлению записи в историю ремонта
    @GetMapping("/add_history")
    public String showAddHistoryForm(@RequestParam String sectionNumber, @RequestParam String typeLocoUnit, @RequestParam String homeDepot, Model model, HttpSession session) {
        String typeLoco = (String) session.getAttribute(TYPE_LOCO);
        String locoNumber = sectionNumber;
        //String homeDepot = locoListService.getLocoListByNumberLocoAndTypeLoco(locoNumber, typeLoco).getHomeDepot();
        //String firstNumber = locoInfoService.getLocoByFirstNumberSection(sectionNumber); // закрыл 02012024
        /*String firstNumber = locoInfoService.getLocoByFirstNumberSectionAndTypeLocoUint(sectionNumber, typeLocoUnit, homeDepot);
        if (firstNumber == null || firstNumber.isEmpty()){
            firstNumber = (String) session.getAttribute("numberLoco");
        }*/
        String firstNumber = (String) session.getAttribute("numberLoco");
        String repairDepot = (String) session.getAttribute("unit");
        String numberTable = (String) session.getAttribute("number_table");

        List<PositionRepairDTO> positionRepairDTOS = positionRepairService.getAllPositionRepairs();
        //String homeDepot = locoListService.getLocoListByNumberLocoAndTypeLoco(locoNumber, typeLoco).getHomeDepot();
        String typeSystem = locoListService.getLocoListByNumberLocoAndTypeLoco(locoNumber, typeLoco).getTypeSystem();

        LocalDate repairDateOld = LocalDate.now();

        // Извлечение сообщения об успешном добавлении из сессии
        /*String successMessage2 = (String) session.getAttribute("successMessage2");
        if (successMessage2 != null) {
            model.addAttribute("successMessage2", successMessage2);

        }*/
        try {
            Optional<RepairHistoryDto> repairHistoryOld = repairHistoryService.findByTypeLocoAndLocoNumberAndDate(typeLoco, locoNumber, repairDateOld);
            if (repairHistoryOld.isPresent()) {
                model.addAttribute("errorMessage1", "История для этого локомотива на эту дату уже существует.");
            }
        } catch (IllegalArgumentException e) {
            // Исключение означает, что история не найдена, продолжаем без ошибки
            // Можно добавить логирование для информации
            // log.info("История на текущую дату не найдена: " + e.getMessage());
        }

        Members user = userRepository.findByNumberTable(numberTable).orElse(null);
        String employee = "no name";

        if (user != null) {
            String fio = user.getFio();
            employee = repairHistoryService.convertToShortName(fio);  // Используем вспомогательный метод для форматирования
            model.addAttribute("employee", employee);
        } else {
            model.addAttribute("errorMessage", "Сотрудник с таким табельным номером не найден");
        }

        List<BlockOnLocoDTO> blocksOnLocoList = blockOnLocoService.getAllBlockOnLocoByLocoNumberAndTypeLoco(sectionNumber, typeLoco);
        List<String> blocksOnLoco = blocksOnLocoList.stream()
                .map(BlockOnLocoDTO::getBlockName)
                .toList();
        List<String> blockNumbers = blocksOnLocoList.stream()
                .map(BlockOnLocoDTO::getBlockNumber)
                .toList();
        LocalDate repairDate = LocalDate.now();
        int countBlocks = blocksOnLoco.size();

        //session.removeAttribute("successMessage2"); // Удаляем сообщение из сессии после использования

        model.addAttribute("repairDate", repairDate);
        model.addAttribute("homeDepot", homeDepot);
        model.addAttribute(TYPE_LOCO, typeLoco);
        model.addAttribute("locoNumber", locoNumber);
        model.addAttribute("firstNumber", firstNumber);
        model.addAttribute("employee", employee);
        model.addAttribute("typeSystem", typeSystem);
        model.addAttribute("repairDepot", repairDepot);
        model.addAttribute("positionRepairDTOS", positionRepairDTOS);
        model.addAttribute("repairHistoryDto", new RepairHistoryDto());
        model.addAttribute("blocksOnLoco", blocksOnLoco);
        model.addAttribute("blockNumbers", blockNumbers);
        model.addAttribute("countBlocks", countBlocks);
        model.addAttribute("typeLocoUnit", typeLocoUnit);


        // Список имен полей для ввода
        List<String> fieldNames = IntStream.rangeClosed(1, countBlocks)
                .mapToObj(i -> "block" + i + "Seal")
                .toList();
        model.addAttribute("fieldNames", fieldNames);

        return "repair_history_7_add_history";
    }

    // Добавление записи в историю ремонта
    @PostMapping("/add_history")
    public String addHistoryForm(@ModelAttribute RepairHistoryDto repairHistoryDto,
                                 @RequestParam("repairDate") String repairDate,
                                 @RequestParam("homeDepot") String homeDepot,
                                 @RequestParam(TYPE_LOCO) String typeLoco,
                                 @RequestParam("locoNumber") String locoNumber,
                                 @RequestParam("employee") String employee,
                                 @RequestParam("typeSystem") String typeSystem,
                                 @RequestParam("repairDepot") String repairDepot,
                                 @RequestParam("positionRepair") Long positionRepairId,
                                 @RequestParam("typeLocoUnit") String typeLocoUnit,
                                 Model model, HttpSession session) {

        String firstNumber;

        Optional<LocoInfo> locoInfoByDepotAndSection = locoInfoService.getLocoInfoByDepotAndSection(homeDepot, locoNumber);

        if (locoInfoByDepotAndSection.isPresent()) {
            firstNumber = locoInfoByDepotAndSection.get().getLocoUnit();
        } else {
            throw new IllegalArgumentException("Локомотив в составе с такой секцией: " + locoNumber + " не найден");
        }

        try {
            repairHistoryDto.setRepairDate(LocalDate.parse(repairDate));
            repairHistoryDto.setHomeDepot(homeDepot);
            repairHistoryDto.setTypeLoco(typeLoco);
            repairHistoryDto.setLocoNumber(locoNumber);
            repairHistoryDto.setEmployee(employee);
            repairHistoryDto.setTypeSystem(typeSystem);
            repairHistoryDto.setRepairDepot(repairDepot);

            String positionRepair = positionRepairService.getPositionRepairById(positionRepairId).getPosRepair();
            repairHistoryDto.setPositionRepair(positionRepair);

            // Обработка данных по пломбам
            repairHistoryDto.setBlock1Seal(repairHistoryDto.getBlock1Seal() != null && !repairHistoryDto.getBlock1Seal().isEmpty() ? repairHistoryDto.getBlock1Seal() : "нет");
            repairHistoryDto.setBlock2Seal(repairHistoryDto.getBlock2Seal() != null && !repairHistoryDto.getBlock2Seal().isEmpty() ? repairHistoryDto.getBlock2Seal() : "нет");
            repairHistoryDto.setBlock3Seal(repairHistoryDto.getBlock3Seal() != null && !repairHistoryDto.getBlock3Seal().isEmpty() ? repairHistoryDto.getBlock3Seal() : "нет");
            repairHistoryDto.setBlock4Seal(repairHistoryDto.getBlock4Seal() != null && !repairHistoryDto.getBlock4Seal().isEmpty() ? repairHistoryDto.getBlock4Seal() : "нет");
            repairHistoryDto.setBlock5Seal(repairHistoryDto.getBlock5Seal() != null && !repairHistoryDto.getBlock5Seal().isEmpty() ? repairHistoryDto.getBlock5Seal() : "нет");
            repairHistoryDto.setBlock6Seal(repairHistoryDto.getBlock6Seal() != null && !repairHistoryDto.getBlock6Seal().isEmpty() ? repairHistoryDto.getBlock6Seal() : "нет");
            repairHistoryDto.setBlock7Seal(repairHistoryDto.getBlock7Seal() != null && !repairHistoryDto.getBlock7Seal().isEmpty() ? repairHistoryDto.getBlock7Seal() : "нет");
            repairHistoryDto.setBlock8Seal(repairHistoryDto.getBlock8Seal() != null && !repairHistoryDto.getBlock8Seal().isEmpty() ? repairHistoryDto.getBlock8Seal() : "нет");
            repairHistoryDto.setBlock9Seal(repairHistoryDto.getBlock9Seal() != null && !repairHistoryDto.getBlock9Seal().isEmpty() ? repairHistoryDto.getBlock9Seal() : "нет");
            repairHistoryDto.setBlock10Seal(repairHistoryDto.getBlock10Seal() != null && !repairHistoryDto.getBlock10Seal().isEmpty() ? repairHistoryDto.getBlock10Seal() : "нет");


            repairHistoryService.save(repairHistoryDto);


            model.addAttribute("successMessage2", "Запись успешно добавлена, перейдите на панель работ по ссылке внизу страницы");
            //session.setAttribute("successMessage2", "Запись успешно добавлена, перейдите на панель работ по ссылке внизу страницы");
            session.setAttribute("sectionNumber", locoNumber);
            //return "repair_history_7_add_history"; // Возвращаемся на ту же страницу
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Ошибка добавления записи: " + e.getMessage());
            //return "repair_history_7_add_history"; // Повторное отображение формы
        }
        // В случае ошибки или успешного добавления, добавляем данные обратно в модель
        model.addAttribute("repairDate", repairDate);
        model.addAttribute("homeDepot", homeDepot);
        model.addAttribute(TYPE_LOCO, typeLoco);
        model.addAttribute("locoNumber", locoNumber);
        model.addAttribute("firstNumber", firstNumber);
        model.addAttribute("employee", employee);
        model.addAttribute("typeSystem", typeSystem);
        model.addAttribute("repairDepot", repairDepot);

        List<PositionRepairDTO> positionRepairDTOS = positionRepairService.getAllPositionRepairs();
        model.addAttribute("positionRepairDTOS", positionRepairDTOS);

        List<BlockOnLocoDTO> blocksOnLocoList = blockOnLocoService.getAllBlockOnLocoByLocoNumberAndTypeLoco(locoNumber, typeLoco);
        List<String> blocksOnLoco = blocksOnLocoList.stream()
                .map(BlockOnLocoDTO::getBlockName)
                .toList();
        model.addAttribute("blocksOnLoco", blocksOnLoco);
        model.addAttribute("countBlocks", blocksOnLoco.size());
        model.addAttribute("typeLocoUnit", typeLocoUnit);

        return redirectToWorkBar(typeLoco, firstNumber, typeLocoUnit, model, session);
    }

    // История ремонта детально
    @GetMapping("/detail_history")
    public String showDetailHistory(@RequestParam String typeLoco, @RequestParam String numberLoco, @RequestParam String repairDate, @RequestParam String typeLocoUnit, Model model, HttpSession session) {
        Optional<RepairHistoryDto> repairHistoryDto = repairHistoryService.findByTypeLocoAndLocoNumberAndDate(typeLoco, numberLoco, LocalDate.parse(repairDate));
        session.setAttribute("sectionNumber", numberLoco);
        session.setAttribute("typeLoco", typeLoco);
        String firstNumber = locoInfoService.getLocoByFirstNumberSection(numberLoco);
        session.setAttribute("firstNumber", firstNumber);

        List<BlockOnLocoDTO> blocksOnLocoList = blockOnLocoService.getAllBlockOnLocoByLocoNumberAndTypeLoco(numberLoco, typeLoco);
        List<String> blocksOnLoco = blocksOnLocoList.stream()
                .map(BlockOnLocoDTO::getBlockName)
                .toList();
        model.addAttribute("blocksOnLocoList", blocksOnLocoList);
        model.addAttribute("blocksOnLoco", blocksOnLoco);
        model.addAttribute("countBlocks", blocksOnLoco.size());

        // Получаем текущую дату
        LocalDate currentDate = LocalDate.now();
        model.addAttribute("currentDate", currentDate);

        // Форматтер для преобразования строки даты в LocalDate
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        // Список для хранения информации о просроченности блоков
        List<Boolean> isBlockExpiredList = new ArrayList<>();

        // Проверяем каждый блок на просроченность
        for (BlockOnLocoDTO block : blocksOnLocoList) {
            LocalDate dateOfIssue = LocalDate.parse(block.getDateOfIssue(), formatter);
            //LocalDate dateOfIssue = LocalDate.parse(block.getDateOfIssue());
            boolean isExpired = dateOfIssue.plusYears(2).isBefore(currentDate);
            isBlockExpiredList.add(isExpired);
        }

        // Добавляем информацию о просроченности блоков в модель
        model.addAttribute("isBlockExpiredList", isBlockExpiredList);
        model.addAttribute("typeLocoUnit", typeLocoUnit);

        if (repairHistoryDto.isPresent()) {

            model.addAttribute("repairHistoryDto", repairHistoryDto.get());
        } else {
            model.addAttribute("error", "История для этой секции на эту дату не найдена.");
        }

        return "repair_history_6_detail_history";
    }

    // Монтаж/демонтаж блоков  НЕ ДЕЙСТВУЮЩИЙ
    @PostMapping("/install_removal")
    public String showInstallBlocks(Model model, HttpSession session){
        String typeLoco = (String) session.getAttribute(TYPE_LOCO);
        String numberLoco = (String) session.getAttribute("numberLoco");

        LocoListDTO locoListDTO = locoListService.getLocoListByNumberLocoAndTypeLoco(numberLoco, typeLoco);
        String systemType = locoListDTO.getTypeSystem();

        List<PositionRepairDTO> positionRepairDTOS = positionRepairService.getAllPositionRepairs();
        List<BlockOnLocoDTO> blockOnLocoDTOS = blockOnLocoService.getAllBlockOnLocoByLocoNumberAndTypeLoco(numberLoco, typeLoco);
        List<BlockRemovalDto> removedBlocks = blockRemovalService.getBlockRemovalByTypeLocoAndNumberLoco(numberLoco, typeLoco);

        String depot = (String) session.getAttribute("unit");
        List<ReceiptBlockDto> receiptBlockDtos = receiptBlockService.getReceiptBlocksByStorageNameAndTypeSystem(depot, systemType);

        model.addAttribute(TYPE_LOCO, typeLoco);
        model.addAttribute("numberLoco", numberLoco);
        model.addAttribute("systemType", systemType);
        model.addAttribute("positionRepairDTOS", positionRepairDTOS); // Добавляем позиции ремонта в модель
        model.addAttribute("blockOnLocoDTOS", blockOnLocoDTOS);
        model.addAttribute("removedBlocks", removedBlocks);
        model.addAttribute("receiptBlockDtos", receiptBlockDtos);

        return "repair_history_4_install_loco";
    }


    // Методы для отгрузки блока со склада и монтажа блока на локомотив
    @GetMapping("/install_block")
    public String showInstallBlockPage(Model model, HttpSession session) {
        String typeLoco = (String) session.getAttribute(TYPE_LOCO);
        String numberLoco = (String) session.getAttribute("numberLoco");
        String sectionNumber = (String) session.getAttribute("sectionNumber"); //Нужно получить номер секции из @Param здесь пока null!
        String depot = (String) session.getAttribute("unit");

        LocoListDTO locoListDTO = locoListService.getLocoListByNumberLocoAndTypeLoco(numberLoco, typeLoco);
        String systemType = locoListDTO.getTypeSystem();

        List<BlockOnLocoDTO> blockOnLocoDTOS = blockOnLocoService.getAllBlockOnLocoByLocoNumberAndTypeLoco(numberLoco, typeLoco);
        List<ReceiptBlockDto> receiptBlockDtos = receiptBlockService.getReceiptBlocksByStorageNameAndTypeSystem(depot, systemType);

        model.addAttribute("blockOnLocoDTOS", blockOnLocoDTOS);
        model.addAttribute("receiptBlockDtos", receiptBlockDtos);
        model.addAttribute(TYPE_LOCO, typeLoco);
        model.addAttribute("numberLoco", numberLoco);
        model.addAttribute("systemType", systemType);

        return "repair_history_10_install_block";
    }

    @PostMapping("/install_block")
    public String installBlock(@RequestParam("blockName") String nameBlock,
                               @RequestParam("blockNumber") String blockNumber,
                               RedirectAttributes redirectAttributes, // Изменено с Model на RedirectAttributes
                               HttpSession session) {
        String numberTable = (String) session.getAttribute("number_table");
        String typeLoco = (String) session.getAttribute("typeLoco");
        String numberLoco = (String) session.getAttribute("numberLoco");
        String region = (String) session.getAttribute("region");

        List<BlockOnLocoDTO> blockOnLocoDTOS = blockOnLocoService.getAllBlockOnLocoByLocoNumberAndTypeLoco(numberLoco, typeLoco);

        List<BlockOnLocoDTO> filteredBlock = blockOnLocoDTOS.stream()
                .filter(filtered -> nameBlock.equals(filtered.getBlockName()))
                .toList();
        if (!filteredBlock.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Такой блок уже установлен на локомотиве.");
            return "redirect:/repair_history/install_block";
        }


        LocoListDTO locoListDTO = locoListService.getLocoListByNumberLocoAndTypeLoco(numberLoco, typeLoco);
        String systemType = locoListDTO.getTypeSystem();

        try {
            shipmentBlockService.shipmentLocoBlockFromStorage(numberTable, systemType, nameBlock, blockNumber, region);
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/repair_history/install_block";
        }

        BlockOnLocoDTO blockOnLocoDTO = new BlockOnLocoDTO();
        blockOnLocoDTO.setLocoListId(locoListDTO.getId());
        blockOnLocoDTO.setBlockName(nameBlock);
        blockOnLocoDTO.setBlockNumber(blockNumber);
        blockOnLocoDTO.setTypeLoco(typeLoco);
        blockOnLocoDTO.setLocoNumber(numberLoco);
        blockOnLocoService.createBlockOnLoco(blockOnLocoDTO);

        redirectAttributes.addFlashAttribute("success", "Блок успешно установлен.");
        return "redirect:/repair_history/install_block";
    }

    // Методы демонтажа блока с локомотива
    @GetMapping("/remove_block/prepare")
    public String showRemoveBlockPage(Model model, HttpSession session) {
        String typeLoco = (String) session.getAttribute(TYPE_LOCO);
        String numberLoco = (String) session.getAttribute("numberLoco");

        List<BlockOnLocoDTO> blockOnLocoDTOS = blockOnLocoService.getAllBlockOnLocoByLocoNumberAndTypeLoco(numberLoco, typeLoco);
        List<PositionRepairDTO> positionRepairDTOS = positionRepairService.getAllPositionRepairs();

        model.addAttribute("blockOnLocoDTOS", blockOnLocoDTOS);
        model.addAttribute("positionRepairDTOS", positionRepairDTOS);
        model.addAttribute(TYPE_LOCO, typeLoco);
        model.addAttribute("numberLoco", numberLoco);

        return "repair_history_8_remove_block";
    }

    @PostMapping("/remove_block")
    public ResponseEntity<Void> removeBlock(@RequestParam String nameBlock,
                                            @RequestParam String blockNumber,
                                            @RequestParam("positionRepair") Long positionRepairId,
                                            HttpSession session) {
        String typeLoco = (String) session.getAttribute(TYPE_LOCO);
        String numberLoco = (String) session.getAttribute("numberLoco");

        // Получение позиции ремонта по ID
        PositionRepairDTO positionRepairDTO = positionRepairService.getPositionRepairById(positionRepairId);
        String positionRepair = positionRepairDTO.getPosRepair();

        String homeDepot = locoListService.getLocoListByNumberLocoAndTypeLoco(numberLoco, typeLoco).getHomeDepot();
        String systemType = locoListService.getLocoListByNumberLocoAndTypeLoco(numberLoco, typeLoco).getTypeSystem();

        // Формирование DTO для записи
        BlockRemovalDto blockRemovalDto = new BlockRemovalDto();
        blockRemovalDto.setTypeLoco(typeLoco);
        blockRemovalDto.setLocoNumber(numberLoco);
        blockRemovalDto.setRegion((String) session.getAttribute("region"));
        blockRemovalDto.setHomeDepot(homeDepot);
        blockRemovalDto.setSystemType(systemType);
        blockRemovalDto.setBlockName(nameBlock);
        blockRemovalDto.setBlockNumber(blockNumber);
        blockRemovalDto.setNumberTable((String) session.getAttribute("number_table"));
        blockRemovalDto.setPosition(positionRepair);

        // Логика добавления записи в "Демонтированные блоки"
        blockRemovalService.createBlockRemoval(blockRemovalDto);

        return ResponseEntity.ok().build();
    }


    // Метод отмены демонтажа блока
    @GetMapping("/cancel_remove_block")
    public String showCancelRemoveBlockPage(Model model, HttpSession session) {
        String typeLoco = (String) session.getAttribute(TYPE_LOCO);
        String numberLoco = (String) session.getAttribute("numberLoco");

        if (typeLoco == null || numberLoco == null) {
            model.addAttribute("error", "Нет данных о типе или номере локомотива.");
            return "repair_history_9_cancel_remove_block";
        }

        List<BlockRemovalDto> removedBlocks = blockRemovalService.getBlockRemovalByTypeLocoAndNumberLoco(typeLoco, numberLoco);
        model.addAttribute("removedBlocks", removedBlocks);

        return "repair_history_9_cancel_remove_block";
    }

    @PostMapping("/cancel_remove_block")
    public String cancelRemoveBlock(@RequestParam("nameBlock") String nameBlock,
                                    @RequestParam("blockNumber") String blockNumber,
                                    RedirectAttributes redirectAttributes,
                                    HttpSession session) {
        String typeLoco = (String) session.getAttribute(TYPE_LOCO);
        String numberLoco = (String) session.getAttribute("numberLoco");
        String region = (String) session.getAttribute("region");
        LocoListDTO locoListDTO = locoListService.getLocoListByNumberLocoAndTypeLoco(numberLoco, typeLoco);
        String homeDepot = locoListDTO.getHomeDepot();
        String systemType = locoListDTO.getTypeSystem();
        String numberTable = (String) session.getAttribute("number_table");

        BlockRemovalDto blockRemovalDto = new BlockRemovalDto();
        blockRemovalDto.setTypeLoco(typeLoco);
        blockRemovalDto.setLocoNumber(numberLoco);
        blockRemovalDto.setBlockNumber(blockNumber);
        blockRemovalDto.setRegion(region);
        blockRemovalDto.setHomeDepot(homeDepot);
        blockRemovalDto.setSystemType(systemType);
        blockRemovalDto.setBlockName(nameBlock);
        blockRemovalDto.setNumberTable(numberTable);

        try {
            blockRemovalService.cancelBlockRemovalFromLoco(blockRemovalDto);
            redirectAttributes.addFlashAttribute("success", "Демонтаж блока успешно отменен.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/repair_history/cancel_remove_block";
    }

    // Рабочая панель
    @GetMapping("/work_bar")
    public String showWorkBar(Model model, HttpSession session) {
        String typeLoco = (String) session.getAttribute(TYPE_LOCO);
        String numberLoco = (String) session.getAttribute("numberLoco");

        model.addAttribute(TYPE_LOCO, typeLoco);
        model.addAttribute("numberLoco", numberLoco);

        return "repair_history_2_work_bar";
    }

    @GetMapping("/history_loco")
    public String showHistoryLoco(@RequestParam String typeLoco, @RequestParam String numberLoco, @RequestParam int page, Model model, HttpSession session) {
        List<RepairHistoryDto> repairHistoryDtos = repairHistoryService.findByTypeLocoAndLocoNumber(typeLoco, numberLoco);
        int pageSize = 5;
        int totalPages = (int) Math.ceil((double) repairHistoryDtos.size() / pageSize);
        int start = Math.min(page * pageSize, repairHistoryDtos.size());
        int end = Math.min((page + 1) * pageSize, repairHistoryDtos.size());

        model.addAttribute("repairHistoryDtos", repairHistoryDtos.subList(start, end));
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        return "repair_history_3_history_loco";
    }

    // Подготовка данных к списанию зап частей и метериалов
    @GetMapping("/spare_parts")
    public String showSpareParts(@RequestParam String typeLoco, @RequestParam String numberLoco, Model model, HttpSession session) {
        String storageName = (String) session.getAttribute("unit");
        String region = (String) session.getAttribute("region");
        String numberTable = (String) session.getAttribute("number_table");

        List<SparePartsReceiptDto> sparePartsReceiptDtos = sparePartsReceiptService.getAllSparePartsStockByStorageName(storageName);

        model.addAttribute("region", region);
        model.addAttribute("storageName", storageName);
        model.addAttribute("numberTable", numberTable);
        model.addAttribute(TYPE_LOCO, typeLoco);
        model.addAttribute("numberLoco", numberLoco);
        model.addAttribute("sparePartsReceiptDtos", sparePartsReceiptDtos);

        return "repair_history_5_spare_parts";
    }

    // Списание зап частей и метериалов
    @PostMapping("/spare_parts/write_off")
    public String writeOffSpareParts(
            @RequestParam("typeLoco") String typeLoco,
            @RequestParam("numberLoco") String numberLoco,
            @RequestParam("sparePartId") Long sparePartId,
            @RequestParam("quantity") Double quantity,
            Model model,
            HttpSession session) {

        String storageName = (String) session.getAttribute("unit");
        String region = (String) session.getAttribute("region");
        String numberTable = (String) session.getAttribute("number_table");

        try {
            // Получаем данные запчасти по ID
            SparePartsReceiptDto sparePartDto = sparePartsReceiptService.getSparePartsReceiptById(sparePartId);
            if (sparePartDto == null) {
                model.addAttribute("errorMessage", "Запчасть не найдена.");
                return showSpareParts(typeLoco, numberLoco, model, session);
            }

            // Подготовка данных для списания
            SparePartsReceiptDto preparedWriteOffSparePartsReceiptDto = sparePartsReceiptService.prepareWriteOffSparePartDto(
                    region, storageName, numberTable, sparePartDto.getSparePartName(), sparePartDto.getMeasure(),
                    sparePartDto.getSparePartNumber(), "на складе", quantity);

            // Проведение списания
            SparePartsReceiptDto writeOffSparePartsReceipt = sparePartsReceiptService.writeOffSparePartDto(preparedWriteOffSparePartsReceiptDto);

            model.addAttribute("writeOffSparePartsReceipt", writeOffSparePartsReceipt);
            model.addAttribute("successMessage", "Списание успешно завершено.");
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", "Ошибка при списании: " + e.getMessage());
        }

        return showSpareParts(typeLoco, numberLoco, model, session);
    }

    //Формирование отчета
    @GetMapping("/repaired-locos")
    public String getRepairedLocos(Model model) {
        List<RepairedLocoDTO> repairedLocos = repairHistoryService.getRepairedLocos();
        model.addAttribute("repairedLocos", repairedLocos);
        return "repair_history_11_repaired-locos"; // Имя шаблона HTML для отображения данных
    }

    // Метод для генерации отчета
    @GetMapping("/generate-report")
    public void generateReport(HttpServletResponse response) throws IOException {
        List<RepairedLocoDTO> repairedLocos = repairHistoryService.getRepairedLocos();

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=repaired-locos-report-basic.xlsx");

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Repaired Locos");

        // Создание стилей
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(createFont(workbook, "Times New Roman", (short) 8, true));
        headerStyle.setAlignment(HorizontalAlignment.CENTER); // Центрирование текста
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER); // Центрирование текста по вертикали
        headerStyle.setWrapText(true); // Перенос текста в заголовках

        CellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setFont(createFont(workbook, "Times New Roman", (short) 14, true));
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        CellStyle dateStyle = workbook.createCellStyle();
        dateStyle.setFont(createFont(workbook, "Times New Roman", (short) 10, true));
        dateStyle.setAlignment(HorizontalAlignment.LEFT);
        dateStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        CellStyle textStyle = workbook.createCellStyle();
        textStyle.setFont(createFont(workbook, "Times New Roman", (short) 7, false));
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

    // Методы для получения отчетов для СПСТ и без учета СПСТ
    @GetMapping("/reports")
    public String showReportsSelection() {
        return "repair_history_13_reports_selection"; // Имя шаблона для формы выбора отчета
    }

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
        model.addAttribute("repairedLocos", repairedLocos);
        model.addAttribute("depotName", depot); // Добавляем наименование депо в модель
        return "repair_history_11_repaired-locos";
    }

    @GetMapping("/filter-report-sps")
    public String filterReportSps(@RequestParam String region, @RequestParam String depot, @RequestParam(required = false) boolean includeSPS, Model model) {
        List<RepairedLocoDTO> repairedLocos;
        if (includeSPS) {
            repairedLocos = repairHistoryService.getRepairedLocosWithTypeSPS(depot);
        } else {
            repairedLocos = repairHistoryService.getRepairedLocosWithoutTypeSPS(depot);
        }
        model.addAttribute("repairedLocos", repairedLocos);
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

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=repaired-locos-report-with-sps.xlsx");

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Repaired Locos");

        // Создание стилей
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(createFont(workbook, "Times New Roman", (short) 8, true));
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerStyle.setWrapText(true);

        CellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setFont(createFont(workbook, "Times New Roman", (short) 14, true));
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        CellStyle dateStyle = workbook.createCellStyle();
        dateStyle.setFont(createFont(workbook, "Times New Roman", (short) 10, true));
        dateStyle.setAlignment(HorizontalAlignment.LEFT);
        dateStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        CellStyle textStyle = workbook.createCellStyle();
        textStyle.setFont(createFont(workbook, "Times New Roman", (short) 7, false));
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

    @GetMapping("/depots")
    @ResponseBody
    public List<HomeDepotDTO> getDepotsByRegion(@RequestParam String regionName) {
        return homeDepotService.getDepotsByRegion(regionName);
    }

    // Метод для отображения формы выбора депо и дат  СПСТ по дате и депо
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

        model.addAttribute("repairedLocos", repairedLocos);
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
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=repaired-locos-report-sps-date.xlsx");

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Repaired Locos");

        // Создание стилей
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(createFont(workbook, "Times New Roman", (short) 8, true));
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerStyle.setWrapText(true);

        CellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setFont(createFont(workbook, "Times New Roman", (short) 14, true));
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        CellStyle dateStyle = workbook.createCellStyle();
        dateStyle.setFont(createFont(workbook, "Times New Roman", (short) 10, true));
        dateStyle.setAlignment(HorizontalAlignment.LEFT);
        dateStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        CellStyle textStyle = workbook.createCellStyle();
        textStyle.setFont(createFont(workbook, "Times New Roman", (short) 7, false));
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

    // Метод для отображения формы выбора депо и дат  БЕЗ СПСТ по дате и депо
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

        model.addAttribute("repairedLocos", repairedLocos);
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
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=repaired-locos-report-sps-without.xlsx");

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Repaired Locos");

        // Создание стилей
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(createFont(workbook, "Times New Roman", (short) 8, true));
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerStyle.setWrapText(true);

        CellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setFont(createFont(workbook, "Times New Roman", (short) 14, true));
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        CellStyle dateStyle = workbook.createCellStyle();
        dateStyle.setFont(createFont(workbook, "Times New Roman", (short) 10, true));
        dateStyle.setAlignment(HorizontalAlignment.LEFT);
        dateStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        CellStyle textStyle = workbook.createCellStyle();
        textStyle.setFont(createFont(workbook, "Times New Roman", (short) 7, false));
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

    //Список депо по имени Региона
    @GetMapping("/region")
    @ResponseBody
    public List<RepDepotDTO> getRepairDepotsByRegion(@RequestParam String region) {
        return repDepotService.getDepotsByRegionName(region);
    }
}
