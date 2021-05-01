package whu.sres.model;

import lombok.Data;

import java.util.List;

@Data
public class Permission {
    private int id;
    private String name; // 名字
    private String permission; // 详细介绍权限
    private String url; // url
    private List<Role> roles; // 具有该权限的所有角色
}
