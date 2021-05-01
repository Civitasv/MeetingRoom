package whu.sres.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class CharsetUtil {
    private final static Logger logger = LoggerFactory.getLogger(CharsetUtil.class);

    public static String ISO_8859_1ToUTF_8(String inStr) {
        return new String(inStr.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }
}
