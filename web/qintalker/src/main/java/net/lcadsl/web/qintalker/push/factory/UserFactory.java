package net.lcadsl.web.qintalker.push.factory;

import com.google.common.base.Strings;
import net.lcadsl.web.qintalker.push.bean.db.User;
import net.lcadsl.web.qintalker.push.utils.Hib;
import net.lcadsl.web.qintalker.push.utils.TextUtil;

import java.util.List;
import java.util.UUID;


public class UserFactory {
    //通过Token找到User
    public static User findByToken(String token) {
        return Hib.query(session -> (User) session
                .createQuery("from User where token=:token")
                .setParameter("token", token)
                .uniqueResult());
    }


    //通过Phone找到User
    public static User findByPhone(String phone) {
        return Hib.query(session -> (User) session
                .createQuery("from User where phone=:inPhone")
                .setParameter("inPhone", phone)
                .uniqueResult());
    }

    //通过Name找到User
    public static User findByName(String name) {
        return Hib.query(session -> (User) session
                .createQuery("from User where name=:name")
                .setParameter("name", name)
                .uniqueResult());
    }

    /**
     * 更新用户信息到数据库
     * @param user User
     * @return User
     */
    public static User update(User user){
        return Hib.query(session -> {
            session.saveOrUpdate(user);
            return user;
        });
    }

    /**
     * 给当前账户绑定PushId
     *
     * @param user   自己的user
     * @param pushId 自己设备的PushId
     * @return 自己的user
     */
    public static User bindPushId(User user, String pushId) {
        if (Strings.isNullOrEmpty(pushId))
            return null;

        //查询是否有其他账户绑定这个设备
        //取消绑定，避免推送混乱
        //查询的列表不能包括我自己
        Hib.queryOnly(session -> {
            @SuppressWarnings("unchecked")
            List<User> userList = (List<User>) session
                    .createQuery("from User where lower(pushId)=:pushId and id!=:userId")
                    .setParameter("pushId", pushId.toLowerCase())
                    .setParameter("userId", user.getId())
                    .list();


            for (User u : userList) {
                //更新为null
                u.setPushId(null);
                session.saveOrUpdate(u);

            }
            return null;
        });

        if (pushId.equalsIgnoreCase(user.getPushId())) {
            //如果绑定的就是当前已经绑定的，那么不需要
            return user;
        } else {
            //如果当前账户绑定的Id和现在需要绑定的不同
            //则退出之前的设备，达到单点登录，
            //给之前的设备推送一条退出消息
            if (Strings.isNullOrEmpty(user.getPushId())) {
                //TODO 推送一个退出消息
            }
            //更新新的设备id
            user.setPushId(pushId);
            return update(user);
        }
    }

    /**
     * 使用账户和密码进行登录
     *
     * @param account  账户
     * @param password 密码
     * @return
     */
    public static User login(String account, String password) {
        final String accountStr = account.trim();
        //进行一次密码加密，与数据库比对
        final String encodePassword = encodePassword(password);
        User user = Hib.query(session -> (User) session
                .createQuery("from User where phone=:phone and password=:password")
                .setParameter("phone", accountStr)
                .setParameter("password", encodePassword)
                .uniqueResult());

        if (user != null) {
            //对user进行登录，更新Token
            user = login(user);
        }
        return user;
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
        if (user != null) {
            user = login(user);
        }
        return user;
    }

    /**
     * 注册部分新建用户逻辑
     *
     * @param account  手机号
     * @param password 密码（加密）
     * @param name     用户名
     * @return 返回一个用户
     */
    private static User createUser(String account, String password, String name) {
        User user = new User();
        user.setName(name);
        user.setPassword(password);
        //账户就是手机号
        user.setPhone(account);

        //数据库存储
        return Hib.query(session -> {
            session.save(user);
            return user;
        });
    }

    /**
     * 把一个User进行登录操作
     * 本质上是给User表添加Token项
     *
     * @param user User
     * @return User
     */
    private static User login(User user) {
        //使用一个随机uuid值充当token
        String newToken = UUID.randomUUID().toString();
        //进行一次base64
        newToken = TextUtil.encodeBase64(newToken);
        user.setToken(newToken);
        return update(user);
    }


    /**
     * 对密码进行加密
     *
     * @param password 原密码
     * @return 加密后的密码
     */
    private static String encodePassword(String password) {
        //密码去除首位空格
        password = password.trim();
        //进行md5加密
        password = TextUtil.getMD5(password);
        //再进行一次对称的BASE64加密
        return TextUtil.encodeBase64(password);
    }
}
