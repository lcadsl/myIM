package net.lcadsl.qintalker.factory.persistence;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import net.lcadsl.qintalker.factory.Factory;
import net.lcadsl.qintalker.factory.model.api.account.AccountRspModel;
import net.lcadsl.qintalker.factory.model.db.User;
import net.lcadsl.qintalker.factory.model.db.User_Table;

public class Account {
    private static final String KEY_PUSH_ID="KEY_PUSH_ID";
    private static final String KEY_IS_BIND="KEY_IS_BIND";
    private static final String KEY_USER_ID="KEY_USER_ID";
    private static final String KEY_TOKEN="KEY_TOKEN";
    private static final String KEY_ACCOUNT="KEY_ACCOUNT";


    //设备的推送id
    private static String pushId;
    //设备id是否已经绑定到了服务器
    private static boolean isBind;
    //登陆状态的token，用来接口请求
    private static String token;
    //登陆的用户id
    private static String userId;
    //登录的账户
    private static String account;

    /**
     * 存储到xml文件
     */
    private static void save(Context context){
        //获取数据持久化的sp
        SharedPreferences sp=context.getSharedPreferences(Account.class.getName(),
                Context.MODE_PRIVATE);
        //存储数据
        sp.edit()
                .putString(KEY_PUSH_ID,pushId)
                .putBoolean(KEY_IS_BIND,isBind)
                .apply();
    }

    /**
     * 进行数据加载
     * @param context   Context
     */
    public static void load(Context context){
        //获取数据持久化的sp
        SharedPreferences sp=context.getSharedPreferences(Account.class.getName(),
                Context.MODE_PRIVATE);
        pushId=sp.getString(KEY_PUSH_ID,"");
        isBind=sp.getBoolean(KEY_IS_BIND,false);
    }


    /**
     * 设置并存储设备的id
     * @param pushId 推送id
     */
    public static void setPushId(String pushId){
        Account.pushId=pushId;
        Account.save(Factory.app());
    }

    /**
     * 获取推送id
     * @return
     */
    public static String getPushId(){
        return pushId;
    }




    /**
     * 返回当前账户是否登录
     * @return  true已登录
     */
    public static boolean isLogin(){
        //用户id和token不为空
        return TextUtils.isEmpty(userId)
                &&TextUtils.isEmpty(token);
    }


    /**
     * 是否已经完善了用户信息
     * @return  TRUE 完成了
     */
    public static boolean isComplete(){
        //TODO
        return isLogin();
    }


    /**
     * 是否已经绑定到了服务器
     * @return True已经绑定
     */
    public static boolean isBind(){
        return isBind;
    }

    /**
     * 设置绑定状态
     *
     */
    public static void setBind(boolean isBind){
        Account.isBind=isBind;
        Account.save(Factory.app());
    }

    /**
     * 保存自己的信息到持久化xml中
     * @param model AccountRspModel
     */
    public static void login(AccountRspModel model){
        //存储当前账户token，id，方便从数据库中查询信息

        Account.token=model.getToken();
        Account.account=model.getAccount();
        Account.userId=model.getUser().getId();

        save(Factory.app());
    }

    /**
     * 获取当前登录的用户信息
     * @return  User
     */
    public static User getUser(){
        //如果为null，返回一个new的User，其次从数据库查询
        return TextUtils.isEmpty(userId)?new User(): SQLite.select()
                .from(User.class)
                .where(User_Table.id.eq(userId))
                .querySingle();
    }
}
