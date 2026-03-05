/**
 * ═══════════════════════════════════════════════════════════
 *  PILAR 3: POLYMORPHISM — Contoh Bangun Datar & Kalkulator
 * ═══════════════════════════════════════════════════════════
 *
 *  @author  Wahyu Amaldi, M.Kom
 *  @institution Universitas Cakrawala
 *
 *  Mendemonstrasikan:
 *  • Runtime Polymorphism (Method Overriding)
 *    → Satu method hitungLuas(), beda implementasi per subclass
 *  • Compile-time Polymorphism (Method Overloading)
 *    → Satu nama method jumlahkan(), beda parameter
 *  • Dynamic Dispatch
 *    → Array bertipe superclass, isi subclass beragam
 *
 *  Compile & Run:
 *    javac Shape.java
 *    java Shape
 * ═══════════════════════════════════════════════════════════
 */

// ═══════════════════════════════════════════
// SUPERCLASS (ABSTRACT): BangunDatar
// ═══════════════════════════════════════════
abstract class BangunDatar {

    protected String nama;
    protected String warna;

    public BangunDatar(String nama, String warna) {
        this.nama = nama;
        this.warna = warna;
    }

    public abstract double hitungLuas();
    public abstract double hitungKeliling();

    public void tampilkanInfo() {
        System.out.println("┌─────────────────────────────────");
        System.out.println("│ Bangun    : " + nama);
        System.out.println("│ Warna     : " + warna);
        System.out.printf("│ Luas      : %.2f%n", hitungLuas());
        System.out.printf("│ Keliling  : %.2f%n", hitungKeliling());
        System.out.println("└─────────────────────────────────");
    }
}

// ═══════════════════════════════════════════
// SUBCLASS: Persegi
// ═══════════════════════════════════════════
class Persegi extends BangunDatar {

    private double sisi;

    public Persegi(double sisi, String warna) {
        super("Persegi", warna);
        this.sisi = sisi;
    }

    @Override
    public double hitungLuas() {
        return sisi * sisi;
    }

    @Override
    public double hitungKeliling() {
        return 4 * sisi;
    }
}

// ═══════════════════════════════════════════
// SUBCLASS: Lingkaran
// ═══════════════════════════════════════════
class Lingkaran extends BangunDatar {

    private double jariJari;

    public Lingkaran(double jariJari, String warna) {
        super("Lingkaran", warna);
        this.jariJari = jariJari;
    }

    @Override
    public double hitungLuas() {
        return Math.PI * jariJari * jariJari;
    }

    @Override
    public double hitungKeliling() {
        return 2 * Math.PI * jariJari;
    }
}

// ═══════════════════════════════════════════
// SUBCLASS: Segitiga
// ═══════════════════════════════════════════
class Segitiga extends BangunDatar {

    private double alas;
    private double tinggi;
    private double sisiA, sisiB, sisiC;

    public Segitiga(double alas, double tinggi, double sisiA, double sisiB, double sisiC, String warna) {
        super("Segitiga", warna);
        this.alas = alas;
        this.tinggi = tinggi;
        this.sisiA = sisiA;
        this.sisiB = sisiB;
        this.sisiC = sisiC;
    }

    @Override
    public double hitungLuas() {
        return 0.5 * alas * tinggi;
    }

    @Override
    public double hitungKeliling() {
        return sisiA + sisiB + sisiC;
    }
}

// ═══════════════════════════════════════════
// CLASS: Kalkulator (Overloading)
// ═══════════════════════════════════════════
class Kalkulator {

    public int jumlahkan(int a, int b) {
        System.out.println("📌 Memanggil jumlahkan(int, int)");
        return a + b;
    }

    public int jumlahkan(int a, int b, int c) {
        System.out.println("📌 Memanggil jumlahkan(int, int, int)");
        return a + b + c;
    }

    public double jumlahkan(double a, double b) {
        System.out.println("📌 Memanggil jumlahkan(double, double)");
        return a + b;
    }

    public String jumlahkan(String a, String b) {
        System.out.println("📌 Memanggil jumlahkan(String, String)");
        return a + b;
    }
}

// ═══════════════════════════════════════════
// MAIN: Demonstrasi Polymorphism
// ═══════════════════════════════════════════
public class Shape {
    public static void main(String[] args) {

        System.out.println("══════════════════════════════════════════");
        System.out.println("  DEMO POLYMORPHISM — BANGUN DATAR       ");
        System.out.println("══════════════════════════════════════════\n");

        // ── Runtime Polymorphism ──
        System.out.println("▸ RUNTIME POLYMORPHISM (Method Overriding)");
        System.out.println("  Satu method hitungLuas(), tiga hasil berbeda!\n");

        BangunDatar[] bangunDatar = {
            new Persegi(5, "Merah"),
            new Lingkaran(7, "Biru"),
            new Segitiga(6, 8, 6, 8, 10, "Hijau")
        };

        for (BangunDatar bangun : bangunDatar) {
            bangun.tampilkanInfo();
            System.out.println();
        }

        // ── Compile-time Polymorphism ──
        System.out.println("══════════════════════════════════════════");
        System.out.println("▸ COMPILE-TIME POLYMORPHISM (Method Overloading)");
        System.out.println("  Satu nama method, empat versi berbeda!\n");

        Kalkulator calc = new Kalkulator();

        System.out.println("Hasil: " + calc.jumlahkan(5, 3));
        System.out.println("Hasil: " + calc.jumlahkan(5, 3, 2));
        System.out.println("Hasil: " + calc.jumlahkan(5.5, 3.2));
        System.out.println("Hasil: " + calc.jumlahkan("Hello, ", "World!"));

        System.out.println("\n══════════════════════════════════════════");
    }
}
