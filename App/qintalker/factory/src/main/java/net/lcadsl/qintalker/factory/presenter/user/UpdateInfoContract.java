package net.lcadsl.qintalker.factory.presenter.user;

import net.lcadsl.qintalker.factory.presenter.BaseContract;

public interface UpdateInfoContract {
    interface Presenter extends BaseContract.Presenter{
        //更新
        void update(String photoFilePath,String desc,boolean isMan);
    }

    interface View extends BaseContract.View<Presenter>{
        //回调成功
        void updateSucceed();
    }
}
