# PurchaseOrder.java — Business Object & Business Process Sample

Path: `samples/business/PurchaseOrder.java`

## Tujuan
Contoh ini menunjukkan Rich Domain Model untuk Purchase Order dan bagaimana business process dijaga di dalam objek.

## Konsep utama
- Business Object yang self-validating
- Aturan bisnis di dalam objek, bukan tersebar di luar
- Status PO: `DRAFT`, `MENUNGGU_APPROVAL`, `DISETUJUI`
- Proses approval otomatis untuk PO kecil dan manual untuk PO besar

## Komponen penting
- `POItem`: value object item PO
- `PurchaseOrderBO`: business object utama dengan validasi, submit, dan approve
- `PurchaseOrderService`: orkestrator proses bisnis

## Compile & Run
```bash
javac PurchaseOrder.java
java PurchaseOrder
```

## Ringkasan perilaku
- Tambah item hanya saat status `DRAFT`
- Submit PO mengubah status sesuai total nilai
- PO kecil disetujui otomatis
- PO besar masuk status approval dan disetujui oleh manajer
- Cegah perubahan setelah submit
