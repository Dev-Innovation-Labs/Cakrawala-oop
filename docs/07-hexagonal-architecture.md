# 🔌 Bab 7: Hexagonal Architecture (Port & Adapter)

> **Penulis:** Wahyu Amaldi, M.Kom · **Institusi:** Universitas Cakrawala

[⬅️ DDD](06-domain-driven-design.md) · [⬅️ Beranda](../README.md) · [Business Object ➡️](08-business-object-process.md)

---

## 📌 Apa Itu Hexagonal Architecture?

**Hexagonal Architecture** (disebut juga **Ports and Adapters**) adalah pola arsitektur yang memisahkan **logika bisnis inti (domain)** dari **detail teknis** seperti database, UI, atau API eksternal.

> **Inti dari Hexagonal:** _"Domain bisnis tidak boleh bergantung pada framework, database, atau teknologi apapun. Teknologi yang harus menyesuaikan domain, bukan sebaliknya."_

---

## 🧠 Analogi Dunia Nyata

### 🔌 Colokan Listrik & Adaptor

Bayangkan kamu punya laptop dari Jepang (colokan tipe A) dan ingin menggunakannya di Indonesia (colokan tipe C):

```
    LAPTOP (Domain)              ADAPTOR              STOP KONTAK (Infrastruktur)
    ┌────────────┐          ┌──────────────┐          ┌──────────────┐
    │            │          │              │          │              │
    │  Butuh     │◄────────►│  Mengubah    │◄────────►│  Listrik     │
    │  listrik   │   PORT   │  bentuk      │ ADAPTER  │  PLN         │
    │  (220V)    │          │  colokan     │          │  Indonesia   │
    │            │          │              │          │              │
    └────────────┘          └──────────────┘          └──────────────┘
    
    TIDAK BERUBAH           BISA DIGANTI              BISA DIGANTI
```

- **Laptop** = Domain (logic bisnis) — tidak berubah
- **Colokan** = Port (interface) — kontrak yang harus dipenuhi
- **Adaptor** = Adapter (implementasi) — bisa diganti sesuai kebutuhan
- **Stop kontak** = Infrastruktur (database, API, dll.)

Laptop-mu **tidak perlu tahu** jenis stop kontak apa yang dipakai. Yang penting ada adaptor yang memenuhi kebutuhan.

---

## 🔑 Struktur Hexagonal

```
                    ┌─────────────────────────────┐
                    │        DRIVING SIDE          │
                    │   (yang MEMICU domain)       │
                    │                              │
                    │   🖥️ UI / Controller         │
                    │   📱 REST API                │
                    │   ⏰ Scheduler               │
                    └─────────────┬───────────────┘
                                  │
                           ┌──────┴──────┐
                           │  INPUT PORT │  ← Interface
                           └──────┬──────┘
                                  │
              ┌───────────────────┴───────────────────┐
              │                                       │
              │           🏛️ DOMAIN CORE              │
              │                                       │
              │   ┌───────────┐  ┌────────────────┐  │
              │   │  Entity   │  │  Value Object  │  │
              │   ├───────────┤  ├────────────────┤  │
              │   │  Pesanan  │  │  Uang          │  │
              │   │  Produk   │  │  Alamat        │  │
              │   └───────────┘  └────────────────┘  │
              │                                       │
              │   ┌────────────────────────────────┐  │
              │   │  Domain Service / Use Case     │  │
              │   │  (Logika Bisnis)               │  │
              │   └────────────────────────────────┘  │
              │                                       │
              └───────────────────┬───────────────────┘
                                  │
                           ┌──────┴──────┐
                           │ OUTPUT PORT │  ← Interface
                           └──────┬──────┘
                                  │
                    ┌─────────────┴───────────────┐
                    │        DRIVEN SIDE           │
                    │  (yang DILAYANI domain)      │
                    │                              │
                    │   🗄️ Database Adapter        │
                    │   📧 Email Adapter           │
                    │   🔗 External API Adapter    │
                    └─────────────────────────────┘
```

---

## 💻 Contoh Lengkap: Sistem Pemesanan

### Langkah 1: Definisikan Domain (Inti)

