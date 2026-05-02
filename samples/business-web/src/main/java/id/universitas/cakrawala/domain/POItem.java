package id.universitas.cakrawala.domain;

/**
 * VALUE OBJECT: POItem
 * Objek tidak dapat diubah (immutable) yang mewakili item dalam PO
 */
public class POItem {
    private final String namaBarang;
    private final int jumlah;
    private final double hargaSatuan;

    public POItem(String namaBarang, int jumlah, double hargaSatuan) {
        if (namaBarang == null || namaBarang.isBlank())
            throw new IllegalArgumentException("Nama barang wajib diisi");
        if (jumlah <= 0)
            throw new IllegalArgumentException("Jumlah harus > 0");
        if (hargaSatuan <= 0)
            throw new IllegalArgumentException("Harga wajib > 0");
        this.namaBarang = namaBarang;
        this.jumlah = jumlah;
        this.hargaSatuan = hargaSatuan;
    }

    public double getSubtotal() { 
        return jumlah * hargaSatuan; 
    }

    public String getNamaBarang() { return namaBarang; }
    public int getJumlah() { return jumlah; }
    public double getHargaSatuan() { return hargaSatuan; }

    @Override
    public String toString() {
        return String.format("%-30s x%-3d @ %,.0f = %,.0f",
            namaBarang, jumlah, hargaSatuan, getSubtotal());
    }
}
