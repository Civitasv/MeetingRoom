package whu.sres.service;


import whu.sres.model.User;

import java.util.List;
import java.util.Map;

public interface TokenService {
    Map<String, Object> getAccessToken(User user);

    Map<String, Object> getRefreshToken(String userId);

    String getUserIdFromToken(String accessToken);

    List<String> getPermissions(String accessToken);

    boolean isExpire(String accessToken);
}
