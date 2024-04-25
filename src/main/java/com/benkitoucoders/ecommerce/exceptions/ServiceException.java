package com.benkitoucoders.ecommerce.exceptions;

import org.springframework.http.HttpStatus;

public class ServiceException extends ApiBasedException {

    public ServiceException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatusCode() {
        return HttpStatus.CONFLICT;
    }
}
