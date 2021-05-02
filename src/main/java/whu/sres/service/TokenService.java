package whu.sres.service;


import whu.sres.model.User;

public interface TokenService {
    String getToken(User user);

    String getUserIdFromToken(String token);
}
