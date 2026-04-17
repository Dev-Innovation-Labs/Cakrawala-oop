# UJIAN TENGAH SEMESTER (UTS)
# Pemrograman Berorientasi Objek

| Komponen | Detail |
|---|---|
| Dosen | Wahyu Amaldi, M.Kom |
| Prodi | Informatika — Universitas Cakrawala |
| Cakupan | Bab 1–5 (Encapsulation, Inheritance, Polymorphism, Abstraction, Design Patterns) |
| Waktu | 150 menit |
| Sifat | Wajib Screen Capture |
| Bobot | 30% dari Nilai Akhir |
| Format Kumpul | 1 file PDF, nama file: `UTS_OOP_[NIM]_[NAMA].pdf` |

---

## ATURAN

1. Semua jawaban berupa **screenshot** kode di editor + output di terminal.
2. Screenshot harus **full window** (terlihat taskbar/jam komputer). Yang di-crop dikurangi nilainya.
3. **NIM harus muncul di output program**, bukan hanya di komentar.
4. Setiap mahasiswa punya **studi kasus berbeda** berdasarkan 2 digit terakhir NIM (lihat tabel di bawah).
5. Jawaban yang tidak sesuai NIM = nilai 0.
6. Jawaban identik antar mahasiswa = nilai 0 untuk kedua pihak.

---

## TABEL STUDI KASUS BERDASARKAN NIM

Cari **2 digit terakhir NIM** Anda di tabel ini. Semua soal menggunakan data dari tabel ini.

| Akhiran NIM | Toko | Barang | Jenis A | Jenis B | Jenis C | Harga | Stok | Diskon Member | Diskon Promo |
|---|---|---|---|---|---|---|---|---|---|
| 00–04 | Toko Roti | Roti | RotiTawar | RotiCoklat | RotiKeju | 12000 | 40 | 5% | 20% |
| 05–09 | Toko Kue | Kue | KueBolu | KueNastar | KueLapis | 18000 | 55 | 6% | 22% |
| 10–14 | Warung Kopi | Minuman | Espresso | Latte | Cappuccino | 22000 | 30 | 8% | 25% |
| 15–19 | Kedai Jus | Jus | JusJeruk | JusMangga | JusAlpukat | 15000 | 45 | 7% | 18% |
| 20–24 | Toko Buku | Buku | Novel | Komik | TextBook | 65000 | 90 | 10% | 15% |
| 25–29 | Toko Alat Tulis | AlatTulis | Pensil | Pulpen | Spidol | 8500 | 120 | 4% | 12% |
| 30–34 | Apotek | Obat | ObatBebas | ObatKeras | ObatHerbal | 32000 | 150 | 7% | 30% |
| 35–39 | Toko Vitamin | Vitamin | VitaminC | VitaminD | VitaminE | 45000 | 70 | 9% | 20% |
| 40–44 | Toko HP | HP | HPMurah | HPMenengah | HPFlagship | 2200000 | 15 | 12% | 10% |
| 45–49 | Toko Laptop | Laptop | LaptopOffice | LaptopGaming | LaptopDesain | 8500000 | 10 | 8% | 5% |
| 50–54 | Rental Sepeda | Sepeda | SepedaBiasa | SepedaListrik | SepedaGunung | 45000 | 12 | 6% | 35% |
| 55–59 | Rental Motor | Motor | MotorMatic | MotorSport | MotorBebek | 75000 | 20 | 5% | 15% |
| 60–64 | Laundry | Cucian | CuciKiloan | CuciSatuan | CuciExpress | 7000 | 0 | 5% | 20% |
| 65–69 | Jasa Setrika | Setrikaan | SetrikaKiloan | SetrikaSatuan | SetrikaExpress | 5000 | 0 | 4% | 15% |
| 70–74 | Kantin | Makanan | NasiGoreng | MieAyam | Bakso | 15000 | 60 | 9% | 25% |
| 75–79 | Warung Makan | Menu | AyamGeprek | AyamBakar | AyamKrispy | 20000 | 50 | 8% | 20% |
| 80–84 | Studio Foto | PaketFoto | FotoBasic | FotoPremium | FotoExclusive | 125000 | 8 | 15% | 40% |
| 85–89 | Percetakan | Cetakan | CetakBrosur | CetakBanner | CetakUndangan | 35000 | 100 | 10% | 25% |
| 90–94 | Pet Shop | Hewan | Kucing | Anjing | Hamster | 150000 | 20 | 11% | 30% |
| 95–99 | Toko Ikan | Ikan | IkanCupang | IkanKoi | IkanNemo | 85000 | 35 | 7% | 18% |

