/**
 * ═══════════════════════════════════════════════════════════════════
 *  POSTING DATA — Create Purchase Order via SAP OData (REAL HTTP)
 * ═══════════════════════════════════════════════════════════════════
 *
 *  @author  Wahyu Amaldi, M.Kom
 *  @institution Universitas Cakrawala
 *
 *  Mendemonstrasikan:
 *  • HTTP POST NYATA ke SAP OData untuk membuat PO baru
 *  • CSRF Token handling (GET fetch → attach ke POST)
 *  • Build JSON payload (request body)
 *  • Validasi data sebelum POST
 *  • Response handling (success / error)
 *  • Encapsulation pada credentials & payload builder
 *
 *  Data Real SAP OData:
 *    Service    : MM_PUR_PO_MAINT_V2_SRV
 *    Entity     : C_PurchaseOrderTP
 *    Endpoint   : https://sap.ilmuprogram.com/sap/opu/odata/sap/
 *
 *  Compile & Run:
 *    javac 05_PostData_ODataCreate.java
 *    java ODataPostDemo
 * ═══════════════════════════════════════════════════════════════════
 */

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

// ═══════════════════════════════════════════════════════════
//  CLASS: ODataConnection — Koneksi & autentikasi SAP
// ═══════════════════════════════════════════════════════════

class ODataConnection {

    private String baseUrl;
    private String username;
    private String password;
    private String client;
    private String csrfToken;
    private String sessionCookies;
    private boolean isConnected;
    private HttpClient httpClient;

    public ODataConnection(String baseUrl, String username, String password, String client) {
        this.baseUrl = baseUrl;
        this.username = username;
        this.password = password;
        this.client = client;
        this.isConnected = false;
        this.httpClient = createHttpClient();
    }

