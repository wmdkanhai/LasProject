package xm.lasproject.presentation.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.GetListener;
import xm.lasproject.R;
import xm.lasproject.bean.User;
import xm.lasproject.data.AddFriendMessage;
import xm.lasproject.presentation.adapter.BaseRecyclerAdapter;
import xm.lasproject.presentation.adapter.SearchUserAdapter;
import xm.lasproject.presentation.contract.IInviteFriendContract;
import xm.lasproject.presentation.presenter.InviteFriendPresenter;

/**
 * 请求添加好友
 */
public class InviteFriendActivity extends AppCompatActivity implements IInviteFriendContract.View{

    @BindView(R.id.tb_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.et_find_name)
    EditText mEtFindName;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;

    private InviteFriendPresenter mInviteFriendPresenter;
    private SearchUserAdapter mSearchUserAdapter;
    BmobIMUserInfo info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friend);
        ButterKnife.bind(this);

        mInviteFriendPresenter = new InviteFriendPresenter(this,this);

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

        //刷新
        mSwipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mInviteFriendPresenter.query(mEtFindName.getText().toString());
            }
        });

    }


    @OnClick(R.id.btn_search)
    public void onSearchClick(View view){
        if (TextUtils.isEmpty(mEtFindName.getText().toString())){
            Toast.makeText(this, "请填写用户名", Toast.LENGTH_SHORT).show();
            mSwipeRefresh.setRefreshing(false);
            return;
        }
        this.showLoading();
        mInviteFriendPresenter.query(mEtFindName.getText().toString());
    }

    @Override
    public void showLoading() {
        mSwipeRefresh.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        mSwipeRefresh.setRefreshing(false);
    }

    @Override
    public void showError(String message) {
        mSwipeRefresh.setRefreshing(false);
        Toast.makeText(this, "出错了："+message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void success(final List<User> list) {
        if (list != null && list.size() > 0) {
            //获取到数据：
            this.hideLoading();
            mSearchUserAdapter = new SearchUserAdapter(this, list);
            mRecyclerView.setAdapter(mSearchUserAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mSearchUserAdapter.notifyDataSetChanged();

            //点击item的时候的事件---》发出添加好友请求
            mSearchUserAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {


                @Override
                public void onItemClick(View itemView, int pos) {
                    //构造聊天方的用户信息:传入用户id、用户名和用户头像三个参数
                    String fileUrl = list.get(pos).getPhoto().getFileUrl(InviteFriendActivity.this);
                    if (fileUrl.equals("")){
                        fileUrl = "https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo/logo_redBlue.png";
                    }
                    String objectId = list.get(pos).getObjectId();
                    String username = list.get(pos).getUsername();
                    info = new BmobIMUserInfo(objectId,username,fileUrl);
                    //在发送请求前，应该判断用户的性别
                    User user = BmobUser.getCurrentUser(InviteFriendActivity.this, User.class);
                    final String sex = user.getSex();
                    BmobQuery<User> query = new BmobQuery<>();
                    query.getObject(InviteFriendActivity.this, objectId, new GetListener<User>() {
                        @Override
                        public void onSuccess(User user) {
                            String sexById = user.getSex();
                            if (sex.equals(sexById)){
                                Toast.makeText(InviteFriendActivity.this, "性别一致啊！！！", Toast.LENGTH_SHORT).show();
                            }else {
                                sendAddFriendMessage(info);
                            }
                        }

                        @Override
                        public void onFailure(int i, String s) {

                        }
                    });



//                    BmobIMConversation c = BmobIM.getInstance().startPrivateConversation(info,false,null);
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("c", c);
//                    startActivity(Chat2Activity.class, bundle, false);
                }
            });

        } else {
           showError("没有此用户");
        }
    }

    public void startActivity(Class<? extends Activity> target, Bundle bundle, boolean finish) {
        Intent intent = new Intent();
        intent.setClass(this, target);
        if (bundle != null)
            intent.putExtra(getPackageName(), bundle);
        startActivity(intent);
        if (finish)
            finish();
    }


    /**
     * 发送添加好友的请求
     */
    private void sendAddFriendMessage(BmobIMUserInfo info) {
        //启动一个会话，如果isTransient设置为true,则不会创建在本地会话表中创建记录，
        //设置isTransient设置为false,则会在本地数据库的会话列表中先创建（如果没有）与该用户的会话信息，且将用户信息存储到本地的用户表中
        BmobIMConversation c = BmobIM.getInstance().startPrivateConversation(info, true,null);
        //这个obtain方法才是真正创建一个管理消息发送的会话
        BmobIMConversation conversation = BmobIMConversation.obtain(BmobIMClient.getInstance(), c);
        AddFriendMessage msg =new AddFriendMessage();
//        User currentUser = BmobUser.getCurrentUser(User.class);
        User currentUser = BmobUser.getCurrentUser(InviteFriendActivity.this, User.class);
        msg.setContent("很高兴认识你，可以加个好友吗?");//给对方的一个留言信息
        Map<String,Object> map =new HashMap<>();
        map.put("name", currentUser.getUsername());//发送者姓名，这里只是举个例子，其实可以不需要传发送者的信息过去
//        map.put("avatar",currentUser.getPhoto().getFileUrl());//发送者的头像
        map.put("avatar","");//发送者的头像
        map.put("uid",currentUser.getObjectId());//发送者的uid
        msg.setExtraMap(map);
        conversation.sendMessage(msg, new MessageSendListener() {
            @Override
            public void done(BmobIMMessage bmobIMMessage, BmobException e) {
                if (e == null) {//发送成功
                    Toast.makeText(InviteFriendActivity.this, "好友请求发送成功，等待验证", Toast.LENGTH_SHORT).show();
                } else {//发送失败
                    Toast.makeText(InviteFriendActivity.this, "发送失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void setPresenter(IInviteFriendContract.Presenter presenter) {
        presenter.query(mEtFindName.getText().toString());
    }
}
