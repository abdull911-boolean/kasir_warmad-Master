package reports;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import kasir_warmad.sistem.Koneksi;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

public class Laporan {

    public void cetakLaporanPenjualan(int bulan, int tahun) {
        try {
            Connection conn = Koneksi.getKoneksi();
            String reportPath = "src/reports/LaporanPenjualan.jasper";
            JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(reportPath);
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("bulan", bulan);
            parameters.put("tahun", tahun);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
            JasperViewer viewer = new JasperViewer(jasperPrint, false);
            viewer.setTitle("Laporan Penjualan Bulanan");
            viewer.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error saat membuat report: " + e.getMessage(), "Report Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
