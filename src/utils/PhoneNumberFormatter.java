/*
 * Click nfs://SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nfs://SystemFileSystem/Templates/Classes/Class.java to edit this template
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

    private final String phonePrefix = "62"; // Prefix for phone number
    private JTextField textField;
    private boolean isInitialFormat = true; // Flag to handle initial input

    public PhoneNumberFormatter(JTextField textField) {
        this.textField = textField;
        setupDocumentListener();
        textField.setText(phonePrefix); // Start with "62"
    }

    // Method to validate phone number length (including 62)
    public boolean isValidPhoneNumber() {
        String text = textField.getText().trim();
        if (text.startsWith(phonePrefix)) {
            int totalLength = text.length(); // Total length including "62"
            return totalLength >= 9 && totalLength <= 13;
        }
        return false;
    }

    private void setupDocumentListener() {
        textField.getDocument().addDocumentListener(new DocumentListener() {
            private void formatInput() {
                SwingUtilities.invokeLater(() -> {
                    String text = textField.getText().trim();
                    int caretPos = textField.getCaretPosition();

                    // If text is empty or prefix is removed, reset to "62"
                    if (text.isEmpty() || !text.startsWith(phonePrefix)) {
                        text = phonePrefix;
                        isInitialFormat = true;
                    } else {
                        // Extract digits after prefix
                        String digits = text.substring(phonePrefix.length()).replaceAll("[^\\d]", "");
                        // Limit total length (including prefix) to 13
                        int maxDigits = 13 - phonePrefix.length();
                        if (digits.length() > maxDigits) {
                            digits = digits.substring(0, maxDigits);
                        }
                        text = phonePrefix + digits;
                        isInitialFormat = false;
                    }

                    // Update text field only if different
                    if (!text.equals(textField.getText())) {
                        textField.setText(text);
                        // Adjust caret position
                        if (caretPos < phonePrefix.length()) {
                            caretPos = phonePrefix.length(); // Prevent caret before prefix
                        } else if (caretPos > text.length()) {
                            caretPos = text.length();
                        }
                        textField.setCaretPosition(caretPos);
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