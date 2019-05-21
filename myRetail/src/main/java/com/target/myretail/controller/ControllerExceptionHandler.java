package com.target.myretail.controller;

import com.target.myretail.exception.ErrorMessage;
import com.target.myretail.exception.ProductNotFoundException;
import com.target.myretail.exception.UpdateIntegrityException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ControllerExceptionHandler {

    /**
     * Used to check if a product is found in a database
     * @param e the exception to process
     * @return the error message from the exception
     */
    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorMessage proccessProductNotFoundException(ProductNotFoundException e) {
        return new ErrorMessage(e.getMessage());
    }

    /**
     * Used to check that updates ID's match
     * @param e the exception to deal with
     * @return the error message from the exception
     */
    @ExceptionHandler(UpdateIntegrityException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ResponseBody
    public ErrorMessage processUpdateIntegrityException(UpdateIntegrityException e) {
        return new ErrorMessage(e.getMessage());
    }
}
