package net.lcadsl.qintalker.push;


import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;

import net.lcadsl.qintalker.common.app.Activity;
import net.lcadsl.qintalker.common.widget.PortraitView;
import net.lcadsl.qintalker.push.frags.main.ActiveFragment;
import net.lcadsl.qintalker.push.frags.main.GroupFragment;

import butterknife.BindView;
import butterknife.OnClick;


public class MainActivity extends Activity implements BottomNavigationView.OnNavigationItemSelectedListener {

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


    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }


    @Override
    protected void initWidget() {
        super.initWidget();

        mNavigation.setOnNavigationItemSelectedListener(this);


//顶部导航栏图片裁剪
        Glide.with(this)
                .load(R.drawable.bg_src_blue)
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
    }

    @OnClick(R.id.im_search)
    void onSearchMenuClick() {

    }

    @OnClick(R.id.btn_action)
    void onActionClick() {

    }


    boolean isFirst = true;

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.action_home) {
            mTitle.setText(R.string.title_home);

            ActiveFragment activeFragment = new ActiveFragment();

            if (isFirst) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.lay_container, activeFragment)
                        .commit();
                isFirst = false;
            } else {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.lay_container, activeFragment)
                        .commit();
            }

        } else if (item.getItemId() == R.id.action_group) {
            mTitle.setText(R.string.title_group);


            GroupFragment groupFragment = new GroupFragment();

            if (isFirst) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.lay_container, groupFragment)
                        .commit();
                isFirst = false;
            } else {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.lay_container, groupFragment)
                        .commit();
            }
        }

        Log.e("TAG", "size:" + getSupportFragmentManager().getFragments().size());


        mTitle.setText(item.getTitle());


        return true;
    }
}
