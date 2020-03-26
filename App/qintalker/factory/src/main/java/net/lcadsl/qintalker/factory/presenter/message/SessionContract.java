package net.lcadsl.qintalker.factory.presenter.message;

import net.lcadsl.qintalker.factory.model.db.Session;
import net.lcadsl.qintalker.factory.model.db.User;
import net.lcadsl.qintalker.factory.presenter.BaseContract;
import net.lcadsl.qintalker.factory.presenter.contact.ContactContract;

public interface SessionContract {
    //不需要额外定义，开始就是调用start
    interface Presenter extends BaseContract.Presenter{

    }

    //在基类中完成
    interface View extends BaseContract.RecyclerView<Presenter, Session>{

    }
}
