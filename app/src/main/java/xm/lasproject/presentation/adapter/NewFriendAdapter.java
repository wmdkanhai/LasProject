package xm.lasproject.presentation.adapter;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import xm.lasproject.R;
import xm.lasproject.bean.AgreeAddFriendMessage;
import xm.lasproject.bean.Friend;
import xm.lasproject.bean.NewFriend;
import xm.lasproject.bean.NewFriendManager;
import xm.lasproject.bean.User;
import xm.lasproject.config.SystemVar;

/**
 * <pre>
 *     author : xm
 *     e-mail : wmdkanhai@qq.com
 *     time   : 2017/03/31
 *     desc   : xxx描述
 *     version: 1.0
 * </pre>
 */

public class NewFriendAdapter extends BaseRecyclerAdapter<NewFriend> {
    private Context mContext;
    private List<NewFriend> data;

    public NewFriendAdapter(Context ctx, List<NewFriend> list) {
        super(ctx, list);
        this.mContext = ctx;
        this.data = list;
    }

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.item_new_friend;
    }

    @Override
    public void bindData(RecyclerViewHolder holder, final int position, NewFriend item) {
        holder.setText(R.id.tv_recent_name, data.get(position).getName());
        holder.setText(R.id.tv_recent_msg, data.get(position).getMsg());
        Integer status = data.get(position).getStatus();
        final Button button = holder.getButton(R.id.btn_aggree);
//        Logger.i("bindData:"+status+","+data.get(position).getUid()+","+data.get(position).getTime());
        if (status == null || status == SystemVar.STATUS_VERIFY_NONE || status == SystemVar.STATUS_VERIFY_READED) {//未添加/已读未添加
            button.setText("接受");
            button.setEnabled(true);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    agreeAdd(data.get(position), new SaveListener() {
                        @Override
                        public void onSuccess() {
                            button.setText("已添加");
                            button.setEnabled(false);
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            button.setEnabled(true);
                            Toast.makeText(mContext, "出错了：" + s, Toast.LENGTH_SHORT).show();
                        }

                    });

                }
            });

        } else {
            button.setText("已添加");
            button.setEnabled(false);
        }
    }


//    @Override
//    public int getItemLayoutId(int viewType) {
//        return R.layout.item_search_user;
//    }
//
//    @Override
//    public void bindData(RecyclerViewHolder holder, int position, User item) {
//        final ImageView imageView = holder.getImageView(R.id.imageView);
//        if (data.get(position).getPhoto() != null) {
//            final BmobFile photo = data.get(position).getPhoto();
//            Glide.with(mContext).load(photo.getFileUrl()).into(imageView);
//        }else {
//            imageView.setImageResource(R.mipmap.ic_launcher);
//        }
//        holder.setText(R.id.tv_username, data.get(position).getUsername());
//    }

    /**
     * 添加到好友表中...
     *
     * @param add
     * @param listener
     */
    private void agreeAdd(final NewFriend add, final SaveListener listener) {
        User user = new User();
        user.setObjectId(add.getUid());
        Friend friend = new Friend();
        User user1 = BmobUser.getCurrentUser(mContext,User.class);
        friend.setUser(user1);
        friend.setFriendUser(user);


        friend.save(mContext, new SaveListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(mContext, "添加", Toast.LENGTH_SHORT).show();
                sendAgreeAddFriendMessage(add, listener);
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(mContext, "出错了：" + s, Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * 发送同意添加好友的请求
     */
    private void sendAgreeAddFriendMessage(final NewFriend add, final SaveListener listener) {
        BmobIMUserInfo info = new BmobIMUserInfo(add.getUid(), add.getName(), add.getAvatar());
        //如果为true,则表明为暂态会话，也就是说该会话仅执行发送消息的操作，不会保存会话和消息到本地数据库中
        BmobIMConversation c = BmobIM.getInstance().startPrivateConversation(info, true, null);
        //这个obtain方法才是真正创建一个管理消息发送的会话
        BmobIMConversation conversation = BmobIMConversation.obtain(BmobIMClient.getInstance(), c);
        //而AgreeAddFriendMessage的isTransient设置为false，表明我希望在对方的会话数据库中保存该类型的消息
        AgreeAddFriendMessage msg = new AgreeAddFriendMessage();
        User currentUser = BmobUser.getCurrentUser(mContext,User.class);
        msg.setContent("我通过了你的好友验证请求，我们可以开始聊天了!");//---这句话是直接存储到对方的消息表中的
        Map<String, Object> map = new HashMap<>();
        map.put("msg", currentUser.getUsername() + "同意添加你为好友");//显示在通知栏上面的内容
        map.put("uid", add.getUid());//发送者的uid-方便请求添加的发送方找到该条添加好友的请求
        map.put("time", add.getTime());//添加好友的请求时间
        msg.setExtraMap(map);
        conversation.sendMessage(msg, new MessageSendListener() {
            @Override
            public void done(BmobIMMessage msg, BmobException e) {
                if (e == null) {//发送成功
                    //修改本地的好友请求记录
                    NewFriendManager.getInstance(mContext).updateNewFriend(add, SystemVar.STATUS_VERIFIED);
//                    listener.done();
                } else {//发送失败
//                    listener.onFailure(e.getErrorCode(), e.getMessage());
                }
            }
        });
    }
}
