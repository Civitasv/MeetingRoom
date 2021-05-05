package whu.sres.service.impl;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import whu.sres.model.Permission;
import whu.sres.model.Role;
import whu.sres.model.User;
import whu.sres.service.TokenService;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;

@Service
public class TokenServiceImpl implements TokenService {
    // access token 过期时间15分钟
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 15 * 60 * 1000L;
    // refresh token 过期时间30天，意味着用户如果一个月都没登录，则需要重新登录一次
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 30 * 24 * 60 * 60 * 1000L;

    private static final String KEY = "3EK6FD+o0+c7tzBNVfjpMkNDi2yARAAKzQlk8O2IKoxQu4nF7EdAh8s3TwpHwrdWT6R";

    @Override
    public Map<String, Object> getAccessToken(User user) {
        Date date = new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRE_TIME);
        // 将用户具有的权限存入claim
        List<String> permissions = new ArrayList<>();
        for (Role role : user.getRoles()) {
            for (Permission permission : role.getPermissions()) {
                permissions.add(permission.getUrl());
            }
        }
        Key signKey = new SecretKeySpec(DatatypeConverter.parseBase64Binary(KEY), SignatureAlgorithm.HS256.getJcaName());
        String accessToken = Jwts.builder()
                .setSubject(user.getId())
                .claim("permissions", permissions)
                .setExpiration(date)
                .signWith(signKey)
                .compact();
        Map<String, Object> map = new HashMap<>();
        map.put("accessToken", accessToken);
        map.put("accessTokenExpiry", date.getTime());
        return map;
    }

    @Override
    public Map<String, Object> getRefreshToken(String userId) {
        Date date = new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRE_TIME);
        Map<String, Object> map = new HashMap<>();
        Key signKey = new SecretKeySpec(DatatypeConverter.parseBase64Binary(KEY), SignatureAlgorithm.HS256.getJcaName());
        String refreshToken = Jwts.builder()
                .setSubject(userId)
                .setExpiration(date)
                .signWith(signKey)
                .compact();
        map.put("refreshToken", refreshToken); // 使用用户id签发refresh token
        map.put("refreshTokenMaxAge", REFRESH_TOKEN_EXPIRE_TIME / 1000);
        return map;
    }

    /**
     * 从access token中获取用户ID
     */
    @Override
    public String getUserIdFromToken(String accessToken) {
        Claims claims = parseJWT(accessToken);
        return claims.getSubject();
    }

    /**
     * 从access token中获取用户权限
     */

    @Override
    public List<String> getPermissions(String accessToken) {
        Claims claims = parseJWT(accessToken);
        return claims.get("permissions", List.class);
    }

    private Claims parseJWT(String accessToken) {
        Key signKey = new SecretKeySpec(DatatypeConverter.parseBase64Binary(KEY), SignatureAlgorithm.HS256.getJcaName());
        Jws<Claims> jws = Jwts.parserBuilder().setSigningKey(signKey)
                .build()
                .parseClaimsJws(accessToken);
        return jws.getBody();
    }

    @Override
    public boolean isExpire(String accessToken) {
        Claims claims = parseJWT(accessToken);
        return claims.getExpiration().before(new Date());
    }
}
