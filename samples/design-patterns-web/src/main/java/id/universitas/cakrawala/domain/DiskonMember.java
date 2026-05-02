package id.universitas.cakrawala.domain;

public class DiskonMember implements StrategiDiskon {
    @Override
    public double hitungDiskon(double harga) {
        return harga * 0.05;
    }

    @Override
    public String getNamaDiskon() {
        return "Member Biasa";
    }

    @Override
    public String getPersentase() {
        return "5%";
    }
}
