package org.example.auth.service.impl;


import com.atguigu.model.system.SysMenu;
import com.atguigu.model.system.SysRole;
import com.atguigu.model.system.SysRoleMenu;
import com.atguigu.model.system.SysUserRole;
import com.atguigu.model.wechat.Menu;
import com.atguigu.vo.system.AssginMenuVo;
import com.atguigu.vo.system.MetaVo;
import com.atguigu.vo.system.RouterVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.example.auth.mapper.SysMenuMapper;
import org.example.auth.service.SysMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.auth.service.SysRoleMenuService;
import org.example.auth.service.utils.MenuHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
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

    @Override
    public List<RouterVo> findMenuListById(Long userId) {
        List<SysMenu> list = null;
        if(userId == 1){
            LambdaQueryWrapper<SysMenu> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SysMenu::getStatus,1);
            queryWrapper.orderByAsc(SysMenu::getSortValue);
            list = baseMapper.selectList(queryWrapper);


        }else {
            list = baseMapper.findMenuListById(userId);
        }
        List<SysMenu> list1 = MenuHelper.buildTree(list);

        List<RouterVo> routerVoList = this.buildRouter(list1);
        return routerVoList;
    }

    private List<RouterVo> buildRouter(List<SysMenu> menus) {
        List<RouterVo> routerVoList = new ArrayList<>();
        for(SysMenu menu:menus){
            RouterVo routerVo = new RouterVo();
            routerVo.setHidden(false);
            routerVo.setAlwaysShow(false);
            routerVo.setPath(getRouterPath(menu));
            routerVo.setComponent(menu.getComponent());
            routerVo.setMeta(new MetaVo(menu.getName(),menu.getIcon()));

            List<SysMenu> children = menu.getChildren();
            if(menu.getType() == 1){
                List<SysMenu> menuList = children.stream()
                        .filter(item-> !StringUtils.isEmpty(item.getComponent()))
                        .collect(Collectors.toList());
                for(SysMenu hiddenMenu:menuList){
                    RouterVo hiddenRouter = new RouterVo();
                    hiddenRouter.setHidden(true);
                    hiddenRouter.setAlwaysShow(false);
                    hiddenRouter.setPath(getRouterPath(hiddenMenu));
                    hiddenRouter.setComponent(hiddenMenu.getComponent());
                    hiddenRouter.setMeta(new MetaVo(hiddenMenu.getName(), hiddenMenu.getIcon()));

                    routerVoList.add(hiddenRouter);
                }
            }else {
                if(!CollectionUtils.isEmpty(children)){
                    if(children.size() >0){
                        routerVo.setAlwaysShow(true);
                    }
                    routerVo.setChildren(buildRouter(children));
                }
            }
            routerVoList.add(routerVo);
        }
        return routerVoList;
    }

    public String getRouterPath(SysMenu menu) {
        String routerPath = "/" + menu.getPath();
        if(menu.getParentId().intValue() != 0) {
            routerPath = menu.getPath();
        }
        return routerPath;
    }

    @Override
    public List<String> findBtnListById(Long userId) {
        List<SysMenu> menuList = null;
        if(userId == 1){
            LambdaQueryWrapper<SysMenu> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SysMenu::getStatus,1);
            menuList = baseMapper.selectList(queryWrapper);
        }else {
            menuList =   baseMapper.findMenuListById(userId);
        }


        return menuList.stream().filter(item->item.getType() == 2)
                .map(SysMenu::getPerms)
                .collect(Collectors.toList());
    }
}
