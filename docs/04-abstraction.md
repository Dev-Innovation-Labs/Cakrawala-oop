# 🎨 Pilar 4: Abstraction (Abstraksi)

> **Penulis:** Wahyu Amaldi, M.Kom · **Institusi:** Universitas Cakrawala

[⬅️ Polymorphism](03-polymorphism.md) · [⬅️ Beranda](../README.md)

---

## 📌 Definisi

**Abstraction** adalah proses **menyembunyikan detail implementasi** yang rumit dan hanya **menampilkan fungsionalitas esensial** kepada pengguna. Fokusnya pada **"apa yang dilakukan"**, bukan **"bagaimana melakukannya"**.

> **Inti dari Abstraction:** _"Tunjukkan apa yang penting, sembunyikan yang rumit."_

---

## 🧠 Analogi Dunia Nyata

### 🚗 Mengendarai Mobil

Ketika kamu mengendarai mobil, kamu berinteraksi dengan **hal-hal sederhana**:

```
         APA YANG KAMU LIHAT                APA YANG TERSEMBUNYI
     ┌─────────────────────────┐        ┌─────────────────────────────┐
     │                         │        │                             │
     │  🔑 Tombol Start        │        │  ⚙️ Proses pembakaran mesin │
     │                         │        │  ⛽ Injeksi bahan bakar     │
     │  🔄 Setir               │  ───►  │  🔧 Sistem power steering  │
     │                         │        │  📡 Sensor elektronik       │
     │  🦶 Pedal Gas & Rem     │        │  💻 ECU mengatur rpm mesin  │
     │                         │        │  🌡️ Sistem pendingin        │
     └─────────────────────────┘        └─────────────────────────────┘
     
     ABSTRAKSI                           IMPLEMENTASI
     (Interface sederhana)               (Detail yang disembunyikan)
```

Kamu **tidak perlu tahu** cara kerja mesin pembakaran internal untuk bisa mengendarai mobil. Cukup tahu: putar kunci, injak gas, belok setir.

### ☕ Mesin Kopi

| Kamu Lakukan (Abstraksi) | Yang Terjadi di Dalam (Implementasi) |
|:--------------------------|:--------------------------------------|
| Tekan tombol "Cappuccino" | Giling biji kopi → Panaskan air → Seduh espresso → Kukus susu → Tuang foam |
| Tekan tombol "Espresso" | Giling biji kopi → Panaskan air → Tekan dengan tekanan 9 bar → Tuang |
| Tekan tombol "Latte" | Giling biji kopi → Seduh → Kukus susu → Campur |

Satu tombol sederhana menyembunyikan proses panjang dan rumit.

---

## 🔑 Konsep Kunci

### 2 Cara Menerapkan Abstraction di Java

```
                      ABSTRACTION
                           │
             ┌─────────────┴─────────────┐
             │                           │
      ┌──────┴───────┐          ┌────────┴────────┐
      │  ABSTRACT    │          │   INTERFACE      │
      │  CLASS       │          │                  │
      ├──────────────┤          ├──────────────────┤
      │ • Bisa punya │          │ • Semua method   │
      │   method     │          │   abstract       │
      │   biasa &    │          │   (default)      │
      │   abstract   │          │ • Tidak bisa     │
      │ • Bisa punya │          │   punya field    │
      │   constructor│          │   instance       │
      │ • Single     │          │ • Multiple       │
      │   inheritance│          │   inheritance ✅  │
      └──────────────┘          └──────────────────┘
       Kata kunci:               Kata kunci:
       abstract class            interface
```

### Perbandingan Detail

| Aspek | Abstract Class | Interface |
|:------|:---------------|:----------|
| Keyword | `abstract class` | `interface` |
| Method biasa | ✅ Boleh | ✅ Boleh (sejak Java 8, pakai `default`) |
| Abstract method | ✅ Boleh | ✅ (semua method secara default abstract) |
| Constructor | ✅ Bisa | ❌ Tidak bisa |
| Field/variabel | ✅ Bisa (semua jenis) | ❌ Hanya `static final` (konstanta) |
| Multiple inheritance | ❌ Hanya 1 parent | ✅ Bisa implements banyak |
| Kapan dipakai? | Ada kode bersama yang di-share | Kontrak/kemampuan tanpa implementasi |

