package org.example.auth.service.impl;


import com.atguigu.model.system.SysUser;
import org.example.auth.mapper.SysUserMapper;
import org.example.auth.service.SysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author xx
 * @since 2023-04-08
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

}
