package whu.sres.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import whu.sres.mapper.UserMapper;
import whu.sres.mapper.UserRoleMapper;
import whu.sres.model.User;
import whu.sres.model.UserRole;
import whu.sres.service.UserService;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private UserMapper mapper;
    private UserRoleMapper userRoleMapper;

    @Autowired
    public void setMapper(UserMapper mapper) {
        this.mapper = mapper;
    }

    @Autowired
    public void setUserRoleMapper(UserRoleMapper userRoleMapper) {
        this.userRoleMapper = userRoleMapper;
    }

    @Override
    public User get(String userId, String password) {
        return mapper.get(userId, password);
    }

    @Override
    public User getByUserId(String id) {
        return mapper.getByUserId(id);
    }

    @Override
    public int add(User user) {
        return mapper.add(user);
    }

    @Override
    public int delete(String id) {
        return mapper.delete(id);
    }

    @Override
    public int update(User user) {
        return mapper.update(user);
    }

    @Override
    public int updatePwdAndPhone(User user) {
        return mapper.updatePwdAndPhone(user);
    }

    @Override
    public int updatePhone(User user) {
        return mapper.updatePhone(user);
    }

    @Override
    public String getPwdByUserId(String id) {
        return mapper.getPwdByUserId(id);
    }

    @Override
    public List<User> getAll() {
        return mapper.getAll();
    }

    @Override
    public List<User> getUsersByName(String name) {
        return mapper.getUsersByName(name);
    }

    @Override
    public List<User> getUsersByPhone(String phone) {
        return mapper.getUsersByPhone(phone);
    }

    @Override
    public int addUserRole(String userId, int roleId) {
        return userRoleMapper.add(new UserRole(userId, roleId));
    }

    @Override
    public int deleteUserRole(String userId, int roleId) {
        return userRoleMapper.delete(new UserRole(userId, roleId));
    }

    @Override
    public int deleteUserRoleByUserId(String userId) {
        return userRoleMapper.deleteUserRoleByUserId(userId);
    }

    @Override
    public List<UserRole> getRoleByUserId(String userId) {
        return userRoleMapper.getRoleByUserId(userId);
    }
}
