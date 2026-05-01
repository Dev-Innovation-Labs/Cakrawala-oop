package id.universitas.cakrawala.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller untuk serve HTML pages
 */
@Controller
@RequestMapping("/")
public class PageController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }

    @GetMapping("/create")
    public String create() {
        return "create";
    }

    @GetMapping("/docs/api")
    public String docsApi() {
        return "docs-api";
    }
}