Contoh: NIM Anda `1234567823` → 2 digit terakhir = `23` → masuk baris **20–24** → Toko Buku, class `Buku`, jenis: Novel / Komik / TextBook, harga 65000, stok 90, diskon member 10%, diskon promo 15%.

---

## SOAL 1 — Instalasi Java dan Encapsulation (20 poin)

Soal ini meminta Anda membuktikan Java sudah terinstall, lalu membuat class pertama dengan data terlindungi (private).

**Langkah-langkah:**

**1a. Cek Java (5 poin)**

Buka terminal lalu ketik:
```
java -version
javac -version
```
Screenshot terminal yang menampilkan versi Java.

**1b. Buat class utama (15 poin)**

Buat file Java dengan nama sesuai kolom **Barang** dari tabel NIM Anda (contoh: `Buku.java`).

Class ini harus punya:
- 4 field `private`: `nama` (String), `harga` (double), `stok` (int), `nim` (String)
- Constructor untuk mengisi semua field
- Getter untuk semua field
- Setter `setHarga()` dan `setStok()` dengan validasi:
  - Jika harga <= 0, cetak `[NIM] ERROR: Harga tidak valid!` dan jangan ubah nilai
  - Jika stok < 0, cetak `[NIM] ERROR: Stok tidak valid!` dan jangan ubah nilai
- Method `info()` yang mencetak semua data barang
- Method `jual(int jumlah)`:
  - Jika stok cukup, kurangi stok dan cetak `[NIM] Terjual [jumlah] [Barang]. Sisa: [stok]`
  - Jika stok tidak cukup, cetak `[NIM] GAGAL: Stok tidak cukup!`

Isi harga dan stok sesuai tabel NIM Anda. Isi nim dengan NIM Anda.

Buat juga `main()` yang menjalankan semua method: `info()`, `jual()` sukses, `jual()` gagal, `setHarga(-1)`, `setStok(-1)`.

Screenshot 1: Kode lengkap di editor.
Screenshot 2: Output di terminal (harus terlihat NIM Anda di setiap baris output).

---

## SOAL 2 — Inheritance (20 poin)

Soal ini meminta Anda membuat 3 subclass dari class Soal 1. Tujuannya menunjukkan bahwa subclass mewarisi sifat superclass dan bisa menambah/mengubah perilaku.

**Langkah-langkah:**

Buat 3 class baru sesuai kolom **Jenis A**, **Jenis B**, **Jenis C** dari tabel NIM Anda. Ketiga class ini meng-extend class dari Soal 1.

Contoh untuk NIM akhiran 23 (Toko Buku):
```
Buku            <-- superclass (dari Soal 1)
├── Novel       <-- subclass A
├── Komik       <-- subclass B
└── TextBook    <-- subclass C
```

Setiap subclass harus:
- Pakai `extends` ke class Soal 1
- Punya constructor yang memanggil `super(...)`
- Punya 1 field tambahan:
  - Jenis A → `String kategori`
  - Jenis B → `double berat`
  - Jenis C → `boolean premium`
- Override method `info()` → panggil `super.info()` lalu cetak field tambahan
- Override method `jual(int jumlah)`:
  - Jenis A → cetak tambahan: `[NIM] [JenisA]: proses standar`
  - Jenis B → cetak tambahan: `[NIM] [JenisB]: perlu penanganan khusus`
  - Jenis C → jika premium true, cetak: `[NIM] [JenisC]: PREMIUM, diskon 10%`

Tambahkan di setiap constructor (superclass dan subclass):
```java
System.out.println("[NIM] Constructor [NamaClass] dipanggil");
```

Buat `main()` yang membuat ketiga objek, panggil `info()` dan `jual(1)` untuk masing-masing.

