# 🧩 Bab 5: Design Patterns untuk Bisnis

> **Penulis:** Wahyu Amaldi, M.Kom · **Institusi:** Universitas Cakrawala

[⬅️ Abstraction](04-abstraction.md) · [⬅️ Beranda](../README.md) · [DDD ➡️](06-domain-driven-design.md)

---

## 📌 Apa Itu Design Pattern?

**Design Pattern** adalah **solusi yang sudah terbukti** untuk masalah desain yang sering muncul dalam pengembangan perangkat lunak. Bukan kode siap copy-paste, melainkan **template berpikir** yang bisa kamu adaptasi ke situasi nyata.

> **Inti dari Design Pattern:** _"Jangan reinvent the wheel — gunakan solusi yang sudah teruji oleh ribuan developer sebelummu."_

---

## 🧠 Analogi Dunia Nyata

### 🍳 Resep Masakan

Bayangkan kamu ingin membuat **nasi goreng**. Kamu tidak perlu menemukan cara memasak dari nol — sudah ada **resep** yang terbukti berhasil:

| Resep (Pattern) | Situasi | Hasil |
|:-----------------|:--------|:------|
| Resep Nasi Goreng | Bahan: nasi, bumbu, telur | Nasi goreng enak |
| Resep Rendang | Bahan: daging, santan, bumbu | Rendang empuk |

- **Resep** = Design Pattern (panduan solusi)
- **Bahan** = Kode program kamu
- **Hasil masakan** = Software yang berjalan baik

Kamu bisa modifikasi resep sesuai selera, tapi kerangka dasarnya sudah terbukti.

---

## 🔑 Empat Pattern Kunci untuk Domain Bisnis

Kita akan membahas 4 pattern yang paling relevan untuk membangun sistem bisnis:

```
┌──────────────────────────────────────────────────────┐
│                 DESIGN PATTERNS                       │
│              untuk Domain Bisnis                      │
├──────────────┬───────────────────────────────────────┤
│  STRATEGY    │  Pilih algoritma saat runtime          │
│  OBSERVER    │  Notifikasi otomatis saat ada perubahan│
│  FACTORY     │  Buat objek tanpa tahu class konkret   │
│  STATE       │  Ubah perilaku berdasarkan kondisi     │
└──────────────┴───────────────────────────────────────┘
```

---

## 1️⃣ Strategy Pattern

### Masalah

Bayangkan kamu punya toko online. Setiap pelanggan bisa dapat **diskon berbeda**: pelanggan baru dapat 5%, member gold dapat 15%, dan saat harbolnas diskon 30%. Bagaimana menangani ini tanpa `if-else` bertubi-tubi?

### Solusi: Strategy Pattern

Setiap jenis diskon dipisahkan menjadi **class tersendiri** yang mengimplementasi satu interface yang sama.

```
        ┌──────────────────────┐
        │  <<interface>>       │
        │  StrategiDiskon      │
        │──────────────────────│
        │ + hitungDiskon(harga)│
        └──────────┬───────────┘
                   │ implements
         ┌─────────┼──────────┐
         │         │          │
    ┌────┴────┐ ┌──┴────┐ ┌──┴─────────┐
    │ Diskon  │ │Diskon │ │ Diskon     │
    │ Member  │ │ Gold  │ │ Harbolnas  │
    │  (5%)   │ │ (15%) │ │  (30%)     │
    └─────────┘ └───────┘ └────────────┘
```

### Contoh Kode

```java
// INTERFACE: Kontrak strategi diskon
interface StrategiDiskon {
    double hitungDiskon(double harga);
    String getNamaDiskon();
}

// STRATEGI 1: Diskon member biasa (5%)
class DiskonMember implements StrategiDiskon {
    @Override
    public double hitungDiskon(double harga) {
        return harga * 0.05;
    }

    @Override
    public String getNamaDiskon() {
        return "Member Biasa (5%)";
    }
}

// STRATEGI 2: Diskon Gold (15%)
class DiskonGold implements StrategiDiskon {
    @Override
    public double hitungDiskon(double harga) {
        return harga * 0.15;
    }

    @Override
    public String getNamaDiskon() {
        return "Member Gold (15%)";
    }
}

// STRATEGI 3: Diskon Harbolnas (30%)
class DiskonHarbolnas implements StrategiDiskon {
    @Override
    public double hitungDiskon(double harga) {
        return harga * 0.30;
    }

    @Override
    public String getNamaDiskon() {
        return "Harbolnas (30%)";
    }
}

// CONTEXT: Kasir yang menggunakan strategi
class Kasir {
    private StrategiDiskon strategi;

    // Strategi bisa diganti kapan saja!
    public void setStrategi(StrategiDiskon strategi) {
        this.strategi = strategi;
    }

    public void prosesPembayaran(String item, double harga) {
        double diskon = strategi.hitungDiskon(harga);
        double total = harga - diskon;

        System.out.println("Item     : " + item);
        System.out.println("Harga    : Rp " + String.format("%,.0f", harga));
        System.out.println("Diskon   : " + strategi.getNamaDiskon());
        System.out.println("Potongan : Rp " + String.format("%,.0f", diskon));
        System.out.println("Total    : Rp " + String.format("%,.0f", total));
        System.out.println("─────────────────────────");
    }
}
```

