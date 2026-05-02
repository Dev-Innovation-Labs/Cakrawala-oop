package id.universitas.cakrawala.domain;

public class DiskonHarbolnas implements StrategiDiskon {
    @Override
    public double hitungDiskon(double harga) {
        return harga * 0.30;
    }

    @Override
    public String getNamaDiskon() {
        return "Harbolnas Special";
    }

    @Override
    public String getPersentase() {
        return "30%";
    }
}
