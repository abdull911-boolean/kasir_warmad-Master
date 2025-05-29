package kasir_warmad.sistem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import javax.swing.table.DefaultTableModel;
import utils.RupiahUtil;

public class SearchBoxBarang {

    private JTextField searchBarangTxt;
    private JTextField kodeBarangTxt;
    private JTextField JumlahT;
    private JTable TabelT;
    private JTextField TotalsemuaT;
    private JTextField KeseluruhanT;
    private JPopupMenu popupMenu;
    private JList<String> searchList;
    private DefaultListModel<String> listModel;
    private List<Barang> barangList;
    private String lastSearchText = ""; // Variabel untuk debounce

    private static class Barang {

        String kode;
        String nama;
        BigDecimal harga;
        int stok;

        Barang(String kode, String nama, BigDecimal harga, int stok) {
            this.kode = kode;
            this.nama = nama;
            this.harga = harga;
            this.stok = stok;
        }
    }

    public SearchBoxBarang(JTextField searchBarangTxt, JTextField kodeBarangTxt, JTextField JumlahT,
            JTable TabelT, JTextField TotalsemuaT, JTextField KeseluruhanT) {
        this.searchBarangTxt = searchBarangTxt;
        this.kodeBarangTxt = kodeBarangTxt;
        this.JumlahT = JumlahT;
        this.TabelT = TabelT;
        this.TotalsemuaT = TotalsemuaT;
        this.KeseluruhanT = KeseluruhanT;
        listModel = new DefaultListModel<>();
        searchList = new JList<>(listModel);
        popupMenu = new JPopupMenu();
        popupMenu.add(new JScrollPane(searchList));
        barangList = new ArrayList<>();

        searchList.setFont(new Font("Arial", Font.PLAIN, 14));
        searchList.setBackground(Color.WHITE);
        searchList.setFixedCellHeight(25);

        // Nonaktifkan fokus pada popup dan JList
        popupMenu.setFocusable(false);
        searchList.setFocusable(false);

        searchBarangTxt.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String searchText = searchBarangTxt.getText().trim();
                System.out.println("Key released: searchText='" + searchText + "', lastSearchText='" + lastSearchText + "'");

                searchBarangTxt.requestFocusInWindow();

                if (searchText.isEmpty()) {
                    System.out.println("Search text kosong, menyembunyikan popup");
                    popupMenu.setVisible(false);
                    listModel.clear();
                    barangList.clear();
                    return;
                }

                if (!searchText.equals(lastSearchText)) {
                    lastSearchText = searchText;
                    System.out.println("Mencari barang dengan searchText: " + searchText);
                    searchBarang(searchText);
                } else {
                    System.out.println("Search text sama dengan sebelumnya, tidak melakukan pencarian ulang");
                }
            }
        });

        searchList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    int index = searchList.getSelectedIndex();
                    System.out.println("Mouse clicked: selectedIndex=" + index + ", barangList size=" + barangList.size());
                    if (index >= 0 && index < barangList.size()) {
                        try {
                            Barang selectedBarang = barangList.get(index);
                            System.out.println("Barang dipilih: kode=" + selectedBarang.kode + ", nama=" + selectedBarang.nama);
                            kodeBarangTxt.setText(selectedBarang.kode);
                            // Set JumlahT menjadi "1" sebelum menambahkan ke tabel
                            JumlahT.setText("1");
                            tambahKeTabel(selectedBarang);
                            System.out.println("Item dipilih dan ditambahkan ke tabel, menyembunyikan popup");
                            popupMenu.setVisible(false);
                            searchBarangTxt.requestFocusInWindow();
                        } catch (Exception ex) {
                            System.err.println("Error saat memilih barang: " + ex.getMessage());
                            ex.printStackTrace();
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    JOptionPane.showMessageDialog(null, "Error saat memilih barang: " + ex.getMessage());
                                }
                            });
                        }
                    } else {
                        System.out.println("Index tidak valid atau barangList tidak sinkron: index=" + index + ", barangList size=" + barangList.size());
                    }
                }
            }
        });

        searchBarangTxt.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                System.out.println("Focus lost detected on searchBarangTxt");
                Timer timer = new Timer(200, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        if (!searchBarangTxt.isFocusOwner()) {
                            System.out.println("Focus benar-benar hilang, menyembunyikan popup");
                            popupMenu.setVisible(false);
                        } else {
                            System.out.println("Focus kembali ke searchBarangTxt, tidak menyembunyikan popup");
                        }
                    }
                });
                timer.setRepeats(false);
                timer.start();
            }
        });

        popupMenu.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentHidden(ComponentEvent e) {
                System.out.println("Popup disembunyikan secara tidak terduga");
            }
        });
    }

    private void searchBarang(String searchText) {
        DefaultListModel<String> newListModel = new DefaultListModel<>();
        List<Barang> newBarangList = new ArrayList<>();

        try {
            Connection conn = Koneksi.getKoneksi();
            if (conn == null) {
                System.err.println("Koneksi ke database gagal: conn adalah null");
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        JOptionPane.showMessageDialog(null, "Gagal terhubung ke database.");
                    }
                });
                return;
            }
            System.out.println("Terhubung ke database. Mencari: " + searchText);
            String sql = "SELECT barcode_barang, nama_barang, harga_jual, jumlah_stok "
                    + "FROM v_stok "
                    + "WHERE nama_barang LIKE ? AND jumlah_stok > 0";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, "%" + searchText + "%");
            ResultSet rs = pst.executeQuery();

            int rowCount = 0;
            while (rs.next()) {
                String kode = rs.getString("barcode_barang");
                String nama = rs.getString("nama_barang");
                BigDecimal harga = rs.getBigDecimal("harga_jual");
                int stok = rs.getInt("jumlah_stok");

                newBarangList.add(new Barang(kode, nama, harga, stok));
                newListModel.addElement(nama + " (Stok: " + stok + ")");
                rowCount++;
            }
            System.out.println("Pencarian untuk '" + searchText + "' menghasilkan " + rowCount + " barang.");

            listModel.clear();
            barangList.clear();
            for (int i = 0; i < newListModel.size(); i++) {
                listModel.addElement(newListModel.getElementAt(i));
            }
            barangList.addAll(newBarangList);
            System.out.println("List model diperbarui dengan " + listModel.getSize() + " item");

            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    if (listModel.getSize() > 0) {
                        System.out.println("Menampilkan popup dengan " + listModel.getSize() + " item");
                        popupMenu.setPreferredSize(new Dimension(searchBarangTxt.getWidth(), 150));
                        popupMenu.show(searchBarangTxt, 0, searchBarangTxt.getHeight());
                        searchBarangTxt.requestFocusInWindow();
                    } else {
                        System.out.println("Tidak ada hasil pencarian, menyembunyikan popup");
                        popupMenu.setVisible(false);
                    }
                }
            });

        } catch (SQLException ex) {
            System.err.println("SQLException: " + ex.getMessage());
            ex.printStackTrace();
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    JOptionPane.showMessageDialog(null, "Error saat mencari barang: " + ex.getMessage());
                }
            });
        } catch (Exception ex) {
            System.err.println("Exception: " + ex.getMessage());
            ex.printStackTrace();
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    JOptionPane.showMessageDialog(null, "Error tak terduga: " + ex.getMessage());
                }
            });
        }
    }

    private void tambahKeTabel(Barang barang) {
        try {
            String jumlahText = JumlahT.getText().trim();
            if (jumlahText.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Jumlah tidak boleh kosong!");
                return;
            }

            int jumlah = Integer.parseInt(jumlahText);
            if (jumlah <= 0) {
                JOptionPane.showMessageDialog(null, "Jumlah harus lebih dari 0!");
                return;
            }

            if (jumlah > barang.stok) {
                JOptionPane.showMessageDialog(null, "Stok tidak cukup untuk \"" + barang.nama
                        + "\". Stok tersedia: " + barang.stok + ", diminta: " + jumlah);
                return;
            }

            Connection conn = Koneksi.getKoneksi();
            if (conn == null) {
                JOptionPane.showMessageDialog(null, "Gagal terhubung ke database.");
                return;
            }

            String sqlUpdate = "UPDATE stok_gudang "
                    + "SET jumlah_stok = jumlah_stok - ? "
                    + "WHERE id_barang_jual = (SELECT id_barang_jual FROM barang_jual WHERE barcode_barang = ?) "
                    + "AND jumlah_stok > 0 "
                    + "LIMIT 1";
            PreparedStatement pstUpdate = conn.prepareStatement(sqlUpdate);
            pstUpdate.setInt(1, jumlah);
            pstUpdate.setString(2, barang.kode);
            int rowsAffected = pstUpdate.executeUpdate();

            if (rowsAffected == 0) {
                JOptionPane.showMessageDialog(null, "Gagal mengurangi stok: Tidak ada stok yang cukup.");
                return;
            }

            DefaultTableModel model = (DefaultTableModel) TabelT.getModel();
            boolean barangSudahAda = false;

            for (int i = 0; i < model.getRowCount(); i++) {
                if (model.getValueAt(i, 0).toString().equals(barang.kode)) {
                    int jumlahLama = Integer.parseInt(model.getValueAt(i, 3).toString());
                    int jumlahBaru = jumlahLama + jumlah;
                    if (jumlahBaru > barang.stok) {
                        JOptionPane.showMessageDialog(null, "Stok tidak cukup untuk \"" + barang.nama
                                + "\". Stok tersedia: " + barang.stok + ", diminta: " + jumlahBaru);
                        return;
                    }
                    BigDecimal totalBaru = barang.harga.multiply(BigDecimal.valueOf(jumlahBaru));
                    model.setValueAt(jumlahBaru, i, 3);
                    model.setValueAt(RupiahUtil.format(totalBaru), i, 4);
                    barangSudahAda = true;
                    break;
                }
            }

            if (!barangSudahAda) {
                BigDecimal total = barang.harga.multiply(BigDecimal.valueOf(jumlah));
                model.addRow(new Object[]{barang.kode, barang.nama, RupiahUtil.format(barang.harga), jumlah, RupiahUtil.format(total)});
            }

            BigDecimal totalKeseluruhan = BigDecimal.ZERO;
            for (int i = 0; i < model.getRowCount(); i++) {
                BigDecimal totalItem = RupiahUtil.parse(model.getValueAt(i, 4).toString());
                totalKeseluruhan = totalKeseluruhan.add(totalItem);
            }

            TotalsemuaT.setText(RupiahUtil.format(totalKeseluruhan));
            KeseluruhanT.setText(RupiahUtil.format(totalKeseluruhan));

            // Hanya reset searchBarangTxt dan JumlahT, biarkan kodeBarangTxt tetap menampilkan barcode
            searchBarangTxt.setText("");
            JumlahT.setText("1");

        } catch (NumberFormatException e) {
            System.err.println("NumberFormatException di tambahKeTabel: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Masukkan angka yang valid untuk jumlah!");
        } catch (SQLException e) {
            System.err.println("SQLException di tambahKeTabel: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error saat memperbarui stok: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Exception di tambahKeTabel: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error tak terduga: " + e.getMessage());
        }
    }
}
