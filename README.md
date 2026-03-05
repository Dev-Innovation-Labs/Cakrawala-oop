# 🏛️ Cakrawala OOP — Menguasai 4 Pilar Object-Oriented Programming

<p align="center">
  <img src="https://img.shields.io/badge/Bahasa-Indonesia-red?style=for-the-badge" alt="Bahasa Indonesia"/>
  <img src="https://img.shields.io/badge/Language-Java-orange?style=for-the-badge&logo=java" alt="Java"/>
  <img src="https://img.shields.io/badge/Level-Beginner%20to%20Advanced-blue?style=for-the-badge" alt="Level"/>
  <img src="https://img.shields.io/badge/License-MIT-green?style=for-the-badge" alt="License"/>
</p>

<p align="center">
  <b>Knowledge base lengkap tentang 4 pilar OOP dalam Bahasa Indonesia, dilengkapi analogi dunia nyata dan contoh kode yang mudah dipahami.</b>
</p>

<p align="center">
  <b>Penulis:</b> Wahyu Amaldi, M.Kom<br/>
  <b>Institusi:</b> Universitas Cakrawala
</p>

---

## 📚 Daftar Isi

| # | Pilar | Deskripsi | Link |
|:-:|:------|:----------|:-----|
| 1 | **Encapsulation** | Membungkus data & menyembunyikan detail implementasi | [📖 Baca](docs/01-encapsulation.md) |
| 2 | **Inheritance** | Mewariskan sifat dari class induk ke class anak | [📖 Baca](docs/02-inheritance.md) |
| 3 | **Polymorphism** | Satu interface, banyak bentuk implementasi | [📖 Baca](docs/03-polymorphism.md) |
| 4 | **Abstraction** | Menyederhanakan kompleksitas dengan menampilkan hal esensial | [📖 Baca](docs/04-abstraction.md) |

### 📝 Latihan Soal (Multiple Choice — Skenario Based)

| # | Pilar | Jumlah Soal | Link |
|:-:|:------|:-----------:|:-----|
| 1 | Encapsulation | 10 soal | [📝 Kerjakan](quiz/01-quiz-encapsulation.md) |
| 2 | Inheritance | 10 soal | [📝 Kerjakan](quiz/02-quiz-inheritance.md) |
| 3 | Polymorphism | 10 soal | [📝 Kerjakan](quiz/03-quiz-polymorphism.md) |
| 4 | Abstraction | 10 soal | [📝 Kerjakan](quiz/04-quiz-abstraction.md) |

---

## 🤔 Apa Itu OOP?

**Object-Oriented Programming (OOP)** adalah paradigma pemrograman yang mengorganisasikan kode ke dalam **objek** — yaitu unit yang menggabungkan **data** (atribut/properti) dan **perilaku** (method/fungsi) menjadi satu kesatuan.

### Analogi Sederhana

Bayangkan kamu sedang membangun kota LEGO:

- Setiap **blok LEGO** adalah sebuah **objek**
- **Cetakan blok** adalah **class** (blueprint)
- Blok punya **warna, ukuran** → itu **atribut**
- Blok bisa **disambung, dilepas** → itu **method**

OOP memiliki **4 pilar utama** yang menjadi fondasi:

```
                    ┌─────────────────────────────┐
                    │   OBJECT-ORIENTED            │
                    │   PROGRAMMING (OOP)          │
                    └──────────┬──────────────────┘
                               │
          ┌────────────────────┼────────────────────┐
          │                    │                     │
    ┌─────┴──────┐     ┌──────┴──────┐      ┌──────┴──────┐
    │            │     │             │      │             │
    │ ENCAPSU-   │     │ INHERI-     │      │ POLYMOR-    │
    │ LATION     │     │ TANCE      │      │ PHISM       │
    │            │     │             │      │             │
    └────────────┘     └─────────────┘      └─────────────┘
                               │
                       ┌───────┴───────┐
                       │               │
                       │ ABSTRACTION   │
                       │               │
                       └───────────────┘
```

---

## 🗂️ Struktur Repositori

```
Cakrawala-OOP/
│
├── README.md                          # Halaman utama (kamu di sini!)
│
├── docs/
│   ├── 01-encapsulation.md            # Pilar 1: Encapsulation
│   ├── 02-inheritance.md              # Pilar 2: Inheritance
│   ├── 03-polymorphism.md             # Pilar 3: Polymorphism
│   └── 04-abstraction.md             # Pilar 4: Abstraction
│
├── samples/
│   ├── encapsulation/
│   │   └── BankAccount.java           # Contoh Encapsulation
│   ├── inheritance/
│   │   └── Animal.java                # Contoh Inheritance
│   ├── polymorphism/
│   │   └── Shape.java                 # Contoh Polymorphism
│   └── abstraction/
│       └── Vehicle.java               # Contoh Abstraction
│
└── quiz/
    ├── 01-quiz-encapsulation.md       # Latihan soal Encapsulation
    ├── 02-quiz-inheritance.md         # Latihan soal Inheritance
    ├── 03-quiz-polymorphism.md        # Latihan soal Polymorphism
    └── 04-quiz-abstraction.md         # Latihan soal Abstraction
```

---

## 🎯 Untuk Siapa Repo Ini?

- 🧑‍🎓 **Mahasiswa** yang sedang mempelajari konsep OOP
- 💼 **Developer pemula** yang ingin memperkuat fondasi
- 📝 **Siapa saja** yang ingin memahami OOP dengan analogi sederhana

---

## 🚀 Cara Menggunakan

1. **Baca dokumentasi** di folder `docs/` secara berurutan (1 → 4)  
2. **Pelajari contoh kode** di folder `samples/`  
3. **Kerjakan latihan soal** di folder `quiz/` untuk uji pemahaman  
4. **Coba modifikasi** kode contoh untuk eksperimen sendiri

```bash
# Clone repositori ini
git clone https://github.com/wahyuamaldi/Cakrawala-OOP.git

# Compile & jalankan contoh (butuh Java JDK)
cd samples/encapsulation
javac BankAccount.java
java BankAccount
```

---

## 📊 Ringkasan Cepat — 4 Pilar OOP

| Pilar | Kata Kunci | Analogi | Manfaat Utama |
|:------|:-----------|:--------|:--------------|
| **Encapsulation** | `private`, getter/setter | Kapsul obat — isi terlindungi | Keamanan data |
| **Inheritance** | `extends`, `super` | Anak mewarisi sifat orang tua | Reusability kode |
| **Polymorphism** | `@Override`, overloading | Remote TV — satu tombol, beda aksi | Fleksibilitas |
| **Abstraction** | `abstract`, `interface` | Dashboard mobil — pakai tanpa tahu mesin | Kesederhanaan |

---

## 📄 Lisensi

Repositori ini dilisensikan di bawah [MIT License](LICENSE).

---

<p align="center">
  <b>Wahyu Amaldi, M.Kom</b><br/>
  Universitas Cakrawala<br/><br/>
  <i>Knowledge base ini disusun sebagai materi pembelajaran Object-Oriented Programming<br/>untuk mahasiswa Universitas Cakrawala.</i>
</p>