---

## 💻 Contoh Kode

### Skenario: Sistem Kendaraan (Abstract Class + Interface)

```java
/**
 * ═══════════════════════════════════════════════
 * INTERFACE: Pengisi Daya
 * → Kontrak untuk kendaraan yang bisa diisi daya
 * → ABSTRAKSI: "Apa yang bisa dilakukan?"
 * ═══════════════════════════════════════════════
 */
interface PengisiDaya {
    void isiDaya(int jumlah);          // Kontrak: harus bisa isi daya
    int getKapasitasBaterai();          // Kontrak: harus punya info kapasitas
    int getLevelBaterai();              // Kontrak: harus tahu level baterai
}

/**
 * ═══════════════════════════════════════════════
 * INTERFACE: Klakson
 * → Kontrak untuk kendaraan yang punya klakson
 * ═══════════════════════════════════════════════
 */
interface Klakson {
    void bunyikanKlakson();            // Kontrak: harus bisa bunyi klakson
}

/**
 * ═══════════════════════════════════════════════
 * ABSTRACT CLASS: Kendaraan
 * → Class abstrak dasar untuk semua kendaraan
 * → Menyediakan template yang HARUS diimplementasi
 *   oleh setiap kendaraan konkret
 * ═══════════════════════════════════════════════
 */
abstract class Kendaraan {

    protected String merk;
    protected String model;
    protected int tahun;
    protected boolean mesinMenyala;

    // Abstract class BISA punya constructor
    public Kendaraan(String merk, String model, int tahun) {
        this.merk = merk;
        this.model = model;
        this.tahun = tahun;
        this.mesinMenyala = false;
    }

    // ══════════════════════════════════════════
    // ABSTRACT METHODS: Harus diimplementasi oleh subclass
    // → "Apa yang harus dilakukan" tanpa "bagaimana"
    // ══════════════════════════════════════════

    /** Setiap kendaraan berjalan dengan cara berbeda */
    public abstract void jalan();

    /** Setiap kendaraan berhenti dengan cara berbeda */
    public abstract void berhenti();

    /** Setiap kendaraan punya cara tampil info bahan bakar berbeda */
    public abstract String infoBahanBakar();

    // ══════════════════════════════════════════
    // CONCRETE METHODS: Implementasi yang di-share
    // → Sama untuk semua kendaraan
    // ══════════════════════════════════════════

    public void nyalakanMesin() {
        if (!mesinMenyala) {
            mesinMenyala = true;
            System.out.println("🔑 Mesin " + merk + " " + model + " menyala!");
        } else {
            System.out.println("⚠️  Mesin sudah menyala.");
        }
    }

    public void matikanMesin() {
        if (mesinMenyala) {
            mesinMenyala = false;
            System.out.println("🔇 Mesin " + merk + " " + model + " mati.");
        } else {
            System.out.println("⚠️  Mesin sudah mati.");
        }
    }

    public void tampilkanInfo() {
        System.out.println("╔═══════════════════════════════════╗");
        System.out.println("║  " + merk + " " + model);
        System.out.println("║  Tahun     : " + tahun);
        System.out.println("║  Mesin     : " + (mesinMenyala ? "🟢 Menyala" : "🔴 Mati"));
        System.out.println("║  " + infoBahanBakar());
        System.out.println("╚═══════════════════════════════════╝");
    }
}

/**
 * ═══════════════════════════════════════════════
 * CONCRETE CLASS: Mobil (BBM)
 * → Mengimplementasi SEMUA method abstract
 * → Detail "bagaimana caranya" ada di sini
 * ═══════════════════════════════════════════════
 */
class Mobil extends Kendaraan implements Klakson {

    private double bensin;        // dalam liter
    private double kapasitasTangki;

    public Mobil(String merk, String model, int tahun, double kapasitasTangki) {
        super(merk, model, tahun);
        this.kapasitasTangki = kapasitasTangki;
        this.bensin = kapasitasTangki; // Mulai full
    }

    @Override
    public void jalan() {
        if (!mesinMenyala) {
            System.out.println("❌ Nyalakan mesin dulu!");
            return;
        }
        if (bensin <= 0) {
            System.out.println("⛽ Bensin habis! Isi dulu.");
            return;
        }
        bensin -= 2.5;
        System.out.println("🚗 " + merk + " " + model + " melaju di jalan raya...");
        System.out.println("   Bensin tersisa: " + String.format("%.1f", bensin) + " liter");
    }

    @Override
    public void berhenti() {
        System.out.println("🛑 " + merk + " " + model + " berhenti.");
    }

    @Override
    public String infoBahanBakar() {
        return "Bensin   : " + String.format("%.1f", bensin) + " / "
                + String.format("%.1f", kapasitasTangki) + " liter";
    }

    @Override
    public void bunyikanKlakson() {
        System.out.println("📢 BEEP BEEP! 🚗");
    }

    public void isiBensin(double liter) {
        bensin = Math.min(bensin + liter, kapasitasTangki);
        System.out.println("⛽ Isi bensin " + liter + " liter. Total: "
                + String.format("%.1f", bensin) + " liter");
    }
}

/**
 * ═══════════════════════════════════════════════
 * CONCRETE CLASS: MobilListrik
 * → Extends Kendaraan + Implements PengisiDaya & Klakson
 * → Contoh MULTIPLE INTERFACE IMPLEMENTATION
 * ═══════════════════════════════════════════════
 */
class MobilListrik extends Kendaraan implements PengisiDaya, Klakson {

    private int levelBaterai;     // dalam persen
    private int kapasitasBaterai; // dalam kWh

    public MobilListrik(String merk, String model, int tahun, int kapasitasBaterai) {
        super(merk, model, tahun);
        this.kapasitasBaterai = kapasitasBaterai;
        this.levelBaterai = 100;
    }

    // ── Implementasi abstract method dari Kendaraan ──

    @Override
    public void jalan() {
        if (!mesinMenyala) {
            System.out.println("❌ Nyalakan mesin dulu!");
            return;
        }
        if (levelBaterai <= 5) {
            System.out.println("🔋 Baterai hampir habis! Charge dulu.");
            return;
        }
        levelBaterai -= 10;
        System.out.println("⚡ " + merk + " " + model + " meluncur tanpa suara...");
        System.out.println("   Baterai tersisa: " + levelBaterai + "%");
    }

    @Override
    public void berhenti() {
        levelBaterai += 2; // Regenerative braking!
        System.out.println("🛑 " + merk + " " + model + " berhenti. (+2% regenerative braking)");
    }

    @Override
    public String infoBahanBakar() {
        return "Baterai  : " + levelBaterai + "% (" + kapasitasBaterai + " kWh)";
    }

    // ── Implementasi interface PengisiDaya ──

    @Override
    public void isiDaya(int jumlah) {
        levelBaterai = Math.min(levelBaterai + jumlah, 100);
        System.out.println("🔌 Charging... Baterai: " + levelBaterai + "%");
    }

    @Override
    public int getKapasitasBaterai() {
        return kapasitasBaterai;
    }

    @Override
    public int getLevelBaterai() {
        return levelBaterai;
    }

    // ── Implementasi interface Klakson ──

    @Override
    public void bunyikanKlakson() {
        System.out.println("📢 *suara futuristik* WOOOOSH! ⚡");
    }
}

/**
 * ═══════════════════════════════════════════════
 * CONCRETE CLASS: SepedaMotor
 * → Implementasi berbeda dari Kendaraan
 * ═══════════════════════════════════════════════
 */
class SepedaMotor extends Kendaraan implements Klakson {

    private double bensin;

    public SepedaMotor(String merk, String model, int tahun) {
        super(merk, model, tahun);
        this.bensin = 15.0; // Tangki kecil
    }

    @Override
    public void jalan() {
        if (!mesinMenyala) {
            System.out.println("❌ Nyalakan mesin dulu!");
            return;
        }
        bensin -= 0.8;
        System.out.println("🏍️ " + merk + " " + model + " melaju lincah...");
        System.out.println("   Bensin tersisa: " + String.format("%.1f", bensin) + " liter");
    }

    @Override
    public void berhenti() {
        System.out.println("🛑 " + merk + " " + model + " berhenti.");
    }

    @Override
    public String infoBahanBakar() {
        return "Bensin   : " + String.format("%.1f", bensin) + " / 15.0 liter";
    }

    @Override
    public void bunyikanKlakson() {
        System.out.println("📢 TIIIN TIIIN! 🏍️");
    }
}

/**
 * ═══════════════════════════════════════════════
 * MAIN: Demonstrasi Abstraction
 * ═══════════════════════════════════════════════
 */
public class Vehicle {
    public static void main(String[] args) {
        System.out.println("══════════════════════════════════════════");
        System.out.println("   DEMO ABSTRACTION — SISTEM KENDARAAN   ");
        System.out.println("══════════════════════════════════════════\n");

        // ─── Mobil BBM ───
        System.out.println("▸ MOBIL BBM\n");
        Mobil avanza = new Mobil("Toyota", "Avanza", 2024, 45.0);
        avanza.tampilkanInfo();
        avanza.nyalakanMesin();
        avanza.jalan();
        avanza.bunyikanKlakson();
        avanza.berhenti();

        System.out.println("\n────────────────────────────────────\n");

        // ─── Mobil Listrik ───
        System.out.println("▸ MOBIL LISTRIK\n");
        MobilListrik tesla = new MobilListrik("Tesla", "Model 3", 2025, 75);
        tesla.tampilkanInfo();
        tesla.nyalakanMesin();
        tesla.jalan();
        tesla.berhenti();        // Regenerative braking!
        tesla.bunyikanKlakson();
        tesla.isiDaya(20);       // Charging

        System.out.println("\n────────────────────────────────────\n");

        // ─── Sepeda Motor ───
        System.out.println("▸ SEPEDA MOTOR\n");
        SepedaMotor nmax = new SepedaMotor("Yamaha", "NMAX", 2024);
        nmax.tampilkanInfo();
        nmax.nyalakanMesin();
        nmax.jalan();
        nmax.bunyikanKlakson();

        System.out.println("\n────────────────────────────────────\n");

        // ─── Kekuatan Abstraction: Polymorphism + Abstraction ───
        System.out.println("▸ KEKUATAN ABSTRACTION\n");
        System.out.println("  Semua kendaraan diperlakukan SAMA");
        System.out.println("  lewat tipe abstrak 'Kendaraan':\n");

        Kendaraan[] garasi = { avanza, tesla, nmax };

        for (Kendaraan k : garasi) {
            k.tampilkanInfo();
            System.out.println();
        }

        System.out.println("══════════════════════════════════════════");
    }
}
```

