package whu.sres.handler;

import com.google.gson.Gson;
import lombok.Data;

@Data
public class Result<T> {
    private Boolean success; // 是否成功

    private Integer code; // 返回码

    private String message; // 返回消息

    private T data; // 详细数据

    public Result<T> success(boolean success) {
        this.setSuccess(success);
        return this;
    }

    public Result<T> code(int code) {
        this.setCode(code);
        return this;
    }

    public Result<T> message(String message) {
        this.setMessage(message);
        return this;
    }

    public Result<T> data(T data) {
        this.setData(data);
        return this;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
