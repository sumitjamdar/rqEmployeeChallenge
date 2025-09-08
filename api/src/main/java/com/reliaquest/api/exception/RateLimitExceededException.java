package com.reliaquest.api.exception;

import org.springframework.web.client.HttpClientErrorException;

public class RateLimitExceededException extends RuntimeException{
    public RateLimitExceededException(String s, HttpClientErrorException.TooManyRequests ex) {
        super(s, ex);
    }
}
