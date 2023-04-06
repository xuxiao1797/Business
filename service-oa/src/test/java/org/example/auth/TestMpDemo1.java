package org.example.auth;

import org.example.auth.mapper.SysRoleMapper;
import com.atguigu.model.system.SysRole;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class TestMpDemo1 {

    @Autowired
    private SysRoleMapper mapper;

    @Test
    public void getAll(){
       List<SysRole> list =   mapper.selectList(null);
        System.out.println(list);
    }

    @Test
    public void add() {
        SysRole role = new SysRole();
        role.setRoleName("角色管理员");
        role.setRoleCode("role");
        role.setDescription("角色管理员");
        int rows =  mapper.insert(role);
        System.out.println(rows);
        System.out.println(role);
    }

    @Test
    public void update() {
       SysRole role =  mapper.selectById(9);
       role.setRoleCode("角色管理员修改");
       int rows = mapper.updateById(role);
        System.out.println(rows);
    }

    @Test
    public void delete() {
        mapper.deleteById(9);
    }

    @Test
    public void testQueryWrapper(){
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_name","总经理");
        List<SysRole> list =  mapper.selectList(queryWrapper);
        System.out.println(list);
    }

    @Test
    public void testLambdaQueryWrapper(){
        LambdaQueryWrapper<SysRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRole::getRoleName,"总经理");
        List<SysRole> list =  mapper.selectList(queryWrapper);
        System.out.println(list);
    }
}
