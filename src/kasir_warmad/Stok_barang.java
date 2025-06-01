/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package kasir_warmad;

import kasir_warmad.sistem.Session;
import desain.GradientPanel;
import javax.swing.*;                     // Semua komponen GUI (JFrame, JButton, JTextField, dll)
import javax.swing.table.DefaultTableModel; // Untuk manipulasi tabel (add row, hapus row, dll)
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import kasir_warmad.sistem.Koneksi;
import com.formdev.flatlaf.intellijthemes.FlatArcOrangeIJTheme;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.sql.Statement;
import java.util.Calendar;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author ThinkPad
 */
public class Stok_barang extends javax.swing.JFrame {

    /**
     * Creates new form Transaksi
     */
public Stok_barang() {
    initComponents();
    IsiKategori();
    IsiBarang();
    IsiTahun();
    IsiBulan();
    
  PencarianS.getDocument().addDocumentListener(new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            searchData();
        }
        @Override
        public void removeUpdate(DocumentEvent e) {
            searchData();
        }
        @Override
        public void changedUpdate(DocumentEvent e) {
            searchData();
        }
    });
}
private void IsiKategori() {
    try {
        Connection conn = Koneksi.getKoneksi();
        String sql = "SELECT DISTINCT kategori_barang FROM barang_jual ORDER BY kategori_barang ASC";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        PilihkategoriS.removeAllItems();
        PilihkategoriS.addItem("");  // item kosong default

        while (rs.next()) {
            PilihkategoriS.addItem(rs.getString("kategori_barang"));
        }

        // Jangan set selected index otomatis supaya tetap kosong di awal

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Gagal memuat kategori: " + e.getMessage());
    }
}

private void IsiBarang() {
    try {
        String kategoriDipilih = (String) PilihkategoriS.getSelectedItem();

        PilihbarangS.removeAllItems();
        PilihbarangS.addItem("");  // item kosong default

        if (kategoriDipilih == null || kategoriDipilih.isEmpty()) {
            // kalau kategori kosong, barang tetap kosong (hanya item kosong)
            return;
        }

        Connection conn = Koneksi.getKoneksi();
        String sql = "SELECT nama_barang FROM barang_jual WHERE kategori_barang = ? ORDER BY nama_barang ASC";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, kategoriDipilih);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            PilihbarangS.addItem(rs.getString("nama_barang"));
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal memuat barang: " + e.getMessage());
    }
}

private void IsiTahun() {
    int tahunSekarang = Calendar.getInstance().get(Calendar.YEAR);
    PilihtahunS.removeAllItems();
    PilihtahunS.addItem("");  // item kosong default
    for (int t = tahunSekarang; t <= tahunSekarang + 10; t++) {
        PilihtahunS.addItem(String.valueOf(t));
    }
}

