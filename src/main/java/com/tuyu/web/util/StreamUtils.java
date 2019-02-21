package com.tuyu.web.util;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author tuyu
 * @date 2/18/19
 * Talk is cheap, show me the code.
 */
@Slf4j
public final class StreamUtils {

    private StreamUtils() {
        throw new AssertionError("no StreamUtils instance for you!");
    }

    /**
     * 从输入流中读取字符串
     *
     * @param inputStream
     *
     * @return
     */
    public static String getString(InputStream inputStream) {
        if (inputStream == null) {
            return null;
        }
        try {
            int len = inputStream.available();
            byte[] bytes = new byte[len];
            inputStream.read(bytes);
            return new String(bytes);
        } catch (IOException e) {
            String msg = "读取流失败";
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

}
