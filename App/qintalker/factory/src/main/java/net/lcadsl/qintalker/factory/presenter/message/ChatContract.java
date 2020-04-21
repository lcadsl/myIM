package net.lcadsl.qintalker.factory.presenter.message;

import net.lcadsl.qintalker.factory.model.db.Group;
import net.lcadsl.qintalker.factory.model.db.Message;
import net.lcadsl.qintalker.factory.model.db.User;
import net.lcadsl.qintalker.factory.model.db.view.MemberUserModel;
import net.lcadsl.qintalker.factory.presenter.BaseContract;

import java.util.List;

/**
 * 聊天契约
 */
public interface ChatContract {
    interface Presenter extends BaseContract.Presenter{
        //发送文字
        void pushText(String content);
        //发送语音
        void pushAudio(String path);
        //发送图片，一次可发送多张，是数组
        void pushImage(String[] paths);
        //重新发送一个消息，返回是否调度成功
        boolean rePush(Message message);
    }


    //界面的基类
    interface View<InitModel> extends BaseContract.RecyclerView<Presenter,Message>{
        //初始化的Model
        void onInit(InitModel model);
    }

    //人聊天的界面
    interface UserView extends View<User>{

    }

    //群聊天的界面
    interface GroupView extends View<Group>{
        //显示管理员菜单
        void showAdminOption(boolean isAdmin);
        //初始化成员
        void onInitGroupMembers(List<MemberUserModel> members,long moreCount);
    }
}
