AbsenApp – Aplikasi Absensi Mahasiswa berbasis Java Swing, menggunakan SQLite untuk penyimpanan lokal dan JCalendar untuk input tanggal.

## Fitur
- Input data absensi mahasiswa
- Penyimpanan lokal menggunakan SQLite
- Pilih tanggal dengan komponen kalender (JDateChooser)
- Tabel data dengan tampilan rapi
- Validasi untuk mencegah duplikat entri (berdasarkan kombinasi Nama, Nim, Tanggal, Keterangan)
- Input Nim hanya boleh angka

## Agar berjalan dengan baik
Pastikan menambahkan dua file .jar berikut ke library / classpath project sebelum dijalankan:
- sqlite-jdbc-3.50.2.0.jar (untuk koneksi ke database SQLite)
- jcalendar-1.4.jar (untuk komponen pemilih tanggal)

## Keterangan
Kedua file .jar tersebut sudah disertakan di dalam folder project ini, jadi cukup tambahkan ke library project melalui IDE (misalnya NetBeans, VSCode) sesuai petunjuk IDE.
