# 🔄 OOP Workflow Engine - Simple Website

**Universitas Cakrawala** - Object-Oriented Programming  
Sistem Manajemen Pengajuan Cuti berbasis State Machine

---

## 📋 Deskripsi

Aplikasi web sederhana yang mendemonstrasikan konsep **OOP Workflow Engine** menggunakan:

- **State Machine**: Status (DRAFT, DIAJUKAN, DISETUJUI_ATASAN, DISETUJUI_HR, DITOLAK, DIBATALKAN)
- **Guard Pattern**: Validasi syarat sebelum transisi (cuti cukup, tanggal valid, H-3)
- **Effect Pattern**: Aksi setelah transisi (kurangi sisa cuti)
- **Spring Boot**: Framework Java modern untuk web application
- **REST API**: Interface untuk operasi CRUD dan workflow management

---

## 🏗️ Struktur Proyek

```
web-workflow/
├── pom.xml                          # Maven configuration
├── src/
│   ├── main/
│   │   ├── java/id/universitas/cakrawala/
│   │   │   ├── WorkflowEngineApplication.java   # Main entry point
│   │   │   ├── domain/
│   │   │   │   ├── LeaveStatus.java            # Enum status
│   │   │   │   └── LeaveRequest.java           # Domain model
│   │   │   ├── workflow/
│   │   │   │   ├── Guard.java                  # Interface guard
│   │   │   │   ├── TransitionEffect.java       # Interface effect
│   │   │   │   ├── Transition.java             # Definisi transisi
│   │   │   │   ├── LeaveWorkflowEngine.java    # Engine utama
│   │   │   │   ├── guards/                     # Implementasi guards
│   │   │   │   │   ├── CutiCukupGuard.java
│   │   │   │   │   ├── TanggalValidGuard.java
│   │   │   │   │   └── MinimalH3Guard.java
│   │   │   │   └── effects/
│   │   │   │       └── KurangiSisaCutiEffect.java
│   │   │   ├── service/
│   │   │   │   └── LeaveRequestService.java    # Business logic
│   │   │   ├── controller/
│   │   │   │   ├── LeaveApiController.java     # REST API
│   │   │   │   └── PageController.java         # Web pages
│   │   │   └── dto/
│   │   │       └── LeaveRequestDTO.java        # Data transfer object
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── templates/
│   │       │   ├── index.html                  # Halaman beranda
│   │       │   ├── dashboard.html              # Dashboard requests
│   │       │   └── create.html                 # Form buat request
│   │       └── static/
│   │           ├── css/style.css               # Stylesheet
│   │           └── js/
│   │               ├── dashboard.js            # Dashboard logic
│   │               └── create.js               # Form submission
│   └── test/
└── README.md
```

---

## 🎯 Fitur

### 1. **Halaman Beranda** (`/`)
- Pengenalan Workflow Engine
- Diagram alur workflow
- Arsitektur OOP
- Link ke Dashboard dan Form Pengajuan

### 2. **Halaman Dashboard** (`/dashboard`)
- Daftar semua pengajuan cuti
- Statistik (total, diajukan, disetujui, ditolak)
- Detail pengajuan dengan modal
- Tombol aksi untuk transisi status
  - **Dari DRAFT**: Ajukan, Batalkan
  - **Dari DIAJUKAN**: Setujui (Atasan), Tolak
  - **Dari DISETUJUI_ATASAN**: Setujui (HR)

### 3. **Halaman Ajukan Cuti** (`/create`)
- Form untuk membuat pengajuan cuti baru
- Validasi form di client-side
- Notifikasi sukses/gagal
- Redirect ke dashboard setelah berhasil

### 4. **REST API** (`/api/leave/*`)
- `GET /api/leave/list` - Daftar semua pengajuan
- `GET /api/leave/{id}` - Detail pengajuan
- `POST /api/leave/create` - Buat pengajuan baru
- `POST /api/leave/{id}/transisi` - Jalankan transisi workflow
- `GET /api/leave/status/list` - Daftar status yang tersedia
- `GET /api/leave/health` - Health check