### Kapan Pakai?

| Gunakan Strategy Ketika | Contoh Nyata |
|:------------------------|:-------------|
| Ada banyak variasi algoritma | Diskon, pajak, ongkir |
| Algoritma bisa berubah saat runtime | Pelanggan upgrade member |
| Ingin menghindari `if-else` yang panjang | Metode pembayaran |

---

## 2️⃣ Observer Pattern

### Masalah

Di sistem e-commerce, saat **stok barang habis lalu tersedia kembali**, kamu ingin:
- Kirim email ke pelanggan yang menunggu
- Update tampilan di website
- Catat log ke sistem

Bagaimana agar semua ini terjadi **otomatis** tanpa saling bergantung?

### Solusi: Observer Pattern

Objek yang berubah (**Subject**) memberi tahu semua **Observer** yang terdaftar.

```
        ┌─────────────┐
        │   SUBJECT    │      "Stok berubah!"
        │   (Produk)   │──────────────────┐
        └──────────────┘                  │
               │ notifikasi               │
         ┌─────┼──────┐                   │
         │     │      │                   │
    ┌────┴──┐ ┌┴────┐ ┌┴──────┐           │
    │Email  │ │Web  │ │Logger │◄──────────┘
    │Notif  │ │View │ │System │
    └───────┘ └─────┘ └───────┘
    OBSERVER  OBSERVER  OBSERVER
```

### Contoh Kode

```java
import java.util.ArrayList;
import java.util.List;

// INTERFACE: Kontrak observer
interface PengamatStok {
    void update(String namaProduk, int stokBaru);
}

// SUBJECT: Produk yang dipantau
class Produk {
    private String nama;
    private int stok;
    private List<PengamatStok> pengamat = new ArrayList<>();

    public Produk(String nama, int stok) {
        this.nama = nama;
        this.stok = stok;
    }

    public void tambahPengamat(PengamatStok p) {
        pengamat.add(p);
    }

    public void setStok(int stokBaru) {
        this.stok = stokBaru;
        // Beritahu semua pengamat!
        for (PengamatStok p : pengamat) {
            p.update(nama, stokBaru);
        }
    }

    public String getNama() { return nama; }
    public int getStok() { return stok; }
}

// OBSERVER 1: Kirim email
class NotifikasiEmail implements PengamatStok {
    @Override
    public void update(String namaProduk, int stokBaru) {
        if (stokBaru > 0) {
            System.out.println("📧 Email: '" + namaProduk
                + "' kembali tersedia! Stok: " + stokBaru);
        }
    }
}

// OBSERVER 2: Update dashboard
class DashboardWeb implements PengamatStok {
    @Override
    public void update(String namaProduk, int stokBaru) {
        System.out.println("🖥️ Dashboard: Stok '" + namaProduk
            + "' diperbarui → " + stokBaru + " unit");
    }
}

// OBSERVER 3: Catat log
class LogSistem implements PengamatStok {
    @Override
    public void update(String namaProduk, int stokBaru) {
        System.out.println("📋 Log: [STOK_UPDATE] "
            + namaProduk + " = " + stokBaru);
    }
}
```

### Kapan Pakai?

| Gunakan Observer Ketika | Contoh Nyata |
|:------------------------|:-------------|
| Satu perubahan harus diberitahukan ke banyak pihak | Notifikasi, event system |
| Observer bisa ditambah/dihapus dinamis | Subscribe/unsubscribe |
| Subject tidak perlu tahu detail tiap observer | Loose coupling |

---

## 3️⃣ Factory Pattern

### Masalah

Kamu punya sistem yang membuat berbagai jenis dokumen: **Invoice**, **Kwitansi**, **Faktur**. Kode pemanggil tidak perlu tahu detail pembuatan masing-masing — cukup bilang "buatkan dokumen jenis X".

