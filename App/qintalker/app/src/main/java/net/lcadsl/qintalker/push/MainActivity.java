package net.lcadsl.qintalker.push;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import net.lcadsl.qintalker.common.Common;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Common();
    }
}
