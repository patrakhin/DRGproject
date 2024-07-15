package com.drgproject.repair.controller;

import com.drgproject.repair.dto.BlockRemovalDto;
import com.drgproject.repair.dto.HomeDepotDTO;
import com.drgproject.repair.dto.RegionDTO;
import com.drgproject.repair.service.BlockRemovalService;
import com.drgproject.repair.service.HomeDepotService;
import com.drgproject.repair.service.RegionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Controller
@RequestMapping("/block_removal")
public class BlockRemovalController {

    private final BlockRemovalService blockRemovalService;
    private final RegionService regionService;
    private final HomeDepotService homeDepotService;

    public BlockRemovalController(BlockRemovalService blockRemovalService, RegionService regionService, HomeDepotService homeDepotService) {
        this.blockRemovalService = blockRemovalService;
        this.regionService = regionService;
        this.homeDepotService = homeDepotService;
    }

    @GetMapping()
    public String getBlockRemovalPage() {
        return "block_removal_1_main"; // Главная страница
    }

    @GetMapping("/all")
    public String getAllBlockRemovalPage(Model model, HttpSession session) {
        String post = (String) session.getAttribute("post");
        List<BlockRemovalDto> blockRemovalDtos = Collections.emptyList();
        if ("Администратор".equals(post)) {
            blockRemovalDtos = blockRemovalService.getAllBlockRemoval();
        } else if ("Регионал".equals(post)) {
            String region = (String) session.getAttribute("region");
            blockRemovalDtos = blockRemovalService.getBlockRemovalByRegion(region);
        }
        model.addAttribute("blockRemovalDtos", blockRemovalDtos);
        return "block_removal_2_list";
    }

    @GetMapping("/byRegion/{region}")
    public String getBlockRemovalByRegion(@PathVariable String region, Model model) {
        List<BlockRemovalDto> blockRemovalDtos = blockRemovalService.getBlockRemovalByRegion(region);
        model.addAttribute("blockRemovalDtos", blockRemovalDtos);
        return "block_removal_2_list";
    }

    @GetMapping("/byTypeAndNumber")
    public String getBlockRemovalByTypeLocoAndNumberLoco(@RequestParam String typeLoco, @RequestParam String numberLoco, Model model) {
        List<BlockRemovalDto> blockRemovalDtos = blockRemovalService.getBlockRemovalByTypeLocoAndNumberLoco(typeLoco, numberLoco);
        model.addAttribute("blockRemovalDtos", blockRemovalDtos);
        return "block_removal_2_list";
    }

    @GetMapping("/bySystemTypeAndBlock")
    public String getBlockRemovalBySystemTypeAndBlockNameAndBlockNumber(@RequestParam String systemType, @RequestParam String blockName, @RequestParam String blockNumber, Model model) {
        BlockRemovalDto blockRemovalDto = blockRemovalService.getBlockRemovalBySystemTypeAndBlockNameAndBlockNumber(systemType, blockName, blockNumber);
        model.addAttribute("blockRemovalDto", blockRemovalDto);
        return "block_removal_3_detail";
    }

    @GetMapping("/create")
    public String showCreateBlockRemovalForm(Model model) {
        List<RegionDTO> regions = regionService.getAllRegions();
        model.addAttribute("regions", regions);
        model.addAttribute("blockRemovalDto", new BlockRemovalDto());
        return "block_removal_4_create";
    }

    @PostMapping("/create")
    public String createBlockRemoval(@ModelAttribute BlockRemovalDto blockRemovalDto) {
        blockRemovalService.createBlockRemoval(blockRemovalDto);
        return "redirect:/block_removal/all";
    }

    @PostMapping("/removeFromLoco")
    public String blockRemovalFromLoco(@ModelAttribute BlockRemovalDto blockRemovalDto) {
        blockRemovalService.blockRemovalFromLoco(blockRemovalDto);
        return "redirect:/block_removal/all";
    }

    @PostMapping("/cancelRemoval")
    public String cancelBlockRemovalFromLoco(@ModelAttribute BlockRemovalDto blockRemovalDto) {
        blockRemovalService.cancelBlockRemovalFromLoco(blockRemovalDto);
        return "redirect:/block_removal/all";
    }

    @GetMapping("/homeDepots/{region}")
    @ResponseBody
    public List<HomeDepotDTO> getHomeDepotsByRegion(@PathVariable String region) {
        return homeDepotService.getDepotsByRegion(region);
    }
}
