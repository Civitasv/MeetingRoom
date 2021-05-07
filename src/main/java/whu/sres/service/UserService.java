package whu.sres.service;

import whu.sres.model.User;

import java.util.List;

public interface UserService {
    User get(String userId, String password);

    User getByUserId(String id);

    int add(User user);

    int delete(String id);

    int update(User user);

    int updatePwd(User user);

    int updatePhone(User user);

    String getPwdByUserId(String id);

    List<User> getAll();

    List<User> getUsersByName(String name);

    List<User> getUsersByPhone(String phone);

}
