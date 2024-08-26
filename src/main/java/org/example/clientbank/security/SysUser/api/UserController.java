package org.example.clientbank.security.SysUser.api;

import lombok.RequiredArgsConstructor;
import org.example.clientbank.security.SysUser.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/login")
    public String userDetails() {
        return "login";
    }

    @GetMapping("/registration")
    public String userRegistrationPage() {
        return "registration";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }

    @PostMapping("/create")
    public String createNewUser(@RequestParam String username, @RequestParam String password) {
        userService.createUser(username, password);
        return "redirect:/registration";
    }

}