    /**
     * Buat HttpClient yang trust semua certificate.
     * SAP sering pakai self-signed cert.
     */
    private HttpClient createHttpClient() {
        try {
            TrustManager[] trustAll = new TrustManager[] {
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
                    public void checkClientTrusted(X509Certificate[] certs, String type) {}
                    public void checkServerTrusted(X509Certificate[] certs, String type) {}
                }
            };
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAll, new SecureRandom());
            return HttpClient.newBuilder()
                .sslContext(sslContext)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
        } catch (Exception e) {
            throw new RuntimeException("Gagal membuat HttpClient: " + e.getMessage());
        }
    }

    /**
     * Build Authorization header (Basic Auth).
     * Credentials di-encode Base64 — detail tersembunyi.
     */
    private String buildAuthHeader() {
        String credentials = username + ":" + password;
        return "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes());
    }

    /**
     * REAL: Fetch CSRF Token dari SAP.
     * SAP OData mewajibkan CSRF token untuk semua operasi POST/PUT/DELETE.
     *
     * Langkah:
     *   1. GET request ke service URL dengan header X-CSRF-Token: Fetch
     *   2. SAP mengembalikan token di response header X-CSRF-Token
     *   3. Simpan juga cookies (session) untuk request berikutnya
     */
    public String fetchCsrfToken(String serviceUrl) {
        System.out.println("   [CSRF] 🔑 Fetching CSRF Token...");
        System.out.println("   [CSRF] GET " + serviceUrl);
        System.out.println("   [CSRF] Header: X-CSRF-Token: Fetch");
        System.out.println("   [CSRF] Header: Authorization: " + maskAuth());

        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(serviceUrl + "?sap-client=" + client + "&$top=1"))
                .header("Authorization", buildAuthHeader())
                .header("X-CSRF-Token", "Fetch")
                .header("Accept", "application/json")
                .GET()
                .build();

            HttpResponse<String> response = httpClient.send(request,
                HttpResponse.BodyHandlers.ofString());

            System.out.println("   [CSRF] HTTP " + response.statusCode());

            // Ambil CSRF token dari response header
            this.csrfToken = response.headers()
                .firstValue("x-csrf-token").orElse(null);

            // Simpan cookies untuk dikirim bersama POST
            List<String> cookies = response.headers().allValues("set-cookie");
            StringBuilder cookieBuilder = new StringBuilder();
            for (String cookie : cookies) {
                if (cookieBuilder.length() > 0) cookieBuilder.append("; ");
                cookieBuilder.append(cookie.split(";")[0]);
            }
            this.sessionCookies = cookieBuilder.toString();

            if (csrfToken != null && !csrfToken.isEmpty()) {
                this.isConnected = true;
                System.out.println("   [CSRF] ✅ Token received: " + csrfToken);
            } else {
                System.out.println("   [CSRF] ❌ Token tidak ditemukan di response header!");
                System.out.println("   [CSRF] Response body: " + response.body().substring(0, Math.min(200, response.body().length())));
            }

            return csrfToken;

        } catch (Exception e) {
            System.out.println("   [CSRF] ❌ Error: " + e.getMessage());
            return null;
        }
    }

    public String getCsrfToken() {
        return csrfToken;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getClient() {
        return client;
    }

    private String maskAuth() {
        return "Basic " + username.substring(0, 3) + "***:********";
    }

    /**
     * REAL: Kirim HTTP POST ke SAP OData.
     * Return response code & body.
     */
    public ODataResponse sendPost(String url, String jsonPayload, String csrfToken) {
        System.out.println("\n   [POST] 📤 Sending POST request...");
        System.out.println("   [POST] URL : " + url + "?sap-client=" + client);
        System.out.println("   [POST] Header: Content-Type: application/json");
        System.out.println("   [POST] Header: X-CSRF-Token: " + csrfToken);
        System.out.println("   [POST] Header: Authorization: " + maskAuth());
        System.out.println("   [POST] Payload size: " + jsonPayload.length() + " bytes");

        try {
            HttpRequest.Builder reqBuilder = HttpRequest.newBuilder()
                .uri(URI.create(url + "?sap-client=" + client))
                .header("Authorization", buildAuthHeader())
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("X-CSRF-Token", csrfToken);

            // Attach session cookies dari CSRF fetch
            if (sessionCookies != null && !sessionCookies.isEmpty()) {
                reqBuilder.header("Cookie", sessionCookies);
            }

            HttpRequest request = reqBuilder
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .build();

            HttpResponse<String> response = httpClient.send(request,
                HttpResponse.BodyHandlers.ofString());

            int statusCode = response.statusCode();
            String responseBody = response.body();

            // Coba extract PO number dari response
            String createdId = extractPONumber(responseBody);

            String statusMessage = switch (statusCode) {
                case 201 -> "Created";
                case 200 -> "OK";
                case 400 -> "Bad Request";
                case 401 -> "Unauthorized";
                case 403 -> "Forbidden (CSRF token issue?)";
                case 404 -> "Not Found";
                case 500 -> "Internal Server Error";
                default -> "HTTP " + statusCode;
            };

            return new ODataResponse(statusCode, statusMessage, responseBody, createdId);

        } catch (Exception e) {
            return new ODataResponse(0, "Connection Error",
                "Error: " + e.getMessage(), null);
        }
    }

    /**
     * Extract PO Number dari response JSON SAP.
     * Contoh: "PurchaseOrder":"4500000123"
     */
    private String extractPONumber(String json) {
        String key = "\"PurchaseOrder\":\"";
        int idx = json.indexOf(key);
        if (idx >= 0) {
            int start = idx + key.length();
            int end = json.indexOf("\"", start);
            if (end > start) {
                String po = json.substring(start, end);
                if (!po.isEmpty()) return po;
            }
        }
        return null;
    }

    /**
     * Extract DraftUUID dari response JSON SAP.
     * Contoh: "DraftUUID":"000c29de-8c2b-1fd1..."
     */
    private String extractDraftUUID(String json) {
        String key = "\"DraftUUID\":\"";
        int idx = json.indexOf(key);
        if (idx >= 0) {
            int start = idx + key.length();
            int end = json.indexOf("\"", start);
            if (end > start) return json.substring(start, end);
        }
        return null;
    }

    /**
     * REAL: Activate draft PO di SAP.
     * Service draft-enabled: POST → draft → Preparation → Activation → PO aktif.
     *
     * Step:
     *   1. POST ...Preparation (validasi di backend)
     *   2. POST ...Activation (simpan & generate PO number)
     */
    public ODataResponse activateDraft(String baseServiceUrl, String draftUUID, String csrfToken) {
        // SAP OData V2: function import di service root level
        String serviceBase = baseServiceUrl.substring(0, baseServiceUrl.lastIndexOf("/"));

        // Step 1: Preparation
        String prepUrl = serviceBase
            + "/C_PurchaseOrderTPPreparation"
            + "?PurchaseOrder=''"
            + "&DraftUUID=guid'" + draftUUID + "'"
            + "&IsActiveEntity=false"
            + "&sap-client=" + client;

        System.out.println("\n   [PREP] 🔧 Calling Preparation action...");
        System.out.println("   [PREP] POST " + prepUrl);

        try {
            HttpRequest prepReq = HttpRequest.newBuilder()
                .uri(URI.create(prepUrl))
                .header("Authorization", buildAuthHeader())
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("X-CSRF-Token", csrfToken)
                .header("Cookie", sessionCookies != null ? sessionCookies : "")
                .POST(HttpRequest.BodyPublishers.ofString("{}"))
                .build();

            HttpResponse<String> prepResp = httpClient.send(prepReq,
                HttpResponse.BodyHandlers.ofString());
            System.out.println("   [PREP] HTTP " + prepResp.statusCode());

            if (prepResp.statusCode() != 200 && prepResp.statusCode() != 201) {
                return new ODataResponse(prepResp.statusCode(), "Preparation Failed",
                    prepResp.body(), null);
            }
            System.out.println("   [PREP] ✅ Preparation OK");

            // Step 2: Activation
            String actUrl = serviceBase
                + "/C_PurchaseOrderTPActivation"
                + "?PurchaseOrder=''"
                + "&DraftUUID=guid'" + draftUUID + "'"
                + "&IsActiveEntity=false"
                + "&sap-client=" + client;

            System.out.println("\n   [ACTIVATE] 🚀 Calling Activation action...");
            System.out.println("   [ACTIVATE] POST " + actUrl);

            HttpRequest actReq = HttpRequest.newBuilder()
                .uri(URI.create(actUrl))
                .header("Authorization", buildAuthHeader())
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("X-CSRF-Token", csrfToken)
                .header("Cookie", sessionCookies != null ? sessionCookies : "")
                .POST(HttpRequest.BodyPublishers.ofString("{}"))
                .build();

            HttpResponse<String> actResp = httpClient.send(actReq,
                HttpResponse.BodyHandlers.ofString());

            System.out.println("   [ACTIVATE] HTTP " + actResp.statusCode());

            String responseBody = actResp.body();
            String poNumber = extractPONumber(responseBody);

            if (actResp.statusCode() == 200 || actResp.statusCode() == 201) {
                System.out.println("   [ACTIVATE] ✅ PO Activated! Number: " + poNumber);
                return new ODataResponse(201, "Created & Activated", responseBody, poNumber);
            } else {
                System.out.println("   [ACTIVATE] ❌ Activation failed!");
                return new ODataResponse(actResp.statusCode(), "Activation Failed",
                    responseBody, null);
            }

        } catch (Exception e) {
            return new ODataResponse(0, "Connection Error",
                "Error: " + e.getMessage(), null);
        }
    }
}

