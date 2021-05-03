import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.DigestUtils;
import whu.sres.MyApplication;
import whu.sres.mapper.RecordMapper;
import whu.sres.mapper.UserMapper;
import whu.sres.model.User;

import java.nio.charset.StandardCharsets;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyApplication.class)
public class MapperTest {
    @Autowired
    UserMapper mapper;

    @Autowired
    RecordMapper meetingMapper;

    @Test
    public void test() {
        System.out.println(mapper.getByUserId("111"));
    }

    @Test
    public void test2() {
        // System.out.println(meetingMapper.getRecordByUserId("111"));
        User user = new User();
        user.setName("test");
        user.setId("XXX");
        user.setPhone("1102");
        user.setPassword("senmeng0921");
        String encryptPwd = DigestUtils.md5DigestAsHex(user.getPassword().getBytes(StandardCharsets.UTF_8));
        user.setPassword(encryptPwd);
        int res = mapper.add(user);
        System.out.println(res);
    }
}
