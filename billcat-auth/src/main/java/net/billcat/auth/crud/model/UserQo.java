package net.billcat.auth.crud.model;

import lombok.Data;

@Data
public class UserQo {
	private String username;
	private String email;
	private String phoneNumber;
}
