package net.lcadsl.web.qintalker.push.service;

import com.google.common.base.Strings;
import net.lcadsl.web.qintalker.push.bean.api.base.ResponseModel;
import net.lcadsl.web.qintalker.push.bean.api.group.GroupCreateModel;
import net.lcadsl.web.qintalker.push.bean.api.group.GroupMemberAddModel;
import net.lcadsl.web.qintalker.push.bean.api.group.GroupMemberUpdateModel;
import net.lcadsl.web.qintalker.push.bean.card.ApplyCard;
import net.lcadsl.web.qintalker.push.bean.card.GroupCard;
import net.lcadsl.web.qintalker.push.bean.card.GroupMemberCard;
import net.lcadsl.web.qintalker.push.bean.db.Group;
import net.lcadsl.web.qintalker.push.bean.db.GroupMember;
import net.lcadsl.web.qintalker.push.bean.db.User;
import net.lcadsl.web.qintalker.push.factory.GroupFactory;
import net.lcadsl.web.qintalker.push.factory.PushFactory;
import net.lcadsl.web.qintalker.push.factory.UserFactory;
import net.lcadsl.web.qintalker.push.provider.LocalDateTimeConverter;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 群组接口的入口
 */

@Path("/group")
public class GroupService extends BaseService {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<GroupCard> create(GroupCreateModel model) {
        if (!GroupCreateModel.check(model)) {
            return ResponseModel.buildParameterError();
        }
        //创建者
        User creator = getSelf();
        //保证创建者不在列表中
        model.getUsers().remove(creator.getId());
        if (model.getUsers().size() == 0) {
            return ResponseModel.buildParameterError();
        }

        //检查是否已有
        if (GroupFactory.findByName(model.getName()) != null) {
            return ResponseModel.buildHaveNameError();
        }

        List<User> users = new ArrayList<>();
        for (String s : model.getUsers()) {
            User user = UserFactory.findById(s);
            if (user == null)
                continue;
            users.add(user);
        }
        //一个成员都没有
        if (users.size() == 0) {
            return ResponseModel.buildParameterError();
        }

        Group group = GroupFactory.create(creator, model, users);
        if (group == null) {
            //服务器异常
            return ResponseModel.buildServiceError();
        }

        //拿管理员信息
        GroupMember creatorMember = GroupFactory.getMember(creator.getId(), group.getId());
        if (creatorMember == null) {
            //服务器异常
            return ResponseModel.buildServiceError();
        }
        //拿到群成员，给所有群成员发送信息，告知已经被添加到群
        Set<GroupMember> members = GroupFactory.getMembers(group);
        if (members == null) {
            //服务器异常
            return ResponseModel.buildServiceError();
        }


        members = members.stream()
                .filter(groupMember -> {
                    return !groupMember.getId().equalsIgnoreCase(creatorMember.getId());
                })
                .collect(Collectors.toSet());

        //开始发起推送
        PushFactory.pushJoinGroup(members);

        return ResponseModel.buildOk(new GroupCard(creatorMember));
    }


    @GET
    @Path("/search/{name:(.*)?}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<List<GroupCard>> search(@PathParam("name") @DefaultValue("") String name) {
        User self = getSelf();
        List<Group> groups = GroupFactory.search(name);
        if (groups != null && groups.size() > 0) {
            List<GroupCard> groupCards = groups.stream()
                    .map(group -> {
                        GroupMember member = GroupFactory.getMember(self.getId(), group.getId());

                        return new GroupCard(group, member);
                    }).collect(Collectors.toList());

            return ResponseModel.buildOk(groupCards);
        }
        return ResponseModel.buildOk();
    }


    @GET
    @Path("/list/{date:(.*)?}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<List<GroupCard>> list(@DefaultValue("") @PathParam("date") String dateStr) {
        User self = getSelf();

        //拿时间
        LocalDateTime dateTime = null;
        if (!Strings.isNullOrEmpty(dateStr)) {
            try {
                dateTime = LocalDateTime.parse(dateStr, LocalDateTimeConverter.FORMATTER);
            } catch (Exception e) {
                dateTime = null;
            }
        }

        Set<GroupMember> members = GroupFactory.getMembers(self);
        if (members == null || members.size() == 0)
            return ResponseModel.buildOk();

        final LocalDateTime finalDateTime = dateTime;
        List<GroupCard> groupCards = members.stream()
                .filter(groupMember -> finalDateTime == null ||
                        groupMember.getUpdateAt().isAfter(finalDateTime))
                .map(GroupCard::new)
                .collect(Collectors.toList());

        return ResponseModel.buildOk(groupCards);
    }


