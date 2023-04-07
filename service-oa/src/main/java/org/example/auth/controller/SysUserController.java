package org.example.auth.controller;


import com.atguigu.model.system.SysUser;
import com.atguigu.vo.system.SysUserQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.example.auth.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import result.Result;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author xx
 * @since 2023-04-08
 */
@Api(tags = "用户维护接口")
@RestController
@RequestMapping("/admin/system/sysUser")
public class SysUserController {

    @Autowired
    private SysUserService service;


    @ApiOperation("用户条件分页查询")
    @GetMapping("/{page}/{limit}")

    public Result index(@PathVariable long page,
                        @PathVariable long limit,
                        SysUserQueryVo sysUserQueryVo){

        Page<SysUser> pageParam = new Page<>(page,limit);
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper();

        String username = sysUserQueryVo.getKeyword();
        String timeBegin = sysUserQueryVo.getCreateTimeBegin();
        String timeEnd = sysUserQueryVo.getCreateTimeEnd();

        if(!StringUtils.isEmpty(username)){
            queryWrapper.like(SysUser::getUsername,username);
        }
        //ge 大于等于
        if(!StringUtils.isEmpty(timeBegin)){
            queryWrapper.ge(SysUser::getCreateTime,timeBegin);
        }

        //le 小于等于
        if(!StringUtils.isEmpty(timeEnd)){
            queryWrapper.le(SysUser::getCreateTime,timeEnd);
        }

        IPage<SysUser> page1 = service.page(pageParam,queryWrapper);


        return Result.ok(page1);
    }

    @ApiOperation(value = "获取用户")
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id) {
        SysUser user = service.getById(id);
        return Result.ok(user);
    }

    @ApiOperation(value = "保存用户")
    @PostMapping("save")
    public Result save(@RequestBody SysUser user) {
        service.save(user);
        return Result.ok();
    }

    @ApiOperation(value = "更新用户")
    @PutMapping("update")
    public Result updateById(@RequestBody SysUser user) {
        service.updateById(user);
        return Result.ok();
    }

    @ApiOperation(value = "删除用户")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id) {
        service.removeById(id);
        return Result.ok();
    }
}

