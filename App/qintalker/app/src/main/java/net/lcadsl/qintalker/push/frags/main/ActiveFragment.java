package net.lcadsl.qintalker.push.frags.main;


import net.lcadsl.qintalker.common.app.Fragment;
import net.lcadsl.qintalker.common.widget.GalleryView;
import net.lcadsl.qintalker.push.R;

import butterknife.BindView;


public class ActiveFragment extends Fragment {
    @BindView(R.id.galleryView)
    GalleryView mGalley;


    public ActiveFragment() {
        // Required empty public constructor
    }


    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_active;
    }

    @Override
    protected void initData() {
        super.initData();

        mGalley.setup(getLoaderManager(), new GalleryView.SelectedChangeListener() {
            @Override
            public void onSelectedCountChanged(int count) {

            }
        });

    }
}

