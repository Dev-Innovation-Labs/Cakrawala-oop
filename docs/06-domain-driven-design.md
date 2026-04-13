# 🏗️ Bab 6: Domain Driven Design (DDD)

> **Penulis:** Wahyu Amaldi, M.Kom · **Institusi:** Universitas Cakrawala

[⬅️ Design Patterns](05-design-patterns.md) · [⬅️ Beranda](../README.md) · [Hexagonal Architecture ➡️](07-hexagonal-architecture.md)

---

## 📌 Apa Itu Domain Driven Design?

**Domain Driven Design (DDD)** adalah pendekatan pengembangan perangkat lunak yang menempatkan **domain bisnis** sebagai pusat dari seluruh desain. Kode program dirancang agar **mencerminkan cara bisnis bekerja** — bukan sekadar menyimpan data ke database.

> **Inti dari DDD:** _"Kode harus berbicara dalam bahasa bisnis. Jika ahli bisnis membaca kode-mu, mereka seharusnya bisa mengerti alurnya."_

---

## 🧠 Analogi Dunia Nyata

### 🏢 Struktur Organisasi Perusahaan

Bayangkan sebuah perusahaan besar:

```
┌──────────────────────────────────────────────────────────┐
│                    PT CAKRAWALA JAYA                       │
├──────────────┬──────────────┬──────────────┬─────────────┤
│  Divisi HR   │ Divisi Sales │ Divisi Gudang│ Divisi Fin  │
│              │              │              │             │
│ • Karyawan   │ • Pelanggan  │ • Produk     │ • Invoice   │
│ • Kontrak    │ • Pesanan    │ • Stok       │ • Pembayaran│
│ • Gaji       │ • Diskon     │ • Pengiriman │ • Pajak     │
└──────────────┴──────────────┴──────────────┴─────────────┘
```

- Setiap **divisi** punya **istilah sendiri** (bahasa ubiquitous)
- Setiap divisi punya **aturan internal** sendiri
- Antar divisi berkomunikasi lewat **prosedur resmi**, bukan asal masuk
- "Karyawan" di HR ≠ "Karyawan" di Sales (konteks berbeda)

**Itulah DDD!** Setiap divisi = **Bounded Context**. Setiap istilah = **Ubiquitous Language**.

---

## 🔑 Konsep Kunci DDD

### Building Blocks

```
┌─────────────────────────────────────────────────────┐
│                   DDD BUILDING BLOCKS                │
├─────────────────┬───────────────────────────────────┤
│  ENTITY         │  Objek dengan identitas unik       │
│                 │  (Pesanan #001 ≠ Pesanan #002)     │
├─────────────────┼───────────────────────────────────┤
│  VALUE OBJECT   │  Objek tanpa identitas, hanya nilai│
│                 │  (Uang Rp 50.000 = Rp 50.000)     │
├─────────────────┼───────────────────────────────────┤
│  AGGREGATE      │  Cluster entity yang dijaga satu   │
│                 │  "penjaga gerbang" (Aggregate Root)│
├─────────────────┼───────────────────────────────────┤
│  REPOSITORY     │  Tempat menyimpan & mengambil      │
│                 │  aggregate (seperti lemari arsip)   │
├─────────────────┼───────────────────────────────────┤
│  DOMAIN EVENT   │  Kejadian penting yang sudah       │
│                 │  terjadi ("Pesanan telah dibayar") │
├─────────────────┼───────────────────────────────────┤
│  DOMAIN SERVICE │  Logika bisnis yang tidak milik     │
│                 │  satu entity tertentu              │
└─────────────────┴───────────────────────────────────┘
```

---

## 1️⃣ Entity — Objek dengan Identitas

**Entity** adalah objek yang memiliki **identitas unik** dan bisa berubah seiring waktu. Dua entity dengan atribut yang sama **tetap berbeda** jika ID-nya berbeda.

### Analogi: KTP

Dua orang bisa bernama "Budi" dan berumur 25 tahun. Tapi KTP mereka berbeda — **identitasnya unik**.

```java
/**
 * ENTITY: Pesanan
 * Identitas unik = nomorPesanan
 * Dua pesanan dengan nomor berbeda = pesanan berbeda
 */
class Pesanan {
    private final String nomorPesanan;  // Identitas — tidak berubah
    private String namaPelanggan;       // Bisa berubah
    private String status;              // Bisa berubah
    private double totalHarga;          // Bisa berubah

    public Pesanan(String nomorPesanan, String namaPelanggan) {
        this.nomorPesanan = nomorPesanan;
        this.namaPelanggan = namaPelanggan;
        this.status = "BARU";
        this.totalHarga = 0;
    }

    // Identitas menentukan kesamaan
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Pesanan) {
            return this.nomorPesanan.equals(((Pesanan) obj).nomorPesanan);
        }
        return false;
    }

    public String getNomorPesanan() { return nomorPesanan; }
    public String getStatus() { return status; }
}
```

### 📦 Contoh Kasus Real: Entity di ERP Inventory/MM

| Modul | Entity | Identitas Unik | Atribut yang Bisa Berubah |
|:------|:-------|:---------------|:--------------------------|
| **Material Master** | Material | Kode Material (MAT-001) | Nama, Harga, Stok, Satuan, Status |
| **Vendor Master** | Vendor | Kode Vendor (VND-001) | Nama, Alamat, Kontak, Rating |
| **Purchase Order** | PurchaseOrder | Nomor PO (PO-2026-001) | Status, Total, Tanggal Kirim |
| **Warehouse** | StorageLocation | Kode Lokasi (WH-A-01-03) | Kapasitas Terpakai, Status |
| **Goods Receipt** | GoodsReceipt | Nomor GR (GR-2026-001) | Status Inspeksi, Catatan |

