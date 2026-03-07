/**
 * ═══════════════════════════════════════════════════════════
 *  BAB 5: DESIGN PATTERNS — Strategy, Observer, Factory, State
 * ═══════════════════════════════════════════════════════════
 *
 *  @author  Wahyu Amaldi, M.Kom
 *  @institution Universitas Cakrawala
 *
 *  Mendemonstrasikan:
 *  • Strategy Pattern  — Diskon pelanggan yang bisa diganti
 *  • Observer Pattern  — Notifikasi stok ke banyak pengamat
 *  • Factory Pattern   — Pembuatan dokumen berdasarkan jenis
 *  • State Pattern     — Status pesanan yang mengubah perilaku
 *
 *  Compile & Run:
 *    javac DiscountStrategy.java
 *    java DiscountStrategy
 * ═══════════════════════════════════════════════════════════
 */

import java.util.ArrayList;
import java.util.List;

// ═══════════════════════════════════════════
// STRATEGY PATTERN: Diskon Pelanggan
// ═══════════════════════════════════════════

interface StrategiDiskon {
    double hitungDiskon(double harga);
    String getNamaDiskon();
}

class DiskonMember implements StrategiDiskon {
    @Override
    public double hitungDiskon(double harga) { return harga * 0.05; }
    @Override
    public String getNamaDiskon() { return "Member Biasa (5%)"; }
}

class DiskonGold implements StrategiDiskon {
    @Override
    public double hitungDiskon(double harga) { return harga * 0.15; }
    @Override
    public String getNamaDiskon() { return "Member Gold (15%)"; }
}

class DiskonHarbolnas implements StrategiDiskon {
    @Override
    public double hitungDiskon(double harga) { return harga * 0.30; }
    @Override
    public String getNamaDiskon() { return "Harbolnas (30%)"; }
}

class Kasir {
    private StrategiDiskon strategi;

    public void setStrategi(StrategiDiskon strategi) {
        this.strategi = strategi;
    }

    public void prosesPembayaran(String item, double harga) {
        double diskon = strategi.hitungDiskon(harga);
        double total = harga - diskon;
        System.out.println("  Item     : " + item);
        System.out.println("  Harga    : Rp " + String.format("%,.0f", harga));
        System.out.println("  Diskon   : " + strategi.getNamaDiskon());
        System.out.println("  Potongan : Rp " + String.format("%,.0f", diskon));
        System.out.println("  Total    : Rp " + String.format("%,.0f", total));
        System.out.println("  ─────────────────────────");
    }
}

// ═══════════════════════════════════════════
// OBSERVER PATTERN: Notifikasi Stok
// ═══════════════════════════════════════════

interface PengamatStok {
    void update(String namaProduk, int stokBaru);
}

class Produk {
    private String nama;
    private int stok;
    private List<PengamatStok> pengamat = new ArrayList<>();

    public Produk(String nama, int stok) {
        this.nama = nama;
        this.stok = stok;
    }

    public void tambahPengamat(PengamatStok p) { pengamat.add(p); }

    public void setStok(int stokBaru) {
        this.stok = stokBaru;
        for (PengamatStok p : pengamat) {
            p.update(nama, stokBaru);
        }
    }
}

class NotifikasiEmail implements PengamatStok {
    @Override
    public void update(String nama, int stok) {
        if (stok > 0) {
            System.out.println("  📧 Email: '" + nama + "' tersedia! Stok: " + stok);
        }
    }
}

class DashboardWeb implements PengamatStok {
    @Override
    public void update(String nama, int stok) {
        System.out.println("  🖥️ Dashboard: Stok '" + nama + "' → " + stok + " unit");
    }
}

// ═══════════════════════════════════════════
// FACTORY PATTERN: Pembuatan Dokumen
// ═══════════════════════════════════════════

interface Dokumen {
    void cetak();
    String getJenis();
}

class Invoice implements Dokumen {
    @Override
    public void cetak() {
        System.out.println("  📄 Mencetak INVOICE — No. INV-2026-001");
    }
    @Override
    public String getJenis() { return "Invoice"; }
}

class Kwitansi implements Dokumen {
    @Override
    public void cetak() {
        System.out.println("  🧾 Mencetak KWITANSI — No. KWT-2026-001");
    }
    @Override
    public String getJenis() { return "Kwitansi"; }
}

class DokumenFactory {
    public static Dokumen buat(String jenis) {
        switch (jenis.toLowerCase()) {
            case "invoice":  return new Invoice();
            case "kwitansi": return new Kwitansi();
            default: throw new IllegalArgumentException("Jenis tidak dikenal: " + jenis);
        }
    }
}

// ═══════════════════════════════════════════
// MAIN: Demonstrasi Semua Pattern
// ═══════════════════════════════════════════

public class DiscountStrategy {
    public static void main(String[] args) {
        System.out.println("══════════════════════════════════════════");
        System.out.println("  DEMO DESIGN PATTERNS                    ");
        System.out.println("══════════════════════════════════════════\n");

        // ── STRATEGY ──
        System.out.println("▸ STRATEGY PATTERN: Diskon Pelanggan\n");
        Kasir kasir = new Kasir();

        kasir.setStrategi(new DiskonMember());
        kasir.prosesPembayaran("Sepatu Nike", 1500000);

        kasir.setStrategi(new DiskonGold());
        kasir.prosesPembayaran("Jaket Adidas", 2000000);

        kasir.setStrategi(new DiskonHarbolnas());
        kasir.prosesPembayaran("Laptop Asus", 12000000);

        // ── OBSERVER ──
        System.out.println("\n▸ OBSERVER PATTERN: Notifikasi Stok\n");
        Produk iphone = new Produk("iPhone 16", 0);
        iphone.tambahPengamat(new NotifikasiEmail());
        iphone.tambahPengamat(new DashboardWeb());

        System.out.println("  Stok iPhone diperbarui:");
        iphone.setStok(50);

        // ── FACTORY ──
        System.out.println("\n▸ FACTORY PATTERN: Buat Dokumen\n");
        Dokumen inv = DokumenFactory.buat("invoice");
        inv.cetak();
        Dokumen kwt = DokumenFactory.buat("kwitansi");
        kwt.cetak();

        System.out.println("\n══════════════════════════════════════════");
    }
}
