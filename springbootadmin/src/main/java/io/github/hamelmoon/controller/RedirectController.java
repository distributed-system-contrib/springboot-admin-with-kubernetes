package io.github.hamelmoon.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.result.view.RedirectView;

@Controller
@RequestMapping("/")
class RedirectController {
    @GetMapping("/")
    public RedirectView redirectToHome() {
        return new RedirectView("/springadmin/");
    }
}
