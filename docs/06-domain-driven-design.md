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
