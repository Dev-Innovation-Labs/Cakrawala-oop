/**
 * ═══════════════════════════════════════════════════════════
 *  PILAR 1: ENCAPSULATION — Contoh Rekening Bank
 * ═══════════════════════════════════════════════════════════
 *
 *  @author  Wahyu Amaldi, M.Kom
 *  @institution Universitas Cakrawala
 *
 *  Mendemonstrasikan:
 *  • Private fields (data tersembunyi)
 *  • Getter & Setter dengan validasi
 *  • Method private sebagai helper internal
 *  • Kontrol akses terhadap data sensitif
 *
 *  Compile & Run:
 *    javac BankAccount.java
 *    java BankAccount
 * ═══════════════════════════════════════════════════════════
 */
public class BankAccount {

    // ═══════════════════════════════════════════
    // 🔒 PRIVATE: Data tersembunyi dari luar
    // ═══════════════════════════════════════════
    private String namaPemilik;
    private double saldo;
    private String pin;
    private String nomorRekening;

    // ═══════════════════════════════════════════
    // 🏗️ CONSTRUCTOR: Inisialisasi dengan validasi
    // ═══════════════════════════════════════════
    public BankAccount(String namaPemilik, String pin, String nomorRekening) {
        setNamaPemilik(namaPemilik);
        setPin(pin);
        this.nomorRekening = nomorRekening;
        this.saldo = 0;
    }

    // ═══════════════════════════════════════════
    // 🔓 GETTER: Membaca data (read-only)
    // ═══════════════════════════════════════════

    public String getNamaPemilik() {
        return namaPemilik;
    }

    public double getSaldo() {
        return saldo;
    }

    /**
     * Nomor rekening di-mask untuk keamanan.
     * "1234567890" → "******7890"
     */
    public String getNomorRekening() {
        int panjang = nomorRekening.length();
        String akhir = nomorRekening.substring(panjang - 4);
        return "******" + akhir;
    }

    // ═══════════════════════════════════════════
    // 🔓 SETTER: Mengubah data (dengan validasi)
    // ═══════════════════════════════════════════

    public void setNamaPemilik(String namaPemilik) {
        if (namaPemilik == null || namaPemilik.trim().length() < 3) {
            throw new IllegalArgumentException(
                "❌ Nama pemilik harus minimal 3 karakter!"
            );
        }
        this.namaPemilik = namaPemilik.trim();
    }

    public void setPin(String pin) {
        if (pin == null || !pin.matches("\\d{6}")) {
            throw new IllegalArgumentException(
                "❌ PIN harus terdiri dari tepat 6 digit angka!"
            );
        }
        this.pin = pin;
    }

    // ═══════════════════════════════════════════
    // 💰 BUSINESS LOGIC: Operasi dengan validasi
    // ═══════════════════════════════════════════

    public void setor(double jumlah) {
        if (jumlah <= 0) {
            throw new IllegalArgumentException(
                "❌ Jumlah setoran harus lebih dari 0!"
            );
        }
        saldo += jumlah;
        System.out.println("✅ Setor Rp " + formatRupiah(jumlah) + " berhasil.");
        tampilkanSaldo();
    }

    public void tarik(double jumlah, String pinInput) {
        if (!verifikasiPin(pinInput)) {
            System.out.println("❌ PIN salah! Transaksi dibatalkan.");
            return;
        }

        if (jumlah <= 0) {
            throw new IllegalArgumentException(
                "❌ Jumlah penarikan harus lebih dari 0!"
            );
        }

        if (jumlah > saldo) {
            System.out.println("❌ Saldo tidak mencukupi!");
            System.out.println("   Saldo saat ini: Rp " + formatRupiah(saldo));
            return;
        }

        saldo -= jumlah;
        System.out.println("✅ Tarik Rp " + formatRupiah(jumlah) + " berhasil.");
        tampilkanSaldo();
    }

    // ═══════════════════════════════════════════
    // 🔒 PRIVATE HELPER: Tidak bisa diakses dari luar
    // ═══════════════════════════════════════════

    private boolean verifikasiPin(String pinInput) {
        return this.pin.equals(pinInput);
    }

    private String formatRupiah(double jumlah) {
        return String.format("%,.0f", jumlah);
    }

    private void tampilkanSaldo() {
        System.out.println("   Saldo saat ini: Rp " + formatRupiah(saldo));
    }

    // ═══════════════════════════════════════════
    // ▶️ MAIN: Demonstrasi
    // ═══════════════════════════════════════════
    public static void main(String[] args) {
        System.out.println("══════════════════════════════════════");
        System.out.println("  DEMO ENCAPSULATION — REKENING BANK  ");
        System.out.println("══════════════════════════════════════\n");

        BankAccount akun = new BankAccount("Budi Santoso", "123456", "9876543210");

        System.out.println("Pemilik : " + akun.getNamaPemilik());
        System.out.println("No. Rek : " + akun.getNomorRekening());
        System.out.println();

        akun.setor(5000000);
        System.out.println();

        akun.tarik(1500000, "123456");
        System.out.println();

        akun.tarik(500000, "000000");
        System.out.println();

        // akun.saldo = -999;        // ❌ COMPILE ERROR! saldo is private
        // akun.pin = "hack";        // ❌ COMPILE ERROR! pin is private

        System.out.println("══════════════════════════════════════");
    }
}
