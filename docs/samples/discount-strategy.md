# DiscountStrategy.java — Design Patterns Sample

Path: `samples/design-patterns/DiscountStrategy.java`

## Tujuan
Contoh ini menunjukkan beberapa design pattern dalam satu file Java: Strategy, Observer, Factory, dan State.

## Konsep utama
- Strategy Pattern untuk diskon yang bisa diganti-ganti
- Observer Pattern untuk notifikasi perubahan stok
- Factory Pattern untuk membuat objek dokumen berdasarkan jenis
- State Pattern di komentar (status pesanan) sebagai konsep tambahan

## Komponen penting
- `StrategiDiskon` dan implementasi:
  - `DiskonMember`
  - `DiskonGold`
  - `DiskonHarbolnas`
- `Kasir`: menggunakan strategi diskon saat proses pembayaran
- `Produk`: subject yang memberitahu observer saat stok berubah
- Observer:
  - `NotifikasiEmail`
  - `DashboardWeb`
- Factory:
  - `Dokumen`
  - `Invoice`
  - `Kwitansi`
  - `DokumenFactory`

## Compile & Run
```bash
javac DiscountStrategy.java
java DiscountStrategy
```

## Ringkasan perilaku
- Kasir mengganti strategi diskon sesuai jenis member atau promosi
- Produk memberi tahu pengamat stok saat stok berubah
- Factory membuat dan mencetak dokumen berdasarkan nama jenis
