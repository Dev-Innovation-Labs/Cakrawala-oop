# 🏢 Studi Kasus OOP — Integrasi SAP OData Purchase Order

> **Penulis:** Wahyu Amaldi, M.Kom · **Institusi:** Universitas Cakrawala  
> **Studi Kasus:** Purchase Order SAP S/4HANA via OData  
> **PO Number:** 4500000004

[⬅️ Kembali ke Beranda](../README.md)

---

## 📌 Tentang Studi Kasus Ini

Dokumen ini mendemonstrasikan **4 pilar OOP (Bab I–IV)** menggunakan **data real** dari sistem SAP S/4HANA yang diakses melalui **OData REST API**.

### Sumber Data

| Parameter | Nilai |
|:----------|:------|
| SAP System | `https://sap.ilmuprogram.com` |
| Client | `777` |
| OData Services | `C_PURCHASEORDER_FS_SRV`, `MM_PUR_POITEMS_MONI_SRV` |
| PO Number | `4500000004` |
| PO Type | `NB` (Standard PO) |

### Data Purchase Order dari SAP

Berikut data real yang di-fetch dari SAP OData:

**PO Header** (Service: `C_PURCHASEORDER_FS_SRV/I_PurchaseOrder`):

| Field | Nilai |
|:------|:------|
| PurchaseOrder | `4500000004` |
| PurchaseOrderType | `NB` (Standard PO) |
| Supplier | `17300001` |
| CompanyCode | `1710` (Andi Coffee) |
| PurchasingOrganization | `1710` |
| PurchasingGroup | `001` |
| DocumentCurrency | `USD` |
| PaymentTerms | `0004` |
| CreatedByUser | `BUDILUHUR` |

**PO Item** (Service: `MM_PUR_POITEMS_MONI_SRV/C_PurchaseOrderItemMoni`):

| Field | Nilai |
|:------|:------|
| PurchaseOrderItem | `00010` |
| PurchaseOrderItemText | `PEMBELIAN LAPTOP` |
| SupplierName | `Wahyu Amaldi (Domestic Supplier)` |
| CompanyCodeName | `Andi Coffee` |
| MaterialGroup | `YBFA08` (Computer Hardware) |
| Plant | `1710` (Coffee Plant – Jakarta) |
| OrderQuantity | `10 PC` |
| NetPriceAmount | `302.00 USD` |
| NetAmount | `3,020.00 USD` |
| IsCompletelyDelivered | `true` ✅ |
| IsFinallyInvoiced | `false` ⏳ |

---

## 📡 OData API Endpoints

### 1. Fetch PO Header

```
GET /sap/opu/odata/sap/C_PURCHASEORDER_FS_SRV/I_PurchaseOrder('4500000004')?$format=json
Authorization: Basic <base64_credentials>
```

### 2. Fetch PO Items (Monitor)

```
GET /sap/opu/odata/sap/MM_PUR_POITEMS_MONI_SRV/C_PurchaseOrderItemMoni(P_DisplayCurrency='IDR')/Results?$filter=PurchaseOrder eq '4500000004'&$format=json
Authorization: Basic <base64_credentials>
```

### 3. Fetch PO Item Detail

```
GET /sap/opu/odata/sap/C_PURCHASEORDER_FS_SRV/I_PurchaseOrderItem?$filter=PurchaseOrder eq '4500000004'&$format=json
Authorization: Basic <base64_credentials>
```

---

## 📚 BAB I: Encapsulation

> **File:** [`samples/sap-odata/01_Encapsulation_PurchaseOrder.java`](../samples/sap-odata/01_Encapsulation_PurchaseOrder.java)

### Konsep yang Diterapkan

