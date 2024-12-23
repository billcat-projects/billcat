package net.billcat.auth.crud.model;

import lombok.Data;

@Data
public class UserDto {
	private String avatar;
	private String firstName;
	private String lastName;
	private String username;
	private String email;
	private String password;
	private String birthDate;
	private String phoneNumber;
}
