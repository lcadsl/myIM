package net.lcadsl.qintalker.factory.presenter.group;

import net.lcadsl.qintalker.factory.model.db.Group;
import net.lcadsl.qintalker.factory.model.db.User;
import net.lcadsl.qintalker.factory.presenter.BaseContract;

/**
 * 群列表契约
 */
public interface GroupsContract {
    //不需要额外定义，开始就是调用start
    interface Presenter extends BaseContract.Presenter{

    }

    //在基类中完成
    interface View extends BaseContract.RecyclerView<Presenter, Group>{

    }
}
