package com.drgproject.repair.service;

import com.drgproject.repair.dto.HomeDepotDTO;
import com.drgproject.repair.entity.HomeDepot;
import com.drgproject.repair.entity.Region;
import com.drgproject.repair.repository.HomeDepotRepository;
import com.drgproject.repair.repository.RegionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class HomeDepotService {

    private final HomeDepotRepository homeDepotRepository;
    private final RegionRepository regionRepository;

    public HomeDepotService(HomeDepotRepository homeDepotRepository, RegionRepository regionRepository) {
        this.homeDepotRepository = homeDepotRepository;
        this.regionRepository = regionRepository;
    }

    @Transactional
    public List<HomeDepotDTO> getAllHomeDepots() {
        return homeDepotRepository.findAll().stream().map(this::convertToDTO).toList();
    }

    @Transactional
    public HomeDepotDTO getHomeDepotById(Long id) {
        Optional<HomeDepot> homeDepot = homeDepotRepository.findById(id);
        return homeDepot.map(this::convertToDTO).orElse(null);
    }

    @Transactional
    public String getRegionNameByDepotId(Long depotId) {
        HomeDepot homeDepot = homeDepotRepository.findById(depotId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid depot ID"));
        return homeDepot.getRegion().getName();
    }

    @Transactional
    public List<HomeDepotDTO> getDepotsByRegion(String regionName) {
        Region region = regionRepository.findRegionByName(regionName)
                .orElseThrow(() -> new IllegalArgumentException("Invalid region name"));
        Long regionId = region.getId();
        return homeDepotRepository.findByRegionId(regionId)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }


    @Transactional
    public HomeDepotDTO createHomeDepot(HomeDepotDTO homeDepotDTO) {
        Region region = regionRepository.findById(homeDepotDTO.getRegionId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid region ID"));
        HomeDepot homeDepot = convertToEntity(homeDepotDTO, region);
        HomeDepot savedHomeDepot = homeDepotRepository.save(homeDepot);
        return convertToDTO(savedHomeDepot);
    }

    @Transactional
    public HomeDepotDTO updateHomeDepot(Long id, HomeDepotDTO homeDepotDTO) {
        Optional<HomeDepot> optionalHomeDepot = homeDepotRepository.findById(id);
        Optional<Region> region = regionRepository.findById(homeDepotDTO.getRegionId());
        if (optionalHomeDepot.isPresent() && region.isPresent()) {
            HomeDepot homeDepot = optionalHomeDepot.get();
            homeDepot.setDepot(homeDepotDTO.getDepot());
            homeDepot.setRegion(region.get());
            homeDepot = homeDepotRepository.save(homeDepot);
            return convertToDTO(homeDepot);
        }
        return null;
    }

    @Transactional
    public boolean deleteHomeDepot(Long id) {
        if (homeDepotRepository.existsById(id)) {
            homeDepotRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Transactional
    public List<HomeDepotDTO> getDepotsByRegionId(Long regionId) {
        List<HomeDepot> homeDepots = homeDepotRepository.findByRegionId(regionId);
        return homeDepots.stream().map(this::convertToDTO).toList();
    }


    public HomeDepotDTO convertToDTO(HomeDepot homeDepot) {
        return new HomeDepotDTO(
                homeDepot.getId(),
                homeDepot.getDepot(),
                homeDepot.getRegion().getId(),
                homeDepot.getRegion().getName() // Заполнение regionName
        );
    }

    public HomeDepot convertToEntity(HomeDepotDTO homeDepotDTO, Region region) {
        HomeDepot homeDepot = new HomeDepot();
        homeDepot.setId(homeDepotDTO.getId());
        homeDepot.setDepot(homeDepotDTO.getDepot());
        homeDepot.setRegion(region);
        return homeDepot;
    }
}
