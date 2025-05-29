package utils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;

/**
 * Kelas util untuk membatasi input JTextField agar hanya angka dan menambahkan
 * listener otomatis.
 */
public class filterAngka {

    // Filter: hanya angka
    public static class AngkaFilter extends DocumentFilter {

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                throws BadLocationException {
            if (string != null && (string.isEmpty() || string.matches("\\d+"))) {
                super.insertString(fb, offset, string, attr);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                throws BadLocationException {
            if (text != null && (text.isEmpty() || text.matches("\\d+"))) {
                super.replace(fb, offset, length, text, attrs);
            }
        }
    }

    /**
     * Terapkan filter angka ke JTextField
     */
    public static void hanyaAngka(JTextField field) {
        ((AbstractDocument) field.getDocument()).setDocumentFilter(new AngkaFilter());
    }

    /**
     * Tambahkan DocumentListener yang menjalankan fungsi saat isi field berubah
     */
    public static void tambahListenerHitung(JTextField field, Runnable fungsiHitung) {
        field.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                fungsiHitung.run();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                fungsiHitung.run();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                fungsiHitung.run();
            }
        });
    }
}
