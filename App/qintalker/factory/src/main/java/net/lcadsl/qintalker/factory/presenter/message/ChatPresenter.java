package net.lcadsl.qintalker.factory.presenter.message;

import android.support.v7.util.DiffUtil;

import net.lcadsl.qintalker.factory.data.helper.MessageHelper;
import net.lcadsl.qintalker.factory.data.message.MessageDataSource;
import net.lcadsl.qintalker.factory.model.api.message.MsgCreateModel;
import net.lcadsl.qintalker.factory.model.db.Message;
import net.lcadsl.qintalker.factory.persistence.Account;
import net.lcadsl.qintalker.factory.presenter.BaseSourcePresenter;
import net.lcadsl.qintalker.factory.utils.DiffUiDataCallback;

import java.util.List;

/**
 * 聊天Presenter的基础类
 */
public class ChatPresenter<View extends ChatContract.View>
        extends BaseSourcePresenter<Message, Message, MessageDataSource, View>
        implements ChatContract.Presenter {

    //接收者id，可能是群或者人
    protected String mReceiverId;
    //区分是人还是群id
    protected int mReceiverType;


    public ChatPresenter(MessageDataSource source, View view, String receiverId, int receiverType) {
        super(source, view);
        this.mReceiverId = receiverId;
        this.mReceiverType = receiverType;
    }


    @Override
    public void pushText(String content) {
        //构建一个新的消息
        MsgCreateModel model = new MsgCreateModel.Builder()
                .receiver(mReceiverId, mReceiverType)
                .content(content, Message.TYPE_STR)
                .build();
        //进行网络发送
        MessageHelper.push(model);
    }

    @Override
    public void pushAudio(String path) {
        //TODO
    }

    @Override
    public void pushImage(String[] paths) {
        //TODO
    }

    @Override
    public boolean rePush(Message message) {
        //确定消息是可重复发送的
        if (Account.getUserId().equalsIgnoreCase(message.getSender().getId())
                && message.getStatus() == Message.STATUS_FAILED) {


            message.setStatus(Message.STATUS_CREATED);

            MsgCreateModel model = MsgCreateModel.buildWithMessage(message);
            MessageHelper.push(model);
            return true;
        }


        return false;
    }

    @Override
    public void onDataLoaded(List<Message> messages) {
        ChatContract.View view = getView();
        if (view == null)
            return;
        //拿到老数据
        List<Message> old = view.getRecyclerAdapter().getItems();

        //差异计算
        DiffUiDataCallback<Message> callback = new DiffUiDataCallback<>(old, messages);

        final DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);

        //进行界面刷新
        refreshData(result, messages);
    }
}
