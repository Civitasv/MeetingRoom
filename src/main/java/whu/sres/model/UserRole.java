package whu.sres.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserRole {
    // user id 唯一标识
    private String userId;
    // 角色 id
    private Integer roleId;
}
