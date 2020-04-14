package net.lcadsl.qintalker.factory.presenter.group;

import android.support.v7.util.DiffUtil;

import net.lcadsl.qintalker.factory.data.group.GroupsDataSource;
import net.lcadsl.qintalker.factory.data.group.GroupsRepository;
import net.lcadsl.qintalker.factory.data.helper.GroupHelper;
import net.lcadsl.qintalker.factory.data.helper.UserHelper;
import net.lcadsl.qintalker.factory.model.db.Group;
import net.lcadsl.qintalker.factory.presenter.BaseSourcePresenter;
import net.lcadsl.qintalker.factory.utils.DiffUiDataCallback;

import java.util.List;

/**
 * 我的群组
 */
public class GroupsPresenter extends BaseSourcePresenter<Group, Group,
        GroupsDataSource,GroupsContract.View> implements GroupsContract.Presenter{
    public GroupsPresenter(GroupsContract.View view) {
        super(new GroupsRepository(), view);
    }

    @Override
    public void start() {
        super.start();

        GroupHelper.refreshGroups();
    }

    @Override
    public void onDataLoaded(List<Group> groups) {
        final GroupsContract.View view=getView();
        if (view==null)
            return;

        List<Group> old=view.getRecyclerAdapter().getItems();

        DiffUiDataCallback<Group> callback=new DiffUiDataCallback<>(old,groups);

        DiffUtil.DiffResult result=DiffUtil.calculateDiff(callback);
        //界面刷新
        refreshData(result,groups);
    }
}
