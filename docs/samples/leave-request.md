# LeaveRequest.java — Workflow Engine Sample

Path: `samples/workflow/LeaveRequest.java`

## Tujuan
Contoh ini mendemonstrasikan engine workflow sederhana untuk pengajuan cuti. Fokusnya pada state machine, guard, dan effect.

## Konsep utama
- `StatusCuti` sebagai state enum
- `Transition` untuk mendefinisikan transisi state
- `Guard` untuk validasi sebelum transisi
- `TransitionEffect` untuk aksi setelah transisi berhasil
- `CutiWorkflowEngine` yang menjalankan transisi secara deklaratif

## Komponen penting
- `CutiRequest`: domain object pengajuan cuti
- Guard:
  - `CutiCukupGuard`
  - `TanggalValidGuard`
  - `MinimalH3Guard`
- Effect:
  - `NotifAtasanEffect`
  - `NotifHREffect`
  - `KurangiSisaCutiEffect`
  - `NotifKaryawanApprovedEffect`
  - `NotifKaryawanRejectedEffect`

## Compile & Run
```bash
javac LeaveRequest.java
java LeaveRequest
```

## Ringkasan perilaku
1. `ajukan` dari `DRAFT` ke `DIAJUKAN` dengan validasi cuti, tanggal, dan H-3
2. `approve_atasan` dari `DIAJUKAN` ke `DISETUJUI_ATASAN`
3. `approve_hr` dari `DISETUJUI_ATASAN` ke `DISETUJUI_HR` dan kurangi sisa cuti
4. `tolak` dari `DIAJUKAN` ke `DITOLAK`
5. validasi menolak transisi yang tidak valid atau kondisi guard yang gagal
