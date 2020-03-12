package net.lcadsl.qintalker.factory.data.helper;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import net.lcadsl.qintalker.factory.model.db.Session;
import net.lcadsl.qintalker.factory.model.db.Session_Table;
import net.lcadsl.qintalker.factory.model.db.User_Table;

/**
 * 会话辅助工具类
 */
class SessionHelper {
    public static Session findFromLocal(String id) {
        //从本地查询session
        return SQLite.select()
                .from(Session.class)
                .where(Session_Table.id.eq(id))
                .querySingle();
    }
}
