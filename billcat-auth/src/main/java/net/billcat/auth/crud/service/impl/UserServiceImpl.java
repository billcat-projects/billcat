package net.billcat.auth.crud.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import net.billcat.auth.crud.enums.UserRole;
import net.billcat.auth.crud.mapper.UserMapper;
import net.billcat.auth.crud.model.*;
import net.billcat.auth.crud.service.UserService;
import net.billcat.common.model.domain.PageParam;
import net.billcat.common.model.domain.PageResult;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

	private final UserMapper userMapper;

	private final PasswordEncoder passwordEncoder;

	@Override
	public void create(UserDto dto) {
		User po = new User();
		BeanUtil.copyProperties(dto, po);

		// encrypt password
		po.setPassword(passwordEncoder.encode(dto.getPassword()));

		// set user role
		po.setRole(UserRole.USER.name());

		userMapper.insert(po);
	}

	@Override
	public void update(Long id, UserDto dto) {
		User po = userMapper.selectById(id);
		BeanUtil.copyProperties(dto, po);
		userMapper.updateById(po);
	}

	@Override
	public void updatePassword(Long id, String oldPassword, String newPassword) {
		User po = userMapper.selectById(id);
		if (po.getPassword().equals(passwordEncoder.encode(oldPassword))) {
			po.setPassword(passwordEncoder.encode(newPassword));
			userMapper.updateById(po);
		}
	}

	@Override
	public int delete(Long id) {
		return userMapper.deleteById(id);
	}

	@Override
	public UserVo findById(Long id) {
		User po = userMapper.selectById(id);
		UserVo vo = new UserVo();
		BeanUtil.copyProperties(po, vo);
		return vo;
	}

	private static LambdaQueryWrapper<User> getQueryWrapper(UserQo qo) {
		return Wrappers.<User>lambdaQuery()
			.like(StrUtil.isNotBlank(qo.getUsername()), User::getUsername, qo.getUsername())
			.like(StrUtil.isNotBlank(qo.getEmail()), User::getEmail, qo.getEmail())
			.like(StrUtil.isNotBlank(qo.getPhoneNumber()), User::getPhoneNumber, qo.getPhoneNumber());
	}

	@Override
	public PageResult<UserVo> findByPage(PageParam pageParam, UserQo qo) {
		Page<User> page = new Page<>(pageParam.getPage(), pageParam.getSize());

		List<PageParam.Sort> sorts = pageParam.getSorts();
		for (PageParam.Sort sort : sorts) {
			OrderItem orderItem = sort.isAsc() ? OrderItem.asc(sort.getField()) : OrderItem.desc(sort.getField());
			page.addOrder(orderItem);
		}

		IPage<User> data = userMapper.selectPage(page, getQueryWrapper(qo));

		// po -> vo
		List<UserVo> records = data.getRecords().stream().map(item -> {
			UserVo vo = new UserVo();
			BeanUtil.copyProperties(item, vo);
			return vo;
		}).collect(Collectors.toList());

		return new PageResult<>(records, data.getTotal());
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, username));
		if (user == null) {
			throw new UsernameNotFoundException(username);
		}

		UserDetails userDetails = new UserDetailsImpl();
		BeanUtil.copyProperties(user, userDetails);
		return userDetails;
	}

}
