# 💊 Pilar 1: Encapsulation (Enkapsulasi)

> **Penulis:** Wahyu Amaldi, M.Kom · **Institusi:** Universitas Cakrawala

[⬅️ Kembali ke Beranda](../README.md) · [Pilar Selanjutnya: Inheritance ➡️](02-inheritance.md)

---

## 📌 Definisi

**Encapsulation** adalah mekanisme **membungkus data (variabel)** dan **kode yang beroperasi pada data tersebut (method)** ke dalam satu unit tunggal (class), serta **membatasi akses langsung** ke beberapa komponen objek.

> **Inti dari Encapsulation:** _"Sembunyikan detail internal, tampilkan hanya yang perlu diketahui."_

---

## 🧠 Analogi Dunia Nyata

### 🏧 Mesin ATM

Bayangkan kamu menggunakan **mesin ATM**:

| Yang Kamu Lihat (Public) | Yang Tersembunyi (Private) |
|:--------------------------|:---------------------------|
| Layar, tombol, slot kartu | Brankas uang di dalam mesin |
| Tombol "Tarik Tunai" | Algoritma verifikasi PIN |
| Struk transaksi | Koneksi ke server bank |
| Saldo di layar | Database nasabah |

Kamu **tidak perlu tahu** cara mesin menghubungi server bank. Kamu cukup:
1. Masukkan kartu
2. Ketik PIN
3. Pilih nominal
4. Ambil uang

**Itulah encapsulation!** Detail rumit disembunyikan, kamu hanya berinteraksi lewat "antarmuka" yang sederhana.

### 💊 Kapsul Obat

Seperti **kapsul obat** — isinya (zat aktif) dibungkus dalam cangkang. Kamu tidak perlu tahu komposisi kimianya, cukup telan kapsulnya.

---

## 🔑 Konsep Kunci

### 1. Access Modifiers (Pengatur Akses)

Access Modifier adalah **kata kunci** yang mengatur **siapa boleh mengakses** suatu variabel atau method. Anggap seperti **sistem keamanan gedung**:

```
┌──────────────────────────────────────────────────────────────────┐
│                🏢 ANALOGI GEDUNG KANTOR                          │
├──────────────┬───────────────────────────────────────────────────┤
│  public      │  🌍 Lobby — SIAPA SAJA boleh masuk               │
│  protected   │  🏠 Ruang keluarga — hanya karyawan & anak       │
│              │     perusahaan (subclass)                         │
│  (default)   │  🏢 Lantai kantor — hanya karyawan 1 divisi      │
│              │     (1 package)                                   │
│  private     │  🔐 Brankas — HANYA pemilik (class itu sendiri)  │
└──────────────┴───────────────────────────────────────────────────┘
```

Java menyediakan **4 level akses**, dari paling terbuka hingga paling tertutup:

```
┌─────────────────────────────────────────────────────────────┐
│                    Tingkat Akses                             │
├──────────────┬──────────┬───────────┬──────────┬────────────┤
│  Modifier    │  Class   │  Package  │ Subclass │  Luar      │
│              │  Sendiri │  yg Sama  │ (Anak)   │  Semua     │
├──────────────┼──────────┼───────────┼──────────┼────────────┤
│  public      │    ✅    │    ✅     │    ✅    │    ✅      │
│  protected   │    ✅    │    ✅     │    ✅    │    ❌      │
│  (default)   │    ✅    │    ✅     │    ❌    │    ❌      │
│  private     │    ✅    │    ❌     │    ❌    │    ❌      │
└──────────────┴──────────┴───────────┴──────────┴────────────┘
```

Berikut penjelasan tiap modifier:

---

#### 🌍 `public` — Terbuka untuk Semua

Bisa diakses dari **mana saja**, oleh **siapa saja**.

