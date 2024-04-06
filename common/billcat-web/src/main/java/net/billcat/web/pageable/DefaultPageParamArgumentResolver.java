package net.billcat.web.pageable;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import net.billcat.common.model.domain.PageParam;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @author lengleng
 * @author hccake
 * @date 2019-06-24
 * <p>
 * 解决Mybatis Plus Order By SQL注入问题
 */
@Slf4j
public class DefaultPageParamArgumentResolver extends PageParamArgumentResolverSupport
		implements PageParamArgumentResolver {

	public DefaultPageParamArgumentResolver(int maxPageSize, String pageParameterName, String sizeParameterName,
			String sortParameterName) {
		setMaxPageSize(maxPageSize);
		setPageParameterName(pageParameterName);
		setSizeParameterName(sizeParameterName);
		setSortParameterName(sortParameterName);
	}

	/**
	 * 判断Controller是否包含page 参数
	 * @param parameter 参数
	 * @return 是否过滤
	 */
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return PageParam.class.isAssignableFrom(parameter.getParameterType());
	}

	/**
	 * @param parameter 入参集合
	 * @param mavContainer model 和 view
	 * @param webRequest web相关
	 * @param binderFactory 入参解析
	 * @return 检查后新的page对象
	 * @throws Exception ex
	 * <p>
	 * page 只支持查询 GET .如需解析POST获取请求报文体处理
	 */
	@Override
	public PageParam resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

		log.info("resolving argument ...");
		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
		if (request == null) {
			return new PageParam();
		}

		PageParam pageParam = getPageParam(parameter, request);

		paramValidate(parameter, mavContainer, webRequest, binderFactory, pageParam);

		return pageParam;
	}

}