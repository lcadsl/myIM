package net.lcadsl.qintalker.factory.data.message;

import android.text.TextUtils;

import net.lcadsl.qintalker.factory.data.helper.DbHelper;
import net.lcadsl.qintalker.factory.data.helper.GroupHelper;
import net.lcadsl.qintalker.factory.data.helper.MessageHelper;
import net.lcadsl.qintalker.factory.data.helper.UserHelper;
import net.lcadsl.qintalker.factory.data.user.UserDispatcher;
import net.lcadsl.qintalker.factory.model.card.MessageCard;
import net.lcadsl.qintalker.factory.model.db.Group;
import net.lcadsl.qintalker.factory.model.db.Message;
import net.lcadsl.qintalker.factory.model.db.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 消息中心的实现类
 *
 * @version 1.0.0
 */
public class MessageDispatcher implements MessageCenter {
    private static MessageCenter instance;
    // 单线程池；处理卡片一个个的消息进行处理
    private final Executor executor = Executors.newSingleThreadExecutor();

    public static MessageCenter instance() {
        if (instance == null) {
            synchronized (UserDispatcher.class) {
                if (instance == null)
                    instance = new MessageDispatcher();
            }
        }
        return instance;
    }


    @Override
    public void dispatch(MessageCard... cards) {
        if (cards == null || cards.length == 0)
            return;

        // 丢到单线程池中
        executor.execute(new MessageCardHandler(cards));
    }

    /**
     * 消息的卡片的线程调度的处理会触发run方法
     */
    private class MessageCardHandler implements Runnable {
        private final MessageCard[] cards;

        MessageCardHandler(MessageCard[] cards) {
            this.cards = cards;
        }

        @Override
        public void run() {
            List<Message> messages = new ArrayList<>();
            for (MessageCard card : cards) {
                //卡片过滤
                if (card == null || TextUtils.isEmpty(card.getSenderId())
                        || TextUtils.isEmpty(card.getId())
                        || (TextUtils.isEmpty(card.getReceiverId())
                        && TextUtils.isEmpty(card.getGroupId())))
                    continue;
                //消息卡片有可能是推送过来的，也有可能是自己造的
                //推送过来的代表服务器一定有
                //如果是自己造的，先存储本地，后发送网络
                //流程：写消息-》存储本地-》发送网络-》网络返回-》刷新本地状态
                Message message = MessageHelper.findFromLocal(card.getId());
                if (message != null) {
                    //消息本身字段应发送后就是不变的，
                    // 如果已经完成则不做处理
                    if (message.getStatus() == Message.STATUS_DONE)
                        continue;
                    // 新状态为完成才更新服务器时间，不然不做更新
                    if (card.getStatus() == Message.STATUS_DONE)
                        //代表网络发送成功，要修改时间为服务器时间
                        message.setCreateAt(card.getCreateAt());
                    // 更新一些会变化的内容
                    message.setContent(card.getContent());
                    message.setAttach(card.getAttach());
                    message.setStatus(card.getStatus());
                } else {
                    //没找到本地消息，初次在数据库存储
                    User sender = UserHelper.search(card.getSenderId());
                    User receiver = null;
                    Group group = null;
                    if (!TextUtils.isEmpty(card.getReceiverId())) {
                        receiver = UserHelper.search(card.getReceiverId());
                    } else if (!TextUtils.isEmpty(card.getGroupId())) {
                        group = GroupHelper.findFromLocal(card.getGroupId());
                    }
                    if (receiver == null && group == null && sender != null)
                        continue;

                    message = card.build(sender, receiver, group);
                }
                messages.add(message);
            }
            if (messages.size() > 0)
                DbHelper.save(Message.class, messages.toArray(new Message[0]));
        }
    }
}