```java
/**
 * ═══════════════════════════════════════
 * DOMAIN: Entity & Value Object
 * Tidak bergantung pada apapun di luar
 * ═══════════════════════════════════════
 */
class Produk {
    private final String kode;
    private final String nama;
    private final double harga;

    public Produk(String kode, String nama, double harga) {
        this.kode = kode;
        this.nama = nama;
        this.harga = harga;
    }

    public String getKode() { return kode; }
    public String getNama() { return nama; }
    public double getHarga() { return harga; }
}

class OrderItem {
    private final Produk produk;
    private final int qty;

    public OrderItem(Produk produk, int qty) {
        if (qty <= 0) throw new IllegalArgumentException("Qty harus > 0");
        this.produk = produk;
        this.qty = qty;
    }

    public double getSubtotal() {
        return produk.getHarga() * qty;
    }

    public Produk getProduk() { return produk; }
    public int getQty() { return qty; }
}
```

### Langkah 2: Definisikan Port (Interface)

```java
/**
 * ═══════════════════════════════════════
 * PORT: Interface yang DOMAIN butuhkan
 * Domain tidak tahu siapa implementornya
 * ═══════════════════════════════════════
 */

// INPUT PORT: Apa yang bisa dilakukan terhadap domain
interface BuatPesananUseCase {
    String buatPesanan(String pelanggan, List<OrderItem> items);
}

// OUTPUT PORT: Apa yang domain butuhkan dari luar
interface SimpanPesananPort {
    void simpan(PesananOrder pesanan);
}

interface KirimNotifikasiPort {
    void kirimKonfirmasi(String pelanggan, String nomorPesanan);
}

interface CariProdukPort {
    Optional<Produk> cariByKode(String kode);
}
```

### Langkah 3: Implementasi Domain Service (Use Case)

```java
/**
 * ═══════════════════════════════════════
 * DOMAIN SERVICE: Logika bisnis murni
 * Menggunakan PORT, bukan implementasi
 * ═══════════════════════════════════════
 */
class BuatPesananService implements BuatPesananUseCase {

    private final SimpanPesananPort simpanPort;
    private final KirimNotifikasiPort notifPort;

    // Dependency Injection via constructor
    public BuatPesananService(SimpanPesananPort simpanPort,
                              KirimNotifikasiPort notifPort) {
        this.simpanPort = simpanPort;
        this.notifPort = notifPort;
    }

    @Override
    public String buatPesanan(String pelanggan, List<OrderItem> items) {
        // Validasi bisnis
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Pesanan harus punya item");
        }

        // Buat pesanan
        String nomor = "ORD-" + System.currentTimeMillis();
        PesananOrder pesanan = new PesananOrder(nomor, pelanggan, items);

        // Simpan (lewat PORT — tidak tahu ke mana)
        simpanPort.simpan(pesanan);

        // Notifikasi (lewat PORT — tidak tahu via apa)
        notifPort.kirimKonfirmasi(pelanggan, nomor);

        return nomor;
    }
}
```

### Langkah 4: Implementasi Adapter

```java
/**
 * ═══════════════════════════════════════
 * ADAPTER: Implementasi teknis dari Port
 * Bisa diganti tanpa mengubah domain!
 * ═══════════════════════════════════════
 */

// ADAPTER: Simpan ke Memory (untuk development/testing)
class SimpanKeMemory implements SimpanPesananPort {
    private Map<String, PesananOrder> storage = new HashMap<>();

    @Override
    public void simpan(PesananOrder pesanan) {
        storage.put(pesanan.getNomor(), pesanan);
        System.out.println("💾 [Memory] Pesanan " + pesanan.getNomor()
            + " tersimpan.");
    }
}

// ADAPTER: Simpan ke Database (untuk production)
class SimpanKeDatabase implements SimpanPesananPort {
    @Override
    public void simpan(PesananOrder pesanan) {
        // Di sini akan ada kode JDBC/JPA
        System.out.println("🗄️ [Database] INSERT INTO pesanan VALUES ('"
            + pesanan.getNomor() + "', ...)");
    }
}

// ADAPTER: Notifikasi via Email
class NotifEmail implements KirimNotifikasiPort {
    @Override
    public void kirimKonfirmasi(String pelanggan, String nomorPesanan) {
        System.out.println("📧 [Email] Halo " + pelanggan
            + ", pesanan " + nomorPesanan + " diterima!");
    }
}

// ADAPTER: Notifikasi via WhatsApp
class NotifWhatsApp implements KirimNotifikasiPort {
    @Override
    public void kirimKonfirmasi(String pelanggan, String nomorPesanan) {
        System.out.println("📱 [WhatsApp] " + pelanggan
            + ": Pesanan " + nomorPesanan + " dikonfirmasi ✅");
    }
}
```

### Langkah 5: Wiring (Merangkai Semuanya)

