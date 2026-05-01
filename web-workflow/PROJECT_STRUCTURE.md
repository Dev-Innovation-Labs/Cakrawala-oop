# 📁 Project Structure Guide

Penjelasan lengkap struktur direktori dan file-file dalam proyek Workflow Engine Web.

## 📂 Tree Structure

```
web-workflow/
│
├── 📄 pom.xml                          ⭐ Maven configuration
├── 📄 README.md                        ⭐ Project documentation
├── 📄 DEPLOYMENT.md                    ⭐ Deployment guide
├── 📄 PROJECT_STRUCTURE.md             ← File ini
├── 📄 .gitignore                       Git ignore file
│
├── 🔧 Scripts
│   ├── run.sh                         Quick start (Linux/macOS)
│   ├── run.bat                        Quick start (Windows)
│   └── build.sh                       Build script with checks
│
└── src/
    ├── main/
    │   ├── java/
    │   │   └── id/universitas/cakrawala/
    │   │       ├── 🎯 WorkflowEngineApplication.java      Main entry point
    │   │       │
    │   │       ├── domain/                 Domain Objects
    │   │       │   ├── LeaveStatus.java     Enum: DRAFT, DIAJUKAN, dll
    │   │       │   └── LeaveRequest.java    Model: Pengajuan cuti
    │   │       │
    │   │       ├── workflow/                Workflow Engine Core
    │   │       │   ├── Guard.java           Interface: Validasi
    │   │       │   ├── TransitionEffect.java Interface: Aksi samping
    │   │       │   ├── Transition.java      Definisi transisi
    │   │       │   ├── LeaveWorkflowEngine.java ⭐ State machine engine
    │   │       │   │
    │   │       │   ├── guards/             Guard implementations
    │   │       │   │   ├── CutiCukupGuard.java
    │   │       │   │   ├── TanggalValidGuard.java
    │   │       │   │   └── MinimalH3Guard.java
    │   │       │   │
    │   │       │   └── effects/            Effect implementations
    │   │       │       └── KurangiSisaCutiEffect.java
    │   │       │
    │   │       ├── service/                 Service Layer
    │   │       │   └── LeaveRequestService.java ⭐ Business logic
    │   │       │
    │   │       ├── controller/              Controllers
    │   │       │   ├── PageController.java   HTML pages
    │   │       │   └── LeaveApiController.java ⭐ REST API
    │   │       │
    │   │       └── dto/                     Data Transfer Objects
    │   │           └── LeaveRequestDTO.java Data mapping
    │   │
    │   └── resources/
    │       ├── application.properties      Spring config
    │       │
    │       ├── templates/                  HTML Templates
    │       │   ├── index.html              Beranda
    │       │   ├── dashboard.html          Dashboard
    │       │   ├── create.html             Form ajukan cuti
    │       │   └── docs-api.html           API documentation
    │       │
    │       └── static/                     Static Files
    │           ├── css/
    │           │   └── style.css           Global styling
    │           └── js/
    │               ├── dashboard.js        Dashboard logic
    │               └── create.js           Form handling
    │
    └── test/                               Unit tests
        └── java/...                        (can add tests here)

```

---

## 📝 File Descriptions

### Root Level Files

#### `pom.xml`
Maven configuration file yang mendefinisikan:
- Dependencies (Spring Boot, Lombok, etc)
- Build plugins
- Java version (17)

**Key Dependencies:**
- `spring-boot-starter-web` - Web framework
- `spring-boot-starter-thymeleaf` - HTML template engine
- `spring-boot-devtools` - Live reload during development

#### `README.md`
Dokumentasi lengkap proyek termasuk:
- Deskripsi project
- Struktur folder
- Cara menjalankan
- Contoh API usage
- Poin pembelajaran OOP

#### `DEPLOYMENT.md`
Panduan deployment dan production setup:
- Build instructions
- Configuration options
- Docker deployment
- Performance tuning
- Troubleshooting

#### `.gitignore`
File yang di-exclude dari version control:
- Maven build artifacts (`target/`)
- IDE files (`.idea/`, `.vscode/`)
- Logs

#### Scripts
- `run.sh` - Quick start script untuk Linux/macOS
- `run.bat` - Quick start script untuk Windows
- `build.sh` - Build script dengan validasi

---

## 🎯 Java Source Files

### Domain Layer (`domain/`)

#### `LeaveStatus.java`
Enum yang mendefinisikan semua status yang mungkin:
```java
DRAFT              // Status awal
DIAJUKAN          // Sudah dikirim ke atasan
DISETUJUI_ATASAN  // Disetujui oleh atasan
DISETUJUI_HR      // Disetujui oleh HR
DITOLAK           // Pengajuan ditolak
DIBATALKAN        // Pengajuan dibatalkan
```

