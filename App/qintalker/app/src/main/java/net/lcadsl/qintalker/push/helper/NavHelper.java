package net.lcadsl.qintalker.push.helper;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;


/**
 * 功能：完成对fragment的调度和重用问题
 * 实现fragment切换
 */

public class NavHelper<T> {
//    所有tab集合
    private final SparseArray<Tab<T>> tabs = new SparseArray<>();
    //    上下文
    private final Context context;
    private final int containerId;
    private final FragmentManager fragmentManager;
    private final OnTabChangedListener<T> listener;
//    存储当前选中的tab
    private Tab<T> currentTab;

    public NavHelper(Context context,
                     int containerId,
                     FragmentManager fragmentManager,
                     OnTabChangedListener<T> listener) {
        this.fragmentManager = fragmentManager;
        this.containerId = containerId;
        this.context = context;
        this.listener = listener;
    }

    /**
     * 添加tab
     * @param menuId tab对应的菜单id
     * @param tab
     */
    public NavHelper<T> add(int menuId,Tab<T> tab){
        tabs.put(menuId, tab);
        return this;
    }

    /**
     * 获取当前的tab
     * @return
     */
    public Tab<T> getCurrentTab(){
        return currentTab;
    }


    /**
     * 点击底部菜单操作
     *
     * @param menuId
     * @return
     */
    public boolean performClickMenu(int menuId) {
//        集合中寻找点击菜单对应的tab，
//        如果有就进行处理，返回true
        Tab<T> tab = tabs.get(menuId);
        if (tab != null){
            doSelect(tab);
            return true;

        }

        return false;
    }

//     进行tab选择操作
    private void doSelect(Tab<T> tab){
        Tab<T> oldTab = null;

        if (currentTab != null){
            oldTab = currentTab;
//            如果当前tab就是点击的tab
//            那么不做处理
            if (oldTab == tab){
                notifyTabReselect(tab);
                return;
            }
        }
//        赋值并调用切换方法
        currentTab = tab;
        doTabChanged(currentTab,oldTab);
    }

    /**
     * 进行Fragment调度操作
     * @param newTab
     * @param oldTab
     */
    private void doTabChanged(Tab<T> newTab,Tab<T> oldTab){
        FragmentTransaction ft = fragmentManager.beginTransaction();

        if (oldTab != null){
            if (oldTab.fragment != null){
//                从界面中移除，但还保留在缓存中
                ft.detach(oldTab.fragment);
            }
        }

        if (newTab != null){
            if (newTab.fragment == null){
//              首次新建
                Fragment fragment = Fragment.instantiate(context,newTab.clx.getName(),null);
//              缓存起来
                newTab.fragment = fragment;
//              提交到FragmentManger
                ft.add(containerId,fragment,newTab.clx.getName());
            }else {
//              从FragmentManger的缓存中重新加载到界面中
                ft.attach(newTab.fragment);
            }
        }
//      提交
        ft.commit();
//      通知回调
        notifyTabSelect(newTab,oldTab);
    }

    /***
     * 回调监听器
     * @param newTab
     * @param oldTab
     */
    private void notifyTabSelect(Tab<T> newTab,Tab<T> oldTab){
        if (listener != null){
            listener.onTabChanged(newTab, oldTab);
        }
    }


//    TODO 再次点击按钮进行刷新
    private void notifyTabReselect(Tab<T> tab){

    }


    public static class Tab<T> {
        public Tab(Class<?> clx,T extra){
            this.clx =clx;
            this.extra =extra;
        }

        //        fragment对应的class信息
        public Class<?> clx;
        //        定义范型，用户自己设定需要使用
        public T extra;
        //内部缓存对应的fragment
        //私有的
        Fragment fragment;
    }

    //    事件处理完成后回调的接口
    public interface OnTabChangedListener<T> {
        void onTabChanged(Tab<T> newTab, Tab<T> oldTab);
    }
}
