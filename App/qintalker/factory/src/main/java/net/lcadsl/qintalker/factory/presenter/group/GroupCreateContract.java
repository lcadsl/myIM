package net.lcadsl.qintalker.factory.presenter.group;

import net.lcadsl.qintalker.factory.model.Author;
import net.lcadsl.qintalker.factory.presenter.BaseContract;

/**
 * 群创建的契约
 */
public interface GroupCreateContract {
    interface Presenter extends BaseContract.Presenter{
        //创建
        void create(String name,String desc,String picture);
        //更改一个model的选中状态
        void changeSelect(ViewModel model,boolean isSelected);
    }

    interface View extends BaseContract.RecyclerView<Presenter,ViewModel>{
        //创建成功
        void onCreateSucceed();
    }

     class ViewModel{
        Author author;
        boolean isSelected;
     }
}
