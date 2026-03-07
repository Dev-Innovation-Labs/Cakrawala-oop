# 📝 Latihan Soal — Bab 6: Domain Driven Design

> **Penulis:** Wahyu Amaldi, M.Kom · **Institusi:** Universitas Cakrawala

[⬅️ Kembali ke Beranda](../README.md) · [📖 Materi DDD](../docs/06-domain-driven-design.md)

---

## Petunjuk

- Pilih **satu jawaban** yang paling tepat untuk setiap soal.
- Setiap soal berbasis **skenario** — baca kasus dengan cermat.
- Kunci jawaban tersedia di [bagian bawah](#-kunci-jawaban).

---

### Soal 1 — Kasus: Identitas vs Kesamaan 🆔

Dua mahasiswa memiliki nama yang sama, "Budi Santoso", dan keduanya mengambil mata kuliah yang sama. Namun mereka tetap **dianggap berbeda** oleh sistem kampus karena NIM mereka berbeda.

Konsep DDD apa yang paling relevan?

- **A.** Value Object — karena namanya sama
- **B.** Entity — karena identitas (NIM) yang menentukan keunikan, bukan atributnya
- **C.** Aggregate — karena mahasiswa punya banyak mata kuliah
- **D.** Repository — karena data mahasiswa disimpan di database

---

### Soal 2 — Kasus: Class Uang 💰

Perhatikan class berikut:

```java
class Uang {
    private final double jumlah;
    private final String mataUang;

    public Uang(double jumlah, String mataUang) {
        this.jumlah = jumlah;
        this.mataUang = mataUang;
    }

    public Uang tambah(Uang lain) {
        return new Uang(this.jumlah + lain.jumlah, this.mataUang);
    }
}
```

Class `Uang` di atas adalah contoh:

- **A.** Entity — karena memiliki field `jumlah`
- **B.** Aggregate — karena bisa menjumlahkan dirinya sendiri
- **C.** Value Object — immutable, tanpa identitas unik, kesamaan berdasarkan nilai
- **D.** Repository — karena menyimpan data uang

---

### Soal 3 — Kasus: Pesanan dan Itemnya 📦

Sebuah sistem e-commerce memiliki `Pesanan` yang berisi banyak `ItemPesanan`. Aturan bisnis:
- Total pesanan dihitung dari semua item
- Item **hanya bisa** ditambah/dihapus lewat objek `Pesanan`
- Item tidak bisa berdiri sendiri tanpa pesanan

Dalam konsep DDD, `Pesanan` berperan sebagai:

- **A.** Value Object — karena tidak punya identitas
- **B.** Repository — karena menyimpan item
- **C.** Aggregate Root — karena menjadi pintu masuk tunggal dan menjaga konsistensi seluruh isi pesanan
- **D.** Domain Event — karena pesanan bisa berubah status

---

### Soal 4 — Kasus: Ubiquitous Language 🗣️

Tim developer dan tim bisnis sedang rapat membahas fitur pengiriman barang. Developer menamakan proses "update status_pengiriman flag di tabel order", sementara tim bisnis menyebutnya "konfirmasi pengiriman".

Prinsip DDD apa yang dilanggar?

- **A.** Aggregate — karena tabel tidak dikelompokkan
- **B.** Repository — karena tidak ada tempat penyimpanan
- **C.** Ubiquitous Language — developer dan bisnis harus menggunakan bahasa yang sama, baik di diskusi maupun di kode
- **D.** Value Object — karena status pengiriman bukan value object

---

### Soal 5 — Kasus: Repository Pattern 🗄️

Perhatikan interface berikut:

```java
interface PesananRepository {
    void simpan(PesananAggregate pesanan);
    Optional<PesananAggregate> cariByNomor(String nomor);
}
```

Mengapa Repository bekerja dengan **Aggregate Root** (`PesananAggregate`) dan bukan langsung dengan Entity biasa atau Value Object?

- **A.** Karena Repository hanya bisa menyimpan class yang besar
- **B.** Karena Aggregate Root adalah satu-satunya pintu masuk — menyimpan aggregate berarti menyimpan seluruh graf objek secara konsisten
- **C.** Karena Java mengharuskan generic type bertipe Aggregate
- **D.** Karena Value Object tidak bisa disimpan ke database

---

### Soal 6 — Kasus: Domain Event 📢

Setelah pesanan dibayar, sistem harus:
1. Kirim email konfirmasi ke pelanggan
2. Update stok gudang
3. Catat ke log audit

Seorang developer menempatkan ketiga logika tersebut **di dalam** method `bayar()` pada class `Pesanan`. Apa masalahnya?

- **A.** Tidak ada masalah, itu praktik yang normal
- **B.** Class `Pesanan` jadi terlalu banyak tanggung jawab. Seharusnya `bayar()` cukup mengubah status dan mengeluarkan Domain Event (`PesananDibayarEvent`), lalu subscriber lain yang menangani efek sampingnya
- **C.** Method `bayar()` seharusnya tidak ada di class `Pesanan`
- **D.** Java tidak mendukung event

---

### Soal 7 — Kasus: Entity vs Value Object 🤔

Mana dari berikut yang **paling tepat** dimodelkan sebagai Value Object?

- **A.** Mahasiswa — karena punya NIM unik
- **B.** Pesanan — karena punya nomor pesanan
- **C.** Alamat — karena kesamaannya ditentukan oleh jalan, kota, dan kode pos, bukan identitas unik
- **D.** Pelanggan — karena punya ID pelanggan

---

### Soal 8 — Kasus: Aggregate Boundary 🚧

Sebuah sistem punya `Pesanan` dan `Pelanggan`. Developer membuat `Pesanan` menyimpan **referensi langsung** ke objek `Pelanggan`. Akibatnya, saat mengambil satu pesanan dari database, objek `Pelanggan` beserta seluruh riwayat pembeliannya juga ikut ter-load.

Apa rekomendasi DDD?

- **A.** Tidak masalah — semua data perlu di-load sekaligus
- **B.** Pesanan sebaiknya hanya menyimpan **ID pelanggan** (bukan objek penuh), karena `Pelanggan` adalah Aggregate yang berbeda
- **C.** Gabungkan `Pesanan` dan `Pelanggan` jadi satu class saja
- **D.** Hapus class `Pelanggan` dan gunakan `String` saja

---

### Soal 9 — Kasus: Invariant Aggregate 🔒

Aturan bisnis: sebuah `KeranjangBelanja` tidak boleh memiliki total melebihi Rp 100 juta. Developer membuat validasi ini di class `Controller`.

Apa yang salah menurut prinsip DDD?

- **A.** Validasi di Controller sudah benar
- **B.** Validasi tersebut adalah **invariant** bisnis yang seharusnya dijaga oleh Aggregate Root (`KeranjangBelanja`) sendiri, bukan oleh Controller
- **C.** Validasi seharusnya di database
- **D.** Rp 100 juta terlalu kecil sebagai batas

---

### Soal 10 — Kasus: Pilih Konsep DDD 🧩

Cocokkan konsep DDD dengan penjelasan yang tepat:

| No | Penjelasan | Konsep |
|:--:|:-----------|:-------|
| 1  | Objek tanpa identitas, immutable | ? |
| 2  | Pintu masuk tunggal ke sekumpulan objek | ? |
| 3  | Notifikasi bahwa sesuatu telah terjadi di domain | ? |
| 4  | Objek dengan identitas unik | ? |

Urutan yang benar untuk 1, 2, 3, 4:

- **A.** Entity, Repository, Event, Value Object
- **B.** Value Object, Aggregate Root, Domain Event, Entity
- **C.** Aggregate Root, Entity, Value Object, Domain Event
- **D.** Domain Event, Value Object, Entity, Aggregate Root

---

## ✅ Kunci Jawaban

<details>
<summary><b>Klik untuk melihat jawaban</b></summary>

| Soal | Jawaban | Penjelasan Singkat |
|:----:|:-------:|:-------------------|
| 1 | **B** | Entity diidentifikasi oleh identitas uniknya (NIM), bukan oleh atribut seperti nama. Dua entity bisa punya atribut identik tapi tetap berbeda jika ID-nya berbeda. |
| 2 | **C** | `Uang` immutable (field `final`, method `tambah()` mengembalikan objek baru), tanpa ID unik, dan kesamaan berdasarkan nilai — ciri khas Value Object. |
| 3 | **C** | Aggregate Root menjadi satu-satunya titik akses. Semua perubahan terhadap `ItemPesanan` harus melewati `Pesanan` untuk menjaga konsistensi. |
| 4 | **C** | Ubiquitous Language menuntut developer dan bisnis menggunakan istilah yang sama. Kode harus berbunyi `konfirmasiPengiriman()`, bukan `updateStatusFlag()`. |
| 5 | **B** | Repository menyimpan dan mengambil Aggregate secara utuh. Aggregate Root menjamin seluruh grafik objek di dalamnya konsisten saat disimpan/dimuat. |
| 6 | **B** | Menempatkan logika email, stok, dan audit di dalam `bayar()` melanggar Single Responsibility. Domain Event memisahkan "apa yang terjadi" dari "siapa yang harus bereaksi". |
| 7 | **C** | Alamat tidak punya identitas unik — dua alamat identik adalah alamat yang sama. Mahasiswa, Pesanan, dan Pelanggan memiliki ID unik sehingga cocok jadi Entity. |
| 8 | **B** | Aggregate yang berbeda sebaiknya direferensi lewat ID, bukan referensi objek langsung. Ini mencegah loading berlebihan dan menjaga batas aggregate tetap jelas. |
| 9 | **B** | Invariant bisnis harus dijaga oleh Aggregate Root sendiri. Jika validasi ada di Controller, maka aggregate bisa diakses dari tempat lain tanpa validasi — aturan bisnis bocor. |
| 10 | **B** | 1=Value Object (tanpa ID, immutable), 2=Aggregate Root (pintu masuk), 3=Domain Event (notifikasi), 4=Entity (identitas unik). |

</details>

---

## 🔗 Navigasi

| Sebelumnya | Berikutnya |
|:-----------|:-----------|
| [📝 Quiz Design Patterns](05-quiz-design-patterns.md) | [📝 Quiz Hexagonal →](07-quiz-hexagonal.md) |

---

<p align="center">
  <b>Wahyu Amaldi, M.Kom</b> · Universitas Cakrawala
</p>
