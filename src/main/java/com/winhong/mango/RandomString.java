package com.winhong.mango;

import java.util.Random;

/**
 * 产生随机字符串
 *
 * @author XiongNeng
 * @version 1.0
 * @since 2014/4/17
 */
public class RandomString {

    private static char[] symbols;

    static {
        StringBuilder tmp = new StringBuilder();
        for (char ch = '0'; ch <= '9'; ++ch)
            tmp.append(ch);
        for (char ch = 'a'; ch <= 'z'; ++ch)
            tmp.append(ch);
        symbols = tmp.toString().toCharArray();
    }

    private final Random random = new Random();

    private final char[] buf;

    public RandomString(int length) {
        if (length < 1)
            throw new IllegalArgumentException("length < 1: " + length);
        buf = new char[length];
    }

    /**
     * 产生随机的电子邮件
     * @return
     */
    public String randomEmail() {
        buf[0] = 's';
        for (int idx = 1; idx < buf.length; ++idx)
            buf[idx] = symbols[random.nextInt(symbols.length)];
        return new String(buf) + "@163.com";
    }

    /**
     * 参数随机名称
     * @return
     */
    public String randomName() {
        buf[0] = 'a';
        for (int idx = 1; idx < buf.length; ++idx)
            buf[idx] = symbols[random.nextInt(symbols.length)];
        return new String(buf);
    }
}
