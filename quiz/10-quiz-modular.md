# 📝 Latihan Soal — Bab 10: Modular Monolith

> **Penulis:** Wahyu Amaldi, M.Kom · **Institusi:** Universitas Cakrawala

[⬅️ Kembali ke Beranda](../README.md) · [📖 Materi Modular Monolith](../docs/10-modular-monolith.md)

---

## Petunjuk

- Pilih **satu jawaban** yang paling tepat untuk setiap soal.
- Setiap soal berbasis **skenario** — baca kasus dengan cermat.
- Kunci jawaban tersedia di [bagian bawah](#-kunci-jawaban).

---

### Soal 1 — Kasus: Analogi Mall 🏬

Modular Monolith sering dianalogikan dengan mal: setiap toko berdiri sendiri (stok sendiri, kasir sendiri), tapi berada dalam **satu gedung** yang sama dan terhubung lewat koridor.

Dalam analogi ini, "satu gedung" mewakili:

- **A.** Satu database per toko
- **B.** Satu proses aplikasi (monolith) yang menjalankan semua modul
- **C.** Satu REST API
- **D.** Satu team developer

---

### Soal 2 — Kasus: Module Boundary 🚧

Sebuah sistem e-commerce memiliki modul Catalog, Order, dan Payment. Seorang developer di modul Order langsung mengakses `private method` di modul Catalog.

Apa yang **salah**?

- **A.** Tidak ada yang salah — satu aplikasi boleh saling akses
- **B.** Melanggar Module Boundary — modul lain hanya boleh mengakses lewat public interface (API modul), bukan internal implementation
- **C.** Private method memang harus bisa diakses dari mana saja
- **D.** Seharusnya menggunakan database query langsung

---

### Soal 3 — Kasus: Event Bus 📡

Perhatikan kode:
```java
class EventBus {
    private Map<String, List<Consumer<DomainEvent>>> subscribers;

    public void publish(DomainEvent event) {
        subscribers.get(event.getTipe())
                   .forEach(handler -> handler.accept(event));
    }
}
```

Apa fungsi utama Event Bus dalam Modular Monolith?

- **A.** Menyimpan data ke database
- **B.** Menjadi penghubung antar modul agar saling berkomunikasi tanpa referensi langsung — loose coupling
- **C.** Menggantikan semua method call biasa
- **D.** Membuat aplikasi menjadi microservice

---

### Soal 4 — Kasus: Komunikasi Antar Modul 🔄

Saat pesanan dibuat di modul Order, modul Payment harus memproses pembayaran. Ada dua pendekatan:

**Pendekatan A:** `orderService.buatPesanan()` langsung memanggil `paymentService.prosesBayar()`

**Pendekatan B:** `orderService` publish event `PESANAN_DIBUAT`, `paymentModule` subscribe dan merespons

Mana yang lebih sesuai prinsip Modular Monolith?

- **A.** Pendekatan A — karena lebih sederhana
- **B.** Pendekatan B — karena modul Order tidak perlu tahu tentang modul Payment, cukup keluarkan event
- **C.** Keduanya sama saja
- **D.** Pendekatan A lebih scaling-friendly

---

### Soal 5 — Kasus: Monolith vs Modular Monolith vs Microservices 📊

Cocokkan karakteristik berikut:

| No | Karakteristik | Arsitektur |
|:--:|:-------------|:-----------|
| 1  | Satu deploy, TANPA batas modul yang jelas | ? |
| 2  | Satu deploy, DENGAN batas modul yang jelas, komunikasi lewat event | ? |
| 3  | Banyak deploy terpisah, komunikasi lewat network | ? |

Urutan yang benar:

- **A.** Microservices, Modular Monolith, Monolith
- **B.** Monolith, Modular Monolith, Microservices
- **C.** Modular Monolith, Monolith, Microservices
- **D.** Monolith, Microservices, Modular Monolith

---

### Soal 6 — Kasus: Domain Event 📨

Modul Catalog mengeluarkan event `STOK_RENDAH` saat stok produk tinggal 5 unit. Pertanyaan: apakah modul Catalog **perlu tahu** siapa yang akan menerima event ini?

- **A.** Ya — Catalog harus tahu bahwa modul Notification dan Purchasing akan menerima event
- **B.** Tidak — Catalog hanya publish event. Siapa yang subscribe bukan urusan Catalog (loose coupling)
- **C.** Ya — tanpa tahu penerima, event tidak akan terkirim
- **D.** Tidak — tapi event tidak akan berfungsi tanpa coupling

---

### Soal 7 — Kasus: Kapan Pindah ke Microservices? 🚀

Sebuah startup memiliki 3 developer dan 1 produk sederhana. Seorang senior menyarankan langsung ke microservices agar "scalable dari awal".

Apa pendekatan yang lebih bijaksana?

- **A.** Langsung microservices — scalability adalah segalanya
- **B.** Mulai dengan Modular Monolith — mudah dideploy, batas modul sudah jelas, dan bisa di-pecah menjadi microservices nanti jika benar-benar dibutuhkan
- **C.** Buat monolith tanpa batas modul — paling cepat
- **D.** Tunggu sampai tim berjumlah 50 baru mulai coding

---

### Soal 8 — Kasus: Alur Event E-Commerce 🛒

Perhatikan alur event berikut:

```
Order dibuat → PESANAN_DIBUAT
    ↓
Payment proses → PEMBAYARAN_BERHASIL
    ↓
Catalog kurangi stok + Notification kirim email
```

Jika modul Payment gagal (misal: saldo tidak cukup), apa yang terjadi dengan stok di modul Catalog?

- **A.** Stok tetap berkurang karena pesanan sudah dibuat
- **B.** Stok TIDAK berkurang — karena event `PEMBAYARAN_BERHASIL` tidak pernah dipublish, jadi Catalog tidak melakukan apa-apa
- **C.** Stok menjadi negatif
- **D.** Sistem crash

---

### Soal 9 — Kasus: Shared Database 🗄️

Dua modul (`Order` dan `Catalog`) mengakses **tabel database yang sama** (`products`). Modul Order langsung UPDATE stok di tabel `products`.

Apa masalahnya?

- **A.** Tidak ada masalah — efisien karena tanpa event
- **B.** Melanggar Module Boundary — modul seharusnya berkomunikasi lewat event/API, bukan lewat tabel yang sama. Shared database membuat modul tightly coupled
- **C.** Database hanya boleh satu tabel
- **D.** INSERT lebih baik dari UPDATE

---

### Soal 10 — Kasus: Keuntungan Modular Monolith 🏆

Manakah yang **BUKAN** keuntungan Modular Monolith dibanding Microservices?

- **A.** Deployment lebih sederhana (satu artifact)
- **B.** Tidak perlu menangani network latency antar service
- **C.** Setiap modul bisa di-deploy dan di-scale secara independen
- **D.** Refactoring lintas modul lebih mudah karena satu codebase

---

## ✅ Kunci Jawaban

<details>
<summary><b>Klik untuk melihat jawaban</b></summary>

| Soal | Jawaban | Penjelasan Singkat |
|:----:|:-------:|:-------------------|
| 1 | **B** | Modular Monolith = satu proses/deploy (gedung), tapi dengan modul-modul terpisah (toko) yang punya batas jelas. |
| 2 | **B** | Module Boundary berarti modul lain hanya mengenal public interface, bukan detail internal. Mengakses internal modul lain menyebabkan tight coupling. |
| 3 | **B** | Event Bus memungkinkan modul berkomunikasi tanpa saling mengenal secara langsung — pengirim dan penerima dipisahkan oleh event. |
| 4 | **B** | Komunikasi via event membuat modul loosely coupled. Order tidak perlu import atau tahu keberadaan Payment — cukup publish event. |
| 5 | **B** | Monolith (satu blok tanpa batas), Modular Monolith (satu deploy + batas modul + event), Microservices (banyak deploy + network). |
| 6 | **B** | Publisher hanya mengeluarkan event. Subscriber mendaftarkan diri sendiri. Ini adalah prinsip loose coupling — publisher tidak perlu tahu siapa yang mendengarkan. |
| 7 | **B** | Modular Monolith memberikan kejelasan batas modul sejak awal, tanpa kompleksitas operasional microservices. Jika perlu, modul bisa dipisahkan nanti. |
| 8 | **B** | Event-driven: jika event `PEMBAYARAN_BERHASIL` tidak dipublish, tidak ada handler yang berjalan. Stok aman karena modul Catalog hanya bereaksi terhadap event tersebut. |
| 9 | **B** | Shared database = shared coupling. Perubahan skema tabel oleh satu modul bisa merusak modul lain. Setiap modul idealnya punya batas data sendiri. |
| 10 | **C** | Deploy dan scale independen adalah keuntungan microservices, bukan modular monolith. Modular monolith di-deploy sebagai satu kesatuan. |

</details>

---

## 🔗 Navigasi

| Sebelumnya | Berikutnya |
|:-----------|:-----------|
| [📝 Quiz Workflow Engine](09-quiz-workflow.md) | [📖 Kurikulum OBE →](../docs/00-obe-curriculum.md) |

---

<p align="center">
  <b>Wahyu Amaldi, M.Kom</b> · Universitas Cakrawala
</p>
