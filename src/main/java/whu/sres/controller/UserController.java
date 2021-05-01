package whu.sres.controller;

import org.springframework.web.bind.annotation.*;
import whu.sres.model.User;

@RestController
@RequestMapping("user")
public class UserController {

    @PostMapping("/login")
    public String login(@RequestBody User requestUser) {
        return "";
    }
}
