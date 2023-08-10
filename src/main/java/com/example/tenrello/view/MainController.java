package com.example.tenrello.view;

import org.springframework.stereotype.Controller;
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

    @GetMapping("/index")
    public String getIndexPage() {
        return "index";
    }
}
