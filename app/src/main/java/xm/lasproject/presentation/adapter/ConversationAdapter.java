package xm.lasproject.presentation.adapter;

import android.content.Context;

import java.util.List;

import xm.lasproject.R;
import xm.lasproject.bean.Conversation;
import xm.lasproject.util.TimeUtil;

/**
 * <pre>
 *     author : xm
 *     e-mail : wmdkanhai@qq.com
 *     time   : 2017/03/31
 *     desc   : xxx描述
 *     version: 1.0
 * </pre>
 */

public class ConversationAdapter extends BaseRecyclerAdapter<Conversation> {
    /**
     * 构造方法
     *
     * @param ctx
     * @param conversation
     */
    private List<Conversation> data;
    private Context mContext;
    public ConversationAdapter(Context ctx, List<Conversation> data) {
        super(ctx, data);
        this.mContext = ctx;
        this.data = data;
    }

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.item_conversation;
    }

    @Override
    public void bindData(RecyclerViewHolder holder, int position, Conversation item) {
        holder.setText(R.id.tv_recent_msg,data.get(position).getLastMessageContent());
        holder.setText(R.id.tv_recent_time, TimeUtil.getChatTime(false,data.get(position).getLastMessageTime()));
        //会话图标
        Object obj = data.get(position).getAvatar();
        if(obj instanceof String){
            String avatar=(String)obj;
//            holder.setImageView(avatar, R.mipmap.ic_launcher, R.id.iv_recent_avatar);
        }else{
            int defaultRes = (int)obj;
//            holder.setImageView(null, defaultRes, R.id.iv_recent_avatar);
        }
        //会话标题
        holder.setText(R.id.tv_recent_name, data.get(position).getcName());
        //查询指定未读消息数
        long unread = data.get(position).getUnReadCount();
        if(unread>0){
//            holder.setVisible(R.id.tv_recent_unread, View.VISIBLE);
            holder.setText(R.id.tv_recent_unread, String.valueOf(unread));
        }else{
//            holder.setVisible(R.id.tv_recent_unread, View.GONE);
        }
    }
}
