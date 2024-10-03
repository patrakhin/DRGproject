package com.drgproject.controller;

import com.drgproject.dto.MemberDTO;
import com.drgproject.repair.dto.RepDepotDTO;
import com.drgproject.repair.dto.RegionDTO;
import com.drgproject.repair.service.RegionService;
import com.drgproject.repair.service.RepDepotService;
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

    private static final String ADMIN = "Администратор";
    private static final String MANAGER = "Регионал";
    private static final String REGION = "region";
    private static final String ERROR_MESSAGE = "errorMessage";

    private final UserService userService;
    private final RepDepotService repDepotService;
    private final RegionService regionService;

    public MemberController(UserService userService,
                            RepDepotService repDepotService,
                            RegionService regionService) {
        this.userService = userService;
        this.repDepotService = repDepotService;
        this.regionService = regionService;
    }

    @GetMapping()
    public String getUsersPage() {
        return "user_company_1_main";
    }

    @GetMapping("/all")
    public String getAllUsersPage(Model model, HttpSession session) {
        String region = (String) session.getAttribute(REGION);
        String post = (String) session.getAttribute("post");
        List<MemberDTO> users = Collections.emptyList();
        if(ADMIN.equals(post)){
            users = userService.getAllUsers();
        }
        if(MANAGER.equals(post)){
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
        String region = (String) session.getAttribute(REGION);
        String post = (String) session.getAttribute("post");
        MemberDTO userDTO = userService.getUserByNumberTable(number);

        if (userDTO != null && ADMIN.equals(post)) {
            model.addAttribute("user", userDTO);
        } else if (userDTO != null && MANAGER.equals(post)) {
            userDTO= userService.getUserByRegionAndNumberTable(region, number);
            model.addAttribute("user", userDTO);
        } else {
            model.addAttribute(ERROR_MESSAGE, "Сотрудник с таким табельным номером не найден");
        }
        return "user_company_3_search"; // Имя шаблона Thymeleaf без .html
    }

    // Метод для отображения формы создания сотрудника
    @GetMapping("/create")
    public String showCreateUserForm(Model model) {
        MemberDTO userDTO = new MemberDTO();

        // Получаем список всех регионов
        List<String> regions = regionService.getAllRegions().stream()
                .map(RegionDTO::getName)
                .toList();
        model.addAttribute("regions", regions);  // Добавляем список регионов в модель

        model.addAttribute("user", userDTO);  // Добавляем DTO пользователя
        return "user_company_4_add";  // Возвращаем имя шаблона
    }

    // Метод для обработки данных формы создания сотрудника
    @PostMapping("/create")
    public String createUser(@ModelAttribute MemberDTO userDTO, Model model) {
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
        } else {
            model.addAttribute(ERROR_MESSAGE, "Сотрудник с таким ID не найден");
        }
        return "user_company_5_update";
    }

    // Метод для обработки данных формы
    @PostMapping("/edit/{id}")
    public String updateUser(@PathVariable long id, @ModelAttribute MemberDTO userDTO, Model model) {
        MemberDTO updatedUser = userService.updateUser(id, userDTO);
        if (updatedUser != null) {
            model.addAttribute("updatedUser", updatedUser);
            return "user_company_5_update_success";
        } else {
            model.addAttribute(ERROR_MESSAGE, "Не удалось обновить данные сотрудника");
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
        String region = (String) session.getAttribute(REGION);
        try {
            if (ADMIN.equals(post)) {
                userService.deleteUserByNumberTable(numTable);
                model.addAttribute("successMessage", "Сотрудник успешно удален");
            } else if (MANAGER.equals(post)) {
                MemberDTO user = userService.getUserByNumberTable(numTable);
                if (user != null && region.equals(user.getRegion())) {
                    userService.deleteUserByNumberTable(numTable);
                    model.addAttribute("successMessage", "Сотрудник успешно удален");
                } else {
                    model.addAttribute(ERROR_MESSAGE, "У вас нет прав на удаление этого сотрудника");
                }
            } else {
                model.addAttribute(ERROR_MESSAGE, "Неверная роль пользователя");
            }
        } catch (Exception e) {
            model.addAttribute(ERROR_MESSAGE, "Ошибка при удалении сотрудника");
        }
        return "user_company_6_delete";
    }

    @GetMapping("/region")
    @ResponseBody
    public List<RepDepotDTO> getDepotsByRegion(@RequestParam String region) {
        return repDepotService.getDepotsByRegionName(region);
    }

}
