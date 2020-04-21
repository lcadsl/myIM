package net.lcadsl.qintalker.factory.presenter.group;

import net.lcadsl.qintalker.factory.model.db.view.MemberUserModel;
import net.lcadsl.qintalker.factory.presenter.BaseContract;

/**
 * 群成员的契约
 */
public interface GroupMembersContract {


    interface Presenter extends BaseContract.Presenter{
        //  刷新方法
        void refresh();
    }

    //界面
    interface View extends BaseContract.RecyclerView<Presenter, MemberUserModel>{
        String getmGroupId();
    }
}
