package net.lcadsl.qintalker.factory.presenter.contact;

import net.lcadsl.qintalker.factory.model.card.UserCard;
import net.lcadsl.qintalker.factory.presenter.BaseContract;

/**
 * 关注的接口定义
 */
public interface FollowContract {
    //任务调度者
    interface Presenter extends BaseContract.Presenter {
        //关注一个人
        void follow(String id);
    }

    interface View extends BaseContract.View<Presenter> {
        //成功的情况下返回一个用户信息UserCard
        void onFollowSucceed(UserCard userCard);
    }
}