---

## 🚀 Cara Menjalankan

### Prasyarat
- Java 17+
- Maven 3.8+

### 1. Build Aplikasi

```bash
cd web-workflow
mvn clean package
```

### 2. Jalankan Aplikasi

```bash
# Menggunakan Maven
mvn spring-boot:run

# Atau menjalankan JAR
java -jar target/workflow-engine-web-1.0.0.jar
```

### 3. Akses di Browser

Buka browser dan navigasi ke:
- **Beranda**: http://localhost:8080/
- **Dashboard**: http://localhost:8080/dashboard
- **Ajukan Cuti**: http://localhost:8080/create
- **API Health**: http://localhost:8080/api/leave/health

---

## 📊 Contoh Penggunaan

### 1. Membuat Pengajuan Cuti

**POST** `/api/leave/create`

```json
{
  "nama": "Andi Wijaya",
  "email": "andi@example.com",
  "mulai": "2026-05-10",
  "selesai": "2026-05-15",
  "sisaCuti": 12
}
```

**Response** (201 Created):
```json
{
  "id": "a1b2c3d4-e5f6-4g7h-i8j9-k0l1m2n3o4p5",
  "nama": "Andi Wijaya",
  "email": "andi@example.com",
  "mulai": "2026-05-10",
  "selesai": "2026-05-15",
  "status": "DRAFT",
  "statusLabel": "Draf",
  "statusColor": "#gray",
  "sisaCuti": 12,
  "jumlahHari": 6,
  "alasanTolak": null,
  "dibuat": "2026-05-01"
}
```

### 2. Ajukan Pengajuan ke Atasan

**POST** `/api/leave/{id}/transisi`

```json
{
  "transisi": "ajukan"
}
```

**Response** (200 OK):
```json
{
  "sukses": true,
  "pesan": "DRAFT → Diajukan",
  "data": {
    "id": "a1b2c3d4-e5f6-4g7h-i8j9-k0l1m2n3o4p5",
    "status": "DIAJUKAN",
    "statusLabel": "Diajukan",
    "sisaCuti": 12,
    ...
  }
}
```

### 3. Setujui oleh Atasan

**POST** `/api/leave/{id}/transisi`

```json
{
  "transisi": "approve_atasan"
}
```

### 4. Setujui oleh HR (dan Kurangi Sisa Cuti)

**POST** `/api/leave/{id}/transisi`

```json
{
  "transisi": "approve_hr"
}
```

Setelah disetujui HR:
- Status berubah menjadi `DISETUJUI_HR`
- Sisa cuti otomatis dikurangi sebesar jumlah hari yang diajukan

---

## 🏗️ Arsitektur OOP

### State Machine Pattern

```
┌─────────────────────────────────────────────────────────┐
│                 LEAVE REQUEST                            │
├─────────────────────────────────────────────────────────┤
│ id          │ UUID unique identifier                     │
│ nama        │ Nama karyawan                              │
│ mulai       │ Tanggal mulai cuti                         │
│ selesai     │ Tanggal selesai cuti                       │
│ status      │ Current state (DRAFT, DIAJUKAN, dsb)       │
│ sisaCuti    │ Sisa hari cuti tersedia                    │
└─────────────────────────────────────────────────────────┘
```

### Transition & Validation

**Guard Pattern** - Validasi sebelum transisi:
- `CutiCukupGuard`: Cek sisa cuti cukup untuk jumlah hari
- `TanggalValidGuard`: Cek tanggal valid (selesai >= mulai)
- `MinimalH3Guard`: Cek pengajuan minimal H-3

**Effect Pattern** - Aksi setelah transisi:
- `KurangiSisaCutiEffect`: Kurangi sisa cuti setelah persetujuan

### State Diagram

