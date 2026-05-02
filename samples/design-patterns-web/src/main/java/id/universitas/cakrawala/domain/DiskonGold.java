package id.universitas.cakrawala.domain;

public class DiskonGold implements StrategiDiskon {
    @Override
    public double hitungDiskon(double harga) {
        return harga * 0.15;
    }

    @Override
    public String getNamaDiskon() {
        return "Member Gold";
    }

    @Override
    public String getPersentase() {
        return "15%";
    }
}
