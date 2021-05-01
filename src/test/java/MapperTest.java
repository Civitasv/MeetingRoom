import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import whu.sres.MyApplication;
import whu.sres.mapper.MeetingMapper;
import whu.sres.mapper.UserMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyApplication.class)
public class MapperTest {
    @Autowired
    UserMapper mapper;

    @Autowired
    MeetingMapper meetingMapper;

    @Test
    public void test() {
        System.out.println(mapper.getByUserId("111"));
    }

    @Test
    public void test2() {
        System.out.println(meetingMapper.getRecordByUserId("111"));
    }
}
