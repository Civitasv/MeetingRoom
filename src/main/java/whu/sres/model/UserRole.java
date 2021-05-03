package whu.sres.model;

import lombok.Data;

@Data
public class UserRole {
    // user id 唯一标识
    private String userId;
    // 角色 id
    private Integer roleId;
}
