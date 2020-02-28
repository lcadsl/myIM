package net.lcadsl.qintalker.push;

import android.text.TextUtils;

import com.igexin.sdk.PushManager;

import net.lcadsl.qintalker.common.app.Application;
import net.lcadsl.qintalker.factory.Factory;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //调用Factory进行初始化
        Factory.setup();
        //推送进行初始化
        PushManager.getInstance().initialize(this, AppPushService.class);
        // 推送注册消息接收服务
        PushManager.getInstance().registerPushIntentService(this, AppMessageReceiverService.class);
    }
}
