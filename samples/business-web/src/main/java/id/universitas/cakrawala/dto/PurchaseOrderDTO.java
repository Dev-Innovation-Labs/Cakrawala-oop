package id.universitas.cakrawala.dto;

import id.universitas.cakrawala.domain.POItem;
import id.universitas.cakrawala.domain.PurchaseOrderBO;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * DTO untuk menampilkan Purchase Order di view
 */
public class PurchaseOrderDTO {
    private String nomorPO;
    private String vendor;
    private String tanggal;
    private String status;
    private List<POItem> items;
    private double total;
    private String totalFormat;
    private boolean perluApproval;
    private String approver;

    public PurchaseOrderDTO(PurchaseOrderBO po) {
        this.nomorPO = po.getNomorPO();
        this.vendor = po.getVendor();
        this.tanggal = po.getTanggal().toString();
        this.status = po.getStatus();
        this.items = po.getItems();
        this.total = po.hitungTotal();
        this.totalFormat = formatRupiah(total);
        this.perluApproval = po.perluApproval();
        this.approver = po.getApprover();
    }

    private String formatRupiah(double amount) {
        return NumberFormat.getCurrencyInstance(new Locale("id", "ID")).format(amount);
    }

    // Getters
    public String getNomorPO() { return nomorPO; }
    public String getVendor() { return vendor; }
    public String getTanggal() { return tanggal; }
    public String getStatus() { return status; }
    public List<POItem> getItems() { return items; }
    public double getTotal() { return total; }
    public String getTotalFormat() { return totalFormat; }
    public boolean isPerluApproval() { return perluApproval; }
    public String getApprover() { return approver; }
    
    public String getStatusBadge() {
        return switch(status) {
            case "DRAFT" -> "warning";
            case "MENUNGGU_APPROVAL" -> "info";
            case "DISETUJUI" -> "success";
            default -> "secondary";
        };
    }

    public String getStatusLabel() {
        return switch(status) {
            case "DRAFT" -> "Draft";
            case "MENUNGGU_APPROVAL" -> "Menunggu Approval";
            case "DISETUJUI" -> "Disetujui";
            default -> status;
        };
    }
}
