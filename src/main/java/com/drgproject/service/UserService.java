package com.drgproject.service;

import com.drgproject.dto.MemberDTO;
import com.drgproject.entity.Members;
import com.drgproject.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {

    private final MemberRepository userRepository;

    public UserService(MemberRepository userRepository) {
        this.userRepository = userRepository;
    }

    //Для админа
    @Transactional
    public List<MemberDTO> getAllUsers() {
        return userRepository.findAll().stream().map(this::convertToDTO).toList();
    }

    //Для регионала
    @Transactional
    public List<MemberDTO> getUsersByRegion(String region) {
        return userRepository.findAll().stream()
                .filter(employee -> Objects.equals(employee.getRegion(), region))
                .map(this::convertToDTO)
                .toList();
    }

    //Для регионала
    @Transactional
    public MemberDTO getUserByRegionAndNumberTable(String region, String numberTable){
        return userRepository.findUserByRegionAndNumberTable(region, numberTable).map(this::convertToDTO).orElse(null);
    }

    //Для остальных
    @Transactional
    public List<MemberDTO> getUsersByRegionAndDepot(String region, String depot) {
        return userRepository.findAll().stream()
                .filter(employee -> Objects.equals(employee.getRegion(), region))
                .filter(employee -> Objects.equals(employee.getUnit(), depot))
                .map(this::convertToDTO)
                .toList();
    }

    @Transactional
    public MemberDTO getUserById(long id) {
        return userRepository.findById(id).map(this::convertToDTO).orElse(null);
    }

    @Transactional
    public MemberDTO getUserByNumberTable(String numberTable) {
        return userRepository.findByNumberTable(numberTable).map(this::convertToDTO).orElse(null);
    }

    @Transactional
    public MemberDTO createUser(MemberDTO userDTO) {
        Members user = convertToEntity(userDTO);
        user = userRepository.save(user);
        return convertToDTO(user);
    }

    @Transactional
    public MemberDTO updateUser(long id, MemberDTO userDTO) {
        Optional<Members> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            Members user = optionalUser.get();
            user.setFio(userDTO.getFio());
            user.setPost(userDTO.getPost());
            user.setUnit(userDTO.getUnit());
            user.setRegion(userDTO.getRegion());
            user.setNumberTable(userDTO.getNumberTable());
            user.setLogin(userDTO.getLogin());
            user.setPassword(userDTO.getPassword());
            user = userRepository.save(user);
            return convertToDTO(user);
        }
        return null;
    }

    @Transactional
    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public void deleteUserByNumberTable(String numberTable) {
        userRepository.deleteById(getUserByNumberTable(numberTable).getId());
    }

    private MemberDTO convertToDTO(Members user) {
        MemberDTO userDTO = new MemberDTO();
        userDTO.setId(user.getId());
        userDTO.setFio(user.getFio());
        userDTO.setPost(user.getPost());
        userDTO.setUnit(user.getUnit());
        userDTO.setRegion(user.getRegion());
        userDTO.setNumberTable(user.getNumberTable());
        userDTO.setLogin(user.getLogin());
        userDTO.setPassword(user.getPassword());
        userDTO.setDateCreate(user.getDateCreate());
        return userDTO;
    }

    private Members convertToEntity(MemberDTO userDTO) {
        Members user = new Members();
        user.setFio(userDTO.getFio());
        user.setPost(userDTO.getPost());
        user.setUnit(userDTO.getUnit());
        user.setRegion(userDTO.getRegion());
        user.setNumberTable(userDTO.getNumberTable());
        user.setLogin(userDTO.getLogin());
        user.setPassword(userDTO.getPassword());
        return user;
    }
}
