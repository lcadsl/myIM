package net.lcadsl.qintalker.push.frags.main;


import android.support.v4.app.LoaderManager;

import net.lcadsl.qintalker.common.app.Fragment;
import net.lcadsl.qintalker.common.widget.GalleyView;
import net.lcadsl.qintalker.push.R;

import butterknife.BindView;


public class ActiveFragment extends Fragment {
    @BindView(R.id.galleyView)
    GalleyView mGalley;


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

        mGalley.setup(getLoaderManager(), new GalleyView.SelectedChangeListener() {
            @Override
            public void onSelectedCountChanged(int count) {

            }
        });

    }
}

