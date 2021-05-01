package whu.sres.model;

import lombok.Data;

import java.util.List;

@Data
public class Role {
    private int id;
    // 角色名
    private String role;
    // 具有该角色的所有用户
    private List<User> users;
    // 权限
    private List<Permission> permissions;
}
