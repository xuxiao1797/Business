package org.example.auth.service.utils;

import com.atguigu.model.system.SysMenu;

import java.util.ArrayList;
import java.util.List;

public class MenuHelper {

    public static List<SysMenu> buildTree(List<SysMenu> menuList) {
        List<SysMenu> list = new ArrayList<>();

        for(SysMenu menu:menuList){
            if(menu.getParentId() == 0){
                list.add(findChildren(menu,menuList));
            }
        }
        return list;
    }

    public static SysMenu findChildren(SysMenu menu,List<SysMenu> menuList){
        menu.setChildren(new ArrayList<>());
        for(SysMenu menu1:menuList){
            if(menu.getId().longValue() == menu1.getParentId().longValue()){
                if (menu.getChildren() == null) {
                    menu.setChildren(new ArrayList<>());
                }
                menu.getChildren().add(findChildren(menu1,menuList));
            }
        }

        return menu;
    }
}