// ═══════════════════════════════════════════════════════════
//  CLASS: ODataResponse — Response dari SAP
// ═══════════════════════════════════════════════════════════

class ODataResponse {

    private int statusCode;
    private String statusMessage;
    private String responseBody;
    private String createdId;

    public ODataResponse(int statusCode, String statusMessage, String responseBody, String createdId) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.responseBody = responseBody;
        this.createdId = createdId;
    }

    public boolean isSuccess() {
        return statusCode == 201;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public String getCreatedId() {
        return createdId;
    }

    public void displayResponse() {
        System.out.println("\n   [RESPONSE] HTTP " + statusCode + " " + statusMessage);
        if (isSuccess()) {
            System.out.println("   [RESPONSE] ✅ Resource created successfully!");
            System.out.println("   [RESPONSE] Created ID: " + createdId);
        } else {
            System.out.println("   [RESPONSE] ❌ Request failed!");
        }
        System.out.println("   [RESPONSE] Body:\n" + responseBody);
    }
}

// ═══════════════════════════════════════════════════════════
//  CLASS: POItem — Item baris PO yang akan di-POST
// ═══════════════════════════════════════════════════════════

class POItem {

    private String itemNumber;
    private String itemText;
    private String materialGroup;
    private String plant;
    private double orderQuantity;
    private double netPriceAmount;
    private String orderPriceUnit;
    private String documentCurrency;
    private String accountAssignmentCategory;

