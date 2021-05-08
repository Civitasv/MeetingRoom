package whu.sres.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import whu.sres.authority.VerifyToken;
import whu.sres.handler.Result;
import whu.sres.handler.ResultCode;
import whu.sres.model.Role;
import whu.sres.model.User;
import whu.sres.service.TokenService;
import whu.sres.service.UserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RestController
@RequestMapping("token")
public class TokenController {
    private TokenService tokenService;

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setTokenService(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @GetMapping("refresh")
    public String refresh(@CookieValue(value = "refresh_token", defaultValue = "") String refreshToken, HttpServletResponse response) {
        // 验证 refresh token
        if (refreshToken.isEmpty()) {
            return new Result<Map<String, Object>>().success(true).message("token不可以为空").code(ResultCode.AUTH_NEED).toString();
        }
        if (tokenService.isExpire(refreshToken)) {
            return new Result<Map<String, Object>>().success(true).message("刷新token已经失效").code(ResultCode.AUTH_NEED).toString();
        }
        // 获取userId
        String userId = tokenService.getUserIdFromToken(refreshToken);
        if (Objects.isNull(userId)) {
            return new Result<Map<String, Object>>().success(true).message("刷新token已经失效").code(ResultCode.AUTH_NEED).toString();
        }
        // 根据userId获取user
        User user = userService.getByUserId(userId);
        if (user == null) {
            return new Result<Map<String, Object>>().success(true).message("用户不存在").code(ResultCode.NOT_FOUND).toString();
        }
        // 重新生成 access token 和 refresh token
        Map<String, Object> accessTokenInfo = tokenService.getAccessToken(user); // 获得access token
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
        Map<String, Object> refreshTokenInfo = tokenService.getRefreshToken(userId);
        // 将 refresh token 加入httponly cookie
        Cookie cookie = new Cookie("refresh_token", refreshTokenInfo.get("refreshToken").toString());
        cookie.setMaxAge(Integer.parseInt(refreshTokenInfo.get("refreshTokenMaxAge").toString()));
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return new Result<Map<String, Object>>().success(true).message("刷新成功").code(ResultCode.OK).data(map).toString();
    }
}
