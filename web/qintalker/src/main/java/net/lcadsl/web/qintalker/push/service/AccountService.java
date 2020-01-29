package net.lcadsl.web.qintalker.push.service;

import javax.ws.rs.GET;
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
}