```java
// 📦 Contoh Real: Entity Material di Sistem ERP
class Material {
    private final String kodeMaterial;   // Identitas — tidak berubah
    private String nama;
    private String kategori;             // RAW_MATERIAL, SPARE_PART, FINISHED_GOOD
    private String satuan;               // PCS, KG, LITER, BOX
    private double hargaPerUnit;         // Bisa berubah (update harga)
    private int stokMinimum;             // Bisa diatur ulang
    private String status;               // AKTIF, DISCONTINUE, BLOCKED

    public Material(String kodeMaterial, String nama, String kategori,
                    String satuan) {
        this.kodeMaterial = kodeMaterial;
        this.nama = nama;
        this.kategori = kategori;
        this.satuan = satuan;
        this.status = "AKTIF";
    }

    public void updateHarga(double hargaBaru) {
        if (hargaBaru <= 0) {
            throw new IllegalArgumentException("Harga harus > 0");
        }
        this.hargaPerUnit = hargaBaru;
    }

    public void discontinue() {
        this.status = "DISCONTINUE";
    }

    // Dua material sama jika kodeMaterial sama
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Material) {
            return this.kodeMaterial.equals(((Material) obj).kodeMaterial);
        }
        return false;
    }

    public String getKodeMaterial() { return kodeMaterial; }
    public String getNama() { return nama; }
    public double getHargaPerUnit() { return hargaPerUnit; }
    public String getSatuan() { return satuan; }
    public int getStokMinimum() { return stokMinimum; }
    public String getStatus() { return status; }
}

// 🏢 Contoh Real: Entity Vendor di Sistem ERP
class Vendor {
    private final String kodeVendor;     // Identitas — tidak berubah
    private String namaPerusahaan;
    private String alamat;
    private String kontak;
    private int rating;                  // 1-5, berubah berdasarkan performa
    private String status;               // AKTIF, BLACKLIST, SUSPENDED

    public Vendor(String kodeVendor, String namaPerusahaan) {
        this.kodeVendor = kodeVendor;
        this.namaPerusahaan = namaPerusahaan;
        this.rating = 3;  // default
        this.status = "AKTIF";
    }

    public void updateRating(int ratingBaru) {
        if (ratingBaru < 1 || ratingBaru > 5) {
            throw new IllegalArgumentException("Rating harus 1-5");
        }
        this.rating = ratingBaru;
        if (ratingBaru == 1) {
            this.status = "SUSPENDED";
        }
    }

    public void blacklist() {
        this.status = "BLACKLIST";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Vendor) {
            return this.kodeVendor.equals(((Vendor) obj).kodeVendor);
        }
        return false;
    }

    public String getKodeVendor() { return kodeVendor; }
    public String getStatus() { return status; }
}
```

> **Kunci Entity:** Walau atribut berubah (harga material naik, rating vendor turun), **identitasnya tetap sama**.

---

## 2️⃣ Value Object — Objek Tanpa Identitas

**Value Object** tidak punya ID. Ia hanya mewakili **nilai**. Dua Value Object dengan nilai yang sama dianggap **identik**.

### Analogi: Uang Kertas

Dua lembar uang Rp 50.000 — kamu tidak peduli **lembar yang mana**, yang penting nilainya sama.

```java
/**
 * VALUE OBJECT: Uang
 * Tidak punya ID — hanya mewakili nilai
 * Immutable — tidak bisa diubah setelah dibuat
 */
class Uang {
    private final double jumlah;
    private final String mataUang;

    public Uang(double jumlah, String mataUang) {
        if (jumlah < 0) {
            throw new IllegalArgumentException("Jumlah tidak boleh negatif");
        }
        this.jumlah = jumlah;
        this.mataUang = mataUang;
    }

    // Operasi menghasilkan objek BARU (immutable)
    public Uang tambah(Uang lain) {
        if (!this.mataUang.equals(lain.mataUang)) {
            throw new IllegalArgumentException("Mata uang harus sama");
        }
        return new Uang(this.jumlah + lain.jumlah, this.mataUang);
    }

    public Uang kurangi(Uang lain) {
        if (!this.mataUang.equals(lain.mataUang)) {
            throw new IllegalArgumentException("Mata uang harus sama");
        }
        return new Uang(this.jumlah - lain.jumlah, this.mataUang);
    }

    // Kesamaan berdasarkan NILAI, bukan identitas
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Uang) {
            Uang other = (Uang) obj;
            return this.jumlah == other.jumlah
                && this.mataUang.equals(other.mataUang);
        }
        return false;
    }

    @Override
    public String toString() {
        return mataUang + " " + String.format("%,.0f", jumlah);
    }
}
```

### Entity vs Value Object

| Aspek | Entity | Value Object |
|:------|:-------|:-------------|
| Identitas | Punya ID unik | Tidak punya ID |
| Kesamaan | Berdasarkan ID | Berdasarkan nilai |
| Mutable? | Ya, bisa berubah | **Immutable** (tidak berubah) |
| Contoh | Pesanan, Pelanggan, Karyawan | Uang, Alamat, Tanggal, Koordinat |

### 📦 Contoh Kasus Real: Value Object di ERP Inventory/MM

