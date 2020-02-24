package net.lcadsl.web.qintalker.push.service;

import net.lcadsl.web.qintalker.push.bean.api.base.ResponseModel;
import net.lcadsl.web.qintalker.push.bean.api.user.UpdateInfoModel;
import net.lcadsl.web.qintalker.push.bean.card.UserCard;
import net.lcadsl.web.qintalker.push.bean.db.User;
import net.lcadsl.web.qintalker.push.factory.UserFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * 用户消息处理的Service
 */
//127.0.0.1/api/user/...
@Path("/user")
public class UserService extends BaseService {


    //用户信息修改接口
    //返回自己的个人信息
    @PUT
    //@Path()
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<UserCard> update(UpdateInfoModel model) {
        if(!UpdateInfoModel.check(model)) {
            //返回参数异常
            return ResponseModel.buildParameterError();
        }

        User self = getSelf();


        //更新用户信息
        self = model.updateToUser(self);
        self = UserFactory.update(self);
        //构架自己的用户信息
        UserCard card = new UserCard(self, true);
        //返回
        return ResponseModel.buildOk(card);


    }

}

