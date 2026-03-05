/**
 * ═══════════════════════════════════════════════════════════
 *  PILAR 4: ABSTRACTION — Contoh Sistem Kendaraan
 * ═══════════════════════════════════════════════════════════
 *
 *  @author  Wahyu Amaldi, M.Kom
 *  @institution Universitas Cakrawala
 *
 *  Mendemonstrasikan:
 *  • Abstract Class (Kendaraan)
 *    → Template dengan method abstract & concrete
 *  • Interface (PengisiDaya, Klakson)
 *    → Kontrak kemampuan yang harus diimplementasi
 *  • Multiple Interface Implementation
 *    → MobilListrik implements PengisiDaya & Klakson
 *  • Perbedaan abstract class vs interface
 *
 *  Compile & Run:
 *    javac Vehicle.java
 *    java Vehicle
 * ═══════════════════════════════════════════════════════════
 */

// ═══════════════════════════════════════════
// INTERFACE: PengisiDaya
// ═══════════════════════════════════════════
interface PengisiDaya {
    void isiDaya(int jumlah);
    int getKapasitasBaterai();
    int getLevelBaterai();
}

// ═══════════════════════════════════════════
// INTERFACE: Klakson
// ═══════════════════════════════════════════
interface Klakson {
    void bunyikanKlakson();
}

// ═══════════════════════════════════════════
// ABSTRACT CLASS: Kendaraan
// ═══════════════════════════════════════════
abstract class Kendaraan {

    protected String merk;
    protected String model;
    protected int tahun;
    protected boolean mesinMenyala;

    public Kendaraan(String merk, String model, int tahun) {
        this.merk = merk;
        this.model = model;
        this.tahun = tahun;
        this.mesinMenyala = false;
    }

    // Abstract methods — HARUS diimplementasi
    public abstract void jalan();
    public abstract void berhenti();
    public abstract String infoBahanBakar();

    // Concrete methods — shared oleh semua subclass
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

// ═══════════════════════════════════════════
// CONCRETE CLASS: Mobil (BBM)
// ═══════════════════════════════════════════
class Mobil extends Kendaraan implements Klakson {

    private double bensin;
    private double kapasitasTangki;

    public Mobil(String merk, String model, int tahun, double kapasitasTangki) {
        super(merk, model, tahun);
        this.kapasitasTangki = kapasitasTangki;
        this.bensin = kapasitasTangki;
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

// ═══════════════════════════════════════════
// CONCRETE CLASS: MobilListrik
// ═══════════════════════════════════════════
class MobilListrik extends Kendaraan implements PengisiDaya, Klakson {

    private int levelBaterai;
    private int kapasitasBaterai;

    public MobilListrik(String merk, String model, int tahun, int kapasitasBaterai) {
        super(merk, model, tahun);
        this.kapasitasBaterai = kapasitasBaterai;
        this.levelBaterai = 100;
    }

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
        levelBaterai += 2;
        System.out.println("🛑 " + merk + " " + model + " berhenti. (+2% regenerative braking)");
    }

    @Override
    public String infoBahanBakar() {
        return "Baterai  : " + levelBaterai + "% (" + kapasitasBaterai + " kWh)";
    }

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

    @Override
    public void bunyikanKlakson() {
        System.out.println("📢 *suara futuristik* WOOOOSH! ⚡");
    }
}

// ═══════════════════════════════════════════
// CONCRETE CLASS: SepedaMotor
// ═══════════════════════════════════════════
class SepedaMotor extends Kendaraan implements Klakson {

    private double bensin;

    public SepedaMotor(String merk, String model, int tahun) {
        super(merk, model, tahun);
        this.bensin = 15.0;
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

// ═══════════════════════════════════════════
// MAIN: Demonstrasi Abstraction
// ═══════════════════════════════════════════
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
        tesla.berhenti();
        tesla.bunyikanKlakson();
        tesla.isiDaya(20);

        System.out.println("\n────────────────────────────────────\n");

        // ─── Sepeda Motor ───
        System.out.println("▸ SEPEDA MOTOR\n");
        SepedaMotor nmax = new SepedaMotor("Yamaha", "NMAX", 2024);
        nmax.tampilkanInfo();
        nmax.nyalakanMesin();
        nmax.jalan();
        nmax.bunyikanKlakson();

        System.out.println("\n────────────────────────────────────\n");

        // ─── Kekuatan Abstraction ───
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
