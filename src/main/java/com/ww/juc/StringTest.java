package com.ww.juc;

/**
 * @author ：黑洞里的光
 * @date ：Created in 2021/8/3 17:14
 * @description：
 */
public class StringTest {
    public static String appendStr(String s){
        s+="bbb";
        return s;
    }

    public static void main(String[] args) {
        String aaa = new String("aaa");
        String s = StringTest.appendStr(aaa);
        System.out.println(aaa);
    }
}
