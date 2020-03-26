package net.lcadsl.qintalker.factory.presenter.message;

import android.support.v7.util.DiffUtil;

import net.lcadsl.qintalker.factory.data.message.SessionDataSource;
import net.lcadsl.qintalker.factory.data.message.SessionRepository;
import net.lcadsl.qintalker.factory.model.db.Session;
import net.lcadsl.qintalker.factory.presenter.BaseSourcePresenter;
import net.lcadsl.qintalker.factory.utils.DiffUiDataCallback;

import java.util.List;

/**
 * 最近聊天列表的Presenter
 */
public class SessionPresenter extends BaseSourcePresenter<Session, Session,
        SessionDataSource, SessionContract.View> implements SessionContract.Presenter {

    public SessionPresenter(SessionContract.View view) {
        super(new SessionRepository(), view);
    }

    @Override
    public void onDataLoaded(List<Session> sessions) {
        SessionContract.View view = getView();
        if (view == null)
            return;

        //差异对比
        List<Session> old = view.getRecyclerAdapter().getItems();
        DiffUiDataCallback<Session> callback = new DiffUiDataCallback<>(old, sessions);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);
        //刷新界面
        refreshData(result,sessions);
    }
}
