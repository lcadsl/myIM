package net.lcadsl.qintalker.factory.data.helper;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import net.lcadsl.qintalker.factory.Factory;
import net.lcadsl.qintalker.factory.R;
import net.lcadsl.qintalker.factory.data.DataSource;
import net.lcadsl.qintalker.factory.model.api.RspModel;
import net.lcadsl.qintalker.factory.model.api.account.AccountRspModel;
import net.lcadsl.qintalker.factory.model.api.user.UserUpdateModel;
import net.lcadsl.qintalker.factory.model.card.UserCard;
import net.lcadsl.qintalker.factory.model.db.User;
import net.lcadsl.qintalker.factory.model.db.User_Table;
import net.lcadsl.qintalker.factory.net.Network;
import net.lcadsl.qintalker.factory.net.RemoteService;
import net.lcadsl.qintalker.factory.presenter.contact.FollowPresenter;

import java.io.IOException;
import java.util.List;

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
                    DbHelper.save(User.class, user);
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


    //搜索的方法
    public static Call search(String name, final DataSource.Callback<List<UserCard>> callback) {
        RemoteService service = Network.remote();
        // 得到一个Call
        Call<RspModel<List<UserCard>>> call = service.userSearch(name);

        call.enqueue(new Callback<RspModel<List<UserCard>>>() {
            @Override
            public void onResponse(Call<RspModel<List<UserCard>>> call, Response<RspModel<List<UserCard>>> response) {
                RspModel<List<UserCard>> rspModel = response.body();
                if (rspModel.success()) {
                    //返回数据
                    callback.onDataLoaded(rspModel.getResult());
                } else {
                    Factory.decodeRspCode(rspModel, callback);
                }
            }

            @Override
            public void onFailure(Call<RspModel<List<UserCard>>> call, Throwable t) {
                callback.onDataNotAvailable(R.string.data_network_error);
            }
        });

        //把当前的调度者返回
        return call;
    }


    //关注的网络请求
    public static void follow(String id, final DataSource.Callback<UserCard> callback) {
        RemoteService service = Network.remote();
        // 得到一个Call
        Call<RspModel<UserCard>> call = service.userFollow(id);

        call.enqueue(new Callback<RspModel<UserCard>>() {
            @Override
            public void onResponse(Call<RspModel<UserCard>> call, Response<RspModel<UserCard>> response) {
                RspModel<UserCard> rspModel = response.body();
                if (rspModel.success()) {
                    UserCard userCard = rspModel.getResult();
                    //保存到本地数据库,并通知联系人列表刷新
                    User user = userCard.build();
                    DbHelper.save(User.class,user);


                    //返回数据
                    callback.onDataLoaded(userCard);
                } else {
                    Factory.decodeRspCode(rspModel, callback);
                }
            }

            @Override
            public void onFailure(Call<RspModel<UserCard>> call, Throwable t) {
                callback.onDataNotAvailable(R.string.data_network_error);
            }
        });


    }


    //刷新联系人的方法
    public static void refreshContacts(final DataSource.Callback<List<UserCard>> callback) {
        RemoteService service = Network.remote();
        service.userContacts()
                .enqueue(new Callback<RspModel<List<UserCard>>>() {
                    @Override
                    public void onResponse(Call<RspModel<List<UserCard>>> call, Response<RspModel<List<UserCard>>> response) {
                        RspModel<List<UserCard>> rspModel = response.body();
                        if (rspModel.success()) {
                            //返回数据
                            callback.onDataLoaded(rspModel.getResult());
                        } else {
                            Factory.decodeRspCode(rspModel, callback);
                        }
                    }

                    @Override
                    public void onFailure(Call<RspModel<List<UserCard>>> call, Throwable t) {
                        callback.onDataNotAvailable(R.string.data_network_error);
                    }
                });

    }


    //从本地查询一个用户的信息
    public static User findFromLocal(String id) {
        return SQLite.select()
                .from(User.class)
                .where(User_Table.id.eq(id))
                .querySingle();
    }
    //从网络查询一个用户的信息
    public static User findFromNet(String id) {
        RemoteService remoteService = Network.remote();
        try {
            Response<RspModel<UserCard>> response = remoteService.userFind(id).execute();
            UserCard card = response.body().getResult();
            if (card != null) {


                User user = card.build();
                DbHelper.save(User.class,user);


                return user;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * 搜索一个用户，优先本地缓存，然后再从网络拉取
     */
    public static User search(String id) {
        User user = findFromLocal(id);
        if (user == null) {
            return findFromNet(id);
        }
        return user;
    }

    /**
     * 搜索一个用户，优先从网络拉取
     */
    public static User searchFirstOfNet(String id) {
        User user = findFromNet(id);
        if (user == null) {
            return findFromLocal(id);
        }
        return user;
    }
}