```java
public class Toko {
    public String nama = "Toko Sejahtera";  // Semua orang bisa lihat nama toko
    
    public void buka() {                     // Semua orang bisa panggil method ini
        System.out.println("Toko sudah buka!");
    }
}

// Dari class MANAPUN:
Toko toko = new Toko();
System.out.println(toko.nama);  // ✅ Bisa
toko.buka();                     // ✅ Bisa
```

> 📌 **Kapan pakai?** Untuk method/variabel yang memang **perlu diakses dari luar** (misalnya getter, setter, API publik).

---

#### 🛡️ `protected` — Keluarga & Satu Kantor

Bisa diakses oleh:
- Class itu sendiri ✅
- Class lain **dalam package yang sama** ✅
- **Subclass** (class anak), walau beda package ✅

```java
// 📁 File: paket_hewan/Hewan.java
package paket_hewan;

public class Hewan {
    protected String nama;   // Protected: bisa diakses subclass

    protected void bernapas() {
        System.out.println(nama + " bernapas...");
    }
}

// 📁 File: paket_kucing/Kucing.java  (BEDA package!)
package paket_kucing;
import paket_hewan.Hewan;

public class Kucing extends Hewan {
    public void tampilkan() {
        nama = "Milo";          // ✅ Bisa! Kucing adalah subclass Hewan
        bernapas();              // ✅ Bisa! Method protected diwarisi
    }
}

// 📁 File: paket_lain/Orang.java  (BUKAN subclass)
package paket_lain;
import paket_hewan.Hewan;

public class Orang {
    public void coba() {
        Hewan h = new Hewan();
        h.nama = "Rex";        // ❌ ERROR! Orang bukan subclass Hewan
    }
}
```

> 📌 **Kapan pakai?** Ketika kamu ingin **subclass bisa mengakses**, tapi **class luar yang bukan keluarga tidak boleh**.

---

#### 📦 `(default)` — Tanpa Keyword (Package-Private)

Jika **tidak menulis** access modifier apapun, maka aksesnya otomatis **default** (package-private).  
Hanya bisa diakses oleh class **dalam package yang sama**.

```java
// 📁 File: toko/Kasir.java
package toko;

class Kasir {                    // Default class — hanya dikenal di package "toko"
    String namaKasir;            // Default field
    
    void hitungTotal() {         // Default method
        System.out.println("Menghitung total...");
    }
}

// 📁 File: toko/Manajer.java   (SAMA package)
package toko;

class Manajer {
    void cekKasir() {
        Kasir kasir = new Kasir();
        kasir.namaKasir = "Ani";     // ✅ Bisa! Satu package
        kasir.hitungTotal();          // ✅ Bisa! Satu package
    }
}

// 📁 File: pelanggan/Pembeli.java  (BEDA package)
package pelanggan;
// import toko.Kasir;  // ❌ ERROR! Kasir tidak bisa diimport (default class)
```

> 📌 **Kapan pakai?** Untuk class/method yang **hanya dibutuhkan internal** di dalam satu package. Jarang digunakan secara eksplisit.

---

#### 🔐 `private` — Hanya Milik Sendiri

**Paling ketat.** Hanya bisa diakses dari **dalam class itu sendiri**. Bahkan subclass pun tidak bisa.

```java
public class AkunBank {
    private String pin;              // 🔐 Hanya AkunBank yang tahu
    private double saldo;            // 🔐 Hanya AkunBank yang tahu

    private boolean cekPin(String input) {  // 🔐 Method rahasia internal
        return pin.equals(input);
    }

    // Method PUBLIC sebagai "pintu resmi" untuk akses data private
    public double getSaldo(String inputPin) {
        if (cekPin(inputPin)) {      // ✅ Bisa panggil cekPin() dari dalam
            return saldo;
        }
        return -1;                    // PIN salah
    }
}

// Dari luar:
AkunBank akun = new AkunBank();
akun.pin;                 // ❌ ERROR! private
akun.saldo;               // ❌ ERROR! private
akun.cekPin("1234");       // ❌ ERROR! private
akun.getSaldo("1234");     // ✅ Bisa! Lewat method public
```

