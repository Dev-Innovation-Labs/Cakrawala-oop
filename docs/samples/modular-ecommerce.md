# ModularECommerce.java — Modular Monolith Sample

Path: `samples/modular/ModularECommerce.java`

## Tujuan
Contoh ini mendemonstrasikan arsitektur modular monolith dengan modul terpisah dan event-driven interaction.

## Konsep utama
- Modul terpisah untuk Catalog, Order, Payment, dan Notification
- EventBus sebagai mediator penghubung modul
- Setiap modul bertanggung jawab atas domainnya sendiri
- Agregasi behavior tanpa monolit besar yang saling bercampur

## Komponen penting
- `EventBus`: dispatcher event antar modul
- `OrderModule`: buat pesanan dan publikasikan event
- `PaymentModule`: proses pembayaran setelah pesanan dibuat
- `CatalogModule`: cek dan kurangi stok
- `NotificationModule`: kirim email / alert berdasarkan event

## Compile & Run
```bash
javac ModularECommerce.java
java ModularECommerce
```

## Ringkasan perilaku
- Skenario 1: order berhasil dan stok dikurangi
- Skenario 2: stok turun dan low-stock alert dikirim
- Skenario 3: order ditolak bila stok tidak mencukupi
- Modul dapat diubah atau ditambah tanpa mengubah semua kode
