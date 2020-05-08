package net.lcadsl.qintalker.common.widget.recycler;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.lcadsl.qintalker.common.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @param <Data>
 * @author lcadsl
 */

@SuppressWarnings({"unused", "unchecked"})
public abstract class RecyclerAdapter<Data>
        extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder<Data>>
        implements View.OnClickListener, View.OnLongClickListener, AdapterCallback<Data> {
    private final List<Data> mDataList;
    private AdapterListener<Data> mListener;

    /**
     * 构造函数模块
     */
    public RecyclerAdapter() {
        this(null);
    }

    public RecyclerAdapter(AdapterListener<Data> listener) {
        this(new ArrayList<Data>(), listener);
    }

    public RecyclerAdapter(List<Data> dataList, AdapterListener<Data> listener) {
        this.mDataList = dataList;
        this.mListener = listener;
    }


    @Override
    public int getItemViewType(int position) {
        return getItemViewType(position, mDataList.get(position));
    }

    //得到布局的类型，position坐标，data当前的数据，返回xml文件的id，用于创建ViewHolder
    @LayoutRes
    protected abstract int getItemViewType(int position, Data data);


    //创建viewholder,约定viewtype类型就是xml布局的id
    @Override
    public ViewHolder<Data> onCreateViewHolder(ViewGroup parent, int viewType) {
        //得到LayoutInflater 用于把xml初始化为view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View root = inflater.inflate(viewType, parent, false);
        ViewHolder<Data> holder = onCreateViewHolder(root, viewType);

        //设置view的tag为viewholder，进行双向绑定
        root.setTag(R.id.tag_recycler_holder, holder);

        //设置事件点击
        root.setOnClickListener(this);
        root.setOnLongClickListener(this);


        //进行界面注解绑定
        holder.unbinder = ButterKnife.bind(holder, root);
        //绑定callback
        holder.callback = this;

        return holder;
    }

    protected abstract ViewHolder<Data> onCreateViewHolder(View root, int viewType);


    //绑定数据到一个holder上，
    //position坐标，holder 来自于viewholder
    @Override
    public void onBindViewHolder(ViewHolder<Data> holder, int position) {
        //得到需要绑定的数据
        Data data = mDataList.get(position);
        //触发holder的绑定方法
        holder.bind(data);
    }


    //得到当前集合的数据量
    @Override
    public int getItemCount() {
        return mDataList.size();
    }


    //返回整个集合
    public List<Data> getItems() {
        return mDataList;
    }

    //插入一条数据并通知插入
    public void add(Data data) {
        mDataList.add(data);
        notifyItemInserted(mDataList.size() - 1);
    }

    //插入数据，并通知这段集合更新
    public void add(Data... dataList) {
        if (dataList != null && dataList.length > 0) {
            int startPos = mDataList.size();
            Collections.addAll(mDataList, dataList);
            notifyItemRangeInserted(startPos, dataList.length);
        }
    }

    //插入数据，并通知这段集合更新
    public void add(Collection<Data> dataList) {
        if (dataList != null && dataList.size() > 0) {
            int startPos = mDataList.size();
            mDataList.addAll(dataList);
            notifyItemRangeInserted(startPos, dataList.size());
        }
    }

    //删除操作
    public void clear() {
        mDataList.clear();
        notifyDataSetChanged();
    }

    public void replace(Collection<Data> dataList) {
        mDataList.clear();
        if (dataList != null && dataList.size() > 0) {
            mDataList.addAll(dataList);
        }
        notifyDataSetChanged();
    }

    @Override
    public void update(Data data, ViewHolder<Data> holder) {
//        得到当前viewholder的坐标
        int pos = holder.getAdapterPosition();
        if (pos >= 0) {
//            进行数据移除与更新
            mDataList.remove(pos);
            mDataList.add(pos, data);
//            通知有更新
            notifyItemChanged(pos);
        }
    }

    @Override
    public void onClick(View v) {
        ViewHolder viewHolder = (ViewHolder) v.getTag(R.id.tag_recycler_holder);
        if (this.mListener != null) {
            //得到viewholder当前对应的适配器中的坐标
            int pos = viewHolder.getAdapterPosition();
            //回调方法
            this.mListener.onItemClick(viewHolder, mDataList.get(pos));
        }
    }


    @Override
    public boolean onLongClick(View v) {
        ViewHolder viewHolder = (ViewHolder) v.getTag(R.id.tag_recycler_holder);
        if (this.mListener != null) {
            //得到viewholder当前对应的适配器中的坐标
            int pos = viewHolder.getAdapterPosition();
            //回调方法
            this.mListener.onItemLongClick(viewHolder, mDataList.get(pos));
            return true;
        }
        return false;
    }

    /**
     * 设置适配器监听
     *
     * @param adapterListener AdapterListener
     */
    public void setListener(AdapterListener<Data> adapterListener) {
        this.mListener = adapterListener;
    }


    //自定义监听器
    public interface AdapterListener<Data> {
        //当cell点击时触发
        void onItemClick(RecyclerAdapter.ViewHolder holder, Data data);

        //当cell长按时触发
        void onItemLongClick(RecyclerAdapter.ViewHolder holder, Data data);
    }

    //自定义的viewholder，<Data>为范型
    public static abstract class ViewHolder<Data> extends RecyclerView.ViewHolder {
        private Unbinder unbinder;
        private AdapterCallback<Data> callback;
        protected Data mData;

        public ViewHolder(View itemView) {
            super(itemView);
        }


        //用于绑定数据的触发，data为绑定的数据
        void bind(Data data) {
            this.mData = data;
            onBind(data);
        }

        protected abstract void onBind(Data data);


        public void updateData(Data data) {
            if (this.callback != null) {
                this.callback.update(data, this);
            }


        }


    }

    /**
     * 对回调接口做一次实现AdapterListener
     *
     * @param <Data>
     */
    public static abstract class AdapterListenerImpl<Data> implements AdapterListener<Data> {

        @Override
        public void onItemClick(ViewHolder holder, Data data) {

        }

        @Override
        public void onItemLongClick(ViewHolder holder, Data data) {

        }
    }
}
