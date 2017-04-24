package xm.lasproject.presentation.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import xm.lasproject.R;

/**
 * 欢迎界面，
 * 1、在此页面中判断用户是否已经登录，如果登录就进入主页面，否则进入登录界面
 * 2、做一些初始化的准备
 */
public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.image)
    ImageView mImage;

    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        sharedPreferences = getSharedPreferences("user",Activity.MODE_PRIVATE);
        final String username = sharedPreferences.getString("username","");
        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(1000);
                if ("".equals(username)){
                    finish();
                    startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                }else {
                    finish();
                    startActivity(new Intent(SplashActivity.this,MainActivity.class));
                }
            }
        }).start();
    }
}
