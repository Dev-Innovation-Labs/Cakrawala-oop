# 🧱 Bab 10: Modular Monolith

> **Penulis:** Wahyu Amaldi, M.Kom · **Institusi:** Universitas Cakrawala

[⬅️ Workflow Engine](09-workflow-engine.md) · [⬅️ Beranda](../README.md)

---

## 📌 Apa Itu Modular Monolith?

**Modular Monolith** adalah arsitektur di mana aplikasi berjalan sebagai **satu unit (monolith)**, tetapi secara internal terorganisasi dalam **modul-modul yang independen** dengan batas yang jelas. Setiap modul memiliki domain, data, dan aturan bisnisnya sendiri.

> **Inti dari Modular Monolith:** _"Satu aplikasi, tapi di dalamnya terstruktur rapi seperti kota dengan distrik-distrik — setiap distrik mandiri, tapi tetap satu kota."_

---

## 🧠 Analogi Dunia Nyata

### 🏬 Mal Besar vs Pasar Tradisional

```
❌ MONOLITH TRADISIONAL (Pasar):         ✅ MODULAR MONOLITH (Mal):
───────────────────────────              ─────────────────────────
┌──────────────────────────┐             ┌────────────────────────┐
│ Semua campur:            │             │  MAL CAKRAWALA         │
│ buah, daging, baju,      │             │                        │
│ elektronik, kosmetik     │             │  ┌──────┐  ┌────────┐ │
│ semua di satu tempat     │             │  │Toko  │  │Toko    │ │
│ tanpa sekat              │             │  │Baju  │  │Elektro │ │
│                          │             │  └──────┘  └────────┘ │
│ ❌ Bau ikan kena baju    │             │  ┌──────┐  ┌────────┐ │
│ ❌ Sulit cari barang     │             │  │Food  │  │Toko    │ │
│ ❌ Satu toko bermasalah  │             │  │Court │  │Buku    │ │
│    = semua terganggu     │             │  └──────┘  └────────┘ │
└──────────────────────────┘             │                        │
                                         │  ✅ Setiap toko mandiri│
                                         │  ✅ Batas jelas        │
                                         │  ✅ Masih 1 gedung     │
                                         └────────────────────────┘
```

### Bagaimana Toko di Mal Berkomunikasi?

```
┌─────────────┐                    ┌─────────────┐
│ Toko Baju   │ ──── voucher ────► │ Food Court  │
│             │                    │             │
│ "Belanja    │                    │ "Terima     │
│  200rb,     │  KOMUNIKASI LEWAT  │  voucher,   │
│  dapat      │  PROSEDUR RESMI    │  kasih      │
│  voucher    │  (bukan asal      │  gratis     │
│  makan"     │   masuk dapur)     │  minuman"   │
└─────────────┘                    └─────────────┘
```

Toko baju **tidak masuk ke dapur** food court. Mereka berkomunikasi lewat **voucher** (API/event antar modul).

---

## 🔑 Perbedaan Arsitektur

```
MONOLITH BIASA:              MODULAR MONOLITH:             MICROSERVICES:
───────────────              ─────────────────             ──────────────

┌──────────────┐             ┌──────────────┐             ┌────┐ ┌────┐
│              │             │ ┌────┐┌────┐ │             │ S1 │ │ S2 │
│   Semua      │             │ │ M1 ││ M2 │ │             └──┬─┘ └──┬─┘
│   jadi satu  │             │ └────┘└────┘ │                │      │
│   tanpa      │             │ ┌────┐┌────┐ │             ┌──┴─┐ ┌──┴─┐
│   batas      │             │ │ M3 ││ M4 │ │             │ S3 │ │ S4 │
│              │             │ └────┘└────┘ │             └────┘ └────┘
└──────────────┘             └──────────────┘
                              SATU APLIKASI               BANYAK SERVICE
   ❌ Spaghetti              TAPI MODULAR                 ✅ Independen
   ❌ Sulit maintain         ✅ Batas jelas               ❌ Kompleks
                              ✅ Deploy mudah              ❌ Network overhead
                              ✅ Siap jadi microservice    ❌ Sulit debug
```