| Konsep | Penerapan di PO |
|:-------|:----------------|
| **Private fields** | `purchaseOrderNumber`, `supplier`, `sapPassword` — tidak bisa diakses langsung |
| **Getter** | `getPurchaseOrderNumber()`, `getNetAmount()` — akses terkontrol |
| **Setter + Validasi** | `setPurchaseOrderNumber()` — validasi 10 digit, `setOrderQuantity()` — harus > 0 |
| **Private method** | `formatCurrency()`, `isReadyToPost()`, `buildODataUrl()` — helper internal |
| **Data masking** | `getSapCredentialsMasked()` — password ter-mask, `getNomorRekening()` → `"******7890"` |

### Diagram Encapsulation

```
┌────────────────────────────────────────────────────┐
│              PurchaseOrder (Class)                  │
├────────────────────────────────────────────────────┤
│ 🔒 PRIVATE (tersembunyi)                          │
│   - purchaseOrderNumber: String                    │
│   - supplier: String                               │
│   - netPriceAmount: double                         │
│   - sapUsername: String                             │
│   - sapPassword: String  ← SANGAT SENSITIF!       │
│                                                    │
│   - formatCurrency(amount): String                 │
│   - isReadyToPost(): boolean                       │
│   - buildODataUrl(): String                        │
├────────────────────────────────────────────────────┤
│ 🔓 PUBLIC (bisa diakses)                          │
│   + getPurchaseOrderNumber(): String               │
│   + getNetAmount(): double   ← dihitung otomatis   │
│   + getSapCredentialsMasked(): String  ← ter-mask  │
│                                                    │
│   + setPurchaseOrderNumber(po): void  ← validasi!  │
│   + setOrderQuantity(qty): void       ← validasi!  │
│   + displaySummary(): void                         │
└────────────────────────────────────────────────────┘
```

### Poin Penting

1. **Data SAP sensitif** (credentials, price) disimpan `private`
2. **Validasi bisnis** di setter mencegah data invalid masuk ke SAP
3. **OData URL** dibangun secara internal — user tidak perlu tahu formatnya
4. **Net Amount** dihitung otomatis (`qty × price`), bukan disimpan manual

### Compile & Run

```bash
cd samples/sap-odata
javac 01_Encapsulation_PurchaseOrder.java
java PurchaseOrder
```

---

## 📚 BAB II: Inheritance

> **File:** [`samples/sap-odata/02_Inheritance_SAPDocument.java`](../samples/sap-odata/02_Inheritance_SAPDocument.java)

### Konsep yang Diterapkan

| Konsep | Penerapan |
|:-------|:----------|
| **Superclass** | `SAPDocument` — field & method umum semua dokumen SAP |
| **Subclass PO** | `SAPPurchaseOrder extends SAPDocument` — tambah supplier, item, delivery |
| **Subclass PR** | `SAPPurchaseRequisition extends SAPDocument` — tambah requisitioner, approval |
| **`super()`** | Constructor child memanggil constructor parent |
| **`@Override`** | `getDocumentDescription()`, `calculateTotal()`, `getStatus()` di-override |
| **Reuse** | `displayBasicInfo()` dari parent dipanggil via `super.displayBasicInfo()` |

### Hierarki Inheritance

```
                    SAPDocument (Superclass)
                    ┌───────────────────────────────┐
                    │ 📄 documentNumber              │
                    │ 📄 documentType                │
                    │ 📄 companyCode                 │
                    │ 📄 createdByUser               │
                    │ 📄 creationDate                │
                    │ 📄 documentCurrency            │
                    │ 📄 odataServiceName            │
                    │ 📄 odataEntitySet              │
                    │                               │
                    │ + getDocumentDescription()     │
                    │ + buildODataUrl()              │
                    │ + calculateTotal()             │
                    │ + getStatus()                  │
                    │ + displayBasicInfo()           │
                    └───────────┬───────────────────┘
                                │ extends
              ┌─────────────────┴──────────────────┐
              │                                    │
    ┌─────────┴────────────┐         ┌─────────────┴──────────┐
    │  SAPPurchaseOrder    │         │  SAPPurchaseRequisition │
    │                      │         │                        │
    │ 🆕 supplier          │         │ 🆕 requisitioner       │
    │ 🆕 supplierName      │         │ 🆕 isApproved          │
    │ 🆕 itemText          │         │ 🆕 isConvertedToPO     │
    │ 🆕 orderQuantity     │         │ 🆕 linkedPONumber      │
    │ 🆕 netPriceAmount    │         │ 🆕 estimatedPrice      │
    │ 🆕 isDelivered       │         │                        │
    │                      │         │ 🔄 getDescription()    │
    │ 🔄 getDescription()  │         │ 🔄 calculateTotal()    │
    │ 🔄 calculateTotal()  │         │ 🔄 getStatus()         │
    │ 🔄 getStatus()       │         │ 🆕 convertToPO()       │
    │ 🆕 displayFullDetails│         │ 🆕 displayFullDetails  │ 
    │                      │         │                        │
    │ OData Service:       │         │ OData Service:         │
    │ C_PURCHASEORDER_     │         │ MM_PUR_PR_             │
    │ FS_SRV               │         │ PROCESS_SRV            │
    └──────────────────────┘         └────────────────────────┘
```

