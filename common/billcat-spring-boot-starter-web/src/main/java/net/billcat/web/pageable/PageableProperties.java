package net.billcat.web.pageable;

import lombok.Data;
import net.billcat.common.model.domain.PageableConstants;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author hccake
 */
@Data
@ConfigurationProperties("ballcat.pageable")
public class PageableProperties {

	/**
	 * 当前页的参数名
	 */
	private String pageParameterName = PageableConstants.DEFAULT_PAGE_PARAMETER;

	/**
	 * 每页数据量的参数名
	 */
	private String sizeParameterName = PageableConstants.DEFAULT_SIZE_PARAMETER;

	/**
	 * 排序规则的参数名
	 */
	private String sortParameterName = PageableConstants.DEFAULT_SORT_PARAMETER;

	/**
	 * 分页查询的每页最大数据量
	 */
	private int maxPageSize = PageableConstants.DEFAULT_MAX_PAGE_SIZE;

}