private void IsiBulan() {
    PilihbulanS.removeAllItems();
    PilihbulanS.addItem("");  // item kosong default
    String[] bulan = {"Januari", "Februari", "Maret", "April", "Mei", "Juni",
                      "Juli", "Agustus", "September", "Oktober", "November", "Desember"};
    for (String b : bulan) {
        PilihbulanS.addItem(b);
    }}

    
    private void searchData() {
        try {
            Connection conn = Koneksi.getKoneksi();

            String keyword = "%" + PencarianS.getText().trim() + "%";

            String kategori = (String) PilihkategoriS.getSelectedItem();
            if (kategori == null) kategori = "";
            String barang = (String) PilihbarangS.getSelectedItem();
            if (barang == null) barang = "";
            String bulanStr = (String) PilihbulanS.getSelectedItem();
            String tahunStr = (String) PilihtahunS.getSelectedItem();

            String sql = "SELECT bj.id_barang_jual, bj.nama_barang, bj.kategori_barang, "
                       + "sg.jumlah_stok, sg.tanggal_kadaluarsa, sb.nama_supplier "
                       + "FROM stok_gudang sg "
                       + "JOIN barang_jual bj ON sg.id_barang_jual = bj.id_barang_jual "
                       + "JOIN supplier_barang sb ON sg.id_supplier_barang = sb.id_supplier_barang "
                       + "WHERE (bj.nama_barang LIKE ? OR bj.kategori_barang LIKE ? OR sb.nama_supplier LIKE ?)";

            if (!kategori.isEmpty()) {
                sql += " AND bj.kategori_barang = ?";
            }
            if (!barang.isEmpty()) {
                sql += " AND bj.nama_barang = ?";
            }
            if (bulanStr != null && !bulanStr.isEmpty()) {
                sql += " AND MONTH(sg.tanggal_kadaluarsa) = ?";
            }
            if (tahunStr != null && !tahunStr.isEmpty()) {
                sql += " AND YEAR(sg.tanggal_kadaluarsa) = ?";
            }

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, keyword);
            ps.setString(2, keyword);
            ps.setString(3, keyword);

            int paramIndex = 4;

            if (!kategori.isEmpty()) {
                ps.setString(paramIndex++, kategori);
            }
            if (!barang.isEmpty()) {
                ps.setString(paramIndex++, barang);
            }
            if (bulanStr != null && !bulanStr.isEmpty()) {
                String[] bulanArray = {"Januari", "Februari", "Maret", "April", "Mei", "Juni",
                                       "Juli", "Agustus", "September", "Oktober", "November", "Desember"};
                int bulan = 0;
                for (int i = 0; i < bulanArray.length; i++) {
                    if (bulanArray[i].equalsIgnoreCase(bulanStr)) {
                        bulan = i + 1;
                        break;
                    }
                }
                ps.setInt(paramIndex++, bulan);
            }
            if (tahunStr != null && !tahunStr.isEmpty()) {
                ps.setInt(paramIndex++, Integer.parseInt(tahunStr));
            }

            ResultSet rs = ps.executeQuery();

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Kode Barang");
            model.addColumn("Nama Barang");
            model.addColumn("Kategori");
            model.addColumn("Stok");
            model.addColumn("Tanggal Kadaluarsa");
            model.addColumn("Nama Supplier");

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("id_barang_jual"),
                    rs.getString("nama_barang"),
                    rs.getString("kategori_barang"),
                    rs.getInt("jumlah_stok"),
                    rs.getDate("tanggal_kadaluarsa"),
                    rs.getString("nama_supplier")
                });
            }

            TabelS.setModel(model);

            TabelS.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value,
                                                               boolean isSelected, boolean hasFocus,
                                                               int row, int column) {
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    int stok = (int) table.getValueAt(row, 3);

                    if (stok < 10) {
                        c.setBackground(Color.RED);
                    } else if (stok <= 20) {
                        c.setBackground(Color.YELLOW);
                    } else {
                        c.setBackground(Color.GREEN);
                    }

                    c.setForeground(Color.BLACK);
                    return c;
                }
            });

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal menampilkan data stok: " + e.getMessage());
        }
    }