| Modul | Value Object | Kenapa Value Object? |
|:------|:-------------|:---------------------|
| **Inventory** | Quantity (50, "KG") | Hanya mewakili jumlah + satuan, tidak perlu ID |
| **Purchasing** | Uang (Rp 15.000.000, "IDR") | Rp 15jt = Rp 15jt, tidak peduli dari mana |
| **Warehouse** | LokasiRak ("Gudang-A", "Rak-03", "Baris-2") | Hanya mewakili posisi di gudang |
| **Material** | Dimensi (100cm x 50cm x 30cm, 2.5kg) | Hanya nilai pengukuran fisik |
| **Procurement** | PeriodePengiriman ("2026-05-01" s/d "2026-05-15") | Rentang waktu, bukan entitas |

```java
// 📦 Contoh Real: Value Object Quantity untuk Sistem Inventory
class Quantity {
    private final double jumlah;
    private final String satuan;   // "KG", "PCS", "LITER", "BOX"

    public Quantity(double jumlah, String satuan) {
        if (jumlah < 0) {
            throw new IllegalArgumentException("Jumlah tidak boleh negatif");
        }
        this.jumlah = jumlah;
        this.satuan = satuan;
    }

    // Operasi menghasilkan objek BARU (immutable)
    public Quantity tambah(Quantity lain) {
        if (!this.satuan.equals(lain.satuan)) {
            throw new IllegalArgumentException(
                "Satuan harus sama! " + satuan + " ≠ " + lain.satuan);
        }
        return new Quantity(this.jumlah + lain.jumlah, this.satuan);
    }

    public Quantity kurangi(Quantity lain) {
        if (!this.satuan.equals(lain.satuan)) {
            throw new IllegalArgumentException("Satuan harus sama!");
        }
        return new Quantity(this.jumlah - lain.jumlah, this.satuan);
    }

    public boolean lebihDari(Quantity lain) {
        return this.jumlah > lain.jumlah;
    }

    // 50 KG di gudang A = 50 KG di gudang B → sama!
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Quantity) {
            Quantity other = (Quantity) obj;
            return this.jumlah == other.jumlah
                && this.satuan.equals(other.satuan);
        }
        return false;
    }

    public double getJumlah() { return jumlah; }
    public String getSatuan() { return satuan; }

    @Override
    public String toString() {
        return String.format("%,.1f %s", jumlah, satuan);
    }
}

// 🏭 Contoh Real: Value Object LokasiRak untuk Sistem Warehouse
class LokasiRak {
    private final String gudang;       // "GUDANG-A"
    private final String rak;          // "RAK-03"
    private final String baris;        // "BARIS-2"

    public LokasiRak(String gudang, String rak, String baris) {
        this.gudang = gudang;
        this.rak = rak;
        this.baris = baris;
    }

    // Pindah lokasi? Buat objek BARU (immutable)
    public LokasiRak pindahRak(String rakBaru) {
        return new LokasiRak(gudang, rakBaru, baris);
    }

    // Kesamaan berdasarkan SEMUA nilai
    // GUDANG-A/RAK-03/BARIS-2 = GUDANG-A/RAK-03/BARIS-2
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof LokasiRak) {
            LokasiRak other = (LokasiRak) obj;
            return gudang.equals(other.gudang)
                && rak.equals(other.rak)
                && baris.equals(other.baris);
        }
        return false;
    }

    @Override
    public String toString() {
        return gudang + "/" + rak + "/" + baris;
    }
}
```

> **Kunci Value Object:** Tidak ada ID, **immutable**, kesamaan berdasarkan nilai. 50 KG tetap 50 KG — tidak peduli di mana.

---

## 3️⃣ Aggregate — Cluster yang Dijaga

**Aggregate** adalah kumpulan Entity dan Value Object yang diperlakukan sebagai **satu kesatuan**. Ada satu Entity utama yang menjadi **Aggregate Root** — semua akses dari luar harus lewat root ini.

### Analogi: Rumah dan Isinya

```
┌──────────────────────────────────────────┐
│           AGGREGATE: Pesanan              │
│           (Aggregate Root)                │
│                                           │
│   ┌─────────────┐   ┌─────────────┐     │
│   │ ItemPesanan  │   │ ItemPesanan │     │
│   │ ─────────── │   │ ─────────── │     │
│   │ Laptop x1   │   │ Mouse x2    │     │
│   │ Rp 15.000.000│  │ Rp 300.000  │     │
│   └─────────────┘   └─────────────┘     │
│                                           │
│   ┌─────────────┐   ┌──────────────┐    │
│   │  Alamat      │   │  Total       │    │
│   │ (Value Obj)  │   │ (Value Obj)  │    │
│   └─────────────┘   └──────────────┘    │
│                                           │
│   Aturan: Total harus = sum(item.harga)  │
│   Aturan: Minimal 1 item per pesanan     │
└──────────────────────────────────────────┘
      ▲
      │ HANYA lewat Root ini
      │ dari luar
```

### Contoh Kode

