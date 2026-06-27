package dao;

import database.DatabaseConnection;
import model.PenjualanUnit;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO untuk tabel penjualan_unit.
 * Adaptif: bekerja baik SEBELUM maupun SESUDAH ALTER TABLE
 * (dengan/tanpa kolom id dan tanggal_penjualan).
 *
 * @author Arjun Septian Amarta
 */
public class PenjualanUnitDAO {

    // ── Deteksi kolom yang tersedia ────────────────────────
    private boolean hasColumn(ResultSet rs, String colName) {
        try {
            ResultSetMetaData meta = rs.getMetaData();
            for (int i = 1; i <= meta.getColumnCount(); i++) {
                if (meta.getColumnLabel(i).equalsIgnoreCase(colName)) return true;
            }
        } catch (SQLException ignored) {}
        return false;
    }

    private boolean tableHasColumn(Connection conn, String tabel, String kolom) {
        try (ResultSet rs = conn.getMetaData().getColumns(null, null, tabel, kolom)) {
            return rs.next();
        } catch (SQLException e) { return false; }
    }

    // ── CREATE ────────────────────────────────────────────
    public boolean tambah(PenjualanUnit p) throws SQLException {
        final String SQL = "INSERT INTO penjualan_unit (no_rangka, tipe, pembeli, harga) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DatabaseConnection.getConnection();
            ps   = conn.prepareStatement(SQL);
            ps.setString(1, p.getNoRangka());
            ps.setString(2, p.getTipe());
            ps.setString(3, p.getPembeli());
            ps.setBigDecimal(4, p.getHarga());
            return ps.executeUpdate() > 0;
        } finally {
            DatabaseConnection.close(conn, ps);
        }
    }

    // ── READ ALL ──────────────────────────────────────────
    public List<PenjualanUnit> getAll() throws SQLException {
        List<PenjualanUnit> list = new ArrayList<>();
        Connection conn = null;
        Statement  stmt = null;
        ResultSet  rs   = null;
        try {
            conn = DatabaseConnection.getConnection();
            // Pilih ORDER BY yang sesuai kolom yang ada
            String orderBy = tableHasColumn(conn, "penjualan_unit", "id")
                           ? "ORDER BY id DESC"
                           : "ORDER BY no_rangka ASC";
            stmt = conn.createStatement();
            rs   = stmt.executeQuery("SELECT * FROM penjualan_unit " + orderBy);
            while (rs.next()) list.add(mapRow(rs));
        } finally {
            DatabaseConnection.close(conn, stmt, rs);
        }
        return list;
    }

    // ── READ BY NO_RANGKA (key utama jika tidak ada id) ───
    public PenjualanUnit getByNoRangka(String noRangka) throws SQLException {
        final String SQL = "SELECT * FROM penjualan_unit WHERE no_rangka = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConnection.getConnection();
            ps   = conn.prepareStatement(SQL);
            ps.setString(1, noRangka);
            rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        } finally {
            DatabaseConnection.close(conn, ps, rs);
        }
        return null;
    }

    // ── READ BY ID (jika kolom id ada) ────────────────────
    public PenjualanUnit getById(int id) throws SQLException {
        final String SQL = "SELECT * FROM penjualan_unit WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConnection.getConnection();
            ps   = conn.prepareStatement(SQL);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        } finally {
            DatabaseConnection.close(conn, ps, rs);
        }
        return null;
    }

    // ── UPDATE by ID ──────────────────────────────────────
    public boolean updateById(PenjualanUnit p) throws SQLException {
        final String SQL = "UPDATE penjualan_unit SET no_rangka=?, tipe=?, pembeli=?, harga=? WHERE id=?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DatabaseConnection.getConnection();
            ps   = conn.prepareStatement(SQL);
            ps.setString(1, p.getNoRangka());
            ps.setString(2, p.getTipe());
            ps.setString(3, p.getPembeli());
            ps.setBigDecimal(4, p.getHarga());
            ps.setInt(5, p.getId());
            return ps.executeUpdate() > 0;
        } finally {
            DatabaseConnection.close(conn, ps);
        }
    }

    // ── UPDATE by NO_RANGKA (fallback jika tidak ada id) ──
    public boolean updateByNoRangka(PenjualanUnit p, String noRangkaLama) throws SQLException {
        final String SQL = "UPDATE penjualan_unit SET no_rangka=?, tipe=?, pembeli=?, harga=? WHERE no_rangka=?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DatabaseConnection.getConnection();
            ps   = conn.prepareStatement(SQL);
            ps.setString(1, p.getNoRangka());
            ps.setString(2, p.getTipe());
            ps.setString(3, p.getPembeli());
            ps.setBigDecimal(4, p.getHarga());
            ps.setString(5, noRangkaLama);
            return ps.executeUpdate() > 0;
        } finally {
            DatabaseConnection.close(conn, ps);
        }
    }

    // ── DELETE by ID ──────────────────────────────────────
    public boolean deleteById(int id) throws SQLException {
        final String SQL = "DELETE FROM penjualan_unit WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DatabaseConnection.getConnection();
            ps   = conn.prepareStatement(SQL);
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } finally {
            DatabaseConnection.close(conn, ps);
        }
    }

    // ── DELETE by NO_RANGKA (fallback) ────────────────────
    public boolean deleteByNoRangka(String noRangka) throws SQLException {
        final String SQL = "DELETE FROM penjualan_unit WHERE no_rangka = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DatabaseConnection.getConnection();
            ps   = conn.prepareStatement(SQL);
            ps.setString(1, noRangka);
            return ps.executeUpdate() > 0;
        } finally {
            DatabaseConnection.close(conn, ps);
        }
    }

    // ── SEARCH ────────────────────────────────────────────
    public List<PenjualanUnit> search(String keyword) throws SQLException {
        final String SQL =
            "SELECT * FROM penjualan_unit " +
            "WHERE no_rangka LIKE ? OR tipe LIKE ? OR pembeli LIKE ? " +
            "ORDER BY no_rangka ASC";
        List<PenjualanUnit> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConnection.getConnection();
            ps   = conn.prepareStatement(SQL);
            String kw = "%" + keyword + "%";
            ps.setString(1, kw); ps.setString(2, kw); ps.setString(3, kw);
            rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } finally {
            DatabaseConnection.close(conn, ps, rs);
        }
        return list;
    }

    // ── COUNT ─────────────────────────────────────────────
    public int count() throws SQLException {
        Connection conn = null; Statement stmt = null; ResultSet rs = null;
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.createStatement();
            rs   = stmt.executeQuery("SELECT COUNT(*) FROM penjualan_unit");
            if (rs.next()) return rs.getInt(1);
        } finally { DatabaseConnection.close(conn, stmt, rs); }
        return 0;
    }

    // ── TOTAL HARGA ───────────────────────────────────────
    public BigDecimal totalHarga() throws SQLException {
        Connection conn = null; Statement stmt = null; ResultSet rs = null;
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.createStatement();
            rs   = stmt.executeQuery("SELECT COALESCE(SUM(harga),0) FROM penjualan_unit");
            if (rs.next()) return rs.getBigDecimal(1);
        } finally { DatabaseConnection.close(conn, stmt, rs); }
        return BigDecimal.ZERO;
    }

    // ── HAS ID COLUMN ─────────────────────────────────────
    public boolean hasIdColumn() throws SQLException {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            return tableHasColumn(conn, "penjualan_unit", "id");
        } finally { DatabaseConnection.close(conn); }
    }

    // ── MAPPER ────────────────────────────────────────────
    private PenjualanUnit mapRow(ResultSet rs) throws SQLException {
        PenjualanUnit p = new PenjualanUnit();
        // id — opsional
        if (hasColumn(rs, "id")) {
            p.setId(rs.getInt("id"));
        }
        p.setNoRangka(rs.getString("no_rangka"));
        p.setTipe(rs.getString("tipe"));
        p.setPembeli(rs.getString("pembeli"));
        p.setHarga(rs.getBigDecimal("harga"));
        // tanggal_penjualan — opsional
        if (hasColumn(rs, "tanggal_penjualan")) {
            p.setTanggalPenjualan(rs.getTimestamp("tanggal_penjualan"));
        }
        return p;
    }
}
