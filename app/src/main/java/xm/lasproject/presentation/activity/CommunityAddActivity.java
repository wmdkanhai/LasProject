package xm.lasproject.presentation.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.listener.SaveListener;
import xm.lasproject.R;
import xm.lasproject.bean.CommunityTheme;

public class CommunityAddActivity extends AppCompatActivity {

    @BindView(R.id.tb_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.et_content)
    EditText mEtContent;
    @BindView(R.id.et_title)
    EditText mEtTitle;
    private SharedPreferences mSharedPreferences;
    private String mModeType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_add);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        mModeType = intent.getStringExtra("modeType");
        mSharedPreferences = getSharedPreferences("user", Activity.MODE_PRIVATE);
        initToolbar();
    }

    private void initToolbar() {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.record_submit_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_submit:
                saveCommunityTheme();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void saveCommunityTheme() {
//        Toast.makeText(this, "添加成功Toast.LENGTH_SHORT).show();
        if (mEtTitle.getText().toString().isEmpty() || mEtContent.getText().toString().isEmpty()){
            Toast.makeText(this, "标题或者内容不能为空", Toast.LENGTH_SHORT).show();
        }else {
            CommunityTheme communityTheme = new CommunityTheme();
            communityTheme.setUsername(mSharedPreferences.getString("username",""));
            communityTheme.setUserObjectId(mSharedPreferences.getString("objectId",""));
            communityTheme.setSex("男");
            communityTheme.setModeType(mModeType);
            communityTheme.setTitle(mEtTitle.getText().toString());
            communityTheme.setContent(mEtContent.getText().toString());
//        communityTheme.setPicture();
            //获取当前系统的时间
            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String date = sDateFormat.format(new java.util.Date());
            communityTheme.setTime(date);

            communityTheme.save(CommunityAddActivity.this, new SaveListener() {
                @Override
                public void onSuccess() {
                    finish();
                    Toast.makeText(CommunityAddActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(int i, String s) {
                    Toast.makeText(CommunityAddActivity.this, "提交失败"+s, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}