```java
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * VALUE OBJECT: Alamat
 */
class Alamat {
    private final String jalan;
    private final String kota;
    private final String kodePos;

    public Alamat(String jalan, String kota, String kodePos) {
        this.jalan = jalan;
        this.kota = kota;
        this.kodePos = kodePos;
    }

    @Override
    public String toString() {
        return jalan + ", " + kota + " " + kodePos;
    }
}

/**
 * ENTITY di dalam Aggregate: ItemPesanan
 */
class ItemPesanan {
    private final String namaProduk;
    private final int jumlah;
    private final Uang hargaSatuan;

    public ItemPesanan(String namaProduk, int jumlah, Uang hargaSatuan) {
        if (jumlah <= 0) {
            throw new IllegalArgumentException("Jumlah harus > 0");
        }
        this.namaProduk = namaProduk;
        this.jumlah = jumlah;
        this.hargaSatuan = hargaSatuan;
    }

    public Uang getSubtotal() {
        return new Uang(
            jumlah * hargaSatuan.getJumlah(),
            hargaSatuan.getMataUang()
        );
    }

    public String getNamaProduk() { return namaProduk; }
    public int getJumlah() { return jumlah; }
}

/**
 * AGGREGATE ROOT: Pesanan
 * Semua akses ke ItemPesanan harus lewat Pesanan
 */
class PesananAggregate {
    private final String nomorPesanan;
    private String namaPelanggan;
    private Alamat alamatKirim;
    private List<ItemPesanan> items = new ArrayList<>();
    private String status;

    public PesananAggregate(String nomorPesanan, String namaPelanggan,
                            Alamat alamatKirim) {
        this.nomorPesanan = nomorPesanan;
        this.namaPelanggan = namaPelanggan;
        this.alamatKirim = alamatKirim;
        this.status = "BARU";
    }

    // Bisnis logic: Tambah item lewat ROOT
    public void tambahItem(String produk, int jumlah, Uang harga) {
        if (!"BARU".equals(status)) {
            throw new IllegalStateException(
                "Tidak bisa tambah item — pesanan sudah " + status);
        }
        items.add(new ItemPesanan(produk, jumlah, harga));
    }

    // Bisnis logic: Hitung total
    public Uang hitungTotal() {
        Uang total = new Uang(0, "IDR");
        for (ItemPesanan item : items) {
            total = total.tambah(item.getSubtotal());
        }
        return total;
    }

    // Bisnis logic: Konfirmasi pesanan
    public void konfirmasi() {
        if (items.isEmpty()) {
            throw new IllegalStateException("Pesanan kosong!");
        }
        this.status = "DIKONFIRMASI";
        System.out.println("✅ Pesanan " + nomorPesanan + " dikonfirmasi.");
        System.out.println("   Total: " + hitungTotal());
    }

    // Mengembalikan copy — lindungi data internal
    public List<ItemPesanan> getItems() {
        return Collections.unmodifiableList(items);
    }

    public String getNomorPesanan() { return nomorPesanan; }
    public String getStatus() { return status; }
}
```

### 📦 Contoh Kasus Real: Aggregate di ERP Inventory/MM

| Modul | Aggregate Root | Entity/VO di Dalamnya | Invariant (Aturan Bisnis) |
|:------|:---------------|:----------------------|:--------------------------|
| **Purchasing** | PurchaseOrder | PO Item, Quantity, Uang | Total = sum(item), min 1 item, vendor harus aktif |
| **Warehouse** | GoodsReceipt | GR Item, Quantity, LokasiRak | Qty diterima ≤ qty di PO |
| **Inventory** | StockCard | StockMovement, Quantity | Stok tidak boleh negatif |
| **Production** | BillOfMaterial | BOMItem, Quantity | Min 1 komponen, tidak boleh referensi diri sendiri |
| **Quality** | InspectionLot | HasilInspeksi, Keputusan | Keputusan baru boleh final jika semua sample diuji |

