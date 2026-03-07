# 📝 Latihan Soal — Bab 7: Hexagonal Architecture

> **Penulis:** Wahyu Amaldi, M.Kom · **Institusi:** Universitas Cakrawala

[⬅️ Kembali ke Beranda](../README.md) · [📖 Materi Hexagonal](../docs/07-hexagonal-architecture.md)

---

## Petunjuk

- Pilih **satu jawaban** yang paling tepat untuk setiap soal.
- Setiap soal berbasis **skenario** — baca kasus dengan cermat.
- Kunci jawaban tersedia di [bagian bawah](#-kunci-jawaban).

---

### Soal 1 — Kasus: Analogi Colokan Listrik 🔌

Hexagonal Architecture sering dianalogikan dengan colokan listrik: perangkat (laptop, kipas angin) bisa berjalan di negara mana pun asalkan ada **adapter** yang cocok dengan colokan lokal.

Dalam analogi ini, "colokan standar" mewakili konsep apa?

- **A.** Adapter — karena bentuknya bermacam-macam
- **B.** Port — karena port adalah kontrak/interface standar yang harus dipenuhi
- **C.** Domain — karena domain adalah inti listrik
- **D.** Controller — karena controller mengontrol aliran listrik

---

### Soal 2 — Kasus: Ganti Penyimpanan 🗄️

Perhatikan kode berikut:

```java
interface SimpanPesananPort {
    void simpan(Pesanan pesanan);
}

class SimpanKeMemory implements SimpanPesananPort {
    public void simpan(Pesanan p) { /* simpan ke Map */ }
}

class SimpanKePostgres implements SimpanPesananPort {
    public void simpan(Pesanan p) { /* INSERT INTO ... */ }
}
```

Apa **keuntungan utama** arsitektur ini?

- **A.** Kode menjadi lebih panjang sehingga terlihat profesional
- **B.** Bisa mengganti implementasi penyimpanan (memory → Postgres) tanpa mengubah inti bisnis sedikit pun
- **C.** `SimpanKePostgres` otomatis lebih cepat dari `SimpanKeMemory`
- **D.** Hanya bisa digunakan untuk penyimpanan database relasional

---

### Soal 3 — Kasus: Driving vs Driven ⬅️➡️

Dalam Hexagonal Architecture:
- **Driving side** (sisi kiri): yang *memanggil* aplikasi
- **Driven side** (sisi kanan): yang *dipanggil* oleh aplikasi

Mana yang termasuk **driven adapter**?

- **A.** REST Controller yang menerima HTTP request
- **B.** CLI yang menerima input dari terminal
- **C.** Adapter yang mengirim email lewat SMTP server
- **D.** Unit test yang memanggil use case

---

### Soal 4 — Kasus: Dependency Rule 📏

Seorang developer menulis kode di mana class `BuatPesananService` (inti bisnis) melakukan `import com.mysql.jdbc.Connection` secara langsung.

Aturan Hexagonal Architecture apa yang dilanggar?

- **A.** Tidak ada yang dilanggar — inti bisnis perlu tahu cara menyimpan data
- **B.** Dependency Rule — inti bisnis tidak boleh bergantung pada teknologi luar. Database harus diakses lewat output port (interface)
- **C.** Inti bisnis harus selalu menggunakan PostgreSQL, bukan MySQL
- **D.** Import hanya boleh dilakukan di class `Main`

---

### Soal 5 — Kasus: Testing Tanpa Database 🧪

Tim QA ingin menjalankan unit test untuk logika bisnis **tanpa** menyalakan database atau server email. Dengan Hexagonal Architecture, bagaimana caranya?

- **A.** Tidak mungkin — bisnis logic selalu butuh database
- **B.** Buat adapter palsu (mock/stub) yang mengimplementasikan output port, lalu inject ke service saat testing
- **C.** Tulis ulang semua kode tanpa database
- **D.** Gunakan database H2 karena itu satu-satunya cara

---

### Soal 6 — Kasus: Port Input vs Output 🔀

Perhatikan diagram berikut:

```
REST API ──► [BuatPesananUseCase] ──► BuatPesananService ──► [SimpanPesananPort] ──► Database
(driving)    (input port)              (inti bisnis)          (output port)          (driven)
```

Interface mana yang didefinisikan oleh **inti bisnis** (bukan oleh teknologi luar)?

- **A.** Hanya `BuatPesananUseCase`
- **B.** Hanya `SimpanPesananPort`
- **C.** Keduanya — baik input port maupun output port didefinisikan oleh inti bisnis
- **D.** Tidak ada — semua interface didefinisikan oleh adapter

---

### Soal 7 — Kasus: Notifikasi Multi-Channel 📧📱

Sebuah aplikasi mengirim notifikasi lewat Email. Kemudian ada permintaan baru: kirim juga lewat WhatsApp dan SMS. Dengan Hexagonal Architecture, apa yang perlu dilakukan?

- **A.** Ubah service inti untuk menambah logika WhatsApp dan SMS
- **B.** Buat adapter baru (`NotifWhatsApp`, `NotifSMS`) yang mengimplementasikan output port `KirimNotifikasiPort`, tanpa mengubah inti bisnis
- **C.** Copy-paste class `NotifEmail` dan ganti isinya
- **D.** Buat service baru untuk setiap channel notifikasi

---

### Soal 8 — Kasus: Salah Struktur 🚫

Seorang developer membuat struktur project seperti ini:

```
src/
├── controller/
│   └── PesananController.java  // memanggil langsung PesananEntity
├── entity/
│   └── PesananEntity.java      // import javax.persistence, @Entity
├── service/
│   └── PesananService.java     // import java.sql.Connection
```

Apa yang **salah** dari perspektif Hexagonal Architecture?

- **A.** Tidak ada yang salah — ini struktur standar
- **B.** `PesananEntity` dan `PesananService` bergantung pada teknologi (JPA, JDBC). Seharusnya domain model murni, dan teknologi hanya muncul di adapter layer
- **C.** Controller seharusnya tidak ada
- **D.** Service harus bernama `PesananFacade`

---

### Soal 9 — Kasus: Migrasi Teknologi 🔄

Perusahaan memutuskan berpindah dari MySQL ke MongoDB. Dengan Hexagonal Architecture yang diterapkan dengan benar, bagian kode mana yang harus berubah?

- **A.** Seluruh kode harus ditulis ulang
- **B.** Hanya adapter penyimpanan (implementasi output port) — inti bisnis dan input port tidak berubah
- **C.** Hanya domain model dan business rules
- **D.** Hanya controller dan REST API

---

### Soal 10 — Kasus: Arsitektur Berlapis 🏗️

Seorang mahasiswa bertanya: *"Apa bedanya Hexagonal Architecture dengan Layered Architecture biasa (Controller → Service → Repository)?"*

Jawaban yang **paling tepat**:

- **A.** Tidak ada bedanya — keduanya sama saja
- **B.** Di Layered Architecture, dependency selalu mengalir satu arah ke bawah. Di Hexagonal, inti bisnis ada di tengah dan **tidak bergantung** ke lapisan mana pun — adapter di luar yang bergantung ke inti lewat port (Dependency Inversion)
- **C.** Hexagonal hanya untuk microservices, Layered untuk monolith
- **D.** Layered lebih modern dari Hexagonal

---

## ✅ Kunci Jawaban

<details>
<summary><b>Klik untuk melihat jawaban</b></summary>

| Soal | Jawaban | Penjelasan Singkat |
|:----:|:-------:|:-------------------|
| 1 | **B** | Port = kontrak standar (interface). Adapter = implementasi spesifik yang menyesuaikan teknologi tertentu ke kontrak tersebut. |
| 2 | **B** | Output port sebagai interface memungkinkan swap implementasi tanpa mengubah business logic — inti dari Hexagonal Architecture. |
| 3 | **C** | Driven adapter adalah yang dipanggil oleh aplikasi ke dunia luar: kirim email, simpan ke database, panggil API eksternal. REST Controller dan CLI adalah driving adapter (memanggil masuk). |
| 4 | **B** | Inti bisnis hanya boleh bergantung pada interface (port). Dependency ke library database berarti inti bisnis terikat teknologi tertentu. |
| 5 | **B** | Output port berbentuk interface — tinggal buat implementasi palsu (mock) yang mengembalikan data dummy. Service diuji tanpa infrastruktur nyata. |
| 6 | **C** | Kedua port (input & output) didefinisikan oleh inti bisnis. Port menentukan "apa yang dibutuhkan" — adapter menentukan "bagaimana pemenuhannya". |
| 7 | **B** | Tambah adapter baru tanpa sentuh inti bisnis. Inilah Open-Closed Principle yang dimungkinkan oleh Hexagonal Architecture. |
| 8 | **B** | Domain model seharusnya bebas dari anotasi dan import teknologi. Annotation `@Entity` dan `import java.sql` menyebabkan domain model terikat teknologi tertentu. |
| 9 | **B** | Dengan port & adapter, migrasi database hanya perlu membuat adapter baru (MongoDB) yang mengimplementasikan output port yang sama. Business rules tetap utuh. |
| 10 | **B** | Hexagonal menerapkan Dependency Inversion: domain di pusat, semua dependency mengarah ke dalam (ke domain). Layered Architecture tradisional punya dependency mengalir ke bawah. |

</details>

---

## 🔗 Navigasi

| Sebelumnya | Berikutnya |
|:-----------|:-----------|
| [📝 Quiz DDD](06-quiz-ddd.md) | [📝 Quiz Business Object →](08-quiz-business.md) |

---

<p align="center">
  <b>Wahyu Amaldi, M.Kom</b> · Universitas Cakrawala
</p>
