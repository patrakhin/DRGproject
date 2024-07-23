package com.drgproject.repair.controller;

import com.drgproject.dto.ReceiptBlockDto;
import com.drgproject.dto.SparePartsReceiptDto;
import com.drgproject.dto.UserDTO;
import com.drgproject.entity.User;
import com.drgproject.repair.RepairHistoryMapper;
import com.drgproject.repair.dto.*;
import com.drgproject.repair.entity.RepairHistory;
import com.drgproject.repair.service.*;
import com.drgproject.repository.ReceiptBlockRepository;
import com.drgproject.repository.UserRepository;
import com.drgproject.service.ReceiptBlockService;
import com.drgproject.service.ShipmentBlockService;
import com.drgproject.service.SparePartsReceiptService;
import com.drgproject.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@CrossOrigin(origins = "*", allowedHeaders = "*")
@Controller
@RequestMapping("/repair_history")
public class RepairHistoryControllerTwo {

    private final RepairHistoryService repairHistoryService;
    private final LocoListService locoListService;
    private final BlockOnLocoService blockOnLocoService;
    private final ReceiptBlockService receiptBlockService;
    private final BlockRemovalService blockRemovalService;
    private final TypeLocoService typeLocoService;
    private final SparePartsReceiptService sparePartsReceiptService;
    private final PositionRepairService positionRepairService;
    private final UserRepository userRepository;
    private final UserService userService;
    private final ShipmentBlockService shipmentBlockService;
    private RestTemplate restTemplate;

    public RepairHistoryControllerTwo(RepairHistoryService repairHistoryService,
                                      LocoListService locoListService,
                                      BlockOnLocoService blockOnLocoService,
                                      ReceiptBlockService receiptBlockService,
                                      BlockRemovalService blockRemovalService,
                                      TypeLocoService typeLocoService,
                                      SparePartsReceiptService sparePartsReceiptService,
                                      PositionRepairService positionRepairService,
                                      UserRepository userRepository,
                                      UserService userService,
                                      ShipmentBlockService shipmentBlockService,
                                      RestTemplate restTemplate) {
        this.repairHistoryService = repairHistoryService;
        this.locoListService = locoListService;
        this.blockOnLocoService = blockOnLocoService;
        this.receiptBlockService = receiptBlockService;
        this.blockRemovalService = blockRemovalService;
        this.typeLocoService = typeLocoService;
        this.sparePartsReceiptService = sparePartsReceiptService;
        this.positionRepairService = positionRepairService;
        this.userRepository = userRepository;
        this.userService = userService;
        this.shipmentBlockService = shipmentBlockService;
        this.restTemplate = restTemplate;
    }

    // Главная страница
    @GetMapping("/search")
    public String showSearchForm(Model model) {
        model.addAttribute("locoList", new LocoListDTO());
        List<TypeLocoDTO> typeLocos = typeLocoService.getAllTypeLocos();
        model.addAttribute("typeLocos", typeLocos);
        return "repair_history_1_main";
    }

    // Результат поиска локомотива по типу и серии
    @PostMapping("/search")
    public String searchLocoByTypeAndNumber(@RequestParam String typeLoco, @RequestParam String numberLoco, Model model, HttpSession session) {
        LocoListDTO locoListDTO = locoListService.getLocoListByNumberLocoAndTypeLoco(numberLoco, typeLoco);
        if (locoListDTO == null) {
            model.addAttribute("error", "Локомотив с таким типом и номером не найден.");
            return showSearchForm(model);
        }

        session.setAttribute("typeLoco", typeLoco);
        session.setAttribute("numberLoco", numberLoco);

        model.addAttribute("typeLoco", typeLoco);
        model.addAttribute("numberLoco", numberLoco);
        model.addAttribute("locoListDTO", locoListDTO);

        return "repair_history_2_work_bar";
    }

