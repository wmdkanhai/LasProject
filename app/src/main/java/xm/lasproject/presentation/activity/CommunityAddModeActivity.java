package xm.lasproject.presentation.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.listener.SaveListener;
import xm.lasproject.R;
import xm.lasproject.bean.Community;

public class CommunityAddModeActivity extends AppCompatActivity {

    @BindView(R.id.tb_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.et_title)
    EditText mEtTitle;
    @BindView(R.id.et_content)
    EditText mEtContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_add_mode);
        ButterKnife.bind(this);

        initView();
    }

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
        switch (item.getItemId()) {
            case R.id.action_submit:
                saveCommunityMode();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void saveCommunityMode() {
        if (mEtTitle.getText().toString().isEmpty() || mEtContent.getText().toString().isEmpty()) {
            Toast.makeText(this, "标题或者内容不能为空", Toast.LENGTH_SHORT).show();
        } else {
            Community community = new Community("CommunityMode");
            Date date = new Date();
            DateFormat format=new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
            String time=format.format(date);
            community.setModeType(time);
            community.setModeDescribe(mEtTitle.getText().toString());
            community.setModeTitle(mEtContent.getText().toString());
//            community.setModePicture(new BmobFile(new File()));

            community.save(this, new SaveListener() {
                @Override
                public void onSuccess() {
                    Toast.makeText(CommunityAddModeActivity.this, "成功", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(int i, String s) {
                    Log.e("--", "onFailure: "+s );
                    Toast.makeText(CommunityAddModeActivity.this, "失败"+s, Toast.LENGTH_SHORT).show();

                }
            });
        }
    }
}
