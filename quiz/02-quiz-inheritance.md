# 📝 Latihan Soal — Pilar 2: Inheritance

> **Penulis:** Wahyu Amaldi, M.Kom · **Institusi:** Universitas Cakrawala

[⬅️ Kembali ke Beranda](../README.md) · [📖 Materi Inheritance](../docs/02-inheritance.md)

---

## Petunjuk

- Pilih **satu jawaban** yang paling tepat untuk setiap soal.
- Setiap soal berbasis **skenario** — baca kasus dengan cermat.
- Kunci jawaban tersedia di [bagian bawah](#-kunci-jawaban).

---

### Soal 1 — Kasus: Sistem Karyawan Perusahaan 🏢

Sebuah perusahaan memiliki banyak jenis karyawan: **Manager**, **Staff**, dan **Intern**. Ketiganya punya atribut yang sama (`nama`, `gaji`, `id`) dan method `absen()`. Namun masing-masing punya tambahan unik:

- Manager punya `jumlahTimAnggota`
- Staff punya `shift`
- Intern punya `durasiMagang`

Pendekatan mana yang **paling tepat** menggunakan inheritance?

- **A.** Buat 3 class terpisah tanpa hubungan, masing-masing mendefinisikan `nama`, `gaji`, `id`, dan `absen()` sendiri-sendiri
- **B.** Buat class `Karyawan` sebagai superclass dengan atribut & method umum, lalu `Manager`, `Staff`, `Intern` meng-extends `Karyawan`
- **C.** Buat satu class `Karyawan` saja dengan field `tipe` bertipe String untuk membedakan jenis
- **D.** Buat class `Manager` sebagai superclass, lalu `Staff` extends `Manager`, dan `Intern` extends `Staff`

---

### Soal 2 — Kasus: Error Misterius 🐛

Seorang developer menulis kode berikut:

```java
class Kendaraan {
    private String merk;

    public Kendaraan(String merk) {
        this.merk = merk;
    }
}

class Mobil extends Kendaraan {
    private int jumlahPintu;

    public Mobil(String merk, int jumlahPintu) {
        this.jumlahPintu = jumlahPintu;
    }
}
```

Kode ini menghasilkan **compile error**. Apa penyebabnya?

- **A.** Class `Mobil` tidak boleh punya field tambahan `jumlahPintu`
- **B.** Constructor `Mobil` tidak memanggil `super(merk)` untuk menginisialisasi superclass
- **C.** Keyword `extends` salah penggunaan
- **D.** Field `merk` di class `Kendaraan` harus `public`

---

### Soal 3 — Kasus: Pet Shop 🐾

Perhatikan kode berikut:

```java
class Hewan {
    protected String nama;

    public Hewan(String nama) {
        this.nama = nama;
    }

    public void bersuara() {
        System.out.println(nama + " bersuara...");
    }
}

class Kucing extends Hewan {
    public Kucing(String nama) {
        super(nama);
    }

    @Override
    public void bersuara() {
        System.out.println(nama + " berkata: Meow!");
    }
}
```

Apa output dari kode berikut?

```java
Hewan h = new Kucing("Milo");
h.bersuara();
```

- **A.** `Milo bersuara...`
- **B.** `Milo berkata: Meow!`
- **C.** Compile error karena variabel bertipe `Hewan` tidak bisa menyimpan objek `Kucing`
- **D.** Runtime error

---

### Soal 4 — Kasus: Tes "Is-A" 🧪

Manakah dari hubungan berikut yang **BENAR** menggunakan inheritance?

- **A.** `class Mesin extends Mobil` — karena Mobil punya Mesin
- **B.** `class Laptop extends Baterai` — karena Laptop butuh Baterai
- **C.** `class Mahasiswa extends Orang` — karena Mahasiswa **adalah** Orang
- **D.** `class Perpustakaan extends Buku` — karena Perpustakaan punya banyak Buku

---

### Soal 5 — Kasus: Pewarisan yang Salah Kaprah ⚠️

Perhatikan hierarki class berikut:

```java
class A {
    private int x = 10;
    protected int y = 20;
    public int z = 30;
}

class B extends A {
    public void tampilkan() {
        System.out.println(x);  // Baris 1
        System.out.println(y);  // Baris 2
        System.out.println(z);  // Baris 3
    }
}
```

Baris mana yang menyebabkan **compile error**?

- **A.** Baris 1 saja — `x` bersifat `private`, tidak diwarisi
- **B.** Baris 2 saja — `protected` tidak bisa diakses subclass
- **C.** Baris 1 dan Baris 3
- **D.** Semua baris error

---

### Soal 6 — Kasus: Multilevel Inheritance 🏗️

Perhatikan hierarki class:

```java
class MakhlukHidup {
    public void bernapas() {
        System.out.println("Bernapas...");
    }
}

class Hewan extends MakhlukHidup {
    public void bergerak() {
        System.out.println("Bergerak...");
    }
}

class Kucing extends Hewan {
    public void mendengkur() {
        System.out.println("Purrrr...");
    }
}
```

Method apa saja yang bisa dipanggil oleh objek `Kucing`?

- **A.** Hanya `mendengkur()`
- **B.** `mendengkur()` dan `bergerak()`
- **C.** `mendengkur()`, `bergerak()`, dan `bernapas()`
- **D.** Hanya `bernapas()` dan `mendengkur()`

---

### Soal 7 — Kasus: Keyword `super` 🔑

Perhatikan kode berikut:

```java
class Hewan {
    public void info() {
        System.out.print("Hewan ");
    }
}

class Kucing extends Hewan {
    @Override
    public void info() {
        super.info();
        System.out.print("Kucing ");
    }
}

class KucingPersia extends Kucing {
    @Override
    public void info() {
        super.info();
        System.out.print("Persia");
    }
}
```

Apa output dari `new KucingPersia().info()`?

- **A.** `Persia`
- **B.** `Kucing Persia`
- **C.** `Hewan Kucing Persia`
- **D.** `Hewan Persia`

---

### Soal 8 — Kasus: Desain Aplikasi Transportasi 🚗

Kamu sedang mendesain sistem untuk aplikasi transportasi online. Ada class `Kendaraan` dengan method `jalan()` dan `berhenti()`. Lalu ada:

- `Mobil` punya `jumlahPintu`
- `Motor` punya `jenisStang`
- `Truk` punya `kapasitasMuatan`

Seorang developer junior mengusulkan agar `Motor` extends `Mobil` karena "sama-sama kendaraan bermesin". Apa tanggapan yang **tepat**?

- **A.** Setuju, karena Motor dan Mobil memang mirip
- **B.** Tidak tepat — Motor bukan "is-a" Mobil. Keduanya seharusnya extends `Kendaraan` secara langsung (hierarchical inheritance)
- **C.** Setuju, karena multilevel inheritance lebih fleksibel
- **D.** Tidak tepat — seharusnya `Mobil` extends `Motor`

---

### Soal 9 — Kasus: Constructor Chain ⛓️

Perhatikan kode berikut:

```java
class A {
    public A() {
        System.out.print("A ");
    }
}

class B extends A {
    public B() {
        System.out.print("B ");
    }
}

class C extends B {
    public C() {
        System.out.print("C ");
    }
}
```

Apa output dari `new C()`?

- **A.** `C `
- **B.** `C B A `
- **C.** `A B C `
- **D.** `B C `

---

### Soal 10 — Kasus: Batasan Inheritance di Java ⛔

Seorang developer ingin membuat class `SmartTV` yang mewarisi dari **dua class** sekaligus:

```java
class TV { ... }
class Komputer { ... }

class SmartTV extends TV, Komputer {  // ???
    // ...
}
```

Apa yang terjadi dan bagaimana solusinya?

- **A.** Kode berjalan normal — Java mendukung multiple inheritance
- **B.** Compile error — Java **tidak mendukung** multiple inheritance dari class. Solusinya: gunakan **interface** untuk salah satunya
- **C.** Compile error — yang benar menggunakan keyword `implements` untuk keduanya
- **D.** Runtime error — JVM tidak bisa menentukan class parent yang aktif

---

## ✅ Kunci Jawaban

<details>
<summary><b>Klik untuk melihat jawaban</b></summary>

| Soal | Jawaban | Penjelasan Singkat |
|:----:|:-------:|:-------------------|
| 1 | **B** | Hierarchical inheritance — `Karyawan` sebagai superclass menyimpan atribut/method umum, lalu setiap jenis karyawan menambah fitur uniknya sendiri. |
| 2 | **B** | Jika superclass tidak punya constructor default (tanpa parameter), subclass **wajib** memanggil `super(...)` secara eksplisit di baris pertama constructor-nya. |
| 3 | **B** | Meskipun variabel bertipe `Hewan`, objek sebenarnya adalah `Kucing`. Method yang dipanggil mengikuti **tipe objek sebenarnya** (dynamic dispatch / runtime polymorphism). |
| 4 | **C** | Tes "is-a": Mahasiswa **adalah** Orang ✅. Mobil **memiliki** Mesin (has-a), Laptop **memiliki** Baterai (has-a), Perpustakaan **memiliki** Buku (has-a). |
| 5 | **A** | `private` tidak bisa diakses oleh subclass meskipun diwarisi. `protected` (y) dan `public` (z) bisa diakses dengan normal di subclass. |
| 6 | **C** | Multilevel inheritance: `Kucing` mewarisi **semua** method public dari `Hewan` dan `MakhlukHidup`. Jadi bisa memanggil `bernapas()`, `bergerak()`, dan `mendengkur()`. |
| 7 | **C** | `KucingPersia.info()` → panggil `super.info()` (Kucing) → yang juga panggil `super.info()` (Hewan). Hasilnya: `"Hewan "` + `"Kucing "` + `"Persia"` = `"Hewan Kucing Persia"`. |
| 8 | **B** | Motor **bukan** Mobil. Keduanya adalah jenis `Kendaraan`. Memakai hierarchical inheritance (`Mobil extends Kendaraan`, `Motor extends Kendaraan`) adalah desain yang benar. |
| 9 | **C** | Java otomatis memanggil constructor parent (implisit `super()`) sebelum constructor child. Urutan: A → B → C. |
| 10 | **B** | Java tidak mendukung multiple inheritance dari class untuk menghindari *diamond problem*. Solusinya: satu class di-extends, sisanya pakai interface. |

</details>

---

## 🔗 Navigasi

| Sebelumnya | Berikutnya |
|:-----------|:-----------|
| [📝 ← Quiz Encapsulation](01-quiz-encapsulation.md) | [📝 Quiz Polymorphism →](03-quiz-polymorphism.md) |

---

<p align="center">
  <b>Wahyu Amaldi, M.Kom</b> · Universitas Cakrawala
</p>
