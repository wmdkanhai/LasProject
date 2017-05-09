package xm.lasproject.presentation.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;

import java.util.Collection;

import xm.lasproject.R;
import xm.lasproject.bean.Conversation;
import xm.lasproject.presentation.contract.IMutlipleItem;
import xm.lasproject.util.TimeUtil;

/**
 * 使用进一步封装的Conversation,教大家怎么自定义会话列表
 *
 * @author smile
 */
public class ConversationAdapter extends BaseRecyclerAdapter1<Conversation> {
    Context mContext;

    public ConversationAdapter(Context context, IMutlipleItem<Conversation> items, Collection<Conversation> datas) {
        super(context,items,datas);
        this.mContext = context;
    }

    @Override
    public void bindView(BaseRecyclerHolder1 holder, Conversation conversation, int position) {
        Log.e("---", "bindView: "+conversation.toString() );
        Log.e("---", "bindView: "+conversation.getLastMessageContent() );
        Log.e("---", "bindView: "+conversation.getLastMessageTime() );

        holder.setText(R.id.tv_recent_msg,conversation.getLastMessageContent());
        holder.setText(R.id.tv_recent_time,TimeUtil.getChatTime(false,conversation.getLastMessageTime()));
        //会话图标
        Object obj = conversation.getAvatar();
        if(obj instanceof String){
            String avatar=(String)obj;
//            Glide.with(mContext).load(avatar).into()
//            holder.setImageView(avatar, R.mipmap.head, R.id.iv_recent_avatar);
        }else{
            int defaultRes = (int)obj;
//            holder.setImageView(null, defaultRes, R.id.iv_recent_avatar);
        }
        //会话标题
        holder.setText(R.id.tv_recent_name, conversation.getcName());
        //查询指定未读消息数
        long unread = conversation.getUnReadCount();
        if(unread>0){
            holder.setVisible(R.id.tv_recent_unread, View.VISIBLE);
            holder.setText(R.id.tv_recent_unread, String.valueOf(unread));
        }else{
            holder.setVisible(R.id.tv_recent_unread, View.GONE);
        }
    }
}