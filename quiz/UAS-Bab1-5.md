# 📝 UJIAN AKHIR SEMESTER (UAS) — PROJECT

## Pemrograman Berorientasi Objek — Semester III

| **Dosen** | Wahyu Amaldi, M.Kom |
|---|---|
| **Prodi** | Informatika — Universitas Cakrawala |
| **Cakupan** | Bab 1–5 (Encapsulation, Inheritance, Polymorphism, Abstraction, Design Patterns) |
| **Waktu** | 150 menit |
| **Sifat** | Closed Book, Tanpa Bantuan AI |
| **Bobot** | 40% dari Nilai Akhir |

---

## ⚠️ PETUNJUK UMUM

1. Soal terdiri dari **4 soal** dengan total **100 poin**.
2. Tulis jawaban dengan **jelas dan lengkap** — jawaban tanpa penjelasan/langkah **TIDAK mendapat nilai penuh**.
3. **Soal bersifat KUMULATIF** — konsep dari bab awal dibutuhkan untuk menjawab soal bab akhir.
4. Semua kode menggunakan **Java 17+**.
5. **Tulis NIM Anda** pada setiap lembar jawaban.
6. Berbeda dengan UTS, UAS ini menuntut Anda **menggabungkan** seluruh pilar OOP + design pattern menjadi **satu sistem terintegrasi**.

---

## SOAL 1 — ANALISIS ARSITEKTUR KODE (25 poin)

> Soal ini menguji kemampuan membaca kode yang melibatkan SELURUH pilar OOP dan design pattern secara bersamaan. Anda **HARUS** menunjukkan langkah trace per baris.

Perhatikan sistem pembayaran rumah sakit berikut:

```java
// === Abstraction: Interface Strategy ===
interface StrategiBiaya {
    double hitung(double biayaDasar);
    String getKategori();
}

class BiayaUmum implements StrategiBiaya {
    public double hitung(double biayaDasar) { return biayaDasar; }
    public String getKategori() { return "UMUM"; }
}

class BiayaBPJS implements StrategiBiaya {
    private double plafonHarian;

    public BiayaBPJS(double plafonHarian) {
        this.plafonHarian = plafonHarian;
    }

    public double hitung(double biayaDasar) {
        double ditanggung = Math.min(biayaDasar, plafonHarian);
        return biayaDasar - ditanggung;
    }

    public String getKategori() { return "BPJS"; }
}

// === Inheritance + Encapsulation ===
abstract class Pasien {
    private String nama;
    private double totalBiaya;
    protected StrategiBiaya strategi;

    Pasien(String nama, StrategiBiaya strategi) {
        this.nama = nama;
        this.strategi = strategi;
        this.totalBiaya = 0;
        log("DAFTAR");
    }

    String getNama() { return nama; }
    double getTotalBiaya() { return totalBiaya; }

    abstract double biayaPerHari();
    abstract String getKelas();

    void rawat(int hari) {
        double biaya = biayaPerHari() * hari;
        double bayar = strategi.hitung(biaya);
        totalBiaya += bayar;
        log("RAWAT-" + hari + "hr");
    }

    void log(String aksi) {
        System.out.println("[" + getKelas() + "/" + strategi.getKategori() + "] "
            + nama + " " + aksi + " total=" + (int)totalBiaya);
    }
}

class PasienVIP extends Pasien {
    private double biayaEkstra;

    PasienVIP(String nama, StrategiBiaya strategi, double biayaEkstra) {
        super(nama, strategi);
        this.biayaEkstra = biayaEkstra;
        log("VIP-READY");
    }

    @Override
    double biayaPerHari() { return 500000 + biayaEkstra; }

    @Override
    String getKelas() { return "VIP"; }
}

class PasienRegular extends Pasien {
    PasienRegular(String nama, StrategiBiaya strategi) {
        super(nama, strategi);
    }

    @Override
    double biayaPerHari() { return 200000; }

    @Override
    String getKelas() { return "REG"; }
}

// === Interface Collision ===
interface Notifikasi {
    void kirim(String pesan);
}

interface LogAktivitas {
    void kirim(String pesan);
}

class SistemRS implements Notifikasi, LogAktivitas {
    private String mode;
    private List<String> riwayat = new ArrayList<>();

    SistemRS(String mode) { this.mode = mode; }

    @Override
    public void kirim(String pesan) {
        String prefix = mode.equals("notif") ? "[NOTIF]" : "[LOG]";
        String hasil = prefix + " " + pesan;
        riwayat.add(hasil);
        System.out.println(hasil);
    }

    void laporanHarian() {
        Notifikasi n = this;
        LogAktivitas l = this;
        n.kirim("Laporan dikirim ke pasien");
        l.kirim("Laporan dicatat ke sistem");
        System.out.println("Total riwayat: " + riwayat.size());
    }
}
```

