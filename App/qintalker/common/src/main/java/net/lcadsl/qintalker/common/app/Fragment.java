package net.lcadsl.qintalker.common.app;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.lcadsl.qintalker.common.widget.convention.PlaceHolderView;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class Fragment extends android.support.v4.app.Fragment {
    //保存root，以便复用
    protected View mRoot;
    protected Unbinder mRootUnBinder;
    protected PlaceHolderView mPlaceHolderView;
    //是否第一次初始化数据
    protected boolean mIsFirstInitData = true;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //初始化参数
        initArgs(getArguments());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (mRoot == null) {
            int layId = getContentLayoutId();
            View root = inflater.inflate(layId, container, false);
            initWidget(root);
            mRoot = root;
        } else {
            if (mRoot.getParent() != null) {
                ((ViewGroup) mRoot.getParent()).removeView(mRoot);
            }
        }


        return mRoot;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mIsFirstInitData) {
            //触发一次后就不会触发
            mIsFirstInitData = false;
            //首次初始化
            onFirstInit();
        }

        initData();
    }

    //初始化相关参数，初始化正确返回true
    protected void initArgs(Bundle bundle) {

    }


    //得到当前界面资源文件ID
    protected abstract int getContentLayoutId();


    protected void initWidget(View root) {
        mRootUnBinder = ButterKnife.bind(this, root);
    }

    protected void initData() {

    }

    /**
     * 当首次初始化数据时会调用
     */
    protected void onFirstInit() {

    }

    //返回键逻辑 返回true代表已处理返回逻辑，activity中不用finish，
    //返回false 代表没有处理，activity自己走自己的逻辑
    public boolean onBackPressed() {
        return false;
    }


    /**
     * 设置占位布局
     *
     * @param placeHolderView 继承了占位布局规范的View
     */
    public void setPlaceHolderView(PlaceHolderView placeHolderView) {
        this.mPlaceHolderView = placeHolderView;
    }
}