    // История ремонта
    @PostMapping("/repair_history")
    public String showRepairHistory(Model model, HttpSession session) {
        String typeLoco = (String) session.getAttribute("typeLoco");
        String numberLoco = (String) session.getAttribute("numberLoco");
        String storageName = (String) session.getAttribute("unit");
        LocoListDTO locoListDTO = locoListService.getLocoListByNumberLocoAndTypeLoco(numberLoco, typeLoco);
        String systemType = locoListDTO.getTypeSystem(); // Получаем тип системы

        List<BlockOnLocoDTO> blockOnLocoDTOS = blockOnLocoService.getAllBlockOnLocoByLocoNumberAndTypeLoco(numberLoco, typeLoco);
        List<ReceiptBlockDto> receiptBlockDtos = receiptBlockService.getReceiptBlocksByStorageName(storageName);
        // Получаем список историй ремонта для конкретного лоеомотива
        List<RepairHistoryDto> repairHistoryDtos = repairHistoryService.findByTypeLocoAndLocoNumber(typeLoco, numberLoco);

        // Добавляемв историю ремонта только три последние записи
        List<RepairHistoryDto> lastThreeEntries = getLastThreeEntries(repairHistoryDtos);

        model.addAttribute("lastThreeEntries", lastThreeEntries);
        model.addAttribute("typeLoco", typeLoco);
        model.addAttribute("numberLoco", numberLoco);
        model.addAttribute("systemType", systemType); // Добавляем systemType в модель
        model.addAttribute("blockOnLocoDTOS", blockOnLocoDTOS);
        model.addAttribute("receiptBlockDtos", receiptBlockDtos);

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

    // Подготовка к добавлению записи в историю ремонта
    @GetMapping("/add_history")
    public String showAddHistoryForm(Model model, HttpSession session) {
        String typeLoco = (String) session.getAttribute("typeLoco");
        String locoNumber = (String) session.getAttribute("numberLoco");
        String repairDepot = (String) session.getAttribute("unit");
        String numberTable = (String) session.getAttribute("number_table");

        List<PositionRepairDTO> positionRepairDTOS = positionRepairService.getAllPositionRepairs();
        String homeDepot = locoListService.getLocoListByNumberLocoAndTypeLoco(locoNumber, typeLoco).getHomeDepot();
        String typeSystem = locoListService.getLocoListByNumberLocoAndTypeLoco(locoNumber, typeLoco).getTypeSystem();

        User user = userRepository.findByNumberTable(numberTable).orElse(null);
        String employee = "no name";
        if (user != null) {
            employee = user.getNumberTable();
            model.addAttribute("employee", employee);
        } else {
            model.addAttribute("errorMessage", "Сотрудник с таким табельным номером не найден");
        }

        LocalDate repairDate = LocalDate.now();

        model.addAttribute("repairDate", repairDate);
        model.addAttribute("homeDepot", homeDepot);
        model.addAttribute("typeLoco", typeLoco);
        model.addAttribute("locoNumber", locoNumber);
        model.addAttribute("employee", employee);
        model.addAttribute("typeSystem", typeSystem);
        model.addAttribute("repairDepot", repairDepot);
        model.addAttribute("positionRepairDTOS", positionRepairDTOS);
        model.addAttribute("repairHistoryDto", new RepairHistoryDto());

        return "repair_history_7_add_history";
    }

    // Добавление записи в историю ремонта
    @PostMapping("/add_history")
    public String addHistoryForm(@ModelAttribute RepairHistoryDto repairHistoryDto,
                                 @RequestParam("repairDate") String repairDate,
                                 @RequestParam("homeDepot") String homeDepot,
                                 @RequestParam("typeLoco") String typeLoco,
                                 @RequestParam("locoNumber") String locoNumber,
                                 @RequestParam("employee") String employee,
                                 @RequestParam("typeSystem") String typeSystem,
                                 @RequestParam("repairDepot") String repairDepot,
                                 @RequestParam("positionRepair") Long positionRepairId,
                                 Model model) {
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

            repairHistoryService.save(repairHistoryDto);
            model.addAttribute("successMessage", "Запись успешно добавлена");
            return "repair_history_7_add_history"; // Возвращаемся на ту же страницу
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Ошибка добавления записи: " + e.getMessage());
            return "repair_history_7_add_history"; // Повторное отображение формы
        }
    }

