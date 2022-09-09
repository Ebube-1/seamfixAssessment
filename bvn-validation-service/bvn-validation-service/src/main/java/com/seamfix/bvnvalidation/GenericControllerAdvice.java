package com.seamfix.bvnvalidation;

import com.seamfix.bvnvalidation.constants.ResponseCode;
import com.seamfix.bvnvalidation.models.responses.BvnValidationResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GenericControllerAdvice {

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BvnValidationResponse handleConstraintViolationErrors(BindException ex) {
        log.error(ex.getMessage(), ex);
        BvnValidationResponse response = new BvnValidationResponse();

        FieldError fieldError = ex.getFieldErrors().get(0);
        response.setMessage(fieldError.getDefaultMessage());
        response.setCode(ResponseCode.BAD_REQUEST);
        return response;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public BvnValidationResponse handleGeneralExceptions(Exception ex) {
        String message = "There was an error processing the request, Please try again later...";
        log.error(message, ex);
        BvnValidationResponse response = new BvnValidationResponse();
        response.setMessage(message);
        response.setCode(ResponseCode.INTERNAL_SERVER_ERROR);
        return response;
    }
}