```java
/**
 * MAIN: Merangkai adapter ke domain
 * Di sinilah satu-satunya tempat yang "tahu" semua class konkret
 */
public class HexagonalOrder {
    public static void main(String[] args) {

        // Pilih ADAPTER yang diinginkan
        SimpanPesananPort storage = new SimpanKeMemory();
        KirimNotifikasiPort notif = new NotifWhatsApp();

        // Inject ke domain service
        BuatPesananService service = new BuatPesananService(storage, notif);

        // Gunakan!
        List<OrderItem> items = List.of(
            new OrderItem(new Produk("LP01", "Laptop", 15000000), 1),
            new OrderItem(new Produk("MS01", "Mouse", 150000), 2)
        );

        String nomorPesanan = service.buatPesanan("Budi", items);
        System.out.println("Nomor pesanan: " + nomorPesanan);

        // Ganti adapter? Tinggal ubah di sini!
        // SimpanPesananPort storage = new SimpanKeDatabase();
        // KirimNotifikasiPort notif = new NotifEmail();
    }
}
```

---

## 🔍 Keuntungan Hexagonal Architecture

### 1. Domain Terlindungi

```
❌ Tanpa Hexagonal:
   Domain ──► JDBC ──► MySQL
   (Jika ganti PostgreSQL, domain HARUS berubah)

✅ Dengan Hexagonal:
   Domain ──► Port (interface) ◄── Adapter ──► MySQL
                                ◄── Adapter ──► PostgreSQL
   (Ganti database? Ganti adapter saja. Domain AMAN.)
```

### 2. Mudah Dites

```java
// Saat testing, ganti adapter dengan MOCK
class MockStorage implements SimpanPesananPort {
    public PesananOrder terakhirDisimpan;

    @Override
    public void simpan(PesananOrder pesanan) {
        this.terakhirDisimpan = pesanan;  // Simpan untuk verifikasi
    }
}

// Test tanpa database sungguhan!
MockStorage mock = new MockStorage();
BuatPesananService service = new BuatPesananService(mock, new NotifEmail());
service.buatPesanan("Test", items);
assert mock.terakhirDisimpan != null;  // ✅ Berhasil!
```

### 3. Teknologi Bisa Diganti

| Kebutuhan | Ganti Adapter | Domain Berubah? |
|:----------|:--------------|:----------------|
| MySQL → PostgreSQL | Buat adapter baru | ❌ Tidak |
| Email → WhatsApp | Buat adapter baru | ❌ Tidak |
| REST → GraphQL | Buat adapter baru | ❌ Tidak |
| Tambah fitur bisnis | Ubah domain | ✅ Ya (memang seharusnya) |

---

## 📋 Aturan Dependensi

```
   ATURAN UTAMA: Dependency mengarah KE DALAM

   ┌──────────────────────────────────────────┐
   │              ADAPTER (Luar)              │
   │                                          │
   │    ┌────────────────────────────────┐    │
   │    │          PORT (Interface)      │    │
   │    │                                │    │
   │    │    ┌──────────────────────┐    │    │
   │    │    │    DOMAIN (Inti)     │    │    │
   │    │    │                      │    │    │
   │    │    │  Entity, VO, Service │    │    │
   │    │    │                      │    │    │
   │    │    └──────────────────────┘    │    │
   │    │                                │    │
   │    └────────────────────────────────┘    │
   │                                          │
   └──────────────────────────────────────────┘

   ✅ Adapter TAHU Port
   ✅ Port TAHU Domain
   ❌ Domain TIDAK TAHU Port/Adapter
   ❌ Port TIDAK TAHU Adapter
```

---

## 📋 Checklist Hexagonal Architecture

- [ ] Domain **tidak import** class dari infrastruktur (database, framework)
- [ ] Setiap ketergantungan eksternal didefinisikan sebagai **Port (interface)**
- [ ] **Adapter** mengimplementasi Port untuk teknologi spesifik
- [ ] Wiring dilakukan di satu tempat (Main/Composition Root)
- [ ] Domain bisa dites **tanpa** database, API, atau framework

---

## 🔗 Navigasi

| Sebelumnya | Berikutnya |
|:-----------|:-----------|
| [📖 ← DDD](06-domain-driven-design.md) | [📖 Business Object →](08-business-object-process.md) |

---

<p align="center"><i>"Arsitektur yang baik memungkinkan keputusan teknis ditunda, dan domain bisnis berdiri sendiri."</i></p>

---

<p align="center">
  <b>Wahyu Amaldi, M.Kom</b> · Universitas Cakrawala
</p>
