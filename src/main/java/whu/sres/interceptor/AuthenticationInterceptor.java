package whu.sres.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import whu.sres.authority.VerifyToken;
import whu.sres.handler.CustomException;
import whu.sres.handler.ResultCode;
import whu.sres.model.Permission;
import whu.sres.model.Role;
import whu.sres.model.User;
import whu.sres.service.TokenService;
import whu.sres.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.List;

public class AuthenticationInterceptor implements HandlerInterceptor {

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

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) {
        // 从 http 请求头中取出 token，这能够免疫XSRF攻击（因为第三方网站构建请求时无法获知我的token，自然也就没办法在请求头中添加该请求信息）
        String token = httpServletRequest.getHeader("Authorization");
        // 如果不是映射到方法直接通过
        if (!(object instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();
        //检查有没有需要用户权限的注解
        if (method.isAnnotationPresent(VerifyToken.class)) {
            VerifyToken verifyToken = method.getAnnotation(VerifyToken.class);
            if (verifyToken.required()) {
                // 执行认证
                if (token == null) {
                    throw new CustomException(ResultCode.AUTH_NEED, "请登录后执行该操作");
                }
                // 获取 token 中的 username
                String userId = tokenService.getUserIdFromToken(token);
                if (userId == null)
                    throw new CustomException(ResultCode.AUTH_NEED, "请登录后执行该操作");
                // 验证 token
                try {
                    List<String> permissions = tokenService.getPermissions(token);
                    // 根据用户角色和url，判断该用户是否具有该权限
                    String url = verifyToken.url();
                    if (permissions.contains(url)) {
                        return true;
                    }
                    throw new CustomException(ResultCode.METHOD_NOT_ALLOWED, "用户不具有该权限");
                } catch (Exception e) {
                    throw new CustomException(ResultCode.AUTH_NEED, "登录已过期，请重新登录");
                }
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest,
                           HttpServletResponse httpServletResponse,
                           Object o, ModelAndView modelAndView) {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest,
                                HttpServletResponse httpServletResponse,
                                Object o, Exception e) {
    }
}