    public POItem(String itemNumber, String itemText, String materialGroup,
                  String plant, double orderQuantity, double netPriceAmount,
                  String orderPriceUnit, String documentCurrency) {
        this.itemNumber = itemNumber;
        this.itemText = itemText;
        this.materialGroup = materialGroup;
        this.plant = plant;
        this.orderQuantity = orderQuantity;
        this.netPriceAmount = netPriceAmount;
        this.orderPriceUnit = orderPriceUnit;
        this.documentCurrency = documentCurrency;
        this.accountAssignmentCategory = "K"; // K = Cost Center (default untuk tanpa material)
    }

    public String getItemNumber() { return itemNumber; }
    public String getItemText() { return itemText; }
    public double getOrderQuantity() { return orderQuantity; }
    public double getNetPriceAmount() { return netPriceAmount; }
    public double getNetAmount() { return orderQuantity * netPriceAmount; }

    /**
     * Validasi item sebelum POST.
     */
    public List<String> validate() {
        List<String> errors = new ArrayList<>();
        if (itemNumber == null || !itemNumber.matches("\\d{5}"))
            errors.add("Item number harus 5 digit (contoh: 00010)");
        if (itemText == null || itemText.trim().length() < 3)
            errors.add("Item text minimal 3 karakter");
        if (orderQuantity <= 0)
            errors.add("Quantity harus > 0");
        if (netPriceAmount < 0)
            errors.add("Net price tidak boleh negatif");
        if (plant == null || plant.trim().isEmpty())
            errors.add("Plant wajib diisi");
        return errors;
    }

    /**
     * Build JSON untuk item ini (bagian dari payload POST).
     */
    public String toJson() {
        return String.format("""
                {
                  "PurchaseOrderItem": "%s",
                  "PurchaseOrderItemText": "%s",
                  "AccountAssignmentCategory": "%s",
                  "MaterialGroup": "%s",
                  "Plant": "%s",
                  "OrderQuantity": "%.0f",
                  "PurchaseOrderQuantityUnit": "%s",
                  "NetPriceAmount": "%.2f",
                  "OrderPriceUnit": "%s",
                  "DocumentCurrency": "%s",
                  "to_PurOrdAcctAssignmentTP": {
                    "results": [{
                      "AccountAssignmentNumber": "01",
                      "GLAccount": "61002000",
                      "CostCenter": "17101101"
                    }]
                  }
                }""",
            itemNumber, itemText, accountAssignmentCategory,
            materialGroup, plant,
            orderQuantity, orderPriceUnit,
            netPriceAmount, orderPriceUnit, documentCurrency);
    }

    public void display() {
        System.out.printf("   │ %s │ %-20s │ %5.0f %-2s │ %10s │ %12s │%n",
            itemNumber, itemText, orderQuantity, orderPriceUnit,
            String.format("%,.2f", netPriceAmount),
            String.format("%,.2f %s", getNetAmount(), documentCurrency));
    }
}

// ═══════════════════════════════════════════════════════════
//  CLASS: POPostPayload — Builder untuk JSON payload POST
// ═══════════════════════════════════════════════════════════

class POPostPayload {

    // Header fields
    private String purchaseOrderType;
    private String supplier;
    private String companyCode;
    private String purchasingOrganization;
    private String purchasingGroup;
    private String documentCurrency;
    private String paymentTerms;
    private LocalDate purchaseOrderDate;

