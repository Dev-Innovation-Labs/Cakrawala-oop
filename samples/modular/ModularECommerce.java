/**
 * ═══════════════════════════════════════════════════════════
 *  BAB 10: MODULAR MONOLITH — E-Commerce Sederhana
 * ═══════════════════════════════════════════════════════════
 *
 *  @author  Wahyu Amaldi, M.Kom
 *  @institution Universitas Cakrawala
 *
 *  Mendemonstrasikan:
 *  • Module Boundary: tiap modul punya interface publik
 *  • Event Bus: komunikasi antar modul lewat event
 *  • Loose Coupling: modul tidak saling referensi langsung
 *  • Domain Event: notifikasi kejadian bisnis
 *
 *  Compile & Run:
 *    javac ModularECommerce.java
 *    java ModularECommerce
 * ═══════════════════════════════════════════════════════════
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

// ═══════════════════════════════════════════
// DOMAIN EVENT
// ═══════════════════════════════════════════
class DomainEvent {
    private final String tipe;
    private final Map<String, Object> data;
    private final long timestamp;

    public DomainEvent(String tipe, Map<String, Object> data) {
        this.tipe = tipe;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    public String getTipe() { return tipe; }

    @SuppressWarnings("unchecked")
    public <T> T getData(String key) { return (T) data.get(key); }

    @Override
    public String toString() {
        return "[Event:" + tipe + "] " + data;
    }
}

// ═══════════════════════════════════════════
// EVENT BUS: Penghubung antar modul
// ═══════════════════════════════════════════
class EventBus {
    private final Map<String, List<Consumer<DomainEvent>>> subscribers
        = new HashMap<>();
    private static EventBus instance;

    private EventBus() {}

    public static EventBus getInstance() {
        if (instance == null) instance = new EventBus();
        return instance;
    }

    public void subscribe(String eventType, Consumer<DomainEvent> handler) {
        subscribers.computeIfAbsent(eventType, k -> new ArrayList<>())
                   .add(handler);
    }

    public void publish(DomainEvent event) {
        System.out.println("\n  📡 EventBus: " + event);
        List<Consumer<DomainEvent>> handlers = subscribers.get(event.getTipe());
        if (handlers != null) {
            for (Consumer<DomainEvent> h : handlers) {
                h.accept(event);
            }
        }
    }

    public void reset() {
        subscribers.clear();
        instance = null;
    }
}

// ═══════════════════════════════════════════
// MODULE 1: CATALOG (Kelola produk & stok)
// ═══════════════════════════════════════════
class CatalogModule {
    private final Map<String, Integer> stok = new HashMap<>();
    private final Map<String, Double> harga = new HashMap<>();

    public CatalogModule() {
        // Data awal
        stok.put("LAPTOP-001", 10);
        stok.put("MOUSE-001", 50);
        stok.put("KEYBOARD-001", 30);
        harga.put("LAPTOP-001", 15_000_000.0);
        harga.put("MOUSE-001", 250_000.0);
        harga.put("KEYBOARD-001", 1_200_000.0);

        // Dengarkan event pembayaran berhasil → kurangi stok
        EventBus.getInstance().subscribe("PEMBAYARAN_BERHASIL", event -> {
            String produkId = event.getData("produkId");
            int jumlah = event.getData("jumlah");
            kurangiStok(produkId, jumlah);
        });
    }

    public boolean cekStok(String produkId, int jumlah) {
        return stok.getOrDefault(produkId, 0) >= jumlah;
    }

    public double getHarga(String produkId) {
        return harga.getOrDefault(produkId, 0.0);
    }

    private void kurangiStok(String produkId, int jumlah) {
        int sisa = stok.get(produkId) - jumlah;
        stok.put(produkId, sisa);
        System.out.println("    📦 [Catalog] Stok " + produkId
            + " dikurangi " + jumlah + " → sisa " + sisa);

        // Jika stok rendah, publish event
        if (sisa <= 5) {
            EventBus.getInstance().publish(new DomainEvent(
                "STOK_RENDAH",
                Map.of("produkId", produkId, "sisa", sisa)
            ));
        }
    }

    public void tampilkanStok() {
        System.out.println("  ┌──────────────────────────────┐");
        System.out.println("  │  CATALOG — Stok Produk       │");
        System.out.println("  ├──────────────────────────────┤");
        for (var entry : stok.entrySet()) {
            System.out.printf("  │  %-15s : %3d unit   │%n",
                entry.getKey(), entry.getValue());
        }
        System.out.println("  └──────────────────────────────┘");
    }
}

// ═══════════════════════════════════════════
// MODULE 2: ORDER (Kelola pesanan)
// ═══════════════════════════════════════════
class OrderModule {
    private final CatalogModule catalog;
    private int counter = 0;

    public OrderModule(CatalogModule catalog) {
        this.catalog = catalog;
    }

    public String buatPesanan(String pelanggan, String produkId, int jumlah) {
        System.out.println("\n  🛒 [Order] Pesanan baru dari: " + pelanggan);

        // Validasi stok
        if (!catalog.cekStok(produkId, jumlah)) {
            System.out.println("    ❌ Stok tidak cukup untuk " + produkId);
            return null;
        }

        counter++;
        String orderId = "ORD-" + String.format("%03d", counter);
        double totalHarga = catalog.getHarga(produkId) * jumlah;

        System.out.printf("    ✅ Pesanan %s dibuat: %s x%d = IDR %,.0f%n",
            orderId, produkId, jumlah, totalHarga);

        // Publish event: pesanan dibuat
        EventBus.getInstance().publish(new DomainEvent(
            "PESANAN_DIBUAT",
            Map.of("orderId", orderId,
                   "pelanggan", pelanggan,
                   "produkId", produkId,
                   "jumlah", jumlah,
                   "total", totalHarga)
        ));

        return orderId;
    }
}

// ═══════════════════════════════════════════
// MODULE 3: PAYMENT (Kelola pembayaran)
// ═══════════════════════════════════════════
class PaymentModule {

    public PaymentModule() {
        // Dengarkan event pesanan dibuat → proses pembayaran
        EventBus.getInstance().subscribe("PESANAN_DIBUAT", event -> {
            String orderId = event.getData("orderId");
            String pelanggan = event.getData("pelanggan");
            double total = event.getData("total");
            String produkId = event.getData("produkId");
            int jumlah = event.getData("jumlah");

            prosesBayar(orderId, pelanggan, produkId, jumlah, total);
        });
    }

    private void prosesBayar(String orderId, String pelanggan,
                             String produkId, int jumlah, double total) {
        System.out.println("    💳 [Payment] Proses bayar " + orderId
            + " — IDR " + String.format("%,.0f", total));

        // Simulasi: pembayaran selalu berhasil
        System.out.println("    ✅ [Payment] Pembayaran berhasil!");

        // Publish event: pembayaran berhasil
        EventBus.getInstance().publish(new DomainEvent(
            "PEMBAYARAN_BERHASIL",
            Map.of("orderId", orderId,
                   "pelanggan", pelanggan,
                   "produkId", produkId,
                   "jumlah", jumlah,
                   "total", total)
        ));
    }
}

// ═══════════════════════════════════════════
// MODULE 4: NOTIFICATION (Modul notifikasi)
// ═══════════════════════════════════════════
class NotificationModule {

    public NotificationModule() {
        // Dengarkan berbagai event
        EventBus.getInstance().subscribe("PEMBAYARAN_BERHASIL", event -> {
            String pelanggan = event.getData("pelanggan");
            String orderId = event.getData("orderId");
            System.out.println("    📧 [Notif] Email ke " + pelanggan
                + ": Pembayaran " + orderId + " berhasil, pesanan diproses.");
        });

        EventBus.getInstance().subscribe("STOK_RENDAH", event -> {
            String produkId = event.getData("produkId");
            int sisa = event.getData("sisa");
            System.out.println("    ⚠️ [Notif] ALERT ke admin: Stok "
                + produkId + " rendah! Sisa: " + sisa);
        });
    }
}

// ═══════════════════════════════════════════
// MAIN: Demonstrasi Modular Monolith
// ═══════════════════════════════════════════
public class ModularECommerce {
    public static void main(String[] args) {
        System.out.println("══════════════════════════════════════════");
        System.out.println("  DEMO MODULAR MONOLITH — E-Commerce");
        System.out.println("══════════════════════════════════════════");

        // ─── Setup Modules ───
        // Urutan penting: subscriber harus ready sebelum publisher
        CatalogModule catalog = new CatalogModule();
        NotificationModule notif = new NotificationModule();
        PaymentModule payment = new PaymentModule();
        OrderModule order = new OrderModule(catalog);

        // ─── Tampilkan stok awal ───
        System.out.println("\n▸ Stok Awal:\n");
        catalog.tampilkanStok();

        // ─── Skenario 1: Pembelian normal ───
        System.out.println("\n▸ Skenario 1: Budi beli 1 Laptop");
        order.buatPesanan("Budi", "LAPTOP-001", 1);

        // ─── Skenario 2: Pembelian banyak ───
        System.out.println("\n▸ Skenario 2: Siti beli 8 Laptop (stok jadi rendah)");
        order.buatPesanan("Siti", "LAPTOP-001", 8);

        // ─── Skenario 3: Stok habis ───
        System.out.println("\n▸ Skenario 3: Andi beli 5 Laptop (stok tidak cukup)");
        order.buatPesanan("Andi", "LAPTOP-001", 5);

        // ─── Tampilkan stok akhir ───
        System.out.println("\n▸ Stok Akhir:\n");
        catalog.tampilkanStok();

        // ─── Ringkasan Arsitektur ───
        System.out.println("\n╔═══════════════════════════════════════════╗");
        System.out.println("║  ALUR EVENT:                              ║");
        System.out.println("║                                           ║");
        System.out.println("║  Order → PESANAN_DIBUAT                   ║");
        System.out.println("║    ↓                                      ║");
        System.out.println("║  Payment → PEMBAYARAN_BERHASIL            ║");
        System.out.println("║    ↓                                      ║");
        System.out.println("║  Catalog (kurangi stok)                   ║");
        System.out.println("║    ↓                                      ║");
        System.out.println("║  Notification (email ke pelanggan)        ║");
        System.out.println("║    ↓                                      ║");
        System.out.println("║  Catalog → STOK_RENDAH (jika sisa ≤ 5)   ║");
        System.out.println("║    ↓                                      ║");
        System.out.println("║  Notification (alert ke admin)            ║");
        System.out.println("╚═══════════════════════════════════════════╝");
    }
}
