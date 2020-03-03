package net.lcadsl.qintalker.factory.data.helper;

import net.lcadsl.qintalker.factory.Factory;
import net.lcadsl.qintalker.factory.R;
import net.lcadsl.qintalker.factory.data.DataSource;
import net.lcadsl.qintalker.factory.model.api.RspModel;
import net.lcadsl.qintalker.factory.model.api.account.AccountRspModel;
import net.lcadsl.qintalker.factory.model.api.user.UserUpdateModel;
import net.lcadsl.qintalker.factory.model.card.UserCard;
import net.lcadsl.qintalker.factory.model.db.User;
import net.lcadsl.qintalker.factory.net.Network;
import net.lcadsl.qintalker.factory.net.RemoteService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserHelper {
    //更新用户信息的操作，异步的
    public static void update(UserUpdateModel model, final DataSource.Callback<UserCard> callback) {
        RemoteService service = Network.remote();
        // 得到一个Call
        Call<RspModel<UserCard>> call = service.userUpdate(model);
        //网络请求
        call.enqueue(new Callback<RspModel<UserCard>>() {
            @Override
            public void onResponse(Call<RspModel<UserCard>> call, Response<RspModel<UserCard>> response) {
                RspModel<UserCard> rspModel = response.body();
                if (rspModel.success()) {
                    UserCard userCard = rspModel.getResult();
                    //数据库的存储操作，需要把UserCard转换为User
                    //保存用户信息
                    User user = userCard.build();
                    user.save();
                    //返回成功
                    callback.onDataLoaded(userCard);
                } else {
                    //错误情况下进行错误分配
                    Factory.decodeRspCode(rspModel, callback);
                }
            }

            @Override
            public void onFailure(Call<RspModel<UserCard>> call, Throwable t) {
                callback.onDataNotAvailable(R.string.data_network_error);
            }
        });
    }
}
