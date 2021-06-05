import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.DigestUtils;
import whu.sres.MyApplication;
import whu.sres.mapper.RecordMapper;
import whu.sres.mapper.UserMapper;
import whu.sres.mapper.UserRoleMapper;
import whu.sres.model.Record;
import whu.sres.model.User;
import whu.sres.model.UserRole;
import whu.sres.util.TimeUtil;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyApplication.class)
public class AddRole {
    @Autowired
    UserMapper mapper;
    @Autowired
    UserRoleMapper userRoleMapper;
    @Autowired
    RecordMapper recordMapper;

    @Test
    public void addRole() {
        List<User> users = mapper.getAll();
        for (User user : users) {
            // 先删除，再指定
            userRoleMapper.delete(new UserRole(user.getId(), 1));
            userRoleMapper.delete(new UserRole(user.getId(), 2));
            userRoleMapper.add(new UserRole(user.getId(), 1));
        }
    }

    @Test
    public void updatePassword() {
        List<User> users = mapper.getAll();
        for (User user : users) {
            String encryptPwd = DigestUtils.md5DigestAsHex("sres".getBytes(StandardCharsets.UTF_8));
            user.setPassword(encryptPwd);
            mapper.updatePwd(user);
        }
    }

    @Test
    public void addRecord() throws IOException {
        File file = new File("D:\\files\\资源与环境科学学院会议室预定系统\\main_BOOKED_RECORDS.txt");
        Files.lines(file.toPath())
                .forEach(item -> {
                    String[] data = item.split(",");
                    String date = data[0];
                    String room = data[1];
                    String state = data[3];
                    String phone = data[4];
                    try {
                        String user_id = fillString(Integer.parseInt(data[5]), 8);
                        String user = data[7];
                        String start = data[8];
                        String end = data[9];
                        if (mapper.getByUserId(user_id) == null)
                            return;
                        Record record = new Record();
                        long timestamp = TimeUtil.transformDateStringToTimestamp(date, "yyyy-MM-dd") / 1000;
                        record.setTimestamp(timestamp);
                        record.setRoom(room);
                        record.setState("Y".equals(state) ? 1 : 0);
                        String[] starts = start.split(":");
                        record.setStart(timestamp + Long.parseLong(starts[0]) * 60 * 60 + Long.parseLong(starts[1]) * 60);
                        String[] ends = end.split(":");
                        record.setEnd(timestamp + Long.parseLong(ends[0]) * 60 * 60 + Long.parseLong(ends[1]) * 60);
                        record.setUserId(user_id);
                        record.setPhone(phone);
                        record.setRealUser(user);
                        recordMapper.add(record);
                    } catch (Exception e) {
                        return;
                    }
                });
    }

    @Test
    public void updateUser() {
        List<User> users = mapper.getAll();
        for (User user : users) {
            if (user.getId().length() < 8) {
                String fill = fillString(Integer.parseInt(user.getId()), 8);
                mapper.updateId(user.getId(), fill);
            }
        }
    }

    /**
     * description: 使用 String.format 格式化数字，实现左侧补 0
     *
     * @param num   需要格式化的数字
     * @param digit 生成字符串长度（保留数字位数）
     * @return String
     * @version v1.0
     * @author w
     * @date 2019年7月19日 下午2:14:31
     */
    public String fillString(int num, int digit) {
        /**
         * 0：表示前面补0
         * digit：表示保留数字位数
         * d：表示参数为正数类型
         */
        return String.format("%0" + digit + "d", num);
    }

}
