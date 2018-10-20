package com.tensquare.usertest;

import sun.misc.BASE64Encoder;

import java.security.MessageDigest;

/**
 * @auther 德哲
 * @date 2018/10/20 12:12.
 */
public class Md5Test {

    /**
     * md5加密（不可逆）
     *    它加密之后生成的字节数组转成字符串，可能是由任意字符组成。（它就包含了可见字符和不可见字符）
     *    我们需要把这个MD5生成的这个字节码数组转成字符串，转成成全是由可见字符组成的
     *
     * Base64编码：基于64个字符编码，它是可逆的。
     * 64个字符分别是：
     *  0-9 a-z A-Z  + /   64
     *
     *  原始字符：s12
     *  base64编码后字符：czEy
     *  先把s12转成ascii码对应的数字，然后再把数字转成二进制
     *
     *  解密后， 有等号是因为 它不超出这64位字之外了才会出现
     *  gdyb21LQTcIANtvYMT7QVQ==
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        String password = "1234";
        MessageDigest md5 = MessageDigest.getInstance("md5");
        byte[] by = md5.digest(password.getBytes());
        BASE64Encoder encoder = new BASE64Encoder();
        String str = encoder.encode(by);
        System.out.println(str);
    }

}
