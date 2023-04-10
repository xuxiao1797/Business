package org.example.auth.controller;


import com.atguigu.model.system.SysMenu;
import com.atguigu.model.wechat.Menu;
import com.atguigu.vo.system.AssginMenuVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.example.auth.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import result.Result;

import java.util.List;

/**
 * <p>
 * 菜单表 前端控制器
 * </p>
 *
 * @author xx
 * @since 2023-04-08
 */

@Api(tags = "菜单管理接口")
@RestController
@RequestMapping("/admin/system/sysMenu")
public class SysMenuController {

    @Autowired
    private SysMenuService sysMenuService;


    @ApiOperation(value = "根据角色获取菜单")
    @GetMapping("/toAssign/{roleId}")
    public Result toAssign(@PathVariable Long roleId) {
        List<SysMenu> list = sysMenuService.findSysMenuByRoleId(roleId);
        return Result.ok(list);
    }

    @ApiOperation(value = "给角色分配权限")
    @PostMapping("/assign")
    public Result assign(@RequestBody AssginMenuVo assignMenuVo) {
        sysMenuService.assign(assignMenuVo);
        return Result.ok();
    }


    @ApiOperation("菜单列表")
    @GetMapping("/findNodes")
    public Result findNodes(){
        List<SysMenu> list = sysMenuService.findNodes();
        return Result.ok(list);
    }


    @ApiOperation(value = "新增菜单")
    @PostMapping("/save")
    public Result save(@RequestBody SysMenu sysMenu){
        sysMenuService.save(sysMenu);
        return Result.ok();
    }

    @ApiOperation(value = "修改菜单")
    @PutMapping("/update")
    public Result updateById(@RequestBody SysMenu permission) {
        sysMenuService.updateById(permission);
        return Result.ok();
    }

    @ApiOperation(value = "删除菜单")
    @DeleteMapping("/remove/{id}")
    public Result remove(@PathVariable Long id) {
        sysMenuService.removeMenuById(id);
        return Result.ok();
    }
}

