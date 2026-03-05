# 🧬 Pilar 2: Inheritance (Pewarisan)

> **Penulis:** Wahyu Amaldi, M.Kom · **Institusi:** Universitas Cakrawala

[⬅️ Encapsulation](01-encapsulation.md) · [⬅️ Beranda](../README.md) · [Polymorphism ➡️](03-polymorphism.md)

---

## 📌 Definisi

**Inheritance** adalah mekanisme di mana sebuah class (**subclass / child**) dapat **mewarisi** atribut dan method dari class lain (**superclass / parent**), sehingga kode bisa digunakan kembali tanpa ditulis ulang.

> **Inti dari Inheritance:** _"Tulis sekali, wariskan ke banyak — jangan ulangi dirimu (DRY: Don't Repeat Yourself)."_

---

## 🧠 Analogi Dunia Nyata

### 👨‍👩‍👧‍👦 Hubungan Orang Tua dan Anak

```
         Ayah (Superclass)
         ┌──────────────────────┐
         │ • Bermata coklat      │
         │ • Berambut hitam      │
         │ • Bisa memasak        │
         │ • Bisa menyetir       │
         └──────────┬───────────┘
                    │ mewariskan
          ┌─────────┴──────────┐
          │                    │
    ┌─────┴──────┐     ┌──────┴──────┐
    │  Anak #1   │     │  Anak #2    │
    │            │     │             │
    │ • Bermata  │     │ • Bermata   │
    │   coklat ✅│     │   coklat ✅ │
    │ • Bisa     │     │ • Bisa      │
    │   memasak ✅│    │   memasak ✅│
    │ • Bisa     │     │ • Bisa main │
    │   melukis 🆕│    │   gitar 🆕  │
    └────────────┘     └─────────────┘
```

- **Anak mewarisi** sifat orang tua (mata coklat, bisa memasak)
- **Anak bisa punya** kemampuan tambahan sendiri (melukis, gitar)
- Anak **tidak perlu belajar ulang** apa yang sudah dimiliki orang tua

---

## 🔑 Konsep Kunci

### 1. Superclass dan Subclass

```
┌─────────────────────────────────────┐
│  SUPERCLASS (Parent / Base Class)   │
│  → Class yang atributnya diwarisi   │
└──────────────┬──────────────────────┘
               │ extends
┌──────────────┴──────────────────────┐
│  SUBCLASS (Child / Derived Class)   │
│  → Class yang mewarisi              │
│  → Bisa menambah fitur sendiri      │
│  → Bisa override method parent      │
└─────────────────────────────────────┘
```

### 2. Keyword Penting

| Keyword | Fungsi | Contoh |
|:--------|:-------|:-------|
| `extends` | Mendeklarasikan pewarisan | `class Kucing extends Hewan` |
| `super` | Mengakses member superclass | `super.nama`, `super()` |
| `@Override` | Menandai method yang di-override | `@Override void suara()` |
| `protected` | Akses oleh subclass | `protected String nama` |

### 3. Jenis-Jenis Inheritance

```
1️⃣ SINGLE INHERITANCE            2️⃣ MULTILEVEL INHERITANCE
   ┌─────────┐                       ┌─────────┐
   │  Hewan  │                       │ Makhluk │
   └────┬────┘                       └────┬────┘
        │                                 │
   ┌────┴────┐                       ┌────┴────┐
   │ Kucing  │                       │  Hewan  │
   └─────────┘                       └────┬────┘
                                          │
                                     ┌────┴────┐
                                     │ Kucing  │
                                     └─────────┘

3️⃣ HIERARCHICAL INHERITANCE
        ┌─────────┐
        │  Hewan  │
        └────┬────┘
       ┌─────┴─────┐
  ┌────┴────┐ ┌────┴────┐
  │ Kucing  │ │  Anjing │
  └─────────┘ └─────────┘
```

> ⚠️ **Java tidak mendukung Multiple Inheritance** (mewarisi dari 2+ class sekaligus). Untuk itu, gunakan **Interface** (dibahas di pilar Abstraction).

---

## 💻 Contoh Kode

### Skenario: Sistem Manajemen Hewan Peliharaan

