package cn.schoolwow.dns.util;

import sun.nio.ch.FileChannelImpl;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 摘要算法工具类
 * */
public class DigestUtil {
    private static final char[] HEX_CHARS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    /**
     * MD5算法获取消息摘要
     * @param input 消息内容
     * */
    public static byte[] MD5(byte[] input) {
        try {
            return MessageDigest.getInstance("MD5").digest(input);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * MD5算法获取消息摘要
     * @param input 消息内容
     * */
    public static String MD5Hex(byte[] input) {
        return byte2hex(MD5(input),32);
    }

    /**
     * MD5算法获取消息摘要
     * @param s 消息内容
     * */
    public static String MD5Hex(String s) {
        return MD5Hex(s.getBytes());
    }

    /**
     * 获取文件MD5值
     * @param file 文件对象
     * */
    public static String MD5FileHex(File file) {
        try {
            return fileHex(file,MessageDigest.getInstance("MD5"),32);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * SHA1算法获取消息摘要
     * @param input 消息内容
     * */
    public static byte[] SHA1(byte[] input) {
        try {
            return MessageDigest.getInstance("SHA").digest(input);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * SHA1算法获取消息摘要
     * @param input 消息内容
     * */
    public static String SHA1Hex(byte[] input) {
        return byte2hex(SHA1(input),40);
    }

    /**
     * SHA1算法获取消息摘要
     * @param s 消息内容
     * */
    public static String SHA1Hex(String s) {
        return SHA1Hex(s.getBytes());
    }

    /**
     * 获取文件SHA1
     * @param file 文件对象
     * */
    public static String SHA1FileHex(File file) {
        try {
            return fileHex(file,MessageDigest.getInstance("SHA"),40);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * HMACSHA256算法获取消息摘要
     * @param key 密钥
     * @param input 消息内容
     * */
    public static byte[] HMACSHA256(byte[] key, byte[] input) {
        SecretKeySpec signingKey = new SecretKeySpec(key, "HmacSHA256");
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(signingKey);
            return mac.doFinal(input);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * HMACSHA256算法获取消息摘要
     * @param key 密钥
     * @param s 消息内容
     * */
    public static String HMACSHA256Hex(String key, String s) {
        return byte2hex(HMACSHA256(key.getBytes(),s.getBytes()),64);
    }

    /**
     * 字节转换成十六进制字符串
     * @param bytes 待转换字节数组
     * */
    public static String byte2hex(byte[] bytes, int length) {
        char[] chars = new char[length];

        for(int i = 0; i < chars.length; i += 2) {
            byte b = bytes[i / 2];
            chars[i] = HEX_CHARS[b >>> 4 & 15];
            chars[i + 1] = HEX_CHARS[b & 15];
        }
        return new String(chars);
    }

    /**
     * 根据对应算法获取文件指纹
     * @param file 文件对象
     * */
    private static String fileHex(File file, MessageDigest messageDigest, int length) {
        try (FileInputStream fis = new FileInputStream(file)){
            MappedByteBuffer mappedByteBuffer = fis.getChannel().map(FileChannel.MapMode.READ_ONLY,0,file.length());
            messageDigest.update(mappedByteBuffer);
            String result = byte2hex(messageDigest.digest(),length);
            Method m = FileChannelImpl.class.getDeclaredMethod("unmap", MappedByteBuffer.class);
            m.setAccessible(true);
            m.invoke(FileChannelImpl.class, mappedByteBuffer);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