> 📌 **Kapan pakai?** Untuk **semua data internal** yang tidak boleh diubah sembarangan. **Ini adalah default pilihan terbaik** untuk field — selalu mulai dengan `private`!

---

#### 🎯 Panduan Cepat: Pilih Access Modifier yang Tepat

```
  Mulai dari sini
       │
       ▼
  ┌─────────────────────────────────────┐
  │ Apakah HARUS diakses dari luar?     │
  └──────────┬────────────┬─────────────┘
             │            │
            YA          TIDAK
             │            │
             ▼            ▼
        ┌─────────┐  🔐 private ✅
        │ Apakah  │
        │ hanya   │
        │ oleh    │
        │ subclass│
        │ saja?   │
        └──┬───┬──┘
          YA  TIDAK
           │    │
           ▼    ▼
     🛡️ protected  🌍 public
```

**Aturan emas:** _Selalu mulai dengan `private`, baru longgarkan jika memang diperlukan._

---

### 2. Getter & Setter

**Getter** → method untuk **membaca** nilai variabel private  
**Setter** → method untuk **mengubah** nilai variabel private (dengan validasi)

```
┌──────────────────────────────────────────┐
│              Class: Akun                 │
├──────────────────────────────────────────┤
│  🔒 private: saldo                      │
│  🔒 private: pin                        │
├──────────────────────────────────────────┤
│  🔓 public: getSaldo()    ← Getter      │
│  🔓 public: setSaldo()    ← Setter      │
│  🔓 public: cekPin()      ← Validator   │
└──────────────────────────────────────────┘
```

---

## 💻 Contoh Kode

### ❌ Tanpa Encapsulation (Buruk)

```java
// BURUK: Semua data bisa diakses dan diubah bebas!
class AkunBank {
    public String namaPemilik;
    public double saldo;
    public String pin;
}

class Main {
    public static void main(String[] args) {
        AkunBank akun = new AkunBank();
        akun.saldo = -50000;       // 😱 Saldo negatif? Boleh aja!
        akun.pin = "";              // 😱 PIN kosong? Silakan!
        akun.namaPemilik = null;    // 😱 Nama null? Tidak ada yang cegah!
    }
}
```

**Masalahnya:**
- Tidak ada validasi → data bisa diisi sembarangan
- Siapapun bisa mengubah saldo langsung → tidak aman
- Tidak ada kontrol terhadap integritas data

---

### ✅ Dengan Encapsulation (Baik)

