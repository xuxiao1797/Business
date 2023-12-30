package org.example.auth.service.impl;

import com.atguigu.model.system.SysUser;
import org.example.auth.service.SysMenuService;
import org.example.auth.service.SysUserService;
import org.example.security.custom.CustomUser;
import org.example.security.custom.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private SysUserService service;

    @Autowired
    private SysMenuService sysMenuService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser user = service.getUserByUsername(username);
        if(null == user) {
            throw new UsernameNotFoundException("用户名不存在！");
        }

        if(user.getStatus() == 0) {
            throw new RuntimeException("账号已停用");
        }

        List<String> userBtnList= sysMenuService.findBtnListById(user.getId());

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        for(String btn:userBtnList){
            authorities.add(new SimpleGrantedAuthority(btn.trim()));
        }


        return new CustomUser(user, authorities);
    }
}
