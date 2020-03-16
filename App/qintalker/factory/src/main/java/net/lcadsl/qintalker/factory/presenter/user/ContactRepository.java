package net.lcadsl.qintalker.factory.presenter.user;

import android.support.annotation.NonNull;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

import net.lcadsl.qintalker.factory.data.BaseDbRepository;
import net.lcadsl.qintalker.factory.data.DataSource;
import net.lcadsl.qintalker.factory.data.helper.DbHelper;
import net.lcadsl.qintalker.factory.model.db.User;
import net.lcadsl.qintalker.factory.model.db.User_Table;
import net.lcadsl.qintalker.factory.persistence.Account;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * 联系人仓库
 *
 * @version 1.0.0
 */
public class ContactRepository extends BaseDbRepository<User> implements ContactDataSource {


    @Override
    public void load(DataSource.SucceedCallback<List<User>> callback) {
        super.load(callback);


        // 加载本地数据库数据
        SQLite.select()
                .from(User.class)
                .where(User_Table.isFollow.eq(true))
                .and(User_Table.id.notEq(Account.getUserId()))
                .orderBy(User_Table.name, true)
                .limit(100)
                .async()
                .queryListResultCallback(this)
                .execute();
    }

    @Override
    protected boolean isRequired(User user) {
        return user.isFollow() && !user.getId().equals(Account.getUserId());
    }


}

