package dev.saul.descarbonizacion_transporte.global.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * Anotación personalizada para validar el formato de un carnet de identidad.
 * 
 * Esta anotación utiliza la clase {@link CarnetValidator} para realizar la
 * validación.
 * Se asegura de que el carnet tenga una longitud correcta y un formato válido,
 * incluyendo una fecha válida en sus primeros dígitos.
 * 
 * <p>
 * Ejemplo de uso:
 * </p>
 * 
 * <pre>
 * {@code
 * @ValidCarnet(message = "El carnet no cumple con el formato requerido")
 * private String carnet;
 * }
 * </pre>
 * 
 * @see CarnetValidator
 */
@Constraint(validatedBy = CarnetValidator.class)
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCarnet {

    /**
     * Mensaje que será mostrado si la validación falla.
     * 
     * @return el mensaje de error.
     */
    String message() default "El formato del carnet de identidad es inválido";

    /**
     * Grupos de validación asociados con esta restricción.
     * 
     * @return los grupos de validación.
     */
    Class<?>[] groups() default {};

    /**
     * Información adicional sobre la carga útil de la restricción.
     * 
     * @return las clases de carga útil.
     */
    Class<? extends Payload>[] payload() default {};
}