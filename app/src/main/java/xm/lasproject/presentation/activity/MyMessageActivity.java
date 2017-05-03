package xm.lasproject.presentation.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.feezu.liuli.timeselector.TimeSelector;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import xm.lasproject.R;
import xm.lasproject.bean.User;

public class MyMessageActivity extends AppCompatActivity {

    @BindView(R.id.tb_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tv_time)
    TextView mTvTime;
    @BindView(R.id.imageView)
    ImageView mImageView;
    @BindView(R.id.rl1)
    RelativeLayout mRl1;
    @BindView(R.id.tv_nickName)
    TextView mTvNickName;
    @BindView(R.id.rl2)
    RelativeLayout mRl2;
    @BindView(R.id.tv_birthday)
    TextView mTvBirthday;
    @BindView(R.id.rl3)
    RelativeLayout mRl3;
    @BindView(R.id.rl4)
    RelativeLayout mRl4;
    @BindView(R.id.rl_setTime)
    RelativeLayout mRlSetTime;
    @BindView(R.id.tv_sex)
    TextView mTvSex;
    private TimeSelector mTimeSelector1;
    private TimeSelector mTimeSelector2;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private String mNickName;
    private User mUser;
    private String mObjectId;
    private String mPairing;
    private String mSex;

    private String mFilePath;
    private Uri mImageUri1;
    private File mFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_message);
        ButterKnife.bind(this);
        mSharedPreferences = getSharedPreferences("user", Activity.MODE_PRIVATE);
        //实例化SharedPreferences.Editor对象
        mEditor = mSharedPreferences.edit();
        mObjectId = mSharedPreferences.getString("objectId", "");

        mUser = new User();
        initView();
        initData();
        setListener();
    }

    private void setListener() {
        //点击选择恋爱时间
        mRlSetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTimeSelector1.show();
            }
        });

        //点击更改我的头像
        mRl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MyMessageActivity.this, "点击了我的头像", Toast.LENGTH_SHORT).show();
