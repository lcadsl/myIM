package net.lcadsl.qintalker.common.app;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import butterknife.ButterKnife;


public abstract class Activity extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        //在界面未初始化之前调用的初始化窗口
        initWidows();

        if(initArgs(getIntent().getExtras())) {
            getContentLayoutId();
            initWidge();
            initData();
        }else {
            finish();
        }

    }

    //初始化窗口
    protected void initWidows(){

    }

    //初始化相关参数，初始化正确返回true
    protected boolean initArgs(Bundle bundle){
        return true;
    }



    //得到当前界面资源文件ID
    protected abstract int getContentLayoutId();

    //初始化控件
    protected void initWidge(){
        ButterKnife.bind(this);

    }
    //初始化数据
    protected void initData(){

    }

    @Override
    public boolean onSupportNavigateUp() {

        //当点击界面导航返回时，finish当前界面
        finish();
        return super.onSupportNavigateUp();
    }


    @Override
    public void onBackPressed() {
        //list得到当前activity下所有Fragment
        @SuppressLint("RestrictedApi")
        List<android.support.v4.app.Fragment> fragments = getSupportFragmentManager().getFragments();
        //判断是否为空
        if (fragments != null && fragments.size()>0){
            for (Fragment fragment : fragments) {
                //判断是否为我们能够处理的fragment类型
                if(fragment instanceof net.lcadsl.qintalker.common.app.Fragment){
                    //判断是否拦截了返回按钮
                    if (((net.lcadsl.qintalker.common.app.Fragment) fragment).onBackPressed()){
                        //如果有直接return
                        return;
                    }
                }
            }
        }

        super.onBackPressed();
        finish();
    }
}
