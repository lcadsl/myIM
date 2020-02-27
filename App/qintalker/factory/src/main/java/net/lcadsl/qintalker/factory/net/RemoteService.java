package net.lcadsl.qintalker.factory.net;


import net.lcadsl.qintalker.factory.model.api.RspModel;
import net.lcadsl.qintalker.factory.model.api.account.AccountRspModel;
import net.lcadsl.qintalker.factory.model.api.account.RegisterModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * 网络请求的所有的接口
 */
public interface RemoteService {

    /**
     * 网络请求一个注册接口
     * @param model 传入的是RegisterModel
     * @return  返回的是RspModel<AccountRspModel>
     */

    @POST("Account/register")
    Call<RspModel<AccountRspModel>> accountRegister(@Body RegisterModel model);
}
