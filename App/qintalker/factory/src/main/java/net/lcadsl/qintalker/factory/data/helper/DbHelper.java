package net.lcadsl.qintalker.factory.data.helper;

import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;

import net.lcadsl.qintalker.factory.model.db.AppDatabase;
import net.lcadsl.qintalker.factory.model.db.User;
import net.lcadsl.qintalker.utils.CollectionUtil;

import java.util.Arrays;
import java.util.List;

/**
 * 数据库的辅助工具类 管：增删改
 */
public class DbHelper {
    private static final DbHelper instance;

    static {
        instance = new DbHelper();
    }

    private DbHelper() {

    }



    /**
     * 新增或者修改的统一方法
     *
     * @param tClass  传递一个class信息
     * @param models  这个class对应的实例的数组
     * @param <Model> 这个实例的泛型，限定条件是BaseModel
     */
    //限定条件是BaseModel
    public static <Model extends BaseModel> void save(final Class<Model> tClass, final Model... models) {
        if (models == null || models.length == 0)
            return;

        DatabaseDefinition definition = FlowManager.getDatabase(AppDatabase.class);
        //提交一个事务
        definition.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {

                //执行
                ModelAdapter<Model> adapter = FlowManager.getModelAdapter(tClass);
                //adapter.saveAll(CollectionUtil.toArrayList(users));
                //保存
                adapter.saveAll(Arrays.asList(models));
                //唤起通知
                instance.notifySave(tClass,models);


            }
        }).build().execute();
    }


    /**
     * 删除的方法
     *
     * @param tClass  传递一个class信息
     * @param models  这个class对应的实例的数组
     * @param <Model> 这个实例的泛型，限定条件是BaseModel
     */
    //限定条件是BaseModel
    public static <Model extends BaseModel> void delete(final Class<Model> tClass, final Model... models) {
        if (models == null || models.length == 0)
            return;

        DatabaseDefinition definition = FlowManager.getDatabase(AppDatabase.class);
        //提交一个事务
        definition.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {

                //执行
                ModelAdapter<Model> adapter = FlowManager.getModelAdapter(tClass);
                //adapter.saveAll(CollectionUtil.toArrayList(users));
                //删除
                adapter.deleteAll(Arrays.asList(models));
                //唤起通知
                instance.notifyDelete(tClass,models);


            }
        }).build().execute();
    }

    /**
     * 进行通知调用
     * @param tClass    通知类型
     * @param models  这个class对应的实例的数组
     * @param <Model> 这个实例的泛型，限定条件是BaseModel
     */
    private final <Model extends BaseModel> void notifySave(final Class<Model> tClass, final Model... models){
       //TODO
    }


    /**
     * 进行通知调用
     * @param tClass    通知类型
     * @param models  这个class对应的实例的数组
     * @param <Model> 这个实例的泛型，限定条件是BaseModel
     */
    private final <Model extends BaseModel> void notifyDelete(final Class<Model> tClass, final Model... models){
        //TODO
    }
}
