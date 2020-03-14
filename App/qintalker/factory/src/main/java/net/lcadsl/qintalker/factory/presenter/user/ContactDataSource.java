package net.lcadsl.qintalker.factory.presenter.user;

import net.lcadsl.qintalker.factory.data.DataSource;
import net.lcadsl.qintalker.factory.model.db.User;

import java.util.List;

/**
 * 联系人数据源
 */
public interface ContactDataSource {
    /**
     * 对数据进行加载的一个职责
     * @param callback  加载成功后返回的CallBack
     */
    void load(DataSource.SucceedCallback<List<User>> callback);

    /**
     * 销毁操作
     */
    void dispose();
}
