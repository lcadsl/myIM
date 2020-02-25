package net.lcadsl.qintalker.factory.presenter;

import android.support.annotation.StringRes;


/**
 * 公共基本契约
 */

public interface BaseContract {

    interface View<T extends Presenter> {


        //公共的显示字符串错误
        void showError(@StringRes int str);

        //公共的显示进度条
        void showLoading();

        //支持设置一个Presenter
        void setPresenter(T presenter);
    }


    interface Presenter {


        //公用的的开始方法
        void start();

        //公用的销毁
        void destroy();
    }
}
