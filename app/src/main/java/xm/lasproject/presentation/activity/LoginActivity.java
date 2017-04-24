package xm.lasproject.presentation.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import xm.lasproject.R;
import xm.lasproject.bean.User;
import xm.lasproject.presentation.contract.ILoginContract;
import xm.lasproject.presentation.presenter.LoginPresenter;

/**
 * 登录界面
 */
public class LoginActivity extends AppCompatActivity implements ILoginContract.View {

    @BindView(R.id.tv_to_signup)
    TextView mTvToSignup;
    @BindView(R.id.et_username)
    EditText mEtUsername;
    @BindView(R.id.et_password)
    EditText mEtPassword;
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    private LoginPresenter mLoginPresenter;
    private User mUser;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        initView();
        mLoginPresenter = new LoginPresenter(this,this);
        mSharedPreferences =  getSharedPreferences("user", Activity.MODE_PRIVATE);

    }

    private void initView() {
        //点击跳转到注册页面
        mTvToSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });

        //点击左上角的返回的点击事件
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //登录
    @OnClick(R.id.btn_login)
    public void loginClick() {
        mUser = new User();
        mUser.setUsername(mEtUsername.getText().toString());
        mUser.setPassword(mEtPassword.getText().toString());
        mLoginPresenter.login(mUser);
        //实例化SharedPreferences.Editor对象
        mEditor = this.mSharedPreferences.edit();

    }


    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, "出错了"+message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void success() {
        Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }

    @Override
    public void setPresenter(ILoginContract.Presenter presenter) {
    }

    @Override
    public void getUserInfoByObjectId(User user){
        Log.e("LoginActivity-----", "getUserInfoByObjectId: "+user.toString());
        mEditor.putString("objectId", user.getObjectId());
        //用putString的方法保存数据
        mEditor.putString("username", user.getUsername());
        mEditor.putString("nickName", user.getNickName());
        mEditor.putString("loveTime", user.getLoveTime());
        mEditor.putString("birthday", user.getUsername());
        mEditor.putString("sex", user.getSex());
        mEditor.putString("birthday", user.getBirthday());
        mEditor.putString("pairing", user.getPairing());
        mEditor.putString("pairingInfo", user.getPairingInfo());
        mEditor.putString("pairingTime", user.getPairingTime());
        mEditor.putString("registTime", user.getRegistTime());

        //提交当前数据
        mEditor.apply();
    }
}

