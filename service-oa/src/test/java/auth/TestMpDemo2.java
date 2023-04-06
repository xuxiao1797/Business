package auth;

import auth.mapper.SysRoleMapper;
import auth.service.SysRoleService;
import com.atguigu.model.system.SysRole;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class TestMpDemo2 {

    @Autowired
    @Qualifier(value = "sysRoleServiceImpl")
    private SysRoleService service;

    @Test
    public void getAll(){
        List<SysRole> list = service.list();
        System.out.println(list);
    }


}
