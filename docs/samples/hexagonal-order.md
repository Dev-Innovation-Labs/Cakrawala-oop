# HexagonalOrder.java — Hexagonal Architecture Sample

Path: `samples/hexagonal/HexagonalOrder.java`

## Tujuan
Contoh ini memperlihatkan implementasi Hexagonal Architecture dengan Port & Adapter.

## Konsep utama
- Input Port (`BuatPesananUseCase`) sebagai use case boundary
- Output Port (`SimpanPesananPort`, `KirimNotifikasiPort`) sebagai abstraksi infrastruktur
- Adapter infrastruktur yang bisa di-swap tanpa mengubah inti bisnis
- Service aplikasi (`BuatPesananService`) sebagai business logic

## Komponen penting
- Domain model: `Pesanan`
- Input port: `BuatPesananUseCase`
- Output ports:
  - `SimpanPesananPort`
  - `KirimNotifikasiPort`
- Adapter:
  - `SimpanKeMemory`
  - `SimpanKeDatabase`
  - `KirimEmail`
  - `KirimWhatsApp`
- Use case service: `BuatPesananService`

## Compile & Run
```bash
javac HexagonalOrder.java
java HexagonalOrder
```

## Ringkasan perilaku
- Buat pesanan dengan `BuatPesananService`
- Simpan lewat repository adapter
- Kirim notifikasi lewat adapter
- Demo dua skenario: memory + email, database + WhatsApp