    // История ремонта детально
    @GetMapping("/detail_history")
    public String showDetailHistory(@RequestParam String typeLoco, @RequestParam String numberLoco, @RequestParam String repairDate, Model model) {
        Optional<RepairHistoryDto> repairHistoryDto = repairHistoryService.findByTypeLocoAndLocoNumberAndDate(typeLoco, numberLoco, LocalDate.parse(repairDate));
        if (repairHistoryDto.isPresent()) {
            model.addAttribute("repairHistoryDto", repairHistoryDto.get());
        } else {
            model.addAttribute("error", "История для этого локомотива на эту дату не найдена.");
        }

        return "repair_history_6_detail_history";
    }

    // Монтаж/демонтаж блоков
    @PostMapping("/install_removal")
    public String showInstallBlocks(Model model, HttpSession session){
        String typeLoco = (String) session.getAttribute("typeLoco");
        String numberLoco = (String) session.getAttribute("numberLoco");

        LocoListDTO locoListDTO = locoListService.getLocoListByNumberLocoAndTypeLoco(numberLoco, typeLoco);
        String systemType = locoListDTO.getTypeSystem();

        List<PositionRepairDTO> positionRepairDTOS = positionRepairService.getAllPositionRepairs();
        List<BlockOnLocoDTO> blockOnLocoDTOS = blockOnLocoService.getAllBlockOnLocoByLocoNumberAndTypeLoco(numberLoco, typeLoco);
        List<BlockRemovalDto> removedBlocks = blockRemovalService.getBlockRemovalByTypeLocoAndNumberLoco(numberLoco, typeLoco);

        String depot = (String) session.getAttribute("unit");
        List<ReceiptBlockDto> receiptBlockDtos = receiptBlockService.getReceiptBlocksByStorageNameAndTypeSystem(depot, systemType);

        model.addAttribute("typeLoco", typeLoco);
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
        String typeLoco = (String) session.getAttribute("typeLoco");
        String numberLoco = (String) session.getAttribute("numberLoco");
        String depot = (String) session.getAttribute("unit");

        LocoListDTO locoListDTO = locoListService.getLocoListByNumberLocoAndTypeLoco(numberLoco, typeLoco);
        String systemType = locoListDTO.getTypeSystem();

        List<BlockOnLocoDTO> blockOnLocoDTOS = blockOnLocoService.getAllBlockOnLocoByLocoNumberAndTypeLoco(numberLoco, typeLoco);
        List<ReceiptBlockDto> receiptBlockDtos = receiptBlockService.getReceiptBlocksByStorageNameAndTypeSystem(depot, systemType);

        model.addAttribute("blockOnLocoDTOS", blockOnLocoDTOS);
        model.addAttribute("receiptBlockDtos", receiptBlockDtos);
        model.addAttribute("typeLoco", typeLoco);
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
        String typeLoco = (String) session.getAttribute("typeLoco");
        String numberLoco = (String) session.getAttribute("numberLoco");

        List<BlockOnLocoDTO> blockOnLocoDTOS = blockOnLocoService.getAllBlockOnLocoByLocoNumberAndTypeLoco(numberLoco, typeLoco);
        List<PositionRepairDTO> positionRepairDTOS = positionRepairService.getAllPositionRepairs();

        model.addAttribute("blockOnLocoDTOS", blockOnLocoDTOS);
        model.addAttribute("positionRepairDTOS", positionRepairDTOS);
        model.addAttribute("typeLoco", typeLoco);
        model.addAttribute("numberLoco", numberLoco);

        return "repair_history_8_remove_block";
    }

    @PostMapping("/remove_block")
    public ResponseEntity<Void> removeBlock(@RequestParam String nameBlock,
                                            @RequestParam String blockNumber,
                                            @RequestParam("positionRepair") Long positionRepairId,
                                            HttpSession session) {
        String typeLoco = (String) session.getAttribute("typeLoco");
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
        String typeLoco = (String) session.getAttribute("typeLoco");
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
        String typeLoco = (String) session.getAttribute("typeLoco");
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
        String typeLoco = (String) session.getAttribute("typeLoco");
        String numberLoco = (String) session.getAttribute("numberLoco");

        model.addAttribute("typeLoco", typeLoco);
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
        model.addAttribute("typeLoco", typeLoco);
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
}