Screenshot 1: Kode semua class di editor.
Screenshot 2: Output di terminal (harus terlihat urutan constructor + output info + jual).

Tulis jawaban (di bawah screenshot):
Kenapa saat membuat objek subclass, constructor superclass jalan duluan?

---

## SOAL 3 — Polymorphism (20 poin)

Soal ini meminta Anda menunjukkan 2 jenis polymorphism: overriding (method yang sama tapi beda perilaku) dan overloading (method dengan nama sama tapi beda parameter).

**Langkah-langkah:**

**3a. Overriding + Dynamic Dispatch (10 poin)**

Buat array bertipe superclass yang diisi ketiga subclass:

```java
Barang[] daftar = {
    new JenisA(...),
    new JenisB(...),
    new JenisC(...)
};
```

(Ganti `Barang`, `JenisA`, `JenisB`, `JenisC` sesuai tabel NIM Anda)

Loop array tersebut, dan untuk setiap elemen cetak:
```java
System.out.println("[NIM] Tipe: " + item.getClass().getSimpleName());
item.info();
item.jual(2);
System.out.println("---");
```

Ini membuktikan walaupun tipe variabel sama (superclass), method yang jalan adalah milik masing-masing subclass.

**3b. Overloading (10 poin)**

Tambahkan 3 method `hitungTotal` di superclass (nama sama, parameter beda):

```java
double hitungTotal(int jumlah) {
    // harga * jumlah
}

double hitungTotal(int jumlah, double diskonPersen) {
    // harga * jumlah * (1 - diskonPersen/100)
}

double hitungTotal(int jumlah, String kodeVoucher) {
    // kalau kodeVoucher sama dengan NIM Anda → diskon 50%
    // kalau beda → tidak ada diskon
}
```

Di `main()`, panggil ketiga versi dan cetak hasilnya. Gunakan angka **Diskon Member** dari tabel NIM Anda untuk parameter diskonPersen.

Screenshot 1: Kode di editor.
Screenshot 2: Output di terminal.

Tulis jawaban (di bawah screenshot):
Apa beda overriding dan overloading? Tunjukkan dari kode Anda masing-masing 1 contoh.

---

## SOAL 4 — Abstraction (20 poin)

Soal ini meminta Anda mengubah superclass menjadi abstract class dan membuat interface. Tujuannya menunjukkan bahwa kita bisa memaksa subclass untuk mengisi method tertentu.

**Langkah-langkah:**

**4a. Abstract Class (10 poin)**

Ubah class dari Soal 1 menjadi `abstract class` dan tambahkan 2 method abstract:

```java
abstract String getDeskripsi();
abstract double hitungPajak();
```

Setiap subclass wajib mengisi kedua method tersebut:
- `getDeskripsi()` → return kalimat deskripsi yang beda per subclass
- `hitungPajak()`:
  - Jenis A → return harga * 11 / 100 (PPN 11%)
  - Jenis B → return harga * 5 / 100 (pajak ringan)
  - Jenis C → return 0 (bebas pajak)

**4b. Interface (10 poin)**

Buat interface `Diskonable`:
```java
interface Diskonable {
    double hitungDiskon();
    String infoDiskon();
}
```

- Jenis B implements Diskonable → `hitungDiskon()` return harga * diskonMember / 100, `infoDiskon()` return "Member [X]% - NIM: [NIM]"
- Jenis C implements Diskonable → `hitungDiskon()` return harga * diskonPromo / 100, `infoDiskon()` return "Promo [X]% - NIM: [NIM]"

(Gunakan angka Diskon Member dan Diskon Promo dari tabel NIM Anda)

Buat `main()` yang:
- Buat ketiga objek, panggil `info()` + `getDeskripsi()` + `hitungPajak()`
- Untuk Jenis B dan C, panggil juga `hitungDiskon()` dan `infoDiskon()`

Screenshot 1: Kode di editor.
Screenshot 2: Output di terminal.

Tulis jawaban (di bawah screenshot):
Apa beda abstract class dan interface? Kapan pakai yang mana?

---

## SOAL 5 — Strategy Pattern (20 poin)

