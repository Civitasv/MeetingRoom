package whu.sres.mapper;

import org.apache.ibatis.annotations.Mapper;
import whu.sres.model.UserRole;

import java.util.List;

@Mapper
public interface UserRoleMapper {
    int add(UserRole userRole);

    int delete(UserRole userRole);

    int deleteUserRoleByUserId(String userId);

    List<UserRole> getRoleByUserId(String userId);
}