```java
/**
 * ═══════════════════════════════════════════
 * SUPERCLASS: Hewan
 * → Class dasar untuk semua hewan
 * ═══════════════════════════════════════════
 */
class Hewan {

    // Protected → bisa diakses oleh subclass
    protected String nama;
    protected int umur;
    protected String jenis;

    // Constructor
    public Hewan(String nama, int umur, String jenis) {
        this.nama = nama;
        this.umur = umur;
        this.jenis = jenis;
    }

    // Method umum untuk semua hewan
    public void makan() {
        System.out.println(nama + " sedang makan 🍽️");
    }

    public void tidur() {
        System.out.println(nama + " sedang tidur 😴");
    }

    public void bersuara() {
        System.out.println(nama + " mengeluarkan suara...");
    }

    public void info() {
        System.out.println("┌──────────────────────────");
        System.out.println("│ Nama  : " + nama);
        System.out.println("│ Umur  : " + umur + " tahun");
        System.out.println("│ Jenis : " + jenis);
        System.out.println("└──────────────────────────");
    }
}

/**
 * ═══════════════════════════════════════════
 * SUBCLASS: Kucing (extends Hewan)
 * → Mewarisi semua dari Hewan
 * → Menambah kemampuan sendiri
 * → Meng-override method bersuara()
 * ═══════════════════════════════════════════
 */
class Kucing extends Hewan {

    // Atribut tambahan milik Kucing saja
    private String warnaBulu;
    private boolean dalamRuangan;

    public Kucing(String nama, int umur, String warnaBulu, boolean dalamRuangan) {
        // super() → memanggil constructor parent (Hewan)
        super(nama, umur, "Kucing");
        this.warnaBulu = warnaBulu;
        this.dalamRuangan = dalamRuangan;
    }

    // Override: Kucing punya suara khas sendiri
    @Override
    public void bersuara() {
        System.out.println(nama + " berkata: Meoooww! 🐱");
    }

    // Override: Tampilkan info lebih lengkap
    @Override
    public void info() {
        super.info();  // Panggil info() dari Hewan dulu
        System.out.println("  Warna Bulu    : " + warnaBulu);
        System.out.println("  Dalam Ruangan : " + (dalamRuangan ? "Ya" : "Tidak"));
    }

    // Method unik milik Kucing
    public void bermainBenang() {
        System.out.println(nama + " sedang bermain benang 🧶");
    }

    public void mendengkur() {
        System.out.println(nama + " mendengkur... purrrr 😻");
    }
}

/**
 * ═══════════════════════════════════════════
 * SUBCLASS: Anjing (extends Hewan)
 * → Mewarisi semua dari Hewan
 * → Punya fitur berbeda dari Kucing
 * ═══════════════════════════════════════════
 */
class Anjing extends Hewan {

    private String ras;
    private boolean terlatih;

    public Anjing(String nama, int umur, String ras, boolean terlatih) {
        super(nama, umur, "Anjing");
        this.ras = ras;
        this.terlatih = terlatih;
    }

    @Override
    public void bersuara() {
        System.out.println(nama + " berkata: Guk guk guk! 🐶");
    }

    @Override
    public void info() {
        super.info();
        System.out.println("  Ras     : " + ras);
        System.out.println("  Terlatih: " + (terlatih ? "Ya" : "Belum"));
    }

    // Method unik milik Anjing
    public void ambilBola() {
        System.out.println(nama + " mengambil bola! 🎾");
    }

    public void jaga() {
        if (terlatih) {
            System.out.println(nama + " menjaga rumah dengan sigap! 🏠");
        } else {
            System.out.println(nama + " belum terlatih untuk menjaga rumah.");
        }
    }
}

/**
 * ═══════════════════════════════════════════
 * SUBCLASS dari SUBCLASS: KucingPersia
 * → Multilevel Inheritance (Hewan → Kucing → KucingPersia)
 * ═══════════════════════════════════════════
 */
class KucingPersia extends Kucing {

    private boolean hidungPesek;

    public KucingPersia(String nama, int umur, String warnaBulu, boolean hidungPesek) {
        super(nama, umur, warnaBulu, true); // Kucing Persia biasanya indoor
        this.hidungPesek = hidungPesek;
    }

    @Override
    public void bersuara() {
        System.out.println(nama + " berkata: Meww~ (suara lembut ala Persia) 👑🐱");
    }

    public void grooming() {
        System.out.println(nama + " sedang di-grooming, bulu jadi makin cantik! ✨");
    }
}

/**
 * ═══════════════════════════════════════════
 * MAIN: Demonstrasi Inheritance
 * ═══════════════════════════════════════════
 */
public class Animal {
    public static void main(String[] args) {
        System.out.println("══════════════════════════════════════");
        System.out.println("   DEMO INHERITANCE — HEWAN PELIHARAAN");
        System.out.println("══════════════════════════════════════\n");

        // ─── Kucing ───
        Kucing milo = new Kucing("Milo", 3, "Oranye", true);
        milo.info();
        milo.bersuara();       // Override dari Hewan
        milo.makan();          // Diwarisi dari Hewan
        milo.bermainBenang();  // Method milik Kucing
        milo.mendengkur();     // Method milik Kucing

        System.out.println();

        // ─── Anjing ───
        Anjing rocky = new Anjing("Rocky", 5, "Golden Retriever", true);
        rocky.info();
        rocky.bersuara();      // Override dari Hewan
        rocky.tidur();         // Diwarisi dari Hewan
        rocky.ambilBola();     // Method milik Anjing
        rocky.jaga();          // Method milik Anjing

        System.out.println();

        // ─── Kucing Persia (Multilevel) ───
        KucingPersia luna = new KucingPersia("Luna", 2, "Putih", true);
        luna.info();           // Override bertingkat
        luna.bersuara();       // Override dari KucingPersia
        luna.bermainBenang();  // Diwarisi dari Kucing
        luna.grooming();       // Method milik KucingPersia

        System.out.println("\n══════════════════════════════════════");
    }
}
```

