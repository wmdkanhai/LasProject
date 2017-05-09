package xm.lasproject.presentation.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import xm.lasproject.R;
import xm.lasproject.bean.User;
import xm.lasproject.presentation.contract.ISignUpContract;
import xm.lasproject.presentation.presenter.SignUpPresenter;

public class SignUpActivity extends AppCompatActivity implements ISignUpContract.View {

    @BindView(R.id.et_username)
    EditText mEtUsername;
    @BindView(R.id.et_password)
    EditText mEtPassword;
    @BindView(R.id.tb_toolbar)
    Toolbar mToolbar;

    private SignUpPresenter mSignUpPresenter;

    private User mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        mSignUpPresenter = new SignUpPresenter(this,this);

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

        EditText editText = (EditText) findViewById(R.id.et_new_password);
        Button btn_sign = (Button) findViewById(R.id.btn_sign_up);
    }

    /**
     * 点击注册事件
     */
    @OnClick(R.id.btn_sign_up)
    public void signUpClick() {
        //获取当前系统的时间
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String date = sDateFormat.format(new java.util.Date());
        mUser = new User();
        mUser.setUsername(mEtUsername.getText().toString());
        mUser.setPassword(mEtPassword.getText().toString());
        mUser.setRegistTime(date);
        mUser.setPairing("0");//是否配对，默认设置为0，表示没有配对
//        mUser.setPhoto(null);
//        mUser.setNickName(null);
//        mUser.setBirthday(null);
//        mUser.setLoveTime(null);
//        mUser.setPairingInfo(null);
//        mUser.setPhotoNumber(null);
//        mUser.setPairingTime(null);
//        mUser.setSex(null);

        mSignUpPresenter.signUp(mUser);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, "出错了" + message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPresenter(ISignUpContract.Presenter presenter) {
        presenter.signUp(mUser);
    }

    /**
     * 注册成功
     */
    @Override
    public void success() {
        Toast.makeText(SignUpActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
        finish();
    }
}
