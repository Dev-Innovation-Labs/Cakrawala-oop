/**
 * ═══════════════════════════════════════════════════════════════════
 *  BAB II: INHERITANCE — Hierarki Dokumen SAP OData
 * ═══════════════════════════════════════════════════════════════════
 *
 *  @author  Wahyu Amaldi, M.Kom
 *  @institution Universitas Cakrawala
 *
 *  Mendemonstrasikan:
 *  • Superclass SAPDocument sebagai parent
 *  • Subclass PurchaseOrder & PurchaseRequisition mewarisi
 *  • Override method untuk perilaku spesifik
 *  • Keyword: extends, super, @Override
 *  • Reuse kode (DRY principle)
 *
 *  Data Real SAP OData:
 *    PO Number  : 4500000004
 *    Service    : C_PURCHASEORDER_FS_SRV / MM_PUR_POITEMS_MONI_SRV
 *    Endpoint   : https://sap.ilmuprogram.com/sap/opu/odata/sap/
 *
 *  Compile & Run:
 *    javac 02_Inheritance_SAPDocument.java
 *    java SAPDocumentDemo
 * ═══════════════════════════════════════════════════════════════════
 */

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

// ═══════════════════════════════════════════════════════════
//  SUPERCLASS: SAPDocument — Dokumen dasar SAP
//  Semua dokumen SAP punya field ini (PO, PR, Contract, dll)
// ═══════════════════════════════════════════════════════════

class SAPDocument {

    // Field yang diwariskan ke semua subclass
    protected String documentNumber;
    protected String documentType;
    protected String companyCode;
    protected String companyCodeName;
    protected String purchasingOrganization;
    protected String purchasingGroup;
    protected String createdByUser;
    protected LocalDate creationDate;
    protected String documentCurrency;

    // OData connection info (diwariskan)
    protected String odataServiceName;
    protected String odataEntitySet;

    public SAPDocument(String documentNumber, String documentType, String companyCode) {
        this.documentNumber = documentNumber;
        this.documentType = documentType;
        this.companyCode = companyCode;
        this.creationDate = LocalDate.now();
        this.documentCurrency = "USD";
    }

    /**
     * Method yang bisa di-override oleh subclass.
     * Setiap dokumen SAP punya cara display berbeda.
     */
    public String getDocumentDescription() {
        return "SAP Document " + documentNumber;
    }

    /**
     * Build OData URL — shared logic, bisa di-override.
     */
    public String buildODataUrl(String baseUrl) {
        return baseUrl + "/sap/opu/odata/sap/" + odataServiceName
             + "/" + odataEntitySet + "('" + documentNumber + "')";
    }

    /**
     * Kalkulasi — default: tidak ada kalkulasi.
     */
    public double calculateTotal() {
        return 0.0;
    }

    /**
     * Status dokumen — default implementation.
     */
    public String getStatus() {
        return "Created";
    }

    /**
     * Display info dasar dokumen.
     */
    public void displayBasicInfo() {
        System.out.println("┌─────────────────────────────────────────────┐");
        System.out.println("│ " + getDocumentDescription());
        System.out.println("├─────────────────────────────────────────────┤");
        System.out.printf("│ Doc Number : %-30s │%n", documentNumber);
        System.out.printf("│ Doc Type   : %-30s │%n", documentType);
        System.out.printf("│ Company    : %-30s │%n", companyCode + (companyCodeName != null ? " - " + companyCodeName : ""));
        System.out.printf("│ Created By : %-30s │%n", createdByUser != null ? createdByUser : "-");
        System.out.printf("│ Created On : %-30s │%n", creationDate.format(DateTimeFormatter.ISO_DATE));
        System.out.printf("│ Currency   : %-30s │%n", documentCurrency);
        System.out.printf("│ Status     : %-30s │%n", getStatus());
        System.out.println("└─────────────────────────────────────────────┘");
    }
}

// ═══════════════════════════════════════════════════════════
//  SUBCLASS 1: SAPPurchaseOrder — Mewarisi SAPDocument
//  Menambahkan field & behavior spesifik Purchase Order
// ═══════════════════════════════════════════════════════════

class SAPPurchaseOrder extends SAPDocument {

    // Field tambahan khusus PO (tidak ada di superclass)
    private String supplier;
    private String supplierName;
    private String itemText;
    private String materialGroupName;
    private String plantName;
    private double orderQuantity;
    private double netPriceAmount;
    private String orderPriceUnit;
    private boolean isCompletelyDelivered;
    private boolean isFinallyInvoiced;

