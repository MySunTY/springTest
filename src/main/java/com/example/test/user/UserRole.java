package com.example.test.user;

import lombok.Getter;

@Getter // 값을 변경할필요없어서 게터만 가져오기
public enum UserRole {
	ADMIN("ROLE_ADMIN"),
	USER("ROLE_USER");
	
	UserRole(String value){
		this.value = value;
	}
	private String value;
}