    @GET
    @Path("/{groupId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<GroupCard> getGroup(@PathParam("groupId") String id) {
        if (Strings.isNullOrEmpty(id))
            return ResponseModel.buildParameterError();

        User self = getSelf();
        GroupMember member = GroupFactory.getMember(self.getId(), id);
        if (member == null)
            return ResponseModel.buildNotFoundGroupError(null);

        return ResponseModel.buildOk(new GroupCard(member));
    }


    @GET
    @Path("/{groupId}/member")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<List<GroupMemberCard>> members(@PathParam("groupId") String groupId) {
        User self = getSelf();

        //没有这个群
        Group group = GroupFactory.findById(groupId);
        if (group == null)
            return ResponseModel.buildNotFoundGroupError(null);

        //检查权限
        GroupMember selfMember = GroupFactory.getMember(self.getId(), groupId);
        if (selfMember == null)
            return ResponseModel.buildNoPermissionError();

        //所有成员
        Set<GroupMember> members = GroupFactory.getMembers(group);
        if (members == null)
            return ResponseModel.buildServiceError();

        //返回
        List<GroupMemberCard> memberCards = members
                .stream()
                .map(GroupMemberCard::new)
                .collect(Collectors.toList());

        return ResponseModel.buildOk(memberCards);
    }


    @POST
    @Path("/{groupId}/member")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<List<GroupMemberCard>> memberAdd(@PathParam("groupId") String groupId, GroupMemberAddModel model) {
        if (Strings.isNullOrEmpty(groupId) || !GroupMemberAddModel.check(model))
            return ResponseModel.buildParameterError();

        //拿到我的信息
        User self = getSelf();

        //移除我之后再进行判断
        model.getUsers().remove(self.getId());
        if (model.getUsers().size() == 0)
            return ResponseModel.buildParameterError();

        //没有这个群
        Group group = GroupFactory.findById(groupId);
        if (group == null)
            return ResponseModel.buildNotFoundGroupError(null);

        //我必须是成员，同时是管理员及以上
        GroupMember selfMember = GroupFactory.getMember(self.getId(), groupId);
        if (selfMember == null || selfMember.getPermissionType() == GroupMember.PERMISSION_TYPE_NONE)
            return ResponseModel.buildNoPermissionError();

        //已有的成员
        Set<GroupMember> oldMembers = GroupFactory.getMembers(group);
        Set<String> oldMemberUserIds = oldMembers.stream()
                .map(GroupMember::getUserId)
                .collect(Collectors.toSet());

        List<User> insertUsers = new ArrayList<>();
        for (String s : model.getUsers()) {
            //找人
            User user = UserFactory.findById(s);
            if (user == null)
                continue;
            //已经在群里了
            if (oldMemberUserIds.contains(user.getId()))
                continue;

            insertUsers.add(user);
        }
        //没有一个新增成员
        if (insertUsers.size() == 0) {
            return ResponseModel.buildParameterError();
        }

        //进行添加操作
        Set<GroupMember> insertMembers = GroupFactory.addMembers(group, insertUsers);
        if (insertMembers == null)
            return ResponseModel.buildServiceError();

        //转换
        List<GroupMemberCard> insertCards = insertMembers.stream()
                .map(GroupMemberCard::new)
                .collect(Collectors.toList());

        //通知，两部曲
        //1.通知新增的成员，你被加入了xxx群
        PushFactory.pushJoinGroup(insertMembers);

        //2.通知群组中其他成员有xxx加入了群
        PushFactory.pushGroupMemberAdd(oldMembers, insertCards);

        return ResponseModel.buildOk(insertCards);
    }


    @PUT
    @Path("/member/{memberId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<GroupMemberCard> modifyMember(@PathParam("memberId") String memberId, GroupMemberUpdateModel model) {
        return null;
    }


    @POST
    @Path("/applyJoin/{groupId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<ApplyCard> join(@PathParam("groupId") String groupId) {
        return null;
    }
}
