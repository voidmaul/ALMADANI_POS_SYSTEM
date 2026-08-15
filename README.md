<div align="center">

# 🏍️ Al-Madani Motor - Enterprise POS System
### *Next-Generation Desktop Point of Sales & Showroom Management System*

<img src="https://img.shields.io/badge/JAVA-JDK%2017+-007396?style=for-the-badge&logo=openjdk&logoColor=white" /> <img src="https://img.shields.io/badge/GUI-JAVA%20SWING-10B981?style=for-the-badge&logo=java&logoColor=white" /> <img src="https://img.shields.io/badge/LICENSE-MIT-2563EB?style=for-the-badge&logo=opensourceinitiative&logoColor=white" /> <img src="https://img.shields.io/badge/THEME-ENTERPRISE%20MODERN-F59E0B?style=for-the-badge&logo=statuspage&logoColor=white" />

</div>

---

Aplikasi **Point of Sales (POS)** desktop interaktif berbasis Java Swing yang dirancang secara profesional untuk manajemen dealer dan showroom kendaraan bermotor modern. Sistem ini mendukung pengoperasian direktori katalog kendaraan dinamis, kalkulator skema pembiayaan (Tunai & Kredit), proteksi data privasi nasabah, serta alur sistem kasir dan pencetakan dokumen Surat Pesanan Kendaraan (SPK) yang lengkap.

* 🔍 **Direktori Katalog Kendaraan Dinamis:** Pengelompokan merek (Honda, Yamaha, Suzuki), tahun rakitan (2024-2026), tipe spesifik, serta matriks variasi warna bawaan pabrik secara real-time.
* 🧮 **Kalkulator Keuangan Pintar:** 
  * **Skema Tunai:** Diskon otomatis khusus pembelian kontan.
  * **Skema Kredit:** Perhitungan transparan tenor 12x, 24x, dan 36x lengkap dengan DP 5%, akumulasi suku bunga bulanan, serta sisa pokok hutang.
* 🛡️ **Proteksi Data Sensitif (*Data Masking*):** Penyamaran otomatis 5 digit terakhir nomor KTP/NIK dan NPWP pada panel tabel log riwayat transaksi untuk keamanan privasi nasabah.
* 🖨️ **Pencetakan SPK & Pratinjau Dokumen Profesional:** Dialog cetak dokumen legal Surat Pesanan Kendaraan (SPK) berbasis HTML yang siap diintegrasikan langsung dengan *Printer Job* sistem operasi maupun ekspor dokumen PDF.
* 📈 **Dashboard & Analitik Target Penjualan:** Pemantauan omset kotor secara langsung terhadap target bulanan showroom sebesar Rp 250.000.000 lengkap dengan indikator persentase progres.

---

## 🖼️ Dokumentasi Tampilan & Alur Aplikasi

Berikut adalah pratinjau visual dari berbagai fitur utama yang tersedia di dalam aplikasi:

### 1. Dashboard Katalog & Direktori Harga OTR
> Pusat direktori kendaraan bermotor untuk meninjau harga OTR Jakarta terbaru dengan fitur filter merek instan yang responsif.
<div align="center">
<img width="1913" height="1005" alt="Screenshot 2026-08-15 233126" src="https://github.com/user-attachments/assets/4fda4bd6-0381-439e-98ce-004bc1c00707" />
</div>

### 2. Form Transaksi Kasir POS (Point of Sales)
> Antarmuka input data pemesan, pemilihan unit kendaraan dengan sinkronisasi warna dinamis, serta kalkulasi finansial otomatis dalam satu layar terpadu.
<div align="center">
<img width="1913" height="1011" alt="Screenshot 2026-08-15 233247" src="https://github.com/user-attachments/assets/9b89dd97-10ce-483e-9b0f-35b81e49ee56" />
</div>

### 3. Pratinjau Cetak Surat Pesanan Kendaraan (SPK)
> Pratinjau dokumen legal transaksi yang siap dicetak langsung ke printer fisik atau disimpan dalam format dokumen PDF.
<div align="center">
<img width="1906" height="1009" alt="Screenshot 2026-08-15 233304" src="https://github.com/user-attachments/assets/732f7252-fddc-4f03-85cf-0dde9e436556" />
</div>

### 4. Rekapitulasi Log Riwayat & Analitik Penjualan
> Tabel log riwayat transaksi lengkap dengan fitur proteksi data (*masking*), tombol aksi cepat cetak ulang nota, serta penghapusan data terotorisasi.
<div align="center">
<img width="1911" height="1006" alt="Screenshot 2026-08-15 233326" src="https://github.com/user-attachments/assets/a3e57309-bf80-4743-bb8b-1c17ab99032e" />
</div>

---

## 📂 Arsitektur & Struktur Project (Clean Architecture / MVC)

Project ini dikembangkan dengan menerapkan prinsip-prinsip *Object-Oriented Programming (OOP)* agar struktur kode bersih, modular, dan mudah dikembangkan:

* `Main.java` — Titik masuk utama (*entry point*) yang menjalankan aplikasi di dalam *Event Dispatch Thread* (EDT).
* `DatabaseMaster.java` — Kelas Model pusat yang memegang basis data statis (katalog merek, matriks tahun rakitan, variasi warna, dan harga OTR).
* `FormPanel.java` & `RiwayatPanel.java` — Kelas View modular yang mengatur tata letak komponen grafis antarmuka (*GUI*), tabel, serta kartu statistik.
* `TransactionController.java` — Kelas pengendali logika bisnis (*business logic*), validasi input, serta algoritma kalkulasi finansial.
* `AlMadaniMotorPOS.java` & `NotaPreviewDialog.java` — Kelas utama pengatur *event handling*, perakitan layout frame utama, serta mesin pencetakan dokumen SPK.

---

## 🚀 Panduan Instalasi & Menjalankan

```bash
1. Clone repository ini atau unduh sebagai Arsip ZIP[cite: 2].
2. Buka project menggunakan IDE Java pilihan Anda (Direkomendasikan IntelliJ IDEA)[cite: 2].
3. Pastikan JDK (versi 17 atau versi terbaru) telah terpasang dan terkonfigurasi pada project[cite: 2].
4. Jalankan aplikasi melalui file Main.java[cite: 2].

## 👨‍💻 Kontributor

* **Muhammad Irfan Maulana** *(Prodi S1-Sistem Informasi | Universitas Siber Asia)*
---
<div align="center">
  <small>Dibuat dengan ❤️ untuk memenuhi penilaian UAS Semester 4 pada mata kuliah Pemrograman Berorientasi Objek (OOP).</small>
</div>