private void loadDataHampirKadaluarsa() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Kode Barang");
        model.addColumn("Nama Barang");
        model.addColumn("Kategori");
        model.addColumn("Stok");
        model.addColumn("Tanggal Kadaluarsa");

        try {
            String sql = "SELECT bj.barcode_barang, bj.nama_barang, bj.kategori_barang, "
                    + "sg.jumlah_stok, sg.tanggal_kadaluarsa "
                    + "FROM stok_gudang sg "
                    + "JOIN barang_jual bj ON sg.id_barang_jual = bj.id_barang_jual "
                    + "WHERE sg.tanggal_kadaluarsa BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 7 DAY) "
                    + "ORDER BY sg.tanggal_kadaluarsa ASC";

            Connection conn = Koneksi.getKoneksi();
            Statement stm = conn.createStatement();
            ResultSet res = stm.executeQuery(sql);

            while (res.next()) {
                model.addRow(new Object[]{
                    res.getString("barcode_barang"),
                    res.getString("nama_barang"),
                    res.getString("kategori_barang"),
                    res.getInt("jumlah_stok"),
                    res.getDate("tanggal_kadaluarsa")
                });
            }

            TabelhampirkadaluarsaS.setModel(model);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error Hampir Kadaluarsa: " + e.getMessage());
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new GradientPanel();
        PilihbulanS = new javax.swing.JComboBox<>();
        PilihbarangS = new javax.swing.JComboBox<>();
        TampilkanstokS = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        TabelS = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        panel1 = new java.awt.Panel();
        DashboardS = new javax.swing.JButton();
        TbarangterlarisK = new javax.swing.JScrollPane();
        TabelhampirkadaluarsaS = new javax.swing.JTable();
        PencarianS = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        PilihkategoriS = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        PilihtahunS = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 204, 153));

        PilihbulanS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PilihbulanSActionPerformed(evt);
            }
        });

        PilihbarangS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PilihbarangSActionPerformed(evt);
            }
        });

        TampilkanstokS.setText("Tampilkan Stok");
        TampilkanstokS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TampilkanstokSActionPerformed(evt);
            }
        });

        TabelS.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Kode Barang", "Nama Barang", "Kategori", "Stok", "Tanggal Kadaluarsa", "Nama Supplier"
            }
        ));
        jScrollPane1.setViewportView(TabelS);

        jLabel1.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Pilih Kategori");

        jLabel3.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Pilih Barang");

        panel1.setBackground(new java.awt.Color(255, 255, 255));

        DashboardS.setFont(new java.awt.Font("Segoe UI Variable", 1, 12)); // NOI18N
        DashboardS.setText("Dashboard");
        DashboardS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DashboardSActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panel1Layout = new javax.swing.GroupLayout(panel1);
        panel1.setLayout(panel1Layout);
        panel1Layout.setHorizontalGroup(
            panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(DashboardS)
                .addGap(61, 61, 61))
        );
        panel1Layout.setVerticalGroup(
            panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(DashboardS)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        TabelhampirkadaluarsaS.setFont(new java.awt.Font("Poppins", java.awt.Font.BOLD, 14)
        );
        TabelhampirkadaluarsaS.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        TbarangterlarisK.setViewportView(TabelhampirkadaluarsaS);

        jLabel2.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Cari Barang");

        jLabel4.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Pilih Bulan");

        PilihkategoriS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PilihkategoriSActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Pilih Tahun");

        PilihtahunS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PilihtahunSActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Arial Black", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Barang Mendekati Kadaluarsa");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(TbarangterlarisK)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(PencarianS, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(jLabel5)
                .addGap(18, 18, 18)
                .addComponent(PilihtahunS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addComponent(PilihbulanS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(PilihkategoriS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(PilihbarangS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 215, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(TampilkanstokS)))
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel6)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(panel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21)
                .addComponent(jLabel6)
                .addGap(18, 18, 18)
                .addComponent(TbarangterlarisK, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(PencarianS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel5)
                            .addComponent(PilihtahunS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(PilihbarangS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(PilihkategoriS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1)
                            .addComponent(PilihbulanS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))))
                .addGap(16, 16, 16)
                .addComponent(TampilkanstokS)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(139, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void TampilkanstokSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TampilkanstokSActionPerformed
 try {
    Connection conn = Koneksi.getKoneksi();

    // Ambil keyword pencarian
    String keyword = PencarianS.getText().trim();
    if (keyword.isEmpty()) {
        // Kalau pencarian kosong, dan semua filter juga kosong, kosongkan tabel dan keluar
        if (PilihbulanS.getSelectedIndex() <= 0 && PilihtahunS.getSelectedIndex() <= 0 &&
            PilihkategoriS.getSelectedIndex() <= 0 && PilihbarangS.getSelectedIndex() <= 0) {
            DefaultTableModel kosong = new DefaultTableModel();
            kosong.addColumn("Kode Barang");
            kosong.addColumn("Nama Barang");
            kosong.addColumn("Kategori");
            kosong.addColumn("Stok");
            kosong.addColumn("Tanggal Kadaluarsa");
            kosong.addColumn("Nama Supplier");
            TabelS.setModel(kosong);
            return;
        }
    }

    // Mulai query dasar
    String sql = "SELECT bj.id_barang_jual, bj.nama_barang, bj.kategori_barang, "
               + "sg.jumlah_stok, sg.tanggal_kadaluarsa, sb.nama_supplier "
               + "FROM stok_gudang sg "
               + "JOIN barang_jual bj ON sg.id_barang_jual = bj.id_barang_jual "
               + "JOIN supplier_barang sb ON sg.id_supplier_barang = sb.id_supplier_barang "
               + "WHERE (bj.nama_barang LIKE ? OR bj.kategori_barang LIKE ? OR sb.nama_supplier LIKE ?) ";

    // Filter tambahan sesuai combo box yang terpilih (index > 0 berarti bukan kosong)
    if (PilihbulanS.getSelectedIndex() > 0) sql += " AND MONTH(sg.tanggal_kadaluarsa) = ? ";
    if (PilihtahunS.getSelectedIndex() > 0) sql += " AND YEAR(sg.tanggal_kadaluarsa) = ? ";
    if (PilihkategoriS.getSelectedIndex() > 0) sql += " AND bj.kategori_barang = ? ";
    if (PilihbarangS.getSelectedIndex() > 0) sql += " AND bj.nama_barang = ? ";

    PreparedStatement ps = conn.prepareStatement(sql);

    int idx = 1;
    String keywordLike = "%" + keyword + "%";
    ps.setString(idx++, keywordLike);
    ps.setString(idx++, keywordLike);
    ps.setString(idx++, keywordLike);

    if (PilihbulanS.getSelectedIndex() > 0) {
        int bulan = PilihbulanS.getSelectedIndex(); // Januari = 1, Februari=2, sesuai urutan
        ps.setInt(idx++, bulan);
    }
    if (PilihtahunS.getSelectedIndex() > 0) {
        int tahun = Integer.parseInt(PilihtahunS.getSelectedItem().toString());
        ps.setInt(idx++, tahun);
    }
    if (PilihkategoriS.getSelectedIndex() > 0) {
        ps.setString(idx++, PilihkategoriS.getSelectedItem().toString());
    }
    if (PilihbarangS.getSelectedIndex() > 0) {
        ps.setString(idx++, PilihbarangS.getSelectedItem().toString());
    }

    ResultSet rs = ps.executeQuery();

    DefaultTableModel model = new DefaultTableModel();
    model.addColumn("Kode Barang");
    model.addColumn("Nama Barang");
    model.addColumn("Kategori");
    model.addColumn("Stok");
    model.addColumn("Tanggal Kadaluarsa");
    model.addColumn("Nama Supplier");

    while (rs.next()) {
        model.addRow(new Object[]{
            rs.getString("id_barang_jual"),
            rs.getString("nama_barang"),
            rs.getString("kategori_barang"),
            rs.getInt("jumlah_stok"),
            rs.getDate("tanggal_kadaluarsa"),
            rs.getString("nama_supplier")
        });
    }

    TabelS.setModel(model);

    // Renderer warna stok
    TabelS.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            int stok = (int) table.getValueAt(row, 3);
            if (stok < 10) c.setBackground(Color.RED);
            else if (stok <= 20) c.setBackground(Color.YELLOW);
            else c.setBackground(Color.GREEN);
            c.setForeground(Color.BLACK);
            return c;
        }
    });

} catch (Exception e) {
    JOptionPane.showMessageDialog(this, "Gagal menampilkan data stok: " + e.getMessage());
}


    }//GEN-LAST:event_TampilkanstokSActionPerformed

    private void DashboardSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DashboardSActionPerformed
        String role = Session.getCurrentUserRole();