### Alur Bisnis: PR → PO

```
  PR dibuat oleh user     PR di-approve      PR dikonversi ke PO
  ┌──────────────┐     ┌──────────────┐     ┌──────────────┐
  │ 📝 Status:   │ ──► │ 👍 Status:   │ ──► │ ✅ Status:   │
  │ Pending      │     │ Approved     │     │ Converted    │
  │ Approval     │     │ (Pending PO) │     │ to PO        │
  └──────────────┘     └──────────────┘     │ 4500000004   │
                                            └──────────────┘
```

### Compile & Run

```bash
cd samples/sap-odata
javac 02_Inheritance_SAPDocument.java
java SAPDocumentDemo
```

---

## 📚 BAB III: Polymorphism

> **File:** [`samples/sap-odata/03_Polymorphism_ODataProcessor.java`](../samples/sap-odata/03_Polymorphism_ODataProcessor.java)

### Konsep yang Diterapkan

| Konsep | Penerapan |
|:-------|:----------|
| **Interface** | `ODataProcessor` — kontrak method untuk semua processor |
| **Abstract class** | `SAPODataDocument` — blueprint dengan abstract methods |
| **Runtime Polymorphism** | `processAny(ODataProcessor)` — 1 method, 3 perilaku |
| **Method Overriding** | Setiap processor override `process()`, `formatOutput()`, `validate()` |
| **Method Overloading** | `search(String)`, `search(String, String)`, `search(double, double)` |
| **Polymorphic collection** | `List<ODataProcessor>` menampung tipe berbeda |

### 3 Implementasi Processor

```
              ODataProcessor (Interface)
              ┌───────────────────────────┐
              │ + getServiceName()        │
              │ + getEntitySet()          │
              │ + buildUrl()             │
              │ + process()    ← KUNCI!  │
              │ + formatOutput()         │
              └─────────┬─────────────────┘
                        │ implements
         ┌──────────────┼──────────────────┐
         │              │                  │
    ┌────┴─────┐  ┌─────┴──────┐  ┌───────┴──────┐
    │ POHeader │  │  POItem    │  │   POGoods    │
    │ Processor│  │  Processor │  │   Receipt    │
    │          │  │            │  │   Processor  │
    ├──────────┤  ├────────────┤  ├──────────────┤
    │Service:  │  │Service:    │  │Service:      │
    │C_PURCHASE│  │MM_PUR_     │  │C_PURCHASE    │
    │ORDER_FS_ │  │POITEMS_    │  │ORDER_FS_     │
    │SRV       │  │MONI_SRV    │  │SRV           │
    ├──────────┤  ├────────────┤  ├──────────────┤
    │Entity:   │  │Entity:     │  │Entity:       │
    │I_Purchase│  │C_Purchase  │  │C_Purchase    │
    │Order     │  │OrderItem   │  │OrderGoods    │
    │          │  │Moni        │  │Receipt       │
    ├──────────┤  ├────────────┤  ├──────────────┤
    │process():│  │process():  │  │process():    │
    │Show hdr  │  │Show item + │  │Show rcvd qty │
    │info      │  │calc total  │  │vs ordered    │
    └──────────┘  └────────────┘  └──────────────┘
```

