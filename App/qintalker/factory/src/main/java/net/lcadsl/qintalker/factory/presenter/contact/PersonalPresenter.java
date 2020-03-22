package net.lcadsl.qintalker.factory.presenter.contact;

import net.lcadsl.qintalker.factory.Factory;
import net.lcadsl.qintalker.factory.data.helper.UserHelper;
import net.lcadsl.qintalker.factory.model.db.User;
import net.lcadsl.qintalker.factory.persistence.Account;
import net.lcadsl.qintalker.factory.presenter.BasePresenter;
import net.lcadsl.qintalker.factory.presenter.account.RegisterContract;
import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

public class PersonalPresenter extends BasePresenter<PersonalContract.View>
        implements PersonalContract.Presenter {


    private User user;

    public PersonalPresenter(PersonalContract.View view) {
        super(view);
    }

    @Override
    public void start() {
        super.start();


        //个人界面用户数据优先从网络拉取
        Factory.runOnAsync(new Runnable() {
            @Override
            public void run() {
                PersonalContract.View view = getView();
                if (view != null) {
                    String id = view.getUserId();
                    User user = UserHelper.searchFirstOfNet(id);
                    onLoaded(user);
                }
            }
        });
    }

    /**
     * 进行界面的设置
     * @param user  用户信息
     */
    private void onLoaded(final User user) {
        this.user = user;
        //是否是我自己
        final boolean isSelf = user.getId().equalsIgnoreCase(Account.getUserId());
        //是否已经关注
        final boolean isFollow = isSelf || user.isFollow();
        //已经关注同时不是自己才能聊天
        final boolean allowSayHello = isFollow && !isSelf;

        //切换到ui线程
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                final PersonalContract.View view=getView();
                if (view==null)
                    return;
               view.onLoadDone(user);
               view.setFollowStatus(isFollow);
               view.allowSayHello(allowSayHello);
            }
        });
    }

    @Override
    public User getUserPersonal() {
        return user;
    }
}
