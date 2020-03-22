package net.lcadsl.qintalker.factory.presenter.message;

import net.lcadsl.qintalker.factory.data.helper.UserHelper;
import net.lcadsl.qintalker.factory.data.message.MessageDataSource;
import net.lcadsl.qintalker.factory.data.message.MessageRepository;
import net.lcadsl.qintalker.factory.model.db.Message;
import net.lcadsl.qintalker.factory.model.db.User;

public class ChatUserPresenter extends ChatPresenter<ChatContract.UserView>
        implements ChatContract.Presenter {


    public ChatUserPresenter(ChatContract.UserView view, String receiverId) {
        //确定数据源，View，接收者，接收者类型
        super(new MessageRepository(receiverId), view, receiverId, Message.RECEIVER_TYPE_NONE);
    }


    @Override
    public void start() {
        super.start();

        //从本地拿这个人的信息
        User receiver = UserHelper.findFromLocal(mReceiverId);
        getView().onInit(receiver);
    }
}
