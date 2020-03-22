package net.lcadsl.qintalker.factory.data.message;

import net.lcadsl.qintalker.factory.data.DataSource;
import net.lcadsl.qintalker.factory.data.DbDataSource;
import net.lcadsl.qintalker.factory.model.db.Message;

/**
 * 消息的数据源定义，他的实现是MessageRepository
 * 关注的对象是Message表
 */
public interface MessageDataSource extends DbDataSource<Message> {
}
