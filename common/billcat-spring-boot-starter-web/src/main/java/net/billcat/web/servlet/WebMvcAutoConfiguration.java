package net.billcat.web.servlet;

import lombok.RequiredArgsConstructor;
import net.billcat.web.pageable.DefaultPageParamArgumentResolver;
import net.billcat.web.pageable.PageParamArgumentResolver;
import net.billcat.web.pageable.PageableProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author Hccake 2019/10/19 17:10
 */
@AutoConfiguration
@RequiredArgsConstructor
@EnableConfigurationProperties({ WebProperties.class, PageableProperties.class })
public class WebMvcAutoConfiguration {

	private final WebProperties webProperties;

	@Bean
	@ConditionalOnMissingBean
	public PageParamArgumentResolver pageParamArgumentResolver(PageableProperties pageableProperties) {
		return new DefaultPageParamArgumentResolver(pageableProperties.getMaxPageSize(),
				pageableProperties.getPageParameterName(), pageableProperties.getSizeParameterName(),
				pageableProperties.getSortParameterName());
	}

	@RequiredArgsConstructor
	@Configuration(proxyBeanMethods = false)
	static class CustomWebMvcConfigurer implements WebMvcConfigurer {

		private final PageParamArgumentResolver pageParamArgumentResolver;

		/**
		 * Page Sql注入过滤
		 * @param argumentResolvers 方法参数解析器集合
		 */
		@Override
		public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
			argumentResolvers.add(this.pageParamArgumentResolver);
		}

	}

}