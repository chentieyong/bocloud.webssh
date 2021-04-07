package com.bocloud.webssh.booter.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RouterController {

    @RequestMapping("/websshpage")
    public String websshpage() {
        return "webssh";
    }

    @RequestMapping("/webssh3")
    public String webssh3() {
        return "webssh3";
    }
}
