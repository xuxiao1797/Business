package org.example.auth.service.impl;


import com.atguigu.model.system.SysUser;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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

    @Override
    public void updateStatus(Long id, Integer status) {
        SysUser user =  baseMapper.selectById(id);
        user.setStatus(status);
        baseMapper.updateById(user);
    }

    @Override
    public SysUser getUserByUsername(String username) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getUsername,username);
        SysUser user =  baseMapper.selectOne(queryWrapper);
        return user;
    }
}
