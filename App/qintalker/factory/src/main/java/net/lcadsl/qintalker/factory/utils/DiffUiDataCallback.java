package net.lcadsl.qintalker.factory.utils;

import android.support.v7.util.DiffUtil;

import java.util.List;

public class DiffUiDataCallback<T extends DiffUiDataCallback.UiDataDiffer<T>> extends DiffUtil.Callback {
    private List<T> mOldList,mNewList;

    public DiffUiDataCallback(List<T> mOldList,List<T> mNewList){
        this.mOldList=mOldList;
        this.mNewList=mNewList;
    }

    @Override
    public int getOldListSize() {
        //旧的数据大小
        return mOldList.size();
    }

    @Override
    public int getNewListSize() {
        //新的数据大小
        return mNewList.size();
    }

    //两个类是否就是同一个东西
    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        T beanOld=mOldList.get(oldItemPosition);
        T beanNew=mNewList.get(newItemPosition);

        return beanNew.isSame(beanOld);
    }

    //在经过相等判断后进一步判断是否有数据更改
    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        T beanOld=mOldList.get(oldItemPosition);
        T beanNew=mNewList.get(newItemPosition);

        return beanNew.isUiContentSame(beanOld);
    }


    //进行比较数据类型
    //范型的目的就是要和自己一个类型的进行比较
    public interface UiDataDiffer<T>{
        //传递一个旧的数据给你，问是否和标识的是同一个数据
        boolean isSame(T old);
        //你和旧的数据对比，内容是否相同
        boolean isUiContentSame(T old);

    }
}
