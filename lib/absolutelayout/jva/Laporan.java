/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package kasir_warmad;

import javax.swing.*;                     // Semua komponen GUI (JFrame, JButton, JTextField, dll)
import javax.swing.table.DefaultTableModel; // Untuk manipulasi tabel (add row, hapus row, dll)
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import desain.GradientPanel;
import java.awt.Font;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Laporan {
    public static void main(String[] args) {
        System.out.println("Program Laporan berjalan!");
    }
}
public class Laporan {
    public static void main(String[] args) {
        System.out.println("Program Laporan berjalan!");
    }
}

    private static class jenislaporan {

        public jenislaporan() {
        }
    }

    private static class jenislaporan {

        public jenislaporan() {
        }
    }
/**
 *
 * @author ThinkPad
 */
public class Laporan extends javax.swing.JFrame {

    private JTable TabelL;
    private JLabel JumlahtransaksiL;
    private JLabel TotalkeseluruhanL;
    }
}


    /**
     *
     */
    public Laporan() {
        initComponents();
        isiComboBox ()
    }
private void isiComboBox() {
        // ISI ComboBox Bulan
        PilihbulanL.removeAllItems();
        String[] namaBulan = {
            "Januari", "Februari", "Maret", "April", "Mei", "Juni",
            "Juli", "Agustus", "September", "Oktober", "November", "Desember"
        };
        for (String b : namaBulan) {
            PilihbulanL.addItem(b);
        }

        // ISI ComboBox Tahun
        PilihtahunL.removeAllItems();
        for (int t = 2020; t <= 2025; t++) {
            PilihtahunL.addItem(String.valueOf(t));
        }

        // ISI ComboBox Jenis Laporan
        JenislaporanL.removeAllItems();
        JenislaporanL.addItem("Penjualan");
        JenislaporanL.addItem("Pembelian");
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
        PilihbulanL = new javax.swing.JComboBox<>();
        PilihtahunL = new javax.swing.JComboBox<>();
        JenislaporanL = new javax.swing.JComboBox<>();
        TampilkanlaporanL = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        TabelL = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        JumlahtransaksiL = new javax.swing.JTextField();
        TotalkeseluruhanL = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        DashboardL = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 204, 153));

        JenislaporanL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JenislaporanLActionPerformed(evt);
            }
        });

        TampilkanlaporanL.setText("Tampilkan Laporan");
        TampilkanlaporanL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TampilkanlaporanLActionPerformed(evt);
            }
        });

        TabelL.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Kode Barang", "Nama Barang", "Jumlah ", "Harga Satuan", "Total Harga", "Nama Supplier"
            }
        ));
        jScrollPane1.setViewportView(TabelL);

        jLabel1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Pilih Tahun");

        jLabel2.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Pilih Bulan");

        jLabel3.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Jenis Laporan");

        TotalkeseluruhanL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TotalkeseluruhanLActionPerformed(evt);
            }
        });

        jLabel4.setBackground(new java.awt.Color(255, 255, 255));
        jLabel4.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Jumlah Transaksi");

        jLabel5.setBackground(new java.awt.Color(255, 255, 255));
        jLabel5.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Total Keseluruhan");

        DashboardL.setText("Dashboard");
        DashboardL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DashboardLActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(DashboardL)
                .addGap(48, 48, 48))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(DashboardL)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(162, 162, 162)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PilihbulanL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(PilihtahunL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(JenislaporanL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 226, Short.MAX_VALUE)
                .addComponent(TampilkanlaporanL)
                .addGap(183, 183, 183))
            .addComponent(jScrollPane1)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(507, 507, 507)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(JumlahtransaksiL, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TotalkeseluruhanL, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(51, 51, 51)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(PilihbulanL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1)
                    .addComponent(PilihtahunL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JenislaporanL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TampilkanlaporanL))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 120, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(JumlahtransaksiL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(TotalkeseluruhanL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(91, 91, 91))
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

    private void TampilkanlaporanLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TampilkanlaporanLActionPerformed
        // Ubah nama bulan ke angka
    // Ubah nama bulan ke angka
        String[] bulanArray = {"Januari", "Februari", "Maret", "April", "Mei", "Juni",
                "Juli", "Agustus", "September", "Oktober", "November", "Desember"};
        int bulanAngka = 0;
        for (int i = 0; i < bulanArray.length; i++) {
            if (pilihbulan.equalsIgnoreCase(bulanArray[i])) {
                bulanAngka = i + 1;
                break;
            }
        }

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Kode Barang");
        model.addColumn("Nama Barang");
        model.addColumn("Jumlah");
        model.addColumn("Harga Satuan");
        model.addColumn("Total Harga");
        model.addColumn("Nama Supplier");

        int totalHargaSemua = 0;
        int jumlahTransaksi = 0;

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/kasir_warmad", "root", "");
            String sql;

            if (jenislaporan.equals("Penjualan")) {
                sql = "SELECT bj.barcode_barang, bj.nama_barang, dtp.jumlah, dtp.harga_satuan, tk.tanggal " +
                        "FROM transaksi_kasir tk " +
                        "JOIN detail_transaksi_pelanggan dtp ON tk.id_transaksi_kasir = dtp.id_transaksi_kasir " +
                        "JOIN barang_jual bj ON dtp.id_barang_jual = bj.id_barang_jual " +
                        "WHERE MONTH(tk.tanggal) = ? AND YEAR(tk.tanggal) = ?";
            } else {
                sql = "SELECT bj.barcode_barang, bj.nama_barang, lp.jumlah_item, " +
                        "lp.total_harga / lp.jumlah_item AS harga_satuan, sb.nama_supplier, lp.tanggal " +
                        "FROM laporan_penjualan lp " +
                        "JOIN barang_jual bj ON lp.id_barang_jual = bj.id_barang_jual " +
                        "JOIN supplier_barang sb ON lp.id_pembelian_supplier = sb.id_supplier_barang " +
                        "WHERE MONTH(lp.tanggal) = ? AND YEAR(lp.tanggal) = ?";
            }

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, bulanAngka);
            ps.setInt(2, Integer.parseInt(pilihtahun));
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String kodeBarang = rs.getString("barcode_barang");
                String namaBarang = rs.getString("nama_barang");
                int jumlah = jenislaporan.equals("Penjualan") ? rs.getInt("jumlah") : rs.getInt("jumlah_item");
                int hargaSatuan = rs.getInt("harga_satuan");
                int total = jumlah * hargaSatuan;
                String supplier = jenislaporan.equals("Penjualan") ? "-" : rs.getString("nama_supplier");

                model.addRow(new Object[]{kodeBarang, namaBarang, jumlah, hargaSatuan, total, supplier});
                jumlahTransaksi++;
                totalHargaSemua += total;
            }

            TabelL.setModel(model);
            TabelL.setEnabled(false);
            JumlahtransaksiL.setText(String.valueOf(jumlahTransaksi));
            TotalkeseluruhanL.setText(String.valueOf(totalHargaSemua));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/kasir_warmad";
        String user = "root";
        String password = "";

        String query = "SELECT tk.id_transaksi_kasir, tk.tanggal, tk.total_harga, " +
                "dtp.id_barang_jual, dtp.jumlah, dtp.harga_satuan, dtp.subtotal, dtp.diskon " +
                "FROM transaksi_kasir tk " +
                "JOIN detail_transaksi_pelanggan dtp ON tk.id_transaksi_kasir = dtp.id_transaksi_kasir " +
                "ORDER BY tk.tanggal DESC";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("===== LAPORAN PENJUALAN =====");
            while (rs.next()) {
                System.out.println("ID Transaksi     : " + rs.getInt("id_transaksi_kasir"));
                System.out.println("Tanggal          : " + rs.getTimestamp("tanggal"));
                System.out.println("ID Barang        : " + rs.getInt("id_barang_jual"));
                System.out.println("Jumlah           : " + rs.getInt("jumlah"));
                System.out.println("Harga Satuan     : " + rs.getBigDecimal("harga_satuan"));
                System.out.println("Subtotal         : " + rs.getBigDecimal("subtotal"));
                System.out.println("Diskon           : " + rs.getBigDecimal("diskon"));
                System.out.println("Total Harga      : " + rs.getBigDecimal("total_harga"));
                System.out.println("----------------------------------");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    }//GEN-LAST:event_TampilkanlaporanLActionPerformed
        // TODO add your handling code here:
    private void DashboardLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DashboardLActionPerformed
     Dashboard da = new Dashboard();
        da.setVisible(true);
        da.pack();
        da.setLocationRelativeTo(null);
        dispose();
    }//GEN-LAST:event_DashboardLActionPerformed
        // TODO add your handling code here:

    private void TotalkeseluruhanLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TotalkeseluruhanLActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TotalkeseluruhanLActionPerformed

    private void JenislaporanLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JenislaporanLActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_JenislaporanLActionPerformed

    /**
     * @param args the command line arguments
     */

