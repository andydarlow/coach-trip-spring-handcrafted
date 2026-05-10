/**
 * convert exceptions thrown in web call into Error responses that hide any
 * hint of the code where the error happend. Also dumps them in a good format
 */
package org.darlow.coachtrip.controllers;

import org.darlow.coachtrip.services.CoachTripScheduleExistsException;
import org.springframework.boot.json.JsonParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tools.jackson.core.exc.StreamReadException;
import tools.jackson.core.exc.UnexpectedEndOfInputException;
import tools.jackson.databind.exc.InvalidFormatException;

import java.time.format.DateTimeParseException;
import java.util.stream.Collectors;

@RestControllerAdvice
public class DTOErrorHandler {


    /**
     * process Validation based exceptions thrown when calling with bad inputs
     * @param ex the exception thrown by the validation process
     * @return error in a format suitable for API response.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorMessage> handleInvalidFormat(HttpMessageNotReadableException ex) {
        Throwable cause = ex.getMostSpecificCause();
        String errorMessage = switch (cause) {
            case InvalidFormatException e ->
                    String.format("Field '%s' cannot accept value '%s'",
                            e.getPath().getFirst().getPropertyName(), e.getValue());
            case DateTimeParseException e -> "Invalid date/time format: " + e.getParsedString()  + ". expected hh:mm";
            // need this one to stop the location of the code parsing the message appaearing. Order impartant.
            case UnexpectedEndOfInputException e -> "Invalid Message format: JSON parse error: Unexpected end-of-input: expected close marker";
            case HttpMessageNotReadableException e -> "Invalid Message format: message not readable";
            case StreamReadException e -> "Invalid Message format: " + e.getOriginalMessage();
            default -> "Readable error: " + cause.getMessage();
        };
        return new ErrorMessage(errorMessage,HttpStatus.BAD_REQUEST ).toResponseEntity();
    }

    @ExceptionHandler(CoachTripScheduleExistsException.class)
    public ResponseEntity<ErrorMessage> handleCoachTripScheduleExistsException(CoachTripScheduleExistsException ex) {
        return new ErrorMessage(ex.getMessage(),HttpStatus.CONFLICT ).toResponseEntity();
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> handleValidationExceptions(MethodArgumentNotValidException ex) {

        // Extract all field errors into your custom format
        String errorMessages = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ":" +
                                        error.getDefaultMessage())
                .sorted()
                .collect(Collectors.joining(","));
        return new ErrorMessage(errorMessages,HttpStatus.BAD_REQUEST ).toResponseEntity();
    }

}
