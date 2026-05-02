package id.universitas.cakrawala.domain;

/**
 * STRATEGY PATTERN
 * Interface untuk berbagai strategi diskon yang dapat diganti
 */
public interface StrategiDiskon {
    double hitungDiskon(double harga);
    String getNamaDiskon();
    String getPersentase();
}
