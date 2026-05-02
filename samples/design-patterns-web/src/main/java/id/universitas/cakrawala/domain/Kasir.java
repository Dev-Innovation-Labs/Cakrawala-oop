package id.universitas.cakrawala.domain;

/**
 * Kasir menggunakan Strategy Pattern
 * Strategi diskon bisa diubah saat runtime
 */
public class Kasir {
    private StrategiDiskon strategi;

    public void setStrategi(StrategiDiskon strategi) {
        this.strategi = strategi;
    }

    public TransaksiResult prosesPembayaran(String item, double harga) {
        if (strategi == null) {
            throw new IllegalStateException("Strategi diskon belum di-set!");
        }

        double diskon = strategi.hitungDiskon(harga);
        double total = harga - diskon;

        return new TransaksiResult(item, harga, strategi.getNamaDiskon(), 
                                  strategi.getPersentase(), diskon, total);
    }

    public StrategiDiskon getStrategi() {
        return strategi;
    }
}
