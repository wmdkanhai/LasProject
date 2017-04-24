package xm.lasproject.presentation.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.listener.UpdateListener;
import xm.lasproject.R;
import xm.lasproject.bean.User;

public class ChangePassword extends AppCompatActivity {

    @BindView(R.id.tb_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.et_old_password)
    EditText mEtOldPassword;
    @BindView(R.id.et_new_password)
    EditText mEtNewPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);
        initView();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.record_submit_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_submit:

                //bmob提供了修改密码的方法，传入旧密码和新密码
                User.updateCurrentUserPassword(this, mEtOldPassword.getText().toString(),
                        mEtNewPassword.getText().toString(), new UpdateListener() {
                            @Override
                            public void onSuccess() {
                                Toast.makeText(ChangePassword.this, "密码修改成功", Toast.LENGTH_SHORT).show();
                                finish();
                            }

                            @Override
                            public void onFailure(int i, String s) {
                                Toast.makeText(ChangePassword.this, "出错了："+s, Toast.LENGTH_SHORT).show();
                            }

                        });

                break;

        }
        return super.onOptionsItemSelected(item);
    }

}
