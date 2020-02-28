package net.lcadsl.qintalker.push;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.Property;
import android.view.View;

import net.lcadsl.qintalker.common.app.Activity;
import net.lcadsl.qintalker.factory.persistence.Account;
import net.lcadsl.qintalker.push.activities.AccountActivity;
import net.lcadsl.qintalker.push.activities.MainActivity;
import net.lcadsl.qintalker.push.frags.assist.PermissionsFragment;
import net.qiujuer.genius.res.Resource;
import net.qiujuer.genius.ui.compat.UiCompat;

public class LaunchActivity extends Activity {

    private ColorDrawable mBgDrawable;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_launch;
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        //拿到根布局
        View root = findViewById(R.id.activity_launch);
        //获取颜色
        int color = UiCompat.getColor(getResources(), R.color.colorPrimary);
        //创建一个Drawable
        ColorDrawable drawable = new ColorDrawable(color);
        //设置给背景
        root.setBackground(drawable);
        mBgDrawable = drawable;
    }


    @Override
    protected void initData() {
        super.initData();

        //动画进入到50%等待pushId获取
        startAnim(0.5f, new Runnable() {
            @Override
            public void run() {
                //检查等待状态
                waitPushReceiverId();

            }
        });
    }


    /**
     * 等待个推框架对pushId设置好值
     */
    private void waitPushReceiverId() {
        if (Account.isLogin()) {
            //已经登录的情况下判断是否绑定
            //如果没有绑定则等待进行绑定
            if (Account.isBind()) {
                skip();
                return;
            }
        } else {
            //没有登录
            // 如果拿到PushId,没有登录的情况下不能绑定PushId
            if (!TextUtils.isEmpty(Account.getPushId())) {
                skip();
                return;
            }
        }


        //循环等待
        getWindow().getDecorView()
                .postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        waitPushReceiverId();
                    }
                }, 500);
    }

    /**
     * 在跳转前要把剩下的50%完成
     */
    private void skip() {
        startAnim(1f, new Runnable() {
            @Override
            public void run() {
                reallySkip();
            }
        });
    }

    private void reallySkip() {
        //权限检测，跳转
        if (PermissionsFragment.haveAll(this, getSupportFragmentManager())) {
            //检查跳转到主页还是登录
            if (Account.isLogin()) {
                MainActivity.show(this);
            } else {
                AccountActivity.show(this);
            }
            finish();
        }
    }

    /**
     * 给背景设置一个动画
     *
     * @param endProgress 动画的结束进度
     * @param endCallback 动画结束时触发
     */
    private void startAnim(float endProgress, Runnable endCallback) {
        //获取一个结束的颜色
        int finalColor = Resource.Color.WHITE;
        //运算当前进度的颜色
        ArgbEvaluator evaluator = new ArgbEvaluator();

        int endColor = (int) evaluator
                .evaluate(endProgress, mBgDrawable.getColor(), finalColor);

        //构建一个属性动画
        ValueAnimator valueAnimator = ObjectAnimator.ofObject(this, property, evaluator, endColor);
        valueAnimator.setDuration(1500);//时间1.5s
        valueAnimator.setIntValues(mBgDrawable.getColor(), endColor);//开始结束值
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //结束时触发
                endCallback.run();
            }
        });
        valueAnimator.start();
    }

    private final Property<LaunchActivity, Object> property = new Property<LaunchActivity, Object>(Object.class, "color") {

        @Override
        public void set(LaunchActivity object, Object value) {
            object.mBgDrawable.setColor((Integer) value);
        }


        @Override
        public Object get(LaunchActivity object) {
            return object.mBgDrawable.getColor();
        }
    };
}



