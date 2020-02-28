package net.lcadsl.qintalker.factory.data.helper;

import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;

import net.lcadsl.qintalker.factory.Factory;
import net.lcadsl.qintalker.factory.R;
import net.lcadsl.qintalker.factory.data.DataSource;
import net.lcadsl.qintalker.factory.model.api.RspModel;
import net.lcadsl.qintalker.factory.model.api.account.AccountRspModel;
import net.lcadsl.qintalker.factory.model.api.account.RegisterModel;
import net.lcadsl.qintalker.factory.model.db.AppDatabase;
import net.lcadsl.qintalker.factory.model.db.User;
import net.lcadsl.qintalker.factory.net.Network;
import net.lcadsl.qintalker.factory.net.RemoteService;
import net.lcadsl.qintalker.factory.persistence.Account;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountHelper {
    /**
     * 注册的接口，异步调用
     *
     * @param model    传递一个注册的model进来
     * @param callback 成功与失败的接口回送
     */
    public static void register(final RegisterModel model, final DataSource.Callback<User> callback) {
        //调用Retrofit对网络请求接口做代理
        RemoteService service = Network.getRetrofit().create(RemoteService.class);
        //得到一个Call
        Call<RspModel<AccountRspModel>> call = service.accountRegister(model);
        call.enqueue(new Callback<RspModel<AccountRspModel>>() {
            @Override
            public void onResponse(Call<RspModel<AccountRspModel>> call,
                                   Response<RspModel<AccountRspModel>> response) {
                //请求成功返回
                //从返回中得到全局Model，内部是使用Gson进行解析
                RspModel<AccountRspModel> rspModel = response.body();

                if (rspModel.success()) {
                    //拿到实体
                    AccountRspModel accountRspModel = rspModel.getResult();
                    //获取我的信息
                    User user = accountRspModel.getUser();
                    //第一种，直接保存
                    user.save();




//                        //第二种，通过ModelAdapter
//                        FlowManager.getModelAdapter(User.class)
//                                .save(user);
//                        //第三种，事务中
//                        DatabaseDefinition definition=FlowManager.getDatabase(AppDatabase.class);
//                        definition.beginTransactionAsync(new ITransaction() {
//                            @Override
//                            public void execute(DatabaseWrapper databaseWrapper) {
//                                FlowManager.getModelAdapter(User.class)
//                                        .save(user);
//                            }
//                        }).build().execute();


                    //同步到xml持久化中
                    Account.login(accountRspModel);

                    //判断绑定状态，是否绑定设备
                    if (accountRspModel.isBand()) {
                        //然后返回
                        callback.onDataLoaded(user);
                    } else {
                        //进行绑定的唤起
                        bindPush(callback);

                    }
                } else {
                    //错误解析
                    Factory.decodeRspCode(rspModel, callback);
                }
            }

            @Override
            public void onFailure(Call<RspModel<AccountRspModel>> call, Throwable t) {
                //网络请求失败

                callback.onDataNotAvailable(R.string.data_network_error);
            }
        });
    }

    /**
     * 对设备id进行绑定操作
     *
     * @param callback Callback
     */
    public static void bindPush(final DataSource.Callback<User> callback) {
        //TODO 先抛出一个错误，其实是绑定没有进行
        Account.setBind(true);
    }
}
