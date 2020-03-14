package net.lcadsl.qintalker.factory.presenter;

import android.support.v7.util.DiffUtil;

import net.lcadsl.qintalker.common.widget.recycler.RecyclerAdapter;
import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.List;

/**
 * 对RecyclerView 进行Presenter封装
 * @param <ViewMode>
 * @param <View>
 */
public class BaseRecyclerPresenter<ViewMode,View extends BaseContract.RecyclerView> extends BasePresenter<View>{

    public BaseRecyclerPresenter(View view) {
        super(view);
    }

    /**
     * 刷新一堆数据到界面中
     * @param dataList  新数据
     */
    protected void refreshData(final List<ViewMode> dataList){
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                View view=getView();
                if (view==null)
                    return;

                //基本的更新数据并刷新界面
                RecyclerAdapter<ViewMode> adapter=view.getRecyclerAdapter();
                adapter.replace(dataList);
                view.onAdapterDataChanged();
            }
        });
    }

    /**
     * 刷新界面操作，该操作可以保证执行方法在主线程进行
     * @param diffResult    差异结果集
     * @param dataList      具体的新数据
     */
    protected void refreshData(final DiffUtil.DiffResult diffResult,final List<ViewMode> dataList){
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                //主线程运行
                refreshDataOnUiThread(diffResult, dataList);

            }
        });
    }


    private void refreshDataOnUiThread(final DiffUtil.DiffResult diffResult,final List<ViewMode> dataList){
        View view=getView();
        if (view==null)
            return;

        //基本的更新数据并刷新界面
        RecyclerAdapter<ViewMode> adapter=view.getRecyclerAdapter();
        //改变数据集合并不通知界面刷新
        adapter.getItems().clear();
        adapter.getItems().addAll(dataList);
        //通知界面刷新占位布局
        view.onAdapterDataChanged();

        //进行增量更新
        diffResult.dispatchUpdatesTo(adapter);
    }
}
