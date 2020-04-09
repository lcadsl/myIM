package net.lcadsl.qintalker.factory.presenter.group;

import net.lcadsl.qintalker.factory.presenter.BaseRecyclerPresenter;

/**
 * 群创建界面的Presenter
 */
public class GroupCreatePresenter extends BaseRecyclerPresenter<GroupCreateContract.ViewModel, GroupCreateContract.View>
        implements GroupCreateContract.Presenter {


    public GroupCreatePresenter(GroupCreateContract.View view) {
        super(view);
    }

    @Override
    public void create(String name, String desc, String picture) {

    }

    @Override
    public void changeSelect(GroupCreateContract.ViewModel model, boolean isSelected) {

    }
}
