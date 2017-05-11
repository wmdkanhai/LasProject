package xm.lasproject.presentation.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMImageMessage;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMSendStatus;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.exception.BmobException;
import xm.lasproject.R;

/**
 * 发送的文本类型
 */
public class SendImageHolder extends BaseViewHolder {

    @BindView(R.id.tv_time)
    TextView mTvTime;
    @BindView(R.id.iv_avatar)
    ImageView mIvAvatar;
    @BindView(R.id.iv_picture)
    ImageView mIvPicture;
    @BindView(R.id.iv_fail_resend)
    ImageView mIvFailResend;
    @BindView(R.id.tv_send_status)
    TextView mTvSendStatus;
    @BindView(R.id.progress_load)
    ProgressBar mProgressLoad;

    BmobIMConversation c;
    Context mContext;

    public SendImageHolder(Context context, ViewGroup root, BmobIMConversation c, OnRecyclerViewListener onRecyclerViewListener) {
        super(context, root, R.layout.item_chat_sent_image, onRecyclerViewListener);
        this.c = c;
        this.mContext = context;
    }

    @Override
    public void bindData(Object o) {
        BmobIMMessage msg = (BmobIMMessage) o;
        //用户信息的获取必须在buildFromDB之前，否则会报错'Entity is detached from DAO context'
        final BmobIMUserInfo info = msg.getBmobIMUserInfo();
        Glide.with(mContext).load(info.getAvatar()).into(mIvAvatar);
//        ImageLoaderFactory.getLoader().loadAvator(iv_avatar, info != null ? info.getAvatar() : null, R.mipmap.ic_launcher);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        String time = dateFormat.format(msg.getCreateTime());
        mTvTime.setText(time);
        //
        final BmobIMImageMessage message = BmobIMImageMessage.buildFromDB(true, msg);
        int status = message.getSendStatus();
        if (status == BmobIMSendStatus.SENDFAILED.getStatus() || status == BmobIMSendStatus.UPLOADAILED.getStatus()) {
            mIvFailResend.setVisibility(View.VISIBLE);
            mProgressLoad.setVisibility(View.GONE);
            mTvSendStatus.setVisibility(View.INVISIBLE);
        } else if (status == BmobIMSendStatus.SENDING.getStatus()) {
            mProgressLoad.setVisibility(View.VISIBLE);
            mIvFailResend.setVisibility(View.GONE);
            mTvSendStatus.setVisibility(View.INVISIBLE);
        } else {
            mTvSendStatus.setVisibility(View.VISIBLE);
            mTvSendStatus.setText("已发送");
            mIvFailResend.setVisibility(View.GONE);
            mProgressLoad.setVisibility(View.GONE);
        }

        //发送的不是远程图片地址，则取本地地址
        Glide.with(mContext).load(message.getRemoteUrl()).into(mIvPicture);
//        ImageLoaderFactory.getLoader().load(mIvPicture, TextUtils.isEmpty(message.getRemoteUrl()) ? message.getLocalPath() : message.getRemoteUrl(), R.mipmap.ic_launcher, null);
//    ViewUtil.setPicture(TextUtils.isEmpty(message.getRemoteUrl()) ? message.getLocalPath():message.getRemoteUrl(), R.mipmap.ic_launcher, mIvPicture,null);

        mIvAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("点击" + info.getName() + "的头像");
            }
        });
        mIvPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("点击图片:" + (TextUtils.isEmpty(message.getRemoteUrl()) ? message.getLocalPath() : message.getRemoteUrl()) + "");
                if (onRecyclerViewListener != null) {
                    onRecyclerViewListener.onItemClick(getAdapterPosition());
                }
            }
        });

        mIvPicture.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onRecyclerViewListener != null) {
                    onRecyclerViewListener.onItemLongClick(getAdapterPosition());
                }
                return true;
            }
        });

        //重发
        mIvFailResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c.resendMessage(message, new MessageSendListener() {
                    @Override
                    public void onStart(BmobIMMessage msg) {
                        mProgressLoad.setVisibility(View.VISIBLE);
                        mIvFailResend.setVisibility(View.GONE);
                        mTvSendStatus.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void done(BmobIMMessage msg, BmobException e) {
                        if (e == null) {
                            mTvSendStatus.setVisibility(View.VISIBLE);
                            mTvSendStatus.setText("已发送");
                            mIvFailResend.setVisibility(View.GONE);
                            mProgressLoad.setVisibility(View.GONE);
                        } else {
                            mIvFailResend.setVisibility(View.VISIBLE);
                            mProgressLoad.setVisibility(View.GONE);
                            mTvSendStatus.setVisibility(View.INVISIBLE);
                        }
                    }
                });
            }
        });
    }

    public void showTime(boolean isShow) {
        mTvTime.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }
}
