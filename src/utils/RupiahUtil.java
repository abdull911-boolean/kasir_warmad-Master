/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

/**
 *
 * @author Ariel
 */
public class RupiahUtil {

    // Format BigDecimal menjadi String "RpX.XXX,00"
    public static String format(BigDecimal value) {
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        format.setMaximumFractionDigits(0);
        return format.format(value);
    }

    // Parsing String "RpX.XXX,00" jadi BigDecimal
    public static BigDecimal parse(String text) {
        if (text == null || text.isEmpty()) {
            return BigDecimal.ZERO;
        }
        try {
            // Ambil hanya digit angka (hapus Rp, titik, koma)
            String clean = text.replaceAll("[^\\d]", "");
            return new BigDecimal(clean);
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }

    // Format angka (misalnya 2000) ke Rp2.000,00
    public static String format(int value) {
        return format(BigDecimal.valueOf(value));
    }
}
