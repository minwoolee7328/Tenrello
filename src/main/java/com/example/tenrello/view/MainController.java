package com.example.tenrello.view;

import com.example.tenrello.security.details.UserDetailsImpl;
import com.example.tenrello.user.dto.LoginUserProfileDto;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/view")
public class MainController {

    @GetMapping("/main")
    public String getLoginPage() {
        return "login";
    }

    @GetMapping("/next")
    public String getNextPage() {
        return "index";
    }

    @GetMapping("/mypage")
    public String getMyPage(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        LoginUserProfileDto profileDto = new LoginUserProfileDto(userDetails.getUser());
        model.addAttribute("profile", profileDto);
        return "mypage";
    }
}
