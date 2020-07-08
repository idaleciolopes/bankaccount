package org.ilopes.bankaccount.rest;

import org.ilopes.bankaccount.personalaccount.ForbiddenOperationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class BankApiAdvice {
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {
        // Gets a clean structure to describe error on real life scenario
        return ex.getMessage();
    }

    @ExceptionHandler(ForbiddenOperationException.class)
    @ResponseStatus(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS)
    public String handleConstraintViolationException(ForbiddenOperationException ex, WebRequest request) {
        return ex.getMessage();
    }
}
