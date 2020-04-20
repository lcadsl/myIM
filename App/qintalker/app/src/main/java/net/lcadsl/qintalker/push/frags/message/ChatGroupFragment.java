package net.lcadsl.qintalker.push.frags.message;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.lcadsl.qintalker.factory.model.db.Group;
import net.lcadsl.qintalker.factory.model.db.view.MemberUserModel;
import net.lcadsl.qintalker.factory.presenter.message.ChatContract;
import net.lcadsl.qintalker.factory.presenter.message.ChatGroupPresenter;
import net.lcadsl.qintalker.push.R;

import java.util.List;

/**
 * 群聊天界面实现
 */
public class ChatGroupFragment extends ChatFragment<Group>
implements ChatContract.GroupView {


    public ChatGroupFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getHeaderLayoutId() {
        return R.layout.lay_chat_header_group;
    }



    @Override
    protected ChatContract.Presenter initPresenter() {
        return new ChatGroupPresenter(this,mReceiverId);
    }

    @Override
    public void onInit(Group group) {

    }



    @Override
    public void onInitGroupMembers(List<MemberUserModel> members, int moreCount) {

    }


    @Override
    public void showAdminOption(boolean isAdmin) {

    }


}
