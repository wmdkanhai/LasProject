package xm.lasproject.presentation.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * @所属包名称 :	com.mvp.recyclerview.ui.utils
 * @描述 :  RecyclerView适配器工具类
 * @作者 :	wh
 * @COPYRIGHT :	copyright(c) 2016,Rights Reserved
 * @版本 :	v1.0
 * @创建日期 :	2016/5/25
 */
public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerViewHolder> {
    //list集合
    protected final List<T> mData;
    protected final Context mContext;
    //上下文
    protected LayoutInflater mInflater;
    //点击item监听
    private OnItemClickListener mClickListener;
    //长按item监听
    private OnItemLongClickListener mLongClickListener;

    /**
     * 构造方法
     *
     * @param ctx
     * @param list
     */
    public BaseRecyclerAdapter(Context ctx, List<T> list) {
        mData = (list != null) ? list : new ArrayList<T>();
        mContext = ctx;
        mInflater = LayoutInflater.from(ctx);
    }

    public void clear() {
        this.mData.clear();
    }

    /**
     * 方法中主要是引入xml布局文件,并且给item点击事件和item长按事件赋值
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        final RecyclerViewHolder holder = new RecyclerViewHolder(mContext,
                mInflater.inflate(getItemLayoutId(viewType), parent, false));
        if (mClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onItemClick(holder.itemView, holder.getPosition());
                }
            });
        }
        if (mLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mLongClickListener.onItemLongClick(holder.itemView, holder.getPosition());
                    return true;
                }
            });
        }
        return holder;
    }

    /**
     * onBindViewHolder这个方法主要是给子项赋值数据的
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        bindData(holder, position, mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    /**
     * add方法是添加item方法
     *
     * @param pos
     * @param item
     */
    public void add(int pos, T item) {
        mData.add(pos, item);
        notifyItemInserted(pos);
    }

    /**
     * delete方法是删除item方法
     *
     * @param pos
     */
    public void delete(int pos) {
        mData.remove(pos);
        notifyItemRemoved(pos);
    }

    /**
     * item点击事件set方法
     *
     * @param listener
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        mClickListener = listener;
    }

    /**
     * item长安事件set方法
     *
     * @param listener
     */
    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        mLongClickListener = listener;
    }

    /**
     * item中xml布局文件方法
     *
     * @param viewType
     * @return
     */
    abstract public int getItemLayoutId(int viewType);

    /**
     * 赋值数据方法
     *
     * @param holder
     * @param position
     * @param item
     */
    abstract public void bindData(RecyclerViewHolder holder, int position, T item);

    /**
     * item点击事件接口
     */
    public interface OnItemClickListener {
        public void onItemClick(View itemView, int pos);
    }

    /**
     * item长按事件接口
     */
    public interface OnItemLongClickListener {
        public void onItemLongClick(View itemView, int pos);
    }
}