/**
 * ═══════════════════════════════════════════════════════════════════
 *  BAB I: ENCAPSULATION — Studi Kasus Purchase Order SAP OData
 * ═══════════════════════════════════════════════════════════════════
 *
 *  @author  Wahyu Amaldi, M.Kom
 *  @institution Universitas Cakrawala
 *
 *  Mendemonstrasikan:
 *  • Private fields untuk data PO (enkapsulasi data SAP)
 *  • Getter & Setter dengan validasi bisnis
 *  • Method private sebagai helper internal
 *  • Kontrol akses terhadap data sensitif (credentials, amount)
 *
 *  Data Real SAP OData:
 *    PO Number  : 4500000004
 *    Service    : MM_PUR_POITEMS_MONI_SRV
 *    Endpoint   : https://sap.ilmuprogram.com/sap/opu/odata/sap/
 *
 *  Compile & Run:
 *    javac 01_Encapsulation_PurchaseOrder.java
 *    java PurchaseOrder
 * ═══════════════════════════════════════════════════════════════════
 */

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

class PurchaseOrder {

    // ═══════════════════════════════════════════
    // 🔒 PRIVATE: Data tersembunyi dari luar
    //    Sama seperti field di SAP tidak bisa
    //    diakses langsung tanpa authorization
    // ═══════════════════════════════════════════
    private String purchaseOrderNumber;     // PurchaseOrder: "4500000004"
    private String purchaseOrderType;       // PurchaseOrderType: "NB"
    private String supplier;                // Supplier: "17300001"
    private String supplierName;            // SupplierName: "Wahyu Amaldi (Domestic Supplier)"
    private String companyCode;             // CompanyCode: "1710"
    private String companyCodeName;         // CompanyCodeName: "Andi Coffee"
    private String purchasingOrganization;  // PurchasingOrganization: "1710"
    private String documentCurrency;        // DocumentCurrency: "USD"
    private String itemText;                // PurchaseOrderItemText: "PEMBELIAN LAPTOP"
    private String materialGroup;           // MaterialGroup: "YBFA08"
    private String materialGroupName;       // MaterialGroupName: "Computer Hardware"
    private String plant;                   // Plant: "1710"
    private String plantName;               // PlantName: "Coffee Plant – Jakarta"
    private double orderQuantity;           // OrderQuantity: 10
    private double netPriceAmount;          // NetPriceAmount: 302.00
    private double netAmount;               // NetAmount: 3020.00
    private String orderPriceUnit;          // OrderPriceUnit: "PC"
    private LocalDate purchaseOrderDate;    // PurchaseOrderDate
    private String createdByUser;           // CreatedByUser: "BUDILUHUR"
    private boolean isCompletelyDelivered;  // IsCompletelyDelivered
    private boolean isFinallyInvoiced;      // IsFinallyInvoiced

    // 🔒 PRIVATE: Credentials SAP (sangat sensitif!)
    private String sapUsername;
    private String sapPassword;

    // ═══════════════════════════════════════════
    // 🏗️ CONSTRUCTOR: Inisialisasi dengan validasi
    // ═══════════════════════════════════════════
    public PurchaseOrder(String poNumber, String poType, String supplier, String companyCode) {
        setPurchaseOrderNumber(poNumber);
        setPurchaseOrderType(poType);
        setSupplier(supplier);
        setCompanyCode(companyCode);
        this.documentCurrency = "USD";
        this.purchaseOrderDate = LocalDate.now();
    }

    // ═══════════════════════════════════════════
    // 🔓 GETTER: Membaca data (read-only access)
    // ═══════════════════════════════════════════

    public String getPurchaseOrderNumber() {
        return purchaseOrderNumber;
    }

    public String getPurchaseOrderType() {
        return purchaseOrderType;
    }

    /**
     * Mendapatkan nama tipe PO yang human-readable.
     * Encapsulation: konversi kode internal ke label.
     */
    public String getPurchaseOrderTypeName() {
        return switch (purchaseOrderType) {
            case "NB" -> "Standard PO";
            case "FO" -> "Framework Order";
            case "UB" -> "Stock Transfer Order";
            case "EC" -> "External Service";
            default -> "Unknown (" + purchaseOrderType + ")";
        };
    }

