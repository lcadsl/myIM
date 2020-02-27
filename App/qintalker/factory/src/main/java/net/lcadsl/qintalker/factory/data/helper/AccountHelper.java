package net.lcadsl.qintalker.factory.data.helper;

import net.lcadsl.qintalker.factory.R;
import net.lcadsl.qintalker.factory.data.DataSource;
import net.lcadsl.qintalker.factory.model.api.account.RegisterModel;
import net.lcadsl.qintalker.factory.model.db.User;

public class AccountHelper {
    /**
     * 注册的接口，异步调用
     * @param model 传递一个注册的model进来
     * @param callback  成功与失败的接口回送
     */
    public static void register(RegisterModel model, final DataSource.Callback<User> callback){
        new Thread(){
            @Override
            public void run() {
                super.run();
                try{
                    Thread.sleep(3000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }

                callback.onDataNotAvailable(R.string.data_rsp_error_parameters);

            }
        }.start();
    }
}
