package net.lcadsl.qintalker.push.activities;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;

import net.lcadsl.qintalker.common.app.Activity;
import net.lcadsl.qintalker.common.widget.PortraitView;
import net.lcadsl.qintalker.push.R;
import net.lcadsl.qintalker.push.activities.AccountActivity;
import net.lcadsl.qintalker.push.frags.assist.PermissionsFragment;
import net.lcadsl.qintalker.push.frags.main.ActiveFragment;
import net.lcadsl.qintalker.push.frags.main.ContactFragment;
import net.lcadsl.qintalker.push.frags.main.GroupFragment;
import net.lcadsl.qintalker.push.helper.NavHelper;
import net.qiujuer.genius.ui.Ui;
import net.qiujuer.genius.ui.widget.FloatActionButton;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;


public class MainActivity extends Activity
        implements BottomNavigationView.OnNavigationItemSelectedListener,
        NavHelper.OnTabChangedListener<Integer> {

    @BindView(R.id.appbar)
    View mLayAppbar;

    @BindView(R.id.im_portrait)
    PortraitView mPortrait;

    @BindView(R.id.txt_title)
    TextView mTitle;

    @BindView(R.id.lay_container)
    FrameLayout mContainer;

    @BindView(R.id.navigation)
    BottomNavigationView mNavigation;

    @BindView(R.id.btn_action)
    FloatActionButton mAction;

    private NavHelper<Integer> mNavHelper;

    /**
     * MainActivity显示的入口
     * @param context 上下文
     */
    public static void show(Context context){
        context.startActivity(new Intent(context,MainActivity.class));
    }


    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }


    @Override
    protected void initWidget() {
        super.initWidget();
//        初始化工具类NavHelper，位于helper包
        mNavHelper = new NavHelper<>(this, R.id.lay_container,
                getSupportFragmentManager(), this);
        mNavHelper.add(R.id.action_home, new NavHelper.Tab<>(ActiveFragment.class, R.string.title_home))
                .add(R.id.action_group, new NavHelper.Tab<>(GroupFragment.class, R.string.title_group))
                .add(R.id.action_contact, new NavHelper.Tab<>(ContactFragment.class, R.string.title_contact));


//        对底部导航按钮的监听
        mNavigation.setOnNavigationItemSelectedListener(this);


//顶部导航栏图片裁剪
        Glide.with(this)
                .load(R.drawable.bg_src_colors)
                .centerCrop().into(new ViewTarget<View, GlideDrawable>(mLayAppbar) {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                this.view.setBackground(resource.getCurrent());
            }
        });


    }

    @Override
    protected void initData() {
        super.initData();
//从底部导航中接管menu，然后手动触发第一次点击
        Menu menu = mNavigation.getMenu();
//触发首次点击home
        menu.performIdentifierAction(R.id.action_home, 0);
    }

    @OnClick(R.id.im_search)
    void onSearchMenuClick() {

    }

    @OnClick(R.id.btn_action)
    void onActionClick() {
        AccountActivity.show(this);
    }


    /**
     * 当底部导航被点击时触发
     *
     * @param item
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        发送到工具类中
        return mNavHelper.performClickMenu(item.getItemId());
    }

    /**
     * 经过工具类NavHelper处理后回调的方法
     *
     * @param newTab
     * @param oldTab
     */
    @Override
    public void onTabChanged(NavHelper.Tab<Integer> newTab, NavHelper.Tab<Integer> oldTab) {
//        从额外字段extra中取出Title资源Id
        mTitle.setText(newTab.extra);

//        对浮动按钮进行隐藏与显示的动画，定义y轴移动值及旋转值
        float transY = 0;
        float rotation = 0;
        if (Objects.equals(newTab.extra, R.string.title_home)) {
            //主界面时隐藏
            transY = Ui.dipToPx(getResources(), 80);
        } else {
            if (Objects.equals(newTab.extra, R.string.title_group)) {
                //群组
                mAction.setImageResource(R.drawable.ic_group_add);
                rotation = -360;
            } else {
                //联系人
                mAction.setImageResource(R.drawable.ic_contact_add);
                rotation = 360;
            }
        }
//        开始动画，1旋转 2y轴位移 3弹性效果 4时间
        mAction.animate()
                .rotation(rotation)
                .translationY(transY)
                .setInterpolator(new AnticipateOvershootInterpolator(1))
                .setDuration(480)
                .start();
    }
}
