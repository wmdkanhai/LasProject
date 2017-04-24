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
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;
import xm.lasproject.R;
import xm.lasproject.bean.Record;
import xm.lasproject.bean.User;

public class AddRecordActivity extends AppCompatActivity {

    @BindView(R.id.tb_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.et_content)
    EditText mEtContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);
        ButterKnife.bind(this);
        
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
        switch (item.getItemId()){
            case R.id.action_submit:
                Record record = new Record();
                User user = BmobUser.getCurrentUser(AddRecordActivity.this, User.class);
                record.setUsername(user.getUsername());
                record.setUserObjectId(user.getObjectId());
                record.setUserPhotoUrl(user.getPhoto().getFileUrl(AddRecordActivity.this));
                record.setRecordContent(mEtContent.getText().toString());
                record.save(AddRecordActivity.this, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(AddRecordActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Toast.makeText(AddRecordActivity.this, "出错了："+s, Toast.LENGTH_SHORT).show();

                    }
                });
                break;
                
        }
        return super.onOptionsItemSelected(item);
    }
}
