package net.lcadsl.qintalker.push.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import net.lcadsl.qintalker.common.app.Activity;
import net.lcadsl.qintalker.factory.model.Author;
import net.lcadsl.qintalker.push.R;

public class MessageActivity extends Activity {

    /**
     * 显示人的聊天界面
     * @param context   上下文
     * @param author    人的信息
     */
    public static void show(Context context, Author author){

    }


    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_message;
    }
}
