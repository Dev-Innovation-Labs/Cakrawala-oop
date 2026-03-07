# 📋 Kurikulum OBE — Object-Oriented Programming: Dari Fondasi Hingga Arsitektur Bisnis

> **Penulis:** Wahyu Amaldi, M.Kom · **Institusi:** Universitas Cakrawala

[⬅️ Kembali ke Beranda](../README.md)

---

## Identitas Mata Kuliah

| Komponen | Detail |
|:---------|:-------|
| **Nama MK** | Pemrograman Berorientasi Objek (OOP) |
| **Kode MK** | IF-2XX |
| **SKS** | 3 SKS (Teori 2 + Praktikum 1) |
| **Semester** | III (Genap/Ganjil) |
| **Program Studi** | Informatika / Sistem Informasi |
| **Prasyarat** | Algoritma & Pemrograman Dasar |
| **Dosen Pengampu** | Wahyu Amaldi, M.Kom |
| **Bahasa Pemrograman** | Java (JDK 17+) |

---

## Deskripsi Mata Kuliah

Mata kuliah ini membekali mahasiswa dengan pemahaman mendalam tentang paradigma **Object-Oriented Programming** — mulai dari empat pilar fundamental (Encapsulation, Inheritance, Polymorphism, Abstraction) hingga penerapannya dalam arsitektur perangkat lunak bisnis (Design Patterns, Domain Driven Design, Hexagonal Architecture, Business Object, Workflow Engine, Modular Monolith).

Mahasiswa tidak hanya belajar **teori**, tetapi langsung mempraktikkan setiap konsep melalui **studi kasus bisnis nyata** menggunakan Java.

---

## Course Learning Outcomes (CLO)

> CLO disusun berdasarkan **Taksonomi Bloom** — dari memahami (C2) hingga mencipta (C6).

| CLO | Deskripsi | Level Bloom | Bab |
|:---:|:----------|:-----------:|:---:|
| **CLO-1** | Mahasiswa mampu **menjelaskan** konsep encapsulation dan menerapkan access modifier serta getter/setter untuk melindungi data objek | C2 — Memahami | 1 |
| **CLO-2** | Mahasiswa mampu **menerapkan** inheritance untuk membangun hierarki class dan membedakan relasi is-a vs has-a | C3 — Menerapkan | 2 |
| **CLO-3** | Mahasiswa mampu **menganalisis** perbedaan overloading dan overriding serta menerapkan polymorphism dalam skenario bisnis | C4 — Menganalisis | 3 |
| **CLO-4** | Mahasiswa mampu **merancang** solusi menggunakan abstract class dan interface sebagai kontrak abstraksi | C5 — Mengevaluasi | 4 |
| **CLO-5** | Mahasiswa mampu **memilih** design pattern yang tepat (Strategy, Observer, Factory, State) untuk masalah bisnis tertentu | C4 — Menganalisis | 5 |
| **CLO-6** | Mahasiswa mampu **membangun** domain model menggunakan konsep DDD (Entity, Value Object, Aggregate, Repository) | C5 — Mengevaluasi | 6 |
| **CLO-7** | Mahasiswa mampu **merancang** aplikasi dengan Hexagonal Architecture dan mendemonstrasikan swap adapter | C5 — Mengevaluasi | 7 |
| **CLO-8** | Mahasiswa mampu **menerapkan** prinsip Business Object (Rich Domain Model) dan Application Service dalam studi kasus bisnis | C3 — Menerapkan | 8 |
| **CLO-9** | Mahasiswa mampu **membangun** workflow engine sederhana dengan konsep State Machine, Guard, dan Effect | C6 — Mencipta | 9 |
| **CLO-10** | Mahasiswa mampu **mendesain** arsitektur Modular Monolith dengan Event Bus dan batas modul yang jelas | C6 — Mencipta | 10 |

---

## Pemetaan CLO → PLO (Program Learning Outcomes)

| CLO | PLO-1 | PLO-2 | PLO-3 | PLO-4 | PLO-5 | PLO-6 |
|:---:|:-----:|:-----:|:-----:|:-----:|:-----:|:-----:|
| CLO-1 | ✅ | | ✅ | | | |
| CLO-2 | ✅ | | ✅ | | | |
| CLO-3 | ✅ | ✅ | ✅ | | | |
| CLO-4 | ✅ | ✅ | ✅ | | | |
| CLO-5 | ✅ | ✅ | ✅ | ✅ | | |
| CLO-6 | ✅ | ✅ | | ✅ | ✅ | |
| CLO-7 | ✅ | ✅ | | ✅ | ✅ | |
| CLO-8 | ✅ | ✅ | | ✅ | ✅ | |
| CLO-9 | ✅ | ✅ | | ✅ | ✅ | ✅ |
| CLO-10 | ✅ | ✅ | | ✅ | ✅ | ✅ |

**Keterangan PLO:**

