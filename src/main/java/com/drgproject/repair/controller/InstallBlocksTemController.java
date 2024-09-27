package com.drgproject.repair.controller;

import com.drgproject.repair.dto.BlockOnLocoDTO;
import com.drgproject.repair.dto.TypeLocoDTO;
import com.drgproject.repair.service.*;
import com.drgproject.service.LocoBlockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Controller
@RequestMapping("/install-temp")
public class InstallBlocksTemController {
    private final BlockOnLocoService blockOnLocoService;
    private final TypeLocoService typeLocoService;
    private final RegionService regionService;
    private final HomeDepotService homeDepotService;
    private final SystemNameService systemNameService;
    private final LocoBlockService locoBlockService;
    private final LocoListService locoListService;


    @Autowired
    public InstallBlocksTemController(BlockOnLocoService blockOnLocoService, TypeLocoService typeLocoService,
                                      RegionService regionService, HomeDepotService homeDepotService, SystemNameService systemNameService, LocoBlockService locoBlockService, LocoListService locoListService) {
        this.blockOnLocoService = blockOnLocoService;
        this.typeLocoService = typeLocoService;
        this.regionService = regionService;
        this.homeDepotService = homeDepotService;
        this.systemNameService = systemNameService;
        this.locoBlockService = locoBlockService;
        this.locoListService = locoListService;
    }

    @GetMapping("/install-block")
    public String showCreateBlockForm(@RequestParam("typeSystem") String typeSystem,
                                      @RequestParam("sectionNumber") String sectionNumber,
                                      @RequestParam("typeLoco") String typeLoco,
                                      @RequestParam("numberLoco") String numberLoco,
                                      @RequestParam("typeLocoUnit") String typeLocoUnit,
                                      Model model) {
        // Создаем список новых объектов DTO для блоков
        List<BlockOnLocoDTO> blocksOnLocoDTO = new ArrayList<>();

        // Фильтруем блоки по выбранному типу системы и добавляем в модель
        List<String> blocks = locoBlockService.getBlocksBySystemType(typeSystem);
        model.addAttribute("blocksList", blocks);


        // Добавляем список новых объектов для заполнения формы
        model.addAttribute("block", blocksOnLocoDTO);

        model.addAttribute("sectionNumber", sectionNumber);
        model.addAttribute("typeLoco", typeLoco);
        model.addAttribute("numberLoco", numberLoco);
        model.addAttribute("typeLocoUnit", typeLocoUnit);

        return "install-block_1_create"; // Шаблон для создания блока
    }

    @PostMapping("/block-on-locos/create-multiple")
    public String createBlocks(
            @RequestParam(required = false) String typeSystem,
            @RequestParam(required = false) String sectionNumber,
            @RequestParam(required = false) String typeLoco,
            @RequestParam(required = false) String numberLoco,
            @RequestParam Map<String, String> parameters,
            @RequestParam String typeLocoUnit,
            Model model) {

        // Проверка существования секции
        boolean locoSectionExists = locoListService.ifSectionExist(typeLoco, sectionNumber);

        if (!locoSectionExists) {
            model.addAttribute("errorMessage", "Секции не существует");
            return "install-block_2_create_end"; // Возвращаем страницу с сообщением об ошибке
        }

        // Логика создания блоков
        try {
            int createdBlocksCount = 0;
            SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd.MM.yyyy");
            for (int i = 0; i < 10; i++) { // Максимум 10 блоков
                String blockName = parameters.get("blocks[" + i + "].blockName");
                String blockNumber = parameters.get("blocks[" + i + "].blockNumber");
                String dateOfIssue = parameters.get("blocks[" + i + "].dateOfIssue");

                if (blockName != null && !blockName.isEmpty() &&
                        blockNumber != null && !blockNumber.isEmpty() &&
                        dateOfIssue != null && !dateOfIssue.isEmpty()) {

                    // Преобразование даты так как со страницы она приходит в гггг.мм.дд а в bloc_on_loco она в дд.мм.гггг
                    String formattedDateOfIssue = convertDateFormat(dateOfIssue, inputDateFormat, outputDateFormat);

                    BlockOnLocoDTO blockDTO = new BlockOnLocoDTO(
                            null, // id можно оставить как null, если он не используется
                            blockName,
                            blockNumber,
                            formattedDateOfIssue,
                            null, // locoListId можно оставить как null, если он не используется
                            typeLoco,
                            sectionNumber
                    );
                    blockOnLocoService.createBlockOnLoco(blockDTO);
                    createdBlocksCount++;
                }
            }

            if (createdBlocksCount > 0) {
                model.addAttribute("successMessage", createdBlocksCount + " блок(а/ов) успешно созданы");
            } else {
                model.addAttribute("errorMessage", "Не создано ни одного блока. Пожалуйста, заполните все поля.");
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Произошла ошибка при создании блоков: " + e.getMessage());
        }

        // Передаем параметры для формы возврата
        model.addAttribute("typeLoco", typeLoco);
        model.addAttribute("numberLoco", numberLoco);
        model.addAttribute("typeLocoUnit", typeLocoUnit);

        return "install-block_2_create_end"; // Возвращаем страницу с результатом
    }

    private String convertDateFormat(String dateStr, SimpleDateFormat inputFormat, SimpleDateFormat outputFormat) {
        try {
            Date date = inputFormat.parse(dateStr);
            return outputFormat.format(date);
        } catch (ParseException e) {
            // Логирование ошибки преобразования
            e.printStackTrace();
            return dateStr; // Возвращаем исходное значение, если произошла ошибка
        }
    }
}
