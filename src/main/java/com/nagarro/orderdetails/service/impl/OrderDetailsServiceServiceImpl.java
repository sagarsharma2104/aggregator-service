package com.nagarro.orderdetails.service.impl;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nagarro.common.entity.Order;
import com.nagarro.common.entity.OrderDetails;
import com.nagarro.common.entity.User;
import com.nagarro.order.service.OrderService;
import com.nagarro.orderdetails.service.OrderDetailsService;
import com.nagarro.user.service.UserService;

import io.opentracing.Span;
import io.opentracing.Tracer;

@Service
public class OrderDetailsServiceServiceImpl implements OrderDetailsService {

	private OrderService orderService;
	private UserService userService;
	private Tracer tracer;

	@Autowired
	OrderDetailsServiceServiceImpl(OrderService orderService, UserService userService, Tracer tracer) {
		this.orderService = orderService;
		this.userService = userService;
		this.tracer = tracer;
	}

	@Override
	public OrderDetails getOrderDetailsByUserId(String userId, Span rootSpan) throws Exception {
		Span span = tracer.buildSpan("Order Details Service --> getOrderDetailsByUserId").asChildOf(rootSpan)
				.start();
	    CompletableFuture < User > user = userService.getUserByUserId(userId, span);
	    CompletableFuture<List<Order>> orders = orderService.getOrdersByUserId(userId, span);

        // Wait until they are all done
        CompletableFuture.allOf(user, orders).join();
        
		OrderDetails orderDetails = new OrderDetails();
		orderDetails.setUser(user.get());
		orderDetails.setOrders(orders.get());

		span.setTag("http.status_code", 200);
		span.finish();
		return orderDetails;
	}

}
