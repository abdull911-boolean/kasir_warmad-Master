/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package kasir_warmad;

import com.formdev.flatlaf.intellijthemes.FlatArcOrangeIJTheme;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.showMessageDialog;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import kasir_warmad.Dashboard;
import kasir_warmad.sistem.Koneksi;
import kasir_warmad.sistem.Session;

public class login extends javax.swing.JFrame {

    /**
     * Creates new form login
     */
    public login() {
        initComponents();  // Inisialisasi komponen GUI
        // Set fokus ke field RFID saat form login dibuka
        SwingUtilities.invokeLater(() -> {
            RfidL.requestFocusInWindow();  // Pastikan nama field sesuai dengan nama objek di form
        });

        UsernameL.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PasswordLBL.requestFocusInWindow();
            }
        });

        PasswordLBL.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginL.doClick(); // Seolah klik tombol login
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelluar1L = new javax.swing.JPanel();
        panelluar2L = new javax.swing.JPanel();
        paneldalamL = new javax.swing.JPanel();
        UsernameL = new javax.swing.JTextField();
        LloginL = new javax.swing.JLabel();
        LusernameL = new javax.swing.JLabel();
        LpasswordL = new javax.swing.JLabel();
        LrfidL = new javax.swing.JLabel();
        RfidL = new javax.swing.JTextField();
        LoginL = new javax.swing.JButton();
        PasswordLBL = new javax.swing.JPasswordField();
        RegisterL1 = new javax.swing.JButton();
        WELCOME = new java.awt.Label();
        label1 = new java.awt.Label();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        panelluar1L.setBackground(new java.awt.Color(255, 255, 255));

        panelluar2L.setBackground(new java.awt.Color(255, 204, 153));

        paneldalamL.setBackground(new java.awt.Color(255, 153, 51));

        UsernameL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UsernameLActionPerformed(evt);
            }
        });

        LloginL.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        LloginL.setForeground(new java.awt.Color(255, 255, 255));
        LloginL.setText("LOGIN");

        LusernameL.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        LusernameL.setForeground(new java.awt.Color(255, 255, 255));
        LusernameL.setText("Username");

        LpasswordL.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        LpasswordL.setForeground(new java.awt.Color(255, 255, 255));
        LpasswordL.setText("Password");

        LrfidL.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        LrfidL.setForeground(new java.awt.Color(255, 255, 255));
        LrfidL.setText("Kode RFID");

        RfidL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RfidLActionPerformed(evt);
            }
        });
        RfidL.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                RfidLKeyReleased(evt);
            }
        });

        LoginL.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        LoginL.setForeground(new java.awt.Color(255, 153, 51));
        LoginL.setText("LOGIN");
        LoginL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LoginLActionPerformed(evt);
            }
        });
        LoginL.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                LoginLKeyReleased(evt);
            }
        });

        RegisterL1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        RegisterL1.setForeground(new java.awt.Color(255, 153, 51));
        RegisterL1.setText("Register");
        RegisterL1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RegisterL1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout paneldalamLLayout = new javax.swing.GroupLayout(paneldalamL);
        paneldalamL.setLayout(paneldalamLLayout);
        paneldalamLLayout.setHorizontalGroup(
            paneldalamLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paneldalamLLayout.createSequentialGroup()
                .addGap(245, 245, 245)
                .addComponent(LloginL)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, paneldalamLLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(paneldalamLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(LusernameL)
                    .addComponent(RfidL, javax.swing.GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE)
                    .addComponent(UsernameL)
                    .addComponent(LpasswordL)
                    .addComponent(LrfidL, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(PasswordLBL))
                .addGap(175, 175, 175))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, paneldalamLLayout.createSequentialGroup()
                .addContainerGap(258, Short.MAX_VALUE)
                .addGroup(paneldalamLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(RegisterL1, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LoginL, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(233, 233, 233))
        );
        paneldalamLLayout.setVerticalGroup(
            paneldalamLLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paneldalamLLayout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addComponent(LloginL)
                .addGap(45, 45, 45)
                .addComponent(LusernameL)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(UsernameL, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(LpasswordL)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PasswordLBL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(LrfidL)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(RfidL, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(LoginL, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(RegisterL1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(146, 146, 146))
        );

        javax.swing.GroupLayout panelluar2LLayout = new javax.swing.GroupLayout(panelluar2L);
        panelluar2L.setLayout(panelluar2LLayout);
        panelluar2LLayout.setHorizontalGroup(
            panelluar2LLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelluar2LLayout.createSequentialGroup()
                .addContainerGap(141, Short.MAX_VALUE)
                .addComponent(paneldalamL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(128, 128, 128))
        );
        panelluar2LLayout.setVerticalGroup(
            panelluar2LLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelluar2LLayout.createSequentialGroup()
                .addGap(110, 110, 110)
                .addComponent(paneldalamL, javax.swing.GroupLayout.PREFERRED_SIZE, 498, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(160, Short.MAX_VALUE))
        );

        WELCOME.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 36)); // NOI18N
        WELCOME.setForeground(new java.awt.Color(255, 153, 0));
        WELCOME.setText("WELCOME");

        label1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        label1.setForeground(new java.awt.Color(255, 153, 0));
        label1.setText("To The Page");

        javax.swing.GroupLayout panelluar1LLayout = new javax.swing.GroupLayout(panelluar1L);
        panelluar1L.setLayout(panelluar1LLayout);
        panelluar1LLayout.setHorizontalGroup(
            panelluar1LLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelluar1LLayout.createSequentialGroup()
                .addGroup(panelluar1LLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelluar1LLayout.createSequentialGroup()
                        .addGap(84, 84, 84)
                        .addComponent(WELCOME, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelluar1LLayout.createSequentialGroup()
                        .addGap(124, 124, 124)
                        .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 98, Short.MAX_VALUE)
                .addComponent(panelluar2L, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        panelluar1LLayout.setVerticalGroup(
            panelluar1LLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelluar2L, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(panelluar1LLayout.createSequentialGroup()
                .addGap(274, 274, 274)
                .addComponent(WELCOME, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelluar1L, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelluar1L, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void UsernameLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UsernameLActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_UsernameLActionPerformed

    private void LoginLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LoginLActionPerformed
        String username, password, rfid, query, passDb = null;
        int userId = 0;

        try {
            Connection con = Koneksi.getKoneksi();
            if (con == null) {
                JOptionPane.showMessageDialog(null, "Gagal mendapatkan koneksi ke database.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            username = UsernameL.getText().trim();
            password = PasswordLBL.getText().trim();
            rfid = RfidL.getText().trim();

            if (!rfid.isEmpty()) {
                // Login via RFID
                query = "SELECT * FROM pengguna_aplikasi WHERE rfid_kode = ?";
                PreparedStatement ps = con.prepareStatement(query);
                ps.setString(1, rfid);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    userId = rs.getInt("id_pengguna");
                    String user = rs.getString("username");
                    String role = rs.getString("role");

                    Session.setCurrentUserId(userId);
                    Session.setCurrentUserRole(role);

                    JOptionPane.showMessageDialog(null, "Login via RFID berhasil! Selamat datang, " + user);

                    Dashboard da = new Dashboard(role);
                    da.setVisible(true);
                    da.pack();
                    da.setLocationRelativeTo(null);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "RFID tidak ditemukan di database", "Login Gagal", JOptionPane.ERROR_MESSAGE);
                }
                rs.close();
                ps.close();
            } else {
                // Login manual
                if (username.isEmpty()) {
                    JOptionPane.showMessageDialog(new JFrame(), "Username is required", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (password.isEmpty()) {
                    JOptionPane.showMessageDialog(new JFrame(), "Password is required", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    query = "SELECT * FROM pengguna_aplikasi WHERE username= ?";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, username);
                    ResultSet rs = ps.executeQuery();

                    if (rs.next()) {
                        passDb = rs.getString("password");
                        userId = rs.getInt("id_pengguna");
                        String role = rs.getString("role");

                        if (org.mindrot.jbcrypt.BCrypt.checkpw(password, passDb)) {
                            Session.setCurrentUserId(userId);
                            Session.setCurrentUserRole(role);

                            JOptionPane.showMessageDialog(null, "Login Berhasil! Welcome " + username);

                            Dashboard da = new Dashboard(role);
                            da.setVisible(true);
                            da.pack();
                            da.setLocationRelativeTo(null);
                            dispose();
                        } else {
                            JOptionPane.showMessageDialog(new JFrame(), "Password salah", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(new JFrame(), "Username tidak ditemukan", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    rs.close();
                    ps.close();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_LoginLActionPerformed

    private void RfidLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RfidLActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_RfidLActionPerformed

    private void RegisterL1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RegisterL1ActionPerformed
        register rg = new register();
        rg.setVisible(true);
        rg.pack();
        rg.setLocationRelativeTo(null);
        dispose();
    }//GEN-LAST:event_RegisterL1ActionPerformed

    private void LoginLKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_LoginLKeyReleased

    }//GEN-LAST:event_LoginLKeyReleased

    private void RfidLKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_RfidLKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String rfid = RfidL.getText().trim();

            if (!rfid.isEmpty()) {
                try {
                    Connection con = Koneksi.getKoneksi();
                    if (con == null) {
                        JOptionPane.showMessageDialog(this, "Gagal mendapatkan koneksi ke database.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    String query = "SELECT * FROM pengguna_aplikasi WHERE rfid_kode = ?";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, rfid);
                    ResultSet rs = ps.executeQuery();

                    if (rs.next()) {
                        String usn = rs.getString("username");
                        int userId = rs.getInt("id_pengguna");
                        String role = rs.getString("role");

                        UsernameL.setText(usn);
                        PasswordLBL.setText("");

                        Session.setCurrentUserId(userId);
                        Session.setCurrentUserRole(role);

                        JOptionPane.showMessageDialog(this, "Login via RFID berhasil! Selamat datang " + usn);

                        Dashboard db = new Dashboard(role);
                        db.setVisible(true);
                        db.pack();
                        db.setLocationRelativeTo(null);
                        this.dispose();
                    } else {
                        JOptionPane.showMessageDialog(this, "RFID tidak ditemukan");
                        register rg = new register();
                        rg.setVisible(true);
                        rg.pack();
                        rg.setLocationRelativeTo(null);
                        dispose();
                    }

                    rs.close();
                    ps.close();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
                }
            }
        }
    }//GEN-LAST:event_RfidLKeyReleased

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
            UIManager.put("Component.innerFocusWidth", 1);
            UIManager.put("Button.innerFocusWidth", 1);
        } catch (Exception ex) {
            System.err.println("Gagal mengatur tema FlatLaf Arc Orange.");
            ex.printStackTrace();
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new login().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel LloginL;
    private javax.swing.JButton LoginL;
    private javax.swing.JLabel LpasswordL;
    private javax.swing.JLabel LrfidL;
    private javax.swing.JLabel LusernameL;
    private javax.swing.JPasswordField PasswordLBL;
    private javax.swing.JButton RegisterL1;
    private javax.swing.JTextField RfidL;
    private javax.swing.JTextField UsernameL;
    private java.awt.Label WELCOME;
    private java.awt.Label label1;
    private javax.swing.JPanel paneldalamL;
    private javax.swing.JPanel panelluar1L;
    private javax.swing.JPanel panelluar2L;
    // End of variables declaration//GEN-END:variables
}
