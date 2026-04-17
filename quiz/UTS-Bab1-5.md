# 📝 UJIAN TENGAH SEMESTER (UTS)

## Pemrograman Berorientasi Objek — Semester III

| **Dosen** | Wahyu Amaldi, M.Kom |
|---|---|
| **Prodi** | Informatika — Universitas Cakrawala |
| **Cakupan** | Bab 1–5 (Encapsulation, Inheritance, Polymorphism, Abstraction, Design Patterns) |
| **Waktu** | 120 menit |
| **Sifat** | Closed Book, Tanpa Bantuan AI |
| **Bobot** | 30% dari Nilai Akhir |

---

## ⚠️ PETUNJUK UMUM

1. Soal terdiri dari **4 bagian** dengan total **100 poin**.
2. Tulis jawaban dengan **jelas dan lengkap** — jawaban tanpa penjelasan/langkah **TIDAK mendapat nilai penuh**.
3. **Beberapa soal saling terhubung** — jawaban soal sebelumnya mungkin dibutuhkan di soal berikutnya.
4. Semua kode menggunakan **Java 17+**.
5. **Tulis NIM Anda** pada setiap lembar jawaban.

---

## BAGIAN A — ANALISIS KODE BERLAPIS (25 poin)

> Soal ini menguji kemampuan membaca kode yang SALING BERGANTUNG antar class.
> Anda **HARUS** menunjukkan langkah trace per baris.

---

### Soal A1 — Jebakan Field Hiding + Constructor Chain + Dynamic Dispatch (15 poin)

Perhatikan **5 class** berikut yang saling berhubungan:

```java
class Akun {
    String tipe = "BASIC";
    double saldo = 0;

    Akun(double saldo) {
        this.saldo = saldo;
        log("BUAT");
    }

    String getTipe() { return tipe; }

    void log(String aksi) {
        System.out.println("[" + getTipe() + "] " + aksi + " saldo=" + (int)saldo);
    }

    double biayaAdmin() { return 2500; }

    void proses() {
        saldo -= biayaAdmin();
        log("ADMIN");
    }
}

class AkunPremium extends Akun {
    String tipe = "PREMIUM";
    double cashback;

    AkunPremium(double saldo, double cashback) {
        super(saldo);
        this.cashback = cashback;
        saldo += cashback;   // LINE-X
        log("CASHBACK");
    }

    @Override
    String getTipe() { return tipe; }

    @Override
    double biayaAdmin() { return 0; }
}

class AkunBisnis extends AkunPremium {
    String tipe = "BISNIS";

    AkunBisnis(double saldo) {
        super(saldo, saldo * 0.01);
        log("INIT");
    }

    @Override
    String getTipe() { return tipe; }

    @Override
    double biayaAdmin() { return 1000; }
}
```

**Pertanyaan (tulis langkah per baris, tidak boleh langsung jawab output):**

**(a)** [5 poin] Tulis **output lengkap** dari kode berikut. Untuk setiap baris output, jelaskan **class mana** yang method-nya dipanggil dan mengapa.

```java
Akun x = new AkunBisnis(100000);
```

**(b)** [5 poin] Setelah constructor selesai, berapa nilai dari:
- `x.saldo`
- `x.tipe`
- `((AkunPremium)x).tipe`
- `((AkunBisnis)x).tipe`
- `((AkunPremium)x).cashback`

Untuk **setiap** jawaban, jelaskan apakah itu field hiding atau inheritance dan dari class mana nilainya berasal.

**(c)** [5 poin] Perhatikan baris yang ditandai `LINE-X`. Apakah baris tersebut mengubah `this.saldo` (field yang diwarisi dari `Akun`)? Jelaskan mengapa. Lalu, apa output dari:

```java
x.proses();
System.out.println("Final: " + (int)x.saldo);
```

---

### Soal A2 — Method Resolution Puzzle (10 poin)

```java
interface Diskon {
    double hitung(double harga);
}

interface Pajak {
    double hitung(double harga);
}

class Kasir implements Diskon, Pajak {
    String mode;

    Kasir(String mode) { this.mode = mode; }

    @Override
    public double hitung(double harga) {
        if (mode.equals("diskon")) return harga * 0.1;
        if (mode.equals("pajak"))  return harga * 0.11;
        return 0;
    }

    double total(double harga) {
        Diskon d = this;
        Pajak p = this;
        return harga - d.hitung(harga) + p.hitung(harga);
    }
}
```