**Pertanyaan:**

**(a)** [6 poin] Tulis **output lengkap** dari kode berikut. Untuk setiap baris output, jelaskan **class mana** yang method-nya dipanggil dan mengapa (dynamic dispatch).

```java
Pasien a = new PasienVIP("Rina", new BiayaBPJS(400000), 300000);
a.rawat(3);
```

**(b)** [5 poin] Setelah `a.rawat(3)`, berapa nilai `a.getTotalBiaya()`? Tunjukkan **langkah kalkulasi lengkap** termasuk:
- `biayaPerHari()` → dari class mana?
- `biaya = biayaPerHari() * hari` → berapa?
- `strategi.hitung(biaya)` → dari class mana? Perhitungan `Math.min`?
- `totalBiaya += bayar` → berapa final?

**(c)** [4 poin] Jika kita ganti strategi di runtime:
```java
Pasien b = new PasienRegular("Doni", new BiayaUmum());
b.rawat(2);
// Sekarang Doni daftar BPJS
// Apakah bisa langsung: b.strategi = new BiayaBPJS(200000) ?
```
Jelaskan mengapa baris tersebut **bisa atau tidak bisa** compile. Hubungkan jawaban Anda dengan konsep **encapsulation** (access modifier `protected`) dan prinsip **Open/Closed Principle**. Usulkan perbaikan desain yang lebih baik.

**(d)** [6 poin] `new SistemRS("notif").laporanHarian()` menghasilkan output apa? Tunjukkan langkah. Apakah `n.kirim()` dan `l.kirim()` memanggil method yang **berbeda**? Mengapa? Apa masalah desain fundamental-nya dan bagaimana Anda memperbaikinya menggunakan **Strategy Pattern**?

**(e)** [4 poin] Refactor class `SistemRS` agar notifikasi dan logging bisa berperilaku berbeda. Tulis **interface dan signature class** menggunakan Strategy Pattern (tidak perlu implementasi lengkap).

---

## SOAL 2 — DIAGNOSIS KODE BERMASALAH + REFACTOR (25 poin)

> Anda menerima kode dari developer junior untuk sistem manajemen perpustakaan online.
> Kode **bisa compile** tapi hasilnya salah dan desainnya buruk.

Perhatikan kode berikut:

```java
// === File: MediaPerpustakaan.java ===
abstract class MediaPerpustakaan {
    private String judul;
    private double hargaSewa;
    protected int jumlahDipinjam;

    MediaPerpustakaan(String judul, double hargaSewa) {
        this.judul = judul;
        this.hargaSewa = hargaSewa;
        this.jumlahDipinjam = 0;
    }

    String getJudul() { return judul; }
    double getHargaSewa() { return hargaSewa; }

    abstract double hitungDenda(int hariTerlambat);

    double totalBiaya(int hariTerlambat) {
        return hargaSewa + hitungDenda(hariTerlambat);
    }

    @Override
    public String toString() {
        return judul + " | Biaya: Rp " + (int)totalBiaya(0);
    }
}

// === File: Buku.java ===
class Buku extends MediaPerpustakaan {
    private String penulis;

    Buku(String judul, double hargaSewa, String penulis) {
        super(judul, hargaSewa);
        this.penulis = penulis;
    }

    @Override
    double hitungDenda(int hariTerlambat) {
        // Denda Rp 1000 per hari, maks 50%
        double denda = hariTerlambat * 1000;
        double maks = getHargaSewa() * 50;    // BUG tersembunyi
        return Math.min(denda, maks);
    }

    String getPenulis() { return penulis; }
}

// === File: DVD.java ===
class DVD extends MediaPerpustakaan {
    private int durasi; // menit

    DVD(String judul, double hargaSewa, int durasi) {
        super(judul, hargaSewa);
        this.durasi = durasi;
    }

    @Override
    double hitungDenda(int hariTerlambat) {
        // Denda Rp 2000 per hari, maks Rp 25000
        return hariTerlambat * 2000;           // BUG: tidak ada cap
    }
}

// === File: StrategiMember.java ===
interface StrategiMember {
    double hitungDiskon(double totalBiaya);
    String getNamaMember();
}

class MemberBiasa implements StrategiMember {
    public double hitungDiskon(double totalBiaya) {
        return 0;  // tidak ada diskon
    }
    public String getNamaMember() { return "Reguler"; }
}

class MemberPremium implements StrategiMember {
    public double hitungDiskon(double totalBiaya) {
        return totalBiaya * 0.20;
    }
    public String getNamaMember() { return "Premium (20%)"; }
}

class MemberPelajar implements StrategiMember {
    public double hitungDiskon(double totalBiaya) {
        return totalBiaya * 0.10;
    }
    public String getNamaMember() { return "Pelajar (10%)"; }
}

// === File: Peminjam.java ===
class Peminjam {
    String nama;                        // BUG: access modifier?
    private List<MediaPerpustakaan> pinjaman = new ArrayList<>();
    private StrategiMember strategi;

    Peminjam(String nama, StrategiMember strategi) {
        this.nama = nama;
        this.strategi = strategi;
    }

    void pinjam(MediaPerpustakaan media) {
        pinjaman.add(media);
        media.jumlahDipinjam++;         // BUG: harusnya lewat method
    }

    double hitungTagihan(int hariTerlambat) {
        double total = 0;
        for (MediaPerpustakaan m : pinjaman) {
            total += m.totalBiaya(hariTerlambat);
        }
        double diskon = strategi.hitungDiskon(total);
        return total - diskon;
    }

    void kembalikanSemua(int hariTerlambat) {
        double tagihan = hitungTagihan(hariTerlambat);
        System.out.println("=== STRUK PENGEMBALIAN ===");
        System.out.println("Peminjam : " + nama);
        System.out.println("Member   : " + strategi.getNamaMember());
        System.out.println("--- Daftar Pinjaman ---");
        for (MediaPerpustakaan m : pinjaman) {
            System.out.println("  " + m.getJudul()
                + " | Denda: Rp " + (int)m.hitungDenda(hariTerlambat)
                + " | Subtotal: Rp " + (int)m.totalBiaya(hariTerlambat));
        }
        System.out.println("--- Ringkasan ---");
        System.out.println("Total sebelum diskon : Rp " + (int)hitungTagihan(0));  // BUG
        System.out.println("Total + denda        : Rp " + (int)(tagihan + strategi.hitungDiskon(tagihan)));
        System.out.println("Diskon " + strategi.getNamaMember() + " : Rp " + (int)strategi.hitungDiskon(tagihan));
        System.out.println("TOTAL BAYAR          : Rp " + (int)tagihan);
        // BUG: pinjaman tidak di-clear setelah dikembalikan
    }
}
```