| PLO | Deskripsi |
|:---:|:----------|
| PLO-1 | Mampu menerapkan dasar-dasar ilmu komputer dan pemrograman |
| PLO-2 | Mampu menganalisis dan merancang solusi perangkat lunak |
| PLO-3 | Mampu mengimplementasikan program dengan paradigma OOP |
| PLO-4 | Mampu menerapkan design pattern dan prinsip desain perangkat lunak |
| PLO-5 | Mampu merancang arsitektur perangkat lunak yang modular dan maintainable |
| PLO-6 | Mampu membangun prototipe sistem bisnis berbasis domain |

---

## Rencana Pembelajaran Semester (RPS) — 16 Minggu

### Part I: Fondasi OOP (Minggu 1–8)

| Minggu | Topik | Bab | CLO | Kegiatan | Asesmen |
|:------:|:------|:---:|:---:|:---------|:--------|
| **1** | Pengantar OOP & Kontrak Kuliah | — | — | Ceramah, diskusi motivasi, setup JDK | — |
| **2** | **Encapsulation** — Access Modifier, Getter/Setter, Validasi | 1 | CLO-1 | Teori + Live coding `BankAccount` | Quiz Bab 1 |
| **3** | **Inheritance** — Hierarki class, `extends`, `super`, is-a vs has-a | 2 | CLO-2 | Teori + Live coding `Animal` | Quiz Bab 2 |
| **4** | **Polymorphism** — Overloading, Overriding, Runtime dispatch | 3 | CLO-3 | Teori + Live coding `Shape` | Quiz Bab 3 |
| **5** | **Abstraction** — Abstract class, Interface, kontrak desain | 4 | CLO-4 | Teori + Live coding `Vehicle` | Quiz Bab 4 |
| **6** | **Workshop Part I** — Studi kasus: bangun mini-app dengan 4 pilar OOP | 1–4 | CLO 1–4 | Coding workshop (kelompok) | Tugas Kelompok 1 |
| **7** | **Review & Presentasi** — Presentasi mini-app kelompok | 1–4 | CLO 1–4 | Presentasi + peer review | Presentasi |
| **8** | **UTS** — Ujian Tengah Semester | 1–4 | CLO 1–4 | Ujian tertulis + coding | **UTS (30%)** |

### Part II: Pola Desain & Arsitektur Bisnis (Minggu 9–16)

| Minggu | Topik | Bab | CLO | Kegiatan | Asesmen |
|:------:|:------|:---:|:---:|:---------|:--------|
| **9** | **Design Patterns** — Strategy, Observer, Factory, State | 5 | CLO-5 | Teori + Live coding `DiscountStrategy` | Quiz Bab 5 |
| **10** | **Domain Driven Design** — Entity, Value Object, Aggregate, Repository | 6 | CLO-6 | Teori + Live coding `OrderAggregate` | Quiz Bab 6 |
| **11** | **Hexagonal Architecture** — Port & Adapter, Dependency Rule | 7 | CLO-7 | Teori + Live coding `HexagonalOrder` | Quiz Bab 7 |
| **12** | **Business Object & Process** — Rich Domain Model, Application Service | 8 | CLO-8 | Teori + Live coding `PurchaseOrder` | Quiz Bab 8 |
| **13** | **Workflow Engine** — State Machine, Guard, Effect | 9 | CLO-9 | Teori + Live coding `LeaveRequest` | Quiz Bab 9 |
| **14** | **Modular Monolith** — Module Boundary, Event Bus, Domain Event | 10 | CLO-10 | Teori + Live coding `ModularECommerce` | Quiz Bab 10 |
| **15** | **Proyek Akhir** — Bangun mini-ERP modular (kelompok) | 5–10 | CLO 5–10 | Workshop proyek akhir | Tugas Kelompok 2 |
| **16** | **UAS** — Ujian Akhir Semester | 1–10 | CLO 1–10 | Ujian tertulis + presentasi proyek | **UAS (30%)** |

---

## Komponen & Bobot Penilaian

| Komponen | Bobot | Deskripsi |
|:---------|:-----:|:----------|
| **Quiz** (10 kali) | 15% | Quiz berbasis skenario per bab (Bab 1–10) |
| **Tugas Kelompok 1** | 10% | Mini-app dengan 4 pilar OOP (Minggu 6–7) |
| **UTS** | 25% | Ujian tertulis + coding (Bab 1–4) |
| **Tugas Kelompok 2** | 15% | Proyek akhir mini-ERP modular (Minggu 15) |
| **UAS** | 25% | Ujian tertulis + presentasi proyek (Bab 1–10) |
| **Partisipasi** | 10% | Kehadiran, diskusi, peer review |
| **Total** | **100%** | |

---

## Rubrik Penilaian Proyek Akhir (Tugas Kelompok 2)

