/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package kasir_warmad;

import kasir_warmad.sistem.Koneksi;
import kasir_warmad.sistem.Session;
import desain.GradientPanel;
import utils.RupiahUtil;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.formdev.flatlaf.intellijthemes.FlatArcOrangeIJTheme;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.util.Date;
import javax.swing.UIManager;
import utils.filterAngka;
import javax.swing.text.*;
import kasir_warmad.sistem.SearchBoxBarang;
import utils.RupiahDocumentFilter;

/**
 *
 * @author ThinkPad
 */
public class Transaksi extends javax.swing.JFrame {

    /**
     * Creates new form Laporan
     */
    public Transaksi() {
        initComponents();
        ((AbstractDocument) PotonganhargaT.getDocument()).setDocumentFilter(new RupiahDocumentFilter());
        ((AbstractDocument) PembayaranT.getDocument()).setDocumentFilter(new RupiahDocumentFilter());
        ((AbstractDocument) JumlahT.getDocument()).setDocumentFilter(new filterAngka.AngkaFilter());
        filterAngka.tambahListenerHitung(PotonganhargaT, this::hitungKeseluruhanDanKembalian);
        filterAngka.tambahListenerHitung(PembayaranT, this::hitungKeseluruhanDanKembalian);
        new SearchBoxBarang(searchBarangTxt, kodeBarangTxt, JumlahT, TabelT, TotalsemuaT, KeseluruhanT);

        kodeBarangTxt.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (ambilDataBarangDariBarcode()) {
                        if (JumlahT.getText().trim().isEmpty() || JumlahT.getText().equals("1")) {
                            JumlahT.setText("1");
                            TambahkanTActionPerformed(null);
                            kodeBarangTxt.requestFocus();
                        } else {
                            JumlahT.requestFocus();
                        }
                    }
                }
            }
        });

        // Listener untuk edit jumlah barang dengan double-click
        TabelT.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) { // Double-click
                    int selectedRow = TabelT.getSelectedRow();
                    if (selectedRow >= 0) {
                        System.out.println("click detected on row: " + selectedRow); // Debugging
                        editJumlahBarang(selectedRow);
                    } else {
                        System.out.println("No row selected on double-click"); // Debugging
                    }
                }
            }
        });

        this.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });
    }

    private void formWindowOpened(java.awt.event.WindowEvent evt) {
        kodeBarangTxt.requestFocusInWindow(); // fokus langsung ke kode barang saat form dibuka
    }

    private void clearInput() {

        kodeBarangTxt.setText("");

        JumlahT.setText("1");
    }

    private void hitungKeseluruhanDanKembalian() {
        try {
            BigDecimal totalSemua = RupiahUtil.parse(TotalsemuaT.getText());
            BigDecimal potongan = RupiahUtil.parse(PotonganhargaT.getText());
            BigDecimal bayar = RupiahUtil.parse(PembayaranT.getText());

            BigDecimal keseluruhan = totalSemua.subtract(potongan);
            KeseluruhanT.setText(RupiahUtil.format(keseluruhan));

            if (bayar.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal kembalian = bayar.subtract(keseluruhan);
                KembalianT.setText(RupiahUtil.format(kembalian));
            } else {
                KembalianT.setText("");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Pastikan semua inputan angka valid!");
        }
    }

    private boolean ambilDataBarangDariBarcode() {
        String barcode = kodeBarangTxt.getText().trim();

        if (!barcode.isEmpty()) {
            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost/kasir_warmad", "root", ""); PreparedStatement pst = con.prepareStatement(
                    "SELECT vs.nama_barang, vs.harga_jual, vs.jumlah_stok, vs.tanggal_kadaluarsa "
                    + "FROM v_stok vs "
                    + "WHERE vs.barcode_barang = ? AND vs.tanggal_kadaluarsa >= CURRENT_DATE")) {

                pst.setString(1, barcode);
                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    // Ambil data untuk keperluan lebih lanjut (misalnya, tampilkan di UI atau simpan)
                    String nama = rs.getString("nama_barang");
                    BigDecimal harga = rs.getBigDecimal("harga_jual");
                    int stok = rs.getInt("jumlah_stok");
                    Date tanggalKadaluarsa = rs.getDate("tanggal_kadaluarsa");

                    // Contoh: Tampilkan data di UI (opsional, sesuaikan dengan kebutuhan Anda)
                    // kodeBarangTxt.setText(barcode); // Sudah diisi sebelumnya
                    // Tambahkan logika lain jika diperlukan, misalnya mengisi tabel atau form
                    return true;
                } else {
                    JOptionPane.showMessageDialog(null, "Produk dengan kode " + barcode + " tidak ditemukan atau kadaluarsa!");
                    clearInput();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat mengambil data: " + ex.getMessage());
                clearInput();
            }
        }
        return false;
    }

    private void bayar() {
        Connection conn = null;
        try {
            conn = Koneksi.getKoneksi();
            if (conn == null) {
                JOptionPane.showMessageDialog(this, "Gagal mendapatkan koneksi ke database.");
                return;
            }

            // Nonaktifkan autocommit untuk mengelola transaksi secara manual
            conn.setAutoCommit(false);

            // Ambil dan konversi semua nilai uang menggunakan parseRupiah()
            BigDecimal totalHarga = RupiahUtil.parse(TotalsemuaT.getText());
            BigDecimal diskon = RupiahUtil.parse(PotonganhargaT.getText());
            BigDecimal pembayaran = RupiahUtil.parse(PembayaranT.getText());
            BigDecimal kembalian = RupiahUtil.parse(KembalianT.getText());

            // Langkah 2: Simpan transaksi utama
            String insertTransaksi = "INSERT INTO transaksi_kasir (id_pengguna_aplikasi, total_harga, diskon, pembayaran, kembalian, metode_pembayaran, tanggal) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(insertTransaksi, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, Session.getCurrentUserId());
            stmt.setBigDecimal(2, totalHarga);
            stmt.setBigDecimal(3, diskon);
            stmt.setBigDecimal(4, pembayaran);
            stmt.setBigDecimal(5, kembalian);
            stmt.setString(6, metodeBayarBox.getSelectedItem().toString());
            stmt.setTimestamp(7, new java.sql.Timestamp(System.currentTimeMillis()));
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            int idTransaksi = 0;
            if (rs.next()) {
                idTransaksi = rs.getInt(1);
            }

            // Langkah 3: Simpan detail transaksi dan kurangi stok
            for (int i = 0; i < TabelT.getRowCount(); i++) {
                String barcode = TabelT.getValueAt(i, 0).toString();
                int jumlah = Integer.parseInt(TabelT.getValueAt(i, 3).toString());
                BigDecimal harga = RupiahUtil.parse(TabelT.getValueAt(i, 2).toString());
                BigDecimal subtotal = RupiahUtil.parse(TabelT.getValueAt(i, 4).toString());

                String getIdBarang = "SELECT id_barang_jual FROM barang_jual WHERE barcode_barang = ?";
                PreparedStatement pstId = conn.prepareStatement(getIdBarang);
                pstId.setString(1, barcode);
                ResultSet rsId = pstId.executeQuery();

                if (rsId.next()) {
                    String idBarangJual = rsId.getString("id_barang_jual");

                    // Simpan detail transaksi
                    String insertDetail = "INSERT INTO detail_transaksi_pelanggan (id_transaksi_kasir, id_barang_jual, jumlah, harga_satuan, subtotal, diskon) VALUES (?, ?, ?, ?, ?, ?)";
                    PreparedStatement pstDetail = conn.prepareStatement(insertDetail);
                    pstDetail.setInt(1, idTransaksi);
                    pstDetail.setString(2, idBarangJual);
                    pstDetail.setInt(3, jumlah);
                    pstDetail.setBigDecimal(4, harga);
                    pstDetail.setBigDecimal(5, subtotal);
                    pstDetail.setBigDecimal(6, BigDecimal.ZERO); // diskon per item
                    pstDetail.executeUpdate();

                    // Kurangi stok dari stok_gudang
                    int sisaJumlah = jumlah;
                    String selectStok = "SELECT id_stok_gudang, jumlah_stok FROM stok_gudang WHERE id_barang_jual = ? AND tanggal_kadaluarsa >= CURRENT_DATE ORDER BY tanggal_kadaluarsa ASC";
                    PreparedStatement pstStok = conn.prepareStatement(selectStok);
                    pstStok.setString(1, idBarangJual);
                    ResultSet rsStok = pstStok.executeQuery();

                    while (rsStok.next() && sisaJumlah > 0) {
                        int stokTersedia = rsStok.getInt("jumlah_stok");
                        String idStokGudang = rsStok.getString("id_stok_gudang");

                        if (stokTersedia > 0) {
                            int jumlahKurang = Math.min(sisaJumlah, stokTersedia);
                            int stokBaru = stokTersedia - jumlahKurang;

                            String updateStok = "UPDATE stok_gudang SET jumlah_stok = ? WHERE id_stok_gudang = ?";
                            PreparedStatement pstUpdate = conn.prepareStatement(updateStok);
                            pstUpdate.setInt(1, stokBaru);
                            pstUpdate.setString(2, idStokGudang);
                            pstUpdate.executeUpdate();

                            sisaJumlah -= jumlahKurang;
                        }
                    }

                    if (sisaJumlah > 0) {
                        throw new Exception("Stok tidak cukup untuk barang dengan barcode: " + barcode);
                    }
                } else {
                    throw new Exception("ID Barang tidak ditemukan untuk barcode: " + barcode);
                }
            }

            // Commit transaksi
            conn.commit();
            JOptionPane.showMessageDialog(this, "Transaksi berhasil disimpan!");

            int cetak = JOptionPane.showConfirmDialog(null, "Cetak struk sekarang?", "Cetak Struk", JOptionPane.YES_NO_OPTION);
            if (cetak == JOptionPane.YES_OPTION) {
                reports.nota n = new reports.nota();
                n.printStrukLangsung(idTransaksi);
            }

            DefaultTableModel model = (DefaultTableModel) TabelT.getModel();
            model.setRowCount(0);
            TotalsemuaT.setText("");
            PotonganhargaT.setText("");
            PembayaranT.setText("");
            KembalianT.setText("");
            metodeBayarBox.setSelectedIndex(0);
            kodeBarangTxt.requestFocus();

        } catch (Exception e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Gagal rollback: " + ex.getMessage());
            }
            JOptionPane.showMessageDialog(this, "Gagal menyimpan transaksi: " + e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true); // Kembalikan autocommit ke true
                    conn.close(); // Tutup koneksi
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }

    private void editJumlahBarang(int selectedRow) {
        DefaultTableModel model = (DefaultTableModel) TabelT.getModel();
        String kode = model.getValueAt(selectedRow, 0).toString();
        String nama = model.getValueAt(selectedRow, 1).toString();
        int jumlahLama = Integer.parseInt(model.getValueAt(selectedRow, 3).toString());

        // Tampilkan dialog untuk memasukkan jumlah baru
        String input = JOptionPane.showInputDialog(this, "Masukkan jumlah baru untuk " + nama + ":", jumlahLama);
        if (input == null || input.trim().isEmpty()) {
            return; // User membatalkan input
        }

        try {
            int jumlahBaru = Integer.parseInt(input);
            if (jumlahBaru <= 0) {
                JOptionPane.showMessageDialog(this, "Jumlah harus lebih dari 0!");
                return;
            }

            // Validasi stok menggunakan view v_stok
            Connection conn = Koneksi.getKoneksi();
            String sql = "SELECT SUM(vs.jumlah_stok) AS total_stok "
                    + "FROM v_stok vs "
                    + "WHERE vs.barcode_barang = ? AND vs.tanggal_kadaluarsa >= CURRENT_DATE";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, kode);
            ResultSet rs = pst.executeQuery();

            if (!rs.next()) {
                JOptionPane.showMessageDialog(this, "Barang dengan kode " + kode + " tidak ditemukan atau kadaluarsa.");
                return;
            }

            int stokTersedia = rs.getInt("total_stok");
            int jumlahTotal = jumlahBaru;

            // Hitung total jumlah barang yang sama di tabel (selain baris yang diedit)
            for (int i = 0; i < model.getRowCount(); i++) {
                if (i != selectedRow && model.getValueAt(i, 0).toString().equals(kode)) {
                    jumlahTotal += Integer.parseInt(model.getValueAt(i, 3).toString());
                }
            }

            if (stokTersedia < jumlahTotal) {
                JOptionPane.showMessageDialog(this, "Stok tidak cukup untuk \"" + nama + "\". Stok tersedia: " + stokTersedia + ", diminta: " + jumlahTotal);
                return;
            }

            // Perbarui jumlah dan total di tabel
            BigDecimal harga = RupiahUtil.parse(model.getValueAt(selectedRow, 2).toString());
            BigDecimal totalBaru = harga.multiply(BigDecimal.valueOf(jumlahBaru));
            model.setValueAt(jumlahBaru, selectedRow, 3);
            model.setValueAt(RupiahUtil.format(totalBaru), selectedRow, 4);

            // Hitung ulang total keseluruhan
            BigDecimal totalKeseluruhan = BigDecimal.ZERO;
            for (int i = 0; i < model.getRowCount(); i++) {
                BigDecimal totalItem = RupiahUtil.parse(model.getValueAt(i, 4).toString());
                totalKeseluruhan = totalKeseluruhan.add(totalItem);
            }

            TotalsemuaT.setText(RupiahUtil.format(totalKeseluruhan));
            KeseluruhanT.setText(RupiahUtil.format(totalKeseluruhan));

            System.out.println("Jumlah barang " + nama + " diperbarui menjadi " + jumlahBaru); // Debugging

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Masukkan angka yang valid untuk jumlah!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error saat memeriksa stok: " + e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
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

        jPanel2 = new GradientPanel();
        jLabel5 = new javax.swing.JLabel();
        PembayaranT = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        KeseluruhanT = new javax.swing.JTextField();
        JumlahT = new javax.swing.JTextField();
        bayarBtn = new javax.swing.JButton();
        PotonganhargaT = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        KembalianT = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        TotalsemuaT = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        TabelT = new javax.swing.JTable();
        HapusT = new javax.swing.JButton();
        metodeBayarBox = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        kodeBarangTxt = new javax.swing.JTextField();
        panel1 = new java.awt.Panel();
        DashboardT = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        searchBarangTxt = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        TambahkanT = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setMaximumSize(new java.awt.Dimension(800, 600));
        jPanel2.setMinimumSize(new java.awt.Dimension(800, 600));

        jLabel5.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Total Semua");

        PembayaranT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PembayaranTActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Potongan Harga");

        KeseluruhanT.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        KeseluruhanT.setEnabled(false);
        KeseluruhanT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                KeseluruhanTActionPerformed(evt);
            }
        });

        JumlahT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JumlahTActionPerformed(evt);
            }
        });

        bayarBtn.setText("BAYAR");
        bayarBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bayarBtnActionPerformed(evt);
            }
        });

        PotonganhargaT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PotonganhargaTActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Kembalian");

        jLabel7.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Pembayaran");

        KembalianT.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        KembalianT.setEnabled(false);
        KembalianT.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                KembalianTFocusLost(evt);
            }
        });
        KembalianT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                KembalianTActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Arial Black", 0, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Kode Barang");

        jLabel4.setFont(new java.awt.Font("Arial Black", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Jumlah barang");

        TotalsemuaT.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        TotalsemuaT.setEnabled(false);
        TotalsemuaT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TotalsemuaTActionPerformed(evt);
            }
        });

        TabelT.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        TabelT.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Barcode", "Nama", "Harga", "Jumlah", "Total"
            }
        ));
        TabelT.setColumnSelectionAllowed(false);
        jScrollPane1.setViewportView(TabelT);

        HapusT.setFont(new java.awt.Font("Segoe UI Variable", 1, 12)); // NOI18N
        HapusT.setText("HAPUS");
        HapusT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HapusTActionPerformed(evt);
            }
        });

        metodeBayarBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cash", "Qris" }));
        metodeBayarBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                metodeBayarBoxActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Metode Pembayaran");

        kodeBarangTxt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                kodeBarangTxtFocusLost(evt);
            }
        });
        kodeBarangTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kodeBarangTxtActionPerformed(evt);
            }
        });

        panel1.setForeground(new java.awt.Color(255, 255, 255));

        DashboardT.setFont(new java.awt.Font("Segoe UI Variable", 1, 12)); // NOI18N
        DashboardT.setText("Dashboard");
        DashboardT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DashboardTActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panel1Layout = new javax.swing.GroupLayout(panel1);
        panel1.setLayout(panel1Layout);
        panel1Layout.setHorizontalGroup(
            panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(DashboardT, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17))
        );
        panel1Layout.setVerticalGroup(
            panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(DashboardT)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        jLabel11.setFont(new java.awt.Font("Arial Black", 0, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Cari Barang");

        jLabel9.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Keseluruhan");

        TambahkanT.setFont(new java.awt.Font("Segoe UI Variable", 1, 12)); // NOI18N
        TambahkanT.setText("TAMBAHKAN");
        TambahkanT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TambahkanTActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(PotonganhargaT, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel8))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(TotalsemuaT, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(82, 82, 82)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(PembayaranT, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(77, 77, 77)
                        .addComponent(jLabel10))
                    .addComponent(KembalianT, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(metodeBayarBox, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(72, 72, 72)
                .addComponent(bayarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(124, 124, 124))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(60, 60, 60)
                        .addComponent(jLabel11)
                        .addGap(28, 28, 28)
                        .addComponent(searchBarangTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1)
                        .addGap(27, 27, 27)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(kodeBarangTxt, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JumlahT, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(46, 46, 46)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(TambahkanT, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(HapusT, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(79, 79, 79)
                        .addComponent(KeseluruhanT, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(38, 38, 38))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(78, 78, 78))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(panel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(kodeBarangTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel11)
                        .addComponent(searchBarangTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(TambahkanT, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(KeseluruhanT, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(JumlahT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)
                            .addComponent(HapusT, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(19, 19, 19)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel5)
                                    .addComponent(TotalsemuaT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(PembayaranT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel10)
                                    .addComponent(metodeBayarBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(41, 41, 41)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(PotonganhargaT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6)
                            .addComponent(jLabel8)
                            .addComponent(KembalianT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(bayarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(26, 26, 26))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.getAccessibleContext().setAccessibleName("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void DashboardTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DashboardTActionPerformed
        String role = Session.getCurrentUserRole();
        Dashboard da = new Dashboard(role);
        da.setVisible(true);
        da.pack();
        da.setLocationRelativeTo(null);
        dispose();
    }//GEN-LAST:event_DashboardTActionPerformed

    private void kodeBarangTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kodeBarangTxtActionPerformed
        // TODO add your handling code here:
        ambilDataBarangDariBarcode();
    }//GEN-LAST:event_kodeBarangTxtActionPerformed

    private void kodeBarangTxtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_kodeBarangTxtFocusLost
        // TODO add your handling code here:
        ambilDataBarangDariBarcode();
//        String barcode = kodeBarangTxt.getText();  // Ambil barcode dari TextField
//
//        if (!barcode.isEmpty()) {
//            try {
//                Connection con = DriverManager.getConnection("jdbc:mysql://localhost/kasir_warmad", "root", "");
//                String sql = "SELECT nama_barang, harga_jual FROM barang_jual WHERE barcode_barang = ?";
//                PreparedStatement pst = con.prepareStatement(sql);
//                pst.setString(1, barcode);
//                ResultSet rs = pst.executeQuery();
//
//                if (rs.next()) {
//                    String namaBarang = rs.getString("nama_barang");
//                    int hargaBarang = rs.getInt("harga_jual");
//
//                    namaBarangTxt.setText(namaBarang);
//                    HargaT.setText(String.valueOf(hargaBarang));
//                } else {
//                    JOptionPane.showMessageDialog(null, "Kode barang tidak ditemukan!");
//                    namaBarangTxt.setText("");
//                    HargaT.setText("");
//                }
//
//                rs.close();
//                pst.close();
//                con.close();
//            } catch (Exception ex) {
//                ex.printStackTrace();
//                JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat mengambil data.");
//            }
//        }
    }//GEN-LAST:event_kodeBarangTxtFocusLost

    private void metodeBayarBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_metodeBayarBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_metodeBayarBoxActionPerformed

    private void HapusTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HapusTActionPerformed
        // Kosongkan isi tabel
        int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin kosongkan semua data transaksi?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            ((DefaultTableModel) TabelT.getModel()).setRowCount(0);
            TotalsemuaT.setText("");
            PotonganhargaT.setText("");
            PembayaranT.setText("");
            KembalianT.setText("");
            KeseluruhanT.setText("");
            searchBarangTxt.setText("");
            kodeBarangTxt.setText("");
            JumlahT.setText("");

            JOptionPane.showMessageDialog(this, "Data transaksi telah dikosongkan.");
        }
    }//GEN-LAST:event_HapusTActionPerformed

    private void TotalsemuaTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TotalsemuaTActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TotalsemuaTActionPerformed

    private void TambahkanTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TambahkanTActionPerformed
        try {
            String kode = kodeBarangTxt.getText();
            int jumlah = Integer.parseInt(JumlahT.getText());

            // Ambil data nama, harga, dan cek kadaluarsa dari v_stok
            Connection conn = Koneksi.getKoneksi();
            String sqlBarang = "SELECT vs.nama_barang, vs.harga_jual, SUM(vs.jumlah_stok) AS total_stok "
                    + "FROM v_stok vs "
                    + "WHERE vs.barcode_barang = ? AND vs.tanggal_kadaluarsa >= CURRENT_DATE "
                    + "GROUP BY vs.nama_barang, vs.harga_jual";
            PreparedStatement pstBarang = conn.prepareStatement(sqlBarang);
            pstBarang.setString(1, kode);
            ResultSet rsBarang = pstBarang.executeQuery();

            if (!rsBarang.next()) {
                JOptionPane.showMessageDialog(null, "Barang dengan kode " + kode + " tidak ditemukan atau kadaluarsa.");
                clearInput();
                return;
            }

            String nama = rsBarang.getString("nama_barang");
            BigDecimal harga = rsBarang.getBigDecimal("harga_jual");
            int stokTersedia = rsBarang.getInt("total_stok");

            // Validasi stok
            int jumlahTotal = jumlah;
            DefaultTableModel model = (DefaultTableModel) TabelT.getModel();
            for (int i = 0; i < model.getRowCount(); i++) {
                if (model.getValueAt(i, 0).toString().equals(kode)) {
                    int jumlahLama = Integer.parseInt(model.getValueAt(i, 3).toString());
                    jumlahTotal += jumlahLama;
                    break;
                }
            }

            if (stokTersedia < jumlahTotal) {
                JOptionPane.showMessageDialog(null, "Stok tidak cukup untuk \"" + nama + "\". Stok tersedia: " + stokTersedia + ", diminta: " + jumlahTotal);
                clearInput();
                return;
            }

            // Tambah ke tabel atau update jumlah jika sudah ada
            boolean barangSudahAda = false;
            for (int i = 0; i < model.getRowCount(); i++) {
                if (model.getValueAt(i, 0).toString().equals(kode)) {
                    int jumlahLama = Integer.parseInt(model.getValueAt(i, 3).toString());
                    int jumlahBaru = jumlahLama + jumlah;
                    BigDecimal totalBaru = harga.multiply(BigDecimal.valueOf(jumlahBaru));
                    model.setValueAt(jumlahBaru, i, 3);
                    model.setValueAt(RupiahUtil.format(totalBaru), i, 4);
                    barangSudahAda = true;
                    break;
                }
            }

            if (!barangSudahAda) {
                BigDecimal total = harga.multiply(BigDecimal.valueOf(jumlah));
                model.addRow(new Object[]{kode, nama, RupiahUtil.format(harga), jumlah, RupiahUtil.format(total)});
            }

            // Hitung ulang total keseluruhan
            BigDecimal totalKeseluruhan = BigDecimal.ZERO;
            for (int i = 0; i < model.getRowCount(); i++) {
                BigDecimal totalItem = RupiahUtil.parse(model.getValueAt(i, 4).toString());
                totalKeseluruhan = totalKeseluruhan.add(totalItem);
            }

            TotalsemuaT.setText(RupiahUtil.format(totalKeseluruhan));
            KeseluruhanT.setText(RupiahUtil.format(totalKeseluruhan));

            // Reset input
            searchBarangTxt.setText("");
            kodeBarangTxt.setText("");
            JumlahT.setText("1");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Masukkan angka yang valid untuk jumlah!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error saat memeriksa stok: " + e.getMessage());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
    }//GEN-LAST:event_TambahkanTActionPerformed

    private void KembalianTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_KembalianTActionPerformed

    }//GEN-LAST:event_KembalianTActionPerformed

    private void KembalianTFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_KembalianTFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_KembalianTFocusLost

    private void PotonganhargaTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PotonganhargaTActionPerformed

    }//GEN-LAST:event_PotonganhargaTActionPerformed

    private void bayarBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bayarBtnActionPerformed
        bayar();
    }//GEN-LAST:event_bayarBtnActionPerformed

    private void JumlahTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JumlahTActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_JumlahTActionPerformed

    private void KeseluruhanTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_KeseluruhanTActionPerformed
        hitungKeseluruhanDanKembalian();
    }//GEN-LAST:event_KeseluruhanTActionPerformed

    private void PembayaranTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PembayaranTActionPerformed
        hitungKeseluruhanDanKembalian();
    }//GEN-LAST:event_PembayaranTActionPerformed

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
        } catch (Exception ex) {
            System.err.println("Gagal mengatur tema FlatLaf Arc Orange.");
            ex.printStackTrace();
        }

        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Transaksi().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton DashboardT;
    private javax.swing.JButton HapusT;
    private javax.swing.JTextField JumlahT;
    private javax.swing.JTextField KembalianT;
    private javax.swing.JTextField KeseluruhanT;
    private javax.swing.JTextField PembayaranT;
    private javax.swing.JTextField PotonganhargaT;
    private javax.swing.JTable TabelT;
    private javax.swing.JButton TambahkanT;
    private javax.swing.JTextField TotalsemuaT;
    private javax.swing.JButton bayarBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField kodeBarangTxt;
    private javax.swing.JComboBox<String> metodeBayarBox;
    private java.awt.Panel panel1;
    private javax.swing.JTextField searchBarangTxt;
    // End of variables declaration//GEN-END:variables
}