### Solusi: Factory Pattern

Satu class **factory** bertanggung jawab membuat objek yang tepat berdasarkan parameter.

```
     Client: "Buatkan Invoice!"
               │
               ▼
     ┌─────────────────────┐
     │   DokumenFactory    │
     │─────────────────────│
     │ + buat(jenis): Dok  │
     └──────────┬──────────┘
                │ menghasilkan
       ┌────────┼────────┐
       │        │        │
  ┌────┴───┐ ┌─┴─────┐ ┌┴──────┐
  │Invoice │ │Kwitansi│ │Faktur │
  └────────┘ └───────┘ └───────┘
```

### Contoh Kode

```java
// INTERFACE: Semua dokumen harus bisa dicetak
interface Dokumen {
    void cetak();
    String getJenis();
}

// CONCRETE: Invoice
class Invoice implements Dokumen {
    @Override
    public void cetak() {
        System.out.println("📄 Mencetak INVOICE...");
        System.out.println("   No. INV-2026-001");
        System.out.println("   Tanggal: 7 Maret 2026");
    }

    @Override
    public String getJenis() { return "Invoice"; }
}

// CONCRETE: Kwitansi
class Kwitansi implements Dokumen {
    @Override
    public void cetak() {
        System.out.println("🧾 Mencetak KWITANSI...");
        System.out.println("   No. KWT-2026-001");
        System.out.println("   Sudah diterima pembayaran.");
    }

    @Override
    public String getJenis() { return "Kwitansi"; }
}

// CONCRETE: Faktur
class FakturPajak implements Dokumen {
    @Override
    public void cetak() {
        System.out.println("📋 Mencetak FAKTUR PAJAK...");
        System.out.println("   No. FP-2026-001");
        System.out.println("   PPN: 11%");
    }

    @Override
    public String getJenis() { return "Faktur Pajak"; }
}

// FACTORY: Buat dokumen berdasarkan jenis
class DokumenFactory {
    public static Dokumen buat(String jenis) {
        switch (jenis.toLowerCase()) {
            case "invoice":  return new Invoice();
            case "kwitansi": return new Kwitansi();
            case "faktur":   return new FakturPajak();
            default:
                throw new IllegalArgumentException(
                    "Jenis dokumen tidak dikenal: " + jenis);
        }
    }
}
```

### Kapan Pakai?

| Gunakan Factory Ketika | Contoh Nyata |
|:-----------------------|:-------------|
| Pembuatan objek melibatkan logika kondisional | Buat dokumen, buat user by role |
| Client tidak perlu tahu class konkret | Plugin system, modul pembayaran |
| Ingin sentralisasi pembuatan objek | Satu tempat untuk kontrol |

---

## 4️⃣ State Pattern

### Masalah

Sebuah **pesanan** di e-commerce memiliki status: `Baru → Diproses → Dikirim → Selesai`. Perilaku objek pesanan **berbeda** tergantung statusnya. Misalnya, pesanan yang sudah "Dikirim" tidak bisa dibatalkan.

### Solusi: State Pattern

Setiap status direpresentasikan sebagai **class tersendiri** yang menentukan perilaku pada status itu.

```
    ┌──────┐  bayar   ┌─────────┐  kirim  ┌────────┐  terima  ┌────────┐
    │ BARU │────────► │DIPROSES │────────►│DIKIRIM │────────►│SELESAI │
    └──────┘          └─────────┘         └────────┘         └────────┘
       │                                       │
       │ batal                                 │ batal?
       ▼                                       ▼
    ┌──────┐                             "Maaf, sudah
    │BATAL │                              dikirim!"
    └──────┘
```

### Contoh Kode

