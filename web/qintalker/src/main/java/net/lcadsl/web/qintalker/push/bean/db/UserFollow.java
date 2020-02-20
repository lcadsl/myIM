package net.lcadsl.web.qintalker.push.bean.db;


import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 用户关系的Model，用于用户之间好友关系的实现
 */
@Entity
@Table(name = "TB_USER_FOLLOW")
public class UserFollow {
    //这是一个主键
    @Id
    @PrimaryKeyJoinColumn
    //主键生成存储的类型
    @GeneratedValue(generator = "uuid")
    //把uuid的生成器定义为uuid2，uuid2是常规的uuid
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    //不允许更改，不允许为空
    @Column(updatable = false, nullable = false)
    private String id;

    //定义一个关系发起人
    //多对一关系，每一次关注都是一条记录
    //可以创建多条关注信息
    //必须存储，一条关注记录一定要有一个发起人
    @ManyToOne(optional = false)
    //定义关联的表字段名为originId，对应的是User.id
    @JoinColumn(name = "originId")
    private User origin;

    //把上面的列提取到Model中
    //不允许为空、更新、插入
    @Column(nullable = false, updatable = false, insertable = false)
    private String originId;

    //定义关注的目标
    //也是多对一
    @ManyToOne(optional = false)
    //定义关联的表字段名为targetId，对应的是User.id
    @JoinColumn(name = "targetId")
    private User target;

    //把上面的列提取到Model中
    //不允许为空、更新、插入
    @Column(nullable = false, updatable = false, insertable = false)
    private String targetId;

    //别名，也就是备注
    @Column
    private String alias;


    //创建时间，在创建时就已经写入
    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createAt = LocalDateTime.now();

    //更新时间，在创建时就已经写入
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updateAt = LocalDateTime.now();









    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getOrigin() {
        return origin;
    }

    public void setOrigin(User origin) {
        this.origin = origin;
    }

    public String getOriginId() {
        return originId;
    }

    public void setOriginId(String originId) {
        this.originId = originId;
    }

    public User getTarget() {
        return target;
    }

    public void setTarget(User target) {
        this.target = target;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
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
}
