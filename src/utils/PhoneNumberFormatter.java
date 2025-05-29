/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author Ariel
 */
public class PhoneNumberFormatter {

    private final String awalanNoTelepon = "+62";
    private JTextField textField;
    private boolean isInitialFormat = true; // Flag untuk menangani input pertama

    public PhoneNumberFormatter(JTextField textField) {
        this.textField = textField;
        setupDocumentListener();
        textField.setText(""); // Mulai dengan field kosong
    }

    private void setupDocumentListener() {
        textField.getDocument().addDocumentListener(new DocumentListener() {
            private void formatInput() {
                SwingUtilities.invokeLater(() -> {
                    String text = textField.getText().trim();

                    // Jika kosong, biarkan kosong
                    if (text.isEmpty()) {
                        isInitialFormat = true; // Reset flag saat kosong
                        return;
                    }

                    String originalText = text; // Simpan teks asli untuk perbandingan
                    int caretPos = textField.getCaretPosition();

                    // Hanya proses jika ini input awal atau ada perubahan
                    if (isInitialFormat) {
                        // Hapus semua karakter non-digit
                        text = text.replaceAll("[^\\d]", "");

                        // Tambahkan awalan +62 hanya untuk input pertama
                        if (!text.isEmpty() && !text.startsWith(awalanNoTelepon)) {
                            text = awalanNoTelepon + text;
                        }

                        // Tambahkan spasi setelah +62
                        if (text.startsWith(awalanNoTelepon) && !text.contains(" ")) {
                            text = awalanNoTelepon + " " + text.substring(awalanNoTelepon.length());
                        }

                        isInitialFormat = false; // Setelah format awal, nonaktifkan flag
                    } else {
                        // Untuk input berikutnya, ambil angka setelah +62
                        if (text.startsWith(awalanNoTelepon + " ")) {
                            String numberPart = text.substring(awalanNoTelepon.length() + 1).replaceAll("[^\\d]", "");
                            text = awalanNoTelepon + " " + numberPart;
                        }
                    }

                    // Update hanya jika berbeda
                    if (!text.equals(textField.getText())) {
                        textField.setText(text);
                        // Posisikan kursor di akhir teks
                        caretPos = text.length();
                        textField.setCaretPosition(caretPos);
                    } else if (caretPos < text.length()) {
                        // Pastikan kursor tetap di akhir jika pengguna mengetik lebih lanjut
                        textField.setCaretPosition(text.length());
                    }
                });
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                formatInput();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                formatInput();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                formatInput();
            }
        });
    }
}
