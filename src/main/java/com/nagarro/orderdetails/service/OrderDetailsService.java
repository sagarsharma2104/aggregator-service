package com.nagarro.orderdetails.service;

import com.nagarro.common.entity.OrderDetails;

import io.opentracing.Span;

public interface OrderDetailsService {

	OrderDetails getOrderDetailsByUserId(String userId, Span rootSpan) throws Exception;

}