### Method Overloading (Compile-time Polymorphism)

```java
// Sama nama "search()" tapi beda parameter → OVERLOADING
item.search("4500000004");                          // → search by PO
item.search("4500000004", "00010");                 // → search by PO + Item
item.search("4500000004", "YBFA08", "1710");        // → search by PO + MatGroup + Plant
item.search(5.0, 20.0);                             // → search by qty range
```

### Compile & Run

```bash
cd samples/sap-odata
javac 03_Polymorphism_ODataProcessor.java
java ODataProcessorDemo
```

---

## 📚 BAB IV: Abstraction

> **File:** [`samples/sap-odata/04_Abstraction_ODataService.java`](../samples/sap-odata/04_Abstraction_ODataService.java)

### Konsep yang Diterapkan

| Konsep | Penerapan |
|:-------|:----------|
| **Abstract class** | `ODataService` — blueprint koneksi OData |
| **Interface `Printable`** | `printSummary()`, `printDetailed()` — print tanpa tahu caranya |
| **Interface `Exportable`** | `exportToJson()`, `exportToCsv()` — export tanpa tahu formatnya |
| **Interface `Validatable`** | `isValid()`, `getValidationErrors()` — validasi tanpa tahu rulesnya |
| **Template Method** | `fetchData()` — urutan langkah fix, detail di subclass |
| **Concrete class** | `PurchaseOrderService` — implementasi nyata yang tersembunyi |

### Abstraksi: Apa yang User LIHAT vs TERSEMBUNYI

```
  ┌─────────────────────────────┐     ┌──────────────────────────────────┐
  │   APA YANG USER LIHAT       │     │   APA YANG TERSEMBUNYI           │
  │   (Abstraksi / Interface)   │     │   (Implementasi / Detail)        │
  ├─────────────────────────────┤     ├──────────────────────────────────┤
  │                             │     │                                  │
  │  poService.loadPO("4500..") │ ──► │  HTTP connect ke SAP server      │
  │                             │     │  Build Basic Auth header         │
  │                             │     │  Send GET request ke OData       │
  │                             │     │  Parse JSON response             │
  │                             │     │  Map ke Java objects             │
  ├─────────────────────────────┤     ├──────────────────────────────────┤
  │  poService.printSummary()   │ ──► │  String.format() 15+ fields     │
  │                             │     │  Box drawing characters          │
  │                             │     │  Currency formatting             │
  ├─────────────────────────────┤     ├──────────────────────────────────┤
  │  poService.exportToJson()   │ ──► │  Text block formatting           │
  │                             │     │  String escaping                 │
  │                             │     │  Number formatting               │
  ├─────────────────────────────┤     ├──────────────────────────────────┤
  │  poService.isValid()        │ ──► │  Check PO number not null        │
  │                             │     │  Check supplier not empty        │
  │                             │     │  Check quantity > 0              │
  │                             │     │  Check price >= 0                │
  │                             │     │  Collect all errors              │
  └─────────────────────────────┘     └──────────────────────────────────┘
```

### Multiple Interface Implementation

```
                            PurchaseOrderService
                        ┌──────────────────────────┐
                        │                          │
        extends         │   ODataService           │
        ┌───────────────│   (Abstract Class)       │
        │               │                          │
        │  implements   │   Printable              │
        ├───────────────│   (Interface)            │
        │               │                          │
        │  implements   │   Exportable             │
        ├───────────────│   (Interface)            │
        │               │                          │
        │  implements   │   Validatable            │
        ├───────────────│   (Interface)            │
        │               │                          │
        │               └──────────────────────────┘
        │
        │  Satu class bisa:
        │  ✅ Konek ke OData (dari ODataService)
        │  ✅ Dicetak (dari Printable)
        │  ✅ Diekspor (dari Exportable)
        │  ✅ Divalidasi (dari Validatable)
```

