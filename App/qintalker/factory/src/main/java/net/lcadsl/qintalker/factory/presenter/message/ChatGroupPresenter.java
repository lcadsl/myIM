package net.lcadsl.qintalker.factory.presenter.message;

import net.lcadsl.qintalker.factory.data.helper.GroupHelper;
import net.lcadsl.qintalker.factory.data.helper.UserHelper;
import net.lcadsl.qintalker.factory.data.message.MessageGroupRepository;
import net.lcadsl.qintalker.factory.data.message.MessageRepository;
import net.lcadsl.qintalker.factory.model.db.Group;
import net.lcadsl.qintalker.factory.model.db.Message;
import net.lcadsl.qintalker.factory.model.db.User;

/**
 * 群聊天的逻辑
 */
public class ChatGroupPresenter extends ChatPresenter<ChatContract.GroupView>
        implements ChatContract.Presenter {


    public ChatGroupPresenter(ChatContract.GroupView view, String receiverId) {
        //确定数据源，View，接收者，接收者类型
        super(new MessageGroupRepository(receiverId), view, receiverId, Message.RECEIVER_TYPE_GROUP);
    }


    @Override
    public void start() {
        super.start();

        //拿群的信息
        Group group = GroupHelper.findFromLocal(mReceiverId);
        if (group != null) {
            //初始化操作


        }
    }
}
