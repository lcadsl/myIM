package net.lcadsl.qintalker.factory;

import net.lcadsl.qintalker.common.app.Application;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Factory {
    //单例模式
    private static final Factory instance;
    private final Executor executor;

    static {
        instance=new Factory();
    }

    private Factory(){
        //新建一个四个线程的线程池
        executor= Executors.newFixedThreadPool(4);
    }

    /**
     * 返回全局的Application
     * @return Application
     */
    public static Application app(){
        return Application.getInstance();
    }

    /**
     * 异步运行的方法
     * @param runnable Runnable
     */
    //runOnAsync异步
    public static void runOnAsync(Runnable runnable){
        //拿到单例拿到线程池然后异步执行
        instance.executor.execute(runnable);
    }

}
