package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Model PenjualanUnit
 * @author Arjun Septian Amarta
 */
public class PenjualanUnit implements Serializable {
    private static final long serialVersionUID = 1L;

    private int       id;
    private String    noRangka;
    private String    tipe;
    private String    pembeli;
    private BigDecimal harga;
    private Timestamp tanggalPenjualan;

    public PenjualanUnit() {}

    public PenjualanUnit(String noRangka, String tipe, String pembeli, BigDecimal harga) {
        this.noRangka = noRangka;
        this.tipe     = tipe;
        this.pembeli  = pembeli;
        this.harga    = harga;
    }

    public PenjualanUnit(int id, String noRangka, String tipe, String pembeli, BigDecimal harga) {
        this.id       = id;
        this.noRangka = noRangka;
        this.tipe     = tipe;
        this.pembeli  = pembeli;
        this.harga    = harga;
    }

    // ── Getters & Setters ─────────────────────────────────
    public int getId()                         { return id; }
    public void setId(int id)                  { this.id = id; }

    public String getNoRangka()                { return noRangka; }
    public void setNoRangka(String v)          { this.noRangka = v; }

    public String getTipe()                    { return tipe; }
    public void setTipe(String v)              { this.tipe = v; }

    public String getPembeli()                 { return pembeli; }
    public void setPembeli(String v)           { this.pembeli = v; }

    public BigDecimal getHarga()               { return harga; }
    public void setHarga(BigDecimal v)         { this.harga = v; }

    public Timestamp getTanggalPenjualan()     { return tanggalPenjualan; }
    public void setTanggalPenjualan(Timestamp v){ this.tanggalPenjualan = v; }

    @Override
    public String toString() {
        return "PenjualanUnit{id=" + id + ", noRangka='" + noRangka + "', tipe='" + tipe
             + "', pembeli='" + pembeli + "', harga=" + harga + '}';
    }
}
