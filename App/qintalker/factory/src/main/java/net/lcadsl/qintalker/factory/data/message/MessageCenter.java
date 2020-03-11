package net.lcadsl.qintalker.factory.data.message;

import net.lcadsl.qintalker.factory.model.card.MessageCard;
import net.lcadsl.qintalker.factory.model.card.UserCard;

/**
 * 消息卡片中心
 */
public interface MessageCenter {
    void dispatch(MessageCard... cards);
}
