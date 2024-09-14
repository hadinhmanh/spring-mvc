package vn.hoidanit.laptopshop.controller.admin;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.service.UploadService;
import vn.hoidanit.laptopshop.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.ServletContext;

@Controller
public class UserController {

    private UserService userService;
    private UploadService uploadService;

    public UserController(UserService userService, UploadService uploadService) {
        this.userService = userService;
        this.uploadService = uploadService;
    }

    // Home
    @RequestMapping("/")
    public String getHomePage(Model model) {
        model.addAttribute("manh", "Tôi là Hà Đình Mạnh");
        return "hello";
    }

    // Bảng thông tin user
    @RequestMapping("/admin/user")
    public String getUserPage(Model model) {
        List<User> arrUsers = this.userService.getAllUsers();
        model.addAttribute("arrUsers", arrUsers);
        return "admin/user/Show";
    }

    // Thông tin 1 user
    @RequestMapping("/admin/user/{id}")
    public String getUserDetailPage(Model model, @PathVariable long id) {
        User user = this.userService.getUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("id", id);
        return "admin/user/Detail";
    }

    // Thêm mới user view
    @GetMapping("/admin/user/create")
    public String getCreateUserPage(Model model) {
        model.addAttribute("newUser", new User());
        return "admin/user/Create";
    }

    // Xử lý thêm mới 1 user
    @PostMapping("/admin/user/create")
    public String createUserPage(Model model,
            @ModelAttribute("newUser") User manh,
            @RequestParam("hamanh") MultipartFile file) {
        // this.userService.handleSaveUser(manh);
        String avatar = this.uploadService.handleSaveUploadFile(file, "avatar");
        return "redirect:/admin/user";
    }

    // Update user view
    @RequestMapping("/admin/user/update/{id}")
    public String getUpdateUserPage(Model model, @PathVariable long id) {
        User user = this.userService.getUserById(id);
        model.addAttribute("newUser", user);
        return "admin/user/Update";
    }

    // Handle update user
    @PostMapping("/admin/user/update")
    public String postUpdateUser(Model model, @ModelAttribute("newUser") User manh) {
        User currentUser = this.userService.getUserById(manh.getId());
        if (currentUser != null) {
            currentUser.setAddress(manh.getAddress());
            currentUser.setFullName(manh.getFullName());
            currentUser.setPhone(manh.getPhone());

            this.userService.handleSaveUser(currentUser);
        }
        return "redirect:/admin/user";
    }

    // Delete user view
    @GetMapping("/admin/user/delete/{id}")
    public String getDeleteUserPage(Model model, @PathVariable long id) {
        model.addAttribute("id", id);
        model.addAttribute("newUser", new User());
        return "admin/user/Delete";
    }

    @PostMapping("/admin/user/delete")
    public String postDeleteUser(Model model, @ModelAttribute("newUser") User manh) {
        User currentUser = this.userService.getUserById(manh.getId());
        this.userService.deleteUserById(manh.getId());
        return "redirect:/admin/user";
    }

}
