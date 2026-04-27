/**
 * ═══════════════════════════════════════════════════════════════════
 *  BAB III: POLYMORPHISM — OData Service Processing SAP
 * ═══════════════════════════════════════════════════════════════════
 *
 *  @author  Wahyu Amaldi, M.Kom
 *  @institution Universitas Cakrawala
 *
 *  Mendemonstrasikan:
 *  • Method Overloading (Compile-time Polymorphism)
 *  • Method Overriding (Runtime Polymorphism)
 *  • Interface & implementasi berbeda
 *  • Satu method "process()" — banyak perilaku
 *
 *  Data Real SAP OData:
 *    PO Number  : 4500000004
 *    Services   : C_PURCHASEORDER_FS_SRV, MM_PUR_POITEMS_MONI_SRV
 *    Endpoint   : https://sap.ilmuprogram.com/sap/opu/odata/sap/
 *
 *  Compile & Run:
 *    javac 03_Polymorphism_ODataProcessor.java
 *    java ODataProcessorDemo
 * ═══════════════════════════════════════════════════════════════════
 */

import java.util.ArrayList;
import java.util.List;

// ═══════════════════════════════════════════════════════════
//  INTERFACE: ODataProcessor
//  "Kontrak" — siapapun yang implement harus punya method ini
//  Seperti tombol "Process" yang beda perilaku per dokumen
// ═══════════════════════════════════════════════════════════

interface ODataProcessor {
    String getServiceName();
    String getEntitySet();
    String buildUrl(String baseUrl);
    void process();
    String formatOutput();
}

// ═══════════════════════════════════════════════════════════
//  BASE CLASS: SAPODataDocument (dengan method overriding)
// ═══════════════════════════════════════════════════════════

abstract class SAPODataDocument {
    protected String documentNumber;
    protected String documentType;
    protected String baseUrl = "https://sap.ilmuprogram.com/sap/opu/odata/sap";

    public SAPODataDocument(String documentNumber, String documentType) {
        this.documentNumber = documentNumber;
        this.documentType = documentType;
    }

    /**
     * Method yang WAJIB di-override oleh subclass.
     * Setiap dokumen punya cara validasi berbeda.
     */
    abstract boolean validate();

    /**
     * Method yang WAJIB di-override.
     * Setiap dokumen punya format output berbeda.
     */
    abstract String toDisplayString();

    /**
     * Method yang bisa di-override (tidak wajib).
     */
    public String getStatusLabel() {
        return "📄 Document";
    }
}

// ═══════════════════════════════════════════════════════════
//  IMPLEMENTASI 1: POHeaderProcessor
//  Memproses PO Header via C_PURCHASEORDER_FS_SRV
// ═══════════════════════════════════════════════════════════

class POHeaderProcessor extends SAPODataDocument implements ODataProcessor {

    private String supplier;
    private String supplierName;
    private String companyCode;
    private String companyCodeName;
    private String currency;
    private String paymentTerms;
    private String createdBy;

    public POHeaderProcessor(String poNumber) {
        super(poNumber, "NB");
    }

    // ── Data loader (simulasi response OData) ──
    public void loadFromOData(String supplier, String supplierName,
                               String companyCode, String companyCodeName,
                               String currency, String paymentTerms, String createdBy) {
        this.supplier = supplier;
        this.supplierName = supplierName;
        this.companyCode = companyCode;
        this.companyCodeName = companyCodeName;
        this.currency = currency;
        this.paymentTerms = paymentTerms;
        this.createdBy = createdBy;
    }

    // ── Interface Implementation ──

    @Override
    public String getServiceName() {
        return "C_PURCHASEORDER_FS_SRV";
    }

    @Override
    public String getEntitySet() {
        return "I_PurchaseOrder";
    }

    @Override
    public String buildUrl(String baseUrl) {
        return baseUrl + "/" + getServiceName()
             + "/" + getEntitySet() + "('" + documentNumber + "')";
    }

    @Override
    public void process() {
        System.out.println("🔄 Processing PO Header: " + documentNumber);
        System.out.println("   Service : " + getServiceName());
        System.out.println("   Entity  : " + getEntitySet());
        System.out.println("   URL     : " + buildUrl(baseUrl));
        if (validate()) {
            System.out.println("   ✅ PO Header valid!");
        } else {
            System.out.println("   ❌ PO Header tidak valid!");
        }
    }

    @Override
    public String formatOutput() {
        return String.format("PO %s | %s | %s (%s) | %s | Created by: %s",
            documentNumber, documentType, supplier, supplierName,
            currency, createdBy);
    }

    // ── Abstract method implementation ──

    @Override
    boolean validate() {
        return documentNumber != null && supplier != null && companyCode != null;
    }

    @Override
    String toDisplayString() {
        return "📋 PO Header " + documentNumber + " - " + supplierName;
    }

    @Override
    public String getStatusLabel() {
        return "📋 Purchase Order Header";
    }
}

// ═══════════════════════════════════════════════════════════
//  IMPLEMENTASI 2: POItemProcessor
//  Memproses PO Item via MM_PUR_POITEMS_MONI_SRV
// ═══════════════════════════════════════════════════════════

