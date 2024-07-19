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
import com.drgproject.service.SparePartsReceiptService;
import com.drgproject.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
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

    public RepairHistoryControllerTwo(RepairHistoryService repairHistoryService,
                                      LocoListService locoListService,
                                      BlockOnLocoService blockOnLocoService,
                                      ReceiptBlockService receiptBlockService,
                                      BlockRemovalService blockRemovalService,
                                      TypeLocoService typeLocoService,
                                      SparePartsReceiptService sparePartsReceiptService,
                                      PositionRepairService positionRepairService,
                                      UserRepository userRepository, UserService userService) {
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
        String systemType = locoListDTO.getTypeSystem(); // Получаем тип системы

        List<BlockOnLocoDTO> blockOnLocoDTOS = blockOnLocoService.getAllBlockOnLocoByLocoNumberAndTypeLoco(numberLoco, typeLoco);
        model.addAttribute("blockOnLocoDTOS", blockOnLocoDTOS);

        String depot = (String) session.getAttribute("unit");
        List<ReceiptBlockDto> receiptBlockDtos = receiptBlockService.getReceiptBlocksByStorageName(depot);
        model.addAttribute("typeLoco", typeLoco);
        model.addAttribute("numberLoco", numberLoco);
        model.addAttribute("systemType", systemType); // Добавляем systemType в модель
        model.addAttribute("blockOnLocoDTOS", blockOnLocoDTOS);
        model.addAttribute("receiptBlockDtos", receiptBlockDtos);

        return "repair_history_4_install_loco";
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
