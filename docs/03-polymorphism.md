# 🎭 Pilar 3: Polymorphism (Polimorfisme)

> **Penulis:** Wahyu Amaldi, M.Kom · **Institusi:** Universitas Cakrawala

[⬅️ Inheritance](02-inheritance.md) · [⬅️ Beranda](../README.md) · [Abstraction ➡️](04-abstraction.md)

---

## 📌 Definisi

**Polymorphism** berasal dari bahasa Yunani: **"poly"** (banyak) + **"morph"** (bentuk). Artinya, **satu hal bisa memiliki banyak bentuk**. Dalam OOP, polymorphism memungkinkan objek dari class berbeda untuk merespons pesan/method yang sama dengan cara yang berbeda-beda.

> **Inti dari Polymorphism:** _"Satu aksi, banyak cara — tergantung siapa yang melakukannya."_

---

## 🧠 Analogi Dunia Nyata

### 🎮 Tombol "Play" di Berbagai Perangkat

Bayangkan tombol **▶️ Play** — bentuknya sama, tapi hasilnya berbeda tergantung perangkatnya:

```
        Tekan Tombol ▶️ PLAY
               │
    ┌──────────┼──────────────┐
    │          │              │
    ▼          ▼              ▼
┌────────┐ ┌────────┐  ┌────────────┐
│Spotify │ │ YouTube│  │ Netflix    │
│        │ │        │  │            │
│🎵 Putar│ │🎬 Putar│  │🎥 Putar    │
│ Musik  │ │ Video  │  │ Film       │
└────────┘ └────────┘  └────────────┘

Satu tombol → tiga perilaku berbeda!
```

### 📱 Tombol "Kirim" di Berbagai Aplikasi

| Tombol "Kirim" | Aksi yang Terjadi |
|:----------------|:------------------|
| WhatsApp | Mengirim pesan chat |
| Gmail | Mengirim email |
| Instagram | Mengirim DM/foto |
| OVO | Mengirim uang |

**Kata "Kirim" sama**, tapi **implementasinya berbeda** tergantung konteks!

---

## 🔑 Konsep Kunci

### Ada 2 Jenis Polymorphism

```
                    POLYMORPHISM
                         │
          ┌──────────────┴──────────────┐
          │                             │
   ┌──────┴───────┐            ┌───────┴────────┐
   │  COMPILE-TIME │            │  RUNTIME       │
   │  (Static)     │            │  (Dynamic)     │
   │               │            │                │
   │  Method       │            │  Method        │
   │  OVERLOADING  │            │  OVERRIDING    │
   └───────────────┘            └────────────────┘
   
   Ditentukan saat             Ditentukan saat
   KOMPILASI                   PROGRAM BERJALAN
```

### Perbandingan Singkat

| Aspek | Overloading | Overriding |
|:------|:------------|:-----------|
| **Kapan?** | Compile-time | Runtime |
| **Di mana?** | Dalam 1 class yang sama | Antara parent & child class |
| **Nama method** | Sama | Sama |
| **Parameter** | **Harus beda** | **Harus sama** |
| **Return type** | Boleh beda | Harus sama (atau covariant) |
| **Keyword** | — | `@Override` |

---

## 💻 Contoh Kode

### Skenario: Sistem Perhitungan Luas Bangun Datar

