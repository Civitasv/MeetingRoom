import static org.junit.Assert.*;

import java.util.TreeSet;

import org.junit.Test;
import whu.sres.service.impl.TokenServiceImpl;
import whu.sres.util.CommonUtil;


public class TestCommonUtil {

    @Test
    public void testPeriod2seq() {
        String startTime = "8:00";
        String endTime = "8:30";
        TreeSet set = CommonUtil.period2seq(startTime, endTime);
        System.out.println(set);
    }

    @Test
    public void testAddSet() {
        TreeSet set1 = CommonUtil.period2seq("7:30", "8:30");
        TreeSet set2 = CommonUtil.period2seq("8:00", "9:30");
        System.out.println("set2: " + set2);
        set2.addAll(set1);
        System.out.println("set1: " + set1);
        System.out.println("merge: " + set2);
        System.out.println("true: " + set2.contains(2));
        System.out.println(set1.contains(set2));
    }

    @Test
    public void testInt2Str() {
        int i = 10;
        String str = CommonUtil.int2String(i);
        System.out.println(str);
    }


    @Test
    public void testIndex2StartTime() {
        String i = "31";
        String result = CommonUtil.index2StartTime(i);
        System.out.println(result);
    }

    @Test
    public void testJWT() {
        TokenServiceImpl tokenService = new TokenServiceImpl();
        String token = tokenService.getRefreshToken("sssssd").get("refreshToken").toString();
        System.out.println(token);
        String token2 = tokenService.getRefreshToken("sssssd").get("refreshToken").toString();
        System.out.println(token2);

        System.out.println(tokenService.getUserIdFromToken("token"));
        System.out.println(tokenService.getUserIdFromToken(token2));
    }
}
