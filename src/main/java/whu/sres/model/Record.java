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
    private int id;
    // 时间戳
    @SerializedName("date")
    private int timestamp;
    private String room;
    private int state;
    private int start;
    private int end;
    private User user;
}