**Output:**
```
══════════════════════════════════════════
   DEMO ABSTRACTION — SISTEM KENDARAAN   
══════════════════════════════════════════

▸ MOBIL BBM

╔═══════════════════════════════════╗
║  Toyota Avanza
║  Tahun     : 2024
║  Mesin     : 🔴 Mati
║  Bensin   : 45.0 / 45.0 liter
╚═══════════════════════════════════╝
🔑 Mesin Toyota Avanza menyala!
🚗 Toyota Avanza melaju di jalan raya...
   Bensin tersisa: 42.5 liter
📢 BEEP BEEP! 🚗
🛑 Toyota Avanza berhenti.

────────────────────────────────────

▸ MOBIL LISTRIK

╔═══════════════════════════════════╗
║  Tesla Model 3
║  Tahun     : 2025
║  Mesin     : 🔴 Mati
║  Baterai  : 100% (75 kWh)
╚═══════════════════════════════════╝
🔑 Mesin Tesla Model 3 menyala!
⚡ Tesla Model 3 meluncur tanpa suara...
   Baterai tersisa: 90%
🛑 Tesla Model 3 berhenti. (+2% regenerative braking)
📢 *suara futuristik* WOOOOSH! ⚡
🔌 Charging... Baterai: 100%

────────────────────────────────────

▸ SEPEDA MOTOR

╔═══════════════════════════════════╗
║  Yamaha NMAX
║  Tahun     : 2024
║  Mesin     : 🔴 Mati
║  Bensin   : 15.0 / 15.0 liter
╚═══════════════════════════════════╝
🔑 Mesin Yamaha NMAX menyala!
🏍️ Yamaha NMAX melaju lincah...
   Bensin tersisa: 14.2 liter
📢 TIIIN TIIIN! 🏍️

══════════════════════════════════════════
```

