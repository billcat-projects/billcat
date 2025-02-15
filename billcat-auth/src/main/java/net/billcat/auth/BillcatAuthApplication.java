package net.billcat.auth;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@MapperScan(basePackages = "net.billcat.auth.crud.mapper")
public class BillcatAuthApplication {

	// If you start the application now,
	// and visit .well-known/openid-configuration you should see response like this:
	// You can read more on what is this endpoint and why it's needed here
	// https://ldapwiki.com/wiki/Openid-configuration#:~:text=Openid%2Dconfiguration%20is%20a%20Well,the%20Identity%20Provider%20(IDP).

	public static void main(String[] args) {

//		SpringApplication.run(BillcatAuthApplication.class);
		System.out.println(new SnowflakeGenerator().next());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
