package com.reliaquest.api.service;

import com.reliaquest.api.exception.RateLimitExceededException;
import com.reliaquest.api.exception.ResourceNotFoundException;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class RestServiceHelper {

    private final RestTemplate restTemplate;

    public RestServiceHelper(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    /**
     * Generic method to make a REST call
     *
     * @param url      API URL
     * @param method   HTTP Method (GET, POST, DELETE, etc.)
     * @param request  Request body (can be null for GET/DELETE without body)
     * @param clazz    Response class type
     * @param <T>      Response type
     * @return Response body of type T
     */
    public <T> T execute(String url, HttpMethod method, Object request, Class<T> clazz) {
        try {
            HttpEntity<Object> entity = new HttpEntity<>(request);
            ResponseEntity<T> response = restTemplate.exchange(url, method, entity, clazz);

            if (response.getStatusCode().is2xxSuccessful()) {
                if (response.getBody() != null) {
                    return response.getBody();
                } else {
                    throw new RuntimeException("Empty response body from: " + url);
                }
            } else {
                throw new RuntimeException("Unexpected status code: " + response.getStatusCode());
            }

        } catch (HttpClientErrorException.NotFound ex) {
            throw new ResourceNotFoundException("Resource not found: " + url, ex);
        } catch (HttpClientErrorException.TooManyRequests ex) {
            throw new RateLimitExceededException("Too many requests: " + url, ex);
        }  catch (HttpServerErrorException ex) {
            throw new RuntimeException("Server error " + ex.getStatusCode() + " from: " + url, ex);
        } catch (Exception ex) {
            throw new RuntimeException("Error calling API: " + url, ex);
        }
    }
}