---

## 🔍 Abstract Class vs Interface: Kapan Digunakan?

### Gunakan Abstract Class Ketika:

```java
// ✅ Ada KODE BERSAMA yang di-share ke semua subclass
abstract class Kendaraan {
    // Semua kendaraan bisa nyalakan/matikan mesin → SHARED
    public void nyalakanMesin() { ... }               
    
    // Tapi cara "jalan" BEDA → ABSTRACT
    public abstract void jalan();
}
```

### Gunakan Interface Ketika:

```java
// ✅ Mendefinisikan KEMAMPUAN (capability) tanpa implementasi
interface PengisiDaya {
    void isiDaya(int jumlah);  // Kontrak: "bisa diisi daya"
}

// ✅ Butuh MULTIPLE INHERITANCE
class MobilListrik extends Kendaraan 
    implements PengisiDaya, Klakson {  // 2 interface sekaligus!
}
```

### Panduan Cepat

```
┌─────────────────────────────────────────────────────────────┐
│                                                             │
│  "Punya kode yang bisa di-share?"                          │
│      → YA  → Abstract Class                                │
│      → TIDAK → Interface                                   │
│                                                             │
│  "Butuh implements dari banyak sumber?"                    │
│      → YA  → Interface                                     │
│      → TIDAK → Abstract Class                              │
│                                                             │
│  "Mendefinisikan kontrak/kemampuan tanpa state?"           │
│      → YA  → Interface                                     │
│      → TIDAK → Abstract Class                              │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

---

## 🧩 Hubungan Abstraction dengan Pilar Lain

```
┌──────────────────────────────────────────────────────────┐
│                                                          │
│  ABSTRACTION menyembunyikan DETAIL                       │
│       ↕                                                  │
│  ENCAPSULATION menyembunyikan DATA                       │
│       ↕                                                  │
│  INHERITANCE memungkinkan class BERBAGI abstraksi         │
│       ↕                                                  │
│  POLYMORPHISM memberi BANYAK BENTUK pada abstraksi        │
│                                                          │
│  → Keempatnya bekerja BERSAMA untuk membuat               │
│    kode yang bersih, modular, dan mudah di-maintain!     │
│                                                          │
└──────────────────────────────────────────────────────────┘
```

---

## ⚠️ Kesalahan Umum

| ❌ Salah | ✅ Benar |
|:---------|:---------|
| Membuat abstract class tanpa method abstract | Jika abstract, harus punya minimal 1 abstract method |
| Mencoba membuat objek dari abstract class | Abstract class **tidak bisa** di-instantiate |
| Menggunakan abstract class padahal hanya butuh kontrak | Gunakan interface sebagai gantinya |
| Terlalu banyak abstraksi (over-engineering) | Abstraksi secukupnya, jangan lebih rumit dari masalahnya |

---

## 📋 Checklist Abstraction

- [ ] Abstract class punya minimal 1 method abstract
- [ ] Interface hanya berisi kontrak (method signature) atau konstanta
- [ ] Gunakan abstract class untuk shared behavior
- [ ] Gunakan interface untuk mendefinisikan kemampuan (capability)
- [ ] Tidak ada class yang langsung meng-instantiate abstract class
- [ ] Setiap method abstract SUDAH diimplementasi di class konkret

---

## 🔗 Navigasi

| Sebelumnya | Berikutnya |
|:-----------|:-----------|
| [📖 ← Polymorphism](03-polymorphism.md) | [🏠 Beranda](../README.md) |

---

<p align="center"><i>"Abstraction bukan tentang membuat sesuatu jadi rumit — justru sebaliknya, tentang membuat yang rumit jadi sederhana."</i></p>

---

<p align="center">
  <b>Wahyu Amaldi, M.Kom</b> · Universitas Cakrawala
</p>
