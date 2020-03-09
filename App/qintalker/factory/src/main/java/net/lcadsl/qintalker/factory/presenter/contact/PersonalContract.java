package net.lcadsl.qintalker.factory.presenter.contact;

import net.lcadsl.qintalker.factory.model.Author;
import net.lcadsl.qintalker.factory.model.db.User;
import net.lcadsl.qintalker.factory.presenter.BaseContract;

public interface PersonalContract {
    interface Presenter extends BaseContract.Presenter{
        //获取用户信息
        User getUserPersonal();
    }

    interface View extends BaseContract.View<Presenter>{
        String getUserId();
        //加载数据完成
        void onLoadDone(User user);
        //是否发起聊天
        void allowSayHello(boolean isAllow);

        void setFollowStatus(boolean isFollow);
    }
}
