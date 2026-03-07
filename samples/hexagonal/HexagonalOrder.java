/**
 * ═══════════════════════════════════════════════════════════
 *  BAB 7: HEXAGONAL ARCHITECTURE — Port & Adapter
 * ═══════════════════════════════════════════════════════════
 *
 *  @author  Wahyu Amaldi, M.Kom
 *  @institution Universitas Cakrawala
 *
 *  Mendemonstrasikan:
 *  • Input Port  → interface use case
 *  • Output Port → interface infrastruktur
 *  • Adapter     → implementasi port
 *  • Swap adapter tanpa ubah inti bisnis
 *
 *  Compile & Run:
 *    javac HexagonalOrder.java
 *    java HexagonalOrder
 * ═══════════════════════════════════════════════════════════
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

// ╔═══════════════════════════════════════════════╗
// ║  DOMAIN MODEL (lingkaran paling dalam)        ║
// ╚═══════════════════════════════════════════════╝
class Pesanan {
    private final String nomor;
    private final String pelanggan;
    private final List<String> items = new ArrayList<>();
    private double total;

    public Pesanan(String nomor, String pelanggan) {
        this.nomor = nomor;
        this.pelanggan = pelanggan;
        this.total = 0;
    }

    public void tambahItem(String nama, double harga) {
        items.add(nama + " @ " + String.format("%,.0f", harga));
        total += harga;
    }

    public String getNomor() { return nomor; }
    public String getPelanggan() { return pelanggan; }
    public double getTotal() { return total; }
    public List<String> getItems() { return items; }

    @Override
    public String toString() {
        return "[" + nomor + "] " + pelanggan + " — " + items.size()
            + " item, total IDR " + String.format("%,.0f", total);
    }
}

// ╔═══════════════════════════════════════════════╗
// ║  OUTPUT PORTS (driven side — keluar)          ║
// ╚═══════════════════════════════════════════════╝
interface SimpanPesananPort {
    void simpan(Pesanan pesanan);
    Optional<Pesanan> cariByNomor(String nomor);
}

interface KirimNotifikasiPort {
    void kirim(String tujuan, String pesan);
}

// ╔═══════════════════════════════════════════════╗
// ║  INPUT PORT (driving side — masuk)            ║
// ╚═══════════════════════════════════════════════╝
interface BuatPesananUseCase {
    Pesanan execute(String pelanggan, Map<String, Double> items);
}

// ╔═══════════════════════════════════════════════╗
// ║  APPLICATION SERVICE (inti bisnis)            ║
// ╚═══════════════════════════════════════════════╝
class BuatPesananService implements BuatPesananUseCase {
    private final SimpanPesananPort repo;
    private final KirimNotifikasiPort notif;
    private int counter = 0;

    public BuatPesananService(SimpanPesananPort repo,
                              KirimNotifikasiPort notif) {
        this.repo = repo;
        this.notif = notif;
    }

    @Override
    public Pesanan execute(String pelanggan, Map<String, Double> items) {
        counter++;
        String nomor = "ORD-" + String.format("%03d", counter);

        Pesanan pesanan = new Pesanan(nomor, pelanggan);
        for (var entry : items.entrySet()) {
            pesanan.tambahItem(entry.getKey(), entry.getValue());
        }

        // Simpan lewat output port
        repo.simpan(pesanan);

        // Kirim notifikasi lewat output port
        notif.kirim(pelanggan,
            "Pesanan " + nomor + " senilai IDR "
            + String.format("%,.0f", pesanan.getTotal()) + " berhasil dibuat.");

        return pesanan;
    }
}

// ╔═══════════════════════════════════════════════╗
// ║  ADAPTER: Penyimpanan — In-Memory             ║
// ╚═══════════════════════════════════════════════╝
class SimpanKeMemory implements SimpanPesananPort {
    private final Map<String, Pesanan> storage = new HashMap<>();

    @Override
    public void simpan(Pesanan pesanan) {
        storage.put(pesanan.getNomor(), pesanan);
        System.out.println("  💾 [Memory] Tersimpan: " + pesanan.getNomor());
    }

    @Override
    public Optional<Pesanan> cariByNomor(String nomor) {
        return Optional.ofNullable(storage.get(nomor));
    }
}

// ╔═══════════════════════════════════════════════╗
// ║  ADAPTER: Penyimpanan — Simulasi Database     ║
// ╚═══════════════════════════════════════════════╝
class SimpanKeDatabase implements SimpanPesananPort {
    private final Map<String, Pesanan> db = new HashMap<>();

    @Override
    public void simpan(Pesanan pesanan) {
        db.put(pesanan.getNomor(), pesanan);
        System.out.println("  🗄️ [Database] INSERT INTO pesanan VALUES('"
            + pesanan.getNomor() + "', '" + pesanan.getPelanggan() + "', "
            + pesanan.getTotal() + ")");
    }

    @Override
    public Optional<Pesanan> cariByNomor(String nomor) {
        return Optional.ofNullable(db.get(nomor));
    }
}

// ╔═══════════════════════════════════════════════╗
// ║  ADAPTER: Notifikasi — Email                  ║
// ╚═══════════════════════════════════════════════╝
class NotifEmail implements KirimNotifikasiPort {
    @Override
    public void kirim(String tujuan, String pesan) {
        System.out.println("  📧 [Email] Kepada " + tujuan + ": " + pesan);
    }
}

// ╔═══════════════════════════════════════════════╗
// ║  ADAPTER: Notifikasi — WhatsApp               ║
// ╚═══════════════════════════════════════════════╝
class NotifWhatsApp implements KirimNotifikasiPort {
    @Override
    public void kirim(String tujuan, String pesan) {
        System.out.println("  📱 [WhatsApp] Ke " + tujuan + ": " + pesan);
    }
}

// ╔═══════════════════════════════════════════════╗
// ║  MAIN: Demonstrasi Hexagonal Architecture     ║
// ╚═══════════════════════════════════════════════╝
public class HexagonalOrder {
    public static void main(String[] args) {
        System.out.println("══════════════════════════════════════════");
        System.out.println("  DEMO HEXAGONAL ARCHITECTURE");
        System.out.println("══════════════════════════════════════════\n");

        // ──── Skenario 1: Memory + Email ────
        System.out.println("▸ Skenario 1: Adapter Memory + Email\n");
        BuatPesananService service1 = new BuatPesananService(
            new SimpanKeMemory(), new NotifEmail());

        Map<String, Double> items1 = new HashMap<>();
        items1.put("Laptop Gaming", 15_000_000.0);
        items1.put("Mouse Wireless", 250_000.0);
        Pesanan p1 = service1.execute("Budi", items1);
        System.out.println("  Hasil: " + p1 + "\n");

        // ──── Skenario 2: Database + WhatsApp ────
        System.out.println("▸ Skenario 2: Adapter Database + WhatsApp\n");
        BuatPesananService service2 = new BuatPesananService(
            new SimpanKeDatabase(), new NotifWhatsApp());

        Map<String, Double> items2 = new HashMap<>();
        items2.put("Keyboard Mekanik", 1_200_000.0);
        items2.put("Monitor 27\"", 4_500_000.0);
        Pesanan p2 = service2.execute("Siti", items2);
        System.out.println("  Hasil: " + p2 + "\n");

        // ──── Poin Penting ────
        System.out.println("╔═══════════════════════════════════════════╗");
        System.out.println("║  POIN PENTING:                            ║");
        System.out.println("║                                           ║");
        System.out.println("║  • BuatPesananService SAMA persis         ║");
        System.out.println("║  • Yang berubah hanya ADAPTER             ║");
        System.out.println("║  • Inti bisnis tidak tersentuh            ║");
        System.out.println("║  • Inilah kekuatan Hexagonal Architecture ║");
        System.out.println("╚═══════════════════════════════════════════╝");
    }
}
