package net.lcadsl.qintalker.push.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import net.lcadsl.qintalker.common.app.Activity;
import net.lcadsl.qintalker.common.app.Fragment;
import net.lcadsl.qintalker.factory.model.Author;
import net.lcadsl.qintalker.factory.model.db.Group;
import net.lcadsl.qintalker.push.R;
import net.lcadsl.qintalker.push.frags.message.ChatGroupFragment;
import net.lcadsl.qintalker.push.frags.message.ChatUserFragment;

public class MessageActivity extends Activity {
    //接收者id可以是群也可以是人
    public static final String KET_RECEIVER_ID = "KET_RECEIVER_ID";
    private static final String KET_RECEIVER_IS_GROUP = "KET_RECEIVER_IS_GROUP";

    private String mReceiverId;
    private boolean mIsGroup;

    /**
     * 显示人的聊天界面
     *
     * @param context 上下文
     * @param author  人的信息
     */
    public static void show(Context context, Author author) {
        if (author == null || context == null || TextUtils.isEmpty(author.getId()))
            return;
        Intent intent = new Intent(context, MessageActivity.class);
        intent.putExtra(KET_RECEIVER_ID, author.getId());
        intent.putExtra(KET_RECEIVER_IS_GROUP, false);


        context.startActivity(intent);
    }

    /**
     * 发起群聊
     *
     * @param context 上下文
     * @param group   群的信息
     */
    public static void show(Context context, Group group) {
        if (group == null || context == null || TextUtils.isEmpty(group.getId()))
            return;
        Intent intent = new Intent(context, MessageActivity.class);
        intent.putExtra(KET_RECEIVER_ID, group.getId());
        intent.putExtra(KET_RECEIVER_IS_GROUP, true);


        context.startActivity(intent);
    }


    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_message;
    }


    @Override
    protected boolean initArgs(Bundle bundle) {
        mReceiverId = bundle.getString(KET_RECEIVER_ID);
        mIsGroup = bundle.getBoolean(KET_RECEIVER_IS_GROUP);

        return !TextUtils.isEmpty(mReceiverId);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        setTitle("");
        Fragment fragment;
        if (mIsGroup)
            fragment = new ChatGroupFragment();
        else
            fragment = new ChatUserFragment();

        //从activity传递参数到Fragment中
        Bundle bundle=new Bundle();
        bundle.putString(KET_RECEIVER_ID,mReceiverId);
        fragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.lay_container,fragment)
                .commit();
    }
}