**(a)** [4 poin] `new Kasir("diskon").total(100000)` mengembalikan berapa? Tunjukkan langkah hitungnya. Apakah hasilnya sesuai logika bisnis "harga - diskon + pajak"? Mengapa?

**(b)** [3 poin] Apa masalah desain fundamental dari class `Kasir`? Mengapa `implements Diskon, Pajak` bermasalah di sini?

**(c)** [3 poin] Tulis ulang desain ini menggunakan **Strategy Pattern** (dari Bab 5) agar diskon dan pajak bisa dihitung secara terpisah. Cukup tulis **interface dan signature class** (tidak perlu implementasi lengkap).

---

## BAGIAN B — KRITIK JAWABAN AI (20 poin)

> Seorang mahasiswa menggunakan AI untuk menjawab soal ujian.
> Tugas Anda: **temukan kesalahan** dalam jawaban AI tersebut.

---

### Soal B1 — AI Salah Trace (10 poin)

Seorang mahasiswa diminta men-trace output kode berikut:

```java
class Hewan {
    String nama;

    Hewan(String nama) {
        this.nama = nama;
        System.out.println("Hewan:" + nama);
        suara();
    }

    void suara() {
        System.out.println(nama + " -> ...");
    }
}

class Kucing extends Hewan {
    String warna;

    Kucing(String nama, String warna) {
        super(nama);
        this.warna = warna;
        System.out.println("Kucing:" + warna);
    }

    @Override
    void suara() {
        System.out.println(nama + " -> Meow! [" + warna + "]");
    }
}
```

Eksekusi: `Hewan h = new Kucing("Milo", "Oranye");`

**Jawaban AI:**
```
Hewan:Milo
Milo -> Meow! [Oranye]
Kucing:Oranye
```

**Pertanyaan:**

**(a)** [5 poin] Apakah jawaban AI di atas **benar atau salah**? Jika salah, tulis output yang benar dan jelaskan **kesalahan spesifik** AI-nya.

**(b)** [5 poin] Jelaskan **mekanisme Java** yang menyebabkan perilaku ini. Mengapa memanggil overridable method dari constructor dianggap **dangerous practice**? Berikan contoh konkret kapan ini menyebabkan bug.

---

### Soal B2 — AI Salah Desain (10 poin)

Seorang mahasiswa bertanya ke AI: *"Bagaimana menerapkan diskon flash sale yang hanya berlaku 1 jam di sistem Kasir dari praktikum Bab 5?"*

**Jawaban AI:**

```java
class DiskonFlashSale implements StrategiDiskon {
    private long waktuMulai;
    private long waktuSelesai;

    public DiskonFlashSale() {
        this.waktuMulai = System.currentTimeMillis();
        this.waktuSelesai = waktuMulai + 3600000;
    }

    @Override
    public double hitungDiskon(double harga) {
        if (System.currentTimeMillis() <= waktuSelesai) {
            return harga * 0.50; // diskon 50%
        }
        return 0;
    }

    @Override
    public String getNamaDiskon() { return "Flash Sale 50%"; }
}

// Penggunaan:
Kasir kasir = new Kasir();
kasir.setStrategi(new DiskonFlashSale());
kasir.prosesPembayaran("Laptop", 10000000);
// ... 1 jam kemudian ...
kasir.prosesPembayaran("Mouse", 500000);
```

**Pertanyaan:**

**(a)** [4 poin] Identifikasi **minimal 3 masalah** dari desain AI di atas (bisa masalah teknis, OOP, atau bisnis logic).

**(b)** [3 poin] Jika `DiskonFlashSale` dibuat jam 10:00 dan `prosesPembayaran("Mouse")` dipanggil jam 11:01, apa yang terjadi pada struk? Method `prosesPembayaran` dari Bab 5 mencetak "Potongan: Rp ..." — berapa yang tercetak? Apakah ini UX yang baik? Mengapa?

**(c)** [3 poin] Tulis **perbaikan desain** yang memisahkan validasi waktu dari kalkulasi diskon. Gunakan prinsip **Single Responsibility** dan pattern dari Bab 5.

