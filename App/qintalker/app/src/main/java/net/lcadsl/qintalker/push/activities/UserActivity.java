package net.lcadsl.qintalker.push.activities;



import android.content.Intent;

import net.lcadsl.qintalker.common.app.Activity;
import net.lcadsl.qintalker.common.app.Fragment;
import net.lcadsl.qintalker.push.R;
import net.lcadsl.qintalker.push.frags.user.UpdateInfoFragment;

public class UserActivity extends Activity {
    private Fragment mCurFragment;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_user;
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
