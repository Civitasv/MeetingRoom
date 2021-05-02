package whu.sres.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Service;
import whu.sres.model.User;
import whu.sres.service.TokenService;

import java.util.Date;

@Service
public class TokenServiceImpl implements TokenService {
    // 过期时间60分钟
    private static final long EXPIRE_TIME = 60 * 60 * 1000;

    @Override
    public String getToken(User user) {
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        String token = "";
        token = JWT.create().withAudience(user.getId()).withExpiresAt(date)
                .sign(Algorithm.HMAC256(user.getPassword()));
        return token;
    }

    /**
     * 从token中获取用户ID
     */
    @Override
    public String getUserIdFromToken(String token) {
        return JWT.decode(token).getAudience().get(0);
    }
}
