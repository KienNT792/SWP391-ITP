package fpt.swp.springmvctt.itp.controller;

import fpt.swp.springmvctt.itp.dto.request.UserCreationRequest;
import fpt.swp.springmvctt.itp.dto.request.UserLoginRequest;
import fpt.swp.springmvctt.itp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    // === LOGIN PAGES ===
    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("loginRequest", new UserLoginRequest());
        return "auth/login";
    }

    @PostMapping("/login")
    public String processLogin(@Valid @ModelAttribute("loginRequest") UserLoginRequest loginRequest,
                              BindingResult result,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "auth/login";
        }

        try {
            userService.authenticate(loginRequest);
            return "redirect:/";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "auth/login";
        }
    }

    // === REGISTER PAGES ===
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("userRequest", new UserCreationRequest());
        return "auth/register";
    }

    @PostMapping("/register")
    public String processRegister(@Valid @ModelAttribute("userRequest") UserCreationRequest userRequest,
                                 BindingResult result,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        
        // Check for basic validation errors first
        if (result.hasErrors()) {
            return "auth/register";
        }
        
        // Simple password confirmation check
        if (!userRequest.getPassword().equals(userRequest.getConfirmPassword())) {
            model.addAttribute("error", "Password and confirm password do not match");
            return "auth/register";
        }

        try {
            userService.createUser(userRequest);
            redirectAttributes.addFlashAttribute("success", "Account created successfully! Please login.");
            return "redirect:/auth/login";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "auth/register";
        }
    }

    // === LOGOUT ===
    @PostMapping("/logout")
    public String logout(RedirectAttributes redirectAttributes) {
        // TODO: Implement logout logic
        redirectAttributes.addFlashAttribute("success", "You have been logged out successfully");
        return "redirect:/auth/login";
    }
}