---

## BAGIAN C — KODE BERMASALAH YANG SALING TERHUBUNG (25 poin)

> Anda menerima kode dari developer junior. Kode **bisa compile** tapi hasilnya salah.
> Soal C1 dan C2 menggunakan **sistem yang sama** — jawaban C1 dibutuhkan di C2.

---

### Soal C1 — Temukan 5 Bug Tersembunyi (13 poin)

Sistem manajemen karyawan berikut memiliki **minimal 5 bug** yang tersebar di berbagai class. Beberapa bug saling berinteraksi.

```java
// === File: Karyawan.java ===
abstract class Karyawan {
    private String nama;
    private double gajiPokok;
    protected int masaKerja;

    Karyawan(String nama, double gajiPokok, int masaKerja) {
        this.nama = nama;
        this.gajiPokok = gajiPokok;
        this.masaKerja = masaKerja;
    }

    String getNama() { return nama; }
    double getGajiPokok() { return gajiPokok; }

    abstract double hitungBonus();

    double totalGaji() {
        return gajiPokok + hitungBonus();
    }

    @Override
    public String toString() {
        return nama + " | Gaji: " + (int)totalGaji();
    }
}

// === File: KaryawanTetap.java ===
class KaryawanTetap extends Karyawan {
    KaryawanTetap(String nama, double gajiPokok, int masaKerja) {
        super(nama, gajiPokok, masaKerja);
    }

    @Override
    double hitungBonus() {
        // Bonus 5% per tahun masa kerja, maksimal 25%
        double persen = masaKerja * 5;
        if (persen > 25) persen = 25;
        return getGajiPokok() * persen;
    }
}

// === File: KaryawanKontrak.java ===
class KaryawanKontrak extends Karyawan {
    private int sisaBulan;

    KaryawanKontrak(String nama, double gajiPokok, int sisaBulan) {
        super(nama, gajiPokok, 0);
        this.sisaBulan = sisaBulan;
    }

    @Override
    double hitungBonus() {
        // Bonus flat 500rb jika sisa kontrak > 6 bulan
        return sisaBulan > 6 ? 500000 : 0;
    }

    int getSisaBulan() { return sisaBulan; }
}

// === File: Payroll.java ===
class Payroll {
    private List<Karyawan> daftar = new ArrayList<>();

    void tambah(Karyawan k) { daftar.add(k); }

    void cetakSlipGaji() {
        double totalPengeluaran = 0;
        for (Karyawan k : daftar) {
            System.out.println(k);
            totalPengeluaran += k.totalGaji();
        }
        System.out.println("TOTAL: " + (int)totalPengeluaran);
    }
}
```

Kode klien:
```java
Payroll p = new Payroll();
p.tambah(new KaryawanTetap("Andi", 8000000, 3));
p.tambah(new KaryawanTetap("Budi", 10000000, 6));
p.tambah(new KaryawanKontrak("Cici", 6000000, 8));

p.cetakSlipGaji();
```

**Tugas (untuk setiap bug, tunjukkan BARIS mana yang bermasalah):**

**(a)** [8 poin] Identifikasi **5 bug** (ada yang halus!). Untuk setiap bug:
- Tunjukkan **baris kode** yang bermasalah
- Jelaskan **apa yang salah** dan **dampaknya pada output**
- Hitung **nilai yang salah vs yang seharusnya** (untuk bug kalkulasi)

**(b)** [5 poin] Hitung output **aktual** (dengan semua bug) dari `cetakSlipGaji()`. Lalu hitung output yang **seharusnya** jika semua bug diperbaiki.

---

### Soal C2 — Refactor dengan Pattern (12 poin)

Lanjutan dari Soal C1. Setelah memperbaiki bug, manajer HR meminta fitur baru:

> *"Kami ingin bisa mengganti skema bonus sewaktu-waktu. Misalnya saat Lebaran, semua karyawan dapat bonus THR 1x gaji pokok. Saat performance review, bonus berdasarkan rating (A=20%, B=10%, C=5%). Dan skema normal seperti sekarang."*

**Pertanyaan:**

