# 📝 Latihan Soal — Pilar 1: Encapsulation

> **Penulis:** Wahyu Amaldi, M.Kom · **Institusi:** Universitas Cakrawala

[⬅️ Kembali ke Beranda](../README.md) · [📖 Materi Encapsulation](../docs/01-encapsulation.md)

---

## Petunjuk

- Pilih **satu jawaban** yang paling tepat untuk setiap soal.
- Setiap soal berbasis **skenario** — baca kasus dengan cermat.
- Kunci jawaban tersedia di [bagian bawah](#-kunci-jawaban).

---

### Soal 1 — Kasus: Sistem E-Wallet 💰

Kamu diminta membangun class `EWallet` untuk aplikasi pembayaran digital. Saldo pengguna **tidak boleh** diubah secara langsung dari luar class, dan setiap perubahan saldo harus melalui validasi.

```java
class EWallet {
    _______ double saldo;

    public void topUp(double jumlah) {
        if (jumlah > 0) {
            saldo += jumlah;
        }
    }
}
```

Access modifier apa yang paling tepat untuk mengisi bagian `_______`?

- **A.** `public`
- **B.** `protected`
- **C.** `private`
- **D.** *(default/tanpa modifier)*

---

### Soal 2 — Kasus: Bug di Aplikasi Klinik 🏥

Seorang developer junior menulis kode berikut untuk menyimpan data pasien:

```java
public class Pasien {
    public String nama;
    public String penyakit;
    public int umur;
}
```

Suatu hari, ada bug: seorang pasien tercatat berumur **-5 tahun**, dan field `penyakit` terisi string kosong `""`. Apa **akar masalah** dari bug ini?

- **A.** Java tidak mendukung validasi tipe data `int`
- **B.** Semua field dideklarasikan `public`, sehingga tidak ada validasi saat data diubah
- **C.** Constructor class `Pasien` salah
- **D.** Bug terjadi karena Java mengizinkan nilai negatif pada tipe `int`

---

### Soal 3 — Kasus: Mesin ATM 🏧

Perhatikan class berikut:

```java
public class ATM {
    private double saldo;
    private String pin;

    public double getSaldo(String inputPin) {
        if (verifikasiPin(inputPin)) {
            return saldo;
        }
        return -1;
    }

    private boolean verifikasiPin(String inputPin) {
        return this.pin.equals(inputPin);
    }
}
```

Dari luar class `ATM`, manakah yang **BISA diakses**?

- **A.** `atm.saldo` dan `atm.pin`
- **B.** `atm.getSaldo("1234")` dan `atm.verifikasiPin("1234")`
- **C.** Hanya `atm.getSaldo("1234")`
- **D.** Semua bisa diakses karena objek sudah di-instantiate

---

### Soal 4 — Kasus: Sistem Perpustakaan 📚

Kamu membuat class `Buku` untuk sistem perpustakaan kampus:

```java
public class Buku {
    private String judul;
    private int stok;

    public void setStok(int stok) {
        this.stok = stok;  // ← Perhatikan baris ini
    }
}
```

Rekan timmu berhasil mengubah stok menjadi **-100** lewat `buku.setStok(-100)`. Apa yang **seharusnya diperbaiki**?

- **A.** Ganti `private int stok` menjadi `public int stok`
- **B.** Hapus method `setStok()` agar stok tidak bisa diubah
- **C.** Tambahkan validasi di dalam `setStok()` agar menolak nilai negatif
- **D.** Ubah tipe data `stok` dari `int` menjadi `String`

---

### Soal 5 — Kasus: Keamanan Data Mahasiswa 🎓

Perhatikan class berikut:

```java
public class Mahasiswa {
    private String nim;
    private double ipk;

    public String getNim() {
        return nim;
    }

    // Tidak ada setNim()

    public double getIpk() {
        return ipk;
    }

    public void setIpk(double ipk) {
        if (ipk >= 0.0 && ipk <= 4.0) {
            this.ipk = ipk;
        }
    }
}
```

Mengapa field `nim` hanya punya **getter** tanpa **setter**?

- **A.** Programmer lupa membuat setter-nya
- **B.** NIM bersifat **read-only** — tidak boleh diubah setelah ditetapkan
- **C.** Java tidak mengizinkan setter untuk tipe String
- **D.** Getter dan setter harus selalu dibuat berpasangan

---

### Soal 6 — Kasus: Restoran Online 🍕

Kamu membuat class `Pesanan` untuk aplikasi delivery makanan:

```java
public class Pesanan {
    private String idPesanan;
    private double totalHarga;
    private boolean sudahBayar;

    public String getIdPesanan() {
        return "ORD-" + idPesanan.substring(idPesanan.length() - 4);
    }
}
```

Jika `idPesanan = "TRX20260305001"`, apa output dari `getIdPesanan()`?

- **A.** `TRX20260305001`
- **B.** `ORD-TRX20260305001`
- **C.** `ORD-5001`
- **D.** `ORD-0305001`

---

### Soal 7 — Kasus: Debat Tim Developer 🤔

Dua developer junior sedang berdebat tentang encapsulation:

- **Andi:** _"Encapsulation itu intinya membuat semua field jadi `private` lalu bikin getter dan setter untuk semuanya."_
- **Budi:** _"Encapsulation itu tentang melindungi data dengan mengontrol akses — getter/setter hanya dibuat jika memang diperlukan, dan setter harus punya validasi."_

Siapa yang **lebih tepat**?

- **A.** Andi, karena setiap field `private` wajib punya getter dan setter
- **B.** Budi, karena inti encapsulation adalah kontrol akses, bukan sekadar membuat getter/setter untuk semua field
- **C.** Keduanya salah, encapsulation hanya tentang menggunakan keyword `private`
- **D.** Keduanya benar, tidak ada perbedaan

---

### Soal 8 — Kasus: Game RPG ⚔️

Perhatikan kode untuk karakter game RPG berikut:

```java
public class Karakter {
    private int hp;
    private int maxHp;

    public Karakter(int maxHp) {
        this.maxHp = maxHp;
        this.hp = maxHp;
    }

    public void serang(Karakter target) {
        target.terimaSerangan(20);
    }

    private void terimaSerangan(int damage) {
        this.hp = Math.max(0, this.hp - damage);
    }
}
```

Apa yang terjadi saat `hero.serang(monster)` dipanggil?

- **A.** Berhasil — HP monster berkurang 20
- **B.** Compile error — `terimaSerangan()` adalah `private`, tidak bisa dipanggil dari objek lain
- **C.** Runtime error — `NullPointerException`
- **D.** HP hero yang berkurang, bukan monster

---

### Soal 9 — Kasus: Tabel Access Modifier 📊

Perhatikan potongan kode ini:

```java
package universitas;

public class Dosen {
    public String nama;
    protected String nip;
    String email;          // default
    private double gaji;
}
```

```java
package universitas;

public class Staff {
    void cekDosen() {
        Dosen d = new Dosen();
        // Akses field di sini
    }
}
```

Field mana saja yang bisa diakses oleh class `Staff` (satu package, **bukan** subclass)?

- **A.** `nama`, `nip`, `email`, dan `gaji` — semuanya
- **B.** `nama`, `nip`, dan `email`
- **C.** Hanya `nama`
- **D.** Hanya `nama` dan `nip`

---

### Soal 10 — Kasus: Code Review 🔍

Kamu sedang melakukan code review dan menemukan kode berikut:

```java
public class AkunBank {
    private double saldo;

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }
}
```

Apa **rekomendasi perbaikan** terbaik yang akan kamu berikan?

- **A.** Kode sudah sempurna, tidak perlu diubah
- **B.** Hapus `setSaldo()` dan ganti dengan method `setor()` dan `tarik()` yang memiliki validasi, karena saldo seharusnya tidak bisa di-set langsung
- **C.** Ubah `saldo` menjadi `public` agar lebih mudah diakses
- **D.** Tambahkan field `public` lain sebagai alternatif akses

---

## ✅ Kunci Jawaban

<details>
<summary><b>Klik untuk melihat jawaban</b></summary>

| Soal | Jawaban | Penjelasan Singkat |
|:----:|:-------:|:-------------------|
| 1 | **C** | `private` memastikan saldo hanya bisa diubah melalui method `topUp()` yang sudah divalidasi, bukan diakses langsung dari luar. |
| 2 | **B** | Field `public` memungkinkan siapapun mengisi nilai tanpa validasi. Dengan `private` + setter bervalidasi, umur negatif dan penyakit kosong bisa dicegah. |
| 3 | **C** | `saldo` dan `pin` bersifat `private` (tidak bisa diakses langsung), `verifikasiPin()` juga `private`. Hanya `getSaldo()` yang `public`. |
| 4 | **C** | Setter yang baik harus **memvalidasi** input. Contoh: `if (stok >= 0) { this.stok = stok; }` |
| 5 | **B** | NIM adalah identitas unik mahasiswa yang tidak boleh diubah setelah ditetapkan. Tidak menyediakan setter adalah **desain yang disengaja**. |
| 6 | **C** | `idPesanan.length() - 4` mengambil 4 karakter terakhir: `"5001"`. Ditambah prefix `"ORD-"` menjadi `"ORD-5001"`. |
| 7 | **B** | Encapsulation bukan sekadar memasang getter/setter di semua field. Intinya adalah **kontrol akses** — setter hanya dibuat jika perlu dan harus memiliki validasi. |
| 8 | **A** | Method `private` **bisa** dipanggil dari **dalam class yang sama**, meskipun pada objek yang berbeda (`target`). Keduanya adalah instance dari class `Karakter`. |
| 9 | **B** | `Staff` berada di package yang sama (`universitas`). `public` ✅, `protected` ✅ (satu package), `default` ✅ (satu package), `private` ❌. Jadi `nama`, `nip`, `email` bisa diakses. |
| 10 | **B** | `setSaldo()` yang menerima nilai bebas membuat saldo bisa di-set sembarangan (termasuk negatif). Lebih baik diganti method bisnis (`setor()`, `tarik()`) dengan validasi. |

</details>

---

## 🔗 Navigasi

| Sebelumnya | Berikutnya |
|:-----------|:-----------|
| [📖 Materi Encapsulation](../docs/01-encapsulation.md) | [📝 Quiz Inheritance →](02-quiz-inheritance.md) |

---

<p align="center">
  <b>Wahyu Amaldi, M.Kom</b> · Universitas Cakrawala
</p>
