package com.drgproject.service;


import com.drgproject.dto.LocomotiveDto;
import com.drgproject.entity.Locomotive;
import com.drgproject.repository.LocomotiveRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class LocomotiveService {

    private final LocomotiveRepository locomotiveRepository;

    public LocomotiveService(LocomotiveRepository locomotiveRepository) {
        this.locomotiveRepository = locomotiveRepository;
    }

    @Transactional
    public List<LocomotiveDto> getAllLocomotive(){
        return locomotiveRepository.findAll().stream().map(this::convertToDto).toList();
    }

    @Transactional
    public LocomotiveDto getLocomotiveById(long id){
        return locomotiveRepository.findById(id).map(this::convertToDto).orElse(null);
    }

    @Transactional
    public List<LocomotiveDto> getLocomotiveByNumber(String locomotiveNumber){
        return locomotiveRepository.findLocomotiveByLocomotiveNumber(locomotiveNumber).stream()
                .map(this::convertToDto)
                .toList();
    }

    @Transactional
    public LocomotiveDto getLocomotiveByNumberAndSection(String locomotiveNumber, String section){
        return locomotiveRepository.findLocomotiveByLocomotiveNumberAndSection(locomotiveNumber, section)
                .map(this::convertToDto)
                .orElse(null);
    }

    @Transactional
    public List<LocomotiveDto> getLocomotiveBySystemType(String systemType){
        return locomotiveRepository.findLocomotiveBySystemType(systemType).stream()
                .map(this::convertToDto)
                .toList();
    }

    @Transactional
    public List<LocomotiveDto> getLocomotiveByHomeDepot(String homeDepot){
        return locomotiveRepository.findLocomotiveByHomeDepot(homeDepot).stream()
                .map(this::convertToDto)
                .toList();
    }

    @Transactional
    public LocomotiveDto createLocomotive(LocomotiveDto locomotiveDto){
        Locomotive locomotive = convertToEntity(locomotiveDto);
        locomotive = locomotiveRepository.save(locomotive);
        return convertToDto(locomotive);
    }

    @Transactional
    public LocomotiveDto updateLocomotive(long id, LocomotiveDto locomotiveDto){
        Optional<Locomotive> locomotiveOptional = locomotiveRepository.findById(id);
        if(locomotiveOptional.isPresent()){
            Locomotive locomotive = locomotiveOptional.get();
            locomotive.setLocomotiveNumber(locomotiveDto.getLocomotiveNumber());
            locomotive.setSection(locomotiveDto.getSection());
            locomotive.setSystemType(locomotiveDto.getSystemType());
            locomotive.setHomeDepot(locomotiveDto.getHomeDepot());
            locomotiveRepository.save(locomotive);
            return convertToDto(locomotive);
        }
        return null;
    }

    @Transactional
    public void deleteLocomotive(long id){
        locomotiveRepository.deleteById(id);
    }

    public void deleteLocomotiveByNumberAndSection(String locomotiveNumber, String section){
        locomotiveRepository.deleteLocomotiveByLocomotiveNumberAndSection(locomotiveNumber, section);
    }

    private LocomotiveDto convertToDto(Locomotive locomotive){
        LocomotiveDto locomotiveDto = new LocomotiveDto();
        locomotiveDto.setId(locomotive.getId());
        locomotiveDto.setLocomotiveNumber(locomotive.getLocomotiveNumber());
        locomotiveDto.setSection(locomotive.getSection());
        locomotiveDto.setSystemType(locomotive.getSystemType());
        locomotiveDto.setHomeDepot(locomotive.getHomeDepot());
        locomotiveDto.setDateCreate(locomotive.getDateCreate());
        locomotiveDto.setBlocks(locomotive.getBlocks());
        locomotiveDto.setRepairHistories(locomotive.getRepairHistories());
        return locomotiveDto;
    }

    private Locomotive convertToEntity(LocomotiveDto locomotiveDto){
        Locomotive locomotive = new Locomotive();
        locomotive.setLocomotiveNumber(locomotiveDto.getLocomotiveNumber());
        locomotive.setSection(locomotiveDto.getSection());
        locomotive.setSystemType(locomotiveDto.getSystemType());
        locomotive.setHomeDepot(locomotiveDto.getHomeDepot());
        return locomotive;
    }

}
