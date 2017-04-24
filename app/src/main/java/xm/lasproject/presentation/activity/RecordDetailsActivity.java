package xm.lasproject.presentation.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.listener.DeleteListener;
import xm.lasproject.R;
import xm.lasproject.bean.Record;
import xm.lasproject.bean.RecordMode;

import static xm.lasproject.R.id.edit_msg;

public class RecordDetailsActivity extends AppCompatActivity {

    private static final String TAG = "RecordDetailsActivity";
    @BindView(R.id.tb_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.imageView)
    ImageView mImageView;
    @BindView(R.id.tv_time)
    TextView mTvTime;
    @BindView(R.id.tv_content)
    TextView mTvContent;
    //消息
    @BindView(edit_msg)
    EditText mEditMsg;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.btn_emo)
    Button mBtnEmo;
    @BindView(R.id.btn_send)
    Button mBtnSend;


    private RecordMode.ResultsBean mRecordDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_details);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        mRecordDetails = (RecordMode.ResultsBean) intent.getSerializableExtra("recordDetails");
        Log.e(TAG, "onCreate: " + mRecordDetails.toString());

        initView(mRecordDetails);
        initBottomView();
    }

    private void initView(RecordMode.ResultsBean recordDetails) {
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

        String createdAt = recordDetails.getCreatedAt();
        //获取到日期后，格式化时间，2017-03-14
        String[] split = createdAt.split(" ");
        mTvTime.setText(split[0]);
        mTvContent.setText(recordDetails.getRecordContent());
        Glide.with(this).load(recordDetails.getUserPhotoUrl()).into(mImageView);
    }

    private void initBottomView() {
        mEditMsg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_UP) {
//                    scrollToBottom();
                }
                return false;
            }
        });
        mEditMsg.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                scrollToBottom();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    //输入的内容不为空就设置可以发送
                } else {

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }


    //点击发送
    @OnClick(R.id.btn_send)
    public void onSendClick(View view) {
        if (mEditMsg.getText().toString().isEmpty()){
            Toast.makeText(this, "请输入评论内容", Toast.LENGTH_SHORT).show();
        }else {
            //保存评论到bmob服务器,要保存的内容：评论内容，记录的Id，评论人的信息
            String strComment = mEditMsg.getText().toString();
            String recordId = mRecordDetails.getObjectId();


        }
    }

    //点击表情
    @OnClick(R.id.btn_emo)
    public void onEmoClick() {
        Toast.makeText(this, "点击了表情", Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.record_details_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                Record record = new Record();
                record.delete(RecordDetailsActivity.this, mRecordDetails.getObjectId(), new DeleteListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(RecordDetailsActivity.this, "成功删除了这个记录", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Toast.makeText(RecordDetailsActivity.this, "出错了：" + s + i, Toast.LENGTH_SHORT).show();
                    }
                });
                break;

        }
        return super.onOptionsItemSelected(item);
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
                ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).showSoftInput(mEditMsg, 0);
        }
    }


}