Kode klien:
```java
Peminjam andi = new Peminjam("Andi", new MemberPremium());
andi.pinjam(new Buku("Java OOP", 5000, "Wahyu"));
andi.pinjam(new DVD("Tutorial Design Pattern", 8000, 120));
andi.pinjam(new Buku("Clean Code", 7000, "Robert"));

andi.kembalikanSemua(10);
```

**Pertanyaan:**

**(a)** [8 poin] Identifikasi **6 bug/masalah desain** yang tersebar di kode. Untuk setiap bug:
- Tunjukkan **baris kode** yang bermasalah
- Jelaskan **apa yang salah** dan **pilar OOP / prinsip desain mana yang dilanggar**
- Hitung **dampak pada output** (untuk bug kalkulasi)

**(b)** [5 poin] Hitung output **aktual** (dengan semua bug) dari `kembalikanSemua(10)`. Lalu hitung output yang **seharusnya** jika semua bug diperbaiki. Tunjukkan **selisih** antara keduanya.

**(c)** [12 poin] Setelah memperbaiki bug, manajer perpustakaan meminta fitur baru:

> *"Kami ingin menambahkan: (1) Notifikasi otomatis ke peminjam jika buku yang ditunggu sudah tersedia kembali, (2) Jenis media baru yaitu Majalah dan EBook tanpa harus mengubah kode yang sudah ada, (3) Sistem denda yang bisa diganti sewaktu-waktu — misal saat libur nasional denda dihapus, saat tahun ajaran baru denda dobel."*

- **(c.1)** [4 poin] Petakan setiap kebutuhan ke **design pattern yang tepat** dan jelaskan mengapa pattern tersebut cocok:

| Kebutuhan | Pattern | Alasan |
|---|---|---|
| (1) Notifikasi ketersediaan | ? | ? |
| (2) Jenis media baru | ? | ? |
| (3) Sistem denda fleksibel | ? | ? |

- **(c.2)** [8 poin] Tulis kode Java **lengkap** yang mengimplementasikan ketiga kebutuhan secara terintegrasi:
  - **Observer Pattern:** Interface `PengamatKetersediaan`, minimal 2 implementasi (Email, SMS)
  - **Factory Pattern:** `MediaFactory` untuk Buku, DVD, Majalah, EBook
  - **Strategy Pattern:** Interface `StrategiDenda`, minimal 3 implementasi (`DendaNormal`, `DendaLibur`, `DendaDobel`)
  - **Demo** di `main()` yang menunjukkan semua fitur bekerja bersama. Kode harus **bisa compile**.

---

## SOAL 3 — KRITIK JAWABAN AI (20 poin)

> Seorang mahasiswa menggunakan AI untuk menjawab soal ujian.
> Tugas Anda: **temukan kesalahan** dan **perbaiki** jawaban AI tersebut.

### Bagian I — AI Salah Trace (10 poin)

