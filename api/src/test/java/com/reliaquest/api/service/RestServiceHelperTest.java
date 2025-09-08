package com.reliaquest.api.service;

import com.reliaquest.api.exception.RateLimitExceededException;
import com.reliaquest.api.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class RestServiceHelperTest {

    private RestTemplate restTemplate;
    private RestServiceHelper helper;

    @BeforeEach
    void setUp() {
        restTemplate = Mockito.mock(RestTemplate.class);
        RestTemplateBuilder builder = Mockito.mock(RestTemplateBuilder.class);
        when(builder.build()).thenReturn(restTemplate);
        helper = new RestServiceHelper(builder);
    }

    @Test
    void testSuccessWithBody() {
        when(restTemplate.exchange(anyString(), any(), any(HttpEntity.class), eq(String.class)))
                .thenReturn(new ResponseEntity<>("ok", HttpStatus.OK));

        String result = helper.execute("http://test", HttpMethod.GET, null, String.class);
        assertEquals("ok", result);
    }

    @Test
    void testSuccessWithNullBody() {
        when(restTemplate.exchange(anyString(), any(), any(HttpEntity.class), eq(String.class)))
                .thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> helper.execute("http://test", HttpMethod.GET, null, String.class));
    }

    @Test
    void testNon2xxStatus() {
        when(restTemplate.exchange(anyString(), any(), any(HttpEntity.class), eq(String.class)))
                .thenReturn(new ResponseEntity<>("fail", HttpStatus.BAD_REQUEST));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> helper.execute("http://test", HttpMethod.GET, null, String.class));
    }

    @Test
    void testNotFound() {
        when(restTemplate.exchange(anyString(), any(), any(HttpEntity.class), eq(String.class)))
                .thenThrow(HttpClientErrorException.NotFound.create(HttpStatus.NOT_FOUND, "Not Found", null, null, null));

        assertThrows(ResourceNotFoundException.class,
                () -> helper.execute("http://test", HttpMethod.GET, null, String.class));
    }

    @Test
    void testTooManyRequests() {
        when(restTemplate.exchange(anyString(), any(), any(HttpEntity.class), eq(String.class)))
                .thenThrow(HttpClientErrorException.TooManyRequests.create(HttpStatus.TOO_MANY_REQUESTS, "Too Many", null, null, null));

        assertThrows(RateLimitExceededException.class,
                () -> helper.execute("http://test", HttpMethod.GET, null, String.class));
    }

    @Test
    void testServerError() {
        when(restTemplate.exchange(anyString(), any(), any(HttpEntity.class), eq(String.class)))
                .thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Error"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> helper.execute("http://test", HttpMethod.GET, null, String.class));
        assertTrue(ex.getMessage().contains("Server error"));
    }

    @Test
    void testGenericException() {
        when(restTemplate.exchange(anyString(), any(), any(HttpEntity.class), eq(String.class)))
                .thenThrow(new RuntimeException("boom"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> helper.execute("http://test", HttpMethod.GET, null, String.class));
        assertTrue(ex.getMessage().contains("Error calling API"));
    }
}
