package net.lcadsl.qintalker.common;

public class Common {

    /**
     * 一些不变的参数，
     * 通常用于一些配置
     */
    public interface Constance{
        //手机号校验的正则,11位手机号
        String REGEX_MOBILE="[1][3,4,5,7,8][0-9]{9}$";


        //基础的网络请求地址
        String API_URL="http://192.168.1.105:8888/api/";


        //云服务器网络请求地址
        //String API_URL="http://114.55.99.31:8888/api/";
    }
}
