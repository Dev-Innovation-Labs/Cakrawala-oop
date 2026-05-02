package id.universitas.cakrawala.domain;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * DTO untuk hasil transaksi pembayaran
 */
public class TransaksiResult {
    private String item;
    private double harga;
    private String namaDiskon;
    private String persentaseDiskon;
    private double nilaiDiskon;
    private double totalBayar;

    public TransaksiResult(String item, double harga, String namaDiskon, 
                          String persentaseDiskon, double nilaiDiskon, double totalBayar) {
        this.item = item;
        this.harga = harga;
        this.namaDiskon = namaDiskon;
        this.persentaseDiskon = persentaseDiskon;
        this.nilaiDiskon = nilaiDiskon;
        this.totalBayar = totalBayar;
    }

    // Getters dengan format Rupiah
    public String getItem() { return item; }
    public String getHargaFormat() { 
        return NumberFormat.getCurrencyInstance(new Locale("id", "ID")).format(harga);
    }
    public String getNamaDiskon() { return namaDiskon; }
    public String getPersentaseDiskon() { return persentaseDiskon; }
    public String getNilaiDiskonFormat() { 
        return NumberFormat.getCurrencyInstance(new Locale("id", "ID")).format(nilaiDiskon);
    }
    public String getTotalBayarFormat() { 
        return NumberFormat.getCurrencyInstance(new Locale("id", "ID")).format(totalBayar);
    }

    // Raw values
    public double getHarga() { return harga; }
    public double getNilaiDiskon() { return nilaiDiskon; }
    public double getTotalBayar() { return totalBayar; }
}
