package net.lcadsl.qintalker.factory.presenter.group;

import net.lcadsl.qintalker.factory.Factory;
import net.lcadsl.qintalker.factory.data.helper.GroupHelper;
import net.lcadsl.qintalker.factory.model.db.view.MemberUserModel;
import net.lcadsl.qintalker.factory.presenter.BaseRecyclerPresenter;

import java.util.List;

public class GroupMembersPresenter extends BaseRecyclerPresenter<MemberUserModel, GroupMembersContract.View>
        implements GroupMembersContract.Presenter {

    public GroupMembersPresenter(GroupMembersContract.View view) {
        super(view);
    }

    @Override
    public void refresh() {
        //显示loading
        start();

        //异步加载
        Factory.runOnAsync(loader);
    }


    private Runnable loader = new Runnable() {
        @Override
        public void run() {
            GroupMembersContract.View view = getView();
            if (view == null)
                return;

            String groupId = view.getmGroupId();

            List<MemberUserModel> models = GroupHelper.getMemberUsers(groupId, -1);

            refreshData(models);
        }
    };
}
