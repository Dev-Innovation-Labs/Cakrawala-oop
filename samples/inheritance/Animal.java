/**
 * ═══════════════════════════════════════════════════════════
 *  PILAR 2: INHERITANCE — Contoh Hewan Peliharaan
 * ═══════════════════════════════════════════════════════════
 *
 *  @author  Wahyu Amaldi, M.Kom
 *  @institution Universitas Cakrawala
 *
 *  Mendemonstrasikan:
 *  • Single Inheritance (Kucing extends Hewan)
 *  • Multilevel Inheritance (KucingPersia extends Kucing extends Hewan)
 *  • Hierarchical Inheritance (Kucing & Anjing extends Hewan)
 *  • Method Override dengan @Override
 *  • Penggunaan super() dan super.method()
 *  • protected access modifier
 *
 *  Compile & Run:
 *    javac Animal.java
 *    java Animal
 * ═══════════════════════════════════════════════════════════
 */

// ═══════════════════════════════════════════
// SUPERCLASS: Hewan
// ═══════════════════════════════════════════
class Hewan {

    protected String nama;
    protected int umur;
    protected String jenis;

    public Hewan(String nama, int umur, String jenis) {
        this.nama = nama;
        this.umur = umur;
        this.jenis = jenis;
    }

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

// ═══════════════════════════════════════════
// SUBCLASS: Kucing (extends Hewan)
// ═══════════════════════════════════════════
class Kucing extends Hewan {

    private String warnaBulu;
    private boolean dalamRuangan;

    public Kucing(String nama, int umur, String warnaBulu, boolean dalamRuangan) {
        super(nama, umur, "Kucing");
        this.warnaBulu = warnaBulu;
        this.dalamRuangan = dalamRuangan;
    }

    @Override
    public void bersuara() {
        System.out.println(nama + " berkata: Meoooww! 🐱");
    }

    @Override
    public void info() {
        super.info();
        System.out.println("  Warna Bulu    : " + warnaBulu);
        System.out.println("  Dalam Ruangan : " + (dalamRuangan ? "Ya" : "Tidak"));
    }

    public void bermainBenang() {
        System.out.println(nama + " sedang bermain benang 🧶");
    }

    public void mendengkur() {
        System.out.println(nama + " mendengkur... purrrr 😻");
    }
}

// ═══════════════════════════════════════════
// SUBCLASS: Anjing (extends Hewan)
// ═══════════════════════════════════════════
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

// ═══════════════════════════════════════════
// MULTILEVEL: KucingPersia (extends Kucing extends Hewan)
// ═══════════════════════════════════════════
class KucingPersia extends Kucing {

    private boolean hidungPesek;

    public KucingPersia(String nama, int umur, String warnaBulu, boolean hidungPesek) {
        super(nama, umur, warnaBulu, true);
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

// ═══════════════════════════════════════════
// MAIN: Demonstrasi Inheritance
// ═══════════════════════════════════════════
public class Animal {
    public static void main(String[] args) {
        System.out.println("══════════════════════════════════════");
        System.out.println("   DEMO INHERITANCE — HEWAN PELIHARAAN");
        System.out.println("══════════════════════════════════════\n");

        // ─── Kucing ───
        Kucing milo = new Kucing("Milo", 3, "Oranye", true);
        milo.info();
        milo.bersuara();
        milo.makan();
        milo.bermainBenang();
        milo.mendengkur();

        System.out.println();

        // ─── Anjing ───
        Anjing rocky = new Anjing("Rocky", 5, "Golden Retriever", true);
        rocky.info();
        rocky.bersuara();
        rocky.tidur();
        rocky.ambilBola();
        rocky.jaga();

        System.out.println();

        // ─── Kucing Persia (Multilevel) ───
        KucingPersia luna = new KucingPersia("Luna", 2, "Putih", true);
        luna.info();
        luna.bersuara();
        luna.bermainBenang();
        luna.grooming();

        System.out.println("\n══════════════════════════════════════");
    }
}
