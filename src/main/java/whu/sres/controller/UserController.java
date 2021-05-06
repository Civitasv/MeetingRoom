package whu.sres.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import whu.sres.authority.VerifyToken;
import whu.sres.handler.Result;
import whu.sres.handler.ResultCode;
import whu.sres.model.User;
import whu.sres.service.TokenService;
import whu.sres.service.UserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Predicate;

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
        int res = userService.add(user);
        if (res != 0) {
            return new Result<String>().success(true).message("成功添加用户！").code(ResultCode.CREATED).toString();
        } else {
            return new Result<String>().success(false).message("添加用户失败！").code(ResultCode.CONFLICT).toString();
        }
    }

    @VerifyToken(url = "/user/delete")
    @DeleteMapping("/delete")
    public String delete(@RequestParam String id) {
        int res = userService.delete(id);
        if (res != 0) {
            return new Result<String>().success(true).message("成功删除用户！").code(ResultCode.OK).toString();
        } else {
            return new Result<String>().success(false).message("删除用户失败！").code(ResultCode.NO_CONTENT).toString();
        }
    }

    @VerifyToken(url = "/user/update")
    @PutMapping("/update")
    public String update(@RequestBody User user) {
        int res = userService.update(user);
        if (res != 0) {
            return new Result<String>().success(true).message("成功更新用户！").code(ResultCode.OK).toString();
        } else {
            return new Result<String>().success(false).message("更新用户失败！").code(ResultCode.NO_CONTENT).toString();
        }
    }
}
