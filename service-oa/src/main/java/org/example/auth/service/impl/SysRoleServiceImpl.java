package org.example.auth.service.impl;

import com.atguigu.model.system.SysUserRole;
import com.atguigu.vo.system.AssginRoleVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.example.auth.mapper.SysRoleMapper;
import org.example.auth.mapper.SysUserRoleMapper;
import org.example.auth.service.SysRoleService;
import com.atguigu.model.system.SysRole;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.auth.service.SysUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Autowired
    private SysUserRoleService service;

    @Override
    public Map<String, Object> findRoleByAdminId(Long id) {
       List<SysRole> allRoleList =  baseMapper.selectList(null);

        LambdaQueryWrapper<SysUserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserRole::getUserId,id);
        List<SysUserRole> userRoleList = service.list(queryWrapper);

        List<Long> userRoleIdList = userRoleList.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());

        List<SysRole> list = new ArrayList<>();
        for(SysRole sysRole : allRoleList){
            if (userRoleIdList.contains(sysRole.getId())){
                list.add(sysRole);
            }
        }
        Map<String, Object> roleMap = new HashMap<>();
        roleMap.put("assignRoleList", list);
        roleMap.put("allRolesList", allRoleList);
        return roleMap;
    }

    @Override
    public void assign(AssginRoleVo assginRoleVo) {
        LambdaQueryWrapper<SysUserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserRole::getUserId,assginRoleVo.getUserId());
        service.remove(queryWrapper);

        List<Long> roleIdList = assginRoleVo.getRoleIdList();
        for(Long id:roleIdList){
            if(StringUtils.isEmpty(id)){
               continue;
            }
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setUserId(assginRoleVo.getUserId());
            sysUserRole.setRoleId(id);
            service.save(sysUserRole);
        }

    }
}