Seorang mahasiswa diminta men-trace output kode berikut:

```java
abstract class Tiket {
    protected String jenis;
    protected double harga;

    Tiket(double harga) {
        this.harga = harga;
        tampilkan();
    }

    abstract String getLabel();

    void tampilkan() {
        System.out.println("Tiket[" + getLabel() + "] Rp " + (int)harga);
    }
}

class TiketVIP extends Tiket {
    private String bonus;

    TiketVIP(double harga, String bonus) {
        super(harga);
        this.bonus = bonus;
        System.out.println("Bonus: " + bonus);
    }

    @Override
    String getLabel() {
        return "VIP+" + bonus;
    }
}

class TiketVVIP extends TiketVIP {
    private String lounge;

    TiketVVIP(double harga, String bonus, String lounge) {
        super(harga, bonus);
        this.lounge = lounge;
        System.out.println("Lounge: " + lounge);
    }

    @Override
    String getLabel() {
        return "VVIP+" + bonus + "+" + lounge;
    }
}
```

Eksekusi: `Tiket t = new TiketVVIP(1500000, "Snack", "Skybox");`

**Jawaban AI:**
```
Tiket[VVIP+Snack+Skybox] Rp 1500000
Bonus: Snack
Lounge: Skybox
```

**Pertanyaan:**

**(a)** [5 poin] Apakah jawaban AI di atas **benar atau salah**? Jika salah, tulis output yang benar dan jelaskan **kesalahan spesifik** AI-nya. Trace **langkah per langkah** melalui constructor chain dan dynamic dispatch.

**(b)** [5 poin] Jelaskan mengapa `getLabel()` di constructor `Tiket` menghasilkan output yang mungkin tidak terduga. Hubungkan jawaban Anda dengan:
- **Mekanisme dynamic dispatch** di Java
- Pengaruh **urutan inisialisasi field** dalam constructor chain
- Mengapa memanggil **overridable method dari constructor** dianggap **anti-pattern**
- Berikan contoh skenario nyata kapan bug ini bisa menyebabkan **NullPointerException**

---

### Bagian II — AI Salah Desain Pattern (10 poin)

Seorang mahasiswa bertanya ke AI: *"Buatkan sistem notifikasi perpustakaan yang menggunakan Observer Pattern jika buku dikembalikan."*

**Jawaban AI:**

```java
class Perpustakaan {
    private List<Buku> koleksi = new ArrayList<>();
    private List<String> emailPenunggu = new ArrayList<>();

    void tambahPenunggu(String email) {
        emailPenunggu.add(email);
    }

    void kembalikanBuku(Buku buku) {
        buku.setTersedia(true);
        koleksi.add(buku);
        // Kirim notifikasi ke semua penunggu
        for (String email : emailPenunggu) {
            System.out.println("Kirim email ke " + email
                + ": Buku '" + buku.getJudul() + "' sudah tersedia!");
        }
        // Hapus semua penunggu setelah notifikasi
        emailPenunggu.clear();
    }
}

// Penggunaan:
Perpustakaan lib = new Perpustakaan();
lib.tambahPenunggu("andi@mail.com");
lib.tambahPenunggu("budi@mail.com");
lib.kembalikanBuku(new Buku("Java OOP"));
```

**Pertanyaan:**

**(a)** [4 poin] Identifikasi **minimal 4 masalah** dari desain AI di atas. Untuk setiap masalah, jelaskan pilar OOP atau prinsip desain mana yang dilanggar.

**(b)** [3 poin] Mengapa menggunakan `List<String>` untuk menyimpan penunggu **bukan** Observer Pattern yang benar? Apa **perbedaan fundamental** antara implementasi AI vs Observer Pattern dari Bab 5?

**(c)** [3 poin] Tulis **perbaikan desain** yang menggunakan Observer Pattern **benar** dari Bab 5. Harus ada:
- Interface observer
- Method subscribe/unsubscribe
- Notifikasi yang bisa diperluas (Email, SMS, Push) tanpa mengubah class `Perpustakaan`

---

## SOAL 4 — PROJECT INTEGRASI PERSONAL (30 poin)

> Soal ini **berbeda untuk setiap mahasiswa** berdasarkan **2 digit terakhir NIM**.
> AI tidak bisa menjawab karena tidak tahu NIM Anda.
> Anda diminta membangun **satu sistem terintegrasi** yang menggabungkan SEMUA Bab 1–5.

**Tentukan skenario Anda berdasarkan 2 digit terakhir NIM:**

