package utils;

import javax.swing.text.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class RupiahDocumentFilter extends DocumentFilter {

    private final NumberFormat format;

    public RupiahDocumentFilter() {
        format = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        format.setMaximumFractionDigits(0);  // Hapus ,00
        format.setMinimumFractionDigits(0);  // Pastikan benar-benar tidak muncul
    }

    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
        if (string == null) {
            return;
        }
        replace(fb, offset, 0, string, attr);
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String string, AttributeSet attrs) throws BadLocationException {
        Document doc = fb.getDocument();
        String oldText = doc.getText(0, doc.getLength());
        String newText = new StringBuilder(oldText).replace(offset, offset + length, string).toString();

        String digitsOnly = newText.replaceAll("[^\\d]", "");

        if (digitsOnly.isEmpty()) {
            fb.replace(0, doc.getLength(), "", attrs);
            return;
        }

        try {
            BigDecimal value = new BigDecimal(digitsOnly);
            String formatted = format.format(value);
            fb.replace(0, doc.getLength(), formatted, attrs);
        } catch (NumberFormatException e) {
            // Ignore invalid input
        }
    }

    @Override
    public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
        replace(fb, offset, length, "", null);
    }
}
