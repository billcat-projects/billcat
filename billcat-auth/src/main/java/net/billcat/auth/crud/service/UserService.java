package net.billcat.auth.crud.service;

import net.billcat.auth.crud.model.UserDto;
import net.billcat.auth.crud.model.UserQo;
import net.billcat.auth.crud.model.UserVo;
import net.billcat.common.model.domain.PageParam;
import net.billcat.common.model.domain.PageResult;

public interface UserService {
	void create(UserDto dto);

	void update(Long id, UserDto dto);

	void updatePassword(Long id, String oldPassword, String newPassword);

	int delete(Long id);

	UserVo findById(Long id);

	PageResult<UserVo> findByPage(PageParam pageParam, UserQo qo);
}
