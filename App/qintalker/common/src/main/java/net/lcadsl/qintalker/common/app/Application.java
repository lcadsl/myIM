package net.lcadsl.qintalker.common.app;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.StringRes;
import android.widget.Toast;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Application extends android.app.Application {

    private static Application instance;
    private List<Activity> activities = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;


        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(android.app.Activity activity, Bundle savedInstanceState) {
                activities.add(activity);
            }

            @Override
            public void onActivityStarted(android.app.Activity activity) {

            }

            @Override
            public void onActivityResumed(android.app.Activity activity) {

            }

            @Override
            public void onActivityPaused(android.app.Activity activity) {

            }

            @Override
            public void onActivityStopped(android.app.Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(android.app.Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(android.app.Activity activity) {
                activities.remove(activity);
            }



        });
    }

    // 退出所有
    public void finishAll(){
        for (Activity activity : activities) {
            activity.finish();
        }

        showAccountView(this);
    }

    protected void showAccountView(Context context){
    }

    public static Application getInstance() {
        return instance;
    }


    /**
     * 获取缓存文件夹地址
     *
     * @return 当前app的缓存文件夹地址
     */
    public static File getCacheDirFile() {
        return instance.getCacheDir();
    }

    public static File getPortraitTmpFile() {
        //得到头像目录的缓存地址
        File dir = new File(getCacheDirFile(), "portrait");
        dir.mkdirs();

        //删除旧的缓存文件
        File[] files = dir.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                file.delete();
            }
        }

        //返回一个当前时间戳的目录文件地址
        File path = new File(dir, SystemClock.uptimeMillis() + ".jpg");
        return path.getAbsoluteFile();
    }

    /**
     * 获取声音文件的本地地址
     *
     * @param isTmp 是否是缓存文件 True则返回的文件地址是一样的
     * @return 录音文件的地址
     */
    public static File getAudioTmpFile(boolean isTmp) {
        File dir = new File(getCacheDirFile(), "audio");
        //noinspection ResultOfMethodCallIgnored
        dir.mkdirs();
        File[] files = dir.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                //noinspection ResultOfMethodCallIgnored
                file.delete();
            }
        }

        // aar
        File path = new File(getCacheDirFile(), isTmp ? "tmp.mp3" : SystemClock.uptimeMillis() + ".mp3");
        return path.getAbsoluteFile();
    }

    /**
     * 显示一个Toast（消息提示框）
     *
     * @param msg 字符串
     */
    public static void showToast(final String msg) {
        //Toast只能在主线程中显示，需要进行线程转换
        //Toast.makeText(instance, msg, Toast.LENGTH_SHORT).show();

        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                Toast.makeText(instance, msg, Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * 显示一个Toast
     *
     * @param msgId 字符串的资源
     */
    public static void showToast(@StringRes int msgId) {
        showToast(instance.getString(msgId));
    }

}
