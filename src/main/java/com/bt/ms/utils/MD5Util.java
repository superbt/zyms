package com.bt.ms.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

@Component
public class MD5Util {

    private static final String salt ="123456a";

    public static String md5(String src){
     return DigestUtils.md5Hex(src);
    }

    public static String inputPwd2From(String inputPwd){
        String str = salt.charAt(0)+salt.charAt(2)+inputPwd+salt.charAt(5)+salt.charAt(4);
        return md5(str);
    }
    public static String form2Dbpwd(String fromPwd,String salt){
        String str = salt.charAt(0)+salt.charAt(2)+fromPwd+salt.charAt(5)+salt.charAt(4);
        return md5(str);
    }

    public static String inputPwd2DbPwd(String inputPwd,String salt){
        String fromPwd = inputPwd2From(inputPwd);
        String dbPwd = form2Dbpwd(fromPwd,salt);
        return dbPwd;
    }

    public static void main(String[] args) {
        System.out.println(inputPwd2From("123456"));
        //fa6dab334e8e78acde47050c812ee573
        System.out.println(form2Dbpwd("fa6dab334e8e78acde47050c812ee573","123456a"));
        System.out.println(inputPwd2DbPwd("123456","123456a"));
    }
}