public class Laporan extends JFrame {

    // Konstruktor class Laporan
    public Laporan() {
        initComponents(); // Membangun GUI
    }

    public static void main(String[] args) {
        /* Set the FlatLaf Arc Orange Look and Feel */
        try {
            UIManager.setLookAndFeel(new FlatArcOrangeIJTheme());
            UIManager.put("Button.arc", 999);
            UIManager.put("defaultFont", new Font("Poppins", Font.BOLD, 14));
        } catch (Exception ex) {
            System.err.println("Gagal mengatur tema FlatLaf Arc Orange.");
            ex.printStackTrace();
        }

        /* Buat dan tampilkan form */
        SwingUtilities.invokeLater(() -> {
            Laporan laporan = new Laporan();
            laporan.setVisible(true);
            laporan.setLocationRelativeTo(null); // Tengah layar
        });
    }

    private void initComponents() {
        setTitle("Laporan");
        setSize(800, 600); // Ukuran default
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel label = new JLabel("Contoh Laporan", SwingConstants.CENTER);
        add(label, BorderLayout.CENTER);
    }
}


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton DashboardL;
    private javax.swing.JComboBox<String> JenislaporanL;
    private javax.swing.JTextField JumlahtransaksiL;
    private javax.swing.JComboBox<String> PilihbulanL;
    private javax.swing.JComboBox<String> PilihtahunL;
    private javax.swing.JTable TabelL;
    private javax.swing.JButton TampilkanlaporanL;
    private javax.swing.JTextField TotalkeseluruhanL;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables

    private void isiCombobox() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