| Kriteria | Bobot | Skor 4 (Sangat Baik) | Skor 3 (Baik) | Skor 2 (Cukup) | Skor 1 (Kurang) |
|:---------|:-----:|:---------------------|:---------------|:----------------|:----------------|
| **Domain Model** | 25% | Entity, VO, Aggregate jelas dan sesuai DDD | Sebagian besar konsep DDD diterapkan | Ada upaya tapi kurang konsisten | Tidak ada domain model |
| **Arsitektur** | 25% | Hexagonal/Modular jelas, modul terpisah baik | Arsitektur cukup jelas tapi ada leaking | Struktur ada tapi batas modul kabur | Semua kode campur tanpa struktur |
| **Design Pattern** | 20% | Minimal 2 pattern diterapkan dengan tepat | 1 pattern diterapkan dengan tepat | Ada pattern tapi salah konteks | Tidak ada pattern |
| **Workflow** | 15% | State machine lengkap (guard + effect) | Ada state machine tapi tanpa guard/effect | Alur status ada tapi tidak pakai engine | Tidak ada alur status |
| **Dokumentasi & Presentasi** | 15% | README jelas, presentasi terstruktur | Dokumentasi cukup, presentasi lancar | Dokumentasi minim | Tidak ada dokumentasi |

---

## Rubrik Penilaian Quiz

| Skor | Kriteria |
|:----:|:---------|
| **100** | Semua jawaban benar dengan pemahaman konsep |
| **80** | 8 dari 10 jawaban benar |
| **60** | 6 dari 10 jawaban benar |
| **40** | 4 dari 10 jawaban benar |
| **< 40** | Kurang dari 4 jawaban benar — perlu remedial |

---

## Referensi & Sumber Belajar

### Buku Utama

1. **Bloch, Joshua.** *Effective Java* (3rd Edition). Addison-Wesley, 2018.
2. **Evans, Eric.** *Domain-Driven Design: Tackling Complexity in the Heart of Software.* Addison-Wesley, 2003.
3. **Gamma et al.** *Design Patterns: Elements of Reusable Object-Oriented Software.* Addison-Wesley, 1994.

### Buku Pendukung

4. **Vernon, Vaughn.** *Domain-Driven Design Distilled.* Addison-Wesley, 2016.
5. **Cockburn, Alistair.** *Hexagonal Architecture (Ports & Adapters).* 2005.
6. **Newman, Sam.** *Building Microservices.* O'Reilly, 2021.

### Sumber Daring

7. **Repository Cakrawala-OOP** — Material utama mata kuliah (repo ini)
8. **Oracle Java Documentation** — https://docs.oracle.com/javase/
9. **Refactoring Guru** — https://refactoring.guru/design-patterns

---

## Peta Pembelajaran

```
Minggu 1-2    Minggu 3-4    Minggu 5       Minggu 6-8
┌──────┐     ┌──────┐     ┌──────────┐    ┌──────────┐
│Encap.│────▶│Inhe- │────▶│Poly. +   │───▶│Workshop +│
│      │     │ritance│    │Abstraction│   │  UTS     │
└──────┘     └──────┘     └──────────┘    └──────────┘
   🏗️            🧬            🎭              📝

Minggu 9      Minggu 10-11   Minggu 12-13   Minggu 14-16
┌──────┐     ┌──────────┐   ┌──────────┐   ┌──────────┐
│Design│────▶│DDD +     │──▶│Business  │──▶│Modular + │
│Patt. │     │Hexagonal │   │+ Workflow│   │Proyek+UAS│
└──────┘     └──────────┘   └──────────┘   └──────────┘
   🧩             🏛️              ⚙️             🏬
```

---

## Catatan untuk Dosen

### Tips Pengajaran

1. **Gunakan analogi** — setiap bab memiliki analogi kehidupan nyata yang sudah disiapkan di material. Mulai setiap pertemuan dengan analogi sebelum masuk ke kode.

2. **Live coding** — jangan hanya menampilkan slide. Buka IDE, tulis kode dari nol, buat error yang disengaja, lalu perbaiki bersama mahasiswa.

3. **Studi kasus berkelanjutan** — hubungkan setiap bab. Contoh: `BankAccount` (Bab 1) bisa dikembangkan menjadi domain model bank (Bab 6), lalu dibungkus Hexagonal (Bab 7), lalu dijadikan modul dalam Modular Monolith (Bab 10).

4. **Quiz sebagai diskusi** — setelah mahasiswa mengerjakan quiz, bahas jawaban di kelas. Soal skenario memicu diskusi yang kaya.

5. **Peer review** — minta mahasiswa saling review kode kelompok lain. Ini melatih kemampuan membaca kode orang lain.

### Prerequisite Check

Sebelum memulai, pastikan mahasiswa sudah memahami:
- Variabel, tipe data, control flow (if, for, while)
- Method/function, parameter, return value
- Array atau List dasar
- Cara compile & run program Java sederhana

---

## 🔗 Navigasi

| Sebelumnya | Berikutnya |
|:-----------|:-----------|
| [⬅️ Beranda](../README.md) | [📖 Bab 1: Encapsulation →](01-encapsulation.md) |

---

<p align="center">
  <b>Wahyu Amaldi, M.Kom</b> · Universitas Cakrawala
</p>
