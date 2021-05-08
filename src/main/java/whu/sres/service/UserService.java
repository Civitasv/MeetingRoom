package whu.sres.service;

import whu.sres.model.User;
import whu.sres.model.UserRole;

import java.util.List;

public interface UserService {
    User get(String userId, String password);

    User getByUserId(String id);

    int add(User user);

    int delete(String id);

    int update(User user);

    int updatePwdAndPhone(User user);

    int updatePhone(User user);

    String getPwdByUserId(String id);

    List<User> getAll();

    List<User> getUsersByName(String name);

    List<User> getUsersByPhone(String phone);

    int addUserRole(String userId, int roleId);

    int deleteUserRole(String userId, int roleId);

    List<UserRole> getRoleByUserId(String userId);
}
