package net.lcadsl.qintalker.factory.data.helper;

import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;

import net.lcadsl.qintalker.factory.model.db.AppDatabase;
import net.lcadsl.qintalker.factory.model.db.Group;
import net.lcadsl.qintalker.factory.model.db.GroupMember;
import net.lcadsl.qintalker.factory.model.db.Group_Table;
import net.lcadsl.qintalker.factory.model.db.Message;
import net.lcadsl.qintalker.factory.model.db.Session;
import net.lcadsl.qintalker.factory.model.db.User;
import net.lcadsl.qintalker.utils.CollectionUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
     * 观察者集合
     */
    private final Map<Class<?>, Set<ChangedListener>> changedListeners = new HashMap<>();

    /**
     * 从所有的监听器中获取某一个表的所有监听者
     *
     * @param modelClass 表对应的class信息
     * @param <Model>    范型
     * @return Set<ChangedListener>
     */
    private <Model extends BaseModel> Set<ChangedListener> getListeners(Class<Model> modelClass) {
        if (changedListeners.containsKey(modelClass)) {
            return changedListeners.get(modelClass);
        }
        return null;
    }

    /**
     * 添加一个监听
     *
     * @param tClass   对某个表关注
     * @param listener 监听者
     * @param <Model>  表的范型
     */
    public static <Model extends BaseModel> void addChangedListener(final Class<Model> tClass, ChangedListener<Model> listener) {
        Set<ChangedListener> changedListeners = instance.getListeners(tClass);
        if (changedListeners == null) {
            //初始化一类型的容器
            changedListeners = new HashSet<>();
            //添加到总的Map
            instance.changedListeners.put(tClass, changedListeners);
        }
        changedListeners.add(listener);
    }

    /**
     * 删除一个监听
     *
     * @param tClass   对某个表关注
     * @param listener 监听者
     * @param <Model>  表的范型
     */
    public static <Model extends BaseModel> void removeChangedListener(final Class<Model> tClass, ChangedListener<Model> listener) {
        Set<ChangedListener> changedListeners = instance.getListeners(tClass);
        if (changedListeners == null) {
            //容器本身为空
            return;
        }
        //从容器中删除监听者
        changedListeners.remove(listener);
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
                instance.notifySave(tClass, models);


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
                instance.notifyDelete(tClass, models);


            }
        }).build().execute();
    }

    /**
     * 进行通知调用
     *
     * @param tClass  通知类型
     * @param models  这个class对应的实例的数组
     * @param <Model> 这个实例的泛型，限定条件是BaseModel
     */
    @SuppressWarnings("unchecked")
    private final <Model extends BaseModel> void notifySave(final Class<Model> tClass, final Model... models) {
        //找监听器
        final Set<ChangedListener> listeners = getListeners(tClass);
        if (listeners != null && listeners.size() > 0) {
            //通用的通知
            for (ChangedListener<Model> listener : listeners) {
                listener.onDataSave(models);
            }
        }
        //例外情况
        //群成员变更需要通知对应的群信息更新
        //消息变化应该通知会话列表更新
        if (GroupMember.class.equals(tClass)) {
            updateGroup((GroupMember[]) models);
        } else if (Message.class.equals(tClass)) {
            updateSession((Message[]) models);
        }
    }


    /**
     * 进行通知调用
     *
     * @param tClass  通知类型
     * @param models  这个class对应的实例的数组
     * @param <Model> 这个实例的泛型，限定条件是BaseModel
     */
    private final <Model extends BaseModel> void notifyDelete(final Class<Model> tClass, final Model... models) {
        //找监听器
        final Set<ChangedListener> listeners = getListeners(tClass);
        if (listeners != null && listeners.size() > 0) {
            //通用的通知
            for (ChangedListener<Model> listener : listeners) {
                listener.onDataDelete(models);
            }
        }
        //例外情况
        //群成员变更需要通知对应的群信息更新
        //消息变化应该通知会话列表更新
        if (GroupMember.class.equals(tClass)) {
            updateGroup((GroupMember[]) models);
        } else if (Message.class.equals(tClass)) {
            updateSession((Message[]) models);
        }
    }

    /**
     * 从成员中找出成员对应的群，并对群进行更新
     *
     * @param members 群成员列表
     */
    private void updateGroup(GroupMember... members) {
        //不重复集合
        final Set<String> groupIds = new HashSet<>();
        for (GroupMember member : members) {
            //添加群id
            groupIds.add(member.getGroup().getId());
        }


        DatabaseDefinition definition = FlowManager.getDatabase(AppDatabase.class);
        //提交一个事务
        definition.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                //找到需要通知的群
                List<Group> groups = SQLite.select()
                        .from(Group.class)
                        .where(Group_Table.id.in(groupIds))
                        .queryList();

                instance.notifySave(Group.class, groups.toArray(new Group[0]));

            }
        }).build().execute();

    }

    /**
     * 从消息列表中筛选出对应的会话，并更新
     *
     * @param messages Message
     */
    private void updateSession(Message... messages) {
        //标识一个Session字段的唯一性
        final Set<Session.Identify> identifies = new HashSet<>();
        for (Message message : messages) {
            Session.Identify identify = Session.createSessionIdentify(message);

            identifies.add(identify);
        }


        DatabaseDefinition definition = FlowManager.getDatabase(AppDatabase.class);
        //提交一个事务
        definition.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                ModelAdapter<Session> adapter = FlowManager.getModelAdapter(Session.class);
                Session[] sessions = new Session[identifies.size()];
                int index = 0;

                for (Session.Identify identify : identifies) {
                    Session session = SessionHelper.findFromLocal(identify.id);

                    if (session == null) {
                        //第一次聊天，创建一个你和对方的会话
                        session = new Session(identify);
                    }
                    //把会话刷新到最新状态
                    session.refreshToNow();
                    //数据存储
                    adapter.save(session);
                    //添加到集合
                    sessions[index++] = session;
                }

                //调用直接进行一次通知分发
                instance.notifySave(Session.class, sessions);

            }
        }).build().execute();
    }


    /**
     * 通知监听器
     */
    @SuppressWarnings({"unused", "unchecked"})
    public interface ChangedListener<Data> {
        void onDataSave(Data... list);

        void onDataDelete(Data... list);
    }
}