```java
/**
 * ═══════════════════════════════════════════════
 * SUPERCLASS: BangunDatar
 * → Mewakili semua bangun datar secara umum
 * ═══════════════════════════════════════════════
 */
abstract class BangunDatar {
    
    protected String nama;
    protected String warna;

    public BangunDatar(String nama, String warna) {
        this.nama = nama;
        this.warna = warna;
    }

    // Method yang akan di-OVERRIDE oleh setiap subclass
    public abstract double hitungLuas();
    public abstract double hitungKeliling();

    // Method umum (tidak perlu override)
    public void tampilkanInfo() {
        System.out.println("┌─────────────────────────────────");
        System.out.println("│ Bangun    : " + nama);
        System.out.println("│ Warna     : " + warna);
        System.out.printf("│ Luas      : %.2f%n", hitungLuas());
        System.out.printf("│ Keliling  : %.2f%n", hitungKeliling());
        System.out.println("└─────────────────────────────────");
    }
}

/**
 * ═══════════════════════════════════════════════
 * SUBCLASS: Persegi
 * → Override hitungLuas() dan hitungKeliling()
 *   dengan rumus khusus persegi
 * ═══════════════════════════════════════════════
 */
class Persegi extends BangunDatar {

    private double sisi;

    public Persegi(double sisi, String warna) {
        super("Persegi", warna);
        this.sisi = sisi;
    }

    @Override
    public double hitungLuas() {
        return sisi * sisi;  // s × s
    }

    @Override
    public double hitungKeliling() {
        return 4 * sisi;     // 4 × s
    }
}

/**
 * ═══════════════════════════════════════════════
 * SUBCLASS: Lingkaran
 * → Override dengan rumus lingkaran
 * ═══════════════════════════════════════════════
 */
class Lingkaran extends BangunDatar {

    private double jariJari;

    public Lingkaran(double jariJari, String warna) {
        super("Lingkaran", warna);
        this.jariJari = jariJari;
    }

    @Override
    public double hitungLuas() {
        return Math.PI * jariJari * jariJari;  // π × r²
    }

    @Override
    public double hitungKeliling() {
        return 2 * Math.PI * jariJari;          // 2 × π × r
    }
}

/**
 * ═══════════════════════════════════════════════
 * SUBCLASS: Segitiga
 * → Override dengan rumus segitiga
 * ═══════════════════════════════════════════════
 */
class Segitiga extends BangunDatar {

    private double alas;
    private double tinggi;
    private double sisiA, sisiB, sisiC;

    public Segitiga(double alas, double tinggi, double sisiA, double sisiB, double sisiC, String warna) {
        super("Segitiga", warna);
        this.alas = alas;
        this.tinggi = tinggi;
        this.sisiA = sisiA;
        this.sisiB = sisiB;
        this.sisiC = sisiC;
    }

    @Override
    public double hitungLuas() {
        return 0.5 * alas * tinggi;  // ½ × a × t
    }

    @Override
    public double hitungKeliling() {
        return sisiA + sisiB + sisiC;  // a + b + c
    }
}

/**
 * ═══════════════════════════════════════════════
 * CLASS: Kalkulator
 * → Demonstrasi METHOD OVERLOADING
 *   (Compile-time Polymorphism)
 * ═══════════════════════════════════════════════
 */
class Kalkulator {

    // Overload 1: Jumlahkan 2 bilangan bulat
    public int jumlahkan(int a, int b) {
        System.out.println("📌 Memanggil jumlahkan(int, int)");
        return a + b;
    }

    // Overload 2: Jumlahkan 3 bilangan bulat
    public int jumlahkan(int a, int b, int c) {
        System.out.println("📌 Memanggil jumlahkan(int, int, int)");
        return a + b + c;
    }

    // Overload 3: Jumlahkan 2 bilangan desimal
    public double jumlahkan(double a, double b) {
        System.out.println("📌 Memanggil jumlahkan(double, double)");
        return a + b;
    }

    // Overload 4: Gabungkan 2 string
    public String jumlahkan(String a, String b) {
        System.out.println("📌 Memanggil jumlahkan(String, String)");
        return a + b;
    }
}

/**
 * ═══════════════════════════════════════════════
 * MAIN: Demonstrasi Polymorphism
 * ═══════════════════════════════════════════════
 */
public class Shape {
    public static void main(String[] args) {

        System.out.println("══════════════════════════════════════════");
        System.out.println("  DEMO POLYMORPHISM — BANGUN DATAR       ");
        System.out.println("══════════════════════════════════════════\n");

        // ─────────────────────────────────────
        // RUNTIME POLYMORPHISM (Method Overriding)
        // ─────────────────────────────────────
        System.out.println("▸ RUNTIME POLYMORPHISM (Overriding)");
        System.out.println("  Satu method hitungLuas(), tiga hasil berbeda!\n");

        // Array bertipe superclass, isi bermacam subclass
        BangunDatar[] bangunDatar = {
            new Persegi(5, "Merah"),
            new Lingkaran(7, "Biru"),
            new Segitiga(6, 8, 6, 8, 10, "Hijau")
        };

        // Panggil method yang SAMA → hasilnya BERBEDA!
        for (BangunDatar bangun : bangunDatar) {
            bangun.tampilkanInfo();  // Polymorphism terjadi di sini!
            System.out.println();
        }

        // ─────────────────────────────────────
        // COMPILE-TIME POLYMORPHISM (Method Overloading)
        // ─────────────────────────────────────
        System.out.println("══════════════════════════════════════════");
        System.out.println("▸ COMPILE-TIME POLYMORPHISM (Overloading)");
        System.out.println("  Satu nama method, empat versi berbeda!\n");

        Kalkulator calc = new Kalkulator();

        // Nama method sama: "jumlahkan"
        // Tapi parameter beda → method yang dipanggil beda!
        System.out.println("Hasil: " + calc.jumlahkan(5, 3));
        System.out.println("Hasil: " + calc.jumlahkan(5, 3, 2));
        System.out.println("Hasil: " + calc.jumlahkan(5.5, 3.2));
        System.out.println("Hasil: " + calc.jumlahkan("Hello, ", "World!"));

        System.out.println("\n══════════════════════════════════════════");
    }
}
```

