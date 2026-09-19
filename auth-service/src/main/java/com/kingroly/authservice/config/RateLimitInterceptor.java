package com.kingroly.authservice.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Rate Limit Interceptor using Bucket4j.
 * 
 * DESIGN PATTERN: Intercepting Filter Pattern.
 * This class intercepts HTTP requests before they reach the controller. 
 * It implements a Token Bucket algorithm to restrict how many times a specific 
 * IP address can hit an endpoint within a given time frame (e.g., to prevent brute-force).
 */
@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    private Bucket resolveBucket(String ip) {
        return cache.computeIfAbsent(ip, this::newBucket);
    }

    /**
     * Creates a new "Bucket" of tokens for a new IP.
     * Rule: 5 tokens (requests) max, refilled at a rate of 5 tokens every 1 minute.
     */
    private Bucket newBucket(String ip) {
        // 5 requests per minute
        Bandwidth limit = Bandwidth.classic(5, Refill.intervally(5, Duration.ofMinutes(1)));
        return Bucket.builder().addLimit(limit).build();
    }

    /**
     * preHandle is executed before the Controller method.
     * We identify the user by their IP address. If they have tokens left in their bucket,
     * they consume 1 token and proceed (return true). If the bucket is empty, 
     * we block the request and return HTTP 429 Too Many Requests (return false).
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ip = request.getRemoteAddr();
        Bucket bucket = resolveBucket(ip);
        if (bucket.tryConsume(1)) {
            return true;
        } else {
            response.sendError(HttpStatus.TOO_MANY_REQUESTS.value(), "Too many requests (Rate Limit exceeded). Try again in 1 minute.");
            return false;
        }
    }
}
