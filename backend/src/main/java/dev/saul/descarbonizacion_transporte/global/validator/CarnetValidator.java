package dev.saul.descarbonizacion_transporte.global.validator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validador personalizado para verificar la validez de un carnet en formato
 * específico.
 * Implementa la interfaz {@link ConstraintValidator} para definir una
 * validación personalizada.
 * 
 * Un carnet válido debe cumplir con las siguientes reglas:
 * <ul>
 * <li>Debe tener exactamente 11 caracteres.</li>
 * <li>Los primeros 6 caracteres representan una fecha de nacimiento en formato
 * "yyMMdd".</li>
 * <li>La fecha debe ser válida y corresponder a un rango realista
 * (1900-2099).</li>
 * <li>Los últimos 5 caracteres deben ser dígitos numéricos.</li>
 * </ul>
 * 
 * Este validador puede ser utilizado en conjunto con la anotación
 * {@link ValidCarnet}.
 */
public class CarnetValidator implements ConstraintValidator<ValidCarnet, String> {

    /**
     * Valida un valor de carnet según las reglas definidas.
     * 
     * @param value   El valor del carnet que se va a validar.
     * @param context Contexto del validador que puede ser usado para personalizar
     *                el mensaje de error.
     * @return {@code true} si el carnet es válido o está vacío/nulo; {@code false}
     *         si no es válido.
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            // Permitir nulos o valores en blanco; se espera que otra validación (como
            // @NotBlank) lo controle.
            return true;
        }

        // Debe tener exactamente 11 dígitos numéricos
        if (!value.matches("\\d{11}")) {
            return false;
        }

        // Extraer y validar la fecha de nacimiento.
        String year = value.substring(0, 2);
        String month = value.substring(2, 4);
        String day = value.substring(4, 6);
        char siglo = value.charAt(6);

        int yearInt = Integer.parseInt(year);
        int monthInt = Integer.parseInt(month);
        int dayInt = Integer.parseInt(day);

        // Construir la fecha completa basada en el año proporcionado.
        int fullYear;
        if (siglo == '9') {
            fullYear = 1800 + yearInt;
        } else if ("012345".indexOf(siglo) >= 0) {
            fullYear = 1900 + yearInt;
        } else if ("678".indexOf(siglo) >= 0) {
            fullYear = 2000 + yearInt;
        } else {
            return false; // Siglo inválido
        }

        // Verificar que la fecha es válida y real.
        LocalDate fechaNacimiento;
        try {
            fechaNacimiento = LocalDate.of(fullYear, monthInt, dayInt);
        } catch (Exception e) {
            return false;
        }
        if (fechaNacimiento.isAfter(LocalDate.now())) {
            return false;
        }

        return true;
    }
}