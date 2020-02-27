package net.lcadsl.qintalker.factory.persistence;

public class Account {
    //设备的推送id
    private static String pushId="test";

    public static void setPushId(String pushId){
        Account.pushId=pushId;
    }

    public static String getPushId(){
        return pushId;
    }
}
