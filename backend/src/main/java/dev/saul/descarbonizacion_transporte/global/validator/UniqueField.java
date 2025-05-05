package dev.saul.descarbonizacion_transporte.global.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * Anotación personalizada para validar que un valor sea único dentro de una
 * colección de MongoDB.
 * 
 * Esta anotación puede aplicarse a campos o métodos y utiliza el validador
 * {@link UniqueFieldValidator} para verificar si un valor ya existe en una
 * colección específica
 * y un campo dado en la base de datos.
 */
@Constraint(validatedBy = UniqueFieldValidator.class)
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueField {

    /**
     * Mensaje de error que se mostrará si el valor no es único.
     * 
     * @return El mensaje de error predeterminado.
     */
    String message() default "El valor ya está registrado";

    /**
     * Grupos de validación que pueden utilizarse para categorizar esta restricción.
     * 
     * @return Los grupos de validación.
     */
    Class<?>[] groups() default {};

    /**
     * Información adicional sobre la carga útil para clientes avanzados.
     * 
     * @return Las clases de carga útil.
     */
    Class<? extends Payload>[] payload() default {};

    /**
     * Nombre de la colección en la base de datos MongoDB donde se realizará la
     * validación.
     * 
     * @return El nombre de la colección.
     */
    String collection();

    /**
     * Nombre del campo en la colección de MongoDB que será verificado como único.
     * 
     * @return El nombre del campo.
     */
    String fieldName();
}
