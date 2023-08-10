package com.example.tenrello.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/main")
    public String getLoginPage() {
        return "login";
    }

    @GetMapping("/next")
    public String getNextPage() {
        return "index";
    }

    @GetMapping("/index")
    public String getIndexPage() {
        return "index";
    }
}
