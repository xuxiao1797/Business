package org.example.auth.controller;

import com.atguigu.model.system.SysUser;
import com.atguigu.vo.system.LoginVo;
import com.atguigu.vo.system.RouterVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import jwt.JwtHelper;
import org.example.auth.service.SysMenuService;
import org.example.auth.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import result.Result;
import utils.MD5;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "后台登录管理")
@RestController
@RequestMapping("/admin/system/index")
public class IndexController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysMenuService sysMenuService;

    @PostMapping("/login")
    public Result login(LoginVo loginVo){
//        Map<String,Object> map = new HashMap<>();
//        map.put("token","admin-token");
//        return Result.ok(map);
        String username = loginVo.getUsername();
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getUsername,username);
        SysUser user =  sysUserService.getOne(queryWrapper);
        if(user == null){
            throw new RuntimeException("用户名或密码错误");
        }
        String encrypt = user.getPassword();
        String inputPassword = MD5.encrypt(loginVo.getPassword());
        if(!encrypt.equals(inputPassword)){
            throw new RuntimeException("用户名或密码错误");
        }

        if(user.getStatus() == 0){
            throw new RuntimeException("该用户已被禁用，请联系管理员");
        }

        String token = JwtHelper.createToken(user.getId(),user.getUsername());

        Map<String,Object> map = new HashMap<>();
        map.put("token",token);

        return Result.ok(map);
    }

    @GetMapping("/info")
    public Result info(HttpServletRequest request){

        String token = request.getHeader("header");
        Long userId = JwtHelper.getUserId(token);
        SysUser user  = sysUserService.getById(userId);

        List<RouterVo> MenuList = sysMenuService.findMenuListById(userId);

        List<String> btnList = sysMenuService.findBtnListById(userId);

        Map<String, Object> map = new HashMap<>();
        map.put("roles","[admin]");
        map.put("name",user.getName());
        map.put("avatar","https://oss.aliyuncs.com/aliyun_id_photo_bucket/default_handsome.jpg");
        map.put("routers",MenuList);
        map.put("buttons",btnList);
        return Result.ok(map);
    }

    @PostMapping("logout")
    public Result logout(){
        return Result.ok();
    }
}
