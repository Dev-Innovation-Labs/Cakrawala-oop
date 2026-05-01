package id.universitas.cakrawala.domain;

import java.time.LocalDate;

/**
 * Status enum untuk Pengajuan Cuti
 */
public enum LeaveStatus {
    DRAFT("Draf", "#gray"),
    DIAJUKAN("Diajukan", "#blue"),
    DISETUJUI_ATASAN("Disetujui Atasan", "#cyan"),
    DISETUJUI_HR("Disetujui HR", "#green"),
    DITOLAK("Ditolak", "#red"),
    DIBATALKAN("Dibatalkan", "#gray");

    private final String label;
    private final String color;

    LeaveStatus(String label, String color) {
        this.label = label;
        this.color = color;
    }

    public String getLabel() {
        return label;
    }

    public String getColor() {
        return color;
    }
}
