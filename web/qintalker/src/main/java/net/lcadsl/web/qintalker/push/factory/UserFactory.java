package net.lcadsl.web.qintalker.push.factory;

import net.lcadsl.web.qintalker.push.bean.db.User;
import net.lcadsl.web.qintalker.push.utils.Hib;
import net.lcadsl.web.qintalker.push.utils.TextUtil;
import org.hibernate.Session;

import java.util.UUID;

/**
 *
 */
public class UserFactory {
    public static User findByPhone(String phone) {
        return Hib.query(session -> (User) session
                .createQuery("from User where phone=:inPhone")
                .setParameter("inPhone", phone)
                .uniqueResult());
    }


    public static User findByName(String name) {
        return Hib.query(session -> (User) session
                .createQuery("from User where name=:name")
                .setParameter("name", name)
                .uniqueResult());
    }

    /**
     * 用户注册
     * 注册的操作需要写入数据库，并返回数据库中的User信息
     *
     * @param account  账户
     * @param password 密码
     * @param name     用户名
     * @return User
     */
    public static User register(String account, String password, String name) {
        //去除账户中的首位空格
        account = account.trim();
        //处理密码
        password = encodePassword(password);

        User user = createUser(account, password, name);
        if (user!=null){
            user=login(user);
        }
        return user;
    }

    /**
     * 注册部分新建用户逻辑
     * @param account   手机号
     * @param password  密码（加密）
     * @param name  用户名
     * @return 返回一个用户
     */
    private static User createUser(String account, String password, String name) {
        User user = new User();
        user.setName(name);
        user.setPassword(password);
        //账户就是手机号
        user.setPhone(account);

        //数据库存储
        return Hib.query(session -> (User) session.save(user));
    }

    /**
     * 把一个User进行登录操作
     * 本质上是给User表添加Token项
     * @param user User
     * @return User
     */
    private static User login(User user){
        //使用一个随机uuid值充当token
        String newToken= UUID.randomUUID().toString();
        //进行一次base64
        newToken=TextUtil.encodeBase64(newToken);
        user.setToken(newToken);
        return Hib.query(session -> {session.saveOrUpdate(user);return user;});
    }




    private static String encodePassword(String password) {
        //密码去除首位空格
        password = password.trim();
        //进行md5加密
        password = TextUtil.getMD5(password);
        //再进行一次对称的BASE64加密
        return TextUtil.encodeBase64(password);
    }
}
