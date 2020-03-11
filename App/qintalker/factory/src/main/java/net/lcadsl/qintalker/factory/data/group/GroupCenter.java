package net.lcadsl.qintalker.factory.data.group;

import net.lcadsl.qintalker.factory.model.card.GroupCard;
import net.lcadsl.qintalker.factory.model.card.GroupMemberCard;
import net.lcadsl.qintalker.factory.model.card.MessageCard;

/**
 * 群中心的接口定义
 */
public interface GroupCenter {
    //群卡片的处理
    void dispatch(GroupCard... cards);
    //群成员的处理
    void dispatch(GroupMemberCard... cards);
}
