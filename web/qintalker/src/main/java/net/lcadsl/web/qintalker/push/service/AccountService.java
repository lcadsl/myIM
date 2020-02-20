package net.lcadsl.web.qintalker.push.service;

import net.lcadsl.web.qintalker.push.bean.db.User;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

//实际访问路径为127.0.0.1/api/account/...
@Path("/account")
public class AccountService {
    //实际访问路径为127.0.0.1/api/account/login
    @GET
    @Path("/login")
    public String get(){
        return "You Get the login.";
    }


    @POST
    @Path("/login")
    public User post(){
        User user=new User();
        user.setName("美女");
        user.setSex(2);
        return user;

    }
}
