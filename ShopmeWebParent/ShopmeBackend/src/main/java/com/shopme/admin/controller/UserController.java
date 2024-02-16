package com.shopme.admin.controller;

import com.shopme.admin.security.exception.UserNotFoundException;
import com.shopme.admin.user.UserService;
import com.shopme.admin.utils.FileUploadUtil;
import com.shopme.admin.export.UserCsvExporter;
import com.shopme.admin.export.UserExcelExporter;
import com.shopme.admin.export.UserPdfExporter;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping("/login")
    public String viewLoginPage() {
        return "login";
    }

    @GetMapping("/users")
    public String listFirstPage(Model model) {

        return listByPage(1, "id", "asc", null, model);
    }

    @GetMapping("/users/page/{pageNumber}")
    public String listByPage(@PathVariable("pageNumber") Integer pageNumber, @Param("sortField") String sortField,
            @Param("sortDir") String sortDir, @Param("keyword") String keyword, Model model) {

        Page<User> page = userService.listByPage(pageNumber, sortField, sortDir, keyword);
        List<User> listUsers = page.getContent();
        long totalItems = page.getTotalElements();

//      vị trí user bắt đầu và kết thúc của 1 page
        int startCount = (pageNumber - 1) * UserService.USERS_PER_PAGE  + 1;
        long endCount = startCount + UserService.USERS_PER_PAGE - 1;
        endCount = endCount > page.getTotalElements() ? page.getTotalElements() : endCount;

        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("listUsers", listUsers);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", reverseSortDir);
        model.addAttribute("keyword", keyword);

        return "users/users";
    }

    @GetMapping("/users/new")
    public String newUser(Model model) {

        User user = new User();
        user.setEnabled(true);
        model.addAttribute("user", user);
        model.addAttribute("pageTitle", "Create New Users");

        List<Role> listRoles = userService.listRoles();
        model.addAttribute("listRoles", listRoles);

        return "users/user_form";
    }

    @PostMapping("/users/save")
    public String saveUser(User user, RedirectAttributes redirectAttributes, @RequestParam("image") MultipartFile multipartFile) throws IOException {

        if(!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

            user.setPhotos(fileName);
            User userSave = userService.save(user);

            String uploadDir = "user-photos/" + userSave.getId();

            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        }
        else {
            if(user.getPhotos().isEmpty()) {
                user.setPhotos(null);
            }
            userService.save(user);
        }

        redirectAttributes.addFlashAttribute("message", "The user has been saved successfully.");
        return getRedirectURLtoAffectedUser(user);
    }

    private static String getRedirectURLtoAffectedUser(User user) {
        String firstPartOfEmail = user.getEmail().split("@")[0];
        return "redirect:/users/page/1?sortField=id&sortDir=asc&keyword=" + firstPartOfEmail;
    }

    @GetMapping("/users/edit/{id}")
    public String editUser(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes, Model model) throws UserNotFoundException {
        try {
            User user = userService.getUser(id);
            List<Role> listRoles = userService.listRoles();

            model.addAttribute("user", user);
            model.addAttribute("listRoles", listRoles);
            model.addAttribute("pageTitle", "Edit User (ID : " + id + ")" );
            return "users/user_form";
        }
        catch (UserNotFoundException exception) {
            redirectAttributes.addFlashAttribute("message", exception.getMessage());
            return "redirect:/users";
        }
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {

        try {
           userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("message", "The user ID " + id + " has been delete success");

        } catch (UserNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
        }
        return "redirect:/users";

    }
    @GetMapping("/users/{id}/enabled/{status}")
    public String updateUserEnableStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean enabled, RedirectAttributes redirectAttributes) {
        userService.updateUserEnabledStatus(id, enabled);

        String status = enabled ? "enabled" : "disabled";
        String message = "The user ID " + id + " has been " + status;

        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/users";
    }

    @GetMapping("/users/export/csv")
    public void exportToCSV(HttpServletResponse httpServletResponse) throws IOException {
        List<User> users = userService.listAll();

        UserCsvExporter userCsvExporter = new UserCsvExporter();
        userCsvExporter.export(users, httpServletResponse);
    }

    @GetMapping("/users/export/excel")
    public void exportToExcel(HttpServletResponse httpServletResponse) throws IOException {
        List<User> users = userService.listAll();

        UserExcelExporter userExcelExporter = new UserExcelExporter();
        userExcelExporter.export(users, httpServletResponse);
    }

    @GetMapping("/users/export/pdf")
    public void exportToPDF(HttpServletResponse httpServletResponse) throws IOException {
        List<User> users = userService.listAll();

        UserPdfExporter userPdfExporter = new UserPdfExporter();
        userPdfExporter.export(users, httpServletResponse);
    }
}
