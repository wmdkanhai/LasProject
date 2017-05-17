package xm.lasproject.presentation.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMAudioMessage;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMImageMessage;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMTextMessage;
import cn.bmob.newim.bean.BmobIMVideoMessage;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.core.BmobRecordManager;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.listener.MessageListHandler;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.newim.listener.MessagesQueryListener;
import cn.bmob.newim.listener.ObseverListener;
import cn.bmob.newim.listener.OnRecordChangeListener;
import cn.bmob.newim.notification.BmobNotificationManager;
import cn.bmob.v3.exception.BmobException;
import xm.lasproject.R;
import xm.lasproject.presentation.adapter.ChatAdapter;
import xm.lasproject.presentation.adapter.OnRecyclerViewListener;
import xm.lasproject.util.Util;

import static xm.lasproject.R.id.btn_speak;
import static xm.lasproject.R.id.edit_msg;
import static xm.lasproject.R.id.layout_record;
import static xm.lasproject.R.id.tv_voice_tips;

public class Chat2Activity extends BaseActivity implements ObseverListener, MessageListHandler {
    @BindView(R.id.activity_chat)
    LinearLayout mActivityChat;

    @BindView(R.id.tb_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;

    //消息
    @BindView(edit_msg)
    EditText mEditMsg;

    @BindView(R.id.btn_chat_emo)
    Button mBtnChatEmo;
    @BindView(btn_speak)
    Button mBtnSpeak;
    @BindView(R.id.btn_chat_voice)
    Button mBtnChatVoice;
    @BindView(R.id.btn_chat_keyboard)
    Button mBtnChatKeyboard;
    @BindView(R.id.btn_chat_send)
    Button mBtnChatSend;

    //语音相关的
    @BindView(R.id.iv_record)
    ImageView mIvRecord;
    @BindView(tv_voice_tips)
    TextView mTvVoiceTips;
    @BindView(layout_record)
    RelativeLayout mLayoutRecord;


    @BindView(R.id.layout_more)
    LinearLayout mLayoutMore;
    @BindView(R.id.layout_add)
    LinearLayout mLayoutAdd;
    @BindView(R.id.layout_emo)
    LinearLayout mLayoutEmo;

    private Drawable[] drawable_Anims;// 话筒动画
    BmobRecordManager recordManager;
    static BmobIMConversation c;

    static ChatAdapter adapter;

    protected static LinearLayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat2);