```java
/**
 * Contoh penerapan Encapsulation pada sistem rekening bank.
 * 
 * Semua atribut dibuat PRIVATE dan hanya bisa diakses
 * melalui method PUBLIC yang memiliki validasi.
 */
public class BankAccount {

    // ═══════════════════════════════════════════
    // 🔒 PRIVATE: Data tersembunyi dari luar
    // ═══════════════════════════════════════════
    private String namaPemilik;
    private double saldo;
    private String pin;
    private String nomorRekening;

    // ═══════════════════════════════════════════
    // 🏗️ CONSTRUCTOR: Inisialisasi dengan validasi
    // ═══════════════════════════════════════════
    public BankAccount(String namaPemilik, String pin, String nomorRekening) {
        setNamaPemilik(namaPemilik);  // Validasi via setter
        setPin(pin);                  // Validasi via setter
        this.nomorRekening = nomorRekening;
        this.saldo = 0;              // Saldo awal selalu 0
    }

    // ═══════════════════════════════════════════
    // 🔓 GETTER: Membaca data (read-only)
    // ═══════════════════════════════════════════

    /** Mendapatkan nama pemilik rekening */
    public String getNamaPemilik() {
        return namaPemilik;
    }

    /** 
     * Mendapatkan saldo saat ini.
     * Perhatikan: tidak ada setSaldo() publik!
     * Saldo hanya bisa berubah via setor() atau tarik().
     */
    public double getSaldo() {
        return saldo;
    }

    /** 
     * Nomor rekening di-mask untuk keamanan.
     * Contoh: "1234567890" → "******7890"
     */
    public String getNomorRekening() {
        int panjang = nomorRekening.length();
        String akhir = nomorRekening.substring(panjang - 4);
        return "******" + akhir;
    }

    // ═══════════════════════════════════════════
    // 🔓 SETTER: Mengubah data (dengan validasi)
    // ═══════════════════════════════════════════

    /** Mengubah nama pemilik — harus minimal 3 karakter */
    public void setNamaPemilik(String namaPemilik) {
        if (namaPemilik == null || namaPemilik.trim().length() < 3) {
            throw new IllegalArgumentException(
                "❌ Nama pemilik harus minimal 3 karakter!"
            );
        }
        this.namaPemilik = namaPemilik.trim();
    }

    /** Mengubah PIN — harus tepat 6 digit angka */
    public void setPin(String pin) {
        if (pin == null || !pin.matches("\\d{6}")) {
            throw new IllegalArgumentException(
                "❌ PIN harus terdiri dari tepat 6 digit angka!"
            );
        }
        this.pin = pin;
    }

    // ═══════════════════════════════════════════
    // 💰 BUSINESS LOGIC: Operasi dengan validasi
    // ═══════════════════════════════════════════

    /** 
     * Setor uang ke rekening.
     * @param jumlah harus lebih dari 0
     */
    public void setor(double jumlah) {
        if (jumlah <= 0) {
            throw new IllegalArgumentException(
                "❌ Jumlah setoran harus lebih dari 0!"
            );
        }
        saldo += jumlah;
        System.out.println("✅ Setor Rp " + formatRupiah(jumlah) + " berhasil.");
        tampilkanSaldo();
    }

    /** 
     * Tarik uang dari rekening.
     * @param jumlah harus lebih dari 0 dan tidak melebihi saldo
     * @param pinInput PIN harus cocok untuk verifikasi
     */
    public void tarik(double jumlah, String pinInput) {
        // Validasi PIN
        if (!verifikasiPin(pinInput)) {
            System.out.println("❌ PIN salah! Transaksi dibatalkan.");
            return;
        }

        // Validasi jumlah
        if (jumlah <= 0) {
            throw new IllegalArgumentException(
                "❌ Jumlah penarikan harus lebih dari 0!"
            );
        }

        // Validasi saldo cukup
        if (jumlah > saldo) {
            System.out.println("❌ Saldo tidak mencukupi!");
            System.out.println("   Saldo saat ini: Rp " + formatRupiah(saldo));
            return;
        }

        saldo -= jumlah;
        System.out.println("✅ Tarik Rp " + formatRupiah(jumlah) + " berhasil.");
        tampilkanSaldo();
    }

    // ═══════════════════════════════════════════
    // 🔒 PRIVATE HELPER: Tidak bisa diakses dari luar
    // ═══════════════════════════════════════════

    /** Verifikasi PIN — method PRIVATE, hanya untuk internal */
    private boolean verifikasiPin(String pinInput) {
        return this.pin.equals(pinInput);
    }

    /** Format angka ke format Rupiah */
    private String formatRupiah(double jumlah) {
        return String.format("%,.0f", jumlah);
    }

    /** Tampilkan saldo saat ini */
    private void tampilkanSaldo() {
        System.out.println("   Saldo saat ini: Rp " + formatRupiah(saldo));
    }

    // ═══════════════════════════════════════════
    // ▶️ MAIN: Demonstrasi
    // ═══════════════════════════════════════════
    public static void main(String[] args) {
        System.out.println("══════════════════════════════════════");
        System.out.println("  DEMO ENCAPSULATION — REKENING BANK  ");
        System.out.println("══════════════════════════════════════\n");

        // Buat akun baru
        BankAccount akun = new BankAccount("Budi Santoso", "123456", "9876543210");

        // Tampilkan info (nomor rekening ter-mask!)
        System.out.println("Pemilik : " + akun.getNamaPemilik());
        System.out.println("No. Rek : " + akun.getNomorRekening()); // ******3210
        System.out.println();

        // Setor uang
        akun.setor(5000000);     // ✅ Berhasil
        System.out.println();

        // Tarik uang dengan PIN benar
        akun.tarik(1500000, "123456");  // ✅ Berhasil
        System.out.println();

        // Tarik uang dengan PIN salah
        akun.tarik(500000, "000000");   // ❌ PIN salah!
        System.out.println();

        // Coba akses langsung?
        // akun.saldo = -999;           // ❌ COMPILE ERROR! saldo is private
        // akun.pin = "hack";           // ❌ COMPILE ERROR! pin is private
        // akun.verifikasiPin("123");    // ❌ COMPILE ERROR! method is private

        System.out.println("══════════════════════════════════════");
    }
}
```

