package net.lcadsl.web.qintalker.push.service;

import net.lcadsl.web.qintalker.push.bean.db.User;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

public class BaseService {
    //添加一个上下文注解，该注解会给SecurityContext赋值
    //值为拦截器返回的SecurityContext
    @Context
    protected SecurityContext securityContext;

    /**
     * 从上下文中直接获取自己的信息
     * @return  User
     */
    protected User getSelf(){
        return (User) securityContext.getUserPrincipal();
    }
}