| Akhiran NIM | Skenario |
|---|---|
| 00–19 | **Sistem Pemesanan Restoran:** Jenis menu (Makanan/Minuman/Dessert), strategi diskon (Member/Promo/Happy Hour), notifikasi dapur saat pesanan masuk, factory untuk pembuatan menu |
| 20–39 | **Sistem Reservasi Hotel:** Jenis kamar (Standard/Deluxe/Suite), strategi harga (Low Season/High Season/Event), notifikasi housekeeping saat checkout, factory untuk pembuatan reservasi |
| 40–59 | **Sistem E-Learning:** Jenis konten (Video/Artikel/Quiz), strategi penilaian (Absolut/Curve/Pass-Fail), notifikasi mahasiswa saat materi baru, factory untuk pembuatan konten |
| 60–79 | **Sistem Klinik Hewan:** Jenis hewan (Kucing/Anjing/Burung), strategi biaya (Reguler/Asuransi/Subsidi), notifikasi pemilik saat hewan selesai diperiksa, factory untuk pembuatan rekam medis |
| 80–99 | **Sistem Toko Buku Online:** Jenis produk (BukuFisik/EBook/AudioBook), strategi pengiriman (Reguler/Express/SameDay), notifikasi pelanggan saat pesanan berubah status, factory untuk pembuatan pesanan |

**Yang HARUS dijawab (sesuai skenario NIM Anda):**

**(a)** [2 poin] Tulis NIM Anda dan skenario yang Anda dapatkan.

**(b)** [4 poin] Buat **class diagram** (format teks) yang menunjukkan **SELURUH class** dalam sistem Anda beserta relasi (`extends`, `implements`, `has-a`, `uses`) dan label pilar OOP / design pattern.

**(c)** [4 poin] Petakan **setiap pilar OOP dan design pattern** yang Anda gunakan ke bagian sistem:

| Konsep | Diterapkan Di | Penjelasan Singkat |
|---|---|---|
| Encapsulation | ? | ? |
| Inheritance | ? | ? |
| Polymorphism | ? | ? |
| Abstraction | ? | ? |
| Strategy Pattern | ? | ? |
| Observer Pattern | ? | ? |
| Factory Pattern | ? | ? |

**(d)** [8 poin] Tulis **kode Java lengkap** yang mencakup:
- Minimal **8 class/interface** yang saling terhubung
- Mendemonstrasikan **keempat pilar OOP** (beri komentar di kode: `// ENCAPSULATION`, dll.)
- Mendemonstrasikan **minimal 3 design pattern** dari Bab 5 (beri komentar: `// STRATEGY`, dll.)
- Method `main()` yang menjalankan skenario bisnis lengkap
- Kode harus **bisa compile dan menghasilkan output yang bermakna**

**(e)** [4 poin] Tulis **output lengkap** dari `main()`. Untuk setiap 3 baris output, jelaskan method mana yang dipanggil, dari class mana, dan pilar/pattern apa yang sedang berjalan.

**(f)** [4 poin] **Skenario perubahan requirement** — misalkan manajer meminta perubahan berikut:

| Skenario | Perubahan |
|---|---|
| Restoran | Tambah jenis menu baru "Paket Hemat" (gabungan Makanan+Minuman, diskon 15%) |
| Hotel | Tambah tipe kamar "Connecting Room" (menghubungkan 2 kamar Standard) |
| E-Learning | Tambah jenis konten "Live Session" (punya jadwal dan kuota peserta) |
| Klinik Hewan | Tambah jenis layanan "Grooming" (tarif terpisah dari pemeriksaan) |
| Toko Buku | Tambah metode pembayaran "Cicilan" (membagi total ke beberapa bulan) |

Jelaskan: class mana yang **TIDAK PERLU diubah**, class mana yang **PERLU ditambah** (bukan diubah), dan hubungkan dengan prinsip **Open/Closed Principle**.

**(g)** [4 poin] **Refleksi kritis:**
- Sebutkan **1 kelemahan** dari desain Anda dan bagaimana memperbaikinya
- Jika diminta menambahkan **State Pattern**, di bagian mana yang paling cocok? Tulis **interface State** dan **2 concrete state** beserta transisinya.

---

## 📊 RUBRIK PENILAIAN

| Soal | Bobot | Kriteria Nilai Penuh | Penalti |
|---|---|---|---|
| **1. Analisis Arsitektur** | 25% | Trace per baris benar + penjelasan dispatch + kalkulasi lengkap | -50% jika langsung jawab tanpa trace |
| **2. Diagnosis & Refactor** | 25% | Semua bug ditemukan + nilai aktual vs seharusnya + kode refactor compile | -40% jika tidak ada perhitungan |
| **3. Kritik Jawaban AI** | 20% | Kesalahan teridentifikasi + penjelasan mekanisme Java + perbaikan tepat | -30% jika tidak jelaskan "mengapa" |
| **4. Project Integrasi** | 30% | NIM sesuai + class diagram + semua pilar + 3 pattern + kode compile + refleksi | 0 jika NIM tidak ditulis |

### Penilaian Khusus Anti-AI:

