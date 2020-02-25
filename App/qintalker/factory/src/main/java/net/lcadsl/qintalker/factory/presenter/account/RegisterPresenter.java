package net.lcadsl.qintalker.factory.presenter.account;

import android.text.TextUtils;

import net.lcadsl.qintalker.common.Common;
import net.lcadsl.qintalker.factory.data.helper.AccountHelper;
import net.lcadsl.qintalker.factory.model.api.RegisterModel;
import net.lcadsl.qintalker.factory.presenter.BasePresenter;

import java.util.regex.Pattern;

public class RegisterPresenter extends BasePresenter<RegisterContract.View> implements RegisterContract.Presenter {
    public RegisterPresenter(RegisterContract.View view) {
        super(view);
    }

    @Override
    public void register(String phone, String name, String password) {
        //调用开始，在start中默认启动loading
        start();


        if (!checkMobile(phone)) {
            //提示
        } else if (name.length() < 2) {
            //姓名要大与两位
        } else if (password.length() < 2) {
            //密码要大于6位
        } else {
            //进行网络请求

            //构造Model，进行请求调用
            RegisterModel model=new RegisterModel(phone,password,name);
            AccountHelper.register(model);
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

}
