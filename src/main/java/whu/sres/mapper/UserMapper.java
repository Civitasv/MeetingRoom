package whu.sres.mapper;

import org.apache.ibatis.annotations.Mapper;
import whu.sres.model.User;

import java.util.List;

@Mapper
public interface UserMapper {
    User get(String id, String password);

    User getByUserId(String id);

    int add(User user);

    int delete(String id);

    int update(User user);

    int updatePwd(User user);

    int updateId(String id, String fill);

    int updatePwdAndPhone(User user);

    int updatePhone(User user);

    String getPwdByUserId(String id);

    List<User> getAll();

    List<User> getUsersByName(String name);

    List<User> getUsersByPhone(String phone);

}