//                String path = Environment.getExternalStorageDirectory() + "/a.jpg";
//                Log.e(path, "path: " + path);
//                File file1 = new File(path);
//                BmobFile file = new BmobFile(file1);
//
//                file.uploadblock(MyMessageActivity.this, new UploadFileListener() {
//                    @Override
//                    public void onSuccess() {
//
//                    }
//
//                    @Override
//                    public void onFailure(int i, String s) {
//
//                    }
//                });
//                mUser.setPhoto(file);
//                if (mObjectId != "") {
//                    mUser.update(MyMessageActivity.this, mObjectId, new UpdateListener() {
//                        @Override
//                        public void onSuccess() {
//                            Log.i("bmob", "更新成功");
//                        }
//
//                        @Override
//                        public void onFailure(int i, String s) {
//                            Log.i("bmob", "更新失败" + s);
//                        }
//                    });
//                }

                final AlertDialog.Builder builder = new AlertDialog.Builder(MyMessageActivity.this);
                builder.setTitle("这里是标题");
                builder.setIcon(R.mipmap.ic_launcher);
                final String[] itemString = {"拍照", "相册"};
                builder.setItems(itemString, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.e("----", "onClick: " + which);
                        if (0 == which) {
                            takePhoto();
                        } else {
                            openAlbum();
                        }
                    }
                });
                builder.show();
            }
        });

        //点击我的昵称
        mRl2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText et = new EditText(MyMessageActivity.this);
                mNickName = mSharedPreferences.getString("nickName", "请设置");
                et.setText(mNickName);
                Toast.makeText(MyMessageActivity.this, "昵称", Toast.LENGTH_SHORT).show();
                new AlertDialog.Builder(MyMessageActivity.this)
                        .setTitle("请输入昵称")
                        .setView(et)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                mTvNickName.setText(et.getText().toString());
                                //用putString的方法保存数据
                                mEditor.putString("nickName", et.getText().toString());
                                //提交当前数据
                                mEditor.apply();
                                //更新数据库
                                String nickName = mSharedPreferences.getString("nickName", "");
                                mUser.setNickName(nickName);
                                if (mObjectId != "") {
                                    mUser.update(MyMessageActivity.this, mObjectId, new UpdateListener() {
                                        @Override
                                        public void onSuccess() {
                                            Log.i("bmob", "更新成功");
                                            Toast.makeText(MyMessageActivity.this, "昵称修改成功", Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onFailure(int i, String s) {
                                            Log.i("bmob", "更新失败" + s);
                                        }
                                    });
                                }
                            }
                        }).setNegativeButton("取消", null).show();
            }
        });

        //点击我的生日
        mRl3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTimeSelector2.show();
            }
        });

        final int sexIndex;
        if ("男".equals(mSex)){
            sexIndex = 0;
        }else if("女".equals(mSex)) {
            sexIndex = 1;
        }else {
            sexIndex = 2;
        }
        //点击设置性别
        mRl4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MyMessageActivity.this);
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setTitle("请选择性别");
                final String[] sex = {"男", "女"};
                //    设置一个单项选择下拉框
                /**
                 * 第一个参数指定我们要显示的一组下拉单选框的数据集合
                 * 第二个参数代表索引，指定默认哪一个单选框被勾选上，1表示默认'女' 会被勾选上
                 * 第三个参数给每一个单选项绑定一个监听器
                 */
                builder.setSingleChoiceItems(sex, sexIndex, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(MyMessageActivity.this, "性别为：" + sex[which], Toast.LENGTH_SHORT).show();
                        //用putString的方法保存数据
                        mEditor.putString("sex", sex[which]);
                        //提交当前数据
                        mEditor.apply();
                        //更新数据库
                        String sex1 = mSharedPreferences.getString("sex", "");
                        mTvSex.setText(sex1);
                        mUser.setSex(sex[which]);

                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mObjectId != "") {
                            mUser.update(MyMessageActivity.this, mObjectId, new UpdateListener() {
                                @Override
                                public void onSuccess() {
                                    Toast.makeText(MyMessageActivity.this, "设置性别成功", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(int i, String s) {
                                    Toast.makeText(MyMessageActivity.this, "出错了："+s, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
        });

    }

    private void initData() {

        String loveTime = mSharedPreferences.getString("loveTime", "请设置");
        String birthday = mSharedPreferences.getString("birthday", "请设置");
        String photo = mSharedPreferences.getString("photo", "");

        mSex = mSharedPreferences.getString("sex", "请设置");
        mTvSex.setText(mSex);

        mNickName = mSharedPreferences.getString("nickName", "请设置");
        mTvTime.setText(loveTime);
        mTvBirthday.setText(birthday);
        mTvNickName.setText(mNickName);

        if ("".equals(photo)){
            Glide.with(this).load(R.mipmap.ic_launcher).into(mImageView);
        }else {
            Glide.with(this).load(photo).into(mImageView);
        }

        //获取当前系统的时间
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String date = sDateFormat.format(new Date());

        //选择恋爱日期使用时间选择器选择时间
        mTimeSelector1 = new TimeSelector(this, new TimeSelector.ResultHandler() {
            @Override
            public void handle(String time) {
                //1、在页面中显示时间、
                String[] split = time.split(" ");
                String loveTime = split[0];
                mTvTime.setText(loveTime);
                //2、在sp中记录时间
                //用putString的方法保存数据
                mEditor.putString("loveTime", loveTime);
                //提交当前数据
                mEditor.apply();
                //3、在数据库中更新时间
                mUser.setLoveTime(loveTime);
                if (mObjectId != "") {
                    mUser.update(MyMessageActivity.this, mObjectId, new UpdateListener() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onFailure(int i, String s) {

                        }
                    });
                }
            }
        }, "1990-05-15 00:00", date);
        mTimeSelector1.setMode(TimeSelector.MODE.YMD);//只显示 年月日

        //选择生日使用时间选择器选择时间
        mTimeSelector2 = new TimeSelector(this, new TimeSelector.ResultHandler() {
            @Override
            public void handle(String time) {
                //1、在页面中显示时间、
                String[] split = time.split(" ");
                String birthday = split[0];
                mTvBirthday.setText(birthday);
                //2、在sp中记录时间
                mSharedPreferences = getSharedPreferences("user", Activity.MODE_PRIVATE);
                //实例化SharedPreferences.Editor对象
                //用putString的方法保存数据
                mEditor.putString("birthday", birthday);
                //提交当前数据
                mEditor.apply();
                //3、在数据库中更新时间
                mUser.setBirthday(birthday);
                if (mObjectId != "") {
                    mUser.update(MyMessageActivity.this, new UpdateListener() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onFailure(int i, String s) {

                        }
                    });
                }

            }
        }, "1990-05-15 00:00", date);
        mTimeSelector2.setMode(TimeSelector.MODE.YMD);//只显示 年月日
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
//        String path = "http://bmob-cdn-10014.b0.upaiyun.com/2017/03/28/905a5c21401a4278800367dbbd182e00.jpg";
//        String path = Environment.getExternalStorageDirectory() + "/a.jpg";

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_friend_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                mPairing = mSharedPreferences.getString("pairing", "");
                if ("0".equals(mPairing)) {
                    startActivity(new Intent(this, InviteFriendActivity.class));
                } else {
                    Toast.makeText(this, "您已经添加过了", Toast.LENGTH_SHORT).show();
                }
                break;

        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * 照相
     */
    private void takePhoto() {
        mFile = new File(Environment.getExternalStorageDirectory(), "a.jpg");
        if (mFile.exists()){
            mFile.delete();
        }
        try {
            mFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mImageUri1 = Uri.fromFile(mFile);
        mFilePath = mFile.getPath();

        //启动相机程序
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri1);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
        startActivityForResult(intent, 100);
    }


    /**
     * 打开相册选择头像
     */
    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, 101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        } else {
            switch (requestCode) {
                case 100:
                    BmobFile icon = new BmobFile(new File(mFilePath));
                    Glide.with(MyMessageActivity.this).load(mFile).into(mImageView);
                    updata(icon);
                    break;
                case 101:
                    Uri originalUri = data.getData();//获取图片uri
                    //以下方法将获取的uri转为String类型哦。
                    String[] imgs1 = {MediaStore.Images.Media.DATA};//将图片URI转换成存储路径
                    Cursor cursor = this.managedQuery(originalUri, imgs1, null, null, null);
                    int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    String img_url = cursor.getString(index);
                    Glide.with(MyMessageActivity.this).load(img_url).into(mImageView);
                    final BmobFile icon2 = new BmobFile(new File(img_url));
                    updata(icon2);
                    break;
            }
        }
    }

    /**
     * 更新头像到Bmob
     * @param icon
     */
    private void updata(final BmobFile icon) {
        icon.uploadblock(this, new UploadFileListener() {
            @Override
            public void onSuccess() {
                final User user = BmobUser.getCurrentUser(MyMessageActivity.this, User.class);
                user.setPhoto(icon.getFileUrl(MyMessageActivity.this));
                user.update(MyMessageActivity.this,user.getObjectId(), new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        mEditor.putString("photo", user.getPhoto());
                        //提交当前数据
                        mEditor.apply();
                    }

                    @Override
                    public void onFailure(int i, String s) {

                    }
                });
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
    }
}
