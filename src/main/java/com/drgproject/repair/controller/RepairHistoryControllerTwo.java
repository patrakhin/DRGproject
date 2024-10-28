package com.drgproject.repair.controller;

import com.drgproject.dto.ReceiptBlockDto;
import com.drgproject.dto.SparePartsReceiptDto;
import com.drgproject.entity.Members;
import com.drgproject.repair.dto.*;
import com.drgproject.repair.entity.LocoInfo;
import com.drgproject.repair.service.*;
import com.drgproject.repository.MemberRepository;
import com.drgproject.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.IntStream;

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
    private final SparePartsReceiptService sparePartsReceiptService;
    private final PositionRepairService positionRepairService;
    private final MemberRepository userRepository;
    private final ShipmentBlockService shipmentBlockService;
    private final UserService userService;
    private final LocoInfoService locoInfoService;
    private final HomeDepotService homeDepotService;
    private final RepDepotService repDepotService;
    private final AllTypeLocoUnitService allTypeLocoUnitService;

    public RepairHistoryControllerTwo(RepairHistoryService repairHistoryService,
                                      LocoListService locoListService,
                                      BlockOnLocoService blockOnLocoService,
                                      ReceiptBlockService receiptBlockService,
                                      BlockRemovalService blockRemovalService,
                                      SparePartsReceiptService sparePartsReceiptService,
                                      PositionRepairService positionRepairService,
                                      MemberRepository userRepository,
                                      ShipmentBlockService shipmentBlockService,
                                      UserService userService,
                                      LocoInfoService locoInfoService,
                                      HomeDepotService homeDepotService,
                                      RepDepotService repDepotService,
                                      AllTypeLocoUnitService allTypeLocoUnitService) {
        this.repairHistoryService = repairHistoryService;
        this.locoListService = locoListService;
        this.blockOnLocoService = blockOnLocoService;
        this.receiptBlockService = receiptBlockService;
        this.blockRemovalService = blockRemovalService;
        this.sparePartsReceiptService = sparePartsReceiptService;
        this.positionRepairService = positionRepairService;
        this.userRepository = userRepository;
        this.shipmentBlockService = shipmentBlockService;
        this.userService = userService;
        this.locoInfoService = locoInfoService;
        this.homeDepotService = homeDepotService;
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

    // Результат поиска локомотива по  серии и номеру
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
        String firstNumber = (String) session.getAttribute("numberLoco");
        String repairDepot = (String) session.getAttribute("unit");
        String numberTable = (String) session.getAttribute("number_table");

        List<PositionRepairDTO> positionRepairDTOS = positionRepairService.getAllPositionRepairs();
        String typeSystem = locoListService.getLocoListByNumberLocoAndTypeLoco(locoNumber, typeLoco).getTypeSystem();
        LocalDate repairDateOld = LocalDate.now();

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
            session.setAttribute("sectionNumber", locoNumber);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Ошибка добавления записи: " + e.getMessage());
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

    @GetMapping("/depots")
    @ResponseBody
    public List<HomeDepotDTO> getDepotsByRegion(@RequestParam String regionName) {
        return homeDepotService.getDepotsByRegion(regionName);
    }

    //Список депо по имени Региона
    @GetMapping("/region")
    @ResponseBody
    public List<RepDepotDTO> getRepairDepotsByRegion(@RequestParam String region) {
        return repDepotService.getDepotsByRegionName(region);
    }
}
