package whu.sres.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import whu.sres.authority.VerifyToken;

@Controller
@RequestMapping("")
public class PageController {
    @GetMapping("/help")
    public String help() {
        return "help";
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/manage")
    public String manage() {
        return "userManage";
    }
}
