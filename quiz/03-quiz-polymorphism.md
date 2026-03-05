# 📝 Latihan Soal — Pilar 3: Polymorphism

> **Penulis:** Wahyu Amaldi, M.Kom · **Institusi:** Universitas Cakrawala

[⬅️ Kembali ke Beranda](../README.md) · [📖 Materi Polymorphism](../docs/03-polymorphism.md)

---

## Petunjuk

- Pilih **satu jawaban** yang paling tepat untuk setiap soal.
- Setiap soal berbasis **skenario** — baca kasus dengan cermat.
- Kunci jawaban tersedia di [bagian bawah](#-kunci-jawaban).

---

### Soal 1 — Kasus: Aplikasi Musik 🎵

Bayangkan kamu membuat aplikasi pemutar media. Ada class `Media` dengan method `play()`. Subclass-nya: `Musik`, `Video`, `Podcast` — masing-masing punya perilaku `play()` yang berbeda.

```java
Media[] playlist = {
    new Musik("Sheila On 7 - Dan"),
    new Video("Tutorial Java"),
    new Podcast("Tech in Asia")
};

for (Media m : playlist) {
    m.play();
}
```

Konsep apa yang ditunjukkan kode di atas?

- **A.** Encapsulation — karena data playlist disembunyikan
- **B.** Runtime Polymorphism — satu method `play()` dipanggil, tapi perilaku berbeda tergantung tipe objek sebenarnya
- **C.** Compile-time Polymorphism — karena ada 3 class berbeda
- **D.** Abstraction — karena menggunakan array bertipe `Media`

---

### Soal 2 — Kasus: Method Overloading 📐

Perhatikan class berikut:

```java
class Kalkulator {
    public int hitung(int a, int b) {
        return a + b;
    }

    public double hitung(double a, double b) {
        return a + b;
    }

    public int hitung(int a, int b, int c) {
        return a + b + c;
    }
}
```

Manakah pernyataan yang **BENAR** tentang kode di atas?

- **A.** Akan compile error karena nama method `hitung` duplikat
- **B.** Ini adalah method **overloading** — satu nama method, parameter berbeda. Ditentukan saat compile-time
- **C.** Ini adalah method **overriding** — method parent di-override oleh child
- **D.** Overloading hanya boleh jika return type berbeda

---

### Soal 3 — Kasus: Sistem Pembayaran 💳

Perhatikan kode berikut:

```java
class Pembayaran {
    public void bayar(double jumlah) {
        System.out.println("Membayar Rp " + jumlah);
    }
}

class BayarKartuKredit extends Pembayaran {
    @Override
    public void bayar(double jumlah) {
        double biayaAdmin = jumlah * 0.02;
        System.out.println("Bayar via Kartu Kredit: Rp " + (jumlah + biayaAdmin));
    }
}

class BayarEWallet extends Pembayaran {
    @Override
    public void bayar(double jumlah) {
        double diskon = jumlah * 0.05;
        System.out.println("Bayar via E-Wallet: Rp " + (jumlah - diskon));
    }
}
```

Apa output dari kode berikut?

```java
Pembayaran p = new BayarEWallet();
p.bayar(100000);
```

- **A.** `Membayar Rp 100000.0`
- **B.** `Bayar via Kartu Kredit: Rp 102000.0`
- **C.** `Bayar via E-Wallet: Rp 95000.0`
- **D.** Compile error — tipe variabel `Pembayaran` tidak bisa menjalankan method subclass

---

### Soal 4 — Kasus: Overloading vs Overriding 🔄

Manakah perbedaan yang **PALING TEPAT** antara method overloading dan method overriding?

- **A.** Overloading terjadi di antara parent-child class, overriding terjadi dalam satu class
- **B.** Overloading mengharuskan nama method berbeda, overriding nama method sama
- **C.** Overloading: **nama sama, parameter beda**, dalam satu class. Overriding: **nama sama, parameter sama**, antara parent dan child class
- **D.** Overloading dan overriding adalah hal yang sama, hanya beda istilah

---

### Soal 5 — Kasus: Jebakan Overloading ⚡

Perhatikan kode berikut:

```java
class Printer {
    public void cetak(int x) {
        System.out.println("int: " + x);
    }

    public void cetak(double x) {
        System.out.println("double: " + x);
    }

    public void cetak(String x) {
        System.out.println("String: " + x);
    }
}
```

Apa output dari pemanggilan berikut?

```java
Printer p = new Printer();
p.cetak(42);
p.cetak(3.14);
p.cetak("Halo");
```

- **A.** `double: 42` / `double: 3.14` / `String: Halo`
- **B.** `int: 42` / `double: 3.14` / `String: Halo`
- **C.** `String: 42` / `String: 3.14` / `String: Halo`
- **D.** Compile error — method `cetak` ambigu

---

### Soal 6 — Kasus: Sistem Notifikasi 🔔

Kamu membuat sistem notifikasi multi-channel:

```java
abstract class Notifikasi {
    public abstract void kirim(String pesan);
}

class NotifEmail extends Notifikasi {
    @Override
    public void kirim(String pesan) {
        System.out.println("📧 Email: " + pesan);
    }
}

class NotifSMS extends Notifikasi {
    @Override
    public void kirim(String pesan) {
        System.out.println("📱 SMS: " + pesan);
    }
}

class NotifPush extends Notifikasi {
    @Override
    public void kirim(String pesan) {
        System.out.println("🔔 Push: " + pesan);
    }
}
```

Kamu ingin menambah channel baru `NotifWhatsApp` tanpa mengubah kode yang sudah ada. Apakah bisa?

- **A.** Tidak bisa — harus mengubah class `Notifikasi` terlebih dahulu
- **B.** Bisa — cukup buat class `NotifWhatsApp extends Notifikasi` dan override `kirim()`. Kode lama tidak perlu diubah, inilah kekuatan polymorphism
- **C.** Bisa — tapi harus mengubah semua class notifikasi yang lain juga
- **D.** Tidak bisa — Java membatasi jumlah subclass maksimal 4

---

### Soal 7 — Kasus: Bangun Datar 📐

Perhatikan kode berikut yang mirip dengan contoh di sample `Shape.java`:

```java
abstract class BangunDatar {
    public abstract double hitungLuas();
}

class Persegi extends BangunDatar {
    private double sisi;
    public Persegi(double sisi) { this.sisi = sisi; }

    @Override
    public double hitungLuas() { return sisi * sisi; }
}

class Lingkaran extends BangunDatar {
    private double r;
    public Lingkaran(double r) { this.r = r; }

    @Override
    public double hitungLuas() { return Math.PI * r * r; }
}
```

Perhatikan method berikut:

```java
public void cetakLuas(BangunDatar b) {
    System.out.println("Luas: " + b.hitungLuas());
}
```

Manakah pemanggilan yang **VALID**?

- **A.** Hanya `cetakLuas(new Persegi(5))`
- **B.** Hanya `cetakLuas(new Lingkaran(7))`
- **C.** Keduanya valid: `cetakLuas(new Persegi(5))` dan `cetakLuas(new Lingkaran(7))` — karena keduanya adalah `BangunDatar`
- **D.** Keduanya invalid karena `BangunDatar` abstract

---

### Soal 8 — Kasus: Override yang Gagal ❌

Perhatikan kode berikut:

```java
class Hewan {
    public void bersuara() {
        System.out.println("...");
    }
}

class Kucing extends Hewan {
    public void berSuara() {    // Perhatikan huruf besar 'S'
        System.out.println("Meow!");
    }
}
```

Apa yang terjadi ketika kode berikut dijalankan?

```java
Hewan h = new Kucing();
h.bersuara();
```

- **A.** `Meow!`
- **B.** `...` — karena `berSuara()` (huruf S besar) **bukan** override dari `bersuara()` (huruf s kecil), melainkan method baru
- **C.** Compile error
- **D.** Runtime error

---

### Soal 9 — Kasus: Tipe Return Overloading 🧩

Apakah kode berikut valid?

```java
class Konversi {
    public int ubah(int x) {
        return x * 2;
    }

    public double ubah(int x) {    // Beda return type, parameter SAMA
        return x * 2.0;
    }
}
```

- **A.** Valid — overloading boleh dibedakan dari return type saja
- **B.** Compile error — overloading **harus** berbeda di **parameter** (jumlah atau tipe), bukan hanya return type
- **C.** Valid — Java otomatis memilih berdasarkan variabel penerima
- **D.** Runtime error — JVM bingung memilih method

---

### Soal 10 — Kasus: Analogi Dunia Nyata 🌏

Di dunia nyata, **tombol "Kirim"** ada di banyak aplikasi:
- Di **WhatsApp**: mengirim pesan chat
- Di **Gmail**: mengirim email
- Di **GoPay**: mentransfer uang

Konsep OOP mana yang paling tepat menggambarkan situasi ini?

- **A.** Encapsulation — karena proses pengiriman disembunyikan
- **B.** Inheritance — karena semua aplikasi mewarisi tombol "Kirim"
- **C.** Polymorphism — satu aksi yang sama ("Kirim") menghasilkan perilaku berbeda tergantung konteksnya
- **D.** Abstraction — karena user tidak tahu cara kerja internal

---

## ✅ Kunci Jawaban

<details>
<summary><b>Klik untuk melihat jawaban</b></summary>

| Soal | Jawaban | Penjelasan Singkat |
|:----:|:-------:|:-------------------|
| 1 | **B** | Array bertipe superclass `Media` diisi berbagai subclass. Saat `play()` dipanggil, Java memilih implementasi berdasarkan **tipe objek sebenarnya** saat runtime. |
| 2 | **B** | Tiga method bernama sama `hitung` tapi dengan parameter berbeda = method overloading. Ini ditentukan saat compile-time berdasarkan tipe & jumlah argumen. |
| 3 | **C** | Variabel bertipe `Pembayaran`, tapi objek sebenarnya `BayarEWallet`. Method `bayar()` yang dipanggil adalah milik `BayarEWallet` (diskon 5%): `100000 - 5000 = 95000`. |
| 4 | **C** | Overloading: **satu class**, nama sama, parameter beda. Overriding: **parent-child**, nama sama, parameter sama, implementasi beda di child. |
| 5 | **B** | Java memilih method overloaded berdasarkan tipe argumen. `42` → `int`, `3.14` → `double`, `"Halo"` → `String`. Masing-masing cocok tepat dengan satu method. |
| 6 | **B** | Ini adalah prinsip **Open/Closed Principle** — class terbuka untuk ekstensi, tertutup untuk modifikasi. Cukup buat subclass baru tanpa mengubah kode lama. |
| 7 | **C** | Parameter bertipe `BangunDatar` bisa menerima **semua subclass**-nya. `Persegi` dan `Lingkaran` keduanya "is-a" `BangunDatar`. |
| 8 | **B** | Java **case-sensitive**: `bersuara()` ≠ `berSuara()`. Class `Kucing` tidak meng-override, melainkan membuat method baru. Yang dipanggil tetap method parent. Inilah pentingnya `@Override` annotation. |
| 9 | **B** | Overloading diidentifikasi dari **daftar parameter**, bukan return type. Dua method dengan parameter identik `(int x)` tapi beda return type = **compile error**. |
| 10 | **C** | Satu aksi/method ("Kirim") memiliki banyak bentuk perilaku tergantung objek yang mengeksekusi — definisi persis dari polymorphism. |

</details>

---

## 🔗 Navigasi

| Sebelumnya | Berikutnya |
|:-----------|:-----------|
| [📝 ← Quiz Inheritance](02-quiz-inheritance.md) | [📝 Quiz Abstraction →](04-quiz-abstraction.md) |

---

<p align="center">
  <b>Wahyu Amaldi, M.Kom</b> · Universitas Cakrawala
</p>
