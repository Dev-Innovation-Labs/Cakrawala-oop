# 📝 Latihan Soal — Bab 5: Design Patterns

> **Penulis:** Wahyu Amaldi, M.Kom · **Institusi:** Universitas Cakrawala

[⬅️ Kembali ke Beranda](../README.md) · [📖 Materi Design Patterns](../docs/05-design-patterns.md)

---

## Petunjuk

- Pilih **satu jawaban** yang paling tepat untuk setiap soal.
- Setiap soal berbasis **skenario** — baca kasus dengan cermat.
- Kunci jawaban tersedia di [bagian bawah](#-kunci-jawaban).

---

### Soal 1 — Kasus: Diskon Marketplace 🛒

Sebuah marketplace punya tiga jenis diskon: diskon persen, diskon nominal tetap, dan beli-1-gratis-1. Jenis diskon bisa berubah kapan saja tanpa mengubah class utama `Kasir`.

Pola desain apa yang **paling tepat** digunakan?

- **A.** Observer — karena banyak objek perlu tahu ada diskon
- **B.** Strategy — karena ada beberapa algoritma diskon yang bisa ditukar saat runtime
- **C.** Factory — karena diskon perlu diproduksi satu per satu
- **D.** Singleton — karena diskon harus global

---

### Soal 2 — Kasus: Notifikasi Stok Barang 📦

Sebuah toko online ingin mengirim notifikasi ke bagian gudang, bagian pembelian, dan dashboard admin **setiap kali stok barang berubah**. Jumlah penerima notifikasi bisa bertambah di masa depan.

Pola desain apa yang paling cocok?

- **A.** Strategy — setiap penerima punya strategi berbeda
- **B.** Factory — buat satu objek notifikasi untuk semua
- **C.** Observer — satu *subject* (stok) memberitahu banyak *observer* saat ada perubahan
- **D.** State — stok punya banyak status

---

### Soal 3 — Kasus: Factory Dokumen 📄

Perhatikan kode berikut:

```java
interface Dokumen {
    void cetak();
}

class Invoice implements Dokumen {
    public void cetak() { System.out.println("Cetak Invoice"); }
}

class Kwitansi implements Dokumen {
    public void cetak() { System.out.println("Cetak Kwitansi"); }
}

class DokumenFactory {
    public static Dokumen buat(String tipe) {
        return switch (tipe) {
            case "invoice"  -> new Invoice();
            case "kwitansi" -> new Kwitansi();
            default -> throw new IllegalArgumentException("Tipe tidak dikenal");
        };
    }
}
```

Apa **keuntungan utama** menggunakan pattern Factory di sini?

- **A.** Tidak perlu menulis class `Invoice` dan `Kwitansi`
- **B.** Semua class otomatis menjadi singleton
- **C.** Client cukup minta "invoice" atau "kwitansi" tanpa tahu detail pembuatan objek
- **D.** Factory membuat kode lebih lambat tapi lebih aman

---

### Soal 4 — Kasus: Mesin Penjual Otomatis (Vending Machine) 🥤

Sebuah vending machine punya status: `IDLE`, `MENERIMA_UANG`, `MENGELUARKAN_BARANG`, dan `ERROR`. Perilaku tombol "Beli" **berbeda-beda** tergantung status mesin saat itu.

Pola desain apa yang paling tepat agar perilaku berubah sesuai status?

- **A.** Strategy — buat strategi untuk setiap tombol
- **B.** Observer — setiap tombol mendengarkan event
- **C.** State — perilaku objek berubah berdasarkan internal state saat ini
- **D.** Factory — buat vending machine berdasarkan tipe

---

### Soal 5 — Kasus: Strategy Salah Pakai 🤔

Seorang mahasiswa menulis kode berikut:

```java
class Kalkulator {
    public double hitung(String operasi, double a, double b) {
        if (operasi.equals("tambah")) return a + b;
        if (operasi.equals("kurang")) return a - b;
        if (operasi.equals("kali")) return a * b;
        if (operasi.equals("bagi")) return a / b;
        return 0;
    }
}
```

Dosen berkata: *"Refactor ini pakai Strategy Pattern."* Apa langkah pertama yang harus dilakukan?

- **A.** Hapus semua `if` dan pakai `switch-case`
- **B.** Buat interface `StrategiOperasi` dengan method `hitung(double a, double b)`, lalu buat implementasi per operasi
- **C.** Tambahkan parameter `strategy` bertipe `String`
- **D.** Buat semua operasi sebagai method terpisah di class yang sama

---

### Soal 6 — Kasus: Observer vs Polling ⏱️

Tim A menggunakan **polling** (pengecekan berkala setiap 5 detik) untuk mengetahui stok berubah. Tim B menggunakan **Observer Pattern**. Apa keunggulan pendekatan Tim B?

- **A.** Observer Pattern lebih cepat karena menggunakan multithreading
- **B.** Observer langsung diberitahu saat ada perubahan, tanpa buang waktu cek terus-menerus
- **C.** Polling tidak mungkin bekerja di Java
- **D.** Observer menggunakan database, polling tidak

---

### Soal 7 — Kasus: Gabungan Pattern 🧩

Sebuah sistem e-commerce memiliki:
1. Diskon yang bisa berubah-ubah → **Pattern X**
2. Notifikasi ke banyak pihak saat pesanan berhasil → **Pattern Y**
3. Pembuatan objek pesanan berdasarkan tipe → **Pattern Z**

Pattern X, Y, dan Z secara berurutan adalah:

- **A.** Factory, State, Observer
- **B.** Strategy, Observer, Factory
- **C.** Observer, Strategy, State
- **D.** State, Factory, Strategy

---

### Soal 8 — Kasus: Status Pesanan Online 📱

Perhatikan kode berikut:

```java
interface StatusPesanan {
    void proses(Pesanan p);
}

class StatusBaru implements StatusPesanan {
    public void proses(Pesanan p) {
        System.out.println("Validasi data pesanan...");
        p.setStatus(new StatusDiproses());
    }
}

class StatusDiproses implements StatusPesanan {
    public void proses(Pesanan p) {
        System.out.println("Kemas & kirim barang...");
        p.setStatus(new StatusDikirim());
    }
}
```

Apa yang terjadi jika `pesanan.proses()` dipanggil dua kali berturut-turut dari status `Baru`?

- **A.** Error karena `proses()` hanya bisa dipanggil sekali
- **B.** Panggilan pertama ubah status ke `Diproses`, panggilan kedua ubah ke `Dikirim`
- **C.** Kedua panggilan tetap di status `Baru`
- **D.** Class `StatusBaru` menghapus dirinya sendiri

---

### Soal 9 — Kasus: Kapan TIDAK Pakai Pattern? ⚠️

Seorang developer diminta membuat fungsi sederhana yang menghitung pajak 11% dari total belanja. Hanya ada **satu** rumus pajak yang berlaku dan tidak akan berubah.

Apakah perlu menggunakan Strategy Pattern?

- **A.** Ya, semua perhitungan wajib pakai Strategy
- **B.** Tidak perlu — satu algoritma tetap cukup ditulis langsung, Strategy justru menambah kompleksitas
- **C.** Ya, karena Strategy membuat kode terlihat profesional
- **D.** Tidak, karena Java tidak mendukung Strategy untuk perhitungan

---

### Soal 10 — Kasus: Refactoring ke State Pattern 🔄

Class berikut menggunakan `if-else` untuk menentukan perilaku berdasarkan status:

```java
class Lampu {
    String status = "mati";

    void tekan() {
        if (status.equals("mati")) {
            System.out.println("Lampu menyala redup");
            status = "redup";
        } else if (status.equals("redup")) {
            System.out.println("Lampu menyala terang");
            status = "terang";
        } else if (status.equals("terang")) {
            System.out.println("Lampu mati");
            status = "mati";
        }
    }
}
```

Jika di-refactor ke State Pattern, maka:

- **A.** Buat 3 class (`Mati`, `Redup`, `Terang`) yang masing-masing implements interface `StatusLampu` dengan method `tekan()`
- **B.** Buat 1 enum dengan semua status dan method `tekan()` di dalamnya
- **C.** Tambahkan lebih banyak `if-else` untuk setiap status baru
- **D.** Pindahkan semua logika ke class `Main`

---

## ✅ Kunci Jawaban

<details>
<summary><b>Klik untuk melihat jawaban</b></summary>

| Soal | Jawaban | Penjelasan Singkat |
|:----:|:-------:|:-------------------|
| 1 | **B** | Strategy memungkinkan beberapa algoritma diskon ditukar kapan saja tanpa mengubah `Kasir`. |
| 2 | **C** | Observer cocok untuk skenario satu-ke-banyak: stok berubah, semua subscriber diberitahu. |
| 3 | **C** | Factory menyembunyikan detail pembuatan. Client cukup memanggil `DokumenFactory.buat("invoice")`. |
| 4 | **C** | State Pattern membuat perilaku objek berubah sesuai status internalnya — persis seperti cara vending machine bekerja. |
| 5 | **B** | Langkah pertama Strategy: buat interface dengan kontrak operasi, lalu buat implementasi terpisah per operasi. |
| 6 | **B** | Observer bersifat *push*: subject memberitahu observer saat terjadi perubahan. Polling bersifat *pull*: cek terus-menerus meskipun tidak ada perubahan. |
| 7 | **B** | Diskon bertukar → Strategy. Notifikasi banyak pihak → Observer. Pembuatan objek berdasarkan tipe → Factory. |
| 8 | **B** | Panggilan pertama menjalankan `StatusBaru.proses()` → status jadi `Diproses`. Panggilan kedua menjalankan `StatusDiproses.proses()` → status jadi `Dikirim`. |
| 9 | **B** | Pattern seperti Strategy dirancang untuk kasus banyak algoritma yang bisa ditukar. Satu rumus tetap cukup ditulis langsung — over-engineering justru merugikan. |
| 10 | **A** | State Pattern: setiap status menjadi class tersendiri yang mengimplementasi interface. Saat ada status baru, cukup tambah class baru tanpa mengubah class lain. |

</details>

---

## 🔗 Navigasi

| Sebelumnya | Berikutnya |
|:-----------|:-----------|
| [📝 Quiz Abstraction](04-quiz-abstraction.md) | [📝 Quiz DDD →](06-quiz-ddd.md) |

---

<p align="center">
  <b>Wahyu Amaldi, M.Kom</b> · Universitas Cakrawala
</p>
