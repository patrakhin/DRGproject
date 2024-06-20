package com.drgproject.repair.service;

import com.drgproject.repair.dto.SystemNameDTO;
import com.drgproject.repair.entity.SystemName;
import com.drgproject.repair.repository.SystemNameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SystemNameService {


    private final SystemNameRepository systemNameRepository;

    public SystemNameService(SystemNameRepository systemNameRepository) {
        this.systemNameRepository = systemNameRepository;
    }

    public List<SystemNameDTO> getAllSystemNames() {
        return systemNameRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public SystemNameDTO getSystemNameById(Long id) {
        Optional<SystemName> systemName = systemNameRepository.findById(id);
        return systemName.map(this::convertToDTO).orElse(null);
    }

    public SystemNameDTO createSystemName(SystemNameDTO systemNameDTO) {
        SystemName systemName = new SystemName(systemNameDTO.getSysName());
        systemName = systemNameRepository.save(systemName);
        return convertToDTO(systemName);
    }

    public SystemNameDTO updateSystemName(Long id, SystemNameDTO systemNameDTO) {
        Optional<SystemName> optionalSystemName = systemNameRepository.findById(id);
        if (optionalSystemName.isPresent()) {
            SystemName systemName = optionalSystemName.get();
            systemName.setSysName(systemNameDTO.getSysName());
            systemName = systemNameRepository.save(systemName);
            return convertToDTO(systemName);
        }
        return null;
    }

    public boolean deleteSystemName(Long id) {
        if (systemNameRepository.existsById(id)) {
            systemNameRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private SystemNameDTO convertToDTO(SystemName systemName) {
        return new SystemNameDTO(systemName.getId(), systemName.getSysName());
    }
}
