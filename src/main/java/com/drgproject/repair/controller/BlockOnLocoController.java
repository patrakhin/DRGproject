package com.drgproject.repair.controller;

import com.drgproject.repair.dto.BlockOnLocoDTO;
import com.drgproject.repair.service.BlockOnLocoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/block-on-locos")
public class BlockOnLocoController {

    private final BlockOnLocoService blockOnLocoService;

    @Autowired
    public BlockOnLocoController(BlockOnLocoService blockOnLocoService) {
        this.blockOnLocoService = blockOnLocoService;
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

    @GetMapping("/create")
    public String showCreateBlockForm(Model model) {
        model.addAttribute("block", new BlockOnLocoDTO());
        return "block_on_locos_3_create";
    }

    @PostMapping("/create")
    public String createBlockOnLoco(@ModelAttribute BlockOnLocoDTO blockOnLocoDTO, Model model) {
        blockOnLocoService.createBlockOnLoco(blockOnLocoDTO);
        model.addAttribute("block", blockOnLocoDTO);
        return "block_on_locos_3_end_create";
    }

    @GetMapping("/update/{id}")
    public String showUpdateBlockForm(@PathVariable Long id, Model model) {
        BlockOnLocoDTO blockOnLoco = blockOnLocoService.getBlockOnLocoById(id);
        model.addAttribute("block", blockOnLoco);
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
        blockOnLocoService.deleteBlockOnLocoByBlockNameAneBlockNumber(blockName, blockNumber);
        return "redirect:/block-on-locos/all";
    }

    @GetMapping("/search")
    public String showSearchForm() {
        return "block_on_locos_6_search";
    }

    @PostMapping("/search")
    public String searchBlockOnLoco(@RequestParam String locoNumber, @RequestParam String typeLoco, Model model) {
        BlockOnLocoDTO blockOnLoco = blockOnLocoService.getBlockOnLocoByLocoNumberAndTypeLoco(locoNumber, typeLoco);
        model.addAttribute("block", blockOnLoco);
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
