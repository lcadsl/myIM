package net.lcadsl.qintalker.push.frags.message;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.lcadsl.qintalker.factory.model.db.Group;
import net.lcadsl.qintalker.factory.presenter.message.ChatContract;
import net.lcadsl.qintalker.push.R;

/**
 * 群聊天界面实现
 */
public class ChatGroupFragment extends ChatFragment<Group>
implements ChatContract.GroupView {


    public ChatGroupFragment() {
        // Required empty public constructor
    }




    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_chat_group;
    }

    @Override
    protected ChatContract.Presenter initPresenter() {
        return null;
    }

    @Override
    public void onInit(Group group) {

    }
}
