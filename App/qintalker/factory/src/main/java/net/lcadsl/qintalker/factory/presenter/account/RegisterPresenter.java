package net.lcadsl.qintalker.factory.presenter.account;

import android.text.TextUtils;

import net.lcadsl.qintalker.common.Common;
import net.lcadsl.qintalker.factory.R;
import net.lcadsl.qintalker.factory.data.DataSource;
import net.lcadsl.qintalker.factory.data.helper.AccountHelper;
import net.lcadsl.qintalker.factory.model.api.account.RegisterModel;
import net.lcadsl.qintalker.factory.model.db.User;
import net.lcadsl.qintalker.factory.presenter.BasePresenter;
import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.regex.Pattern;

public class RegisterPresenter extends BasePresenter<RegisterContract.View>
        implements RegisterContract.Presenter, DataSource.Callback<User> {
    public RegisterPresenter(RegisterContract.View view) {
        super(view);
    }

    @Override
    public void register(String phone, String name, String password) {
        //调用开始，在start中默认启动loading
        start();
        //得到view接口
        RegisterContract.View view=getView();

        //校验
        if (!checkMobile(phone)) {
            //提示
            view.showError(R.string.data_account_register_invalid_parameter_mobile);
        } else if (name.length() < 2) {
            //姓名要大与两位
            view.showError(R.string.data_account_register_invalid_parameter_name);
        } else if (password.length() < 2) {
            //密码要大于6位
            view.showError(R.string.data_account_register_invalid_parameter_password);
        } else {
            //进行网络请求

            //构造Model，进行请求调用
            RegisterModel model=new RegisterModel(phone,password,name);
            //进行网络请求，并设置回送口为自己
            AccountHelper.register(model,this );
        }
    }

    /**
     * 检查手机号是否合法
     *
     * @param phone 手机号
     * @return 合法为true
     */
    @Override
    public boolean checkMobile(String phone) {
        return !TextUtils.isEmpty(phone)
                && Pattern.matches(Common.Constance.REGEX_MOBILE, phone);
    }

    @Override
    public void onDataLoaded(User user) {
        //当网络请求成功，注册好了，回送一个用户信息回来
        //通知界面注册成功
        final RegisterContract.View view=getView();
        if (view==null)
            return;
        //此时是从网络回送回来，并不保证处于主线程状态
        //强制执行在主线程中
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                //调用主界面注册成功
                view.registerSuccess();
            }
        });
    }

    @Override
    public void onDataNotAvailable(final int strRes) {
        //网络请求告知注册失败
        final RegisterContract.View view=getView();
        if (view==null)
            return;
        //此时是从网络回送回来，并不保证处于主线程状态
        //强制执行在主线程中
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.showError(strRes);
            }
        });
    }
}
