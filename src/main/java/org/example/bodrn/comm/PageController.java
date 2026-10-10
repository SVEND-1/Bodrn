package org.example.bodrn.comm;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    @GetMapping("/")
    public String index() {
        return "forward:/index.html";
    }
    @GetMapping("/admin")
    public String adminPage() {
        return "forward:/admin.html";
    }
}
