package net.lcadsl.qintalker.factory.data.user;

import android.text.TextUtils;

import net.lcadsl.qintalker.factory.data.helper.DbHelper;
import net.lcadsl.qintalker.factory.model.card.UserCard;
import net.lcadsl.qintalker.factory.model.db.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class UserDispatcher implements UserCenter {
    private static UserCenter instance;
    //单线程池
    private final Executor executor = Executors.newSingleThreadExecutor();

    public static UserCenter instance() {
        if (instance == null) {
            synchronized (UserDispatcher.class) {
                if (instance == null)
                    instance = new UserDispatcher();
            }
        }
        return instance;
    }

    @Override
    public void dispatch(UserCard... cards) {
        if (cards == null || cards.length == 0)
            return;
        //丢到单线程池中
        executor.execute(new UserCardHandler(cards));
    }

    /**
     * 线程调度会触发run方法
     */
    private class UserCardHandler implements Runnable {
        private final UserCard[] cards;

        UserCardHandler(UserCard[] cards) {
            this.cards = cards;
        }

        @Override
        public void run() {
            //当被线程调度的时候触发
            List<User> users = new ArrayList<>();
            for (UserCard card : cards) {
                //进行过滤
                if (card == null || TextUtils.isEmpty(card.getId()))
                    continue;
                //添加
                users.add(card.build());
            }
            //进行数据库存储、分发通知，异步操作
            DbHelper.save(User.class, users.toArray(new User[0]));
        }
    }
}
