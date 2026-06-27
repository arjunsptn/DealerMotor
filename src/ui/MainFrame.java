package ui;

import dao.PenjualanUnitDAO;
import model.PenjualanUnit;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * MainFrame - UI Utama Aplikasi CRUD Penjualan Unit Motor
 * Tema: Merah, Hitam, Putih
 *
 * @author Arjun Septian Amarta
 */
public class MainFrame extends JFrame {

    // ── Warna Tema ─────────────────────────────────────────────────
    private static final Color RED_PRIMARY   = new Color(180, 0, 0);
    private static final Color RED_DARK      = new Color(120, 0, 0);
    private static final Color RED_HOVER     = new Color(210, 30, 30);
    private static final Color BLACK_BG      = new Color(18, 18, 18);
    private static final Color BLACK_PANEL   = new Color(30, 30, 30);
    private static final Color BLACK_CARD    = new Color(40, 40, 40);
    private static final Color BLACK_BORDER  = new Color(60, 60, 60);
    private static final Color WHITE_TEXT    = new Color(240, 240, 240);
    private static final Color GRAY_TEXT     = new Color(170, 170, 170);
    private static final Color TABLE_ROW_ODD = new Color(35, 35, 35);
    private static final Color TABLE_ROW_EVEN= new Color(45, 45, 45);
    private static final Color TABLE_SELECT  = new Color(140, 0, 0);
    private static final Color BTN_UPDATE    = new Color(200, 100, 0);
    private static final Color BTN_DELETE    = new Color(150, 0, 0);
    private static final Color BTN_SEARCH    = new Color(0, 100, 160);
    private static final Color BTN_RESET     = new Color(70, 70, 70);

    // ── Komponen ───────────────────────────────────────────────────
    private JTextField txtNoRangka, txtTipe, txtPembeli, txtHarga, txtCari;
    private JTable     tableData;
    private DefaultTableModel tableModel;
    private JLabel     lblStatus, lblTotal, lblJumlah;
    private JButton    btnTambah, btnUpdate, btnHapus, btnBersih, btnCari, btnReset, btnRefresh;

    private PenjualanUnitDAO dao       = new PenjualanUnitDAO();
    private int              selectedId = -1;
    private String           selectedNoRangka = "";
    private final DecimalFormat df     = new DecimalFormat("#,##0");

    // ══════════════════════════════════════════════════════════════
    public MainFrame() {
        setTitle("Dealer Motor — Penjualan Unit  |  Arjun Septian Amarta");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 680);
        setMinimumSize(new Dimension(900, 580));
        setLocationRelativeTo(null);
        getContentPane().setBackground(BLACK_BG);
        setLayout(new BorderLayout(0, 0));

        add(buildHeader(),     BorderLayout.NORTH);
        add(buildCenter(),     BorderLayout.CENTER);
        add(buildStatusBar(),  BorderLayout.SOUTH);

