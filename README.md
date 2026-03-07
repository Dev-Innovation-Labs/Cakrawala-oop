# 🏛️ Cakrawala OOP — Dari Fondasi OOP Hingga Arsitektur Bisnis

<p align="center">
  <img src="https://img.shields.io/badge/Bahasa-Indonesia-red?style=for-the-badge" alt="Bahasa Indonesia"/>
  <img src="https://img.shields.io/badge/Language-Java-orange?style=for-the-badge&logo=java" alt="Java"/>
  <img src="https://img.shields.io/badge/Level-Beginner%20to%20Advanced-blue?style=for-the-badge" alt="Level"/>
  <img src="https://img.shields.io/badge/License-MIT-green?style=for-the-badge" alt="License"/>
</p>

<p align="center">
  <b>Panduan lengkap berbahasa Indonesia — mulai dari 4 pilar OOP, Design Patterns,<br/>hingga arsitektur bisnis nyata: DDD, Hexagonal, Workflow Engine, dan Modular Monolith.</b>
</p>

<p align="center">
  <b>Penulis:</b> Wahyu Amaldi, M.Kom<br/>
  <b>Institusi:</b> Universitas Cakrawala
</p>

---

## 🗺️ Peta Pembelajaran

```
    PART I                          PART II
    Fondasi OOP                     Arsitektur & Bisnis
    ───────────                     ───────────────────
    
    ┌──────────────┐                ┌──────────────────┐
    │ 1 Encapsu-   │                │ 5 Design         │
    │   lation     │───┐            │   Patterns       │
    ├──────────────┤   │            ├──────────────────┤
    │ 2 Inheri-    │   │            │ 6 Domain Driven  │
    │   tance      │───┼──────────► │   Design (DDD)   │
    ├──────────────┤   │            ├──────────────────┤
    │ 3 Polymor-   │   │            │ 7 Hexagonal      │
    │   phism      │───┤            │   Architecture   │
    ├──────────────┤   │            ├──────────────────┤
    │ 4 Abstrac-   │   │            │ 8 Business Object│
    │   tion       │───┘            │   & Process      │
    └──────────────┘                ├──────────────────┤
                                    │ 9 Workflow       │
                                    │   Engine         │
                                    ├──────────────────┤
                                    │10 Modular        │
                                    │   Monolith       │
                                    └──────────────────┘
```

---

## 📚 Daftar Isi

### Part I — Fondasi OOP

| # | Topik | Deskripsi | Link |
|:-:|:------|:----------|:-----|
| 1 | **Encapsulation** | Membungkus data & melindungi detail internal | [📖 Baca](docs/01-encapsulation.md) |
| 2 | **Inheritance** | Mewariskan sifat dari class induk ke class anak | [📖 Baca](docs/02-inheritance.md) |
| 3 | **Polymorphism** | Satu interface, banyak bentuk implementasi | [📖 Baca](docs/03-polymorphism.md) |
| 4 | **Abstraction** | Menyederhanakan kompleksitas, tampilkan yang esensial | [📖 Baca](docs/04-abstraction.md) |

### Part II — Arsitektur & Domain Bisnis

| # | Topik | Deskripsi | Link |
|:-:|:------|:----------|:-----|
| 5 | **Design Patterns** | Pola desain yang terbukti: Strategy, Observer, Factory, State | [📖 Baca](docs/05-design-patterns.md) |
| 6 | **Domain Driven Design** | Entity, Value Object, Aggregate, Repository, Domain Event | [📖 Baca](docs/06-domain-driven-design.md) |
| 7 | **Hexagonal Architecture** | Port & Adapter — pisahkan domain dari infrastruktur | [📖 Baca](docs/07-hexagonal-architecture.md) |
| 8 | **Business Object & Process** | Modelkan objek dan proses bisnis nyata dengan OOP | [📖 Baca](docs/08-business-object-process.md) |
| 9 | **Workflow Engine** | Mesin alur kerja berbasis State Machine & object | [📖 Baca](docs/09-workflow-engine.md) |
| 10 | **Modular Monolith** | Arsitektur modular dengan Bounded Context | [📖 Baca](docs/10-modular-monolith.md) |