**Output:**
```
══════════════════════════════════════
  DEMO ENCAPSULATION — REKENING BANK  
══════════════════════════════════════

Pemilik : Budi Santoso
No. Rek : ******3210

✅ Setor Rp 5,000,000 berhasil.
   Saldo saat ini: Rp 5,000,000

✅ Tarik Rp 1,500,000 berhasil.
   Saldo saat ini: Rp 3,500,000

❌ PIN salah! Transaksi dibatalkan.

══════════════════════════════════════
```

---

## 🔍 Mengapa Encapsulation Penting?

### 1. 🛡️ Keamanan Data
```java
// Tanpa encapsulation → siapapun bisa ubah saldo
akun.saldo = 999999999;  // 😱 Berbahaya!

// Dengan encapsulation → harus lewat method yang divalidasi
akun.setor(999999999);   // ✅ Terkontrol
```

### 2. 🔧 Mudah Diubah Tanpa Merusak
```java
// Tim developer bisa mengubah implementasi internal
// TANPA mengubah kode yang sudah menggunakan class ini

// Sebelum: saldo disimpan sebagai double
private double saldo;

// Sesudah: saldo disimpan sebagai BigDecimal (lebih akurat)
private BigDecimal saldo;

// Method publik TETAP SAMA → kode lain tidak perlu diubah!
public double getSaldo() { 
    return saldo.doubleValue(); 
}
```

### 3. ✅ Validasi Otomatis
```java
// Setiap perubahan data WAJIB melewati validasi
public void setPin(String pin) {
    if (!pin.matches("\\d{6}")) {
        throw new IllegalArgumentException("PIN harus 6 digit!");
    }
    this.pin = pin;
}
```

---

## 📋 Checklist Encapsulation

Apakah kode kamu sudah menerapkan encapsulation dengan benar?

- [ ] Semua variabel instance diberi akses `private`
- [ ] Getter disediakan hanya untuk field yang perlu dibaca dari luar
- [ ] Setter menyertakan **validasi** sebelum mengubah nilai
- [ ] Data sensitif (PIN, password) **tidak** punya getter yang mengembalikan nilai asli
- [ ] Method internal/helper diberi akses `private`
- [ ] Tidak ada field `public` tanpa alasan yang jelas

---

## ⚠️ Kesalahan Umum

| ❌ Salah | ✅ Benar |
|:---------|:---------|
| Semua field `public` | Field `private` + getter/setter |
| Getter/setter tanpa validasi | Setter dengan validasi ketat |
| Mengembalikan referensi objek mutable | Mengembalikan copy (defensive copy) |
| Terlalu banyak getter/setter | Hanya yang benar-benar diperlukan |

---

## 🔗 Navigasi

| Sebelumnya | Berikutnya |
|:-----------|:-----------|
| [🏠 Beranda](../README.md) | [📖 Inheritance →](02-inheritance.md) |

---

<p align="center"><i>"Encapsulation bukan soal menyembunyikan, tapi soal melindungi."</i></p>

---

<p align="center">
  <b>Wahyu Amaldi, M.Kom</b> · Universitas Cakrawala
</p>
