package net.billcat.auth.crud.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("users")
public class User {
	@TableId
	private Long id;
	private String avatar;
	private String firstName;
	private String lastName;
	private String username;
	private String email;
	private String password;
	private String birthDate;
	private String phoneNumber;
	private String status;
	private String role;
	private LocalDateTime createdAt;
	private LocalDateTime deletedAt;
}