    // Items
    private List<POItem> items;

    public POPostPayload() {
        this.items = new ArrayList<>();
        this.purchaseOrderDate = LocalDate.now();
    }

    // ── Setter (dengan validasi) ──

    public void setPurchaseOrderType(String type) {
        String[] validTypes = {"NB", "FO", "UB", "EC"};
        boolean valid = false;
        for (String t : validTypes) {
            if (t.equals(type)) { valid = true; break; }
        }
        if (!valid) throw new IllegalArgumentException("❌ PO Type tidak valid! Gunakan: NB, FO, UB, EC");
        this.purchaseOrderType = type;
    }

    public void setSupplier(String supplier) {
        if (supplier == null || supplier.trim().isEmpty())
            throw new IllegalArgumentException("❌ Supplier tidak boleh kosong!");
        this.supplier = supplier;
    }

    public void setCompanyCode(String companyCode) {
        if (companyCode == null || !companyCode.matches("\\d{4}"))
            throw new IllegalArgumentException("❌ Company Code harus 4 digit!");
        this.companyCode = companyCode;
    }

    public void setPurchasingOrganization(String purchOrg) {
        this.purchasingOrganization = purchOrg;
    }

    public void setPurchasingGroup(String purchGroup) {
        this.purchasingGroup = purchGroup;
    }

    public void setDocumentCurrency(String currency) {
        this.documentCurrency = currency;
    }

    public void setPaymentTerms(String terms) {
        this.paymentTerms = terms;
    }

    public void addItem(POItem item) {
        this.items.add(item);
    }

    public List<POItem> getItems() {
        return items;
    }

    // ── Validasi keseluruhan payload ──

    public List<String> validate() {
        List<String> errors = new ArrayList<>();

        // Validasi header
        if (purchaseOrderType == null) errors.add("PO Type wajib diisi");
        if (supplier == null || supplier.isEmpty()) errors.add("Supplier wajib diisi");
        if (companyCode == null || companyCode.isEmpty()) errors.add("Company Code wajib diisi");
        if (purchasingOrganization == null) errors.add("Purchasing Organization wajib diisi");
        if (purchasingGroup == null) errors.add("Purchasing Group wajib diisi");
        if (documentCurrency == null) errors.add("Document Currency wajib diisi");

        // Validasi minimal 1 item
        if (items.isEmpty()) {
            errors.add("Minimal harus ada 1 item PO");
        } else {
            for (POItem item : items) {
                List<String> itemErrors = item.validate();
                for (String err : itemErrors) {
                    errors.add("Item " + item.getItemNumber() + ": " + err);
                }
            }
        }

        return errors;
    }

    public boolean isValid() {
        return validate().isEmpty();
    }

    // ── Hitung total semua item ──

    public double calculateGrandTotal() {
        double total = 0;
        for (POItem item : items) {
            total += item.getNetAmount();
        }
        return total;
    }

    // ── Build JSON payload untuk POST ──

    public String toJson() {
        StringBuilder itemsJson = new StringBuilder();
        for (int i = 0; i < items.size(); i++) {
            itemsJson.append(items.get(i).toJson());
            if (i < items.size() - 1) itemsJson.append(",");
        }

        // SAP OData V2 butuh format tanggal: /Date(epoch_ms)/
        long epochMs = purchaseOrderDate.atStartOfDay()
            .toInstant(java.time.ZoneOffset.UTC).toEpochMilli();

        return String.format("""
            {
              "PurchaseOrderType": "%s",
              "Supplier": "%s",
              "CompanyCode": "%s",
              "PurchasingOrganization": "%s",
              "PurchasingGroup": "%s",
              "DocumentCurrency": "%s",
              "PaymentTerms": "%s",
              "PurchaseOrderDate": "/Date(%d)/",
              "to_PurchaseOrderItemTP": {
                "results": [
            %s
                ]
              }
            }""",
            purchaseOrderType, supplier, companyCode,
            purchasingOrganization, purchasingGroup,
            documentCurrency, paymentTerms,
            epochMs,
            itemsJson.toString());
    }