```java
// 📋 Contoh Real: Aggregate Purchase Order di Sistem ERP
class POItem {
    private final String kodeMaterial;
    private final String namaMaterial;
    private final Quantity jumlahPesan;
    private final Uang hargaSatuan;
    private Quantity jumlahDiterima;    // Diupdate saat Goods Receipt

    public POItem(String kodeMaterial, String namaMaterial,
                  Quantity jumlahPesan, Uang hargaSatuan) {
        this.kodeMaterial = kodeMaterial;
        this.namaMaterial = namaMaterial;
        this.jumlahPesan = jumlahPesan;
        this.hargaSatuan = hargaSatuan;
        this.jumlahDiterima = new Quantity(0, jumlahPesan.getSatuan());
    }

    public Uang getSubtotal() {
        return new Uang(
            jumlahPesan.getJumlah() * hargaSatuan.getJumlah(),
            hargaSatuan.getMataUang()
        );
    }

    public void terimaBarang(Quantity qty) {
        Quantity totalDiterima = jumlahDiterima.tambah(qty);
        if (totalDiterima.lebihDari(jumlahPesan)) {
            throw new IllegalStateException(
                "Penerimaan melebihi qty PO! Sisa: "
                + jumlahPesan.kurangi(jumlahDiterima));
        }
        this.jumlahDiterima = totalDiterima;
    }

    public boolean sudahLengkap() {
        return jumlahDiterima.getJumlah() >= jumlahPesan.getJumlah();
    }

    public String getKodeMaterial() { return kodeMaterial; }
    public Quantity getJumlahPesan() { return jumlahPesan; }
}

/**
 * AGGREGATE ROOT: PurchaseOrder
 * Menjaga aturan: min 1 item, total konsisten, status valid
 */
class PurchaseOrderAggregate {
    private final String nomorPO;
    private final String kodeVendor;
    private final String tanggalPO;
    private List<POItem> items = new ArrayList<>();
    private String status;  // DRAFT, APPROVED, SENT, RECEIVED, CLOSED

    public PurchaseOrderAggregate(String nomorPO, String kodeVendor,
                                   String tanggalPO) {
        this.nomorPO = nomorPO;
        this.kodeVendor = kodeVendor;
        this.tanggalPO = tanggalPO;
        this.status = "DRAFT";
    }

    // Semua akses lewat ROOT — menjaga invariant
    public void tambahItem(String kodeMaterial, String namaMaterial,
                            Quantity jumlah, Uang harga) {
        if (!"DRAFT".equals(status)) {
            throw new IllegalStateException(
                "PO sudah " + status + " — tidak bisa tambah item!");
        }
        items.add(new POItem(kodeMaterial, namaMaterial, jumlah, harga));
    }

    // Invariant: PO harus punya minimal 1 item
    public void approve() {
        if (items.isEmpty()) {
            throw new IllegalStateException("PO kosong — tidak bisa approve!");
        }
        this.status = "APPROVED";
        System.out.println("✅ PO " + nomorPO + " approved. Total: " + hitungTotal());
    }

    public Uang hitungTotal() {
        Uang total = new Uang(0, "IDR");
        for (POItem item : items) {
            total = total.tambah(item.getSubtotal());
        }
        return total;
    }

    public String getNomorPO() { return nomorPO; }
    public String getStatus() { return status; }
    public List<POItem> getItems() {
        return Collections.unmodifiableList(items);
    }
}

// 📥 Contoh Real: Aggregate Goods Receipt (Penerimaan Barang)
/**
 * AGGREGATE ROOT: GoodsReceipt
 * Menjaga invariant: qty diterima ≤ qty PO, stok konsisten
 */
class GoodsReceiptAggregate {
    private final String nomorGR;
    private final String nomorPO;         // Referensi ke PO
    private final String tanggalTerima;
    private List<GRItem> items = new ArrayList<>();
    private String status;  // DRAFT, POSTED, CANCELLED

    public GoodsReceiptAggregate(String nomorGR, String nomorPO,
                                  String tanggalTerima) {
        this.nomorGR = nomorGR;
        this.nomorPO = nomorPO;
        this.tanggalTerima = tanggalTerima;
        this.status = "DRAFT";
    }

    public void tambahItem(String kodeMaterial, Quantity jumlahDiterima,
                            LokasiRak lokasi) {
        if (!"DRAFT".equals(status)) {
            throw new IllegalStateException("GR sudah diposting!");
        }
        items.add(new GRItem(kodeMaterial, jumlahDiterima, lokasi));
    }

    // Invariant: GR harus punya item untuk di-post
    public void posting() {
        if (items.isEmpty()) {
            throw new IllegalStateException("GR kosong!");
        }
        this.status = "POSTED";
        System.out.println("📥 GR " + nomorGR + " posted — "
            + items.size() + " material diterima.");
    }

    public String getNomorGR() { return nomorGR; }
    public String getStatus() { return status; }
}

class GRItem {
    private final String kodeMaterial;
    private final Quantity jumlahDiterima;
    private final LokasiRak lokasiPenyimpanan;

    public GRItem(String kodeMaterial, Quantity jumlahDiterima,
                  LokasiRak lokasi) {
        this.kodeMaterial = kodeMaterial;
        this.jumlahDiterima = jumlahDiterima;
        this.lokasiPenyimpanan = lokasi;
    }

    public String getKodeMaterial() { return kodeMaterial; }
    public Quantity getJumlahDiterima() { return jumlahDiterima; }
}
```

> **Kunci Aggregate:** Semua operasi lewat Root, Root menjaga **invariant** (aturan bisnis yang tidak boleh dilanggar).

---

## 4️⃣ Repository — Lemari Arsip

**Repository** adalah abstraksi untuk menyimpan dan mengambil Aggregate. Ia menyembunyikan detail **di mana** data disimpan (database, file, memory).

### Analogi: Lemari Arsip Kantor

Kamu bilang ke petugas arsip: _"Tolong ambilkan berkas pesanan nomor 001"_. Kamu tidak perlu tahu berkas itu di rak mana, laci mana, map warna apa.

```java
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * INTERFACE Repository: Kontrak penyimpanan
 */
interface PesananRepository {
    void simpan(PesananAggregate pesanan);
    Optional<PesananAggregate> cariByNomor(String nomorPesanan);
    List<PesananAggregate> cariSemua();
}

/**
 * IMPLEMENTASI: Simpan di memory (untuk demo/testing)
 * Di dunia nyata: bisa ke Database, File, API, dsb.
 */
class PesananRepositoryMemory implements PesananRepository {
    private Map<String, PesananAggregate> storage = new HashMap<>();

    @Override
    public void simpan(PesananAggregate pesanan) {
        storage.put(pesanan.getNomorPesanan(), pesanan);
        System.out.println("💾 Pesanan " + pesanan.getNomorPesanan()
            + " tersimpan.");
    }

    @Override
    public Optional<PesananAggregate> cariByNomor(String nomorPesanan) {
        return Optional.ofNullable(storage.get(nomorPesanan));
    }

    @Override
    public List<PesananAggregate> cariSemua() {
        return new ArrayList<>(storage.values());
    }
}
```

### 📦 Contoh Kasus Real: Repository di ERP Inventory/MM