### 📝 Latihan Soal (Skenario Based)

| # | Topik | Soal | Link |
|:-:|:------|:----:|:-----|
| 1 | Encapsulation | 10 | [📝 Kerjakan](quiz/01-quiz-encapsulation.md) |
| 2 | Inheritance | 10 | [📝 Kerjakan](quiz/02-quiz-inheritance.md) |
| 3 | Polymorphism | 10 | [📝 Kerjakan](quiz/03-quiz-polymorphism.md) |
| 4 | Abstraction | 10 | [📝 Kerjakan](quiz/04-quiz-abstraction.md) |
| 5 | Design Patterns | 10 | [📝 Kerjakan](quiz/05-quiz-design-patterns.md) |
| 6 | Domain Driven Design | 10 | [📝 Kerjakan](quiz/06-quiz-ddd.md) |
| 7 | Hexagonal Architecture | 10 | [📝 Kerjakan](quiz/07-quiz-hexagonal.md) |
| 8 | Business Object & Process | 10 | [📝 Kerjakan](quiz/08-quiz-business.md) |
| 9 | Workflow Engine | 10 | [📝 Kerjakan](quiz/09-quiz-workflow.md) |
| 10 | Modular Monolith | 10 | [📝 Kerjakan](quiz/10-quiz-modular.md) |

### 📋 Kurikulum OBE

| Dokumen | Link |
|:--------|:-----|
| Kurikulum Outcome-Based Education (OBE) | [📋 Lihat](docs/00-obe-curriculum.md) |

---

## 🤔 Kenapa Repo Ini Berbeda?

Kebanyakan materi OOP berhenti di teori 4 pilar. **Cakrawala OOP** melangkah lebih jauh — menunjukkan bagaimana pilar-pilar itu **benar-benar digunakan** untuk membangun sistem bisnis nyata.

```
  Repo OOP biasa:                    Cakrawala OOP:
  ─────────────────                  ─────────────────────────
  ✅ Encapsulation                   ✅ Encapsulation
  ✅ Inheritance                     ✅ Inheritance
  ✅ Polymorphism                    ✅ Polymorphism
  ✅ Abstraction                     ✅ Abstraction
  ❌ Selesai.                        ✅ Design Patterns
                                     ✅ Domain Driven Design
                                     ✅ Hexagonal Architecture
                                     ✅ Business Object & Process
                                     ✅ Workflow Engine
                                     ✅ Modular Monolith
                                     ✅ Kurikulum OBE
```

### Analogi Perjalanan

Bayangkan belajar memasak:
- **Part I** = belajar teknik dasar (memotong, menumis, merebus, menggoreng)
- **Part II** = memasak hidangan lengkap untuk restoran sungguhan

Kamu tidak bisa membuka restoran hanya dengan tahu cara memotong bawang. Kamu juga butuh tahu cara **merancang menu**, **mengelola dapur**, dan **menyajikan pesanan**. Itulah Part II dari Cakrawala OOP.

---

## 🗂️ Struktur Repositori

```
Cakrawala-OOP/
│
├── README.md
│
├── docs/
│   ├── 00-obe-curriculum.md           # Kurikulum OBE
│   ├── 01-encapsulation.md            # Bab 1: Encapsulation
│   ├── 02-inheritance.md              # Bab 2: Inheritance
│   ├── 03-polymorphism.md             # Bab 3: Polymorphism
│   ├── 04-abstraction.md              # Bab 4: Abstraction
│   ├── 05-design-patterns.md          # Bab 5: Design Patterns
│   ├── 06-domain-driven-design.md     # Bab 6: Domain Driven Design
│   ├── 07-hexagonal-architecture.md   # Bab 7: Hexagonal Architecture
│   ├── 08-business-object-process.md  # Bab 8: Business Object & Process
│   ├── 09-workflow-engine.md          # Bab 9: Workflow Engine
│   └── 10-modular-monolith.md         # Bab 10: Modular Monolith
│
├── samples/
│   ├── encapsulation/
│   │   └── BankAccount.java
│   ├── inheritance/
│   │   └── Animal.java
│   ├── polymorphism/
│   │   └── Shape.java
│   ├── abstraction/
│   │   └── Vehicle.java
│   ├── design-patterns/
│   │   └── DiscountStrategy.java
│   ├── ddd/
│   │   └── OrderAggregate.java
│   ├── hexagonal/
│   │   └── HexagonalOrder.java
│   ├── business/
│   │   └── PurchaseOrder.java
│   ├── workflow/
│   │   └── LeaveRequest.java
│   └── modular/
│       └── ModularECommerce.java
│
└── quiz/
    ├── 01-quiz-encapsulation.md
    ├── 02-quiz-inheritance.md
    ├── 03-quiz-polymorphism.md
    ├── 04-quiz-abstraction.md
    ├── 05-quiz-design-patterns.md
    ├── 06-quiz-ddd.md
    ├── 07-quiz-hexagonal.md
    ├── 08-quiz-business.md
    ├── 09-quiz-workflow.md
    └── 10-quiz-modular.md
```

