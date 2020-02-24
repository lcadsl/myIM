package net.lcadsl.web.qintalker.push;

import net.lcadsl.web.qintalker.push.provider.GsonProvider;
import net.lcadsl.web.qintalker.push.service.AccountService;
import org.glassfish.jersey.server.ResourceConfig;

import java.util.logging.Logger;

public class Application extends ResourceConfig {
    public Application(){
        //注册逻辑处理的包的名字
        //packages("net.lcadsl.web.qintalker.push.service");
        packages(AccountService.class.getPackage().getName());
        //Json解析器
        //register(JacksonJsonProvider.class);
        //替换解析器为Gson
        register(GsonProvider.class);
        //日志打印输出
        register(Logger.class);
    }
}
