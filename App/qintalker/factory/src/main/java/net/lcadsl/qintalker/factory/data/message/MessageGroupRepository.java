package net.lcadsl.qintalker.factory.data.message;

import android.support.annotation.NonNull;

import com.raizlabs.android.dbflow.sql.language.OperatorGroup;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

import net.lcadsl.qintalker.factory.data.BaseDbRepository;
import net.lcadsl.qintalker.factory.model.db.Message;
import net.lcadsl.qintalker.factory.model.db.Message_Table;

import java.util.Collections;
import java.util.List;

/**
 * 跟群聊天的时候的聊天列表 关注的内容是我发给群的或者别人发送给群的
 */
public class MessageGroupRepository extends BaseDbRepository<Message>
        implements MessageDataSource {
    //聊天的群Id
    private String receiverId;

    public MessageGroupRepository(String receiverId) {
        super();
        this.receiverId = receiverId;
    }

    @Override
    public void load(SucceedCallback<List<Message>> callback) {
        super.load(callback);

        //(sender_id == receiverId and group_id==null)or(receiver_id == receiverId)

        SQLite.select()
                .from(Message.class)
                .where(Message_Table.group_id.eq(receiverId))
                .orderBy(Message_Table.createAt, false)
                .limit(30)
                .async()
                .queryListResultCallback(this)
                .execute();


    }

    @Override
    protected boolean isRequired(Message message) {
        //如果消息的group不为空，那一定是发送到群的
        //如果群id等于我们需要的，那就通过
        return message.getGroup() != null && receiverId.equalsIgnoreCase(message.getGroup().getId());


    }

    @Override
    public void onListQueryResult(QueryTransaction transaction, @NonNull List<Message> tResult) {

        //反转返回的集合
        Collections.reverse(tResult);
        super.onListQueryResult(transaction, tResult);
    }
}
