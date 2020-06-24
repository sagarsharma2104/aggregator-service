package com.nagarro.orderdetails.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nagarro.common.entity.OrderDetails;
import com.nagarro.orderdetails.service.OrderDetailsService;

import io.opentracing.Span;
import io.opentracing.Tracer;

@RestController
@RequestMapping(value = "orderdetails")
public class OrderDetailsController {

	private OrderDetailsService orderDetailsService;
	private Tracer tracer;

	@Autowired
	public OrderDetailsController(OrderDetailsService orderDetailsService, Tracer tracer) {
		this.orderDetailsService = orderDetailsService;
		this.tracer = tracer;
	}

	@GetMapping(value = "/{userId}")
	public OrderDetails getOrderDetailsByUserId(@PathVariable(value = "userId") String userId) {
		OrderDetails orderDetails = null;
		Span span = tracer.buildSpan("Order Details Controller --> getOrderDetailsByUserId").start();
		try {
			orderDetails = orderDetailsService.getOrderDetailsByUserId(userId, span);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		span.finish();
		return orderDetails;
	}
}
