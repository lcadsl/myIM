package net.lcadsl.qintalker.push;


import net.lcadsl.qintalker.common.app.Activity;
import net.lcadsl.qintalker.push.activities.MainActivity;
import net.lcadsl.qintalker.push.frags.assist.PermissionsFragment;

public class LaunchActivity extends Activity {


    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_launch;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (PermissionsFragment.haveAll(this, getSupportFragmentManager())) {
            MainActivity.show(this);
            finish();
        }


    }
}



