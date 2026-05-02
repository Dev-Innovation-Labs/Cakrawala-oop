# Design Patterns Web Simulator

Simple web application untuk simulasi **Strategy Pattern** dari file `samples/design-patterns/DiscountStrategy.java` dengan menggunakan Spring Boot dan Java.

## Fitur

✅ **Strategy Pattern Simulator** - Simulasi interaktif sistem diskon dinamis dengan berbagai strategi:
- Member Biasa (5%)
- Member Gold (15%)  
- Harbolnas Special (30%)

✅ **UI Responsif** - Interface yang user-friendly dengan gradient modern

✅ **REST API** - Endpoint API untuk simulasi strategy pattern

✅ **Real-time Calculation** - Kalkulasi diskon real-time saat memasukkan harga

## Tech Stack

- **Java 17** - Java Development Kit
- **Spring Boot 3.2.0** - Web Framework
- **Maven** - Build Tool
- **Thymeleaf** - Template Engine
- **HTML5 + CSS3 + JavaScript** - Frontend

## Struktur Project

```
samples/design-patterns-web/
├── src/main/java/id/universitas/cakrawala/
│   ├── DesignPatternsApplication.java       # Main Application
│   ├── controller/
│   │   └── DesignPatternsController.java    # REST Controller
│   ├── service/
│   │   └── DesignPatternsService.java       # Business Logic
│   └── domain/
│       ├── StrategiDiskon.java              # Strategy Interface
│       ├── DiskonMember.java                # Concrete Strategy
│       ├── DiskonGold.java                  # Concrete Strategy
│       ├── DiskonHarbolnas.java             # Concrete Strategy
│       ├── Kasir.java                       # Context Class
│       └── TransaksiResult.java             # DTO
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
cd samples/design-patterns-web
bash run.sh
```

### Windows

```bash
cd samples\design-patterns-web
run.bat
```

### Manual dengan Maven

```bash
export JAVA_HOME=/usr/local/Cellar/openjdk@17/17.0.19/libexec/openjdk.jdk/Contents/Home
mvn spring-boot:run
```

## Akses Aplikasi

Setelah aplikasi berjalan, akses di:

- **Home**: http://localhost:8081
- **Simulator**: http://localhost:8081/simulator

## API Endpoint

### POST /api/simulasi

Simulasi Strategy Pattern dengan parameter:

**Request:**
```
POST /api/simulasi
Content-Type: application/x-www-form-urlencoded

tipeDiskon=member&item=Sepatu Nike&harga=1500000
```

**Response:**
```json
{
  "item": "Sepatu Nike",
  "hargaFormat": "Rp 1.500.000",
  "namaDiskon": "Member Biasa",
  "persentaseDiskon": "5%",
  "nilaiDiskonFormat": "Rp 75.000",
  "totalBayarFormat": "Rp 1.425.000"
}
```

## Konsep Design Pattern yang Diimplementasikan

### 1. **Strategy Pattern**
Memungkinkan pemilihan algoritma diskon saat runtime tanpa mengubah kode Kasir.

```java
// Set strategi dinamis
Kasir kasir = new Kasir();
kasir.setStrategi(new DiskonMember());  // atau DiskonGold, DiskonHarbolnas
TransaksiResult hasil = kasir.prosesPembayaran("Sepatu", 1500000);
```

### 2. **Factory Pattern (Implicit)**
Service menggunakan factory method untuk membuat strategi diskon berdasarkan tipe.

```java
private StrategiDiskon getStrategiDiskon(String tipe) {
    return switch (tipe.toLowerCase()) {
        case "member" -> new DiskonMember();
        case "gold" -> new DiskonGold();
        case "harbolnas" -> new DiskonHarbolnas();
        default -> new DiskonMember();
    };
}
```

## Requirement

- Java 17+
- Maven 3.6+
- Browser modern (Chrome, Firefox, Safari, Edge)

## Author

Universitas Cakrawala - OOP Learning Platform

## Port

Aplikasi berjalan di **port 8081** (berbeda dengan web-workflow yang menggunakan port 8080)