- Jawaban yang **hanya berisi kode tanpa penjelasan langkah** → nilai maksimal 40%.
- Jawaban yang **tidak menunjukkan trace per baris** pada soal tracing → nilai maksimal 30%.
- Jawaban Soal 4 yang **tidak menyertakan NIM** → nilai 0.
- Jawaban yang **menggunakan terminologi/pola yang tidak diajarkan** di Bab 1–5 (misal: Singleton, Builder, Decorator — kecuali diminta) → akan diperiksa lebih lanjut.
- Plagiarisme antar mahasiswa pada Soal 4 (skenario identik dengan NIM berbeda range) → nilai 0 untuk kedua pihak.
- Kode Soal 4 yang **tidak bisa compile** → nilai maksimal 50% dari bobot Soal 4.

---

## 🔑 KUNCI JAWABAN — HANYA UNTUK DOSEN

<details>
<summary>⚠️ Klik untuk membuka kunci jawaban</summary>

### Soal 1

**Constructor chain `new PasienVIP("Rina", new BiayaBPJS(400000), 300000)`:**

1. `PasienVIP("Rina", new BiayaBPJS(400000), 300000)` → panggil `super("Rina", strategi)`
2. `Pasien("Rina", strategi)` → `this.nama = "Rina"`, `this.strategi = BiayaBPJS`, `this.totalBiaya = 0`
3. Panggil `log("DAFTAR")` → panggil `getKelas()` — **dynamic dispatch ke PasienVIP.getKelas()** → return `"VIP"`
4. `strategi.getKategori()` → BiayaBPJS → return `"BPJS"`
5. Output: `[VIP/BPJS] Rina DAFTAR total=0`
6. Kembali ke PasienVIP constructor: `this.biayaEkstra = 300000`
7. Panggil `log("VIP-READY")` → `getKelas()` = "VIP", `strategi.getKategori()` = "BPJS"
8. Output: `[VIP/BPJS] Rina VIP-READY total=0`

**`a.rawat(3)`:**

9. `biayaPerHari()` → dynamic dispatch ke `PasienVIP.biayaPerHari()` → `500000 + 300000 = 800000`
10. `biaya = 800000 * 3 = 2400000`
11. `strategi.hitung(2400000)` → `BiayaBPJS.hitung(2400000)`:
    - `ditanggung = Math.min(2400000, 400000) = 400000`
    - `return 2400000 - 400000 = 2000000`
12. `totalBiaya += 2000000` → `totalBiaya = 2000000`
13. `log("RAWAT-3hr")` → Output: `[VIP/BPJS] Rina RAWAT-3hr total=2000000`

**Output lengkap:**
```
[VIP/BPJS] Rina DAFTAR total=0
[VIP/BPJS] Rina VIP-READY total=0
[VIP/BPJS] Rina RAWAT-3hr total=2000000
```

**Soal 1(b):** `a.getTotalBiaya()` = **2000000**

Langkah kalkulasi:
- `biayaPerHari()` dari PasienVIP = 500000 + 300000 = 800000
- `biaya = 800000 * 3 = 2400000`
- `strategi.hitung(2400000)` dari BiayaBPJS:
  - `ditanggung = Math.min(2400000, 400000) = 400000`
  - `bayar = 2400000 - 400000 = 2000000`
- `totalBiaya = 0 + 2000000 = 2000000`

**Soal 1(c):**
`b.strategi = new BiayaBPJS(200000)` — **BISA compile** karena `strategi` adalah `protected`. Dari subclass atau same package, field protected bisa diakses. Namun dari kode klien di package berbeda, ini **tidak bisa**.

Masalah desain:
- Langsung mengubah field → melanggar **encapsulation** (tidak ada validasi)
- Lebih baik: tambahkan method `void setStrategi(StrategiBiaya s)` di class Pasien → sesuai prinsip **Open/Closed** dan bisa ditambah validasi

**Soal 1(d):** `new SistemRS("notif").laporanHarian()`:

1. `n.kirim("Laporan dikirim ke pasien")`:
   - `n` bertipe Notifikasi, tapi actual object = SistemRS("notif")
   - Panggil `SistemRS.kirim()` → `mode = "notif"` → `prefix = "[NOTIF]"`
   - Output: `[NOTIF] Laporan dikirim ke pasien`
   - `riwayat` = 1 item

2. `l.kirim("Laporan dicatat ke sistem")`:
   - `l` bertipe LogAktivitas, tapi **SAMA object-nya** = SistemRS("notif")
   - Panggil `SistemRS.kirim()` → `mode = "notif"` → `prefix = "[NOTIF]"` (BUKAN "[LOG]"!)
   - Output: `[NOTIF] Laporan dicatat ke sistem`
   - `riwayat` = 2 item

3. Output: `Total riwayat: 2`

