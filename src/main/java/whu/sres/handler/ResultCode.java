package whu.sres.handler;

public interface ResultCode {
    // GET
    int OK = 200;
    // POST
    int CREATED = 201;
    int NOT_FOUND = 404; // 未找到资源
    int CONFLICT = 409;  // 冲突
    // PUT
    int NO_CONTENT = 204; // 无该内容
    int METHOD_NOT_ALLOWED = 405; // 无权限
    // DELETE
    int AUTH_NEED = 401; // 需要权限
}
