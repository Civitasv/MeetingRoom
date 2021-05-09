package whu.sres.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import whu.sres.authority.VerifyToken;
import whu.sres.handler.Result;
import whu.sres.handler.ResultCode;
import whu.sres.model.Record;
import whu.sres.model.Role;
import whu.sres.model.User;
import whu.sres.service.TokenService;
import whu.sres.service.UserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.*;

@RestController
@RequestMapping("user")
public class UserController {
    private UserService userService;
    private TokenService tokenService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setTokenService(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public String login(@RequestBody User requestUser, HttpServletResponse response) {
        String username = requestUser.getId();
        String password = requestUser.getPassword();
        // 密码加密
        String encryptPwd = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
        User user = userService.get(username, encryptPwd);
        if (!Objects.isNull(user)) { // 验证成功，可登录
            Map<String, Object> accessTokenInfo = tokenService.getAccessToken(user); // 获得access token
            Map<String, Object> refreshTokenInfo = tokenService.getRefreshToken(user.getId()); // 获得refresh token
            Map<String, Object> map = new HashMap<>();
            map.put("access_token", accessTokenInfo.get("accessToken"));
            map.put("access_token_expiry", accessTokenInfo.get("accessTokenExpiry"));
            map.put("user_id", user.getId());
            map.put("user_name", user.getName());
            map.put("user_phone", user.getPhone());
            List<String> roles = new ArrayList<>();
            for (Role role : user.getRoles()) {
                roles.add(role.getRole());
            }
            map.put("user_roles", roles);
            // 将 refresh token 加入httponly cookie
            Cookie cookie = new Cookie("refresh_token", refreshTokenInfo.get("refreshToken").toString());
            cookie.setMaxAge(Integer.parseInt(refreshTokenInfo.get("refreshTokenMaxAge").toString()));
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            response.addCookie(cookie);

            return new Result<Map<String, Object>>().success(true).message("登录成功").code(ResultCode.OK).data(map).toString();
        } else {
            return new Result<String>().success(false).message("登陆失败, 请检查用户名和密码").code(ResultCode.NOT_FOUND).toString();
        }
    }

    @GetMapping("logout")
    public String logout(@CookieValue(value = "refresh_token", defaultValue = "") String refreshToken, HttpServletRequest request, HttpServletResponse response) {
        if (refreshToken.isEmpty()) {
            return new Result<Map<String, Object>>().success(true).message("退出成功").code(ResultCode.OK).toString();
        }
        if (tokenService.isExpire(refreshToken)) {
            return new Result<Map<String, Object>>().success(true).message("退出成功").code(ResultCode.OK).toString();
        }
        // 清除token
        Cookie[] cookies = request.getCookies();
        Optional<Cookie> cookieOptional = Arrays.stream(cookies)
                .filter(cookie1 -> "refresh_token".equals(cookie1.getName()))
                .findFirst();
        if (cookieOptional.isPresent()) {
            Cookie cookie = cookieOptional.get();
            cookie.setMaxAge(0);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            response.addCookie(cookie);
        }
        return new Result<Map<String, Object>>().success(true).message("退出成功").code(ResultCode.OK).toString();
    }

    @PostMapping("/repeat")
    public String repeat(@RequestBody String userId) {
        User user = userService.getByUserId(userId);
        if (!Objects.isNull(user)) {
            return new Result<String>().success(true).message("该用户名可用！").code(ResultCode.OK).toString();
        } else {
            return new Result<String>().success(false).message("该用户名已被注册！").code(ResultCode.CONFLICT).toString();
        }
    }

    @VerifyToken(url = "/user/add")
    @PostMapping("/add")
    public String add(@RequestBody User user) {
        // 密码加密
        String encryptPwd = DigestUtils.md5DigestAsHex(user.getPassword().getBytes(StandardCharsets.UTF_8));
        user.setPassword(encryptPwd);
        // 添加用户
        userService.add(user);
        // 获取roleId，为用户指定角色
        Integer roleId = user.getRoleId();
        if (roleId == 1) { // 普通用户
            userService.addUserRole(user.getId(), 1);
        } else if (roleId == 2) { // 管理员
            userService.addUserRole(user.getId(), 1);
            userService.addUserRole(user.getId(), 2);
        }
        return new Result<String>().success(true).message("成功添加用户！").code(ResultCode.CREATED).toString();
    }

    @VerifyToken(url = "/user/delete")
    @DeleteMapping("/delete")
    public String delete(@RequestParam String id) {
        userService.delete(id);
        userService.deleteUserRoleByUserId(id);
        return new Result<String>().success(true).message("成功删除用户！").code(ResultCode.OK).toString();
    }

    @VerifyToken(url = "/user/update")
    @PutMapping("/update")
    public String update(@RequestBody User user) {
        // 更新用户
        userService.update(user);
        // 获取roleId，更新用户角色
        Integer roleId = user.getRoleId();
        // 先删除，再指定
        userService.deleteUserRole(user.getId(), 1);
        userService.deleteUserRole(user.getId(), 2);
        if (roleId == 1) { // 普通用户
            userService.addUserRole(user.getId(), 1);
        } else if (roleId == 2) { // 管理员
            userService.addUserRole(user.getId(), 1);
            userService.addUserRole(user.getId(), 2);
        }
        return new Result<String>().success(true).message("成功更新用户！").code(ResultCode.OK).toString();
    }

    @VerifyToken(url = "/user/updatePwd")
    @PutMapping("/updatePwd")
    public String updatePwd(@RequestBody User user) {
        String encryptPwd = DigestUtils.md5DigestAsHex(user.getPassword().getBytes(StandardCharsets.UTF_8));
        user.setPassword(encryptPwd);
        userService.updatePwdAndPhone(user);
        return new Result<String>().success(true).message("成功更新用户密码和手机号码！").code(ResultCode.OK).toString();
    }

    @VerifyToken(url = "/user/updatePhone")
    @PutMapping("/updatePhone")
    public String updatePhone(@RequestBody User user) {
        userService.updatePhone(user);
        return new Result<String>().success(true).message("成功更新用户手机！").code(ResultCode.OK).toString();
    }

    @VerifyToken(url = "/user/all")
    @GetMapping("/all")
    public String getAll(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        PageHelper.startPage(page, size);
        PageInfo<User> users = new PageInfo<>(userService.getAll());
        return new Result<PageInfo<User>>().data(users).success(true).message("用户数据获取成功").code(ResultCode.OK).toString();
    }
}

