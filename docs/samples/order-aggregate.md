# OrderAggregate.java — DDD Sample

Path: `samples/ddd/OrderAggregate.java`

## Tujuan
Contoh ini menjelaskan konsep Domain-Driven Design: Entity, Value Object, Aggregate Root, dan Repository.

## Konsep utama
- Value Object: `Uang`, `Alamat`
- Entity / Aggregate Root: `PesananAggregate`
- Repository sederhana: `PesananRepository`
- Konsistensi bisnis dijaga oleh aggregate root

## Komponen penting
- `Uang`: immutable value object dengan operasi penjumlahan
- `Alamat`: immutable value object untuk alamat pengiriman
- `ItemPesanan`: entity dalam aggregate
- `PesananAggregate`: root yang memuat barang, total, dan status
- `PesananRepository`: penyimpanan dan pencarian aggregate

## Compile & Run
```bash
javac OrderAggregate.java
java OrderAggregate
```

## Ringkasan perilaku
- Buat pesanan dengan customer, alamat, dan item
- Hitung total secara agregat
- Konfirmasi pesanan dan cegah perubahan setelah konfirmasi
- Simpan dan ambil pesanan melalui repository