Dashboard da = new Dashboard(role);
da.setVisible(true);
da.pack();
da.setLocationRelativeTo(null);
dispose();

    }//GEN-LAST:event_DashboardSActionPerformed

    private void PilihbulanSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PilihbulanSActionPerformed

    }//GEN-LAST:event_PilihbulanSActionPerformed

    private void PilihbarangSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PilihbarangSActionPerformed

    }//GEN-LAST:event_PilihbarangSActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
    loadDataHampirKadaluarsa();        // TODO add your handling code here:
    }//GEN-LAST:event_formWindowOpened

    private void PilihkategoriSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PilihkategoriSActionPerformed
    IsiBarang();
    }//GEN-LAST:event_PilihkategoriSActionPerformed

    private void PilihtahunSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PilihtahunSActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_PilihtahunSActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            UIManager.setLookAndFeel(new FlatArcOrangeIJTheme());
            UIManager.put("Button.arc", 999);
            UIManager.put("defaultFont", new Font("Poppins", Font.BOLD, 14));
            Object put = UIManager.put("Component.arrowType", "triangle");
        } catch (Exception ex) {
            System.err.println("Gagal mengatur tema FlatLaf Arc Orange.");
            ex.printStackTrace();
        }

        /* Jalankan GUI di Event Dispatch Thread */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Stok_barang().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton DashboardS;
    private javax.swing.JTextField PencarianS;
    private javax.swing.JComboBox<String> PilihbarangS;
    private javax.swing.JComboBox<String> PilihbulanS;
    private javax.swing.JComboBox<String> PilihkategoriS;
    private javax.swing.JComboBox<String> PilihtahunS;
    private javax.swing.JTable TabelS;
    private javax.swing.JTable TabelhampirkadaluarsaS;
    private javax.swing.JButton TampilkanstokS;
    private javax.swing.JScrollPane TbarangterlarisK;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private java.awt.Panel panel1;
    // End of variables declaration//GEN-END:variables

    
}
