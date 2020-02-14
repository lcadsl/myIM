package net.lcadsl.qintalker.push.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.yalantis.ucrop.UCrop;

import net.lcadsl.qintalker.common.app.Activity;
import net.lcadsl.qintalker.common.app.Fragment;
import net.lcadsl.qintalker.push.R;
import net.lcadsl.qintalker.push.frags.account.UpdateInfoFragment;

public class AccountActivity extends Activity {
    private Fragment mCurFragment;

    /**
     * 账户activity显示的入口
     * @param context Context
     */
    public static void show(Context context){
        context.startActivity(new Intent(context,AccountActivity.class));
    }



    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_account;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        mCurFragment = new UpdateInfoFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.lay_container,mCurFragment)
                .commit();
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCurFragment.onActivityResult(requestCode,resultCode,data);
    }



}
