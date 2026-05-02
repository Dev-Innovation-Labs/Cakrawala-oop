package id.universitas.cakrawala.controller;

import id.universitas.cakrawala.domain.TransaksiResult;
import id.universitas.cakrawala.service.DesignPatternsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Controller untuk halaman Design Patterns
 */
@Controller
@RequestMapping("/")
public class DesignPatternsController {

    @Autowired
    private DesignPatternsService service;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("daftarDiskon", service.getDaftarDiskon());
        model.addAttribute("daftarProduk", service.getDaftarProduk());
        return "index";
    }

    @GetMapping("/simulator")
    public String simulator(Model model) {
        model.addAttribute("daftarDiskon", service.getDaftarDiskon());
        model.addAttribute("daftarProduk", service.getDaftarProduk());
        return "simulator";
    }

    /**
     * API endpoint untuk simulasi Strategy Pattern
     */
    @PostMapping("/api/simulasi")
    @ResponseBody
    public TransaksiResult simulasi(@RequestParam String tipeDiskon, 
                                     @RequestParam String item, 
                                     @RequestParam double harga) {
        return service.simulasiStrategy(tipeDiskon, item, harga);
    }
}
