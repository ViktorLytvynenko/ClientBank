package org.example.clientbank;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HelloController {

    public String hello() {
        return "forward:/index.html";
    }
}
