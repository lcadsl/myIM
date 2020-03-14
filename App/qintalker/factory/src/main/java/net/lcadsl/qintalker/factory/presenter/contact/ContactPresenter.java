package net.lcadsl.qintalker.factory.presenter.contact;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;

import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.CursorResult;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

import net.lcadsl.qintalker.factory.data.DataSource;
import net.lcadsl.qintalker.factory.data.helper.UserHelper;
import net.lcadsl.qintalker.factory.model.card.UserCard;
import net.lcadsl.qintalker.factory.model.db.AppDatabase;
import net.lcadsl.qintalker.factory.model.db.User;
import net.lcadsl.qintalker.factory.model.db.User_Table;
import net.lcadsl.qintalker.factory.persistence.Account;
import net.lcadsl.qintalker.factory.presenter.BasePresenter;
import net.lcadsl.qintalker.factory.presenter.user.ContactDataSource;
import net.lcadsl.qintalker.factory.presenter.user.ContactRepository;
import net.lcadsl.qintalker.factory.utils.DiffUiDataCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * 联系人的Presenter实现
 */
public class ContactPresenter extends BasePresenter<ContactContract.View>
        implements ContactContract.Presenter,DataSource.SucceedCallback<List<User>> {

    private ContactDataSource mSource;

    public ContactPresenter(ContactContract.View view) {
        super(view);
        mSource=new ContactRepository();
    }


    @Override
    public void start() {
        super.start();

        //进行本地的数据加载，并添加监听
        mSource.load(this);


        //加载网络数据
        UserHelper.refreshContacts();


        //TODO 问题：关注后存储了数据库但是没有刷新联系人
        //TODO 问题：只有全局刷新，不能单条刷新
        //TODO 问题：本地刷新和网络刷新有可能冲突
        //TODO 问题：如何识别已经在数据库有的数据
    }

    private void diff(List<User> oldList, List<User> newList) {
        //进行数据对比
        DiffUtil.Callback callback = new DiffUiDataCallback<>(oldList, newList);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);
        //在对比完成后进行数据的赋值
        getView().getRecyclerAdapter().replace(newList);
        //尝试刷新界面
        result.dispatchUpdatesTo(getView().getRecyclerAdapter());
        getView().onAdapterDataChanged();
    }

    @Override
    public void onDataLoaded(List<User> users) {
        //无论怎么操作，数据变更，最终都会通知到这里
    }

    @Override
    public void destroy() {
        super.destroy();
        //当界面销毁的时候，应该把数据监听进行销毁
        mSource.dispose();
    }
}
