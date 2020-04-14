package net.lcadsl.qintalker.push.frags.main;


import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.lcadsl.qintalker.common.app.Fragment;
import net.lcadsl.qintalker.common.app.PresenterFragment;
import net.lcadsl.qintalker.common.widget.EmptyView;
import net.lcadsl.qintalker.common.widget.PortraitView;
import net.lcadsl.qintalker.common.widget.recycler.RecyclerAdapter;
import net.lcadsl.qintalker.factory.model.db.Group;
import net.lcadsl.qintalker.factory.presenter.group.GroupsContract;
import net.lcadsl.qintalker.factory.presenter.group.GroupsPresenter;
import net.lcadsl.qintalker.push.R;
import net.lcadsl.qintalker.push.activities.MessageActivity;

import butterknife.BindView;


public class GroupFragment extends PresenterFragment<GroupsContract.Presenter>
        implements GroupsContract.View {

    @BindView(R.id.empty)
    EmptyView mEmptyView;

    @BindView(R.id.recycler)
    RecyclerView mRecycler;


    private RecyclerAdapter<Group> mAdapter;


    public GroupFragment() {
        // Required empty public constructor
    }


    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_group;
    }


    @Override
    protected void initWidget(View root) {
        super.initWidget(root);

        // 初始化Recycler
        mRecycler.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mRecycler.setAdapter(mAdapter = new RecyclerAdapter<Group>() {
            @Override
            protected int getItemViewType(int position, Group group) {
                // 返回cell的布局id
                return R.layout.cell_group_list;
            }

            @Override
            protected ViewHolder<Group> onCreateViewHolder(View root, int viewType) {
                return new GroupFragment.ViewHolder(root);
            }
        });

        // 点击事件监听
        mAdapter.setListener(new RecyclerAdapter.AdapterListenerImpl<Group>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder holder, Group group) {
                // 跳转到聊天界面
                MessageActivity.show(getContext(), group);
            }
        });

        // 初始化占位布局
        mEmptyView.bind(mRecycler);
        setPlaceHolderView(mEmptyView);

    }

    @Override
    protected void onFirstInit(){
        super.onFirstInit();
        mPresenter.start();
    }

    @Override
    protected GroupsContract.Presenter initPresenter() {
        return new GroupsPresenter(this);
    }

    @Override
    public RecyclerAdapter<Group> getRecyclerAdapter() {
        return mAdapter;
    }

    @Override
    public void onAdapterDataChanged() {
        mPlaceHolderView.triggerOkOrEmpty(mAdapter.getItemCount() > 0);
    }


    class ViewHolder extends RecyclerAdapter.ViewHolder<Group> {
        @BindView(R.id.im_portrait)
        PortraitView mPortraitView;

        @BindView(R.id.txt_name)
        TextView mName;

        @BindView(R.id.txt_desc)
        TextView mDesc;

        @BindView(R.id.txt_member)
        TextView mMember;


        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Group group) {
            mPortraitView.setup(Glide.with(GroupFragment.this), group.getPicture());
            mName.setText(group.getName());
            mDesc.setText(group.getDesc());

            if (group.holder != null && group.holder instanceof String) {
                mMember.setText((String) group.holder);
            } else {
                mMember.setText("");
            }
        }

    }

}