        c = BmobIMConversation.obtain(BmobIMClient.getInstance(), (BmobIMConversation) getBundle().getSerializable("c"));
        ButterKnife.bind(this);
        initNaviView();
        initSwipeLayout();
        initVoiceView();
        initBottomView();
    }

    private void initSwipeLayout() {
        mSwipeRefresh.setEnabled(true);
        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        adapter = new ChatAdapter(this, c);
        mRecyclerView.setAdapter(adapter);
        mActivityChat.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mActivityChat.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                mSwipeRefresh.setRefreshing(true);
                //自动刷新
                queryMessages(null);
            }
        });

        //下拉加载
        mSwipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                BmobIMMessage msg = adapter.getFirstMessage();
                queryMessages(msg);
            }
        });

        //设置RecyclerView的点击事件
        adapter.setOnRecyclerViewListener(new OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position) {
                Log.e("", "" + position);
            }

            @Override
            public boolean onItemLongClick(int position) {
                //这里省了个懒，直接长按就删除了该消息
                c.deleteMessage(adapter.getItem(position));
                adapter.remove(position);
                return true;
            }
        });
    }

    //初始化语音界面
    private void initVoiceView() {
        mBtnSpeak.setOnTouchListener(new VoiceTouchListener());
        initVoiceAnimRes();
        initRecordManager();
    }

    private void initNaviView() {
        mToolbar.setTitle(c.getConversationTitle().toString());
    }


    /**
     * 初始化语音动画资源
     *
     * @param
     * @return void
     * @Title: initVoiceAnimRes
     */
    private void initVoiceAnimRes() {
        drawable_Anims = new Drawable[]{
                getResources().getDrawable(R.drawable.chat_icon_voice2),
                getResources().getDrawable(R.drawable.chat_icon_voice3),
                getResources().getDrawable(R.drawable.chat_icon_voice4),
                getResources().getDrawable(R.drawable.chat_icon_voice5),
                getResources().getDrawable(R.drawable.chat_icon_voice6)};
    }

    private void initRecordManager() {
        // 语音相关管理器
        recordManager = BmobRecordManager.getInstance(this);
        // 设置音量大小监听--在这里开发者可以自己实现：当剩余10秒情况下的给用户的提示，类似微信的语音那样
        recordManager.setOnRecordChangeListener(new OnRecordChangeListener() {

            @Override
            public void onVolumnChanged(int value) {
                mIvRecord.setImageDrawable(drawable_Anims[value]);
            }

            @Override
            public void onTimeChanged(int recordTime, String localPath) {
                Log.e("voice", "已录音长度:" + recordTime);
                if (recordTime >= BmobRecordManager.MAX_RECORD_TIME) {// 1分钟结束，发送消息
                    // 需要重置按钮
                    mBtnSpeak.setPressed(false);
                    mBtnSpeak.setClickable(false);
                    // 取消录音框
                    mLayoutRecord.setVisibility(View.INVISIBLE);
                    // 发送语音消息
                    sendVoiceMessage(localPath, recordTime);
                    //是为了防止过了录音时间后，会多发一条语音出去的情况。
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            mBtnSpeak.setClickable(true);
                        }
                    }, 1000);
                }
            }
        });
    }

    /**
     * 发送语音消息
     *
     * @param local
     * @param length
     * @return void
     * @Title: sendVoiceMessage
     */
    private void sendVoiceMessage(String local, int length) {
        BmobIMAudioMessage audio = new BmobIMAudioMessage(local);
        //可设置额外信息-开发者设置的额外信息，需要开发者自己从extra中取出来
        Map<String, Object> map = new HashMap<>();
        map.put("from", "优酷");
        audio.setExtraMap(map);
        //设置语音文件时长：可选
//        audio.setDuration(length);
        c.sendMessage(audio, listener);
    }

    /**
     * 消息发送监听器
     */
    public static MessageSendListener listener = new MessageSendListener() {

        @Override
        public void onProgress(int value) {
            super.onProgress(value);
            //文件类型的消息才有进度值
            Log.e("onProgress：", "" + value);
        }

        @Override
        public void onStart(BmobIMMessage msg) {
            super.onStart(msg);
            adapter.addMessage(msg);
//            mEditMsg.setText("");
            scrollToBottom();
        }

        @Override
        public void done(BmobIMMessage msg, BmobException e) {
            adapter.notifyDataSetChanged();
//            mEditMsg.setText("");
            scrollToBottom();
            if (e != null) {
//                toast(e.getMessage());
            }
        }
    };

    @Override
    public void onMessageReceive(List<MessageEvent> list) {
        Log.e("聊天页面接收到消息：", "" + list.size());
        //当注册页面消息监听时候，有消息（包含离线消息）到来时会回调该方法
        for (int i = 0; i < list.size(); i++) {
            addMessage2Chat(list.get(i));
        }
    }

    private void addMessage2Chat(MessageEvent event) {
        BmobIMMessage msg = event.getMessage();
        if (c != null && event != null && c.getConversationId().equals(event.getConversation().getConversationId()) //如果是当前会话的消息
                && !msg.isTransient()) {//并且不为暂态消息
            if (adapter.findPosition(msg) < 0) {//如果未添加到界面中
                adapter.addMessage(msg);
                //更新该会话下面的已读状态
                c.updateReceiveStatus(msg);
            }
            scrollToBottom();
        } else {
            Log.e("chatActivity", "不是与当前聊天对象的消息");
        }
    }


    /**
     * 长按说话
     *
     * @author smile
     * @date 2014-7-1 下午6:10:16
     */
    class VoiceTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (!Util.checkSdCard()) {
                        toast("发送语音需要sdcard支持！");
                        return false;
                    }
                    try {
                        v.setPressed(true);
                        mLayoutRecord.setVisibility(View.VISIBLE);
                        mTvVoiceTips.setText(getString(R.string.voice_cancel_tips));
                        // 开始录音
                        recordManager.startRecording(c.getConversationId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                case MotionEvent.ACTION_MOVE: {
                    if (event.getY() < 0) {
                        mTvVoiceTips.setText(getString(R.string.voice_cancel_tips));
                        mTvVoiceTips.setTextColor(Color.RED);
                    } else {
                        mTvVoiceTips.setText(getString(R.string.voice_up_tips));
                        mTvVoiceTips.setTextColor(Color.WHITE);
                    }
                    return true;
                }
                case MotionEvent.ACTION_UP:
                    v.setPressed(false);
                    mLayoutRecord.setVisibility(View.INVISIBLE);
                    try {
                        if (event.getY() < 0) {// 放弃录音
                            recordManager.cancelRecording();
                            Log.e("voice", "放弃发送语音");
                        } else {
                            int recordTime = recordManager.stopRecording();
                            if (recordTime > 1) {
                                // 发送语音文件
                                sendVoiceMessage(recordManager.getRecordFilePath(c.getConversationId()), recordTime);
                            } else {// 录音时间过短，则提示录音过短的提示
                                mLayoutRecord.setVisibility(View.GONE);
                                showShortToast().show();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                default:
                    return false;
            }
        }
    }

    Toast toast;

    /**
     * 显示录音时间过短的Toast
     *
     * @return void
     * @Title: showShortToast
     */
    private Toast showShortToast() {
        if (toast == null) {
            toast = new Toast(this);
        }
        View view = LayoutInflater.from(this).inflate(
                R.layout.include_chat_voice_short, null);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        return toast;
    }


    private void initBottomView() {
        mEditMsg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_UP) {
                    scrollToBottom();
                }
                return false;
            }
        });
        mEditMsg.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                scrollToBottom();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    mBtnChatSend.setVisibility(View.VISIBLE);
                    mBtnChatKeyboard.setVisibility(View.GONE);
                    mBtnChatVoice.setVisibility(View.GONE);
                } else {
                    if (mBtnChatVoice.getVisibility() != View.VISIBLE) {
                        mBtnChatVoice.setVisibility(View.VISIBLE);
                        mBtnChatSend.setVisibility(View.GONE);
                        mBtnChatKeyboard.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private static void scrollToBottom() {
        layoutManager.scrollToPositionWithOffset(adapter.getItemCount() - 1, 0);
    }


    //点击加号，
    @OnClick(R.id.btn_chat_add)
    public void onAddClick(View view) {
        if (mLayoutMore.getVisibility() == View.GONE) {
            mLayoutMore.setVisibility(View.VISIBLE);
            mLayoutAdd.setVisibility(View.VISIBLE);
            mLayoutEmo.setVisibility(View.GONE);
            hideSoftInputView();
        } else {
            if (mLayoutEmo.getVisibility() == View.VISIBLE) {
                mLayoutEmo.setVisibility(View.GONE);
                mLayoutAdd.setVisibility(View.VISIBLE);
            } else {
                mLayoutMore.setVisibility(View.GONE);
            }
        }
    }

    //点击发送
    @OnClick(R.id.btn_chat_send)
    public void onSendClick(View view) {
        sendMessage();
        mEditMsg.setText("");
    }

    /**
     * 发送文本消息
     */
    private void sendMessage() {
        String text = mEditMsg.getText().toString();
        if (TextUtils.isEmpty(text.trim())) {
            toast("请输入内容");
            return;
        }
        BmobIMTextMessage msg = new BmobIMTextMessage();
        msg.setContent(text);
        //可设置额外信息
        Map<String, Object> map = new HashMap<>();
        map.put("level", "1");//随意增加信息
        msg.setExtraMap(map);
        c.sendMessage(msg, listener);

    }


    //点击语音图标，切换成长按说话
    @OnClick(R.id.btn_chat_voice)
    public void onVoiceClick(View view) {
        mEditMsg.setVisibility(View.GONE);
        mLayoutMore.setVisibility(View.GONE);
        mBtnChatVoice.setVisibility(View.GONE);
        mBtnChatKeyboard.setVisibility(View.VISIBLE);
        mBtnSpeak.setVisibility(View.VISIBLE);
        hideSoftInputView();
    }

    //点击键盘，切换到文本输入，也就是非语音状态
    @OnClick(R.id.btn_chat_keyboard)
    public void onKeyClick(View view) {
        showEditState(false);
    }

    private void showEditState(boolean b) {
        mEditMsg.setVisibility(View.VISIBLE);
        mBtnChatKeyboard.setVisibility(View.GONE);
        mBtnChatVoice.setVisibility(View.VISIBLE);
        mBtnSpeak.setVisibility(View.GONE);
        mEditMsg.requestFocus();
        if (b) {
            mLayoutMore.setVisibility(View.VISIBLE);
            mLayoutEmo.setVisibility(View.VISIBLE);
            mLayoutAdd.setVisibility(View.GONE);
            hideSoftInputView();
        } else {
            mLayoutMore.setVisibility(View.GONE);
            showSoftInputView();
        }
    }

    //点击更多中的图片
    @OnClick(R.id.tv_picture)
    public void onPictureClick(View view) {
//        BmobIMImageMessage image =new BmobIMImageMessage(Environment.getExternalStorageDirectory()+"/a.jpg");
//        c.sendMessage(image, listener);
        openAlbum();
    }

    //点击更多中的照相
    @OnClick(R.id.tv_camera)
    public void onCameraClick(View view) {
        Intent intent = new Intent(Chat2Activity.this, JCameraActivity.class);
        startActivityForResult(intent, 102);
    }

    public static void sendVideo(String url) {
        if (url != null) {
            BmobIMVideoMessage videoMessage = new BmobIMVideoMessage(url);
            c.sendMessage(videoMessage, listener);
        }
    }


    //点击更多中的位置
    @OnClick(R.id.tv_location)
    public void onLocationClick(View view) {
//        sendLocationMessage();
    }


    /**
     * 照相
     */


    /**
     * 打开相册选择头像
     */
    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, 101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        } else {
            switch (requestCode) {
                case 100:

                    break;
                case 101:
                    Uri originalUri = data.getData();//获取图片uri
                    //以下方法将获取的uri转为String类型哦。
                    String[] imgs1 = {MediaStore.Images.Media.DATA};//将图片URI转换成存储路径
                    Cursor cursor = this.managedQuery(originalUri, imgs1, null, null, null);
                    int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    String img_url = cursor.getString(index);
                    sendPhoto2(img_url);
                    break;
                case 102:

                    break;
            }
        }
    }

    public static void sendPhoto(Bitmap bitmap) {
        File file=new File(Environment.getExternalStorageDirectory(),"photo.jpg");//将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BmobIMImageMessage image = new BmobIMImageMessage(file);
        c.sendMessage(image, listener);
    }

    private void sendPhoto2(String url) {
        BmobIMImageMessage image = new BmobIMImageMessage(url);
        c.sendMessage(image, listener);
    }



    /**
     * 首次加载，可设置msg为null，下拉刷新的时候，默认取消息表的第一个msg作为刷新的起始时间点，默认按照消息时间的降序排列
     *
     * @param msg
     */
    public void queryMessages(BmobIMMessage msg) {
        c.queryMessages(msg, 10, new MessagesQueryListener() {
            @Override
            public void done(List<BmobIMMessage> list, BmobException e) {
                mSwipeRefresh.setRefreshing(false);
                if (e == null) {
                    if (null != list && list.size() > 0) {
                        adapter.addMessages(list);
                        layoutManager.scrollToPositionWithOffset(list.size() - 1, 0);
                    }
                } else {
                    toast(e.getMessage() + "(" + e.getErrorCode() + ")");
                }
            }
        });
    }

    /**
     * 隐藏软键盘
     */
    public void hideSoftInputView() {
        InputMethodManager manager = ((InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE));
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 显示软键盘
     */
    public void showSoftInputView() {
        if (getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                        .showSoftInput(mEditMsg, 0);
        }
    }

    @Override
    protected void onResume() {
        //锁屏期间的收到的未读消息需要添加到聊天界面中
        addUnReadMessage();
        //添加页面消息监听器
        BmobIM.getInstance().addMessageListHandler(this);
        // 有可能锁屏期间，在聊天界面出现通知栏，这时候需要清除通知
        BmobNotificationManager.getInstance(this).cancelNotification();
        super.onResume();
    }

    /**
     * 添加未读的通知栏消息到聊天界面
     */
    private void addUnReadMessage() {
        List<MessageEvent> cache = BmobNotificationManager.getInstance(this).getNotificationCacheList();
        if (cache.size() > 0) {
            int size = cache.size();
            for (int i = 0; i < size; i++) {
                MessageEvent event = cache.get(i);
                addMessage2Chat(event);
            }
        }
        scrollToBottom();
    }


    @Override
    protected void onPause() {
        //移除页面消息监听器
        BmobIM.getInstance().removeMessageListHandler(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        //清理资源
        if (recordManager != null) {
            recordManager.clear();
        }
        //更新此会话的所有消息为已读状态
        if (c != null) {
            c.updateLocalCache();
        }
        hideSoftInputView();
        super.onDestroy();
    }
}