**(a)** [4 poin] Mengapa desain saat ini (abstract method `hitungBonus()` yang di-hardcode di setiap subclass) **tidak cocok** untuk kebutuhan di atas? Jelaskan prinsip OOP mana yang dilanggar.

**(b)** [8 poin] Refactor menggunakan **Strategy Pattern**. Tulis kode Java lengkap yang meliputi:
- Interface `StrategiBonus`
- Minimal 3 implementasi: `BonusNormal`, `BonusTHR`, `BonusPerformance`
- Class `Karyawan` yang sudah dimodifikasi untuk menerima strategy
- Contoh penggunaan yang menunjukkan **pergantian strategi di runtime**

Kode harus **bisa compile** dan menunjukkan bagaimana Andi yang sebelumnya pakai bonus normal, diganti ke THR saat Lebaran.

---

## BAGIAN D — SKENARIO BISNIS PERSONAL (30 poin)

> Soal ini **berbeda untuk setiap mahasiswa** berdasarkan **2 digit terakhir NIM**.
> AI tidak bisa menjawab karena tidak tahu NIM Anda.

---

### Soal D1 — Desain Sistem dari Nol (15 poin)

**Tentukan skenario Anda berdasarkan 2 digit terakhir NIM:**

| Akhiran NIM | Skenario |
|---|---|
| 00–19 | Sistem pemesanan tiket bioskop (jenis kursi: Regular/VIP/Couple, diskon member, notifikasi jika film sold out) |
| 20–39 | Sistem laundry kiloan (jenis cucian: Reguler/Express/SuperExpress, diskon pelanggan setia, notifikasi saat cucian selesai) |
| 40–59 | Sistem rental mobil (jenis mobil: City Car/SUV/Van, diskon weekend, notifikasi saat mobil dikembalikan) |
| 60–79 | Sistem apotek (jenis obat: Bebas/KerasTerbatas/Resep, diskon BPJS, notifikasi saat stok menipis) |
| 80–99 | Sistem parkir mall (jenis kendaraan: Motor/Mobil/Truk, tarif progresif per jam, notifikasi saat area penuh) |

**Yang HARUS dijawab (sesuai skenario NIM Anda):**

**(a)** [3 poin] Tulis NIM Anda dan skenario yang Anda dapatkan.

**(b)** [4 poin] Identifikasi **pilar OOP** mana yang akan digunakan di setiap bagian sistem. Tulis dalam format tabel:

| Bagian Sistem | Pilar OOP | Alasan |
|---|---|---|
| Contoh: Harga tiket private | Encapsulation | Mencegah manipulasi harga langsung |
| ... | ... | ... |

**(c)** [4 poin] Identifikasi **design pattern** yang digunakan dan jelaskan **mengapa** pattern tersebut cocok (bukan pattern lain). Minimal 2 pattern berbeda.

**(d)** [4 poin] Tulis **implementasi Java** untuk 3 class utama yang saling terhubung. Kode harus mendemonstrasikan minimal 3 pilar OOP dan 1 design pattern.

---

### Soal D2 — Hubungkan ke Bab Praktikum (15 poin)

> Soal ini meminta Anda **menghubungkan** kode praktikum yang sudah dipelajari dengan skenario baru.

Anda diminta **memodifikasi** kode dari **2 praktikum berbeda** (pilih dari Bab 1–5) dan menggabungkannya menjadi satu sistem yang utuh.

**Contoh:** Mengambil class `BankAccount` dari Bab 1 + `StrategiDiskon` dari Bab 5 → digabung menjadi sistem pembayaran e-commerce.

**Yang HARUS dijawab:**

**(a)** [3 poin] Sebutkan **2 praktikum yang Anda pilih** (Bab berapa) dan class apa yang akan diambil. Jelaskan mengapa kedua praktikum tersebut bisa digabung.

**(b)** [5 poin] Tulis **class diagram** (format teks) yang menunjukkan:
- Class mana yang diambil dari praktikum masing-masing
- Class baru apa yang Anda tambahkan sebagai "jembatan"
- Relasi antar class (extends, implements, has-a, uses)

**(c)** [4 poin] Tulis **kode Java** dari class "jembatan" yang menghubungkan kedua praktikum. Kode harus menunjukkan:
- Minimal 1 pilar OOP berbeda dari yang sudah ada di praktikum asli
- Minimal 1 method yang memanggil method dari kedua praktikum