    // ── Display preview sebelum POST ──

    public void displayPreview() {
        System.out.println("╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║           📤 POST PREVIEW — Purchase Order Baru              ║");
        System.out.println("╠═══════════════════════════════════════════════════════════════╣");
        System.out.printf("║ PO Type       : %-44s ║%n", purchaseOrderType + " (" + getTypeName() + ")");
        System.out.printf("║ Supplier      : %-44s ║%n", supplier);
        System.out.printf("║ Company Code  : %-44s ║%n", companyCode);
        System.out.printf("║ Purch. Org    : %-44s ║%n", purchasingOrganization);
        System.out.printf("║ Purch. Group  : %-44s ║%n", purchasingGroup);
        System.out.printf("║ Currency      : %-44s ║%n", documentCurrency);
        System.out.printf("║ Payment Terms : %-44s ║%n", paymentTerms);
        System.out.printf("║ PO Date       : %-44s ║%n", purchaseOrderDate.format(DateTimeFormatter.ISO_DATE));
        System.out.println("╠═══════════════════════════════════════════════════════════════╣");
        System.out.println("║ ITEMS:                                                       ║");
        System.out.println("║   ┌───────┬──────────────────────┬──────────┬────────────────┐║");
        System.out.println("║   │ Item  │ Description          │ Qty      │ Amount         │║");
        System.out.println("║   ├───────┼──────────────────────┼──────────┼────────────────┤║");
        for (POItem item : items) {
            System.out.print("║");
            item.display();
            System.out.println("║");
        }
        System.out.println("║   └───────┴──────────────────────┴──────────┴────────────────┘║");
        System.out.printf("║ GRAND TOTAL   : %-44s ║%n",
            String.format("%,.2f %s", calculateGrandTotal(), documentCurrency));
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");
    }

    private String getTypeName() {
        return switch (purchaseOrderType) {
            case "NB" -> "Standard PO";
            case "FO" -> "Framework Order";
            case "UB" -> "Stock Transfer Order";
            case "EC" -> "External Service";
            default -> purchaseOrderType;
        };
    }
}

// ═══════════════════════════════════════════════════════════
//  CLASS: POPostService — Orchestrator untuk POST PO
// ═══════════════════════════════════════════════════════════

class POPostService {

    private ODataConnection connection;
    private String serviceName;
    private String entitySet;

    public POPostService(ODataConnection connection) {
        this.connection = connection;
        this.serviceName = "MM_PUR_PO_MAINT_V2_SRV";
        this.entitySet = "C_PurchaseOrderTP";
    }

    /**
     * Build URL lengkap untuk POST.
     */
    private String buildPostUrl() {
        return connection.getBaseUrl() + "/sap/opu/odata/sap/"
             + serviceName + "/" + entitySet;
    }

