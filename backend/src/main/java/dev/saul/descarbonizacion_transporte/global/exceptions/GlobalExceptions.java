package dev.saul.descarbonizacion_transporte.global.exceptions;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import dev.saul.descarbonizacion_transporte.global.dto.MessageDto;
import dev.saul.descarbonizacion_transporte.global.utils.Operations;

/**
 * Clase encargada del manejo global de excepciones dentro de la aplicación.
 * 
 * Esta clase captura diferentes tipos de excepciones que pueden ocurrir durante
 * la ejecución
 * de la API y responde con un mensaje estructurado de acuerdo al tipo de error.
 * 
 * Las excepciones son manejadas de forma centralizada utilizando las
 * anotaciones
 * `@ExceptionHandler` y `@RestControllerAdvice`, permitiendo que se gestionen
 * excepciones en todo el proyecto sin la necesidad de duplicar código.
 * 
 * @see ResourceNotFoundException
 * @see AttributeException
 * @see MethodArgumentNotValidException
 */
@RestControllerAdvice
public class GlobalExceptions {

    /**
     * Maneja las excepciones del tipo {@link ResourceNotFoundException}.
     * 
     * @param e La excepción {@link ResourceNotFoundException} que indica que no se
     *          encontró el recurso solicitado.
     * @return Una respuesta HTTP con un código de estado 404 (NOT FOUND) y un
     *         mensaje detallado del error.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<MessageDto> throwNotFoundException(ResourceNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new MessageDto(HttpStatus.NOT_FOUND, e.getMessage()));
    }

    /**
     * Maneja las excepciones del tipo {@link AttributeException}.
     * 
     * @param e La excepción {@link AttributeException} que indica un error
     *          relacionado con un atributo no válido.
     * @return Una respuesta HTTP con un código de estado 400 (BAD REQUEST) y el
     *         mensaje del error.
     */
    @ExceptionHandler(AttributeException.class)
    public ResponseEntity<MessageDto> throwAttributeException(AttributeException e) {
        return ResponseEntity.internalServerError()
                .body(new MessageDto(HttpStatus.BAD_REQUEST, e.getMessage()));
    }

    /**
     * Maneja todas las demás excepciones no especificadas anteriormente.
     * 
     * @param e La excepción genérica {@link Exception} que captura errores no
     *          controlados específicamente.
     * @return Una respuesta HTTP con un código de estado 500 (INTERNAL SERVER
     *         ERROR) y el mensaje del error.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<MessageDto> generalException(Exception e) {
        return ResponseEntity.badRequest()
                .body(new MessageDto(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
    }

    /**
     * Maneja las excepciones de validación, como
     * {@link MethodArgumentNotValidException},
     * que ocurren cuando los datos enviados en la solicitud no cumplen con las
     * restricciones de validación.
     * 
     * @param e La excepción {@link MethodArgumentNotValidException} que contiene
     *          los errores de validación.
     * @return Una respuesta HTTP con un código de estado 400 (BAD REQUEST) y una
     *         lista de mensajes de error detallados.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MessageDto> validationException(MethodArgumentNotValidException e) {
        List<String> messages = new ArrayList<>();
        e.getBindingResult().getAllErrors().forEach((err) -> {
            messages.add(err.getDefaultMessage());
        });

        return ResponseEntity.badRequest()
                .body(new MessageDto(HttpStatus.BAD_REQUEST, Operations.trimBrackets(messages.toString())));
    }

}