package xm.lasproject.presentation.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.core.ConnectionStatus;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.newim.listener.ConnectStatusChangeListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import xm.lasproject.R;
import xm.lasproject.bean.User;
import xm.lasproject.event.RefreshEvent;
import xm.lasproject.presentation.fragment.CommunityFragment;
import xm.lasproject.presentation.fragment.ConversationFragment;
import xm.lasproject.presentation.fragment.RecordFragment;
import xm.lasproject.presentation.fragment.SettingFragment;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.rl_main)
    RelativeLayout mRlMain;
    @BindView(R.id.rb_0)
    RadioButton mRb0;
    @BindView(R.id.rb_1)
    RadioButton mRb1;
    @BindView(R.id.rb_2)
    RadioButton mRb2;
    @BindView(R.id.rb_3)
    RadioButton mRb3;
    @BindView(R.id.rg_tab)
    RadioGroup mRgTab;

    private FragmentManager mFragmentManager;
    private RecordFragment mRecordFragment;
    private ConversationFragment mConversationFragment;
    private CommunityFragment mCommunityFragment;
    private SettingFragment mSettingFragment;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initView();
        mRgTab.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                changeFragment(checkedId);
            }
        });
        changeFragment(R.id.rb_0);

        //connect server
        mUser = BmobUser.getCurrentUser(MainActivity.this, User.class);

        BmobIM.connect(mUser.getObjectId(), new ConnectListener() {
            @Override
            public void done(String uid, BmobException e) {
                if (e == null) {
                    Log.e("---","connect success");
                    //服务器连接成功就发送一个更新事件，同步更新会话及主页的小红点
                    EventBus.getDefault().post(new RefreshEvent());
                } else {
//                    Logger.e(e.getErrorCode() + "/" + e.getMessage());
                }
            }
        });
        //监听连接状态，也可通过BmobIM.getInstance().getCurrentStatus()来获取当前的长连接状态
        BmobIM.getInstance().setOnConnectStatusChangeListener(new ConnectStatusChangeListener() {
            @Override
            public void onChange(ConnectionStatus status) {
                Toast.makeText(MainActivity.this, ""+ status.getMsg(), Toast.LENGTH_SHORT).show();
            }
        });

        //解决leancanary提示InputMethodManager内存泄露的问题
//        IMMLeaks.fixFocusedViewLeak(getApplication());

    }


//    private void changeFragment(int checkedId) {
//        if (checkedId != R.id.rb_1) {
//            mFragmentManager = getSupportFragmentManager();
//            FragmentTransaction transaction = mFragmentManager.beginTransaction();
//            Fragment fragment = getInstanceByIndex(checkedId);
//            transaction.replace(R.id.rl_main, fragment).commit();
//        } else {
//            String pairing = mUser.getPairing();
//            if ("0".equals(pairing)) {
//                startActivity(new Intent(this, ChatActivity.class));
//            }else {
//                String pairingInfo = mUser.getPairingInfo();
//                String[] split = pairingInfo.split(",");
//                String paringId = split[0];
//                String paringUsername = split[1];
//                String paringFileUrl = split[2];
//                BmobIMUserInfo info = new BmobIMUserInfo(paringId,paringUsername,paringFileUrl);
//                BmobIMConversation c = BmobIM.getInstance().startPrivateConversation(info,false,null);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("c", c);
//                Intent intent = new Intent();
//                intent.putExtra(getPackageName(), bundle);
//                intent.setClass(this,Chat2Activity.class);
//                startActivity(intent);
//            }
////            finish();
//        }
//    }
private void changeFragment(int checkedId) {


//    if (checkedId == R.id.rb_1){
//        startActivity(new Intent(MainActivity.this,ChatActivity.class));
//    }else {
        mFragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        Fragment fragment = getInstanceByIndex(checkedId);
        transaction.replace(R.id.rl_main, fragment).commit();
//    }
}

    public Fragment getInstanceByIndex(int resId) {
        Fragment fragment = null;
        resetColor(resId);
        switch (resId) {
            case R.id.rb_0:
                fragment = mRecordFragment;
                break;
            case R.id.rb_1:
                fragment = mConversationFragment;
                break;
            case R.id.rb_2:
                fragment = mCommunityFragment;
                break;
            case R.id.rb_3:
                fragment = mSettingFragment;
                break;
            default:
                break;

        }
        return fragment;
    }

    private void resetColor(int resId) {
        for (int i = 0; i < mRgTab.getChildCount(); i++) {
            RadioButton radio = (RadioButton) mRgTab.getChildAt(i);
            radio.setTextColor(ContextCompat.getColor(this, R.color.txt_gray));
        }
        RadioButton radio = (RadioButton) findViewById(resId);
        radio.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
    }

    private void initView() {
        if (mRecordFragment == null) {
            mRecordFragment = RecordFragment.newInstance();
        }

        if (mConversationFragment == null){
//            mConversationFragment = new ConversationFragment(MainActivity.this);
            mConversationFragment = ConversationFragment.newInstance();
        }

        if (mCommunityFragment == null) {
            mCommunityFragment = CommunityFragment.newInstance();
        }

        if (mSettingFragment == null) {
            mSettingFragment = SettingFragment.newInstance();
        }

    }

    // 定义一个变量，来标识是否退出
    private static boolean isExit = false;

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                exit();
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
            System.exit(0);
        }
    }

}
