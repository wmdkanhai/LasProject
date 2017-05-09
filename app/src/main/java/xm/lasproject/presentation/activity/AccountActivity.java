package xm.lasproject.presentation.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import xm.lasproject.R;
import xm.lasproject.bean.User;

public class AccountActivity extends AppCompatActivity {

    @BindView(R.id.tb_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tv_username)
    TextView mTvUsername;
    @BindView(R.id.ll_changePassword)
    LinearLayout mLlChangePassword;
    @BindView(R.id.ll_forgetPassword)
    LinearLayout mLlForgetPassword;
    @BindView(R.id.ll_exit)
    LinearLayout mLlExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        ButterKnife.bind(this);
        initView();
        initData();
        setListener();
    }

    /**
     * 给item设置监听事件
     */
    private void setListener() {
        //点击更改密码，到更改密码页面
        mLlChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountActivity.this, ChangePassword.class));
            }
        });

        //点击忘记密码，到找回密码页面
        mLlForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountActivity.this, RetrievePassword.class));
            }
        });

        //点击退出当前账号
        mLlExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //退出，清除本地资源
                SharedPreferences sharedPreferences = getSharedPreferences("user", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                User.logOut(AccountActivity.this);   //清除缓存用户对象
//                android.os.Process.killProcess(android.os.Process.myPid());   //获取PID
//                System.exit(0);   //常规java、c#的标准退出法，返回值为0代表正常退出
                finish();
                startActivity(new Intent(AccountActivity.this, LoginActivity.class));
            }
        });
    }

    private void initData() {
        SharedPreferences sharedPreferences = getSharedPreferences("user",
                Activity.MODE_PRIVATE);
        //获取保存在本地的用户名、密码
        String username = sharedPreferences.getString("username", "");
        mTvUsername.setText(username);
    }

    /**
     * 初始化控件，Toolbar的设置
     */
    private void initView() {
        //显示Toolbar左边的返回
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
