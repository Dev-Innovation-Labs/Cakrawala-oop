package id.universitas.cakrawala.service;

import id.universitas.cakrawala.domain.*;
import org.springframework.stereotype.Service;

/**
 * Service untuk Design Patterns simulation
 */
@Service
public class DesignPatternsService {
    private Kasir kasir = new Kasir();

    /**
     * Simulasi Strategy Pattern dengan berbagai strategi diskon
     */
    public TransaksiResult simulasiStrategy(String tipeDiskon, String item, double harga) {
        // Set strategi berdasarkan input
        StrategiDiskon strategi = getStrategiDiskon(tipeDiskon);
        kasir.setStrategi(strategi);

        // Proses pembayaran
        return kasir.prosesPembayaran(item, harga);
    }

    /**
     * Factory pattern untuk membuat strategi diskon
     */
    private StrategiDiskon getStrategiDiskon(String tipe) {
        return switch (tipe.toLowerCase()) {
            case "member" -> new DiskonMember();
            case "gold" -> new DiskonGold();
            case "harbolnas" -> new DiskonHarbolnas();
            default -> new DiskonMember(); // default
        };
    }

    /**
     * Daftar semua tipe diskon yang tersedia
     */
    public String[] getDaftarDiskon() {
        return new String[]{"member", "gold", "harbolnas"};
    }

    /**
     * Daftar contoh produk
     */
    public String[] getDaftarProduk() {
        return new String[]{
            "Sepatu Nike - 1,500,000",
            "Jaket Adidas - 2,000,000",
            "Laptop Asus - 12,000,000",
            "Smartphone Samsung - 5,000,000",
            "Headphone JBL - 800,000"
        };
    }
}