---

## 💻 Contoh: Sistem E-Commerce Modular

### Struktur Modul

```
e-commerce/
├── module-catalog/          ← Modul Katalog Produk
│   ├── CatalogModule.java
│   ├── Produk.java
│   └── CatalogService.java
│
├── module-order/            ← Modul Pesanan
│   ├── OrderModule.java
│   ├── Order.java
│   └── OrderService.java
│
├── module-payment/          ← Modul Pembayaran
│   ├── PaymentModule.java
│   ├── Payment.java
│   └── PaymentService.java
│
└── shared/                  ← Kontrak antar-modul
    ├── ModuleRegistry.java
    └── events/
        ├── OrderCreatedEvent.java
        └── PaymentCompletedEvent.java
```

### Aturan Modul

```
╔═══════════════════════════════════════════════════╗
║              ATURAN MODULAR MONOLITH               ║
╠═══════════════════════════════════════════════════╣
║                                                   ║
║  1. Setiap modul punya PACKAGE sendiri            ║
║  2. Modul HANYA expose interface publik           ║
║  3. Modul TIDAK boleh akses class internal        ║
║     modul lain                                    ║
║  4. Komunikasi antar modul lewat EVENT atau       ║
║     INTERFACE, bukan langsung                     ║
║  5. Setiap modul bisa punya DATABASE/TABLE sendiri║
║                                                   ║
╚═══════════════════════════════════════════════════╝
```

### Implementasi

