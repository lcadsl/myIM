package net.lcadsl.qintalker.factory.data.group;

import android.text.TextUtils;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import net.lcadsl.qintalker.factory.data.BaseDbRepository;
import net.lcadsl.qintalker.factory.data.helper.GroupHelper;
import net.lcadsl.qintalker.factory.model.db.Group;
import net.lcadsl.qintalker.factory.model.db.Group_Table;
import net.lcadsl.qintalker.factory.model.db.view.MemberUserModel;

import java.util.List;

/**
 * 我的群组的数据仓库，是对GroupDataSource的实现
 */
public class GroupsRepository extends BaseDbRepository<Group>
        implements GroupsDataSource{


    @Override
    public void load(SucceedCallback<List<Group>> callback) {
        super.load(callback);

        SQLite.select()
                .from(Group.class)
                .orderBy(Group_Table.name,true)
                .limit(100)
                .async()
                .queryListResultCallback(this)
                .execute();
    }

    @Override
    protected boolean isRequired(Group group) {
        if (group.getGroupMemberCount()>0){
            group.holder=buildGroupHolder(group);
        }else {
            group.holder=null;
            GroupHelper.refreshGroupMember(group);
        }


        //所有的群都要关注
        return true;
    }

    //初始化界面显示的成员信息
    private String buildGroupHolder(Group group) {
        List<MemberUserModel> userModels=group.getLatelyGroupMembers();
        if (userModels==null||userModels.size()==0)
            return null;

        StringBuilder builder=new StringBuilder();
        for (MemberUserModel userModel : userModels) {
            builder.append(TextUtils.isEmpty(userModel.alias)?userModel.name:userModel.alias);
            builder.append(", ");
        }

        builder.delete(builder.lastIndexOf(", "),builder.length());


        return builder.toString();
    }
}
