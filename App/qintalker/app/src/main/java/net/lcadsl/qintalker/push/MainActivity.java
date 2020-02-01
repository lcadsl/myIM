package net.lcadsl.qintalker.push;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import net.lcadsl.qintalker.common.Common;
import net.lcadsl.qintalker.common.app.Activity;

import butterknife.BindView;

public class MainActivity extends Activity {
    @BindView(R.id.txt_test)
    TextView mTestText;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }


    @Override
    protected void initWidge() {
        super.initWidge();
        mTestText.setText("Test Hello.");
    }
}
