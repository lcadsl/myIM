package net.lcadsl.qintalker.factory.presenter.contact;

import net.lcadsl.qintalker.common.widget.recycler.RecyclerAdapter;
import net.lcadsl.qintalker.factory.model.db.User;
import net.lcadsl.qintalker.factory.presenter.BaseContract;

public interface ContactContract {
    //不需要额外定义，开始就是调用start
    interface Presenter extends BaseContract.Presenter{

    }

    //在基类中完成
    interface View extends BaseContract.RecyclerView<Presenter,User>{

    }
}
