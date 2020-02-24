package net.lcadsl.qintalker.push.frags.account;


import android.content.Context;

import net.lcadsl.qintalker.common.app.Fragment;
import net.lcadsl.qintalker.push.R;

/**
 * 注册的界面
 */
public class RegisterFragment extends Fragment {
    private AccountTrigger mAccountTrigger;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //拿到activity的引用
        mAccountTrigger = (AccountTrigger) context;
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_register;
    }

}