Setiap status memiliki:
- `label` - Display name (ID)
- `color` - UI color code

#### `LeaveRequest.java`
Domain model yang merepresentasikan pengajuan cuti:
```java
id              // UUID unik
nama            // Nama karyawan
email           // Email
mulai           // Tanggal mulai
selesai         // Tanggal selesai
status          // Current status
sisaCuti        // Sisa hari cuti
alasanTolak     // Alasan jika ditolak
dibuat          // Timestamp creation
```

---

### Workflow Engine (`workflow/`)

#### `Guard.java` (Interface)
Interface untuk validasi syarat transisi:
```java
boolean boleh(LeaveRequest req)     // Return true jika valid
String alasanGagal()                // Message jika gagal
```

#### `TransitionEffect.java` (Interface)
Interface untuk aksi samping setelah transisi:
```java
void execute(LeaveRequest req)  // Execute side effect
```

#### `Transition.java`
Class yang mendefinisikan satu transisi:
```java
nama        // Nama transisi (misal: "ajukan")
dari        // Status asal (DRAFT)
ke          // Status tujuan (DIAJUKAN)
guards      // List<Guard> - validasi
effects     // List<TransitionEffect> - aksi
```

#### `LeaveWorkflowEngine.java` ⭐
Core workflow engine yang menjalankan state machine:
```java
void daftarTransisi(Transition t)                // Register transisi
TransitionResult jalankan(req, namaTransisi)    // Execute workflow
```

**Process:**
1. Find matching transition
2. Validate all guards
3. Change status
4. Execute all effects
5. Return result

#### Guard Implementations

**`CutiCukupGuard.java`**
- Validasi: `sisaCuti >= jumlahHari`

**`TanggalValidGuard.java`**
- Validasi: `selesai >= mulai`

**`MinimalH3Guard.java`**
- Validasi: `mulai >= hari ini + 3 hari`

#### Effect Implementations

**`KurangiSisaCutiEffect.java`**
- Kurangi `sisaCuti` sebesar `jumlahHari`

---

### Service Layer (`service/`)

#### `LeaveRequestService.java` ⭐
Business logic layer:
```java
createRequest(req)          // Buat request baru
getRequest(id)              // Get by ID
getAllRequests()            // Get all requests
transisi(id, namaTransisi)  // Execute workflow
```

**Responsibilities:**
- Manage in-memory storage (HashMap)
- Initialize workflow engine
- Handle service-level operations

---

### Controller Layer (`controller/`)

#### `PageController.java`
Routes untuk HTML pages:
- `GET /` → index.html
- `GET /dashboard` → dashboard.html
- `GET /create` → create.html
- `GET /docs/api` → docs-api.html

#### `LeaveApiController.java` ⭐
REST API endpoints:
```
GET    /api/leave/health         // Health check
GET    /api/leave/list           // Get all requests
GET    /api/leave/{id}           // Get by ID
POST   /api/leave/create         // Create new
POST   /api/leave/{id}/transisi  // Execute transisi
GET    /api/leave/status/list    // Get all statuses
```

---

### DTO Layer (`dto/`)

#### `LeaveRequestDTO.java`
Data Transfer Object untuk API response:
- Convert `LeaveRequest` → JSON
- Include computed fields (statusLabel, statusColor)
- Serialization-friendly

---

## 🎨 Frontend Files

### Templates (`resources/templates/`)

#### `index.html`
Halaman beranda:
- Hero section
- Feature cards
- Workflow diagram
- Code examples
- CTA buttons

#### `dashboard.html`
Dashboard requests:
- Statistics cards
- Requests table
- Detail modal
- Action buttons (transisi)

#### `create.html`
Form ajukan cuti:
- Input fields
- Validation info
- Success/error messages

#### `docs-api.html`
API documentation:
- Endpoint descriptions
- Request/response examples
- cURL examples
- Error handling
- Status codes

### Static Files

#### `css/style.css` ⭐
Global stylesheet:
- CSS variables untuk colors
- Component styles
- Responsive design
- Animations
- ~500 lines

#### `js/dashboard.js`
Dashboard functionality:
- Load requests
- Display in table
- Show detail modal
- Handle transitions
- Update statistics

#### `js/create.js`
Form handling:
- Form submission
- Validation
- API call
- Success/error feedback

---

## 🔄 Data Flow

### Happy Path: Membuat & Menyetujui Cuti

