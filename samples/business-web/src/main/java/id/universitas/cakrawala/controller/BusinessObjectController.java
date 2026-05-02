package id.universitas.cakrawala.controller;

import id.universitas.cakrawala.domain.PurchaseOrderBO;
import id.universitas.cakrawala.dto.PurchaseOrderDTO;
import id.universitas.cakrawala.service.PengadaanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Controller untuk Business Object Simulator
 */
@Controller
@RequestMapping("/")
public class BusinessObjectController {

    @Autowired
    private PengadaanService service;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("daftarVendor", service.getDaftarVendor());
        return "index";
    }

    @GetMapping("/simulator")
    public String simulator(Model model) {
        model.addAttribute("daftarVendor", service.getDaftarVendor());
        model.addAttribute("daftarBarang", service.getDaftarBarang());
        return "simulator";
    }

    /**
     * API: Buat PO baru
     */
    @PostMapping("/api/po/buat")
    @ResponseBody
    public PurchaseOrderDTO buatPO(@RequestParam String vendor) {
        PurchaseOrderBO po = service.buatPO(vendor);
        return new PurchaseOrderDTO(po);
    }

    /**
     * API: Ambil PO
     */
    @GetMapping("/api/po/{nomorPO}")
    @ResponseBody
    public PurchaseOrderDTO getPO(@PathVariable String nomorPO) {
        PurchaseOrderBO po = service.getPO(nomorPO);
        return new PurchaseOrderDTO(po);
    }

    /**
     * API: Tambah item ke PO
     */
    @PostMapping("/api/po/{nomorPO}/item")
    @ResponseBody
    public PurchaseOrderDTO tambahItem(@PathVariable String nomorPO,
                                        @RequestParam String namaBarang,
                                        @RequestParam int jumlah,
                                        @RequestParam double harga) {
        service.tambahItem(nomorPO, namaBarang, jumlah, harga);
        return new PurchaseOrderDTO(service.getPO(nomorPO));
    }

    /**
     * API: Submit PO
     */
    @PostMapping("/api/po/{nomorPO}/submit")
    @ResponseBody
    public PurchaseOrderDTO submitPO(@PathVariable String nomorPO) {
        service.submitPO(nomorPO);
        return new PurchaseOrderDTO(service.getPO(nomorPO));
    }

    /**
     * API: Approve PO
     */
    @PostMapping("/api/po/{nomorPO}/approve")
    @ResponseBody
    public PurchaseOrderDTO approvePO(@PathVariable String nomorPO,
                                       @RequestParam String approver) {
        service.approvePO(nomorPO, approver);
        return new PurchaseOrderDTO(service.getPO(nomorPO));
    }

    /**
     * API: Hapus item
     */
    @DeleteMapping("/api/po/{nomorPO}/item/{index}")
    @ResponseBody
    public PurchaseOrderDTO hapusItem(@PathVariable String nomorPO,
                                       @PathVariable int index) {
        service.hapusItem(nomorPO, index);
        return new PurchaseOrderDTO(service.getPO(nomorPO));
    }
}
