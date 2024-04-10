package com.benkitoucoders.ecommerce.exceptions;

import org.springframework.http.HttpStatus;

public class NoStockExistException extends ApiBasedException {
    public NoStockExistException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatusCode() {
        return HttpStatus.NOT_FOUND;
    }

}