**Output:**
```
══════════════════════════════════════════
  DEMO POLYMORPHISM — BANGUN DATAR       
══════════════════════════════════════════

▸ RUNTIME POLYMORPHISM (Overriding)
  Satu method hitungLuas(), tiga hasil berbeda!

┌─────────────────────────────────
│ Bangun    : Persegi
│ Warna     : Merah
│ Luas      : 25.00              ← s × s = 5 × 5
│ Keliling  : 20.00
└─────────────────────────────────

┌─────────────────────────────────
│ Bangun    : Lingkaran
│ Warna     : Biru
│ Luas      : 153.94             ← π × r² = π × 7²
│ Keliling  : 43.98
└─────────────────────────────────

┌─────────────────────────────────
│ Bangun    : Segitiga
│ Warna     : Hijau
│ Luas      : 24.00              ← ½ × a × t = ½ × 6 × 8
│ Keliling  : 24.00
└─────────────────────────────────

══════════════════════════════════════════
▸ COMPILE-TIME POLYMORPHISM (Overloading)
  Satu nama method, empat versi berbeda!

📌 Memanggil jumlahkan(int, int)
Hasil: 8
📌 Memanggil jumlahkan(int, int, int)
Hasil: 10
📌 Memanggil jumlahkan(double, double)
Hasil: 8.7
📌 Memanggil jumlahkan(String, String)
Hasil: Hello, World!

══════════════════════════════════════════
```

---

## 🔍 Polymorphism Lebih Dalam

### Keajaiban Tipe Superclass

```java
// Variabel bertipe SUPERCLASS, tapi isinya SUBCLASS
BangunDatar bentuk = new Lingkaran(5, "Merah");

bentuk.hitungLuas();  // → Memanggil hitungLuas() milik LINGKARAN!
                      //   Bukan milik BangunDatar!
```

Ini disebut **Dynamic Dispatch** — Java menentukan method mana yang dipanggil berdasarkan **tipe objek sebenarnya** (runtime), bukan tipe variabel (compile-time).

### Polymorphism di Method Parameter

```java
// Method menerima SUPERCLASS sebagai parameter
public void cetakInfo(BangunDatar bangun) {
    bangun.tampilkanInfo();  // Bekerja untuk SEMUA subclass!
}

// Bisa dipanggil dengan subclass apapun
cetakInfo(new Persegi(5, "Merah"));
cetakInfo(new Lingkaran(7, "Biru"));
cetakInfo(new Segitiga(6, 8, 6, 8, 10, "Hijau"));
```

---

## 📋 Kapan Menggunakan Overloading vs Overriding?

| Situasi | Gunakan |
|:--------|:--------|
| Method yang sama tapi **beda input** | **Overloading** |
| Method yang sama tapi **beda implementasi** per subclass | **Overriding** |
| Konstruktor dengan opsi parameter berbeda | **Overloading** |
| Mengubah perilaku method warisan parent | **Overriding** |

---

## ⚠️ Kesalahan Umum

| ❌ Salah | ✅ Benar |
|:---------|:---------|
| Mengira overloading = beda return type saja | Overloading harus beda **parameter** |
| Lupa `@Override` saat override | Selalu gunakan `@Override` annotation |
| Menggunakan `private` method lalu coba override | `private` method tidak bisa di-override |
| Mengira `static` method bisa di-override | `static` method di-***hide***, bukan di-override |

---

## 🔗 Navigasi

| Sebelumnya | Berikutnya |
|:-----------|:-----------|
| [📖 ← Inheritance](02-inheritance.md) | [📖 Abstraction →](04-abstraction.md) |

---

<p align="center"><i>"Polymorphism adalah tentang fleksibilitas — satu panggilan, ribuan kemungkinan."</i></p>

---

<p align="center">
  <b>Wahyu Amaldi, M.Kom</b> · Universitas Cakrawala
</p>
