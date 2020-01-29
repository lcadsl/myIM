package net.lcadsl.web.qintalker.push;

import net.lcadsl.web.qintalker.push.service.AccountService;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJsonProvider;
import org.glassfish.jersey.server.ResourceConfig;

import java.util.logging.Logger;

public class Application extends ResourceConfig {
    public Application(){
        //注册逻辑处理的包名
        //packages("net.lcadsl.web.qintalker.push.service");
        packages(AccountService.class.getPackage().getName());
        //Json解析器
        register(JacksonJsonProvider.class);
        //日志打印输出
        register(Logger.class);
    }
}