**Output:**
```
[NOTIF] Laporan dikirim ke pasien
[NOTIF] Laporan dicatat ke sistem
Total riwayat: 2
```

`n.kirim()` dan `l.kirim()` memanggil **method yang SAMA** karena satu objek hanya punya satu implementasi `kirim()`. Masalah: `Notifikasi` dan `LogAktivitas` punya method signature identik. Melanggar **Interface Segregation Principle**.

**Soal 1(e)** Refactor:
```java
interface PengirimPesan {
    void kirim(String pesan);
}

class NotifikasiPasien implements PengirimPesan { ... } // prefix [NOTIF]
class LoggerSistem implements PengirimPesan { ... }     // prefix [LOG]

class SistemRS {
    private PengirimPesan notifikasi;
    private PengirimPesan logger;
    // Strategy: bisa set terpisah
}
```

---

### Soal 2

**6 Bug/Masalah Desain:**

1. **Bug persen denda Buku (KRITIS):** `getHargaSewa() * 50` seharusnya `getHargaSewa() * 0.50`. Untuk Buku "Java OOP" (harga 5000), maks denda = `5000 * 50 = 250000` (harusnya `5000 * 0.50 = 2500`). Denda 10 hari = `10 * 1000 = 10000` → `Math.min(10000, 250000) = 10000` (kebetulan tidak kena cap, tapi maks denda salah besar).

2. **Bug cap denda DVD:** Requirement bilang "maks Rp 25000" tapi kode tidak ada cap. `hitungDenda(10) = 10 * 2000 = 20000` (kebetulan < 25000, tapi jika 15 hari → 30000, melewati cap). Fix: `return Math.min(hariTerlambat * 2000, 25000)`.

3. **Bug access modifier Peminjam.nama:** Field `nama` package-private (tanpa modifier). Harusnya `private` untuk encapsulation. Siapapun di package yang sama bisa langsung mengubah `andi.nama = "Hacker"`.

4. **Bug akses langsung jumlahDipinjam:** `media.jumlahDipinjam++` mengakses field protected langsung dari luar hierarki. Seharusnya lewat method `media.tambahPeminjaman()` — melanggar **encapsulation**.

5. **Bug "Total sebelum diskon":** `hitungTagihan(0)` harusnya menampilkan total yang **sudah termasuk denda tapi sebelum diskon**. Tapi `hitungTagihan(0)` menghitung tanpa denda DAN sudah dikurangi diskon → angka yang ditampilkan salah konteksnya.

6. **Bug pinjaman tidak di-clear:** Setelah `kembalikanSemua()`, list `pinjaman` tidak dikosongkan. Jika dipanggil lagi, media yang sudah dikembalikan akan dihitung ulang → tagihan dobel.

**Output aktual (dengan bug):**

Daftar pinjaman:
- Buku "Java OOP": harga=5000, denda=min(10000, 250000)=10000, subtotal=15000
- DVD "Tutorial DP": harga=8000, denda=20000 (no cap), subtotal=28000
- Buku "Clean Code": harga=7000, denda=min(10000, 350000)=10000, subtotal=17000

Total = 15000 + 28000 + 17000 = 60000
Diskon = 60000 * 0.20 = 12000, tagihan = 48000
hitungTagihan(0) = (20000 - 4000) = 16000
strategi.hitungDiskon(48000) = 9600

```
=== STRUK PENGEMBALIAN ===
Peminjam : Andi
Member   : Premium (20%)
--- Daftar Pinjaman ---
  Java OOP | Denda: Rp 10000 | Subtotal: Rp 15000
  Tutorial Design Pattern | Denda: Rp 20000 | Subtotal: Rp 28000
  Clean Code | Denda: Rp 10000 | Subtotal: Rp 17000
--- Ringkasan ---
Total sebelum diskon : Rp 16000
Total + denda        : Rp 57600
Diskon Premium (20%) : Rp 9600
TOTAL BAYAR          : Rp 48000
```

**Output seharusnya (bug fixed):**

Denda Buku "Java OOP": min(10000, 2500) = 2500, subtotal=7500
Denda DVD: min(20000, 25000) = 20000, subtotal=28000
Denda Buku "Clean Code": min(10000, 3500) = 3500, subtotal=10500

Total = 46000, Diskon = 9200, Tagihan = 36800

```
=== STRUK PENGEMBALIAN ===
Peminjam : Andi
Member   : Premium (20%)
--- Daftar Pinjaman ---
  Java OOP | Denda: Rp 2500 | Subtotal: Rp 7500
  Tutorial Design Pattern | Denda: Rp 20000 | Subtotal: Rp 28000
  Clean Code | Denda: Rp 3500 | Subtotal: Rp 10500
--- Ringkasan ---
Total sebelum diskon : Rp 46000
Total + denda        : Rp 46000
Diskon Premium (20%) : Rp 9200
TOTAL BAYAR          : Rp 36800
```