    /**
     * Proses POST Purchase Order ke SAP.
     *
     * Alur lengkap (Draft-enabled service):
     *   1. Validasi payload
     *   2. Fetch CSRF Token (GET request)
     *   3. Build JSON payload
     *   4. Kirim POST request → membuat DRAFT
     *   5. Preparation (validasi backend)
     *   6. Activation (simpan & generate PO number)
     *   7. Handle response
     */
    public ODataResponse createPurchaseOrder(POPostPayload payload) {
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println("   🚀 MEMULAI PROSES POST PURCHASE ORDER");
        System.out.println("═══════════════════════════════════════════════════════\n");

        // ── Step 1: Validasi ──
        System.out.println("   [STEP 1/7] 🔍 Validasi payload...");
        List<String> errors = payload.validate();
        if (!errors.isEmpty()) {
            System.out.println("   ❌ Validasi GAGAL! Error:");
            for (String err : errors) {
                System.out.println("      - " + err);
            }
            return new ODataResponse(400, "Bad Request", "Validation failed", null);
        }
        System.out.println("   ✅ Payload valid!\n");

        // ── Step 2: Fetch CSRF Token ──
        System.out.println("   [STEP 2/7] 🔑 Fetching CSRF Token...");
        String serviceUrl = buildPostUrl();
        String csrfToken = connection.fetchCsrfToken(serviceUrl);
        System.out.println();

        // ── Step 3: Build JSON ──
        System.out.println("   [STEP 3/7] 📝 Building JSON payload...");
        String jsonPayload = payload.toJson();
        System.out.println("   ✅ JSON payload built (" + jsonPayload.length() + " bytes)");

        // ── Step 4: Kirim POST (buat draft) ──
        System.out.println("\n   [STEP 4/7] 📤 Sending POST to SAP (create draft)...");
        ODataResponse draftResponse = connection.sendPost(serviceUrl, jsonPayload, csrfToken);

        if (!draftResponse.isSuccess()) {
            System.out.println("\n   [STEP 4/7] ❌ POST failed!");
            draftResponse.displayResponse();
            return draftResponse;
        }
        System.out.println("\n   [STEP 4/7] ✅ Draft created!");

        // ── Step 5-6: Extract DraftUUID & Activate ──
        String draftUUID = extractDraftUUID(draftResponse.getResponseBody());
        if (draftUUID == null) {
            System.out.println("   ❌ DraftUUID tidak ditemukan di response!");
            draftResponse.displayResponse();
            return draftResponse;
        }
        System.out.println("   Draft UUID: " + draftUUID);

        System.out.println("\n   [STEP 5-6/7] 🔧 Preparation & Activation...");
        ODataResponse finalResponse = connection.activateDraft(serviceUrl, draftUUID, csrfToken);

        // ── Step 7: Handle Response ──
        System.out.println("\n   [STEP 7/7] 📥 Final result:");
        finalResponse.displayResponse();

        return finalResponse;
    }

    /**
     * Extract DraftUUID dari response body.
     */
    private String extractDraftUUID(String json) {
        String key = "\"DraftUUID\":\"";
        int idx = json.indexOf(key);
        if (idx >= 0) {
            int start = idx + key.length();
            int end = json.indexOf("\"", start);
            if (end > start) return json.substring(start, end);
        }
        return null;
    }
}

// ═══════════════════════════════════════════════════════════
//  MAIN: Demo Posting Data ke SAP OData
// ═══════════════════════════════════════════════════════════

class ODataPostDemo {

