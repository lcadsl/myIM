package net.lcadsl.qintalker.factory.presenter.contact;

import android.support.annotation.NonNull;

import com.raizlabs.android.dbflow.sql.language.CursorResult;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

import net.lcadsl.qintalker.factory.model.db.User;
import net.lcadsl.qintalker.factory.model.db.User_Table;
import net.lcadsl.qintalker.factory.persistence.Account;
import net.lcadsl.qintalker.factory.presenter.BasePresenter;

import java.util.List;

/**
 * 联系人的Presenter实现
 */
public class ContactPresenter extends BasePresenter<ContactContract.View>
        implements ContactContract.Presenter {

    public ContactPresenter(ContactContract.View view) {
        super(view);
    }


    @Override
    public void start() {
        super.start();

        //TODO 加载数据

        SQLite.select()
                .from(User.class)
                .where(User_Table.isFollow.eq(true))
                .and(User_Table.id.notEq(Account.getUserId()))
                .orderBy(User_Table.name,true)
                .limit(100)
                .async()
                .queryListResultCallback(new QueryTransaction.QueryResultListCallback<User>() {
                    @Override
                    public void onListQueryResult(QueryTransaction transaction, @NonNull List<User> tResult) {

                        getView().getRecyclerAdapter().replace(tResult);
                        getView().onAdapterDataChanged();

                    }
                })
                .execute();
    }
}
