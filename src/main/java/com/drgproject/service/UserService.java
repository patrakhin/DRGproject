package com.drgproject.service;

import com.drgproject.dto.UserDTO;
import com.drgproject.entity.User;
import com.drgproject.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //Для админа
    @Transactional
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(this::convertToDTO).toList();
    }

    //Для регионала
    @Transactional
    public List<UserDTO> getUsersByRegion(String region) {
        return userRepository.findAll().stream()
                .filter(employee -> Objects.equals(employee.getRegion(), region))
                .map(this::convertToDTO)
                .toList();
    }

    //Для регионала
    @Transactional
    public UserDTO getUserByRegionAndNumberTable(String region, String numberTable){
        return userRepository.findUserByRegionAndNumberTable(region, numberTable).map(this::convertToDTO).orElse(null);
    }

    //Для остальных
    @Transactional
    public List<UserDTO> getUsersByRegionAndDepot(String region, String depot) {
        return userRepository.findAll().stream()
                .filter(employee -> Objects.equals(employee.getRegion(), region))
                .filter(employee -> Objects.equals(employee.getUnit(), depot))
                .map(this::convertToDTO)
                .toList();
    }

    @Transactional
    public UserDTO getUserById(long id) {
        return userRepository.findById(id).map(this::convertToDTO).orElse(null);
    }

    @Transactional
    public UserDTO getUserByNumberTable(String numberTable) {
        return userRepository.findByNumberTable(numberTable).map(this::convertToDTO).orElse(null);
    }

    @Transactional
    public UserDTO createUser(UserDTO userDTO) {
        User user = convertToEntity(userDTO);
        user = userRepository.save(user);
        return convertToDTO(user);
    }

    @Transactional
    public UserDTO updateUser(long id, UserDTO userDTO) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
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

    private UserDTO convertToDTO(User user) {
        UserDTO userDTO = new UserDTO();
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

    private User convertToEntity(UserDTO userDTO) {
        User user = new User();
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
