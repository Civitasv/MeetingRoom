package whu.sres.mapper;

import org.apache.ibatis.annotations.Mapper;
import whu.sres.model.RolePermission;

@Mapper
public interface RolePermissionMapper {
    int add(RolePermission rolePermission);
}
