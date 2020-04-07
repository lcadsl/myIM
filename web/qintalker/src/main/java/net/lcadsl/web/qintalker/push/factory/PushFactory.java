package net.lcadsl.web.qintalker.push.factory;

import com.google.common.base.Strings;
import net.lcadsl.web.qintalker.push.bean.api.base.PushModel;
import net.lcadsl.web.qintalker.push.bean.card.GroupMemberCard;
import net.lcadsl.web.qintalker.push.bean.card.MessageCard;
import net.lcadsl.web.qintalker.push.bean.db.*;
import net.lcadsl.web.qintalker.push.utils.Hib;
import net.lcadsl.web.qintalker.push.utils.PushDispatcher;
import net.lcadsl.web.qintalker.push.utils.TextUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 消息存储与处理的工具类
 */
public class PushFactory {
    //发送一条消息，并在当前的发送历史记录中存储
    public static void pushNewMessage(User sender, Message message) {
        if (sender == null || message == null)
            return;
        //消息卡片，用于发送
        MessageCard card = new MessageCard(message);
        //要推送的字符串
        String entity = TextUtil.toJson(card);
        //发送者
        PushDispatcher dispatcher = new PushDispatcher();

        if (message.getGroup() == null && Strings.isNullOrEmpty(message.getGroupId())) {
            //给朋友发送消息
            User receiver = UserFactory.findById(message.getReceiverId());
            if (receiver == null)
                return;
            //历史记录
            PushHistory history = new PushHistory();
            //普通消息类型
            history.setEntityType(PushModel.ENTITY_TYPE_MESSAGE);
            history.setEntity(entity);
            history.setReceiver(receiver);
            //接收者当前设备的推送id
            history.setReceiverPushId(receiver.getPushId());


            //推送的真实model
            PushModel pushModel = new PushModel();
            //每一条历史记录都是独立的，可以单独发送
            pushModel.add(history.getEntityType(), history.getEntity());

            //把需要发送的数据丢给发送者进行发送
            dispatcher.add(receiver, pushModel);
            //保存到数据库
            Hib.queryOnly(session -> session.save(history));

        } else {
            Group group = message.getGroup();
            //延迟加载可能为空，此时可以用id查询
            if (group == null)
                group = GroupFactory.findById(message.getGroupId());
            //如果还找不到，则返回
            if (group == null)
                return;


            //给群发送消息
            Set<GroupMember> members = GroupFactory.getMembers(group);
            if (members == null || members.size() == 0)
                return;

            //发送时过滤自己
            members = members.stream()
                    .filter(groupMember -> !groupMember.getUserId()
                            .equalsIgnoreCase(sender.getId()))
                    .collect(Collectors.toSet());

            if (members.size() == 0)
                return;

            //一个历史记录列表
            List<PushHistory> histories = new ArrayList<>();

            addGroupMembersPushModel(dispatcher,
                    histories,
                    members,
                    entity,
                    PushModel.ENTITY_TYPE_MESSAGE);


            Hib.queryOnly(session -> {
                for (PushHistory history : histories) {
                    session.saveOrUpdate(history);
                }
                return null;
            });
        }
        //发送者进行真实的提交
        dispatcher.submit();

    }

    /**
     * 给群成员构建一个消息
     * 把消息存储到数据库的历史记录中
     * 每个人的每条消息都是一个记录
     */
    private static void addGroupMembersPushModel(PushDispatcher dispatcher,
                                                 List<PushHistory> histories,
                                                 Set<GroupMember> members,
                                                 String entity,
                                                 int entityTypeMessage) {

        for (GroupMember member : members) {
            User receiver = member.getUser();
            if (receiver == null)
                return;

            PushHistory history = new PushHistory();
            history.setEntityType(entityTypeMessage);
            history.setEntity(entity);
            history.setReceiver(receiver);
            history.setReceiverPushId(receiver.getPushId());

            histories.add(history);
            //构建一个消息model
            PushModel pushModel = new PushModel();
            pushModel.add(history.getEntityType(), history.getEntity());
            //添加到发送者的数据集中
            dispatcher.add(receiver, pushModel);

        }

    }

    public static void pushJoinGroup(Set<GroupMember> members) {
        //TODO 给群成员发送已经被添加
    }

    //通知老成员有新人加入
    public static void pushGroupMemberAdd(Set<GroupMember> oldMembers, List<GroupMemberCard> insertCards) {

    }
}
