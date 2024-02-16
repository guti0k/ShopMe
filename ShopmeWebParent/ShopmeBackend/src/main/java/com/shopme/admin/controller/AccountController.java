package com.shopme.admin.controller;

import com.shopme.admin.security.ShopmeUserDetails;
import com.shopme.admin.user.UserService;
import com.shopme.admin.utils.FileUploadUtil;
import com.shopme.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
public class AccountController {
    @Autowired
    private UserService userService;

    @GetMapping(value = "/account")
    public String viewDetails(@AuthenticationPrincipal ShopmeUserDetails loggerUser, Model model) {
        String email = loggerUser.getUsername();
        User user = userService.getByEmail(email);

        model.addAttribute("user", user);
        model.addAttribute("pageTitle", "Your Account Details");
        return "users/account_form";
    }

    @PostMapping("/account/update")
    public String saveUserDetails(@AuthenticationPrincipal ShopmeUserDetails loggerUser, User user, RedirectAttributes redirectAttributes, @RequestParam("image") MultipartFile multipartFile) throws IOException {

        if(!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

            user.setPhotos(fileName);
            User userSave = userService.updateAccount(user);

            String uploadDir = "user-photos/" + userSave.getId();

            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        }
        else {
            if(user.getPhotos().isEmpty()) {
                user.setPhotos(null);
            }
            userService.updateAccount(user);
        }

        loggerUser.getUser().setFirstName(user.getFirstName());
        loggerUser.getUser().setLastName(user.getLastName());

        redirectAttributes.addFlashAttribute("message", "The user has been saved updated.");
        return "redirect:/account";
    }

}