**(d)** [3 poin] Tulis output yang dihasilkan saat class jembatan Anda dijalankan. Jelaskan **alur eksekusi** baris per baris.

---

## 📊 RUBRIK PENILAIAN

| Bagian | Bobot | Kriteria Nilai Penuh | Penalti Tanpa Langkah |
|---|---|---|---|
| **A. Analisis Kode Berlapis** | 25% | Trace per baris benar + penjelasan field hiding/dispatch | -50% jika langsung jawab output |
| **B. Kritik Jawaban AI** | 20% | Kesalahan teridentifikasi + penjelasan mekanisme Java | -30% jika tidak jelaskan "mengapa" |
| **C. Bug + Refactor** | 25% | Semua bug + nilai aktual vs seharusnya + refactor compile | -40% jika tidak ada perhitungan |
| **D. Skenario Personal** | 30% | NIM sesuai + desain tepat + kode terhubung + pilar teridentifikasi | 0 jika NIM tidak ditulis |

### Penilaian Khusus Anti-AI:
- Jawaban yang **hanya berisi kode tanpa penjelasan langkah** → nilai maksimal 40%.
- Jawaban yang **tidak menunjukkan trace per baris** pada soal tracing → nilai maksimal 30%.
- Jawaban Bagian D yang **tidak menyertakan NIM** → nilai 0.
- Jawaban yang **menggunakan terminologi/pola yang tidak diajarkan** di Bab 1–5 → akan diperiksa lebih lanjut.
- Plagiarisme antar mahasiswa pada Bagian D (skenario identik dengan NIM berbeda range) → nilai 0 untuk kedua pihak.

---

## 🔑 KUNCI JAWABAN — HANYA UNTUK DOSEN

<details>
<summary>⚠️ Klik untuk membuka kunci jawaban</summary>

### Bagian A1

**Constructor chain `new AkunBisnis(100000)`:**

1. `AkunBisnis(100000)` → panggil `super(100000, 100000*0.01)` = `super(100000, 1000)`
2. `AkunPremium(100000, 1000)` → panggil `super(100000)` 
3. `Akun(100000)` → `this.saldo = 100000`, panggil `log("BUAT")`
4. `log("BUAT")` → panggil `getTipe()` — **dynamic dispatch ke AkunBisnis.getTipe()** → return `tipe` dari `AkunBisnis`
5. **TAPI** field `tipe` di AkunBisnis belum diinisialisasi! (constructor belum sampai sana) → `tipe = null`
6. Output: `[null] BUAT saldo=100000`
7. Kembali ke AkunPremium constructor: `this.cashback = 1000`
8. `saldo += cashback` → **JEBAKAN: `saldo` di sini adalah PARAMETER lokal**, bukan `this.saldo`! Parameter `saldo` = 100000, jadi `saldo` (lokal) menjadi 101000, tapi `this.saldo` tetap 100000.
9. `log("CASHBACK")` → `getTipe()` → AkunBisnis.getTipe() → `tipe` masih null (AkunBisnis constructor belum jalan) → Output: `[null] CASHBACK saldo=100000`
10. Kembali ke AkunBisnis constructor: field `tipe = "BISNIS"` sudah diinisialisasi
11. `log("INIT")` → `getTipe()` → AkunBisnis.getTipe() → `tipe = "BISNIS"` → Output: `[BISNIS] INIT saldo=100000`

**Output lengkap:**
```
[null] BUAT saldo=100000
[null] CASHBACK saldo=100000
[BISNIS] INIT saldo=100000
```

**A1(b) — Nilai field setelah constructor:**
- `x.saldo` = **100000** (tipe referensi Akun, field dari Akun, LINE-X tidak mengubah this.saldo)
- `x.tipe` = **"BASIC"** (tipe referensi Akun → field hiding → ambil Akun.tipe)
- `((AkunPremium)x).tipe` = **"PREMIUM"** (cast ke AkunPremium → ambil AkunPremium.tipe)
- `((AkunBisnis)x).tipe` = **"BISNIS"** (cast ke AkunBisnis → ambil AkunBisnis.tipe)
- `((AkunPremium)x).cashback` = **1000.0**

