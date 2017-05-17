package xm.lasproject.presentation.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import cn.bmob.newim.bean.BmobIMImageMessage;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import xm.lasproject.R;
import xm.lasproject.presentation.activity.PlayPhotoActivity;

/**
 * 接收到的文本类型
 */
public class ReceiveImageHolder extends BaseViewHolder {

    @BindView(R.id.tv_time)
    TextView tv_time;
    @BindView(R.id.iv_avatar)
    ImageView iv_avatar;
    @BindView(R.id.iv_picture)
    ImageView iv_picture;
    @BindView(R.id.progress_load)
    ProgressBar progress_load;

    Context mContext;

    public ReceiveImageHolder(Context context, ViewGroup root, OnRecyclerViewListener onRecyclerViewListener) {
        super(context, root, R.layout.item_chat_received_image, onRecyclerViewListener);
        this.mContext = context;
    }

    /**
     * @param o
     */
    @Override
    public void bindData(Object o) {
        BmobIMMessage msg = (BmobIMMessage) o;
        //用户信息的获取必须在buildFromDB之前，否则会报错'Entity is detached from DAO context'
        final BmobIMUserInfo info = msg.getBmobIMUserInfo();

//        Glide.with(mContext).load(info.getAvatar()).placeholder(R.mipmap.ic_launcher).into(iv_avatar);
        Glide.with(mContext).load(R.mipmap.ic_launcher).placeholder(R.mipmap.ic_launcher).into(iv_avatar);
//        ImageLoaderFactory.getLoader().loadAvator(iv_avatar, info != null ? info.getAvatar() : null, R.mipmap.head);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        String time = dateFormat.format(msg.getCreateTime());
        tv_time.setText(time);
        //可使用buildFromDB方法转化为指定类型的消息
        final BmobIMImageMessage message = BmobIMImageMessage.buildFromDB(false, msg);
//        //显示图片
        Glide.with(mContext).load(message.getRemoteUrl()).into(iv_picture);
        progress_load.setVisibility(View.GONE);
//        ImageLoaderFactory.getLoader().load(iv_picture, message.getRemoteUrl(), R.mipmap.ic_launcher, new ImageLoadingListener() {
//            ;
//
//            @Override
//            public void onLoadingStarted(String s, View view) {
//                progress_load.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
//                progress_load.setVisibility(View.INVISIBLE);
//            }
//
//            @Override
//            public void onLoadingCancelled(String s, View view) {
//                progress_load.setVisibility(View.INVISIBLE);
//            }
//
//            @Override
//            public void onLoadingFailed(String s, View view, FailReason failReason) {
//                progress_load.setVisibility(View.INVISIBLE);
//            }
//        });


        iv_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        iv_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "照片被点击了", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClass(mContext, PlayPhotoActivity.class);
                intent.putExtra("data",message.getRemoteUrl());
                mContext.startActivity(intent);
                if (onRecyclerViewListener != null) {
                    onRecyclerViewListener.onItemClick(getAdapterPosition());
                }
            }
        });

        iv_picture.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onRecyclerViewListener != null) {
                    onRecyclerViewListener.onItemLongClick(getAdapterPosition());
                }
                return true;
            }
        });

    }

    public void showTime(boolean isShow) {
        tv_time.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }
}