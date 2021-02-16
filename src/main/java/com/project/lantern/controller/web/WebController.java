package com.project.lantern.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/{path:[^\\.]+}/**")
    public String forward() {
        return "forward:/index.html";
    }

}
