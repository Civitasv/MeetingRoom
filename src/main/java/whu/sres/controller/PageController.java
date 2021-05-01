package whu.sres.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("page")
public class PageController {
    @GetMapping("/help")
    public String help() {
        return "help";
    }

    @GetMapping("/manage")
    public String manage() {
        return "userManage";
    }
}
