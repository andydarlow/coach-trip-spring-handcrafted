package org.darlow.coachtrip.controllers;
/**
 * standardise error message format using this.
 */

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public record ErrorMessage(String message, HttpStatus httpStatus) {

    public ResponseEntity<ErrorMessage> toResponseEntity() {
        return ResponseEntity.status(httpStatus).body(this);
    }
}
