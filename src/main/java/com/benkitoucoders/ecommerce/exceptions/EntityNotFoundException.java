package com.benkitoucoders.ecommerce.exceptions;

import com.benkitoucoders.ecommerce.handlers.ApiBasedException;
import org.springframework.http.HttpStatus;

public class EntityNotFoundException extends ApiBasedException {
    public EntityNotFoundException(String message) {
        super(message);
    }
    @Override
    public HttpStatus getStatusCode() {
        return HttpStatus.NOT_FOUND;
    }
}
