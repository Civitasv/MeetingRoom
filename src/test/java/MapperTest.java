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
    }

    @Test
    public void testUser() {
        System.out.println(mapper.getAll());
    }
}
