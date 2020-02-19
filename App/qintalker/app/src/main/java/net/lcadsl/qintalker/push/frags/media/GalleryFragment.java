package net.lcadsl.qintalker.push.frags.media;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import net.lcadsl.qintalker.common.widget.GalleryView;
import net.lcadsl.qintalker.push.R;
import net.qiujuer.genius.ui.Ui;

/**
 * 图片选择fragment
 */
public class GalleryFragment extends BottomSheetDialogFragment implements GalleryView.SelectedChangeListener {
    private GalleryView mGallery;
    private OnSelectedListener mlistener;

    public GalleryFragment() {

    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new TransStatusBottomSheetDialog(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 获取GalleryView
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        mGallery = (GalleryView) root.findViewById(R.id.galleryView);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        mGallery.setup(getLoaderManager(), this);
    }

    @Override
    public void onSelectedCountChanged(int count) {
        //如果选中一张图片
        if (count > 0) {
            //隐藏自己
            dismiss();
            if (mlistener != null) {
                //得到所有选中的图片的路径
                String[] paths = mGallery.getSelectedPath();
                //返回第一张
                mlistener.onSelectedImage(paths[0]);
                mlistener = null;
            }
        }
    }

    /**
     * 设置事件监听并返回自己
     *
     * @param listener OnSelectedListener
     * @return GalleryFragment
     */
    public GalleryFragment setListener(OnSelectedListener listener) {
        mlistener = listener;
        return this;
    }

    /**
     * 选中图片的监听器
     */
    public interface OnSelectedListener {
        void onSelectedImage(String path);
    }


    public static class TransStatusBottomSheetDialog extends BottomSheetDialog {

        public TransStatusBottomSheetDialog(@NonNull Context context) {
            super(context);
        }

        public TransStatusBottomSheetDialog(@NonNull Context context, int theme) {
            super(context, theme);
        }

        protected TransStatusBottomSheetDialog(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
            super(context, cancelable, cancelListener);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);


            final Window window = getWindow();
            if (window == null) {
                return;
            }

            int screenHeight = getContext().getResources().getDisplayMetrics().heightPixels;

            int statusHeight = (int) Ui.dipToPx(getContext().getResources(), 25);

            int dialogHeight = screenHeight - statusHeight;
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                    dialogHeight <= 0 ? ViewGroup.LayoutParams.MATCH_PARENT : dialogHeight);
        }
    }
}
