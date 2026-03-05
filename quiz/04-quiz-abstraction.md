# 📝 Latihan Soal — Pilar 4: Abstraction

> **Penulis:** Wahyu Amaldi, M.Kom · **Institusi:** Universitas Cakrawala

[⬅️ Kembali ke Beranda](../README.md) · [📖 Materi Abstraction](../docs/04-abstraction.md)

---

## Petunjuk

- Pilih **satu jawaban** yang paling tepat untuk setiap soal.
- Setiap soal berbasis **skenario** — baca kasus dengan cermat.
- Kunci jawaban tersedia di [bagian bawah](#-kunci-jawaban).

---

### Soal 1 — Kasus: Mesin Kopi ☕

Di sebuah kafe, kamu tinggal tekan tombol **"Cappuccino"** dan kopi langsung jadi. Kamu tidak perlu tahu proses: giling biji → panaskan air → seduh → kukus susu → tuang foam.

Konsep OOP mana yang paling tepat menggambarkan situasi ini?

- **A.** Encapsulation — data kopi disembunyikan dalam mesin
- **B.** Inheritance — mesin kopi mewarisi kemampuan dari mesin sebelumnya
- **C.** Polymorphism — satu tombol bisa buat banyak jenis kopi
- **D.** Abstraction — detail proses rumit disembunyikan, user hanya berinteraksi dengan antarmuka sederhana (tombol)

---

### Soal 2 — Kasus: Abstract Class vs Interface 🤔

Tim kamu sedang mendesain sistem kendaraan. Ada kebutuhan:

1. Semua kendaraan bisa `nyalakanMesin()` dan `matikanMesin()` — **implementasinya sama** untuk semua kendaraan
2. Setiap kendaraan punya cara `jalan()` yang **berbeda-beda**
3. Beberapa kendaraan bisa **diisi daya** (listrik), tapi tidak semuanya

Pendekatan desain mana yang **paling tepat**?

- **A.** Buat semua sebagai interface
- **B.** Buat `Kendaraan` sebagai **abstract class** (shared method + abstract method), dan `PengisiDaya` sebagai **interface** untuk kendaraan yang bisa dicharge
- **C.** Buat `Kendaraan` dan `PengisiDaya` keduanya sebagai abstract class
- **D.** Buat satu class konkret `Kendaraan` tanpa abstraksi

---

### Soal 3 — Kasus: Instantiate Abstract Class 🚫

Perhatikan kode berikut:

```java
abstract class Hewan {
    public abstract void bersuara();
}

public class Main {
    public static void main(String[] args) {
        Hewan h = new Hewan();    // ← Baris ini
        h.bersuara();
    }
}
```

Apa yang terjadi?

- **A.** Program berjalan normal, mencetak output kosong
- **B.** Runtime error — abstract class tidak punya implementasi
- **C.** **Compile error** — abstract class **tidak bisa** di-instantiate langsung
- **D.** Program berjalan tapi method `bersuara()` tidak terpanggil

---

### Soal 4 — Kasus: Interface Multiple Implementation 🔌

Perhatikan kode berikut:

```java
interface Terbang {
    void terbang();
}

interface Berenang {
    void berenang();
}

class Bebek implements Terbang, Berenang {
    @Override
    public void terbang() {
        System.out.println("Bebek terbang rendah 🦆");
    }

    @Override
    public void berenang() {
        System.out.println("Bebek berenang di danau 🏊");
    }
}
```

Mengapa desain ini **tidak bisa** dilakukan dengan dua abstract class?

- **A.** Abstract class tidak bisa punya method abstract
- **B.** Java **tidak mendukung** extends dari 2 class sekaligus (no multiple inheritance of class), tapi **bisa implements** banyak interface
- **C.** Interface lebih lambat performanya
- **D.** Abstract class lebih modern dari interface

---

### Soal 5 — Kasus: Kontrak yang Dilanggar 📋

Perhatikan kode berikut:

```java
abstract class BangunDatar {
    public abstract double hitungLuas();
    public abstract double hitungKeliling();
}

class Persegi extends BangunDatar {
    private double sisi;

    public Persegi(double sisi) {
        this.sisi = sisi;
    }

    @Override
    public double hitungLuas() {
        return sisi * sisi;
    }

    // hitungKeliling() TIDAK diimplementasi!
}
```

Apa yang terjadi?

- **A.** Program berjalan normal — method yang tidak diimplementasi otomatis return 0
- **B.** **Compile error** — `Persegi` **harus** mengimplementasi **semua** abstract method dari `BangunDatar`, atau dideklarasikan `abstract` juga
- **C.** Runtime error saat `hitungKeliling()` dipanggil
- **D.** Hanya warning, bukan error

---

### Soal 6 — Kasus: Sistem E-Commerce 🛒

Kamu membuat sistem pembayaran e-commerce:

```java
interface MetodePembayaran {
    boolean prosesBayar(double jumlah);
    String getNamaPembayaran();
}

class BCA implements MetodePembayaran {
    @Override
    public boolean prosesBayar(double jumlah) {
        System.out.println("Transfer BCA: Rp " + jumlah);
        return true;
    }

    @Override
    public String getNamaPembayaran() {
        return "Bank BCA";
    }
}

class GoPay implements MetodePembayaran {
    @Override
    public boolean prosesBayar(double jumlah) {
        System.out.println("Bayar GoPay: Rp " + jumlah);
        return true;
    }

    @Override
    public String getNamaPembayaran() {
        return "GoPay";
    }
}
```

Lalu ada class `Checkout`:

```java
class Checkout {
    public void bayar(MetodePembayaran metode, double total) {
        System.out.println("Metode: " + metode.getNamaPembayaran());
        metode.prosesBayar(total);
    }
}
```

Apa **keuntungan utama** dari menggunakan interface `MetodePembayaran` di sini?

- **A.** Kode jadi lebih panjang dan kompleks
- **B.** Class `Checkout` **tidak perlu tahu** metode pembayaran apa yang dipakai — bisa BCA, GoPay, atau metode baru tanpa mengubah class `Checkout`
- **C.** Interface membuat program berjalan lebih cepat
- **D.** Tidak ada keuntungan, bisa ditulis tanpa interface

---

### Soal 7 — Kasus: Abstract Class dengan Concrete Method 🏗️

Perhatikan kode berikut:

```java
abstract class Kendaraan {
    protected String merk;

    public void nyalakanMesin() {
        System.out.println(merk + " mesin menyala!");
    }

    public abstract void jalan();
}

class Motor extends Kendaraan {
    public Motor(String merk) {
        this.merk = merk;
    }

    @Override
    public void jalan() {
        System.out.println(merk + " melaju di jalanan!");
    }
}
```

Apa output dari kode berikut?

```java
Kendaraan k = new Motor("Honda");
k.nyalakanMesin();
k.jalan();
```

- **A.** Compile error — abstract class tidak bisa digunakan sebagai tipe variabel
- **B.** `Honda mesin menyala!` lalu `Honda melaju di jalanan!`
- **C.** Hanya `Honda mesin menyala!` — method abstract tidak bisa dipanggil
- **D.** Runtime error

---

### Soal 8 — Kasus: Interface vs Abstract Class — Kapan Pakai? 🎯

Sebuah tim developer sedang membuat game. Mereka punya:

- Semua karakter punya `hp`, `nama`, dan method `tampilkanInfo()` yang **implementasinya sama**
- Method `serang()` **beda** untuk setiap jenis karakter
- Beberapa karakter bisa **terbang**, tapi tidak semua

Mana yang paling tepat?

- **A.** Abstract class `Karakter` dengan `hp`, `nama`, `tampilkanInfo()` (concrete), dan `serang()` (abstract). Interface `Terbang` untuk karakter yang bisa terbang.
- **B.** Interface `Karakter` dan interface `Terbang`
- **C.** Dua abstract class: `Karakter` dan `Terbang`
- **D.** Semua dibuat sebagai class konkret tanpa abstraksi

---

### Soal 9 — Kasus: Menambah Fitur Tanpa Merusak 🔧

Kamu sudah punya sistem yang bekerja:

```java
abstract class Kendaraan {
    public abstract void jalan();
    public abstract void berhenti();
}

class Mobil extends Kendaraan { /* implementasi lengkap */ }
class Motor extends Kendaraan { /* implementasi lengkap */ }
```

Sekarang klien minta tambah kendaraan jenis **Truk**. Langkah yang **paling tepat** adalah:

- **A.** Ubah class `Mobil` agar mendukung fitur truk juga
- **B.** Buat class baru `Truk extends Kendaraan` dan implementasi `jalan()` serta `berhenti()` — tanpa mengubah `Mobil` atau `Motor`
- **C.** Hapus semua class dan buat ulang dari awal
- **D.** Tambahkan method `jalanTruk()` di abstract class `Kendaraan`

---

### Soal 10 — Kasus: Empat Pilar Bekerja Bersama 🏛️

Perhatikan kode lengkap di bawah ini:

```java
// Abstraction: abstract class + interface
abstract class Kendaraan {
    private String merk;     // Encapsulation: private field

    public Kendaraan(String merk) {
        this.merk = merk;
    }

    public String getMerk() { return merk; }  // Encapsulation: getter

    public abstract void jalan();              // Abstraction: abstract method
}

interface Klakson {                            // Abstraction: interface
    void bunyikanKlakson();
}

// Inheritance: extends + implements
class Mobil extends Kendaraan implements Klakson {
    public Mobil(String merk) {
        super(merk);                            // Inheritance: super
    }

    @Override
    public void jalan() {                       // Polymorphism: override
        System.out.println(getMerk() + " melaju!");
    }

    @Override
    public void bunyikanKlakson() {
        System.out.println("BEEP BEEP!");
    }
}
```

Berapa pilar OOP yang diterapkan dalam kode di atas?

- **A.** 1 pilar (hanya Encapsulation)
- **B.** 2 pilar (Encapsulation dan Inheritance)
- **C.** 3 pilar (Encapsulation, Inheritance, dan Polymorphism)
- **D.** **4 pilar** — Encapsulation (private + getter), Inheritance (extends, super), Polymorphism (override), Abstraction (abstract class, interface)

---

## ✅ Kunci Jawaban

<details>
<summary><b>Klik untuk melihat jawaban</b></summary>

| Soal | Jawaban | Penjelasan Singkat |
|:----:|:-------:|:-------------------|
| 1 | **D** | Abstraction menyembunyikan **proses dan detail implementasi**, menampilkan hanya **antarmuka sederhana** (tombol Cappuccino). Fokusnya pada "apa yang dilakukan", bukan "bagaimana". |
| 2 | **B** | Abstract class cocok untuk shared behavior (`nyalakanMesin()`, `matikanMesin()`) + abstract method (`jalan()`). Interface cocok untuk kemampuan opsional (`PengisiDaya`) karena tidak semua kendaraan butuh fitur charging. |
| 3 | **C** | Abstract class **tidak bisa** dibuat objeknya langsung (`new Hewan()` tidak valid). Harus melalui subclass konkret yang sudah mengimplementasi semua method abstract. |
| 4 | **B** | Java hanya mendukung single inheritance untuk class, tapi mendukung **multiple implementation** untuk interface. Sehingga `Bebek` bisa implements `Terbang` dan `Berenang` sekaligus. |
| 5 | **B** | Jika sebuah class extends abstract class, ia **wajib** mengimplementasi **semua** method abstract. Jika tidak, class tersebut juga harus dideklarasikan sebagai `abstract`. |
| 6 | **B** | Interface bertindak sebagai **kontrak**. Class `Checkout` cukup tahu bahwa parameter-nya bertipe `MetodePembayaran`. Metode baru bisa ditambah tanpa mengubah `Checkout` — ini prinsip **loose coupling**. |
| 7 | **B** | Abstract class bisa digunakan sebagai **tipe variabel** (selama objeknya adalah subclass konkret). `nyalakanMesin()` dari abstract class dan `jalan()` yang di-override, keduanya berjalan normal. |
| 8 | **A** | Abstract class untuk shared state (`hp`, `nama`) dan shared behavior (`tampilkanInfo()`), plus abstract method (`serang()`). Interface `Terbang` untuk kemampuan opsional karena tidak semua karakter terbang. |
| 9 | **B** | Cukup buat subclass baru tanpa menyentuh kode lama — ini adalah prinsip **Open/Closed Principle** dan kekuatan abstraction. Kode lama tetap aman. |
| 10 | **D** | Semua 4 pilar diterapkan: **Encapsulation** (private + getter), **Inheritance** (extends, super), **Polymorphism** (method override), **Abstraction** (abstract class + interface). |

</details>

---

## 🔗 Navigasi

| Sebelumnya | Berikutnya |
|:-----------|:-----------|
| [📝 ← Quiz Polymorphism](03-quiz-polymorphism.md) | [🏠 Beranda](../README.md) |

---

<p align="center">
  <b>Wahyu Amaldi, M.Kom</b> · Universitas Cakrawala
</p>