```
┌─────────┐  ajukan  ┌──────────────┐  approve_atasan  ┌────────────────┐
│  DRAFT  │─────────>│  DIAJUKAN    │────────────────>│ DISETUJUI_ATASAN│
└─────────┘          └──────┬───────┘                 └────────┬────────┘
    ▲                       │                                   │
    │ batalkan              │ tolak                            │ approve_hr
    │                       ▼                                   ▼
    │               ┌──────────────┐                 ┌─────────────────┐
    └───────────────│   DITOLAK    │                 │  DISETUJUI_HR   │
                    └──────────────┘                 └─────────────────┘
```

---

## 📚 Kelas-Kelas Utama

### Domain Objects
- **`LeaveRequest`**: Entitas pengajuan cuti
- **`LeaveStatus`**: Enum untuk status

### Workflow Engine
- **`Guard`**: Interface untuk validasi
- **`TransitionEffect`**: Interface untuk aksi samping
- **`Transition`**: Definisi transisi antar status
- **`LeaveWorkflowEngine`**: Engine yang menjalankan state machine

### Service Layer
- **`LeaveRequestService`**: Business logic, manajemen request dan workflow

### REST API
- **`LeaveApiController`**: Endpoints untuk operasi CRUD dan workflow

---

## 🔄 Workflow Lifecycle

### Happy Path (Persetujuan Penuh)

1. **Create**: User membuat pengajuan → Status = `DRAFT`
2. **Submit**: User mengajukan → Status = `DIAJUKAN`
   - Guards: Cuti cukup? Tanggal valid? H-3?
3. **Manager Approve**: Atasan menyetujui → Status = `DISETUJUI_ATASAN`
4. **HR Approve**: HR menyetujui → Status = `DISETUJUI_HR`
   - Effects: Sisa cuti dikurangi

### Rejection Path

1. **Create**: User membuat pengajuan → Status = `DRAFT`
2. **Submit**: User mengajukan → Status = `DIAJUKAN`
3. **Reject**: Atasan menolak → Status = `DITOLAK`
   - Sisa cuti tetap

### Cancel Path

1. **Create**: User membuat pengajuan → Status = `DRAFT`
2. **Cancel**: User membatalkan → Status = `DIBATALKAN`

---

## 🛠️ Development

### Menambah Guard Baru

```java
public class KurangHariMinggguanGuard implements Guard {
    @Override
    public boolean boleh(LeaveRequest req) {
        // Custom logic
        return true;
    }

    @Override
    public String alasanGagal() {
        return "Alasan...";
    }
}
```

Kemudian daftarkan di `LeaveRequestService`:

```java
engine.daftarTransisi(new Transition(
    "ajukan",
    LeaveStatus.DRAFT,
    LeaveStatus.DIAJUKAN,
    List.of(
        new CutiCukupGuard(),
        new TanggalValidGuard(),
        new MinimalH3Guard(),
        new KurangHariMinggguanGuard()  // Add here
    ),
    List.of()
));
```

### Menambah Effect Baru

```java
public class SendEmailEffect implements TransitionEffect {
    @Override
    public void execute(LeaveRequest req) {
        // Send email to employee
    }
}
```

---

## 📝 Poin-Poin Pembelajaran OOP

1. **Encapsulation**: Domain objects membungkus data dan behavior
2. **Inheritance**: Guard dan Effect adalah interfaces yang diimplementasikan
3. **Polymorphism**: Banyak implementasi Guard/Effect yang berbeda
4. **Abstraction**: LeaveWorkflowEngine menyembunyikan kompleksitas state machine
5. **Dependency Injection**: Spring menginjeksi LeaveRequestService ke controller
6. **Design Patterns**:
   - **State Pattern**: Manage state transitions
   - **Strategy Pattern**: Guard dan Effect adalah strategies
   - **Template Method**: Workflow engine mengikuti template workflow

---

## 📄 Lisensi

© 2024 Universitas Cakrawala - Wahyu Amaldi, M.Kom

---

## 📖 Referensi

- [Bab 09: Workflow Engine](../docs/09-workflow-engine.md)
- [LeaveRequest Sample](../samples/workflow/LeaveRequest.java)
- Spring Boot Documentation: https://spring.io/projects/spring-boot
- State Machine Pattern: https://refactoring.guru/design-patterns/state