| Modul | Repository | Method Utama |
|:------|:-----------|:-------------|
| **Material** | MaterialRepository | `cariByKode()`, `cariByKategori()`, `simpan()` |
| **Purchasing** | PurchaseOrderRepository | `cariByNomorPO()`, `cariByVendor()`, `simpan()` |
| **Vendor** | VendorRepository | `cariByKode()`, `cariAktif()`, `simpan()` |
| **Warehouse** | GoodsReceiptRepository | `cariByNomorGR()`, `cariByPO()`, `simpan()` |
| **Inventory** | StockCardRepository | `cariByMaterial()`, `cariDibawahMinimum()`, `simpan()` |

```java
// 📦 Contoh Real: Repository Material di Sistem ERP
interface MaterialRepository {
    void simpan(Material material);
    Optional<Material> cariByKode(String kodeMaterial);
    List<Material> cariByKategori(String kategori);
    List<Material> cariAktif();
    List<Material> cariDibawahStokMinimum();
}

// Implementasi pakai database
class MaterialRepositoryDatabase implements MaterialRepository {
    @Override
    public void simpan(Material material) {
        // INSERT INTO material (kode, nama, kategori, ...) VALUES (...)
        // Detail database disembunyikan dari domain!
        System.out.println("💾 Material " + material.getKodeMaterial()
            + " tersimpan ke DB.");
    }

    @Override
    public Optional<Material> cariByKode(String kodeMaterial) {
        // SELECT * FROM material WHERE kode_material = ?
        // Domain tidak perlu tahu pakai SQL, MongoDB, atau API
        return Optional.empty();
    }

    @Override
    public List<Material> cariByKategori(String kategori) {
        // SELECT * FROM material WHERE kategori = ? AND status = 'AKTIF'
        return new ArrayList<>();
    }

    @Override
    public List<Material> cariAktif() {
        // SELECT * FROM material WHERE status = 'AKTIF'
        return new ArrayList<>();
    }

    @Override
    public List<Material> cariDibawahStokMinimum() {
        // SELECT m.* FROM material m
        // JOIN stock_card sc ON m.kode = sc.kode_material
        // WHERE sc.qty_on_hand < m.stok_minimum
        return new ArrayList<>();
    }
}

// Implementasi pakai memory (untuk testing)
class MaterialRepositoryMemory implements MaterialRepository {
    private Map<String, Material> storage = new HashMap<>();

    @Override
    public void simpan(Material material) {
        storage.put(material.getKodeMaterial(), material);
    }

    @Override
    public Optional<Material> cariByKode(String kodeMaterial) {
        return Optional.ofNullable(storage.get(kodeMaterial));
    }

    @Override
    public List<Material> cariByKategori(String kategori) {
        return new ArrayList<>(storage.values());
    }

    @Override
    public List<Material> cariAktif() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public List<Material> cariDibawahStokMinimum() {
        return new ArrayList<>(storage.values());
    }
}
```

> **Kunci Repository:** Interface di domain, implementasi di infrastructure. **Bisa ganti database tanpa ubah logika bisnis.**

---

## 5️⃣ Domain Event — Kejadian Penting

**Domain Event** adalah catatan bahwa **sesuatu yang penting telah terjadi** di domain. Event ini bisa memicu aksi lain di bagian sistem yang berbeda.

### Analogi: Pengumuman di Kantor

_"Pesanan #001 sudah dibayar!"_ → Bagian gudang mulai packing, bagian finance mencatat, bagian CS mengirim konfirmasi ke pelanggan.

```java
import java.time.LocalDateTime;

/**
 * DOMAIN EVENT: Sesuatu yang sudah terjadi
 */
class PesananDibayarEvent {
    private final String nomorPesanan;
    private final Uang jumlahBayar;
    private final LocalDateTime waktu;

    public PesananDibayarEvent(String nomorPesanan, Uang jumlahBayar) {
        this.nomorPesanan = nomorPesanan;
        this.jumlahBayar = jumlahBayar;
        this.waktu = LocalDateTime.now();
    }

    public String getNomorPesanan() { return nomorPesanan; }
    public Uang getJumlahBayar() { return jumlahBayar; }
    public LocalDateTime getWaktu() { return waktu; }

    @Override
    public String toString() {
        return "📢 Event: Pesanan " + nomorPesanan
            + " dibayar " + jumlahBayar + " pada " + waktu;
    }
}
```

### 📦 Contoh Kasus Real: Domain Event di ERP Inventory/MM

| Modul | Event | Trigger | Reaksi yang Terjadi |
|:------|:------|:--------|:--------------------|
| **Inventory** | `StokDibawahMinimum` | Stok on-hand < stok minimum | Buat Purchase Requisition otomatis, notif Purchasing |
| **Purchasing** | `PurchaseOrderDisetujui` | Manager approve PO | Kirim PO ke vendor, update budget, catat komitmen |
| **Warehouse** | `BarangDiterima` | Goods Receipt di-posting | Update stok, update status PO, notif Quality Control |
| **Quality** | `InspeksiGagal` | Material tidak lolos QC | Buat Return Order, blokir material, notif vendor |
| **Inventory** | `StockOpnameDilakukan` | Hitung fisik selesai | Adjustment stok, catat selisih, notif Finance |