### Compile & Run

```bash
cd samples/sap-odata
javac 04_Abstraction_ODataService.java
java ODataServiceDemo
```

---

## 🔗 Mapping OOP ke SAP OData

| Pilar OOP | Analogi SAP OData |
|:----------|:------------------|
| **Encapsulation** | Field PO private, akses via getter/setter, credentials ter-mask |
| **Inheritance** | `SAPDocument` → `PurchaseOrder`, `PurchaseRequisition` (shared fields) |
| **Polymorphism** | `ODataProcessor.process()` — beda behavior per service (Header vs Item vs GR) |
| **Abstraction** | `ODataService.fetchData()` — user panggil 1 method, detail HTTP/auth tersembunyi |

---

## 📂 Struktur File

```
samples/sap-odata/
├── 01_Encapsulation_PurchaseOrder.java    ← Bab I
├── 02_Inheritance_SAPDocument.java        ← Bab II
├── 03_Polymorphism_ODataProcessor.java    ← Bab III
└── 04_Abstraction_ODataService.java       ← Bab IV
```

---

## 🧪 Response JSON SAP (Reference)

### PO Header Response

```json
{
  "d": {
    "PurchaseOrder": "4500000004",
    "PurchaseOrderType": "NB",
    "Supplier": "17300001",
    "CompanyCode": "1710",
    "PurchasingOrganization": "1710",
    "PurchasingGroup": "001",
    "DocumentCurrency": "USD",
    "PaymentTerms": "0004",
    "CreatedByUser": "BUDILUHUR",
    "CreationDate": "/Date(1760832000000)/",
    "PurchasingProcessingStatus": "02",
    "LastChangeDateTime": "/Date(1760889628496+0000)/"
  }
}
```

### PO Item Response

```json
{
  "d": {
    "results": [
      {
        "PurchaseOrder": "4500000004",
        "PurchaseOrderItem": "00010",
        "PurchaseOrderTypeName": "Standard PO",
        "Supplier": "17300001",
        "SupplierName": "Wahyu Amaldi (Domestic Supplier)",
        "CompanyCode": "1710",
        "CompanyCodeName": "Andi Coffee",
        "PurchaseOrderItemText": "PEMBELIAN LAPTOP",
        "MaterialGroup": "YBFA08",
        "MaterialGroupName": "Computer Hardware",
        "Plant": "1710",
        "PlantName": "Coffee Plant – Jakarta",
        "OrderQuantity": "10",
        "NetPriceAmount": "302.00",
        "NetAmount": "3020.00",
        "OrderPriceUnit": "PC",
        "DocumentCurrency": "USD",
        "IsCompletelyDelivered": true,
        "IsFinallyInvoiced": false,
        "GoodsReceiptQty": "10",
        "InvoiceReceiptQty": "10",
        "AccountAssignmentCategory": "A",
        "AcctAssignmentCategoryName": "Asset"
      }
    ]
  }
}
```

---

## 📖 Cara Menjalankan Semua Contoh

```bash
cd samples/sap-odata

# Bab I: Encapsulation
javac 01_Encapsulation_PurchaseOrder.java && java PurchaseOrder

# Bab II: Inheritance
javac 02_Inheritance_SAPDocument.java && java SAPDocumentDemo

# Bab III: Polymorphism
javac 03_Polymorphism_ODataProcessor.java && java ODataProcessorDemo

# Bab IV: Abstraction
javac 04_Abstraction_ODataService.java && java ODataServiceDemo
```

---

> **Catatan:** Data pada dokumen ini diambil langsung dari sistem SAP via OData pada April 2026. PO `4500000004` adalah Purchase Order untuk pembelian 10 unit laptop senilai **3,020.00 USD** dari supplier **Wahyu Amaldi (Domestic Supplier)** untuk perusahaan **Andi Coffee** di plant **Coffee Plant – Jakarta**.
