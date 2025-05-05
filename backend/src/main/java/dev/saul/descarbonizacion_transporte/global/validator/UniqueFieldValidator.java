package dev.saul.descarbonizacion_transporte.global.validator;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Clase validadora para la anotación personalizada {@link UniqueField}.
 * 
 * Esta clase utiliza `MongoTemplate` para interactuar con MongoDB y verificar
 * si un valor ya existe en un campo específico de una colección.
 */
public class UniqueFieldValidator implements ConstraintValidator<UniqueField, String> {

    @Autowired
    private MongoTemplate mongoTemplate;

    private String collectionName;
    private String fieldName;

    /**
     * Inicializa los parámetros de la anotación {@link UniqueField}.
     * 
     * @param constraintAnnotation Instancia de la anotación con sus parámetros.
     */
    @Override
    public void initialize(UniqueField constraintAnnotation) {
        this.collectionName = constraintAnnotation.collection();
        this.fieldName = constraintAnnotation.fieldName();
    }

    /**
     * Valida si el valor proporcionado es único en el campo y colección
     * especificados.
     * 
     * @param value   El valor a validar.
     * @param context El contexto de validación.
     * @return {@code true} si el valor es único o está vacío; {@code false} si ya
     *         existe.
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true; // La validación @NotBlank se encargará de valores nulos o vacíos.
        }

        // Obtiene la base de datos y la colección desde MongoTemplate
        MongoDatabase database = mongoTemplate.getDb();
        MongoCollection<Document> collection = database.getCollection(collectionName);

        // Consulta para verificar si el valor ya existe
        long count = collection.countDocuments(new Document(fieldName, value).append("isActive", true));

        // Retorna true si el valor no existe en la colección, false de lo contrario
        return count == 0;
    }
}