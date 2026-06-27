-- ============================================================
--  ALTER TABLE penjualan_unit
--  Tambah kolom id, tanggal_penjualan, created_at, updated_at
--  Author: Arjun Septian Amarta
-- ============================================================

USE db_dealermotor_231011402962;

-- Langkah 1: Hapus PRIMARY KEY lama (no_rangka)
ALTER TABLE penjualan_unit DROP PRIMARY KEY;

-- Langkah 2: Tambah kolom id AUTO_INCREMENT sebagai PRIMARY KEY
ALTER TABLE penjualan_unit
  ADD COLUMN id INT NOT NULL AUTO_INCREMENT FIRST,
  ADD PRIMARY KEY (id);

-- Langkah 3: Jadikan no_rangka sebagai UNIQUE KEY
ALTER TABLE penjualan_unit
  ADD UNIQUE KEY uq_no_rangka (no_rangka);

-- Langkah 4: Tambah kolom waktu
ALTER TABLE penjualan_unit
  ADD COLUMN tanggal_penjualan TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  ADD COLUMN created_at        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  ADD COLUMN updated_at        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;

-- Verifikasi
DESCRIBE penjualan_unit;
SELECT * FROM penjualan_unit;