```java
import java.util.*;

/**
 * ═══════════════════════════════════════
 * SHARED: Event antar modul
 * ═══════════════════════════════════════
 */
interface DomainEvent {
    String getEventName();
}

class OrderCreatedEvent implements DomainEvent {
    private final String orderId;
    private final String pelanggan;
    private final double total;

    public OrderCreatedEvent(String orderId, String pelanggan, double total) {
        this.orderId = orderId;
        this.pelanggan = pelanggan;
        this.total = total;
    }

    @Override
    public String getEventName() { return "ORDER_CREATED"; }
    public String getOrderId() { return orderId; }
    public String getPelanggan() { return pelanggan; }
    public double getTotal() { return total; }
}

class PaymentCompletedEvent implements DomainEvent {
    private final String orderId;
    private final double jumlah;

    public PaymentCompletedEvent(String orderId, double jumlah) {
        this.orderId = orderId;
        this.jumlah = jumlah;
    }

    @Override
    public String getEventName() { return "PAYMENT_COMPLETED"; }
    public String getOrderId() { return orderId; }
    public double getJumlah() { return jumlah; }
}

/**
 * ═══════════════════════════════════════
 * SHARED: Event Bus (penghubung antar modul)
 * ═══════════════════════════════════════
 */
interface EventListener {
    void handle(DomainEvent event);
}

class EventBus {
    private Map<String, List<EventListener>> listeners = new HashMap<>();

    public void subscribe(String eventName, EventListener listener) {
        listeners.computeIfAbsent(eventName, k -> new ArrayList<>())
                 .add(listener);
    }

    public void publish(DomainEvent event) {
        System.out.println("\n📢 Event Bus: " + event.getEventName());
        List<EventListener> handlers = listeners.get(event.getEventName());
        if (handlers != null) {
            for (EventListener listener : handlers) {
                listener.handle(event);
            }
        }
    }
}

/**
 * ═══════════════════════════════════════
 * MODULE: Catalog (Katalog Produk)
 * ═══════════════════════════════════════
 */

// Hanya interface ini yang terlihat dari luar modul
interface CatalogQuery {
    Optional<String> getNamaProduk(String kode);
    Optional<Double> getHarga(String kode);
    boolean cekStok(String kode, int qty);
}

class CatalogModule implements CatalogQuery {
    // Data INTERNAL — modul lain tidak boleh akses langsung
    private Map<String, String> namaProduk = new HashMap<>();
    private Map<String, Double> hargaProduk = new HashMap<>();
    private Map<String, Integer> stokProduk = new HashMap<>();

    public CatalogModule() {
        // Seed data
        namaProduk.put("LP01", "Laptop Gaming");
        hargaProduk.put("LP01", 15_000_000.0);
        stokProduk.put("LP01", 10);

        namaProduk.put("MS01", "Mouse Wireless");
        hargaProduk.put("MS01", 250_000.0);
        stokProduk.put("MS01", 50);

        namaProduk.put("KB01", "Keyboard Mechanical");
        hargaProduk.put("KB01", 800_000.0);
        stokProduk.put("KB01", 30);
    }

    // PUBLIC API — bisa diakses modul lain
    @Override
    public Optional<String> getNamaProduk(String kode) {
        return Optional.ofNullable(namaProduk.get(kode));
    }

    @Override
    public Optional<Double> getHarga(String kode) {
        return Optional.ofNullable(hargaProduk.get(kode));
    }

    @Override
    public boolean cekStok(String kode, int qty) {
        return stokProduk.getOrDefault(kode, 0) >= qty;
    }

    // INTERNAL — hanya dipakai dalam modul ini
    void kurangiStok(String kode, int qty) {
        int stokSaatIni = stokProduk.getOrDefault(kode, 0);
        stokProduk.put(kode, stokSaatIni - qty);
        System.out.println("  📦 [Catalog] Stok " + kode
            + " dikurangi " + qty + " → sisa: " + (stokSaatIni - qty));
    }
}

/**
 * ═══════════════════════════════════════
 * MODULE: Order (Pesanan)
 * ═══════════════════════════════════════
 */
class OrderModule {
    private CatalogQuery catalog;  // Akses LEWAT interface, bukan class
    private EventBus eventBus;
    private Map<String, String> orders = new HashMap<>();

    public OrderModule(CatalogQuery catalog, EventBus eventBus) {
        this.catalog = catalog;
        this.eventBus = eventBus;
    }

    public String buatPesanan(String pelanggan, String kodeProduk, int qty) {
        System.out.println("\n── [Order Module] Buat Pesanan ──");

        // Validasi: produk ada?
        String nama = catalog.getNamaProduk(kodeProduk)
            .orElseThrow(() -> new RuntimeException("Produk tidak ditemukan"));

        // Validasi: stok cukup?
        if (!catalog.cekStok(kodeProduk, qty)) {
            throw new RuntimeException("Stok tidak cukup untuk " + nama);
        }

        // Buat pesanan
        double harga = catalog.getHarga(kodeProduk).get();
        double total = harga * qty;
        String orderId = "ORD-" + System.currentTimeMillis();

        orders.put(orderId, "MENUNGGU_BAYAR");

        System.out.println("  ✅ Pesanan " + orderId + " dibuat");
        System.out.println("  📋 " + nama + " x" + qty
            + " = Rp " + String.format("%,.0f", total));

        // Publish event → modul lain bereaksi
        eventBus.publish(
            new OrderCreatedEvent(orderId, pelanggan, total));

        return orderId;
    }

    public void tandaiLunas(String orderId) {
        orders.put(orderId, "LUNAS");
        System.out.println("  ✅ [Order] " + orderId + " → LUNAS");
    }
}

/**
 * ═══════════════════════════════════════
 * MODULE: Payment (Pembayaran)
 *
 * Modul ini LISTEN event dari Order,
 * bukan import class Order langsung!
 * ═══════════════════════════════════════
 */
class PaymentModule implements EventListener {
    private EventBus eventBus;
    private OrderModule orderModule;  // Untuk callback

    public PaymentModule(EventBus eventBus, OrderModule orderModule) {
        this.eventBus = eventBus;
        this.orderModule = orderModule;
        // Subscribe ke event ORDER_CREATED
        eventBus.subscribe("ORDER_CREATED", this);
    }

    @Override
    public void handle(DomainEvent event) {
        if (event instanceof OrderCreatedEvent) {
            OrderCreatedEvent e = (OrderCreatedEvent) event;
            System.out.println("  💳 [Payment] Menunggu pembayaran untuk "
                + e.getOrderId() + " sebesar Rp "
                + String.format("%,.0f", e.getTotal()));
        }
    }

    public void prosesBayar(String orderId, double jumlah) {
        System.out.println("\n── [Payment Module] Proses Bayar ──");
        System.out.println("  💰 Pembayaran Rp "
            + String.format("%,.0f", jumlah) + " diterima");

        // Update order (via module API, bukan akses langsung)
        orderModule.tandaiLunas(orderId);

        // Publish event pembayaran selesai
        eventBus.publish(new PaymentCompletedEvent(orderId, jumlah));
    }
}
```

