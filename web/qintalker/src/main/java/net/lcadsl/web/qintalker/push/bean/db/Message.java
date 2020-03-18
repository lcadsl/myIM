package net.lcadsl.web.qintalker.push.bean.db;

import net.lcadsl.web.qintalker.push.bean.api.message.MessageCreateModel;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 消息的Model，对应数据库
 */
@Entity
@Table(name = "TB_MESSAGE")
public class Message {
    public static final int RECEIVER_TYPE_NONE = 1;
    public static final int RECEIVER_TYPE_GROUP = 2;
    public static final int TYPE_STR = 1;//字符串类型
    public static final int TYPE_PIC = 2;//图片类型
    public static final int TYPE_FILE = 3;//文件类型
    public static final int TYPE_AUDIO = 4;//字符串类型

    //这是一个主键
    @Id
    @PrimaryKeyJoinColumn
    //主键生成存储的类型为UUID
    //不自动生成，ID由代码写入，客户端负责
//    @GeneratedValue(generator = "uuid")
    //把uuid的生成器定义为uuid2，uuid2是常规的uuid
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    //不允许更改，不允许为空
    @Column(updatable = false, nullable = false)
    private String id;

    //内容不为空，类型TEXT
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    //附件
    @Column
    private String attach;

    //消息类型
    @Column(nullable = false)
    private int type;

    //创建时间，在创建时就已经写入
    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createAt = LocalDateTime.now();

    //更新时间，在创建时就已经写入
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updateAt = LocalDateTime.now();

    //发送者,不为空
    //多消息对应一发送者
    @JoinColumn(name = "senderId")
    @ManyToOne(optional = false)
    private User sender;

    //对应senderId
    @Column(nullable = false, updatable = false, insertable = false)
    private String senderId;

    //接收者，可为空
    //多消息对应一接收者
    @JoinColumn(name = "receiverId")
    @ManyToOne
    private User receiver;

    //对应receiverId
    @Column(updatable = false, insertable = false)
    private String receiverId;

    //一个群可以接受多个消息
    @ManyToOne
    @JoinColumn(name = "groupId")
    private Group group;
    @Column(updatable = false, insertable = false)
    private String groupId;

    public Message() {

    }

    //普通朋友发送的构造函数
    public Message(User sender, User receiver, MessageCreateModel model) {
        this.id = model.getId();
        this.content = model.getContent();
        this.attach = model.getAttach();
        this.type = model.getType();

        this.sender=sender;
        this.receiver=receiver;
    }

    //发送给群的构造函数
    public Message(User sender, Group group, MessageCreateModel model) {
        this.id = model.getId();
        this.content = model.getContent();
        this.attach = model.getAttach();
        this.type = model.getType();

        this.sender=sender;
        this.group=group;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
