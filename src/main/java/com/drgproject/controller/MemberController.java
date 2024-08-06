package com.drgproject.controller;

import com.drgproject.dto.MemberDTO;
import com.drgproject.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Controller
@RequestMapping("/users")
public class MemberController {

    private final UserService userService;

    public MemberController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String getUsersPage() {
        return "user_company_1_main"; // Имя шаблона Thymeleaf без .html
    }

    @GetMapping("/all")
    public String getAllUsersPage(Model model, HttpSession session) {
        String region = (String) session.getAttribute("region");
        String post = (String) session.getAttribute("post");
        //List<UserDTO> users = userService.getAllUsers();
        List<MemberDTO> users = Collections.emptyList();
        if("Администратор".equals(post)){
            users = userService.getAllUsers();
        }
        if("Регионал".equals(post)){
            users = userService.getUsersByRegion(region);
        }
        model.addAttribute("users", users);
        return "user_company_2_list";
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberDTO> getUserById(@PathVariable long id) {
        MemberDTO userDTO = userService.getUserById(id);
        if (userDTO != null) {
            return ResponseEntity.ok(userDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search") //Поиск по табельному №
    public String showSearchPage() {
        return "user_company_3_search";
    }

    @GetMapping("/byNumberTable")
    public String getUserByNumberTable(@RequestParam String number, Model model, HttpSession session) {
        String region = (String) session.getAttribute("region");
        String post = (String) session.getAttribute("post");
        MemberDTO userDTO = userService.getUserByNumberTable(number);

        if (userDTO != null && "Администратор".equals(post)) {
            model.addAttribute("user", userDTO);
        } else if (userDTO != null && "Регионал".equals(post)) {
            userDTO= userService.getUserByRegionAndNumberTable(region, number);
            model.addAttribute("user", userDTO);
        } else {
            model.addAttribute("errorMessage", "Сотрудник с таким табельным номером не найден");
        }
        return "user_company_3_search"; // Имя шаблона Thymeleaf без .html
    }

    // Метод для отображения формы создания сотрудника
    @GetMapping("/create")
    public String showCreateUserForm(Model model, HttpSession session) {
        MemberDTO userDTO = new MemberDTO();
        String post = (String) session.getAttribute("post");
        if ("Регионал".equals(post)){
            String region = (String) session.getAttribute("region");
            userDTO.setRegion(region);
        }
        model.addAttribute("user", userDTO);
        model.addAttribute("post", post); // Добавляем переменную post в модель
        return "user_company_4_add";
    }

    // Метод для обработки данных формы создания сотрудника
    @PostMapping("/create")
    public String createUser(@ModelAttribute MemberDTO userDTO, Model model, HttpSession session) {
        String post = (String) session.getAttribute("post");
        if ("Регионал".equals(post)){
            String region = (String) session.getAttribute("region");
            userDTO.setRegion(region);
        }
        MemberDTO createdUser = userService.createUser(userDTO);
        model.addAttribute("createdUser", createdUser);
        return "user_company_4_add_success";
    }

    // Метод для отображения формы редактирования данных сотрудника
    @GetMapping("/edit")
    public String showEditUserForm(@RequestParam long id, Model model) {
        MemberDTO userDTO = userService.getUserById(id);
        if (userDTO != null) {
            model.addAttribute("user", userDTO);
            return "user_company_5_update";
        } else {
            model.addAttribute("errorMessage", "Сотрудник с таким ID не найден");
            return "user_company_5_update";
        }
    }

    // Метод для обработки данных формы
    @PostMapping("/edit/{id}")
    public String updateUser(@PathVariable long id, @ModelAttribute MemberDTO userDTO, Model model) {
        MemberDTO updatedUser = userService.updateUser(id, userDTO);
        if (updatedUser != null) {
            model.addAttribute("updatedUser", updatedUser);
            return "user_company_5_update_success";
        } else {
            model.addAttribute("errorMessage", "Не удалось обновить данные сотрудника");
            return "user_company_5_update";
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // Метод для отображения страницы удаления сотрудника
    @GetMapping("/delete")
    public String showDeleteUserForm(Model model, HttpSession session) {
        String post = (String) session.getAttribute("post");
        model.addAttribute("post", post);
        return "user_company_6_delete";
    }

    // Метод для обработки удаления сотрудника по табельному номеру
    @PostMapping("/deleteByNumberTable")
    public String deleteUserByNumberTable(@RequestParam String numTable, Model model, HttpSession session) {
        String post = (String) session.getAttribute("post");
        String region = (String) session.getAttribute("region");

        try {
            if ("Администратор".equals(post)) {
                userService.deleteUserByNumberTable(numTable);
                model.addAttribute("successMessage", "Сотрудник успешно удален");
            } else if ("Регионал".equals(post)) {
                MemberDTO user = userService.getUserByNumberTable(numTable);
                if (user != null && region.equals(user.getRegion())) {
                    userService.deleteUserByNumberTable(numTable);
                    model.addAttribute("successMessage", "Сотрудник успешно удален");
                } else {
                    model.addAttribute("errorMessage", "У вас нет прав на удаление этого сотрудника");
                }
            } else {
                model.addAttribute("errorMessage", "Неверная роль пользователя");
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Ошибка при удалении сотрудника");
        }

        return "user_company_6_delete";
    }

}
