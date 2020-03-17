package utils;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

public class Validar {

    public Validar() {}

    public static boolean esEntero(String string) {
        if (StringUtils.isBlank(string)) return false;

        try {
            Integer.parseInt(string);
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }

    public static boolean esDecimal(String string) {
        if (StringUtils.isBlank(string)) return false;

        try {
            Float.parseFloat(string);
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }

    public static boolean esEmail(String email) {
        if (StringUtils.isBlank(email))  return false;

        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(email).matches();
    }

    public static boolean esCuentaBancaria(String cuentaBancaria) {
        if (StringUtils.isBlank(cuentaBancaria)) return false;

        String regex = "^ES\\d{22}$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(cuentaBancaria).matches();
    }

    public static boolean esNumeroFactura(String numeroFactura) {
        if (StringUtils.isBlank(numeroFactura)) return false;

        String regex = "^F\\d{5}$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(numeroFactura).matches();
    }

    public static boolean esCodigoPostal(String codigoPostal) {
        if (StringUtils.isBlank(codigoPostal)) return false;

        String regex = "^\\d{5}$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(codigoPostal).matches();
    }
}
