package com.drgproject.repair.controller;

import com.drgproject.repair.dto.*;
import com.drgproject.repair.service.*;
import com.drgproject.service.LocoBlockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/block-on-locos")
public class BlockOnLocoController {

    public static final String BLOCK = "block";
    private final BlockOnLocoService blockOnLocoService;
    private final TypeLocoService typeLocoService;
    private final SystemNameService systemNameService;
    private final LocoBlockService locoBlockService;
    private final LocoListService locoListService;

    @Autowired
    public BlockOnLocoController(BlockOnLocoService blockOnLocoService,
                                 TypeLocoService typeLocoService,
                                 SystemNameService systemNameService,
                                 LocoBlockService locoBlockService,
                                 LocoListService locoListService) {
        this.blockOnLocoService = blockOnLocoService;
        this.typeLocoService = typeLocoService;
        this.systemNameService = systemNameService;
        this.locoBlockService = locoBlockService;
        this.locoListService = locoListService;
    }

    @GetMapping
    public String getHomeDepotsPage() {
        return "block_on_locos_1_main";
    }

    @GetMapping("/all")
    public String getAllBlockOnLocos(Model model) {
        List<BlockOnLocoDTO> blockOnLocos = blockOnLocoService.getAllBlockOnLocos();
        model.addAttribute("blocks", blockOnLocos);
        return "block_on_locos_2_all";
    }

    @GetMapping("/select-system")
    public String showSelectSystemForm(Model model) {
        // Получение всех типов систем и добавление их в модель
        List<SystemNameDTO> systemNames = systemNameService.getAllSystemNames();
        model.addAttribute("systemNames", systemNames);
        model.addAttribute("blockForm", new BlockOnLocoFormDTO());
        return "block_on_locos_3_middle_create"; // Шаблон для выбора системы
    }

    @GetMapping("/create")
    public String showCreateBlockForm(@RequestParam("typeSystem") String typeSystem, Model model) {
        // Создаем новый объект DTO для блока
        BlockOnLocoDTO blockOnLocoDTO = new BlockOnLocoDTO();
        // Фильтруем блоки по выбранному типу системы и добавляем в модель
        List<String> blocks = locoBlockService.getBlocksBySystemType(typeSystem);
        model.addAttribute("blocksList", blocks);
        // Список серий локомотива
        List<TypeLocoDTO> typeLocos = typeLocoService.getAllTypeLocos();
        model.addAttribute("typeLocos", typeLocos);
        // Добавляем объект для заполнения формы
        model.addAttribute(BLOCK, blockOnLocoDTO);
        return "block_on_locos_3_create"; // Шаблон для создания блока
    }

    @PostMapping("/create")
    public String createBlockOnLoco(@ModelAttribute BlockOnLocoDTO blockOnLocoDTO, @ModelAttribute("errorMessage") String errorMessage,Model model) {
        String sectionNumber = blockOnLocoDTO.getLocoNumber();
        String typeLoco = blockOnLocoDTO.getTypeLoco();
        boolean locoSectionExists = locoListService.ifSectionExist(typeLoco, sectionNumber);
        if (!locoSectionExists) {
            model.addAttribute("errorMessage", "Секции не существует");
            return "redirect:/block-on-locos/select-system";
        }
        blockOnLocoService.createBlockOnLoco(blockOnLocoDTO);
        // Добавляем объект для заполнения формы
        model.addAttribute(BLOCK, blockOnLocoDTO);
        return "block_on_locos_3_end_create";
    }

    @GetMapping("/update/{id}")
    public String showUpdateBlockForm(@PathVariable Long id, Model model) {
        BlockOnLocoDTO blockOnLoco = blockOnLocoService.getBlockOnLocoById(id);
        model.addAttribute(BLOCK, blockOnLoco);
        return "block_on_locos_4_update";
    }

    @PostMapping("/update/{id}")
    public String updateBlockOnLoco(@PathVariable Long id, @ModelAttribute BlockOnLocoDTO blockOnLocoDTO) {
        blockOnLocoService.updateBlockOnLoco(id, blockOnLocoDTO);
        return "redirect:/block-on-locos/all";
    }

    @GetMapping("/delete")
    public String showDeleteBlockForm() {
        return "block_on_locos_5_delete";
    }

    @PostMapping("/delete")
    public String deleteBlockOnLoco(@RequestParam String blockName, @RequestParam String blockNumber) {
        blockOnLocoService.deleteBlockOnLocoByBlNameAndBlNumberWithLogging(blockName, blockNumber);
        return "redirect:/block-on-locos/all";
    }

    @PostMapping("/delete/repair")
    public String deleteBlockOnLocoRepair(@RequestParam String blockName, @RequestParam String blockNumber) {
        blockOnLocoService.deleteBlockOnLocoByBlNameAndBlNumberWithLogging(blockName, blockNumber);
        return "redirect:/repair_history/remove_block/prepare";
    }

    @GetMapping("/search")
    public String showSearchForm() {
        return "block_on_locos_6_search";
    }

    @PostMapping("/search")
    public String searchBlockOnLoco(@RequestParam String locoNumber, @RequestParam String typeLoco, Model model) {
        BlockOnLocoDTO blockOnLoco = blockOnLocoService.getBlockOnLocoByLocoNumberAndTypeLoco(locoNumber, typeLoco);
        model.addAttribute(BLOCK, blockOnLoco);
        return "block_on_locos_7_view";
    }

    @GetMapping("/search-by-loco")
    public String showSearchByLocoForm() {
        return "block_on_locos_8_search_by_loco";
    }

    @PostMapping("/search-by-loco")
    public String searchAllBlockOnLocosByLocoNumberAndTypeLoco(@RequestParam String locoNumber, @RequestParam String typeLoco, Model model) {
        List<BlockOnLocoDTO> blockOnLocos = blockOnLocoService.getAllBlockOnLocoByLocoNumberAndTypeLoco(locoNumber, typeLoco);
        model.addAttribute("blocks", blockOnLocos);
        return "block_on_locos_9_list_by_loco";
    }
}
