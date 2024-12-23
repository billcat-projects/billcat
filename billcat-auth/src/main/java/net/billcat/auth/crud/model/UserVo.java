package net.billcat.auth.crud.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserVo {

	private Long id;

	private String avatar;

	private String firstName;

	private String lastName;

	private String username;

	private String email;

	private String birthDate;

	private String phoneNumber;

	private String status;

	private String role;

	private LocalDateTime createdAt;

	private LocalDateTime deletedAt;

}
