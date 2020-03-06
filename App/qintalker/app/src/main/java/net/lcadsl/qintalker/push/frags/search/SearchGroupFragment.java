package net.lcadsl.qintalker.push.frags.search;


import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.lcadsl.qintalker.common.app.Fragment;
import net.lcadsl.qintalker.push.R;
import net.lcadsl.qintalker.push.activities.SearchActivity;

/**
 * 搜索群的界面实现
 */
public class SearchGroupFragment extends Fragment
implements SearchActivity.SearchFragment {


    public SearchGroupFragment() {
        // Required empty public constructor
    }


    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_search_group;
    }

    @Override
    public void search(String content) {

    }
}
