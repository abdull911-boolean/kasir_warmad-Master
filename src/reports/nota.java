/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package reports;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import kasir_warmad.sistem.Koneksi;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
/**
 *
 * @author Ariel
 */
public class nota {

    public void cetakStruk(int idTransaksi) {
        try {
            Connection conn = Koneksi.getKoneksi();
            // Path ke file .jasper (hasil compile dari .jrxml)
            String reportPath = "src/reports/struk_penjualan.jasper";
            
            // Load report dari file .jasper
            JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(reportPath);
            
            // Parameter untuk report
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("ID_TRANSAKSI", idTransaksi);
            
            // Fill report dengan data
            JasperPrint jasperPrint = JasperFillManager.fillReport(
                jasperReport, 
                parameters, 
                conn
            );
            
            // Tampilkan preview report
            JasperViewer viewer = new JasperViewer(jasperPrint, false);
            viewer.setVisible(true);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "Error saat membuat report: " + e.getMessage(), 
                "Report Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public void printStrukLangsung(int idTransaksi) {
        try {
            Connection conn = Koneksi.getKoneksi();
            
            String reportPath = "src/reports/struk_penjualan.jasper";
            JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(reportPath);
            
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("ID_TRANSAKSI", idTransaksi);
            
            JasperPrint jasperPrint = JasperFillManager.fillReport(
                jasperReport, 
                parameters, 
                conn
            );
            
            // Print langsung ke printer default
            JasperPrintManager.printReport(jasperPrint, false);
            
            JOptionPane.showMessageDialog(null, 
                "Struk berhasil dicetak!", 
                "Print Success", 
                JOptionPane.INFORMATION_MESSAGE);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "Error saat print: " + e.getMessage(), 
                "Print Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
