package net.lcadsl.web.qintalker.push.service;

import net.lcadsl.web.qintalker.push.bean.api.account.AccountRspModel;
import net.lcadsl.web.qintalker.push.bean.api.account.LoginModel;
import net.lcadsl.web.qintalker.push.bean.api.account.RegisterModel;
import net.lcadsl.web.qintalker.push.bean.api.base.ResponseModel;
import net.lcadsl.web.qintalker.push.bean.db.User;
import net.lcadsl.web.qintalker.push.factory.UserFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

//实际访问路径为127.0.0.1/api/account/...
@Path("/account")
public class AccountService {
    //登录
    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<AccountRspModel> login(LoginModel model) {
        User user = UserFactory.findByPhone(model.getAccount().trim());



    }










    //注册
    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<AccountRspModel> register(RegisterModel model) {
        User user = UserFactory.findByPhone(model.getAccount().trim());

        if (user != null) {
            //已有账户
            return ResponseModel.buildHaveAccountError();
        }

        user = UserFactory.findByName(model.getName().trim());

        if (user != null) {
            //已有名字
            return ResponseModel.buildHaveNameError();
        }

        //开始注册逻辑
        user = UserFactory.register(model.getAccount(),
                model.getPassword(),
                model.getName());


        if (user != null) {
            //返回当前账户
            AccountRspModel rspModel = new AccountRspModel(user);
            return ResponseModel.buildOk(rspModel);

        } else {
            //注册异常
            return ResponseModel.buildRegisterError();
        }

    }

}
