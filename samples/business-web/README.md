# Business Object Web Simulator

Interactive web application untuk simulasi **Rich Domain Model** dan **Business Process** dari file `samples/business/PurchaseOrder.java` menggunakan Spring Boot dan Java.

## Fitur

✅ **Purchase Order Management** - Buat, kelola, dan track PO dari awal sampai approval
- Nomor PO unik otomatis
- Input vendor dinamis
- Validasi item dan harga

✅ **Business Rules Automation** - Aturan bisnis terintegrasi dalam objek
- Maksimum 20 item per PO
- Approval otomatis jika nilai < Rp 50 juta
- Approval required jika nilai > Rp 50 juta

✅ **Status Workflow** - Transisi status otomatis
- DRAFT → MENUNGGU_APPROVAL → DISETUJUI
- Status tracking dengan badge visual

✅ **Rich Domain Model** - Objek bisnis yang "pintar"
- Self-validating objects
- Encapsulated business logic
- Value Objects yang immutable

## Tech Stack

- **Java 17** - Java Development Kit
- **Spring Boot 3.2.0** - Web Framework
- **Maven** - Build Tool
- **Thymeleaf** - Template Engine
- **HTML5 + CSS3 + JavaScript** - Frontend

## Struktur Project

```
samples/business-web/
├── src/main/java/id/universitas/cakrawala/
│   ├── BusinessObjectApplication.java       # Main Application
│   ├── controller/
│   │   └── BusinessObjectController.java    # REST API Controller
│   ├── service/
│   │   └── PengadaanService.java           # Application Service
│   ├── domain/
│   │   ├── POItem.java                     # Value Object
│   │   └── PurchaseOrderBO.java            # Business Object
│   └── dto/
│       └── PurchaseOrderDTO.java           # Data Transfer Object
├── src/main/resources/
│   ├── application.properties               # Configuration
│   └── templates/
│       ├── index.html                       # Home Page
│       └── simulator.html                   # Simulator Page
├── pom.xml                                  # Maven Configuration
├── run.sh                                   # Linux/Mac Startup
└── run.bat                                  # Windows Startup
```

## Cara Menjalankan

### Linux / macOS

```bash
cd samples/business-web
bash run.sh
```

### Windows

```bash
cd samples\business-web
run.bat
```

### Manual dengan Maven

```bash
export JAVA_HOME=/usr/local/Cellar/openjdk@17/17.0.19/libexec/openjdk.jdk/Contents/Home
mvn spring-boot:run
```

## Akses Aplikasi

Setelah aplikasi berjalan, akses di:

- **Home**: http://localhost:8082
- **Simulator**: http://localhost:8082/simulator

## API Endpoints

### POST /api/po/buat
Buat Purchase Order baru

**Request:**
```
POST /api/po/buat
Content-Type: application/x-www-form-urlencoded

vendor=PT Maju Terus
```

**Response:**
```json
{
  "nomorPO": "PO-1704067200000-1",
  "vendor": "PT Maju Terus",
  "tanggal": "2026-05-02",
  "status": "DRAFT",
  "items": [],
  "total": 0,
  "totalFormat": "Rp 0",
  "perluApproval": false
}
```

### GET /api/po/{nomorPO}
Ambil data PO

### POST /api/po/{nomorPO}/item
Tambah item ke PO

**Parameters:**
- `namaBarang`: Nama barang
- `jumlah`: Jumlah item
- `harga`: Harga satuan

### POST /api/po/{nomorPO}/submit
Submit PO untuk approval (jika nilai > 50jt) atau langsung disetujui

### POST /api/po/{nomorPO}/approve
Approve PO yang menunggu approval

**Parameters:**
- `approver`: Nama approver/manager

### DELETE /api/po/{nomorPO}/item/{index}
Hapus item dari PO (hanya untuk draft)

## Business Logic

### Rich Domain Model (PurchaseOrderBO)

```java
// Validasi saat konstruksi
PurchaseOrderBO po = new PurchaseOrderBO("PO-001", "Vendor ABC");

// Tambah item dengan validasi
po.tambahItem("Laptop", 2, 10_000_000);
po.tambahItem("Mouse", 5, 100_000);

// Hitung total otomatis
double total = po.hitungTotal(); // 20.5 juta

// Cek apakah perlu approval (> 50 juta)
boolean needsApproval = po.perluApproval(); // false

// Submit dengan status otomatis
po.submit(); // Status berubah ke DISETUJUI

// Untuk PO > 50 juta:
po2.submit(); // Status berubah ke MENUNGGU_APPROVAL
po2.approve("Manajer Budi"); // Status berubah ke DISETUJUI
```

### Value Object (POItem)

```java
// Immutable value object
POItem item = new POItem("Kertas A4", 50, 45_000);
double subtotal = item.getSubtotal(); // 2.25 juta
```

### Application Service (PengadaanService)

Orkestrator proses bisnis yang menggunakan domain objects.

## Requirement

- Java 17+
- Maven 3.6+
- Browser modern (Chrome, Firefox, Safari, Edge)

## Author

Universitas Cakrawala - Business Object Learning Platform

## Port

Aplikasi berjalan di **port 8082** (berbeda dengan design-patterns-web di port 8081)
