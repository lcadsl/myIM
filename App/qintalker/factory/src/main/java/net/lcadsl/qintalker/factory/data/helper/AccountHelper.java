package net.lcadsl.qintalker.factory.data.helper;

import net.lcadsl.qintalker.factory.Factory;
import net.lcadsl.qintalker.factory.R;
import net.lcadsl.qintalker.factory.data.DataSource;
import net.lcadsl.qintalker.factory.model.api.RspModel;
import net.lcadsl.qintalker.factory.model.api.account.AccountRspModel;
import net.lcadsl.qintalker.factory.model.api.account.RegisterModel;
import net.lcadsl.qintalker.factory.model.db.User;
import net.lcadsl.qintalker.factory.net.Network;
import net.lcadsl.qintalker.factory.net.RemoteService;

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
    public static void register(RegisterModel model, final DataSource.Callback<User> callback) {
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
                    //判断绑定状态，是否绑定设备
                    if (accountRspModel.isBand()) {
                        User user = accountRspModel.getUser();
                        //TODO 进行数据库写入和缓存绑定
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
        callback.onDataNotAvailable(R.string.app_name);
    }
}