        loadData();
    }

    // ── HEADER ─────────────────────────────────────────────────────
    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(RED_PRIMARY);
        header.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        JLabel icon = new JLabel("🏍");
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));
        icon.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 12));

        JPanel titlePane = new JPanel(new GridLayout(2, 1));
        titlePane.setOpaque(false);

        JLabel title = new JLabel("DEALER MOTOR — PENJUALAN UNIT");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.WHITE);

        JLabel sub = new JLabel("Sistem Manajemen Data Penjualan  |  Arjun Septian Amarta");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sub.setForeground(new Color(255, 200, 200));

        titlePane.add(title);
        titlePane.add(sub);

        header.add(icon,      BorderLayout.WEST);
        header.add(titlePane, BorderLayout.CENTER);
        return header;
    }

    // ── CENTER (form kiri + tabel kanan) ──────────────────────────
    private JPanel buildCenter() {
        JPanel center = new JPanel(new BorderLayout(10, 0));
        center.setBackground(BLACK_BG);
        center.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));

        center.add(buildFormPanel(),  BorderLayout.WEST);
        center.add(buildTablePanel(), BorderLayout.CENTER);
        return center;
    }

    // ── FORM PANEL ─────────────────────────────────────────────────
    private JPanel buildFormPanel() {
        JPanel outer = new JPanel(new BorderLayout(0, 8));
        outer.setBackground(BLACK_BG);
        outer.setPreferredSize(new Dimension(300, 0));

        // ── Kartu Input ──
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(BLACK_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(RED_PRIMARY, 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill      = GridBagConstraints.HORIZONTAL;
        gbc.insets    = new Insets(4, 0, 4, 0);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx   = 1.0;

        // Judul kartu
        JLabel cardTitle = new JLabel("  INPUT DATA PENJUALAN");
        cardTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        cardTitle.setForeground(RED_PRIMARY);
        cardTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        card.add(cardTitle, gbc);

        // Fields
        txtNoRangka = createField(card, gbc, "No Rangka", "Contoh: MH1JC1234567890");
        txtTipe     = createField(card, gbc, "Tipe Motor",  "Contoh: Honda CB150R");
        txtPembeli  = createField(card, gbc, "Nama Pembeli","Contoh: Budi Santoso");
        txtHarga    = createField(card, gbc, "Harga (Rp)",  "Contoh: 25000000");

        // Separator
        JSeparator sep = new JSeparator();
        sep.setForeground(BLACK_BORDER);
        gbc.insets = new Insets(8, 0, 8, 0);
        card.add(sep, gbc);
        gbc.insets = new Insets(4, 0, 4, 0);

        // Tombol CRUD
        btnTambah = createButton("➕  TAMBAH",  RED_PRIMARY);
        btnUpdate = createButton("✏️  UPDATE",  BTN_UPDATE);
        btnHapus  = createButton("🗑  HAPUS",   BTN_DELETE);
        btnBersih = createButton("🔄  BERSIH",  BTN_RESET);

        JPanel btnGrid = new JPanel(new GridLayout(2, 2, 6, 6));
        btnGrid.setOpaque(false);
        btnGrid.add(btnTambah);
        btnGrid.add(btnUpdate);
        btnGrid.add(btnHapus);
        btnGrid.add(btnBersih);
        card.add(btnGrid, gbc);

        // ── Kartu Cari ──
        JPanel searchCard = new JPanel(new BorderLayout(6, 6));
        searchCard.setBackground(BLACK_CARD);
        searchCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BLACK_BORDER, 1),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));

        JLabel searchTitle = new JLabel("  CARI DATA");
        searchTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        searchTitle.setForeground(GRAY_TEXT);

        txtCari = new JTextField();
        styleField(txtCari, "Ketik No Rangka / Tipe / Pembeli...");

        JPanel searchBtns = new JPanel(new GridLayout(1, 2, 6, 0));
        searchBtns.setOpaque(false);
        btnCari  = createButton("🔍 Cari",   BTN_SEARCH);
        btnReset = createButton("✖ Reset",   BTN_RESET);
        btnCari.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnReset.setFont(new Font("Segoe UI", Font.BOLD, 12));
        searchBtns.add(btnCari);
        searchBtns.add(btnReset);

        searchCard.add(searchTitle, BorderLayout.NORTH);
        searchCard.add(txtCari,     BorderLayout.CENTER);
        searchCard.add(searchBtns,  BorderLayout.SOUTH);

        outer.add(card,       BorderLayout.CENTER);
        outer.add(searchCard, BorderLayout.SOUTH);

        // Action Listeners
        btnTambah.addActionListener(e -> aksiTambah());
        btnUpdate.addActionListener(e -> aksiUpdate());
        btnHapus .addActionListener(e -> aksiHapus());
        btnBersih.addActionListener(e -> bersihForm());
        btnCari  .addActionListener(e -> aksiCari());
        btnReset .addActionListener(e -> aksiReset());
        txtCari.addActionListener  (e -> aksiCari());

        return outer;
    }

    /** Helper: tambah label + field ke GridBag */
    private JTextField createField(JPanel parent, GridBagConstraints gbc, String label, String placeholder) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(GRAY_TEXT);
        parent.add(lbl, gbc);

        JTextField tf = new JTextField();
        styleField(tf, placeholder);
        parent.add(tf, gbc);
        return tf;
    }

    private void styleField(JTextField tf, String placeholder) {
        tf.setBackground(BLACK_PANEL);
        tf.setForeground(WHITE_TEXT);
        tf.setCaretColor(Color.WHITE);
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BLACK_BORDER),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        // Placeholder
        tf.putClientProperty("JTextField.placeholderText", placeholder);
        tf.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                tf.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(RED_PRIMARY),
                    BorderFactory.createEmptyBorder(5, 8, 5, 8)
                ));
            }
            @Override public void focusLost(FocusEvent e) {
                tf.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BLACK_BORDER),
                    BorderFactory.createEmptyBorder(5, 8, 5, 8)
                ));
            }
        });
    }

    private JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        Color darker = bg.darker();
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { btn.setBackground(darker); }
            @Override public void mouseExited (MouseEvent e) { btn.setBackground(bg); }
        });
        return btn;
    }

    // ── TABLE PANEL ────────────────────────────────────────────────
    private JPanel buildTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 6));
        panel.setBackground(BLACK_BG);

        // Toolbar atas tabel
        JPanel toolbar = new JPanel(new BorderLayout());
        toolbar.setBackground(BLACK_BG);

        JLabel tblTitle = new JLabel("  DAFTAR PENJUALAN UNIT");
        tblTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tblTitle.setForeground(RED_PRIMARY);

        btnRefresh = createButton("⟳  Refresh", new Color(50, 50, 50));
        btnRefresh.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnRefresh.setPreferredSize(new Dimension(110, 30));
        btnRefresh.addActionListener(e -> loadData());

        toolbar.add(tblTitle,  BorderLayout.WEST);
        toolbar.add(btnRefresh, BorderLayout.EAST);

        // Model tabel
        String[] cols = {"ID", "No Rangka", "Tipe Motor", "Nama Pembeli", "Harga (Rp)", "Tgl Penjualan"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        tableData = new JTable(tableModel) {
            @Override public Component prepareRenderer(TableCellRenderer r, int row, int col) {
                Component c = super.prepareRenderer(r, row, col);
                if (isRowSelected(row)) {
                    c.setBackground(TABLE_SELECT);
                    c.setForeground(Color.WHITE);
                } else {
                    c.setBackground(row % 2 == 0 ? TABLE_ROW_EVEN : TABLE_ROW_ODD);
                    c.setForeground(WHITE_TEXT);
                }
                return c;
            }
        };

        // Style tabel
        tableData.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tableData.setRowHeight(28);
        tableData.setShowGrid(false);
        tableData.setIntercellSpacing(new Dimension(0, 1));
        tableData.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableData.setBackground(TABLE_ROW_EVEN);
        tableData.setForeground(WHITE_TEXT);
        tableData.setSelectionBackground(TABLE_SELECT);
        tableData.setSelectionForeground(Color.WHITE);
        tableData.setFillsViewportHeight(true);

        // Header tabel
        JTableHeader header = tableData.getTableHeader();
        header.setBackground(RED_DARK);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setReorderingAllowed(false);
        header.setPreferredSize(new Dimension(0, 35));

        // Lebar kolom
        tableData.getColumnModel().getColumn(0).setPreferredWidth(40);
        tableData.getColumnModel().getColumn(1).setPreferredWidth(160);
        tableData.getColumnModel().getColumn(2).setPreferredWidth(140);
        tableData.getColumnModel().getColumn(3).setPreferredWidth(150);
        tableData.getColumnModel().getColumn(4).setPreferredWidth(120);
        tableData.getColumnModel().getColumn(5).setPreferredWidth(150);

        // Klik baris → isi form
        tableData.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = tableData.getSelectedRow();
                if (row >= 0) {
                    Object idVal = tableModel.getValueAt(row, 0);
                    selectedId = (idVal != null && !idVal.toString().equals("-"))
                                 ? (int) idVal : -1;
                    isiFormDariTabel(row);
                }
            }
        });

        JScrollPane scroll = new JScrollPane(tableData);
        scroll.setBackground(BLACK_PANEL);
        scroll.getViewport().setBackground(BLACK_PANEL);
        scroll.setBorder(BorderFactory.createLineBorder(BLACK_BORDER));

        // Info ringkas bawah tabel
        JPanel infoBar = new JPanel(new GridLayout(1, 2));
        infoBar.setBackground(BLACK_CARD);
        infoBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        lblJumlah = makeInfoLabel("Jumlah: 0 unit");
        lblTotal  = makeInfoLabel("Total: Rp 0");

        infoBar.add(lblJumlah);
        infoBar.add(lblTotal);

        panel.add(toolbar,  BorderLayout.NORTH);
        panel.add(scroll,   BorderLayout.CENTER);
        panel.add(infoBar,  BorderLayout.SOUTH);
        return panel;
    }

    private JLabel makeInfoLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(GRAY_TEXT);
        return lbl;
    }

    // ── STATUS BAR ─────────────────────────────────────────────────
    private JPanel buildStatusBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(RED_DARK);
        bar.setBorder(BorderFactory.createEmptyBorder(4, 14, 4, 14));

        lblStatus = new JLabel("Siap");
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblStatus.setForeground(new Color(255, 220, 220));

        JLabel copy = new JLabel("© Arjun Septian Amarta — Dealer Motor App v1.0");
        copy.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        copy.setForeground(new Color(200, 160, 160));

        bar.add(lblStatus, BorderLayout.WEST);
        bar.add(copy,      BorderLayout.EAST);
        return bar;
    }

    // ══════════════════════════════════════════════════════════════
    //  LOGIC / AKSI
    // ══════════════════════════════════════════════════════════════

    private void loadData() {
        try {
            tableModel.setRowCount(0);
            List<PenjualanUnit> list = dao.getAll();
            for (PenjualanUnit p : list) {
                tableModel.addRow(new Object[]{
                    p.getId() > 0 ? p.getId() : "-",
                    p.getNoRangka(),
                    p.getTipe(),
                    p.getPembeli(),
                    "Rp " + df.format(p.getHarga()),
                    p.getTanggalPenjualan() != null
                        ? p.getTanggalPenjualan().toString().replace(".0","")
                        : "-"
                });
            }
            updateInfoBar();
            setStatus("Data dimuat — " + list.size() + " record");
        } catch (SQLException ex) {
            String detail = "Gagal memuat data!\n\n"
                + "Pesan   : " + ex.getMessage() + "\n"
                + "SQLState: " + ex.getSQLState() + "\n"
                + "ErrCode : " + ex.getErrorCode();
            JOptionPane.showMessageDialog(this, detail, "Database Error", JOptionPane.ERROR_MESSAGE);
            setStatus("ERROR: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void aksiTambah() {
        if (!validasiInput()) return;
        try {
            PenjualanUnit p = buildFromForm();
            if (dao.tambah(p)) {
                showSuccess("Data berhasil ditambahkan!");
                bersihForm();
                loadData();
            }
        } catch (SQLException ex) {
            showError("Gagal tambah: " + ex.getMessage());
        }
    }

    private void aksiUpdate() {
        if (selectedId < 0 && selectedNoRangka.isEmpty()) {
            showWarn("Pilih baris data terlebih dahulu!"); return;
        }
        if (!validasiInput()) return;
        try {
            PenjualanUnit p = buildFromForm();
            boolean ok;
            if (selectedId > 0) {
                p.setId(selectedId);
                ok = dao.updateById(p);
            } else {
                ok = dao.updateByNoRangka(p, selectedNoRangka);
            }
            if (ok) { showSuccess("Data berhasil diupdate!"); bersihForm(); loadData(); }
        } catch (SQLException ex) {
            showError("Gagal update: " + ex.getMessage());
        }
    }

    private void aksiHapus() {
        if (selectedId < 0 && selectedNoRangka.isEmpty()) {
            showWarn("Pilih baris data terlebih dahulu!"); return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
            "Yakin ingin menghapus data ini?", "Konfirmasi Hapus",
            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean ok;
                if (selectedId > 0) {
                    ok = dao.deleteById(selectedId);
                } else {
                    ok = dao.deleteByNoRangka(selectedNoRangka);
                }
                if (ok) { showSuccess("Data berhasil dihapus!"); bersihForm(); loadData(); }
            } catch (SQLException ex) {
                showError("Gagal hapus: " + ex.getMessage());
            }
        }
    }

    private void aksiCari() {
        String kw = txtCari.getText().trim();
        if (kw.isEmpty()) { showWarn("Masukkan kata kunci pencarian!"); return; }
        try {
            tableModel.setRowCount(0);
            List<PenjualanUnit> list = dao.search(kw);
            if (list.isEmpty()) {
                setStatus("Pencarian: tidak ada hasil untuk \"" + kw + "\"");
                JOptionPane.showMessageDialog(this, "Data tidak ditemukan.", "Informasi", JOptionPane.INFORMATION_MESSAGE);
            } else {
                for (PenjualanUnit p : list) {
                    tableModel.addRow(new Object[]{
                        p.getId(), p.getNoRangka(), p.getTipe(), p.getPembeli(),
                        "Rp " + df.format(p.getHarga()),
                        p.getTanggalPenjualan() != null ? p.getTanggalPenjualan().toString().replace(".0","") : "-"
                    });
                }
                setStatus("Hasil pencarian \"" + kw + "\": " + list.size() + " record");
            }
        } catch (SQLException ex) {
            showError("Gagal cari: " + ex.getMessage());
        }
    }

    private void aksiReset() {
        txtCari.setText("");
        loadData();
    }

    private void bersihForm() {
        txtNoRangka.setText("");
        txtTipe.setText("");
        txtPembeli.setText("");
        txtHarga.setText("");
        selectedId = -1;
        selectedNoRangka = "";
        tableData.clearSelection();
        setStatus("Form dibersihkan");
    }

    private void isiFormDariTabel(int row) {
        // Kolom: 0=ID, 1=No Rangka, 2=Tipe, 3=Pembeli, 4=Harga, 5=Tgl
        Object idVal = tableModel.getValueAt(row, 0);
        selectedId       = (idVal != null && !idVal.toString().equals("-")) ? (int) idVal : -1;
        selectedNoRangka = tableModel.getValueAt(row, 1).toString();

        txtNoRangka.setText(selectedNoRangka);
        txtTipe    .setText(tableModel.getValueAt(row, 2).toString());
        txtPembeli .setText(tableModel.getValueAt(row, 3).toString());
        String hargaRaw = tableModel.getValueAt(row, 4).toString()
                            .replace("Rp ", "").replaceAll("[^0-9]", "");
        txtHarga.setText(hargaRaw);
        setStatus("Baris dipilih — No Rangka: " + selectedNoRangka);
    }

    // ── Validasi ───────────────────────────────────────────────────
    private boolean validasiInput() {
        if (isEmpty(txtNoRangka) || isEmpty(txtTipe) || isEmpty(txtPembeli) || isEmpty(txtHarga)) {
            showWarn("Semua field wajib diisi!");
            return false;
        }
        try {
            new BigDecimal(txtHarga.getText().trim().replaceAll("[^0-9]", ""));
        } catch (NumberFormatException ex) {
            showWarn("Harga harus berupa angka!");
            return false;
        }
        return true;
    }

    private boolean isEmpty(JTextField tf) { return tf.getText().trim().isEmpty(); }

    private PenjualanUnit buildFromForm() {
        String hargaStr = txtHarga.getText().trim().replaceAll("[^0-9]", "");
        return new PenjualanUnit(
            txtNoRangka.getText().trim(),
            txtTipe    .getText().trim(),
            txtPembeli .getText().trim(),
            new BigDecimal(hargaStr)
        );
    }

    // ── Info Bar ───────────────────────────────────────────────────
    private void updateInfoBar() {
        try {
            int      jumlah = dao.count();
            BigDecimal total = dao.totalHarga();
            lblJumlah.setText("Jumlah: " + jumlah + " unit");
            lblTotal .setText("Total Harga: Rp " + df.format(total));
        } catch (SQLException ex) {
            lblJumlah.setText("Jumlah: -");
            lblTotal .setText("Total: -");
        }
    }

    // ── Pesan ──────────────────────────────────────────────────────
    private void setStatus(String msg) { lblStatus.setText(" " + msg); }
    private void showError  (String m) { JOptionPane.showMessageDialog(this, m, "Error",    JOptionPane.ERROR_MESSAGE);   setStatus("Error: " + m); }
    private void showWarn   (String m) { JOptionPane.showMessageDialog(this, m, "Perhatian",JOptionPane.WARNING_MESSAGE); setStatus("Peringatan: " + m); }
    private void showSuccess(String m) { JOptionPane.showMessageDialog(this, m, "Sukses",   JOptionPane.INFORMATION_MESSAGE); setStatus(m); }

    // ══════════════════════════════════════════════════════════════
    //  MAIN
    // ══════════════════════════════════════════════════════════════
    public static void main(String[] args) {
        // Coba FlatLaf Dark, fallback ke cross-platform LAF
        try {
            // FlatDarkLaf dari com.formdev:flatlaf
            UIManager.setLookAndFeel("com.formdev.flatlaf.FlatDarkLaf");
            // Override warna aksen FlatLaf agar sesuai tema merah-hitam-putih
            UIManager.put("Component.focusColor",          new Color(180, 0, 0));
            UIManager.put("Component.focusedBorderColor",  new Color(180, 0, 0));
            UIManager.put("TabbedPane.focusColor",         new Color(180, 0, 0));
            UIManager.put("Button.default.focusColor",     new Color(180, 0, 0));
        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ignored) {}
        }

        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}
