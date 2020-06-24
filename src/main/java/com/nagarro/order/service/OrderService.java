package com.nagarro.order.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.nagarro.common.entity.Order;

import io.opentracing.Span;
import io.opentracing.Tracer;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderService {

	@Value("${order.service.url}")
	private String orderServiceUrl;

	@Autowired
	Tracer tracer;

	@Autowired
	RestTemplate restTemplate;

	@Async("threadPoolTaskExecutor")
	public CompletableFuture<List<Order>> getOrdersByUserId(String userId, Span rootSpan) {
		List<Order> response = null;
		UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
				.fromUriString(orderServiceUrl + "/orders/" + userId);
		Span span = tracer.buildSpan("Order Service --> getOrdersByUserId").asChildOf(rootSpan).start();

		ResponseEntity<List<Order>> responseEntity = restTemplate.exchange(uriComponentsBuilder.toUriString(),
				HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), new ParameterizedTypeReference<List<Order>>() {
				});
		if (responseEntity.getStatusCode() == HttpStatus.OK) {
			span.setTag("http.status_code", 200);
			log.info("Request to order-service is successful.");
			response = responseEntity.getBody();
		} else {
			span.setTag("http.status_code", 500);
			log.error("There was a problem with requesting response from order-service.");
		}
		span.finish();
		return CompletableFuture.completedFuture(response);
	}
}
