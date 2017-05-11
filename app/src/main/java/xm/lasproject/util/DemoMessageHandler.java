package xm.lasproject.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Map;

import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMMessageType;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.newim.listener.BmobIMMessageHandler;
import cn.bmob.newim.notification.BmobNotificationManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.UpdateListener;
import xm.lasproject.R;
import xm.lasproject.bean.AgreeAddFriendMessage;
import xm.lasproject.bean.NewFriend;
import xm.lasproject.bean.NewFriendManager;
import xm.lasproject.bean.User;
import xm.lasproject.data.AddFriendMessage;
import xm.lasproject.event.RefreshEvent;
import xm.lasproject.presentation.activity.ChatActivity;
import xm.lasproject.presentation.activity.MainActivity;

/**消息接收器
 * @author smile
 * @project DemoMessageHandler
 * @date 2016-03-08-17:37
 */
public class DemoMessageHandler extends BmobIMMessageHandler {

    private Context context;

    public DemoMessageHandler(Context context) {
        this.context = context;
    }

    @Override
    public void onMessageReceive(final MessageEvent event) {
        //当接收到服务器发来的消息时，此方法被调用
//        Logger.i(event.getConversation().getConversationTitle() + "," + event.getMessage().getMsgType() + "," + event.getMessage().getContent());
        excuteMessage(event);
    }



    @Override
    public void onOfflineReceive(final OfflineMessageEvent event) {
        //每次调用connect方法时会查询一次离线消息，如果有，此方法会被调用
        Map<String,List<MessageEvent>> map =event.getEventMap();
//        Logger.i("离线消息属于" + map.size() + "个用户");
        //挨个检测下离线消息所属的用户的信息是否需要更新
        for (Map.Entry<String, List<MessageEvent>> entry : map.entrySet()) {
            List<MessageEvent> list =entry.getValue();
            int size = list.size();
            for(int i=0;i<size;i++){
                excuteMessage(list.get(i));
            }
        }
    }


    /**
     * 处理消息
     * @param event
     */
    private void excuteMessage(MessageEvent event) {
        BmobIMMessage msg = event.getMessage();
        if (BmobIMMessageType.getMessageTypeValue(msg.getMsgType()) == 0) {//用户自定义的消息类型，其类型值均为0
            processCustomMessage(msg, event.getFromUserInfo());
        } else {//SDK内部内部支持的消息类型
            if (BmobNotificationManager.getInstance(context).isShowNotification()) {//如果需要显示通知栏，SDK提供以下两种显示方式：
                Intent pendingIntent = new Intent(context, ChatActivity.class);
                pendingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                //1、多个用户的多条消息合并成一条通知：有XX个联系人发来了XX条消息
                BmobNotificationManager.getInstance(context).showNotification(event, pendingIntent);
                //2、自定义通知消息：始终只有一条通知，新消息覆盖旧消息
//                        BmobIMUserInfo info =event.getFromUserInfo();
//                        //这里可以是应用图标，也可以将聊天头像转成bitmap
//                        Bitmap largetIcon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
//                        BmobNotificationManager.getInstance(context).showNotification(largetIcon,
//                                info.getName(),msg.getContent(),"您有一条新消息",pendingIntent);
            } else {//直接发送消息事件

                EventBus.getDefault().post(event);
            }
        }
    }

    /**
     * 处理自定义消息类型
     * @param msg
     */
    private void processCustomMessage(BmobIMMessage msg, BmobIMUserInfo info) {
        String type =msg.getMsgType();
        //发送页面刷新的广播
        EventBus.getDefault().post(new RefreshEvent());
        //处理消息
        if(type.equals("add")){//接收到的添加好友的请求
            NewFriend friend = AddFriendMessage.convert(msg);
            //本地好友请求表做下校验，本地没有的才允许显示通知栏--有可能离线消息会有些重复
            long id = NewFriendManager.getInstance(context).insertOrUpdateNewFriend(friend);
            if(id>0){
                showAddNotify(friend);
            }
        }else if(type.equals("agree")){//接收到的对方同意添加自己为好友,此时需要做的事情：1、添加对方为好友，2、显示通知
            AgreeAddFriendMessage agree = AgreeAddFriendMessage.convert(msg);
            addFriend(agree.getFromId());//添加消息的发送方为好友
            //这里应该也需要做下校验--来检测下是否已经同意过该好友请求，我这里省略了
            showAgreeNotify(info,agree);
            //处理当邀请的用户同意添加好友后更新数据

            final User currentUser = BmobUser.getCurrentUser(context, User.class);
            final String Uid= info.getUserId();
            BmobQuery<User> query = new BmobQuery<User>();
            query.getObject(context, Uid, new GetListener<User>() {
                @Override
                public void onSuccess(User user) {
                    //同意请求后，修改配对信息
                    //修改当前用户在数据库中的信息
                    currentUser.setPairing("1");
                    String pairingInfo = Uid + "," + user.getUsername() + ","+user.getPhoto();
                    currentUser.setPairingInfo(pairingInfo);
                    currentUser.update(context, new UpdateListener() {
                        @Override
                        public void onSuccess() {
                            Log.e("DemoMessageHandler", "用户的信息修改成功");
                        }

                        @Override
                        public void onFailure(int i, String s) {

                        }
                    });

                }

                @Override
                public void onFailure(int i, String s) {

                }
            });

        }else{
            Toast.makeText(context,"接收到的自定义消息："+msg.getMsgType() + "," + msg.getContent() + "," + msg.getExtra(),Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 显示对方添加自己为好友的通知
     * @param friend
     */
    private void showAddNotify(NewFriend friend){
        Intent pendingIntent = new Intent(context, MainActivity.class);
        pendingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        //这里可以是应用图标，也可以将聊天头像转成bitmap
        Bitmap largetIcon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        BmobNotificationManager.getInstance(context).showNotification(largetIcon,
                friend.getName(), friend.getMsg(), friend.getName() + "请求添加你为朋友", pendingIntent);
    }

    /**
     * 显示对方同意添加自己为好友的通知
     * @param info
     * @param agree
     */
    private void showAgreeNotify(BmobIMUserInfo info,AgreeAddFriendMessage agree){
        Intent pendingIntent = new Intent(context, MainActivity.class);
        pendingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        Bitmap largetIcon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        BmobNotificationManager.getInstance(context).showNotification(largetIcon,info.getName(),agree.getMsg(),agree.getMsg(),pendingIntent);
    }

    /**
     * 添加对方为自己的好友
     * @param uid
     */
    private void addFriend(String uid){

    }

}
