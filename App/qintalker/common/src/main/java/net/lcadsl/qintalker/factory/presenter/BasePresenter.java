package net.lcadsl.qintalker.factory.presenter;

public class BasePresenter<T extends BaseContract.View> implements BaseContract.Presenter {

    protected T mView;

    public BasePresenter(T view) {
        setView(view);
    }

    /**
     * 设置一个View，子类可以复写
     *
     * @param view
     */
    protected void setView(T view) {
        this.mView = view;
    }

    /**
     * 给子类使用的获取View的操作 不允许复写
     *
     * @return View
     */
    protected final T getView() {
        return mView;
    }

    @Override
    public void start() {
        //开始的时候进行loading调用
        T view = mView;
        if (view != null) {
            view.showLoading();
        }
    }

    @Override
    public void destroy() {
        T view = mView;
        mView = null;
        if (view != null) {
            //把presenter设置为null
            view.setPresenter(null);
        }

    }
}
