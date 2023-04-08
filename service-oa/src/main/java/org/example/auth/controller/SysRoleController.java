package org.example.auth.controller;

import com.atguigu.vo.system.SysRoleQueryVo;
import com.atguigu.vo.system.AssginRoleVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.auth.service.SysRoleService;
import com.atguigu.model.system.SysRole;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import result.Result;

import java.util.List;
import java.util.Map;

@Api(tags = "角色管理接口")
@RestController
@RequestMapping("/admin/system/sysRole")
public class SysRoleController {
    //注入
    @Autowired
    @Qualifier(value = "sysRoleServiceImpl")
    private SysRoleService sysRoleService;

    @ApiOperation(value = "获取角色")
    @GetMapping("/toAssign/{userId}")
    public Result toAssign(@PathVariable long userId) {
        Map<String, Object> roleMap = sysRoleService.findRoleByAdminId(userId);
        return Result.ok(roleMap);
    }

    @ApiOperation(value = "给用户分配角色")
    @PostMapping("/assign")
    public Result assign(@RequestBody AssginRoleVo assginRoleVo){
        sysRoleService.assign(assginRoleVo);
        return Result.ok();
    }

    //查
    @ApiOperation("查询所有角色")
    @GetMapping("/findAll")
    public Result findAll(){
        List<SysRole> list = sysRoleService.list();
        return Result.ok(list);
    }

    @ApiOperation("条件分页查询")
    @GetMapping("/{page}/{limit}")
    public Result pageQueryRole(@PathVariable long page,
                                @PathVariable long limit,
                                SysRoleQueryVo sysRoleQueryVo){

        Page<SysRole> pageParam = new Page<>(page,limit);
        LambdaQueryWrapper<SysRole> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        String roleName = sysRoleQueryVo.getRoleName();
        if(!StringUtils.isEmpty(roleName)){
            lambdaQueryWrapper.like(SysRole::getRoleName,roleName).or().like(SysRole::getRoleCode,roleName);
        }

        IPage iPage = sysRoleService.page(pageParam,lambdaQueryWrapper);

        return Result.ok(iPage);
    }

    @ApiOperation("添加角色")
    @PostMapping("/save")
    public Result save(@RequestBody SysRole role){
        boolean isSaved = sysRoleService.save(role);
        if(isSaved){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

    @ApiOperation("根据id查询")
    @GetMapping("get/{id}")
    public Result getById(@PathVariable long id){
        SysRole role = sysRoleService.getById(id);

        return Result.ok(role);
    }

    @ApiOperation("修改角色")
    @PutMapping("/update")
    public Result update(@RequestBody SysRole role){
        boolean isSaved = sysRoleService.updateById(role);
        if(isSaved){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

    @ApiOperation("根据id删除")
    @DeleteMapping("/remove/{id}")
    public Result remove(@PathVariable long id){
        boolean isDeleted =  sysRoleService.removeById(id);
        if(isDeleted){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

    @ApiOperation("批量删除")
    @DeleteMapping("/batchRemove")
    public Result batchRemove(@RequestBody List<Long> idList){
        boolean isDeleted =  sysRoleService.removeByIds(idList);
        if(isDeleted){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }








}