### Wiring: Merangkai Semua Modul

```java
/**
 * MAIN: Composition Root — merangkai modul
 */
public class ModularECommerce {
    public static void main(String[] args) {
        System.out.println("══════════════════════════════════════");
        System.out.println("  DEMO MODULAR MONOLITH — E-COMMERCE ");
        System.out.println("══════════════════════════════════════");

        // 1. Buat infrastruktur shared
        EventBus eventBus = new EventBus();

        // 2. Buat modul-modul
        CatalogModule catalog = new CatalogModule();
        OrderModule order = new OrderModule(catalog, eventBus);
        PaymentModule payment = new PaymentModule(eventBus, order);

        // Subscribe catalog ke event pembayaran (kurangi stok)
        eventBus.subscribe("PAYMENT_COMPLETED", event -> {
            if (event instanceof PaymentCompletedEvent) {
                PaymentCompletedEvent e = (PaymentCompletedEvent) event;
                catalog.kurangiStok("LP01", 1);  // Simplified
            }
        });

        // 3. Jalankan skenario bisnis
        System.out.println("\n── Skenario: Budi beli Laptop Gaming ──");
        String orderId = order.buatPesanan("Budi", "LP01", 1);

        System.out.println("\n── Skenario: Budi bayar ──");
        payment.prosesBayar(orderId, 15_000_000);

        System.out.println("\n══════════════════════════════════════");
    }
}
```

---

## 🔍 Prinsip Modular Monolith

### 1. Module Boundary

```
Modul A                           Modul B
┌─────────────────┐               ┌─────────────────┐
│                 │               │                 │
│  ┌───────────┐  │  Public API   │  ┌───────────┐  │
│  │ Internal  │  │  (Interface)  │  │ Internal  │  │
│  │ Class     │  │◄─────────────►│  │ Class     │  │
│  └───────────┘  │               │  └───────────┘  │
│  ┌───────────┐  │               │  ┌───────────┐  │
│  │ Internal  │  │               │  │ Internal  │  │
│  │ Service   │  │               │  │ Service   │  │
│  └───────────┘  │               │  └───────────┘  │
│                 │               │                 │
└─────────────────┘               └─────────────────┘

✅ Komunikasi lewat interface/event
❌ TIDAK boleh import class internal modul lain
```

### 2. Kapan Modular Monolith Cocok?

| Situasi | Arsitektur |
|:--------|:-----------|
| Tim kecil (2-5 orang), produk awal | Modular Monolith ✅ |
| Tim besar, domain sudah matang | Microservices |
| Perlu deploy cepat, satu server | Modular Monolith ✅ |
| Butuh skalabilitas per modul | Microservices |
| Belum yakin batas modul yang tepat | Modular Monolith ✅ (bisa evolusi) |

---

## 📋 Checklist Modular Monolith

- [ ] Setiap modul punya **package/folder** terpisah
- [ ] Modul hanya expose **public interface**, bukan class internal
- [ ] Komunikasi antar modul via **event** atau **interface**
- [ ] Setiap modul bisa dites **secara independen**
- [ ] **Tidak ada** import class internal dari modul lain
- [ ] Event Bus/Mediator menangani komunikasi antar modul

---

## 🔗 Navigasi

| Sebelumnya | Berikutnya |
|:-----------|:-----------|
| [📖 ← Workflow Engine](09-workflow-engine.md) | [🏠 Beranda](../README.md) |

---

<p align="center"><i>"Modular Monolith: kompleksitas terkontrol hari ini, siap berevolusi besok."</i></p>

---

<p align="center">
  <b>Wahyu Amaldi, M.Kom</b> · Universitas Cakrawala
</p>