    public SAPPurchaseOrder(String poNumber, String poType, String companyCode,
                            String supplier, String supplierName) {
        // Memanggil constructor parent (SAPDocument)
        super(poNumber, poType, companyCode);  // ← keyword 'super'

        this.supplier = supplier;
        this.supplierName = supplierName;

        // Set OData service spesifik untuk PO
        this.odataServiceName = "C_PURCHASEORDER_FS_SRV";
        this.odataEntitySet = "I_PurchaseOrder";
    }

    // Setter untuk field tambahan
    public void setItemDetails(String itemText, String materialGroupName,
                                double quantity, double price, String unit) {
        this.itemText = itemText;
        this.materialGroupName = materialGroupName;
        this.orderQuantity = quantity;
        this.netPriceAmount = price;
        this.orderPriceUnit = unit;
    }

    public void setPlantName(String plantName) {
        this.plantName = plantName;
    }

    public void setDeliveryStatus(boolean delivered, boolean invoiced) {
        this.isCompletelyDelivered = delivered;
        this.isFinallyInvoiced = invoiced;
    }

    // ── Override methods dari parent ──

    @Override
    public String getDocumentDescription() {
        return "📋 Purchase Order: " + documentNumber + " (" + getPOTypeName() + ")";
    }

    @Override
    public double calculateTotal() {
        return orderQuantity * netPriceAmount;  // PO punya kalkulasi Qty × Price
    }

    @Override
    public String getStatus() {
        if (isCompletelyDelivered && isFinallyInvoiced) return "✅ Completed";
        if (isCompletelyDelivered) return "📦 Delivered (Pending Invoice)";
        return "⏳ Open";
    }

    /**
     * Method baru khusus PO (tidak ada di parent).
     */
    public String getPOTypeName() {
        return switch (documentType) {
            case "NB" -> "Standard PO";
            case "FO" -> "Framework Order";
            case "UB" -> "Stock Transfer Order";
            default -> documentType;
        };
    }

    /**
     * Display detail khusus PO (memanggil parent + tambahan).
     */
    public void displayFullDetails() {
        // Panggil method parent dulu
        super.displayBasicInfo();

        // Tambahkan detail khusus PO
        System.out.println("┌─── PO Details ───────────────────────────────┐");
        System.out.printf("│ Supplier   : %-30s │%n", supplier + " - " + supplierName);
        System.out.printf("│ Plant      : %-30s │%n", plantName != null ? plantName : "-");
        System.out.printf("│ Item       : %-30s │%n", itemText != null ? itemText : "-");
        System.out.printf("│ Mat. Group : %-30s │%n", materialGroupName != null ? materialGroupName : "-");
        System.out.printf("│ Quantity   : %-30s │%n", orderQuantity + " " + (orderPriceUnit != null ? orderPriceUnit : ""));
        System.out.printf("│ Unit Price : %-30s │%n", String.format("%,.2f %s", netPriceAmount, documentCurrency));
        System.out.printf("│ Total      : %-30s │%n", String.format("%,.2f %s", calculateTotal(), documentCurrency));
        System.out.printf("│ Delivered  : %-30s │%n", isCompletelyDelivered ? "✅ Yes" : "❌ No");
        System.out.printf("│ Invoiced   : %-30s │%n", isFinallyInvoiced ? "✅ Yes" : "❌ No");
        System.out.println("└──────────────────────────────────────────────┘");
    }
}

// ═══════════════════════════════════════════════════════════
//  SUBCLASS 2: SAPPurchaseRequisition — Juga mewarisi SAPDocument
//  Dokumen PR berbeda behavior dari PO
// ═══════════════════════════════════════════════════════════

class SAPPurchaseRequisition extends SAPDocument {

    private String requisitioner;
    private String itemText;
    private double requestedQuantity;
    private double estimatedPrice;
    private String requestPriceUnit;
    private boolean isApproved;
    private boolean isConvertedToPO;
    private String linkedPONumber;

    public SAPPurchaseRequisition(String prNumber, String companyCode,
                                  String requisitioner) {
        super(prNumber, "NB", companyCode);
        this.requisitioner = requisitioner;

        // OData service PR berbeda dari PO!
        this.odataServiceName = "MM_PUR_PR_PROCESS_SRV";
        this.odataEntitySet = "C_PurchaseRequisition";
    }

    public void setItemDetails(String itemText, double quantity, double price, String unit) {
        this.itemText = itemText;
        this.requestedQuantity = quantity;
        this.estimatedPrice = price;
        this.requestPriceUnit = unit;
    }

