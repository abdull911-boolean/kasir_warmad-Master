/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package kasir_warmad.tampilan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import kasir_warmad.Dashboard;
import kasir_warmad.Kelola_barang;
import kasir_warmad.sistem.Koneksi;
import kasir_warmad.sistem.Session;
import utils.PhoneNumberFormatter;
import utils.RupiahUtil;

/**
 *
 * @author Ariel
 */
public class Supplier extends javax.swing.JDialog {

    /**
     * Creates new form Supplier
     */
    public Supplier(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        tampilkanBarangKeTabelS();
        new PhoneNumberFormatter(KontakTxt);

        tableS.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && tableS.getSelectedRow() != -1) {
                int selectedRow = tableS.getSelectedRow();
                String idSupplier = tableS.getValueAt(selectedRow, 0).toString();
                String namaSupplier = tableS.getValueAt(selectedRow, 1).toString();
                String alamatSupplier = tableS.getValueAt(selectedRow, 2).toString();
                String kontakSupplier = tableS.getValueAt(selectedRow, 3).toString();
                showEditDialog(idSupplier, namaSupplier, alamatSupplier, kontakSupplier);
            }
        });
    }

    private String generateIdSupplier() throws SQLException {
        String prefix = "SPLRBRNG25";
        String query = "SELECT id_supplier_barang FROM supplier_barang ORDER BY id_supplier_barang DESC LIMIT 1";

        // Ambil koneksi dari kelas Koneksi
        Connection conn = Koneksi.getKoneksi();
        if (conn == null) {
            throw new SQLException("Gagal mendapatkan koneksi ke database.");
        }

        try (PreparedStatement pst = conn.prepareStatement(query); ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                String lastId = rs.getString("id_supplier_barang");
                int number = Integer.parseInt(lastId.substring(10));
                return prefix + String.format("%03d", number + 1);
            }
        }
        return prefix + "001";
    }

    public void tampilkanBarangKeTabelS() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID Supplier");
        model.addColumn("Nama Supplier");
        model.addColumn("Alamat Supplier");
        model.addColumn("Kontak Supplier");

        try (Connection conn = Koneksi.getKoneksi(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(
                "SELECT * FROM supplier_barang;"
        )) {

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("id_supplier_barang"),
                    rs.getString("nama_supplier"),
                    rs.getString("alamat_supplier"),
                    rs.getString("kontak_supplier")
                });
            }

            tableS.setModel(model);
            System.out.println("DEBUG: Data berhasil dimuat ke tabel");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal menampilkan data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showEditDialog(String idSupplier, String namaSupplier, String alamatSupplier, String kontakSupplier) {
        JDialog editDialog = new JDialog(this, "Edit Supplier", true);
        editDialog.setSize(300, 250);
        editDialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        // Labels and text fields
        JLabel namaLabel = new JLabel("Nama");
        namaLabel.setBounds(20, 20, 80, 25);
        JTextField namaField = new JTextField(namaSupplier);
        namaField.setBounds(100, 20, 160, 25);

        JLabel alamatLabel = new JLabel("Alamat");
        alamatLabel.setBounds(20, 60, 80, 25);
        JTextField alamatField = new JTextField(alamatSupplier);
        alamatField.setBounds(100, 60, 160, 25);

        JLabel kontakLabel = new JLabel("Kontak");
        kontakLabel.setBounds(20, 100, 80, 25);
        JTextField kontakField = new JTextField(kontakSupplier);
        kontakField.setBounds(100, 100, 160, 25);

        // Buttons
        JButton okButton = new JButton("OK");
        okButton.setBounds(100, 150, 80, 25);
        okButton.addActionListener(e -> {
            String newNama = namaField.getText().trim();
            String newAlamat = alamatField.getText().trim();
            String newKontak = kontakField.getText().trim();

            // Validate fields
            if (newNama.isEmpty() || newAlamat.isEmpty() || newKontak.isEmpty()) {
                JOptionPane.showMessageDialog(editDialog, "Semua field harus diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

//            PhoneNumberFormatter tempFormatter = new PhoneNumberFormatter(KontakTxt);
//            if (!tempFormatter.isValidPhoneNumber()) {
//                JOptionPane.showMessageDialog(null, "Nomor kontak harus antara 9-13 karakter termasuk prefix 62!", "Peringatan", JOptionPane.WARNING_MESSAGE);
//                return;
//            }

            // Update database
            try (Connection conn = Koneksi.getKoneksi(); PreparedStatement ps = conn.prepareStatement(
                    "UPDATE supplier_barang SET nama_supplier = ?, alamat_supplier = ?, kontak_supplier = ? WHERE id_supplier_barang = ?")) {
                ps.setString(1, newNama);
                ps.setString(2, newAlamat);
                ps.setString(3, newKontak);
                ps.setString(4, idSupplier);
                int rowsAffected = ps.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(editDialog, "Data Supplier berhasil diperbarui!");
                    tampilkanBarangKeTabelS(); // Refresh table
                    editDialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(editDialog, "Gagal memperbarui data: Supplier tidak ditemukan!");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(editDialog, "Gagal menyimpan data: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBounds(180, 150, 80, 25);
        cancelButton.addActionListener(e -> editDialog.dispose());

        // Add components to panel
        panel.add(namaLabel);
        panel.add(namaField);
        panel.add(alamatLabel);
        panel.add(alamatField);
        panel.add(kontakLabel);
        panel.add(kontakField);
        panel.add(okButton);
        panel.add(cancelButton);

        editDialog.add(panel);
        editDialog.setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableS = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        namaS = new javax.swing.JTextField();
        KontakTxt = new javax.swing.JTextField();
        alamatS = new javax.swing.JTextField();
        batalS = new javax.swing.JButton();
        simpanS = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        tableS.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tableS);

        jLabel1.setText("Nama");

        jLabel2.setText("Kontak");

        jLabel3.setText("Alamat");

        batalS.setText("Batal");
        batalS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batalSActionPerformed(evt);
            }
        });

        simpanS.setText("Simpan");
        simpanS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                simpanSActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Verdana", 0, 24)); // NOI18N
        jLabel4.setText("Supplier");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(namaS, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(KontakTxt, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(alamatS, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addComponent(simpanS)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(batalS)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 47, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 329, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(59, 59, 59)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(namaS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(KontakTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(alamatS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 80, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(batalS)
                    .addComponent(simpanS))
                .addGap(33, 33, 33))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 6, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void simpanSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_simpanSActionPerformed
        String namaSupplier = namaS.getText().trim();
        String alamatSupplier = alamatS.getText().trim();
        String kontakSupplier = KontakTxt.getText().trim();

        // Validate fields
        if (namaSupplier.isEmpty() || alamatSupplier.isEmpty() || kontakSupplier.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Semua field harus diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

//        PhoneNumberFormatter tempFormatter = new PhoneNumberFormatter(KontakTxt);
//        if (!tempFormatter.isValidPhoneNumber()) {
//            JOptionPane.showMessageDialog(null, "Nomor kontak harus antara 9-13 karakter termasuk prefix 62!", "Peringatan", JOptionPane.WARNING_MESSAGE);
//            return;
//        }

        try {
            Connection conn = Koneksi.getKoneksi();
            String idSupplierStr = generateIdSupplier();
            String insertSupplier = "INSERT INTO supplier_barang (id_supplier_barang, nama_supplier, alamat_supplier, kontak_supplier) VALUES (?, ?, ?, ?)";
            try (PreparedStatement psSupplier = conn.prepareStatement(insertSupplier)) {
                psSupplier.setString(1, idSupplierStr);
                psSupplier.setString(2, namaSupplier);
                psSupplier.setString(3, alamatSupplier);
                psSupplier.setString(4, kontakSupplier);
                psSupplier.executeUpdate();
            }

            JOptionPane.showMessageDialog(null, "Data Supplier berhasil disimpan!");
            namaS.setText("");
            alamatS.setText("");
            KontakTxt.setText("");
            tampilkanBarangKeTabelS();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal menyimpan data: " + e.getMessage());
            e.printStackTrace();
        }
    }//GEN-LAST:event_simpanSActionPerformed

    private void batalSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batalSActionPerformed
        // TODO add your handling code here:
//        String role = Session.getCurrentUserRole();
//        Dashboard da = new Dashboard(role);
//        da.setVisible(true);
//        da.pack();
//        da.setLocationRelativeTo(null);
        dispose();
    }//GEN-LAST:event_batalSActionPerformed

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
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Supplier.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Supplier.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Supplier.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Supplier.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Supplier dialog = new Supplier(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField KontakTxt;
    private javax.swing.JTextField alamatS;
    private javax.swing.JButton batalS;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField namaS;
    private javax.swing.JButton simpanS;
    private javax.swing.JTable tableS;
    // End of variables declaration//GEN-END:variables
}