```java
// INTERFACE: Setiap state menentukan perilaku
interface StatusPesanan {
    void bayar(Pesanan pesanan);
    void kirim(Pesanan pesanan);
    void batalkan(Pesanan pesanan);
    String getNamaStatus();
}

// STATE: Baru
class StatusBaru implements StatusPesanan {
    @Override
    public void bayar(Pesanan p) {
        System.out.println("✅ Pembayaran diterima! Status → Diproses");
        p.setStatus(new StatusDiproses());
    }

    @Override
    public void kirim(Pesanan p) {
        System.out.println("❌ Belum dibayar, tidak bisa dikirim.");
    }

    @Override
    public void batalkan(Pesanan p) {
        System.out.println("✅ Pesanan dibatalkan.");
        p.setStatus(new StatusBatal());
    }

    @Override
    public String getNamaStatus() { return "BARU"; }
}

// STATE: Diproses
class StatusDiproses implements StatusPesanan {
    @Override
    public void bayar(Pesanan p) {
        System.out.println("⚠️ Sudah dibayar sebelumnya.");
    }

    @Override
    public void kirim(Pesanan p) {
        System.out.println("✅ Pesanan dikirim! Status → Dikirim");
        p.setStatus(new StatusDikirim());
    }

    @Override
    public void batalkan(Pesanan p) {
        System.out.println("✅ Pesanan dibatalkan. Refund diproses.");
        p.setStatus(new StatusBatal());
    }

    @Override
    public String getNamaStatus() { return "DIPROSES"; }
}

// STATE: Dikirim
class StatusDikirim implements StatusPesanan {
    @Override
    public void bayar(Pesanan p) {
        System.out.println("⚠️ Sudah dibayar.");
    }

    @Override
    public void kirim(Pesanan p) {
        System.out.println("⚠️ Sudah dalam pengiriman.");
    }

    @Override
    public void batalkan(Pesanan p) {
        System.out.println("❌ Tidak bisa dibatalkan — sudah dikirim!");
    }

    @Override
    public String getNamaStatus() { return "DIKIRIM"; }
}

// STATE: Batal
class StatusBatal implements StatusPesanan {
    @Override
    public void bayar(Pesanan p) {
        System.out.println("❌ Pesanan sudah dibatalkan.");
    }

    @Override
    public void kirim(Pesanan p) {
        System.out.println("❌ Pesanan sudah dibatalkan.");
    }

    @Override
    public void batalkan(Pesanan p) {
        System.out.println("⚠️ Sudah dibatalkan sebelumnya.");
    }

    @Override
    public String getNamaStatus() { return "BATAL"; }
}

// CONTEXT: Pesanan yang perilakunya berubah sesuai status
class Pesanan {
    private String id;
    private StatusPesanan status;

    public Pesanan(String id) {
        this.id = id;
        this.status = new StatusBaru();
    }

    public void setStatus(StatusPesanan status) {
        this.status = status;
    }

    public void bayar()    { status.bayar(this); }
    public void kirim()    { status.kirim(this); }
    public void batalkan() { status.batalkan(this); }

    public void tampilkan() {
        System.out.println("[" + id + "] Status: " + status.getNamaStatus());
    }
}
```

### Kapan Pakai?

| Gunakan State Ketika | Contoh Nyata |
|:---------------------|:-------------|
| Objek punya banyak status yang mengubah perilaku | Pesanan, tiket, izin cuti |
| Menghindari `if/switch` berdasarkan status | Workflow approval |
| Transisi antar-status perlu diatur ketat | Mesin ATM, proses bisnis |

---

## 🔗 Hubungan Design Pattern dengan 4 Pilar OOP

```
┌───────────────────────────────────────────────────────┐
│                                                       │
│  STRATEGY    → Polymorphism + Abstraction             │
│                (satu interface, banyak implementasi)   │
│                                                       │
│  OBSERVER    → Encapsulation + Polymorphism           │
│                (loose coupling, notifikasi polimorfik) │
│                                                       │
│  FACTORY     → Abstraction + Encapsulation            │
│                (sembunyikan proses pembuatan objek)    │
│                                                       │
│  STATE       → Polymorphism + Encapsulation           │
│                (perilaku berubah sesuai internal state)│
│                                                       │
└───────────────────────────────────────────────────────┘
```

---

## 📋 Checklist Design Patterns

- [ ] Gunakan **Strategy** saat ada banyak variasi algoritma yang bisa diganti
- [ ] Gunakan **Observer** saat satu perubahan harus dikirim ke banyak listener
- [ ] Gunakan **Factory** saat pembuatan objek perlu disentralisasi
- [ ] Gunakan **State** saat perilaku objek berubah berdasarkan kondisi internal
- [ ] Jangan gunakan pattern hanya demi "keren" — pastikan ada masalah nyata yang diselesaikan

---

## 🔗 Navigasi

| Sebelumnya | Berikutnya |
|:-----------|:-----------|
| [📖 ← Abstraction](04-abstraction.md) | [📖 Domain Driven Design →](06-domain-driven-design.md) |

---

<p align="center"><i>"Design Pattern bukan tentang menghafal nama, tapi tentang mengenali masalah dan menerapkan solusi yang tepat."</i></p>

---

<p align="center">
  <b>Wahyu Amaldi, M.Kom</b> · Universitas Cakrawala
</p>
