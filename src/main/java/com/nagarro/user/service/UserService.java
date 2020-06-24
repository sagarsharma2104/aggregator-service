package com.nagarro.user.service;

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

import com.nagarro.common.entity.User;

import io.opentracing.Span;
import io.opentracing.Tracer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService {

	@Value("${user.service.url}")
	private String userServiceUrl;

	@Value("${user.service.uri}")
	private String userServiceUri;

	@Autowired
	private Tracer tracer;

	@Autowired
	RestTemplate restTemplate;

	@Async("threadPoolTaskExecutor")
	public CompletableFuture <User> getUserByUserId(String userId, Span rootSpan) {
		User response = null;
		Span span = tracer.buildSpan("User Service --> getUserByUserId").asChildOf(rootSpan)
				.start();
		UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
				.fromUriString(userServiceUrl + "/users/" + userId);
		
		ResponseEntity<User> responseEntity = restTemplate.exchange(uriComponentsBuilder.toUriString(), HttpMethod.GET,
				new HttpEntity<>(new HttpHeaders()), new ParameterizedTypeReference<User>() {
				});

		if (responseEntity.getStatusCode() == HttpStatus.OK) {
			span.setTag("http.status_code", 200);
			log.info("getUserByUserId Request successfully completed from user-service.");
			response = responseEntity.getBody();
			log.info(response.toString());
		} else {
			span.setTag("http.status_code", 500);
			log.error("There was a problem while fetching getUserByUserId response from user-service.");
		}
		span.finish();
		return CompletableFuture.completedFuture(response);
	}

}
