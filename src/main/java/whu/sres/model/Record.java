package whu.sres.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TreeSet;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import whu.sres.util.CommonUtil;
import whu.sres.util.ConnectionUtil;
import whu.sres.util.SystemDate;

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