**A1(c) — LINE-X:**
`saldo += cashback` di constructor AkunPremium: parameter `saldo` **shadows** the field `this.saldo`. Ini mengubah **parameter lokal**, bukan field. `this.saldo` tetap 100000.

```
x.proses():
→ biayaAdmin() → dynamic dispatch ke AkunBisnis → return 1000
→ saldo = 100000 - 1000 = 99000
→ log("ADMIN") → getTipe() → "BISNIS"
Output: [BISNIS] ADMIN saldo=99000
Final: 99000
```

---

### Bagian A2

**(a)** `new Kasir("diskon").total(100000)`:
- `d.hitung(100000)` → mode="diskon" → `100000 * 0.1 = 10000`
- `p.hitung(100000)` → **SAMA objeknya**, mode masih "diskon" → `100000 * 0.1 = 10000`
- `return 100000 - 10000 + 10000 = 100000`

Hasilnya 100000 (= harga asli). Diskon dan pajak saling cancel karena keduanya memanggil **method yang sama** pada **objek yang sama**. Tidak sesuai logika bisnis.

**(b)** Masalah: `Diskon` dan `Pajak` punya method signature identik (`double hitung(double)`). Java hanya bisa punya **1 implementasi** — tidak bisa membedakan kapan dipanggil sebagai Diskon vs Pajak. Ini **diamond problem** di level interface.

**(c)** Refactor:
```java
interface StrategiHitung {
    double hitung(double harga);
}

class HitungDiskon implements StrategiHitung { ... }
class HitungPajak implements StrategiHitung { ... }

class Kasir {
    private StrategiHitung diskon;
    private StrategiHitung pajak;
    // bisa set terpisah
}
```

---

### Bagian B1

**(a)** Jawaban AI **SALAH**. Output yang benar:
```
Hewan:Milo
Milo -> Meow! [null]
Kucing:Oranye
```

Kesalahan AI: `warna` bernilai `null` saat `suara()` dipanggil di constructor `Hewan`, karena field `warna` di `Kucing` **belum diinisialisasi** (constructor Kucing belum sampai ke `this.warna = warna`). AI mengasumsikan `warna` sudah "Oranye".

**(b)** Mekanisme: Saat `super(nama)` dipanggil di constructor Kucing, flow masuk ke constructor Hewan. Di sana `suara()` dipanggil → dynamic dispatch ke `Kucing.suara()` (karena actual object = Kucing). Tapi `Kucing.suara()` mengakses `warna` yang belum diassign. Ini berbahaya karena method override bisa mengakses field yang belum terinisialisasi, menyebabkan NullPointerException atau data salah.

---

### Bagian B2

**(a)** Minimal 3 masalah:
1. `System.currentTimeMillis()` dipanggil setiap `hitungDiskon()` — **non-deterministic**, tidak bisa di-test. Waktu seharusnya di-inject.
2. Tidak ada feedback ke pelanggan ketika flash sale expired — struk cetak "Potongan: Rp 0" tanpa penjelasan kenapa diskon hilang.
3. Violasi **Single Responsibility** — class bertanggung jawab atas kalkulasi diskon DAN validasi waktu.
4. (Bonus) Hardcode 50% — seharusnya parameterisasi.

**(b)** Setelah jam 11:01: `hitungDiskon(500000)` return 0. Struk cetak "Potongan: Rp 0" dan "Total: Rp 500000". Tapi "Diskon: Flash Sale 50%" masih tercetak oleh `getNamaDiskon()` — misleading untuk pelanggan.

**(c)** Pisahkan:
```java
class ValidatorWaktu {
    boolean masihAktif(long mulai, long durasi) { ... }
}

class DiskonFlashSale implements StrategiDiskon {
    private StrategiDiskon diskonAsli;
    private ValidatorWaktu validator;
    // Decorator: jika aktif → delegate ke diskonAsli, jika tidak → return 0 + nama berubah
}
```

---

### Bagian C1

**5 Bug:**

1. **Bug persen (KRITIS):** `KaryawanTetap.hitungBonus()` → `getGajiPokok() * persen`. Untuk Andi: `8000000 * 15 = 120.000.000` (harusnya `8000000 * 0.15 = 1.200.000`). **Lupa membagi 100.**

