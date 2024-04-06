package net.billcat.mybatis.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.dialects.IDialect;
import com.baomidou.mybatisplus.extension.plugins.pagination.dialects.MySqlDialect;
import com.baomidou.mybatisplus.extension.plugins.pagination.dialects.PostgreDialect;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class MyBatisPlusAutoConfiguration {

	@Bean
	@ConditionalOnClass(name = "org.postgresql.Driver")
	public PostgreDialect postgreDialect() {
		return new PostgreDialect();
	}

	@Bean
	@ConditionalOnClass(name = "com.mysql.cj.jdbc.Driver")
	public MySqlDialect mysqlDialect() {
		return new MySqlDialect();
	}

	@Bean
	@ConditionalOnMissingBean(MybatisPlusInterceptor.class)
	public MybatisPlusInterceptor mybatisPlusInterceptor(IDialect dialect) {
		MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
		// 添加分页插件
		interceptor.addInnerInterceptor(new PaginationInnerInterceptor(dialect));

		return interceptor;
	}

}
