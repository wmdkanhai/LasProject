package xm.lasproject.presentation.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @所属包名称 :	com.mvp.recyclerview.ui.utils
 * @描述 :  ViewHolder类
 * @作者 :	吴昊
 * @COPYRIGHT :	copyright(c) 2016,Rights Reserved
 * @版本 :	v1.0
 * @创建日期 :	2016/5/25
 */
public class RecyclerViewHolder extends RecyclerView.ViewHolder {
    /**
     * 集合类，layout里包含的View,以view的id作为key，value是view对象
     */
    private SparseArray<View> mViews;
    /**
     * 上下文对象
     */
    private Context mContext;

    /**
     * 构造方法
     *
     * @param ctx
     * @param itemView
     */
    public RecyclerViewHolder(Context ctx, View itemView) {
        super(itemView);
        mContext = ctx;
        mViews = new SparseArray<View>();
    }

    /**
     * 存放xml页面方法
     *
     * @param viewId
     * @param <T>
     * @return
     */
    private <T extends View> T findViewById(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public View getView(int viewId) {
        return findViewById(viewId);
    }

    /**
     * 存放文本的id
     *
     * @param viewId
     * @return
     */
    public TextView getTextView(int viewId) {
        return (TextView) getView(viewId);
    }

    /**
     * 存放button的id
     *
     * @param viewId
     * @return
     */
    public Button getButton(int viewId) {
        return (Button) getView(viewId);
    }

    /**
     * 存放图片的id
     *
     * @param viewId
     * @return
     */
    public ImageView getImageView(int viewId) {
        return (ImageView) getView(viewId);
    }
    public LinearLayout getLinearLayout(int viewId) {
        return (LinearLayout) getView(viewId);
    }

    /**
     * 存放图片按钮的id
     *
     * @param viewId
     * @return
     */
    public ImageButton getImageButton(int viewId) {
        return (ImageButton) getView(viewId);
    }

    /**
     * 存放输入框的id
     *
     * @param viewId
     * @return
     */
    public EditText getEditText(int viewId) {
        return (EditText) getView(viewId);
    }

    /**
     * 存放文本xml中的id并且可以赋值数据的方法
     *
     * @param viewId
     * @param value
     * @return
     */
    public RecyclerViewHolder setText(int viewId, String value) {
        TextView view = findViewById(viewId);
        view.setText(value);
        return null;
    }

    /**
     * 存放图片xml中的id并且可以赋值数据的方法
     *
     * @param viewId
     * @param resId
     * @return
     */
    public RecyclerViewHolder setBackground(int viewId, int resId) {
        View view = findViewById(viewId);
        view.setBackgroundColor(resId);
        return null;
    }

    /**
     * 存放点击事件监听
     *
     * @param viewId
     * @param listener
     * @return
     */
    public RecyclerViewHolder setClickListener(int viewId, View.OnClickListener listener) {
        View view = findViewById(viewId);
        view.setOnClickListener(listener);
        return null;
    }
}