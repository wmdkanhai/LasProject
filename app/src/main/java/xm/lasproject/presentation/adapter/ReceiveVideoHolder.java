package xm.lasproject.presentation.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;
import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import okhttp3.Call;
import xm.lasproject.R;
import xm.lasproject.presentation.activity.PlayVideoActivity;

/**
 * 接收到的视频类型--这是举个例子，并没有展示出视频缩略图等信息，开发者可自行设置
 */
public class ReceiveVideoHolder extends BaseViewHolder {

    @BindView(R.id.tv_time)
    TextView tv_time;
    @BindView(R.id.iv_avatar)
    ImageView iv_avatar;
    @BindView(R.id.tv_message)
    ImageView tv_message;

    Context mContext;
    private String mData;

    public ReceiveVideoHolder(Context context, ViewGroup root, OnRecyclerViewListener onRecyclerViewListener) {
        super(context, root, R.layout.item_chat_received_video, onRecyclerViewListener);
        this.mContext = context;
    }

    @OnClick({R.id.iv_avatar})
    public void onAvatarClick(View view) {

    }

    @Override
    public void bindData(Object o) {
        final BmobIMMessage message = (BmobIMMessage) o;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        String time = dateFormat.format(message.getCreateTime());
        tv_time.setText(time);
        final BmobIMUserInfo info = message.getBmobIMUserInfo();
//        Glide.with(mContext).load(info.getAvatar()).into(iv_avatar);
        Glide.with(mContext).load(R.mipmap.ic_launcher).into(iv_avatar);
//        ImageLoaderFactory.getLoader().loadAvator(iv_avatar, info != null ? info.getAvatar() : null, R.mipmap.head);
        String content = message.getContent();

//        tv_message.setText("接收到的视频文件：" + content);
        Log.e("ReceiveVideoHolder", "bindData: "+content+"" );

        OkHttpUtils
                .get()
                .url(content)
                .build()
                .execute(new FileCallBack(Environment.getExternalStorageDirectory().getAbsolutePath(),"a" ) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("-OkHttpUtils--", "onError: "+e.getMessage() );
                    }

                    @Override
                    public void onResponse(File response, int id) {
                        Log.e("---", "success: "+response.getPath().toString() );
                        mData = response.getAbsolutePath();
                        Glide.with(mContext).load(response).error(R.mipmap.ic_launcher).into(tv_message);
                    }

                });

        iv_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });

        tv_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "接收录像被点击", Toast.LENGTH_SHORT).show();
                //跳转到播放界面
                Intent intent = new Intent();
                intent.setClass(mContext, PlayVideoActivity.class);
                intent.putExtra("data",mData);
                mContext.startActivity(intent);
                if (onRecyclerViewListener != null) {
                    onRecyclerViewListener.onItemClick(getAdapterPosition());
                }
            }
        });

        tv_message.setOnLongClickListener(new View.OnLongClickListener() {
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