class POItemProcessor extends SAPODataDocument implements ODataProcessor {

    private String poItem;
    private String itemText;
    private String materialGroupName;
    private String plantName;
    private double orderQuantity;
    private double netPriceAmount;
    private String orderPriceUnit;
    private String displayCurrency;
    private boolean isDelivered;
    private boolean isInvoiced;

    public POItemProcessor(String poNumber, String poItem) {
        super(poNumber, "NB");
        this.poItem = poItem;
    }

    public void loadFromOData(String itemText, String materialGroupName,
                               String plantName, double qty, double price,
                               String unit, String currency,
                               boolean delivered, boolean invoiced) {
        this.itemText = itemText;
        this.materialGroupName = materialGroupName;
        this.plantName = plantName;
        this.orderQuantity = qty;
        this.netPriceAmount = price;
        this.orderPriceUnit = unit;
        this.displayCurrency = currency;
        this.isDelivered = delivered;
        this.isInvoiced = invoiced;
    }

    // ── Interface Implementation (BERBEDA dari POHeaderProcessor!) ──

    @Override
    public String getServiceName() {
        return "MM_PUR_POITEMS_MONI_SRV";  // Service BERBEDA!
    }

    @Override
    public String getEntitySet() {
        return "C_PurchaseOrderItemMoni(P_DisplayCurrency='IDR')/Results";
    }

    @Override
    public String buildUrl(String baseUrl) {
        return baseUrl + "/" + getServiceName()
             + "/" + getEntitySet()
             + "?$filter=PurchaseOrder eq '" + documentNumber + "'";
    }

    @Override
    public void process() {
        System.out.println("🔄 Processing PO Item: " + documentNumber + "/" + poItem);
        System.out.println("   Service : " + getServiceName());
        System.out.println("   Entity  : " + getEntitySet());
        System.out.println("   URL     : " + buildUrl(baseUrl));
        if (validate()) {
            System.out.println("   ✅ PO Item valid! Total: " + formatTotal());
        } else {
            System.out.println("   ❌ PO Item tidak valid!");
        }
    }

    @Override
    public String formatOutput() {
        return String.format("PO %s/%s | %s | Qty: %.0f %s | Price: %,.2f | Total: %,.2f %s | %s %s",
            documentNumber, poItem, itemText,
            orderQuantity, orderPriceUnit,
            netPriceAmount, calculateTotal(), displayCurrency,
            isDelivered ? "✅Delivered" : "❌NotDelivered",
            isInvoiced ? "✅Invoiced" : "⏳PendingInvoice");
    }

    // ── Abstract method implementation ──

    @Override
    boolean validate() {
        return documentNumber != null && poItem != null
            && orderQuantity > 0 && netPriceAmount >= 0;
    }

    @Override
    String toDisplayString() {
        return "📦 PO Item " + documentNumber + "/" + poItem + " - " + itemText;
    }

    @Override
    public String getStatusLabel() {
        return "📦 Purchase Order Item";
    }

    // ── Method khusus POItem ──

    public double calculateTotal() {
        return orderQuantity * netPriceAmount;
    }

    private String formatTotal() {
        return String.format("%,.2f %s", calculateTotal(), displayCurrency);
    }

    // ═══════════════════════════════════════════
    //  METHOD OVERLOADING (Compile-time Polymorphism)
    //  Sama nama "search()" tapi beda parameter!
    // ═══════════════════════════════════════════

    /** Cari berdasarkan PO number saja */
    public String search(String poNumber) {
        return "🔍 Search PO: " + poNumber;
    }

    /** Cari berdasarkan PO number + item number */
    public String search(String poNumber, String itemNumber) {
        return "🔍 Search PO/Item: " + poNumber + "/" + itemNumber;
    }

    /** Cari berdasarkan PO number + material group + plant */
    public String search(String poNumber, String materialGroup, String plant) {
        return "🔍 Search PO: " + poNumber + " | MatGroup: " + materialGroup + " | Plant: " + plant;
    }

    /** Cari berdasarkan quantity range */
    public String search(double minQty, double maxQty) {
        return "🔍 Search by Qty Range: " + minQty + " - " + maxQty;
    }
}

// ═══════════════════════════════════════════════════════════
//  IMPLEMENTASI 3: POGoodsReceiptProcessor
//  Memproses Goods Receipt via C_PURCHASEORDER_FS_SRV
// ═══════════════════════════════════════════════════════════

class POGoodsReceiptProcessor extends SAPODataDocument implements ODataProcessor {

    private String poNumber;
    private double receivedQuantity;
    private double orderedQuantity;
    private String unit;
    private boolean isComplete;

    public POGoodsReceiptProcessor(String poNumber) {
        super(poNumber, "GR");
        this.poNumber = poNumber;
    }

    public void loadFromOData(double received, double ordered, String unit) {
        this.receivedQuantity = received;
        this.orderedQuantity = ordered;
        this.unit = unit;
        this.isComplete = received >= ordered;
    }

    @Override
    public String getServiceName() {
        return "C_PURCHASEORDER_FS_SRV";
    }

