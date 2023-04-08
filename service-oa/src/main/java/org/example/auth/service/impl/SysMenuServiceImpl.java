package org.example.auth.service.impl;


import com.atguigu.model.system.SysMenu;
import com.atguigu.model.system.SysRole;
import com.atguigu.model.system.SysRoleMenu;
import com.atguigu.model.system.SysUserRole;
import com.atguigu.model.wechat.Menu;
import com.atguigu.vo.system.AssginMenuVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.example.auth.mapper.SysMenuMapper;
import org.example.auth.service.SysMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.auth.service.SysRoleMenuService;
import org.example.auth.service.utils.MenuHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author xx
 * @since 2023-04-08
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Autowired
    private SysRoleMenuService sysRoleMenuService;


    @Override
    public List<SysMenu> findNodes() {
        List<SysMenu> menuList = baseMapper.selectList(null);

        List<SysMenu> list = MenuHelper.buildTree(menuList);

        return list;
    }

    @Override
    public void removeMenuById(Long id) {
        LambdaQueryWrapper<SysMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysMenu::getParentId,id);
        Integer count = baseMapper.selectCount(queryWrapper);
        if(count > 0){
            throw new RuntimeException("菜单不能删除");
        }
        baseMapper.deleteById(id);
    }

    @Override
    public List<SysMenu> findSysMenuByRoleId(Long roleId) {
        LambdaQueryWrapper<SysMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysMenu::getStatus,1);
        List<SysMenu> menuList = baseMapper.selectList(queryWrapper);

        LambdaQueryWrapper<SysRoleMenu> roleMenuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roleMenuLambdaQueryWrapper.eq(SysRoleMenu::getRoleId,roleId);
       List<SysRoleMenu> roleMenuList =  sysRoleMenuService.list(roleMenuLambdaQueryWrapper);

       List<Long> roleMenuIdList  = roleMenuList.stream().map(SysRoleMenu::getMenuId).collect(Collectors.toList());

        menuList.forEach(item->{
            item.setSelect(roleMenuIdList.contains(item.getId()));
        });

        return MenuHelper.buildTree(menuList);
    }

    @Override
    public void assign(AssginMenuVo assignMenuVo) {
        LambdaQueryWrapper<SysRoleMenu> queryWrapper= new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRoleMenu::getRoleId,assignMenuVo.getRoleId());
        sysRoleMenuService.remove(queryWrapper);

        List<Long> list = assignMenuVo.getMenuIdList();
        for(Long id : list){
            if(StringUtils.isEmpty(id)){
                continue;
            }
            SysRoleMenu roleMenu = new SysRoleMenu();
            roleMenu.setRoleId(assignMenuVo.getRoleId());
            roleMenu.setMenuId(id);
            sysRoleMenuService.save(roleMenu);
        }

    }
}