    public void setApprovalStatus(boolean approved) {
        this.isApproved = approved;
    }

    public void convertToPO(String poNumber) {
        if (!isApproved) {
            System.out.println("❌ PR belum di-approve! Tidak bisa dikonversi ke PO.");
            return;
        }
        this.isConvertedToPO = true;
        this.linkedPONumber = poNumber;
        System.out.println("✅ PR " + documentNumber + " berhasil dikonversi ke PO " + poNumber);
    }

    // ── Override methods ──

    @Override
    public String getDocumentDescription() {
        return "📝 Purchase Requisition: " + documentNumber;
    }

    @Override
    public double calculateTotal() {
        return requestedQuantity * estimatedPrice;
    }

    @Override
    public String getStatus() {
        if (isConvertedToPO) return "✅ Converted to PO " + linkedPONumber;
        if (isApproved) return "👍 Approved (Pending PO)";
        return "⏳ Pending Approval";
    }

    public void displayFullDetails() {
        super.displayBasicInfo();
        System.out.println("┌─── PR Details ───────────────────────────────┐");
        System.out.printf("│ Requisitioner : %-27s │%n", requisitioner);
        System.out.printf("│ Item          : %-27s │%n", itemText != null ? itemText : "-");
        System.out.printf("│ Quantity      : %-27s │%n", requestedQuantity + " " + (requestPriceUnit != null ? requestPriceUnit : ""));
        System.out.printf("│ Est. Price    : %-27s │%n", String.format("%,.2f %s", estimatedPrice, documentCurrency));
        System.out.printf("│ Est. Total    : %-27s │%n", String.format("%,.2f %s", calculateTotal(), documentCurrency));
        System.out.printf("│ Approved      : %-27s │%n", isApproved ? "✅ Yes" : "❌ No");
        if (isConvertedToPO) {
            System.out.printf("│ Linked PO     : %-27s │%n", linkedPONumber);
        }
        System.out.println("└──────────────────────────────────────────────┘");
    }
}

// ═══════════════════════════════════════════════════════════
//  MAIN: Demo Inheritance
// ═══════════════════════════════════════════════════════════

public class SAPDocumentDemo {

    public static void main(String[] args) {
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println(" BAB II: INHERITANCE — Hierarki Dokumen SAP OData");
        System.out.println("═══════════════════════════════════════════════════════\n");

        // ── 1. Buat PO dari data SAP real ──
        System.out.println("▶ [1] Purchase Order (dari SAP OData):\n");
        SAPPurchaseOrder po = new SAPPurchaseOrder(
            "4500000004", "NB", "1710",
            "17300001", "Wahyu Amaldi (Domestic Supplier)"
        );
        po.companyCodeName = "Andi Coffee";
        po.createdByUser = "BUDILUHUR";
        po.purchasingOrganization = "1710";
        po.purchasingGroup = "001";
        po.setPlantName("Coffee Plant – Jakarta");
        po.setItemDetails("PEMBELIAN LAPTOP", "Computer Hardware", 10, 302.00, "PC");
        po.setDeliveryStatus(true, false);
        po.displayFullDetails();

        // ── 2. Buat PR yang terkait ──
        System.out.println("\n▶ [2] Purchase Requisition (simulasi):\n");
        SAPPurchaseRequisition pr = new SAPPurchaseRequisition(
            "0010000001", "1710", "Wahyu Amaldi"
        );
        pr.companyCodeName = "Andi Coffee";
        pr.createdByUser = "WAHYU.AMALDI";
        pr.setItemDetails("PEMBELIAN LAPTOP", 10, 300.00, "PC");
        pr.setApprovalStatus(true);
        pr.displayFullDetails();

        // ── 3. Konversi PR ke PO (logika bisnis) ──
        System.out.println("\n▶ [3] Proses Konversi PR → PO:\n");
        pr.convertToPO("4500000004");
        System.out.println("   Status PR: " + pr.getStatus());

        // ── 4. Demo Inheritance: keduanya adalah SAPDocument ──
        System.out.println("\n▶ [4] Polymorphic Array — SAPDocument[]:\n");
        SAPDocument[] documents = { po, pr };
        for (SAPDocument doc : documents) {
            System.out.println("   " + doc.getDocumentDescription());
            System.out.println("   Status   : " + doc.getStatus());
            System.out.printf("   Total    : %,.2f %s%n", doc.calculateTotal(), doc.documentCurrency);
            System.out.println("   OData URL: " + doc.buildODataUrl("https://sap.ilmuprogram.com"));
            System.out.println();
        }
    }
}
