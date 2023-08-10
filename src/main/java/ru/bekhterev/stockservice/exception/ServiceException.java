package ru.bekhterev.stockservice.exception;

import org.springframework.http.HttpStatusCode;

public class ServiceException extends RuntimeException {

    private final HttpStatusCode httpStatus;

    public HttpStatusCode getHttpStatus() {
        return httpStatus;
    }

    public ServiceException(String message, HttpStatusCode httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
