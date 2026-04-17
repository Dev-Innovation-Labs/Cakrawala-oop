/**
 * ═══════════════════════════════════════════════════════════════════
 *  BAB IV: ABSTRACTION — Abstraksi Layanan OData SAP
 * ═══════════════════════════════════════════════════════════════════
 *
 *  @author  Wahyu Amaldi, M.Kom
 *  @institution Universitas Cakrawala
 *
 *  Mendemonstrasikan:
 *  • Abstract class: ODataService (blueprint)
 *  • Interface: Printable, Exportable, Validatable
 *  • Implementasi konkrit yang menyembunyikan detail
 *  • User hanya tahu "APA", bukan "BAGAIMANA"
 *
 *  Data Real SAP OData:
 *    PO Number  : 4500000004
 *    Endpoint   : https://sap.ilmuprogram.com/sap/opu/odata/sap/
 *
 *  Compile & Run:
 *    javac 04_Abstraction_ODataService.java
 *    java ODataServiceDemo
 * ═══════════════════════════════════════════════════════════════════
 */

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Base64;

// ═══════════════════════════════════════════════════════════
//  INTERFACE 1: Printable — "Bisa dicetak"
//  Abstraksi: user tahu "bisa print", tanpa tahu caranya
// ═══════════════════════════════════════════════════════════

interface Printable {
    void printSummary();
    void printDetailed();
}

// ═══════════════════════════════════════════════════════════
//  INTERFACE 2: Exportable — "Bisa diekspor"
// ═══════════════════════════════════════════════════════════

interface Exportable {
    String exportToJson();
    String exportToCsv();
}

// ═══════════════════════════════════════════════════════════
//  INTERFACE 3: Validatable — "Bisa divalidasi"
// ═══════════════════════════════════════════════════════════

interface Validatable {
    boolean isValid();
    List<String> getValidationErrors();
}

// ═══════════════════════════════════════════════════════════
//  ABSTRACT CLASS: ODataService
//  Blueprint untuk semua koneksi OData SAP
//  User cukup panggil fetchData() — detail koneksi tersembunyi
// ═══════════════════════════════════════════════════════════

abstract class ODataService {

    protected String baseUrl;
    protected String username;
    protected String client;

    public ODataService(String baseUrl, String username, String client) {
        this.baseUrl = baseUrl;
        this.username = username;
        this.client = client;
    }

    // ── Abstract methods: WAJIB diimplementasi subclass ──

    /** Nama service OData (setiap subclass beda) */
    abstract String getServiceName();

    /** Entity set yang diakses */
    abstract String getEntitySet();

    /** Proses data yang di-fetch */
    abstract void processResponse(String jsonResponse);

    // ── Concrete methods: Sudah ada implementasi ──

    /**
     * Build URL lengkap OData.
     * User tidak perlu tahu format URL — cukup panggil method ini.
     */
    public String buildServiceUrl() {
        return baseUrl + "/sap/opu/odata/sap/" + getServiceName()
             + "/" + getEntitySet();
    }

    /**
     * Build Authorization header (Basic Auth).
     * ABSTRAKSI: user tidak perlu tahu cara encode Base64.
     */
    protected String buildAuthHeader(String password) {
        String credentials = username + ":" + password;
        return "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes());
    }

    /**
     * Fetch data — template method pattern.
     * Langkah-langkah tersembunyi, user hanya panggil fetchData().
     */
    public void fetchData() {
        System.out.println("   [1/4] 🔗 Connecting to: " + buildServiceUrl());
        System.out.println("   [2/4] 🔑 Authenticating: " + username + "@client=" + client);
        System.out.println("   [3/4] 📥 Fetching data from: " + getEntitySet());

        // Simulasi response
        String simulatedResponse = "{ \"d\": { \"results\": [...] } }";
        processResponse(simulatedResponse);

        System.out.println("   [4/4] ✅ Data processed successfully!");
    }

    /**
     * Informasi koneksi (untuk debugging).
     */
    public void displayConnectionInfo() {
        System.out.println("   Base URL : " + baseUrl);
        System.out.println("   Service  : " + getServiceName());
        System.out.println("   Entity   : " + getEntitySet());
        System.out.println("   User     : " + username);
        System.out.println("   Client   : " + client);
    }
}