```java
// 📉 Contoh Real: Event Stok di Bawah Minimum
class StokDibawahMinimumEvent {
    private final String kodeMaterial;
    private final String namaMaterial;
    private final Quantity stokSaatIni;
    private final Quantity stokMinimum;
    private final LocalDateTime waktu;

    public StokDibawahMinimumEvent(String kodeMaterial, String namaMaterial,
                                    Quantity stokSaatIni, Quantity stokMinimum) {
        this.kodeMaterial = kodeMaterial;
        this.namaMaterial = namaMaterial;
        this.stokSaatIni = stokSaatIni;
        this.stokMinimum = stokMinimum;
        this.waktu = LocalDateTime.now();
    }

    public String getKodeMaterial() { return kodeMaterial; }
    public Quantity getStokSaatIni() { return stokSaatIni; }
    public Quantity getStokMinimum() { return stokMinimum; }
}

// Siapa yang bereaksi terhadap event ini?
class InventoryEventHandler {
    public void handle(StokDibawahMinimumEvent event) {
        System.out.println("⚠️ Stok kritis: " + event.getKodeMaterial()
            + " sisa " + event.getStokSaatIni()
            + " (minimum: " + event.getStokMinimum() + ")");
        // → Modul Purchasing: buat Purchase Requisition otomatis
        // → Modul Warehouse: tandai material sebagai "KRITIS"
        // → Modul Notification: kirim email ke tim procurement
        // → Dashboard: tampilkan alert di halaman monitoring
    }
}

// 📥 Contoh Real: Event Barang Diterima di Gudang
class BarangDiterimaEvent {
    private final String nomorGR;
    private final String nomorPO;
    private final String kodeMaterial;
    private final Quantity jumlahDiterima;
    private final LokasiRak lokasi;
    private final LocalDateTime waktu;

    public BarangDiterimaEvent(String nomorGR, String nomorPO,
                                String kodeMaterial, Quantity jumlahDiterima,
                                LokasiRak lokasi) {
        this.nomorGR = nomorGR;
        this.nomorPO = nomorPO;
        this.kodeMaterial = kodeMaterial;
        this.jumlahDiterima = jumlahDiterima;
        this.lokasi = lokasi;
        this.waktu = LocalDateTime.now();
    }

    public String getNomorPO() { return nomorPO; }
    public String getKodeMaterial() { return kodeMaterial; }
    public Quantity getJumlahDiterima() { return jumlahDiterima; }
}

class WarehouseEventHandler {
    public void handle(BarangDiterimaEvent event) {
        System.out.println("📥 GR " + event.getNomorGR()
            + ": " + event.getKodeMaterial()
            + " diterima " + event.getJumlahDiterima());
        // → Modul Inventory: tambah stok on-hand
        // → Modul Purchasing: update status PO (partial/complete)
        // → Modul Quality: buat Inspection Lot jika material perlu QC
        // → Modul Finance: catat accrual (hutang ke vendor)
    }
}
```

> **Kunci Domain Event:** Nama event selalu **past tense** (sudah terjadi). Satu event bisa memicu **banyak reaksi** di modul ERP yang berbeda.

---

## 6️⃣ Domain Service — Logika Lintas Entity

**Domain Service** berisi logika bisnis yang **tidak cocok ditempatkan di satu Entity tertentu** — biasanya melibatkan beberapa Entity atau Aggregate sekaligus.

### Analogi: Notaris

Kalau kamu jual rumah ke orang lain, siapa yang proses transaksinya? Bukan penjual, bukan pembeli — tapi **notaris** (pihak ketiga yang netral). Notaris = Domain Service.

### Kapan Pakai Domain Service?

| Situasi | Contoh di ERP/MM |
|:--------|:-----------------|
| Logika melibatkan **2+ Aggregate** | Transfer stok antar gudang (2 StockCard) |
| Tidak jelas milik Entity mana | Hitung valuasi material (tergantung metode, harga, qty) |
| Operasi kalkulasi kompleks | Perhitungan harga rata-rata bergerak (Moving Average) |
| Integrasi aturan bisnis lintas modul | Cek stok minimum → buat Purchase Requisition otomatis |

### 📦 Contoh Kasus Real: Domain Service di ERP Inventory/MM

| Modul | Service | Kenapa Bukan di Entity? |
|:------|:--------|:------------------------|
| **Inventory** | StockTransferService | Melibatkan 2 gudang/lokasi sekaligus |
| **Purchasing** | ReorderService | Melibatkan Material, StockCard, dan PurchaseRequisition |
| **Valuation** | MaterialValuationService | Tergantung metode (FIFO, Moving Avg) — bukan milik 1 entity |
| **Warehouse** | BinAllocationService | Melibatkan material, kapasitas rak, dan aturan penyimpanan |
| **Quality** | VendorScoringService | Melibatkan vendor, GR history, dan inspection results |