    public String getSupplier() {
        return supplier;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public String getCompanyCodeName() {
        return companyCodeName;
    }

    public String getItemText() {
        return itemText;
    }

    public double getOrderQuantity() {
        return orderQuantity;
    }

    public double getNetPriceAmount() {
        return netPriceAmount;
    }

    /**
     * Net Amount dihitung otomatis: Qty × Price.
     * Encapsulation: logika perhitungan tersembunyi.
     */
    public double getNetAmount() {
        return orderQuantity * netPriceAmount;
    }

    public String getDocumentCurrency() {
        return documentCurrency;
    }

    public String getPlantName() {
        return plantName;
    }

    public boolean isCompletelyDelivered() {
        return isCompletelyDelivered;
    }

    public boolean isFinallyInvoiced() {
        return isFinallyInvoiced;
    }

    /**
     * Menampilkan password SAP yang di-mask.
     * Encapsulation: data sensitif TIDAK pernah ditampilkan.
     */
    public String getSapCredentialsMasked() {
        if (sapUsername == null) return "[Not Set]";
        return sapUsername.substring(0, 3) + "***" + " / ********";
    }

    // ═══════════════════════════════════════════
    // 🔓 SETTER: Mengubah data (dengan validasi)
    // ═══════════════════════════════════════════

    public void setPurchaseOrderNumber(String poNumber) {
        if (poNumber == null || !poNumber.matches("\\d{10}")) {
            throw new IllegalArgumentException(
                "❌ PO Number harus 10 digit angka! (contoh: 4500000004)"
            );
        }
        this.purchaseOrderNumber = poNumber;
    }

    public void setPurchaseOrderType(String poType) {
        String[] validTypes = {"NB", "FO", "UB", "EC", "MK", "WK"};
        boolean valid = false;
        for (String t : validTypes) {
            if (t.equals(poType)) { valid = true; break; }
        }
        if (!valid) {
            throw new IllegalArgumentException(
                "❌ PO Type tidak valid! Gunakan: NB, FO, UB, EC, MK, WK"
            );
        }
        this.purchaseOrderType = poType;
    }

    public void setSupplier(String supplier) {
        if (supplier == null || supplier.trim().isEmpty()) {
            throw new IllegalArgumentException("❌ Supplier tidak boleh kosong!");
        }
        this.supplier = supplier;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public void setCompanyCode(String companyCode) {
        if (companyCode == null || !companyCode.matches("\\d{4}")) {
            throw new IllegalArgumentException(
                "❌ Company Code harus 4 digit! (contoh: 1710)"
            );
        }
        this.companyCode = companyCode;
    }

    public void setCompanyCodeName(String name) {
        this.companyCodeName = name;
    }

    public void setItemText(String itemText) {
        if (itemText == null || itemText.trim().length() < 3) {
            throw new IllegalArgumentException(
                "❌ Item text minimal 3 karakter!"
            );
        }
        this.itemText = itemText.trim().toUpperCase();
    }

    public void setOrderQuantity(double qty) {
        if (qty <= 0) {
            throw new IllegalArgumentException(
                "❌ Order Quantity harus lebih dari 0!"
            );
        }
        this.orderQuantity = qty;
    }

    public void setNetPriceAmount(double price) {
        if (price < 0) {
            throw new IllegalArgumentException(
                "❌ Net Price tidak boleh negatif!"
            );
        }
        this.netPriceAmount = price;
    }

    public void setPlant(String plant) {
        this.plant = plant;
    }

    public void setPlantName(String plantName) {
        this.plantName = plantName;
    }

    public void setMaterialGroup(String materialGroup, String materialGroupName) {
        this.materialGroup = materialGroup;
        this.materialGroupName = materialGroupName;
    }

    public void setOrderPriceUnit(String unit) {
        this.orderPriceUnit = unit;
    }

    public void setDeliveryStatus(boolean delivered) {
        this.isCompletelyDelivered = delivered;
    }

    public void setInvoiceStatus(boolean invoiced) {
        this.isFinallyInvoiced = invoiced;
    }

    /**
     * Set kredensial SAP — disimpan private, tidak bisa dibaca langsung.
     */
    public void setSapCredentials(String username, String password) {
        if (username == null || password == null) {
            throw new IllegalArgumentException("❌ Credentials tidak boleh null!");
        }
        this.sapUsername = username;
        this.sapPassword = password;
    }

    // ═══════════════════════════════════════════
    // 🔒 PRIVATE METHOD: Helper internal
    // ═══════════════════════════════════════════

    /**
     * Format angka ke format currency.
     * Private: hanya digunakan internal untuk display.
     */
    private String formatCurrency(double amount) {
        return String.format("%,.2f %s", amount, documentCurrency);
    }

    /**
     * Validasi apakah PO bisa di-post berdasarkan business rules.
     * Private: logika bisnis internal.
     */
    private boolean isReadyToPost() {
        return purchaseOrderNumber != null
            && supplier != null
            && orderQuantity > 0
            && netPriceAmount > 0
            && itemText != null;
    }

    /**
     * Generate OData URL untuk mengakses PO ini.
     * Private: URL internal tidak perlu diketahui user.
     */
    private String buildODataUrl() {
        return "https://sap.ilmuprogram.com/sap/opu/odata/sap/"
             + "C_PURCHASEORDER_FS_SRV/I_PurchaseOrder('"
             + purchaseOrderNumber + "')";
    }

    // ═══════════════════════════════════════════
    // 📋 PUBLIC METHOD: Aksi yang bisa dilakukan
    // ═══════════════════════════════════════════

    /**
     * Menampilkan ringkasan PO (menggunakan private helpers).
     */
    public void displaySummary() {
        System.out.println("╔═══════════════════════════════════════════════════╗");
        System.out.println("║       📋 PURCHASE ORDER SUMMARY                  ║");
        System.out.println("╠═══════════════════════════════════════════════════╣");
        System.out.printf("║ PO Number     : %-32s ║%n", purchaseOrderNumber);
        System.out.printf("║ PO Type       : %-32s ║%n", getPurchaseOrderTypeName());
        System.out.printf("║ Supplier      : %-32s ║%n", supplier + " - " + (supplierName != null ? supplierName : ""));
        System.out.printf("║ Company       : %-32s ║%n", companyCode + " - " + (companyCodeName != null ? companyCodeName : ""));
        System.out.printf("║ Plant         : %-32s ║%n", (plantName != null ? plantName : plant));
        System.out.println("╠═══════════════════════════════════════════════════╣");
        System.out.printf("║ Item          : %-32s ║%n", (itemText != null ? itemText : "-"));
        System.out.printf("║ Material Grp  : %-32s ║%n", (materialGroupName != null ? materialGroupName : "-"));
        System.out.printf("║ Quantity      : %-32s ║%n", orderQuantity + " " + (orderPriceUnit != null ? orderPriceUnit : ""));
        System.out.printf("║ Unit Price    : %-32s ║%n", formatCurrency(netPriceAmount));
        System.out.printf("║ Net Amount    : %-32s ║%n", formatCurrency(getNetAmount()));
        System.out.println("╠═══════════════════════════════════════════════════╣");
        System.out.printf("║ Delivered     : %-32s ║%n", isCompletelyDelivered ? "✅ Yes" : "❌ No");
        System.out.printf("║ Invoiced      : %-32s ║%n", isFinallyInvoiced ? "✅ Yes" : "❌ No");
        System.out.printf("║ Ready to Post : %-32s ║%n", isReadyToPost() ? "✅ Yes" : "❌ No");
        System.out.println("╠═══════════════════════════════════════════════════╣");
        System.out.printf("║ SAP Cred      : %-32s ║%n", getSapCredentialsMasked());
        System.out.printf("║ OData URL     : %-32s ║%n", "(tersembunyi - private)");
        System.out.println("╚═══════════════════════════════════════════════════╝");
    }

    // ═══════════════════════════════════════════
    //  MAIN: Demo penggunaan encapsulation
    // ═══════════════════════════════════════════
    public static void main(String[] args) {
        System.out.println("═══════════════════════════════════════════════════");
        System.out.println(" BAB I: ENCAPSULATION — Purchase Order SAP OData");
        System.out.println("═══════════════════════════════════════════════════\n");

        // ── Membuat PO dengan data dari SAP ──
        PurchaseOrder po = new PurchaseOrder("4500000004", "NB", "17300001", "1710");
        po.setSupplierName("Wahyu Amaldi (Domestic Supplier)");
        po.setCompanyCodeName("Andi Coffee");
        po.setItemText("PEMBELIAN LAPTOP");
        po.setMaterialGroup("YBFA08", "Computer Hardware");
        po.setPlant("1710");
        po.setPlantName("Coffee Plant – Jakarta");
        po.setOrderQuantity(10);
        po.setNetPriceAmount(302.00);
        po.setOrderPriceUnit("PC");
        po.setDeliveryStatus(true);
        po.setInvoiceStatus(false);
        po.setSapCredentials("wahyu.amaldi", "secretPassword");

        // ── Tampilkan PO ──
        po.displaySummary();

        // ── Demo: Mengakses data via getter (aman) ──
        System.out.println("\n🔓 Akses via Getter (public):");
        System.out.println("   PO Number  : " + po.getPurchaseOrderNumber());
        System.out.println("   Supplier   : " + po.getSupplierName());
        System.out.println("   Net Amount : " + po.getNetAmount() + " " + po.getDocumentCurrency());

        // ── Demo: Data sensitif ter-mask ──
        System.out.println("\n🔒 Data sensitif (ter-enkapsulasi):");
        System.out.println("   SAP Cred   : " + po.getSapCredentialsMasked());
        System.out.println("   (Password & OData URL tidak bisa diakses dari luar!)");

        // ── Demo: Validasi setter ──
        System.out.println("\n⚠️  Demo Validasi Setter:");
        try {
            po.setOrderQuantity(-5);
        } catch (IllegalArgumentException e) {
            System.out.println("   " + e.getMessage());
        }
        try {
            po.setPurchaseOrderNumber("123");
        } catch (IllegalArgumentException e) {
            System.out.println("   " + e.getMessage());
        }
        try {
            po.setPurchaseOrderType("XX");
        } catch (IllegalArgumentException e) {
            System.out.println("   " + e.getMessage());
        }
    }
}