// ═══════════════════════════════════════════════════════════
//  CONCRETE CLASS: PurchaseOrderService
//  Implementasi nyata — detail tersembunyi dari user
// ═══════════════════════════════════════════════════════════

class PurchaseOrderService extends ODataService
    implements Printable, Exportable, Validatable {

    // Data PO (diisi setelah fetch)
    private String poNumber;
    private String poType;
    private String poTypeName;
    private String supplier;
    private String supplierName;
    private String companyCode;
    private String companyCodeName;
    private String currency;
    private String paymentTerms;
    private String createdBy;
    private LocalDate creationDate;

    // Item data
    private String itemText;
    private String materialGroup;
    private String materialGroupName;
    private String plant;
    private String plantName;
    private double orderQuantity;
    private double netPriceAmount;
    private String orderPriceUnit;
    private boolean isDelivered;
    private boolean isInvoiced;

    public PurchaseOrderService(String baseUrl, String username, String client) {
        super(baseUrl, username, client);
    }

    /**
     * Load data PO dari SAP OData (simulasi dengan data real).
     * User cukup panggil loadPO("4500000004") — semua detail tersembunyi.
     */
    public void loadPO(String poNumber) {
        this.poNumber = poNumber;
        System.out.println("\n📋 Loading PO " + poNumber + " from SAP...\n");
        fetchData();  // Panggil abstract template method

        // Simulasi data dari response OData (data real dari SAP)
        this.poType = "NB";
        this.poTypeName = "Standard PO";
        this.supplier = "17300001";
        this.supplierName = "Wahyu Amaldi (Domestic Supplier)";
        this.companyCode = "1710";
        this.companyCodeName = "Andi Coffee";
        this.currency = "USD";
        this.paymentTerms = "0004";
        this.createdBy = "BUDILUHUR";
        this.creationDate = LocalDate.of(2025, 10, 19);

        this.itemText = "PEMBELIAN LAPTOP";
        this.materialGroup = "YBFA08";
        this.materialGroupName = "Computer Hardware";
        this.plant = "1710";
        this.plantName = "Coffee Plant – Jakarta";
        this.orderQuantity = 10;
        this.netPriceAmount = 302.00;
        this.orderPriceUnit = "PC";
        this.isDelivered = true;
        this.isInvoiced = false;
    }

    // ── Abstract method implementations ──

    @Override
    String getServiceName() {
        return "C_PURCHASEORDER_FS_SRV";
    }

    @Override
    String getEntitySet() {
        return "I_PurchaseOrder('" + (poNumber != null ? poNumber : "") + "')";
    }

    @Override
    void processResponse(String jsonResponse) {
        System.out.println("   [3.5/4] ⚙️ Parsing PO response JSON...");
        // Detail parsing tersembunyi — user tidak perlu tahu
        // cara parse JSON, mapping field, handle null, dsb.
    }

    // ── Printable interface ──

    @Override
    public void printSummary() {
        System.out.println("╔═══════════════════════════════════════════╗");
        System.out.println("║     📋 PO SUMMARY (Abstracted View)      ║");
        System.out.println("╠═══════════════════════════════════════════╣");
        System.out.printf("║ PO         : %-28s ║%n", poNumber);
        System.out.printf("║ Type       : %-28s ║%n", poTypeName);
        System.out.printf("║ Supplier   : %-28s ║%n", supplierName);
        System.out.printf("║ Company    : %-28s ║%n", companyCodeName);
        System.out.printf("║ Item       : %-28s ║%n", itemText);
        System.out.printf("║ Total      : %-28s ║%n",
            String.format("%,.2f %s", orderQuantity * netPriceAmount, currency));
        System.out.println("╚═══════════════════════════════════════════╝");
    }

    @Override
    public void printDetailed() {
        System.out.println("╔═══════════════════════════════════════════════════╗");
        System.out.println("║         📋 PO DETAILED (Full Abstracted View)     ║");
        System.out.println("╠═══════════════════════════════════════════════════╣");
        System.out.println("║ ── HEADER ──────────────────────────────────────  ║");
        System.out.printf("║ PO Number     : %-32s ║%n", poNumber);
        System.out.printf("║ PO Type       : %-32s ║%n", poType + " (" + poTypeName + ")");
        System.out.printf("║ Supplier      : %-32s ║%n", supplier);
        System.out.printf("║ Supplier Name : %-32s ║%n", supplierName);
        System.out.printf("║ Company Code  : %-32s ║%n", companyCode + " (" + companyCodeName + ")");
        System.out.printf("║ Currency      : %-32s ║%n", currency);
        System.out.printf("║ Payment Terms : %-32s ║%n", paymentTerms);
        System.out.printf("║ Created By    : %-32s ║%n", createdBy);
        System.out.printf("║ Created On    : %-32s ║%n", creationDate.format(DateTimeFormatter.ISO_DATE));
        System.out.println("║ ── ITEM ────────────────────────────────────────  ║");
        System.out.printf("║ Item Text     : %-32s ║%n", itemText);
        System.out.printf("║ Material Grp  : %-32s ║%n", materialGroup + " (" + materialGroupName + ")");
        System.out.printf("║ Plant         : %-32s ║%n", plant + " (" + plantName + ")");
        System.out.printf("║ Quantity      : %-32s ║%n", orderQuantity + " " + orderPriceUnit);
        System.out.printf("║ Net Price     : %-32s ║%n", String.format("%,.2f %s", netPriceAmount, currency));
        System.out.printf("║ Net Amount    : %-32s ║%n", String.format("%,.2f %s", orderQuantity * netPriceAmount, currency));
        System.out.println("║ ── STATUS ──────────────────────────────────────  ║");
        System.out.printf("║ Delivered     : %-32s ║%n", isDelivered ? "✅ Yes" : "❌ No");
        System.out.printf("║ Invoiced      : %-32s ║%n", isInvoiced ? "✅ Yes" : "❌ No");
        System.out.println("║ ── ODATA ───────────────────────────────────────  ║");
        System.out.printf("║ Service       : %-32s ║%n", getServiceName());
        System.out.printf("║ Entity        : %-32s ║%n", "I_PurchaseOrder");
        System.out.println("╚═══════════════════════════════════════════════════╝");
    }

    // ── Exportable interface ──

    @Override
    public String exportToJson() {
        return String.format("""
            {
              "PurchaseOrder": "%s",
              "PurchaseOrderType": "%s",
              "Supplier": "%s",
              "SupplierName": "%s",
              "CompanyCode": "%s",
              "CompanyCodeName": "%s",
              "DocumentCurrency": "%s",
              "PurchaseOrderItemText": "%s",
              "MaterialGroup": "%s",
              "Plant": "%s",
              "OrderQuantity": %.0f,
              "NetPriceAmount": %.2f,
              "NetAmount": %.2f,
              "IsCompletelyDelivered": %s,
              "IsFinallyInvoiced": %s
            }""",
            poNumber, poType, supplier, supplierName,
            companyCode, companyCodeName, currency,
            itemText, materialGroup, plant,
            orderQuantity, netPriceAmount, orderQuantity * netPriceAmount,
            isDelivered, isInvoiced);
    }

    @Override
    public String exportToCsv() {
        String header = "PurchaseOrder,Type,Supplier,SupplierName,CompanyCode,"
                       + "Item,Qty,Price,Total,Currency,Delivered,Invoiced";
        String data = String.format("%s,%s,%s,%s,%s,%s,%.0f,%.2f,%.2f,%s,%s,%s",
            poNumber, poType, supplier, supplierName, companyCode,
            itemText, orderQuantity, netPriceAmount, orderQuantity * netPriceAmount,
            currency, isDelivered, isInvoiced);
        return header + "\n" + data;
    }

    // ── Validatable interface ──

    @Override
    public boolean isValid() {
        return getValidationErrors().isEmpty();
    }

    @Override
    public List<String> getValidationErrors() {
        List<String> errors = new ArrayList<>();
        if (poNumber == null || poNumber.isEmpty()) errors.add("PO Number is required");
        if (supplier == null || supplier.isEmpty()) errors.add("Supplier is required");
        if (companyCode == null || companyCode.isEmpty()) errors.add("Company Code is required");
        if (orderQuantity <= 0) errors.add("Quantity must be > 0");
        if (netPriceAmount < 0) errors.add("Price cannot be negative");
        return errors;
    }
}

