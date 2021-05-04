package whu.sres.model;

import java.util.List;

import lombok.Data;

@Data
public class User {
    // 唯一值
    private String id;
    // 密码
    private String password;
    // 电话
    private String phone;
    // 名字
    private String name;
    // 角色
    private List<Role> roles;
    // 刷新token
    private String refreshToken;
}