Selisih: Rp 48000 - Rp 36800 = **Rp 11200** lebih mahal karena bug.

**Soal 2(c):** Pattern mapping:
| Kebutuhan | Pattern | Alasan |
|---|---|---|
| Notifikasi ketersediaan | Observer | Subject (Buku) notifikasi ke banyak pengamat tanpa coupling |
| Jenis media baru | Factory | Buat objek tanpa ubah kode existing (Open/Closed) |
| Denda fleksibel | Strategy | Ganti algoritma denda di runtime tanpa ubah class media |

---

### Soal 3 — Bagian I

**(a)** Jawaban AI **SALAH**.

Trace yang benar:
1. `new TiketVVIP(1500000, "Snack", "Skybox")` → panggil `super(1500000, "Snack")`
2. `TiketVIP(1500000, "Snack")` → panggil `super(1500000)`
3. `Tiket(1500000)` → `this.harga = 1500000`
4. Panggil `tampilkan()` → panggil `getLabel()` — **dynamic dispatch ke TiketVVIP.getLabel()**
5. `TiketVVIP.getLabel()` → `"VVIP+" + bonus + "+" + lounge`
   - `bonus` di TiketVIP belum diinisialisasi → `null`
   - `lounge` di TiketVVIP belum diinisialisasi → `null`
6. Output: `Tiket[VVIP+null+null] Rp 1500000`
7. Kembali ke TiketVIP constructor: `this.bonus = "Snack"`
8. Output: `Bonus: Snack`
9. Kembali ke TiketVVIP constructor: `this.lounge = "Skybox"`
10. Output: `Lounge: Skybox`

**Output yang benar:**
```
Tiket[VVIP+null+null] Rp 1500000
Bonus: Snack
Lounge: Skybox
```

**(b)** Dynamic dispatch: Java memanggil method override berdasarkan **actual type** (TiketVVIP). Saat `Tiket` constructor berjalan, field subclass belum diinisialisasi → `null`. Anti-pattern karena bisa menyebabkan NPE jika method melakukan `bonus.toUpperCase()`.

---

### Soal 3 — Bagian II

**(a)** 4 Masalah:
1. **Bukan Observer Pattern:** `List<String>` bukan `List<Observer>`. Tidak ada interface — melanggar **Abstraction** dan **Polymorphism**.
2. **Coupling terlalu tinggi:** `Perpustakaan` mengirim email langsung — melanggar **Single Responsibility** dan **Open/Closed**.
3. **`emailPenunggu.clear()` terlalu agresif:** Menghapus SEMUA penunggu setelah 1 buku dikembalikan.
4. **Penunggu tidak terhubung ke buku spesifik:** Semua penunggu diberitahu untuk semua buku.

**(b)** `List<String>` menyimpan data pasif. Observer Pattern menyimpan **objek yang berperilaku** (punya method `update()`). Di Bab 5, observer adalah interface `PengamatStok` → bisa extend tanpa ubah subject.

**(c)** Perbaikan:
```java
interface PengamatBuku {
    void onBukuTersedia(String judul);
}

class NotifEmail implements PengamatBuku { ... }
class NotifSMS implements PengamatBuku { ... }

class BukuPerpustakaan {
    private List<PengamatBuku> pengamat = new ArrayList<>();
    void subscribe(PengamatBuku p) { pengamat.add(p); }
    void unsubscribe(PengamatBuku p) { pengamat.remove(p); }
    void kembalikan() {
        for (PengamatBuku p : pengamat) p.onBukuTersedia(judul);
    }
}
```

---

### Soal 4

**Penilaian per skenario — verifikasi NIM cocok dengan range.**

Checklist penilaian:
- [ ] Minimal 8 class/interface
- [ ] Keempat pilar OOP teridentifikasi dengan komentar
- [ ] Minimal 3 design pattern (Strategy + Observer + Factory) teridentifikasi
- [ ] Kode bisa compile
- [ ] Method main() menghasilkan output yang bermakna
- [ ] Class saling terhubung (bukan terpisah)

Contoh penerapan State Pattern per skenario:
- Restoran: Status pesanan (Diterima → Dimasak → Siap → Disajikan)
- Hotel: Status kamar (Tersedia → Dipesan → CheckedIn → CheckedOut → Cleaning)
- E-Learning: Status tugas (Draft → Submitted → Reviewed → Graded)
- Klinik: Status pemeriksaan (Registrasi → Pemeriksaan → Pengobatan → Selesai)
- Toko Buku: Status pesanan (Pending → Diproses → Dikirim → Diterima)

</details>

---

*Tunjukkan bahwa Anda menguasai OOP — bukan sekadar menghafal, tapi bisa membangun sistem yang utuh dan bermakna.* 🎯