package net.lcadsl.qintalker.common.app;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public abstract class Activity extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        //在界面未初始化之前调用的初始化窗口
        initWindows();


        if(initArgs(getIntent().getExtras())){


        getContentLayoutId();
        initData();
        initWidget();
        }else {
            finish();
        }
    }
    //初始化窗口
    protected void initWindows(){

    }

    //初始化参数Bundle 初始化正确返回true
    protected boolean initArgs(Bundle bundle){
        return true;
    }
    //得到当前界面的资源文件id，返回id
    protected abstract int getContentLayoutId();


    //初始化控件

    protected void initWidget(){

    }
    //初始化数据
    protected void initData(){

    }

    @Override
    public boolean onSupportNavigateUp() {
        //当点击界面导航返回时，Finish当前界面
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        List <android.support.v4.app.Fragment> Fragments = getSupportFragmentManager().getFragments();

        super.onBackPressed();
        finish();
    }
}