    public static void main(String[] args) {
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println(" POSTING DATA — Create Purchase Order via SAP OData (REAL HTTP)");
        System.out.println("═══════════════════════════════════════════════════════════════\n");

        // ═══════════════════════════════════════════
        //  STEP 1: Setup koneksi SAP
        // ═══════════════════════════════════════════
        System.out.println("▶ [1] Setup Koneksi SAP OData:\n");
        ODataConnection connection = new ODataConnection(
            "https://sap.ilmuprogram.com",
            "wahyu.amaldi",
            "Pas671_ok12345",
            "777"
        );
        System.out.println("   ✅ Koneksi disiapkan ke: https://sap.ilmuprogram.com (client 777)\n");

        // ═══════════════════════════════════════════
        //  STEP 2: Siapkan payload PO Header
        // ═══════════════════════════════════════════
        System.out.println("▶ [2] Menyiapkan Payload Purchase Order:\n");
        POPostPayload payload = new POPostPayload();
        payload.setPurchaseOrderType("NB");
        payload.setSupplier("17300001");
        payload.setCompanyCode("1710");
        payload.setPurchasingOrganization("1710");
        payload.setPurchasingGroup("001");
        payload.setDocumentCurrency("USD");
        payload.setPaymentTerms("0004");

        // ═══════════════════════════════════════════
        //  STEP 3: Tambahkan item-item PO
        // ═══════════════════════════════════════════
        System.out.println("   Menambahkan items...\n");

        POItem item1 = new POItem("00010", "PEMBELIAN LAPTOP",
            "YBFA08", "1710", 10, 302.00, "PC", "USD");
        payload.addItem(item1);

        POItem item2 = new POItem("00020", "PEMBELIAN MONITOR",
            "YBFA08", "1710", 10, 150.00, "PC", "USD");
        payload.addItem(item2);

        POItem item3 = new POItem("00030", "PEMBELIAN KEYBOARD",
            "YBFA08", "1710", 20, 25.00, "PC", "USD");
        payload.addItem(item3);

        // ═══════════════════════════════════════════
        //  STEP 4: Preview sebelum POST
        // ═══════════════════════════════════════════
        System.out.println("▶ [3] Preview Data yang Akan Di-POST:\n");
        payload.displayPreview();

        // ═══════════════════════════════════════════
        //  STEP 5: Tampilkan JSON payload
        // ═══════════════════════════════════════════
        System.out.println("\n▶ [4] JSON Payload yang Dikirim ke SAP:\n");
        System.out.println(payload.toJson());

        // ═══════════════════════════════════════════
        //  STEP 6: Eksekusi POST
        // ═══════════════════════════════════════════
        System.out.println("\n▶ [5] Eksekusi POST ke SAP OData:\n");
        POPostService postService = new POPostService(connection);
        ODataResponse response = postService.createPurchaseOrder(payload);

        // ═══════════════════════════════════════════
        //  STEP 7: Ringkasan hasil
        // ═══════════════════════════════════════════
        System.out.println("\n▶ [6] Ringkasan Hasil POST:\n");
        System.out.println("╔═══════════════════════════════════════════════════╗");
        System.out.println("║            📋 HASIL POSTING DATA                 ║");
        System.out.println("╠═══════════════════════════════════════════════════╣");
        System.out.printf("║ Status     : %-36s ║%n",
            response.isSuccess() ? "✅ " + response.getStatusCode() + " " + response.getStatusMessage()
                                 : "❌ " + response.getStatusCode() + " " + response.getStatusMessage());
        System.out.printf("║ PO Created : %-36s ║%n",
            response.getCreatedId() != null ? response.getCreatedId() : "-");
        System.out.printf("║ Items      : %-36s ║%n", payload.getItems().size() + " item(s)");
        System.out.printf("║ Total      : %-36s ║%n",
            String.format("%,.2f USD", payload.calculateGrandTotal()));
        System.out.println("╚═══════════════════════════════════════════════════╝");

        // ═══════════════════════════════════════════
        //  DEMO: Validasi gagal (payload tidak lengkap)
        // ═══════════════════════════════════════════
        System.out.println("\n▶ [7] Demo Validasi Gagal (payload kosong — TIDAK dikirim ke SAP):\n");
        POPostPayload invalidPayload = new POPostPayload();
        POPostService postService2 = new POPostService(connection);
        postService2.createPurchaseOrder(invalidPayload);

        // ═══════════════════════════════════════════
        //  RESUME: Alur POST OData SAP
        // ═══════════════════════════════════════════
        System.out.println("\n═══════════════════════════════════════════════════════════════");
        System.out.println(" 📌 RESUME — Alur POST Data ke SAP OData:");
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println();
        System.out.println("  ┌──────────────┐    ┌──────────────┐    ┌──────────────┐");
        System.out.println("  │  1. VALIDASI  │───▶│ 2. GET CSRF  │───▶│ 3. BUILD JSON│");
        System.out.println("  │  Cek payload  │    │ Token fetch   │    │ Payload body │");
        System.out.println("  └──────────────┘    └──────────────┘    └──────┬───────┘");
        System.out.println("                                                 │");
        System.out.println("  ┌──────────────┐    ┌──────────────┐    ┌──────▼───────┐");
        System.out.println("  │ 5. HANDLE    │◀───│ 4. HTTP POST │◀───│ Attach token │");
        System.out.println("  │ Response 201 │    │ Send request  │    │ + Auth header│");
        System.out.println("  └──────────────┘    └──────────────┘    └──────────────┘");
        System.out.println();
        System.out.println("  POST URL: /sap/opu/odata/sap/MM_PUR_PO_MAINT_V2_SRV/C_PurchaseOrderTP");
        System.out.println("  Headers : Content-Type: application/json");
        System.out.println("            X-CSRF-Token: <token dari step 2>");
        System.out.println("            Authorization: Basic <base64 credentials>");
    }
}