2. **Bug batas persen:** Cap di 25, bukan 0.25. Untuk Budi: `masaKerja=6 → persen=30 → cap 25 → 10000000 * 25 = 250.000.000`. Harusnya `0.25 → 2.500.000`.

3. **Bug akses modifier:** `hitungBonus()` dan `totalGaji()` package-private, bukan `public`. Bisa bermasalah jika class di package berbeda. (Lebih halus, tapi melanggar prinsip encapsulation.)

4. **Bug validasi:** Tidak ada validasi negatif untuk gajiPokok, masaKerja, atau sisaBulan. Bisa buat `new KaryawanTetap("X", -5000000, -2)`.

5. **Bug logika kontrak:** `sisaBulan > 6` → strictly greater than. Karyawan dengan sisa tepat 6 bulan tidak dapat bonus. Requirement ambigu tapi kemungkinan harusnya `>= 6`.

**Output aktual (dengan bug):**
```
Andi | Gaji: 128000000        (8jt + 8jt*15 = 8jt + 120jt)
Budi | Gaji: 260000000        (10jt + 10jt*25 = 10jt + 250jt)
Cici | Gaji: 6500000          (6jt + 500rb — ini benar)
TOTAL: 394500000
```

**Output seharusnya (bug fixed):**
```
Andi | Gaji: 9200000          (8jt + 8jt*0.15 = 8jt + 1.2jt)
Budi | Gaji: 12500000         (10jt + 10jt*0.25 = 10jt + 2.5jt)
Cici | Gaji: 6500000          (6jt + 500rb)
TOTAL: 28200000
```

---

### Bagian C2

**(a)** `hitungBonus()` di-hardcode di masing-masing subclass → melanggar **Open/Closed Principle**. Jika ingin ganti skema bonus saat Lebaran, harus modifikasi setiap subclass. Juga tidak bisa ganti strategy di runtime (terikat ke tipe class).

**(b)** Contoh refactor:
```java
interface StrategiBonus {
    double hitungBonus(Karyawan k);
}

class BonusNormal implements StrategiBonus {
    public double hitungBonus(Karyawan k) {
        double persen = Math.min(k.getMasaKerja() * 0.05, 0.25);
        return k.getGajiPokok() * persen;
    }
}

class BonusTHR implements StrategiBonus {
    public double hitungBonus(Karyawan k) {
        return k.getGajiPokok(); // 1x gaji pokok
    }
}

class BonusPerformance implements StrategiBonus {
    private String rating;
    public BonusPerformance(String rating) { this.rating = rating; }
    public double hitungBonus(Karyawan k) {
        return switch(rating) {
            case "A" -> k.getGajiPokok() * 0.20;
            case "B" -> k.getGajiPokok() * 0.10;
            default  -> k.getGajiPokok() * 0.05;
        };
    }
}

class Karyawan {
    private String nama;
    private double gajiPokok;
    private StrategiBonus strategi;
    
    void setStrategi(StrategiBonus s) { this.strategi = s; }
    double totalGaji() { return gajiPokok + strategi.hitungBonus(this); }
}

// Runtime swap:
Karyawan andi = new Karyawan("Andi", 8000000);
andi.setStrategi(new BonusNormal());
System.out.println(andi); // Gaji normal

andi.setStrategi(new BonusTHR()); // Lebaran!
System.out.println(andi); // Gaji + THR
```

---

### Bagian D

**D1:** Penilaian per skenario — verifikasi NIM cocok dengan range. Jika NIM berakhiran 45 tapi jawab soal bioskop (range 00–19) → **nilai 0**.

**D2:** Verifikasi class yang diambil benar-benar ada di praktikum:
- Bab 1: BankAccount, verifikasiPin, formatRupiah
- Bab 2: Hewan, Kucing, Anjing, KucingPersia
- Bab 3: BangunDatar, Persegi, Lingkaran, Kalkulator
- Bab 4: Kendaraan, Mobil, MobilListrik, interface PengisiDaya/Klakson
- Bab 5: StrategiDiskon, Kasir, PengamatStok, Produk, DokumenFactory

Jika menyebut class yang tidak ada di praktikum → periksa kemungkinan AI-generated.

</details>

---

*Selamat mengerjakan. Tunjukkan pemahaman, bukan hafalan.* 🎯