// ═══════════════════════════════════════════════════════════
//  MAIN: Demo Abstraction
// ═══════════════════════════════════════════════════════════

public class ODataServiceDemo {

    /**
     * Method yang hanya tahu interface, bukan implementasi.
     * ABSTRAKSI MURNI: tidak peduli class mana yang masuk.
     */
    static void printDocument(Printable doc) {
        doc.printSummary();
    }

    static void exportDocument(Exportable doc) {
        System.out.println(doc.exportToJson());
    }

    static void validateDocument(Validatable doc) {
        if (doc.isValid()) {
            System.out.println("   ✅ Dokumen valid!");
        } else {
            System.out.println("   ❌ Validation errors:");
            for (String err : doc.getValidationErrors()) {
                System.out.println("      - " + err);
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println(" BAB IV: ABSTRACTION — Abstraksi Layanan OData SAP");
        System.out.println("═══════════════════════════════════════════════════════\n");

        // ═══════════════════════════════════════════
        //  ABSTRAKSI #1: Load PO — user cukup panggil 1 method
        //  Detail koneksi, auth, parsing → TERSEMBUNYI
        // ═══════════════════════════════════════════
        System.out.println("▶ [1] Abstraksi Koneksi OData:\n");
        PurchaseOrderService poService = new PurchaseOrderService(
            "https://sap.ilmuprogram.com",
            "wahyu.amaldi",
            "777"
        );
        poService.loadPO("4500000004");

        // ═══════════════════════════════════════════
        //  ABSTRAKSI #2: Print — user pilih summary atau detail
        //  Cara format, layout, emoji → TERSEMBUNYI
        // ═══════════════════════════════════════════
        System.out.println("\n▶ [2] Abstraksi Print — printSummary():\n");
        printDocument(poService);  // Hanya tahu "Printable", bukan "PurchaseOrderService"

        System.out.println("\n   printDetailed():\n");
        poService.printDetailed();

        // ═══════════════════════════════════════════
        //  ABSTRAKSI #3: Export — user pilih format
        //  Cara build JSON/CSV → TERSEMBUNYI
        // ═══════════════════════════════════════════
        System.out.println("\n▶ [3] Abstraksi Export — exportToJson():\n");
        exportDocument(poService);

        System.out.println("\n   exportToCsv():\n");
        System.out.println(poService.exportToCsv());

        // ═══════════════════════════════════════════
        //  ABSTRAKSI #4: Validate — user cukup tanya "valid?"
        //  Aturan validasi → TERSEMBUNYI
        // ═══════════════════════════════════════════
        System.out.println("\n▶ [4] Abstraksi Validasi — isValid():\n");
        validateDocument(poService);

        // ═══════════════════════════════════════════
        //  ABSTRAKSI #5: Connection Info
        // ═══════════════════════════════════════════
        System.out.println("\n▶ [5] Abstraksi OData Connection:\n");
        poService.displayConnectionInfo();

        // ═══════════════════════════════════════════
        //  RESUME: Apa yang user LIHAT vs TERSEMBUNYI
        // ═══════════════════════════════════════════
        System.out.println("\n═══════════════════════════════════════════════════════");
        System.out.println(" 📌 RESUME ABSTRACTION:");
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println();
        System.out.println("  Yang USER lihat (Abstraksi)  │  Yang TERSEMBUNYI (Implementasi)");
        System.out.println("  ─────────────────────────────┼──────────────────────────────────");
        System.out.println("  poService.loadPO(\"4500..\")   │  HTTP connect, auth, parse JSON");
        System.out.println("  poService.printSummary()     │  Format string, layout, emoji");
        System.out.println("  poService.exportToJson()     │  String.format, escaping, structure");
        System.out.println("  poService.isValid()          │  15+ business rules dicek");
        System.out.println("  poService.buildServiceUrl()  │  URL concat + encoding");
        System.out.println();
        System.out.println("  → User HANYA perlu tahu APA yang dilakukan");
        System.out.println("  → User TIDAK perlu tahu BAGAIMANA caranya");
    }
}