---

## 🎯 Untuk Siapa?

- 🧑‍🎓 **Mahasiswa** yang ingin memahami OOP dari dasar hingga arsitektur
- 💼 **Developer** yang ingin memperkuat fondasi desain perangkat lunak
- 🏢 **Praktisi** yang ingin menerapkan Domain Driven Design di proyek nyata
- 📝 **Dosen** yang membutuhkan materi OOP berbahasa Indonesia dengan kurikulum OBE

---

## 🚀 Cara Menggunakan

1. **Baca Part I** (Bab 1–4) untuk memahami fondasi OOP
2. **Kerjakan quiz** setiap bab untuk uji pemahaman
3. **Lanjut Part II** (Bab 5–10) untuk aplikasi arsitektural
4. **Pelajari contoh kode** di folder `samples/`
5. **Coba modifikasi** dan eksperimen sendiri

```bash
# Clone repositori
git clone https://github.com/wahyuamaldi/Cakrawala-OOP.git

# Compile & jalankan contoh (butuh Java JDK 17+)
cd samples/encapsulation
javac BankAccount.java
java BankAccount
```

---

## 📊 Ringkasan Semua Bab

### Part I — Fondasi

| Bab | Kata Kunci | Analogi | Manfaat |
|:----|:-----------|:--------|:--------|
| **Encapsulation** | `private`, getter/setter | Kapsul obat — isi terlindungi | Keamanan data |
| **Inheritance** | `extends`, `super` | Anak mewarisi sifat orang tua | Reusability kode |
| **Polymorphism** | `@Override`, overloading | Remote TV — satu tombol, beda aksi | Fleksibilitas |
| **Abstraction** | `abstract`, `interface` | Dashboard mobil — pakai tanpa tahu mesin | Kesederhanaan |

### Part II — Arsitektur

| Bab | Kata Kunci | Analogi | Manfaat |
|:----|:-----------|:--------|:--------|
| **Design Patterns** | Strategy, Observer, Factory | Resep masakan — solusi yang sudah terbukti | Kode bersih & teruji |
| **DDD** | Entity, Aggregate, Repository | Organisasi perusahaan — batas divisi jelas | Model bisnis akurat |
| **Hexagonal** | Port, Adapter | Colokan listrik — alat beda, port sama | Independen infrastruktur |
| **Business Object** | Order, Invoice, Payment | Formulir bisnis — alur kerja nyata | Logika bisnis terpusat |
| **Workflow Engine** | State, Transition, Guard | Mesin ATM — setiap tombol ubah kondisi | Alur terkontrol |
| **Modular Monolith** | Module, Bounded Context | Mal besar — setiap toko mandiri | Skalabilitas |

---

## 📄 Lisensi

Repositori ini dilisensikan di bawah [MIT License](LICENSE).

---

<p align="center">
  <b>Wahyu Amaldi, M.Kom</b><br/>
  Universitas Cakrawala<br/><br/>
  <i>Disusun sebagai materi pembelajaran Object-Oriented Programming & Software Architecture<br/>untuk mahasiswa Universitas Cakrawala.</i>
</p>
