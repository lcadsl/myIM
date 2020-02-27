package net.lcadsl.qintalker.factory.data;


import android.support.annotation.StringRes;

/**
 * 数据源接口定义
 */
public interface DataSource {

    /**
     * 同时包括成功与失败的回调接口
     * @param <T> 泛型
     */
    interface Callback<T> extends SuccessCallback<T>,FailedCallback{

    }


    interface SuccessCallback<T>{
        //数据加载成功，网络请求成功
        void onDataLoaded(T t);

    }


    interface FailedCallback{
        //数据加载失败，网络请求失败
        void onDataNotAvailable(@StringRes int strRes);

    }
}
