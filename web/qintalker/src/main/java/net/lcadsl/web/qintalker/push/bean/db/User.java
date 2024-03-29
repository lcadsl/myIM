package net.lcadsl.web.qintalker.push.bean.db;


import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * 用户的Model，对应数据库
 */
@Entity
@Table(name = "TB_USER")
public class User implements Principal {
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

    //用户名必须唯一
    @Column(nullable = false, length = 128, unique = true)
    private String name;

    //电话必须唯一
    @Column(nullable = false, length = 62, unique = true)
    private String phone;

    //密码
    @Column(nullable = false)
    private String password;

    //头像允许为空
    @Column
    private String portrait;

    //个人描述
    @Column
    private String description;


    //性别，有初始值，所以不为空
    @Column(nullable = false)
    private int sex = 0;

    //认证token,可以拉取用户信息，所以唯一
    @Column(unique = true)
    private String token;

    //用于推送的设备id
    @Column
    private String pushId;

    //创建时间，在创建时就已经写入
    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createAt=LocalDateTime.now();

    //更新时间，在创建时就已经写入
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updateAt=LocalDateTime.now();

    //最后接收信息时间
    @Column
    private LocalDateTime lastReceivedAt=LocalDateTime.now();


    //我关注的人列表
    //对应的表是TB_USER_FOLLOW.originId
    @JoinColumn(name = "originId")
    //定义为懒加载，默认加载User信息的时候，并不查询这个集合
    @LazyCollection(LazyCollectionOption.EXTRA)
    //一对多关系
    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Set<UserFollow> following=new HashSet<>();


    //关注我的人的列表
    //对应的表是TB_USER_FOLLOW.targetId
    @JoinColumn(name = "targetId")
    //定义为懒加载，默认加载User信息的时候，并不查询这个集合
    @LazyCollection(LazyCollectionOption.EXTRA)
    //一对多关系
    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Set<UserFollow> followers=new HashSet<>();

    @JoinColumn(name = "ownerId")
    // 懒加载集合方式为尽可能的不加载具体的数据，
    // 当访问groups.size()仅仅查询数量，不加载具体的Group信息
    // 只有当遍历集合的时候才加载具体的数据
    @LazyCollection(LazyCollectionOption.EXTRA)
    // FetchType.LAZY：懒加载，加载用户信息时不加载这个集合
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Group> groups=new HashSet<>();






    public Set<UserFollow> getFollowing() {
        return following;
    }

    public void setFollowing(Set<UserFollow> following) {
        this.following = following;
    }

    public Set<UserFollow> getFollowers() {
        return followers;
    }

    public void setFollowers(Set<UserFollow> followers) {
        this.followers = followers;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
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

    public LocalDateTime getLastReceivedAt() {
        return lastReceivedAt;
    }

    public void setLastReceivedAt(LocalDateTime lastReceivedAt) {
        this.lastReceivedAt = lastReceivedAt;
    }

    public Set<Group> getGroups() {
        return groups;
    }

    public void setGroups(Set<Group> groups) {
        this.groups = groups;
    }
}