```java
// 🔄 Contoh Real: Transfer Stok Antar Gudang
/**
 * DOMAIN SERVICE: StockTransferService
 * Transfer stok melibatkan 2 lokasi — tidak adil jika ditaruh
 * di salah satu Entity StockCard saja.
 */
class StockTransferService {

    public void transfer(StockCard gudangAsal, StockCard gudangTujuan,
                         Quantity jumlah) {
        // Validasi bisnis
        if (jumlah.getJumlah() <= 0) {
            throw new IllegalArgumentException("Jumlah transfer harus > 0");
        }

        // Proses: kurangi dari asal, tambah ke tujuan
        gudangAsal.keluarkan(jumlah);     // Gagal jika stok tidak cukup
        gudangTujuan.masukkan(jumlah);

        System.out.println("✅ Transfer " + jumlah + " berhasil."
            + " Dari: " + gudangAsal.getLokasi()
            + " → Ke: " + gudangTujuan.getLokasi());
    }
}

// StockCard yang digunakan oleh service di atas
class StockCard {
    private final String kodeMaterial;
    private final LokasiRak lokasi;
    private Quantity stokOnHand;

    public StockCard(String kodeMaterial, LokasiRak lokasi, Quantity stokAwal) {
        this.kodeMaterial = kodeMaterial;
        this.lokasi = lokasi;
        this.stokOnHand = stokAwal;
    }

    public void masukkan(Quantity qty) {
        this.stokOnHand = this.stokOnHand.tambah(qty);
    }

    public void keluarkan(Quantity qty) {
        if (qty.lebihDari(stokOnHand)) {
            throw new IllegalStateException(
                "Stok tidak cukup! On-hand: " + stokOnHand);
        }
        this.stokOnHand = this.stokOnHand.kurangi(qty);
    }

    public Quantity getStokOnHand() { return stokOnHand; }
    public LokasiRak getLokasi() { return lokasi; }
}

// 📉 Contoh Real: Auto-Reorder (Buat PR otomatis saat stok kritis)
/**
 * DOMAIN SERVICE: ReorderService
 * Melibatkan Material, StockCard, dan PurchaseRequisition
 * — bukan milik satu Entity tertentu.
 */
class ReorderService {
    private final MaterialRepository materialRepo;

    public ReorderService(MaterialRepository materialRepo) {
        this.materialRepo = materialRepo;
    }

    public void cekDanReorder(StockCard stockCard, Material material) {
        Quantity minimum = new Quantity(material.getStokMinimum(),
                                        material.getSatuan());

        if (!stockCard.getStokOnHand().lebihDari(minimum)) {
            // Stok di bawah minimum → buat Purchase Requisition
            Quantity reorderQty = hitungReorderQuantity(material);

            System.out.println("📋 Auto-PR dibuat: "
                + material.getKodeMaterial()
                + " qty: " + reorderQty
                + " (stok sekarang: " + stockCard.getStokOnHand()
                + ", minimum: " + minimum + ")");
        }
    }

    private Quantity hitungReorderQuantity(Material material) {
        // Logika bisnis: pesan 2x stok minimum
        return new Quantity(
            material.getStokMinimum() * 2,
            material.getSatuan()
        );
    }
}

// 💰 Contoh Real: Material Valuation (Hitung Nilai Material)
/**
 * DOMAIN SERVICE: MaterialValuationService
 * Hitung valuasi tergantung metode — FIFO, LIFO, Moving Average
 * — bukan milik entity Material atau StockCard
 */
class MaterialValuationService {

    public Uang hitungNilaiStok(Quantity stokOnHand,
                                 List<Uang> riwayatHargaBeli,
                                 String metode) {
        switch (metode) {
            case "MOVING_AVERAGE":
                return hitungMovingAverage(stokOnHand, riwayatHargaBeli);
            case "LAST_PRICE":
                return hitungLastPrice(stokOnHand, riwayatHargaBeli);
            default:
                throw new IllegalArgumentException(
                    "Metode valuasi tidak dikenal: " + metode);
        }
    }

    private Uang hitungMovingAverage(Quantity stok, List<Uang> hargaList) {
        double totalHarga = 0;
        for (Uang harga : hargaList) {
            totalHarga += harga.getJumlah();
        }
        double rataRata = totalHarga / hargaList.size();
        double nilaiTotal = rataRata * stok.getJumlah();
        return new Uang(nilaiTotal, "IDR");
    }

    private Uang hitungLastPrice(Quantity stok, List<Uang> hargaList) {
        Uang hargaTerakhir = hargaList.get(hargaList.size() - 1);
        double nilaiTotal = hargaTerakhir.getJumlah() * stok.getJumlah();
        return new Uang(nilaiTotal, "IDR");
    }
}
```

> **Kunci Domain Service:** Logika bisnis yang **bukan milik satu Entity**. Tanpa state sendiri — hanya menjalankan operasi.

---

## 🔍 Ubiquitous Language — Bahasa Bersama

Salah satu prinsip terpenting DDD adalah **Ubiquitous Language**: developer dan ahli bisnis harus menggunakan **istilah yang sama**.

```
❌ JANGAN:                          ✅ LAKUKAN:
─────────────                       ─────────────
"record"                            "Pesanan"
"status_flag = 1"                   "Pesanan dikonfirmasi"
"insert into order_table"           "simpan Pesanan ke Repository"
"process()"                         "konfirmasiPesanan()"
"data.total_amount"                 "pesanan.hitungTotal()"
```

---

## 📋 Checklist DDD

- [ ] Setiap **Entity** punya identitas unik (ID)
- [ ] **Value Object** dibuat immutable (tidak bisa diubah)
- [ ] **Aggregate** punya satu Root yang menjaga invariant
- [ ] Akses ke entity internal selalu **lewat Aggregate Root**
- [ ] **Repository** adalah interface — implementasi bisa diganti
- [ ] **Domain Event** merepresentasikan kejadian yang sudah terjadi (past tense)
- [ ] Nama class dan method mengikuti **Ubiquitous Language** bisnis

---

## 🔗 Navigasi

| Sebelumnya | Berikutnya |
|:-----------|:-----------|
| [📖 ← Design Patterns](05-design-patterns.md) | [📖 Hexagonal Architecture →](07-hexagonal-architecture.md) |

---

<p align="center"><i>"DDD bukan tentang teknologi — tapi tentang memahami bisnis dan menuangkannya ke dalam kode."</i></p>

---

<p align="center">
  <b>Wahyu Amaldi, M.Kom</b> · Universitas Cakrawala
</p>
