package com.devotion.dao.utils;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ByteArrayInputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;

/**
 * 功能描述： Utils工具类
 */
public class Utils {

    public static final int bufSize = 4096;

    /**
     * 获取缓冲区大小
     *
     * @param o 缓存对象
     * @return 缓冲区容量
     * @throws IOException IO异常
     */
    public static int size(final Object o) throws IOException {
        if (o == null) {
            return 0;
        }
        /** 缓存数据，向其内部缓冲区写入数据，缓冲区自动增长，当写入完成时可以从中提取数据 */
        ByteArrayOutputStream buf = new ByteArrayOutputStream(bufSize);
        ObjectOutputStream out = new ObjectOutputStream(buf);
        out.writeObject(o);
        out.flush();
        buf.close();

        return buf.size();
    }

    /**
     * 对象复制
     *
     * @param o 对象
     * @return 复制后的对象
     * @throws IOException            IO异常
     * @throws ClassNotFoundException 未找到类异常
     */
    public static Object copy(final Object o) throws IOException, ClassNotFoundException {
        if (o == null) {
            return null;
        }
        /** 缓存数据，向其内部缓冲区写入数据，缓冲区自动增长，当写入完成时可以从中提取数据 */
        ByteArrayOutputStream outBuf = new ByteArrayOutputStream(bufSize);
        ObjectOutput out = new ObjectOutputStream(outBuf);
        out.writeObject(o);
        out.flush();
        outBuf.close();
        /** 将字节数组转化为输入流 */
        ByteArrayInputStream inBuf = new ByteArrayInputStream(outBuf.toByteArray());
        ObjectInput in = new ObjectInputStream(inBuf);
        return in.readObject();
    }

    public static boolean stringIsNotEmpty(CharSequence str) {
        boolean notEmpty = true;
        if (null == str || str.toString().trim().length() == 0)
            notEmpty = false;
        return notEmpty;
    }

    /**
     * 私有构造函数Utils
     */
    private Utils() {

    }
}