**Output:**
```
══════════════════════════════════════
   DEMO INHERITANCE — HEWAN PELIHARAAN
══════════════════════════════════════

┌──────────────────────────
│ Nama  : Milo
│ Umur  : 3 tahun
│ Jenis : Kucing
└──────────────────────────
  Warna Bulu    : Oranye
  Dalam Ruangan : Ya
Milo berkata: Meoooww! 🐱
Milo sedang makan 🍽️
Milo sedang bermain benang 🧶
Milo mendengkur... purrrr 😻

┌──────────────────────────
│ Nama  : Rocky
│ Umur  : 5 tahun
│ Jenis : Anjing
└──────────────────────────
  Ras     : Golden Retriever
  Terlatih: Ya
Rocky berkata: Guk guk guk! 🐶
Rocky sedang tidur 😴
Rocky mengambil bola! 🎾
Rocky menjaga rumah dengan sigap! 🏠

┌──────────────────────────
│ Nama  : Luna
│ Umur  : 2 tahun
│ Jenis : Kucing
└──────────────────────────
  Warna Bulu    : Putih
  Dalam Ruangan : Ya
Luna berkata: Meww~ (suara lembut ala Persia) 👑🐱
Luna sedang bermain benang 🧶
Luna sedang di-grooming, bulu jadi makin cantik! ✨

══════════════════════════════════════
```

---

## 🔍 Apa yang Diwarisi dan Tidak?

| Aspek | Diwarisi? | Keterangan |
|:------|:---------:|:-----------|
| `public` methods | ✅ | Bisa diakses langsung oleh subclass |
| `protected` methods | ✅ | Bisa diakses oleh subclass |
| `public` fields | ✅ | Diwarisi, tapi lebih baik gunakan getter |
| `protected` fields | ✅ | Bisa diakses dalam subclass |
| `private` fields | ❌ | Hanya bisa diakses via getter/setter |
| `private` methods | ❌ | Tidak terlihat oleh subclass |
| Constructor | ❌ | Tidak diwarisi, tapi bisa dipanggil via `super()` |

---

## 🧩 Kapan Menggunakan Inheritance?

### ✅ Gunakan Inheritance Ketika:

```
"Apakah Kucing ADALAH (is-a) Hewan?" → YA → gunakan inheritance
"Apakah Anjing ADALAH (is-a) Hewan?" → YA → gunakan inheritance
```

### ❌ Jangan Gunakan Inheritance Ketika:

```
"Apakah Mobil ADALAH (is-a) Mesin?"  → TIDAK → Mobil MEMILIKI (has-a) Mesin
                                       → Gunakan COMPOSITION, bukan inheritance
```

### Tes "Is-A" vs "Has-A"

| Hubungan | Tipe | Contoh |
|:---------|:-----|:-------|
| Kucing **is-a** Hewan | Inheritance | `class Kucing extends Hewan` |
| Mobil **has-a** Mesin | Composition | `class Mobil { Mesin mesin; }` |
| Mahasiswa **is-a** Orang | Inheritance | `class Mahasiswa extends Orang` |
| Mahasiswa **has-a** NIM | Composition | `class Mahasiswa { String nim; }` |

---

## ⚠️ Kesalahan Umum

| ❌ Salah | ✅ Benar |
|:---------|:---------|
| Inheritance terlalu dalam (5+ level) | Maksimal 2-3 level |
| Mewarisi hanya untuk reuse, bukan karena relasi "is-a" | Gunakan composition jika bukan "is-a" |
| Lupa panggil `super()` di constructor | Selalu panggil `super()` jika parent punya constructor |
| Override method tanpa `@Override` | Selalu gunakan `@Override` annotation |

---

## 🔗 Navigasi

| Sebelumnya | Berikutnya |
|:-----------|:-----------|
| [📖 ← Encapsulation](01-encapsulation.md) | [📖 Polymorphism →](03-polymorphism.md) |

---

<p align="center"><i>"Inheritance seperti silsilah keluarga — anak mewarisi, tapi bisa juga punya keunikan sendiri."</i></p>

---

<p align="center">
  <b>Wahyu Amaldi, M.Kom</b> · Universitas Cakrawala
</p>
