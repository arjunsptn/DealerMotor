# 🏍 Dealer Motor — Aplikasi CRUD Penjualan Unit
**Tema: Merah · Hitam · Putih**

> Dibuat oleh: **Arjun Septian Amarta**  
> Platform: Java Swing + MySQL (NetBeans / Ant)

---

## 📁 Struktur Proyek

```
dealermotor_app/
├── src/
│   ├── database/
│   │   └── DatabaseConnection.java    ← koneksi JDBC
│   ├── model/
│   │   └── PenjualanUnit.java         ← class model/entity
│   ├── dao/
│   │   └── PenjualanUnitDAO.java      ← operasi CRUD ke DB
│   └── ui/
│       └── MainFrame.java             ← tampilan utama (Swing)
├── db/
│   └── db_dealermotor_231011402962.sql
├── lib/
│   └── mysql-connector-java-8.0.33.jar  ← (unduh sendiri, lihat bawah)
├── nbproject/
│   ├── project.xml
│   └── project.properties
├── build.xml
├── manifest.mf
├── db.properties
└── README.md
```

---

## ⚙️ Cara Setup

### 1. Buat Database MySQL

```bash
mysql -u root -p < db/db_dealermotor_231011402962.sql
```
Atau buka MySQL Workbench → jalankan isi file `.sql` tersebut.

### 2. Unduh MySQL Connector/J

- Link: https://dev.mysql.com/downloads/connector/j/
- Pilih: **Platform Independent (.zip)**
- Ekstrak, ambil file `mysql-connector-java-8.0.33.jar`
- Salin ke folder **`lib/`** di dalam proyek ini

### 3. Buka di NetBeans

1. **File → Open Project** → pilih folder `dealermotor_app`
2. Klik kanan proyek → **Properties → Libraries**
3. Klik **Add JAR/Folder** → pilih `lib/mysql-connector-java-8.0.33.jar`
4. Konfigurasi password MySQL di:  
   `src/database/DatabaseConnection.java` → ubah `PASSWORD = "..."`
5. Tekan **F6** atau klik **Run Project**

### 4. Build via Ant (tanpa NetBeans)

```bash
ant jar
java -jar dist/dealermotor_app.jar
```

---

## 🖥️ Fitur Aplikasi

| Fitur | Keterangan |
|-------|-----------|
| ➕ Tambah | Input data penjualan baru |
| ✏️ Update | Edit data yang dipilih di tabel |
| 🗑 Hapus | Hapus data dengan konfirmasi |
| 🔍 Cari | Cari berdasarkan No Rangka / Tipe / Pembeli |
| ✖ Reset | Tampilkan kembali semua data |
| ⟳ Refresh | Muat ulang data dari database |
| 📊 Info Bar | Total unit & total harga di bagian bawah tabel |

---

## 🗄️ Skema Database

**Tabel: `penjualan_unit`**

| Kolom | Tipe | Keterangan |
|-------|------|-----------|
| id | INT AUTO_INCREMENT | Primary Key |
| no_rangka | VARCHAR(50) UNIQUE | Nomor rangka motor |
| tipe | VARCHAR(100) | Tipe/model motor |
| pembeli | VARCHAR(150) | Nama pembeli |
| harga | DECIMAL(15,2) | Harga jual (Rupiah) |
| tanggal_penjualan | TIMESTAMP | Waktu penjualan |
| created_at | TIMESTAMP | Dibuat pada |
| updated_at | TIMESTAMP | Diperbarui pada |

---

## 🎨 Tema Tampilan

- **Background utama**: Hitam (#121212)
- **Panel/kartu**: Abu gelap (#282828)
- **Aksen utama**: Merah tua (#B40000)
- **Header/footer**: Merah gelap (#780000)
- **Teks**: Putih (#F0F0F0) & abu terang (#AAAAAA)
- **Baris terpilih**: Merah (#8C0000)

---

## ⚠️ Troubleshooting

| Error | Solusi |
|-------|--------|
| `MySQL Driver tidak ditemukan` | Pastikan JAR ada di `lib/` dan sudah ditambahkan ke classpath |
| `Connection refused` | MySQL Server harus berjalan di port 3306 |
| `Unknown database` | Jalankan file SQL untuk membuat database |
| `Duplicate entry 'no_rangka'` | No Rangka sudah ada, gunakan yang berbeda |

---

© 2024 Arjun Septian Amarta — Dealer Motor App
