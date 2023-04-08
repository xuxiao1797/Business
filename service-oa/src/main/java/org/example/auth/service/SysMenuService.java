package org.example.auth.service;


import com.atguigu.model.system.SysMenu;
import com.atguigu.model.wechat.Menu;
import com.atguigu.vo.system.AssginMenuVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author xx
 * @since 2023-04-08
 */
public interface SysMenuService extends IService<SysMenu> {

    List<SysMenu> findNodes();

    void removeMenuById(Long id);

    List<SysMenu> findSysMenuByRoleId(Long roleId);

    void assign(AssginMenuVo assignMenuVo);
}
