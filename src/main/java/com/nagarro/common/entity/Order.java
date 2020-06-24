package com.nagarro.common.entity;

import com.nagarro.common.entity.Order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {

	private long id;
	private float amount;
	private String date;
}
