package net.lcadsl.qintalker.push.frags.user;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yalantis.ucrop.UCrop;

import net.lcadsl.qintalker.common.app.Application;
import net.lcadsl.qintalker.common.app.Fragment;
import net.lcadsl.qintalker.common.app.PresenterFragment;
import net.lcadsl.qintalker.common.widget.PortraitView;
import net.lcadsl.qintalker.factory.Factory;
import net.lcadsl.qintalker.factory.net.UploadHelper;
import net.lcadsl.qintalker.factory.presenter.user.UpdateInfoContract;
import net.lcadsl.qintalker.factory.presenter.user.UpdateInfoPresenter;
import net.lcadsl.qintalker.push.R;
import net.lcadsl.qintalker.push.activities.MainActivity;
import net.lcadsl.qintalker.push.frags.media.GalleryFragment;
import net.qiujuer.genius.ui.widget.Loading;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * 用户更新信息的界面
 */
public class UpdateInfoFragment extends PresenterFragment<UpdateInfoContract.Presenter>
        implements UpdateInfoContract.View{
    @BindView(R.id.im_sex)
    ImageView mSex;

    @BindView(R.id.edit_desc)
    EditText mDesc;

    @BindView(R.id.im_portrait)
    PortraitView mPortrait;

    @BindView(R.id.loading)
    Loading mLoading;

    @BindView(R.id.btn_submit)
    Button mSubmit;

    //头像的本地路径
    private String mPortraitPath;

    private boolean isMan=true;


    public UpdateInfoFragment() {
        // Required empty public constructor
    }


    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_update_info;
    }

    @OnClick(R.id.im_portrait)
    void onPortraitClick() {
        new GalleryFragment()
                .setListener(new GalleryFragment.OnSelectedListener() {
                    @Override
                    public void onSelectedImage(String path) {
                        UCrop.Options options = new UCrop.Options();
                        //设置图片处理的格式
                        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
                        //设置压缩后的图片精度
                        options.setCompressionQuality(96);

                        //得到头像缓存地址
                        File dPath = Application.getPortraitTmpFile();


                        UCrop.of(Uri.fromFile(new File(path)), Uri.fromFile(dPath))
                                .withAspectRatio(1, 1)//比例
                                .withMaxResultSize(520, 520)//返回最大的尺寸
                                .withOptions(options)//相关参数
                                .start(getActivity());
                    }
                }).show(getChildFragmentManager(), GalleryFragment.class.getName());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //收到从activity传递过来的回调，然后取出其中的值进行图片加载
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
            if (resultUri != null) {
                loadPortrait(resultUri);
            }

        } else if (resultCode == UCrop.RESULT_ERROR) {
            Application.showToast(R.string.data_rsp_error_unknown);
        }
    }

    //加载uri到当前头像中
    private void loadPortrait(Uri uri) {
        //得到头像地址
        mPortraitPath=uri.getPath();


        Glide.with(this)
                .load(uri)
                .asBitmap()
                .centerCrop()
                .into(mPortrait);


    }
    @OnClick(R.id.im_sex)
    void onSexClick(){
        //性别图标点击时触发
        isMan=!isMan;//改变性别

        Drawable drawable=getResources().getDrawable(isMan?R.drawable.ic_sex_man:R.drawable.ic_sex_woman);
        mSex.setImageDrawable(drawable);
        //设置背景的层级，切换颜色
        mSex.getBackground().setLevel(isMan?0:1);
    }


    @OnClick(R.id.btn_submit)
    void onSubmitClick() {
        String desc = mDesc.getText().toString();
        // 调用P层进行注册
        mPresenter.update(mPortraitPath,desc,isMan);
    }


    @Override
    public void showError(int str) {
        super.showError(str);
        // 当需要显示错误的时候触发，一定是结束了

        // 停止Loading
        mLoading.stop();
        // 让控件可以输入
        mDesc.setEnabled(true);
        mPortrait.setEnabled(true);
        mSex.setEnabled(true);
        // 提交按钮可以继续点击
        mSubmit.setEnabled(true);
    }

    @Override
    public void showLoading() {
        super.showLoading();

        // 正在进行时，正在进行注册，界面不可操作
        // 开始Loading
        mLoading.start();
        // 让控件不可以输入
        mDesc.setEnabled(false);
        mPortrait.setEnabled(false);
        mSex.setEnabled(false);
        // 提交按钮不可以继续点击
        mSubmit.setEnabled(false);
    }


    @Override
    public void updateSucceed() {
        //更新成功跳转到主界面
        MainActivity.show(getContext());
        getActivity().finish();
    }


    @Override
    protected UpdateInfoContract.Presenter initPresenter() {
        //初始化Presenter
        return new UpdateInfoPresenter(this);
    }


}
