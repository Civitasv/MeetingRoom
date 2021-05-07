package whu.sres.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;


@Data
public class Record {
    private Integer id;
    // 时间戳
    @SerializedName("date")
    private Long timestamp;
    private String room;
    private Integer state;
    private Long start;
    private Long end;
    private String userId;
    private User user;
    private String phone;
    private String realUser;
}