Soal ini meminta Anda membuat sistem kasir yang bisa ganti-ganti cara menghitung diskon tanpa mengubah kode kasir. Ini disebut Strategy Pattern.

Bayangkan: toko Anda punya 3 jenis diskon. Anda mau bisa ganti diskon kapan saja tanpa ubah class kasir.

**Langkah-langkah:**

Buat interface:
```java
interface StrategiDiskon {
    double hitungHargaAkhir(double harga, int jumlah);
    String namaStrategi();
}
```

Buat 3 class yang implements interface di atas:

1. **TanpaDiskon** — harga akhir = harga * jumlah (tidak ada potongan)
2. **DiskonMember** — harga akhir = harga * jumlah * (1 - diskonMember/100). Pakai angka **Diskon Member** dari tabel NIM Anda.
3. **DiskonPromo** — harga akhir = harga * jumlah * (1 - diskonPromo/100). Pakai angka **Diskon Promo** dari tabel NIM Anda.

Buat class `Kasir`:
```java
class Kasir {
    private StrategiDiskon strategi;

    void setStrategi(StrategiDiskon s) {
        this.strategi = s;
    }

    void bayar(String namaBarang, double harga, int jumlah, String nim) {
        double total = strategi.hitungHargaAkhir(harga, jumlah);
        double diskon = (harga * jumlah) - total;
        System.out.println("=== STRUK " + nim + " ===");
        System.out.println("Strategi : " + strategi.namaStrategi());
        System.out.println("Barang   : " + namaBarang);
        System.out.println("Harga    : " + (int)harga);
        System.out.println("Jumlah   : " + jumlah);
        System.out.println("Diskon   : " + (int)diskon);
        System.out.println("Total    : " + (int)total);
        System.out.println("==================");
    }
}
```

Di `main()`, buat 1 objek Kasir, lalu jalankan 3 transaksi dengan strategi berbeda:
- Transaksi 1: pakai TanpaDiskon, beli 3 Jenis A
- Transaksi 2: pakai DiskonMember, beli 2 Jenis B
- Transaksi 3: pakai DiskonPromo, beli 5 Jenis C

Gunakan nama barang dan harga dari tabel NIM Anda.

Screenshot 1: Kode di editor (interface + 3 strategi + kasir + main).
Screenshot 2: Output 3 struk di terminal.

Tulis jawaban (di bawah screenshot):
Kenapa Strategy Pattern berguna? Apa yang terjadi kalau kita hardcode semua jenis diskon di dalam class Kasir tanpa pakai interface?

---

## PENILAIAN

| Soal | Poin | Yang Dinilai |
|---|---|---|
| 1. Instalasi + Encapsulation | 20 | Java jalan + class dengan private field + validasi |
| 2. Inheritance | 20 | 3 subclass + extends + super() + override + constructor chain |
| 3. Polymorphism | 20 | Dynamic dispatch + overloading 3 versi |
| 4. Abstraction | 20 | Abstract class + interface + implement |
| 5. Strategy Pattern | 20 | Interface + 3 strategi + kasir + ganti strategi runtime |
| TOTAL | 100 | |

### Pengurangan Nilai

| Pelanggaran | Pengurangan |
|---|---|
| NIM tidak muncul di output | -50% per soal |
| Screenshot di-crop (tidak full window) | -50% per screenshot |
| Data tidak sesuai tabel NIM | -100% per soal |
| Jawaban identik antar mahasiswa | 0 untuk kedua pihak |
| Tidak ada jawaban tertulis | -30% per soal |
| Kode error tapi ada usaha debug | -20% per soal |
| Screenshot palsu / diedit | 0 seluruh UTS |

---

## CHECKLIST SEBELUM SUBMIT

- [ ] NIM muncul di output setiap soal
- [ ] Harga, stok, diskon sesuai tabel NIM saya
- [ ] Nama class dan jenis sesuai tabel NIM saya
- [ ] Screenshot full window
- [ ] Pertanyaan tertulis sudah dijawab (Soal 2, 3, 4, 5)
- [ ] File PDF dengan nama: `UTS_OOP_[NIM]_[NAMA].pdf`

---

Selamat mengerjakan.

— Wahyu Amaldi, M.Kom
