package net.lcadsl.qintalker.common.app;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;





public abstract class Fragment extends android.support.v4.app.Fragment {
    protected View mRoot;

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
            //初始化当前根目录，但是不在创建时就添加到container里
            View root = inflater.inflate(layId, container, false);
            initWidget(root);
            mRoot = root;
        } else {
            if (mRoot.getParent() != null) {
                //把当前root从其父控件中移除
                ((ViewGroup) mRoot.getParent()).removeView(mRoot);
            }
        }

        return mRoot;
    }





    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //当view创建完成时初始化数据
        initData();
    }

    //初始化参数
    protected void initArgs(Bundle bundle){

    }

    @LayoutRes
    protected abstract int getContentLayoutId();
    //初始化控件
    protected void initWidget(View root){

    }
    //初始化数据
    protected void initData(){

    }

    //返回键逻辑,返回键触发时调用，返回true代表已处理返回逻辑，Activity不用finish
    //返回false代表我没有处理逻辑，activity自己走自己的逻辑
    public boolean onBackPressed(){
        return false;
    }
}