```
1. User Input (create.html form)
   ↓
2. POST /api/leave/create
   ↓
3. LeaveApiController.createRequest()
   ↓
4. LeaveRequestService.createRequest()
   ↓
5. Store in HashMap + return LeaveRequestDTO
   ↓
6. Success message shown in UI
   ↓
7. User navigates to dashboard
   ↓
8. GET /api/leave/list
   ↓
9. Display table with requests
   ↓
10. User clicks transisi button
    ↓
11. POST /api/leave/{id}/transisi
    ↓
12. LeaveApiController.transisi()
    ↓
13. LeaveRequestService.transisi()
    ↓
14. LeaveWorkflowEngine.jalankan()
    ↓
15. Find transition + validate guards + execute effects
    ↓
16. Return result to controller
    ↓
17. Return JSON response
    ↓
18. UI shows success/error + refresh table
```

---

## 📊 Class Relationships

```
┌─────────────────────────────────────────────────────────┐
│  LeaveRequest (Domain)                                  │
│  - id, nama, email, mulai, selesai, status, sisaCuti   │
└────────────┬────────────────────────────────────────────┘
             │
             │ uses
             ▼
┌─────────────────────────────────────────────────────────┐
│  LeaveWorkflowEngine (State Machine)                    │
│  - Transition[]                                          │
│  - Transition { Guard[], TransitionEffect[] }           │
└─────────────────────────────────────────────────────────┘
             │
             │ used by
             ▼
┌─────────────────────────────────────────────────────────┐
│  LeaveRequestService (Business Logic)                   │
│  - LeaveRequest repository (HashMap)                    │
│  - LeaveWorkflowEngine engine                           │
└────────────┬────────────────────────────────────────────┘
             │
             │ used by
             ▼
┌─────────────────────────────────────────────────────────┐
│  LeaveApiController (REST API)                          │
│  - LeaveRequestService service                          │
└─────────────────────────────────────────────────────────┘
             │
             │ returns
             ▼
┌─────────────────────────────────────────────────────────┐
│  LeaveRequestDTO (Data Transfer)                        │
│  - Maps to/from LeaveRequest                            │
└─────────────────────────────────────────────────────────┘
```

---

## 🔧 Configuration

#### `application.properties`
```properties
spring.application.name=workflow-engine-web
server.port=8080
server.servlet.context-path=/
spring.thymeleaf.prefix=classpath:/templates/
spring.thymeleaf.suffix=.html
logging.level.id.universitas.cakrawala=DEBUG
```

---

## 🚀 Build & Run Flow

### Build Process

```
pom.xml (define dependencies)
   ↓
mvn clean package
   ↓
1. Clean previous build
2. Resolve dependencies
3. Compile Java sources
4. Run tests
5. Package into JAR
   ↓
target/workflow-engine-web-1.0.0.jar
```

### Run Process

```
java -jar workflow-engine-web-1.0.0.jar
   ↓
1. Spring Boot starts embedded Tomcat
2. Initialize application context
3. Scan and register beans
4. Initialize LeaveRequestService
5. Start REST API endpoints
   ↓
Server running on http://localhost:8080
```

---

## 📈 Key Files for Understanding OOP Concepts

1. **Encapsulation**: LeaveRequest, LeaveStatus
   - Data bundled with methods
   - Private fields with getters/setters

2. **Inheritance**: Guard, TransitionEffect interfaces
   - Multiple implementations inherit from interface
   - CutiCukupGuard, TanggalValidGuard, etc

3. **Polymorphism**: Different Guard implementations
   - Each has own `boleh()` logic
   - Called polymorphically in engine

4. **Abstraction**: LeaveWorkflowEngine
   - Hides complexity of state machine
   - Users call `jalankan()` without knowing internal details

5. **Design Patterns**:
   - State Pattern: LeaveWorkflowEngine
   - Strategy Pattern: Guard & TransitionEffect
   - Repository Pattern: LeaveRequestService
   - DTO Pattern: LeaveRequestDTO

---

## 📖 For Learning

**Start reading files in this order:**

1. `README.md` - Understand what the project does
2. `LeaveStatus.java` - Simple enum
3. `LeaveRequest.java` - Domain model
4. `Guard.java` & implementations - Strategy pattern
5. `Transition.java` - Transition definition
6. `LeaveWorkflowEngine.java` - Core logic (hardest!)
7. `LeaveRequestService.java` - Service layer
8. `LeaveApiController.java` - REST endpoints
9. HTML/JS files - Frontend

---

## 🔍 File Sizes Reference

- `pom.xml` - ~80 lines
- `LeaveStatus.java` - ~25 lines
- `LeaveRequest.java` - ~60 lines
- `Guard.java` - ~8 lines
- `LeaveWorkflowEngine.java` - ~65 lines
- `LeaveRequestService.java` - ~90 lines
- `LeaveApiController.java` - ~120 lines
- `style.css` - ~500 lines
- `index.html` - ~120 lines
- `dashboard.html` - ~80 lines
- `dashboard.js` - ~150 lines

---

*Last updated: 2024*