    @Override
    public String getEntitySet() {
        return "C_PurchaseOrderGoodsReceipt";
    }

    @Override
    public String buildUrl(String baseUrl) {
        return baseUrl + "/" + getServiceName()
             + "/" + getEntitySet()
             + "?$filter=PurchaseOrder eq '" + documentNumber + "'";
    }

    @Override
    public void process() {
        System.out.println("🔄 Processing Goods Receipt: " + documentNumber);
        System.out.println("   Service : " + getServiceName());
        System.out.println("   Received: " + receivedQuantity + " / " + orderedQuantity + " " + unit);
        System.out.println("   Status  : " + (isComplete ? "✅ Complete" : "⏳ Partial"));
    }

    @Override
    public String formatOutput() {
        return String.format("GR for PO %s | Received: %.0f/%.0f %s | %s",
            documentNumber, receivedQuantity, orderedQuantity, unit,
            isComplete ? "Complete" : "Partial");
    }

    @Override
    boolean validate() {
        return documentNumber != null && receivedQuantity >= 0;
    }

    @Override
    String toDisplayString() {
        return "📦 Goods Receipt for PO " + documentNumber;
    }

    @Override
    public String getStatusLabel() {
        return "📦 Goods Receipt";
    }
}

// ═══════════════════════════════════════════════════════════
//  MAIN: Demo Polymorphism
// ═══════════════════════════════════════════════════════════

class ODataProcessorDemo {

    /**
     * Method yang menerima ODataProcessor (interface type).
     * Tidak peduli IMPLEMENTASI mana yang masuk — semuanya bisa diproses!
     * INI adalah kekuatan polymorphism.
     */
    static void processAny(ODataProcessor processor) {
        System.out.println("─".repeat(55));
        processor.process();
        System.out.println("   Output : " + processor.formatOutput());
        System.out.println();
    }

    /**
     * Method yang menerima SAPODataDocument (abstract class type).
     */
    static void displayAny(SAPODataDocument doc) {
        System.out.println("   " + doc.getStatusLabel() + ": " + doc.toDisplayString());
    }

    public static void main(String[] args) {
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println(" BAB III: POLYMORPHISM — OData Service Processing");
        System.out.println("═══════════════════════════════════════════════════════\n");

        // ── 1. Buat 3 processor berbeda ──
        POHeaderProcessor header = new POHeaderProcessor("4500000004");
        header.loadFromOData("17300001", "Wahyu Amaldi (Domestic Supplier)",
            "1710", "Andi Coffee", "USD", "0004", "BUDILUHUR");

        POItemProcessor item = new POItemProcessor("4500000004", "00010");
        item.loadFromOData("PEMBELIAN LAPTOP", "Computer Hardware",
            "Coffee Plant – Jakarta", 10, 302.00, "PC", "USD", true, false);

        POGoodsReceiptProcessor gr = new POGoodsReceiptProcessor("4500000004");
        gr.loadFromOData(10, 10, "PC");

        // ── 2. Runtime Polymorphism: satu method, tiga perilaku ──
        System.out.println("▶ [1] Runtime Polymorphism — processAny():\n");
        System.out.println("   Satu method processAny() — tiga perilaku berbeda!\n");

        processAny(header);  // Memanggil POHeaderProcessor.process()
        processAny(item);    // Memanggil POItemProcessor.process()
        processAny(gr);      // Memanggil POGoodsReceiptProcessor.process()

        // ── 3. Polymorphic List ──
        System.out.println("▶ [2] Polymorphic List — List<ODataProcessor>:\n");
        List<ODataProcessor> processors = new ArrayList<>();
        processors.add(header);
        processors.add(item);
        processors.add(gr);

        for (ODataProcessor p : processors) {
            System.out.println("   📌 " + p.formatOutput());
        }

        // ── 4. Abstract class polymorphism ──
        System.out.println("\n▶ [3] Abstract Class Polymorphism — displayAny():\n");
        SAPODataDocument[] docs = { header, item, gr };
        for (SAPODataDocument doc : docs) {
            displayAny(doc);
        }

        // ── 5. Method Overloading (Compile-time Polymorphism) ──
        System.out.println("\n▶ [4] Method Overloading — search() dengan parameter berbeda:\n");
        System.out.println("   " + item.search("4500000004"));
        System.out.println("   " + item.search("4500000004", "00010"));
        System.out.println("   " + item.search("4500000004", "YBFA08", "1710"));
        System.out.println("   " + item.search(5.0, 20.0));

        // ── 6. instanceof check ──
        System.out.println("\n▶ [5] Type Check dengan instanceof:\n");
        for (SAPODataDocument doc : docs) {
            if (doc instanceof POHeaderProcessor) {
                System.out.println("   " + doc.documentNumber + " adalah PO Header");
            } else if (doc instanceof POItemProcessor) {
                System.out.println("   " + doc.documentNumber + " adalah PO Item");
            } else if (doc instanceof POGoodsReceiptProcessor) {
                System.out.println("   " + doc.documentNumber + " adalah Goods Receipt");
            }
        }
    }
}
