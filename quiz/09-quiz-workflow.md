# 📝 Latihan Soal — Bab 9: Workflow Engine

> **Penulis:** Wahyu Amaldi, M.Kom · **Institusi:** Universitas Cakrawala

[⬅️ Kembali ke Beranda](../README.md) · [📖 Materi Workflow Engine](../docs/09-workflow-engine.md)

---

## Petunjuk

- Pilih **satu jawaban** yang paling tepat untuk setiap soal.
- Setiap soal berbasis **skenario** — baca kasus dengan cermat.
- Kunci jawaban tersedia di [bagian bawah](#-kunci-jawaban).

---

### Soal 1 — Kasus: Analogi ATM 🏧

Sebuah mesin ATM memiliki interaksi:
1. Masukkan kartu → status IDLE berubah ke AUTENTIKASI
2. Masukkan PIN → jika benar, status berubah ke MENU
3. Pilih tarik tunai → status berubah ke PROSES

Ini adalah contoh konsep:

- **A.** Observer Pattern — karena mesin mengamati pengguna
- **B.** State Machine — karena mesin memiliki status-status yang bertransisi berdasarkan aksi tertentu
- **C.** Factory Pattern — karena mesin memproduksi uang
- **D.** Strategy Pattern — karena ada banyak cara tarik tunai

---

### Soal 2 — Kasus: Guard dalam Pengajuan Cuti 🛡️

Sistem pengajuan cuti memiliki aturan:
> Pengajuan hanya boleh dilakukan jika sisa cuti mencukupi.

Dalam konteks workflow engine, pengecekan ini disebut:

- **A.** Effect — karena dilakukan sebagai efek samping
- **B.** Transition — karena ini bagian dari perpindahan status
- **C.** Guard — karena ini adalah syarat yang harus terpenuhi SEBELUM transisi diizinkan
- **D.** State — karena ini menentukan status awal

---

### Soal 3 — Kasus: Effect Setelah Approval ⚡

Setelah cuti karyawan disetujui HR, sistem otomatis:
1. Mengurangi sisa cuti karyawan
2. Mengirim email ke karyawan

Aksi otomatis ini dalam workflow engine disebut:

- **A.** Guard — karena ini terjadi sebagai proteksi
- **B.** State — karena ini mengubah status
- **C.** Effect — karena ini adalah aksi yang dijalankan SETELAH transisi berhasil
- **D.** Transition — karena ini bagian dari perpindahan

---

### Soal 4 — Kasus: Transisi Tidak Valid ❌

Seorang karyawan mencoba meng-approve cuti yang statusnya sudah `DITOLAK`. Apa yang seharusnya terjadi?

- **A.** Sistem otomatis mengubah status ke DISETUJUI karena yang terakhir yang berlaku
- **B.** Workflow engine menolak transisi karena tidak ada transisi valid dari DITOLAK ke DISETUJUI
- **C.** Status kembali ke DRAFT
- **D.** Sistem crash

---

### Soal 5 — Kasus: Deklaratif vs Imperatif 📋

Perbandingan dua pendekatan:

**Pendekatan A (Imperatif):**
```java
if (status == DIAJUKAN && role == ATASAN) {
    if (sisaCuti >= jumlahHari) {
        status = DISETUJUI_ATASAN;
        kirimEmailHR();
    }
} else if (status == DISETUJUI_ATASAN && role == HR) {
    status = DISETUJUI_HR;
    kurangiCuti();
    kirimEmailKaryawan();
}
```

**Pendekatan B (Deklaratif):**
```java
engine.daftarTransisi(new Transition("approve_atasan",
    DIAJUKAN, DISETUJUI_ATASAN,
    List.of(cutiCukupGuard),
    List.of(notifHREffect)));

engine.daftarTransisi(new Transition("approve_hr",
    DISETUJUI_ATASAN, DISETUJUI_HR,
    List.of(),
    List.of(kurangiCutiEffect, notifKaryawanEffect)));
```

Keunggulan Pendekatan B:

- **A.** Lebih lambat jadi lebih aman
- **B.** Alur workflow terbaca jelas sebagai data (dari-ke), mudah ditambah transisi baru tanpa ubah if-else, dan guard/effect bisa di-reuse
- **C.** Tidak bisa dipakai di Java
- **D.** Hanya cocok untuk kasus cuti saja

---

### Soal 6 — Kasus: Komponen Workflow Engine 🔧

Cocokkan komponen workflow engine dengan penjelasannya:

| No | Komponen | Penjelasan |
|:--:|:---------|:-----------|
| 1  | State    | ? |
| 2  | Guard    | ? |
| 3  | Effect   | ? |
| 4  | Transition | ? |

Pilihan:
- **A.** 1=Aksi setelah transisi, 2=Status saat ini, 3=Syarat, 4=Perpindahan status
- **B.** 1=Status/kondisi saat ini, 2=Syarat sebelum transisi, 3=Aksi setelah transisi berhasil, 4=Definisi perpindahan dari satu state ke state lain
- **C.** 1=Perpindahan, 2=Aksi, 3=Status, 4=Syarat
- **D.** 1=Syarat, 2=Status, 3=Perpindahan, 4=Aksi

---

### Soal 7 — Kasus: Multi-Level Approval 🏢

Sebuah dokumen harus disetujui oleh 3 level: Supervisor → Manajer → Direktur. Masing-masing level bisa **menyetujui** (ke level berikutnya) atau **menolak** (kembali ke pembuat).

Berapa **transisi minimum** yang diperlukan?

Alur: DRAFT → submit → REVIEW_SPV → [approve/reject]
     REVIEW_SPV → approve → REVIEW_MGR → [approve/reject]
     REVIEW_MGR → approve → REVIEW_DIR → [approve/reject]
     REVIEW_DIR → approve → SELESAI

- **A.** 3 transisi
- **B.** 4 transisi (3 approve + 1 reject)
- **C.** 7 transisi (1 submit + 3 approve + 3 reject)
- **D.** 10 transisi

---

### Soal 8 — Kasus: Guard yang Bisa Di-reuse ♻️

Perhatikan guard berikut:
```java
class CutiCukupGuard implements Guard {
    public boolean boleh(CutiRequest req) {
        return req.getSisaCuti() >= req.getJumlahHari();
    }
}
```

Guard ini bisa dipakai di transisi mana saja?

- **A.** Hanya di transisi DRAFT → DIAJUKAN
- **B.** Di SEMUA transisi yang perlu memastikan sisa cuti mencukupi — guard bersifat reusable
- **C.** Hanya di transisi yang melibatkan HR
- **D.** Guard tidak bisa di-reuse — harus dibuat baru untuk setiap transisi

---

### Soal 9 — Kasus: Workflow untuk Reimburse 💸

Sebuah proses reimburse memiliki alur:
```
DRAFT → [submit] → REVIEW → [approve] → FINANCE → [bayar] → SELESAI
                      ↓
                  [tolak] → DITOLAK
```

Guard untuk transisi "bayar": saldo kas perusahaan harus mencukupi.
Effect untuk transisi "bayar": kurangi kas, kirim transfer, kirim notif.

Jika saldo kas perusahaan tidak mencukupi, apa yang terjadi saat transisi "bayar" dicoba?

- **A.** Transisi tetap berjalan dan kas menjadi negatif
- **B.** Guard mencegah transisi — status tetap di FINANCE, effect tidak dijalankan
- **C.** Sistem crash karena kas negatif
- **D.** Status otomatis berubah ke DITOLAK

---

### Soal 10 — Kasus: Menambah Status Baru 🆕

Perusahaan menambahkan aturan baru: setelah disetujui atasan, cuti harus di-review oleh Kepala Departemen sebelum masuk ke HR.

Alur lama: DIAJUKAN → DISETUJUI_ATASAN → DISETUJUI_HR
Alur baru: DIAJUKAN → DISETUJUI_ATASAN → REVIEW_KADEP → DISETUJUI_HR

Dengan workflow engine yang deklaratif, apa yang perlu dilakukan?

- **A.** Tulis ulang seluruh workflow engine
- **B.** Tambahkan state `REVIEW_KADEP`, ubah transisi lama yang mengarah ke DISETUJUI_HR dari DISETUJUI_ATASAN menjadi ke REVIEW_KADEP, lalu tambah transisi baru dari REVIEW_KADEP ke DISETUJUI_HR
- **C.** Hapus semua guard dan tulis ulang
- **D.** Tidak mungkin menambah status baru tanpa membangun engine dari awal

---

## ✅ Kunci Jawaban

<details>
<summary><b>Klik untuk melihat jawaban</b></summary>

| Soal | Jawaban | Penjelasan Singkat |
|:----:|:-------:|:-------------------|
| 1 | **B** | ATM memiliki status (IDLE, AUTENTIKASI, MENU, PROSES) yang bertransisi berdasarkan aksi — definisi state machine. |
| 2 | **C** | Guard adalah syarat yang harus terpenuhi sebelum transisi diizinkan. Jika guard gagal, transisi ditolak. |
| 3 | **C** | Effect adalah aksi yang dijalankan otomatis setelah transisi berhasil — kurangi cuti, kirim email, catat log, dsb. |
| 4 | **B** | Workflow engine hanya mengizinkan transisi yang sudah didaftarkan. Tidak ada transisi dari DITOLAK ke DISETUJUI, jadi engine menolak. |
| 5 | **B** | Pendekatan deklaratif membuat alur workflow terbaca sebagai data, mudah dipahami, dan komponen (guard, effect) bisa di-reuse di transisi lain. |
| 6 | **B** | State = status. Guard = syarat. Effect = aksi setelah berhasil. Transition = definisi perpindahan. |
| 7 | **C** | 1 submit (DRAFT→REVIEW_SPV) + 3 approve (SPV→MGR, MGR→DIR, DIR→SELESAI) + 3 reject (SPV→DRAFT, MGR→DRAFT, DIR→DRAFT) = 7. |
| 8 | **B** | Guard dipisahkan sebagai objek tersendiri sehingga bisa dipasang di transisi mana saja yang membutuhkan pengecekan serupa — prinsip reusability. |
| 9 | **B** | Guard dievaluasi SEBELUM transisi. Jika gagal, status tidak berubah dan effect tidak dijalankan. |
| 10 | **B** | Workflow engine deklaratif memungkinkan penambahan state dan transisi baru tanpa mengubah engine itu sendiri — cukup daftarkan transisi baru. |

</details>

---

## 🔗 Navigasi

| Sebelumnya | Berikutnya |
|:-----------|:-----------|
| [📝 Quiz Business Object](08-quiz-business.md) | [📝 Quiz Modular Monolith →](10-quiz-modular.md) |

---

<p align="center">
  <b>Wahyu Amaldi, M.Kom</b> · Universitas Cakrawala
</p>
