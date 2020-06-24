package com.nagarro.common.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

	private Long id;

	private String name;

	private String email;

	private int pincode;

	@Override
	public String toString() {
		return this.name + " ---" + this.email + "---" + this.pincode;
	}
}