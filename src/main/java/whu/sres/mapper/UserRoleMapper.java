package whu.sres.mapper;

import org.apache.ibatis.annotations.Mapper;
import whu.sres.model.UserRole;

@Mapper
public interface UserRoleMapper {
    int add(UserRole userRole);
}
