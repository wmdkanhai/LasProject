package xm.lasproject.presentation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import xm.lasproject.R;
import xm.lasproject.bean.CommentInfo;
import xm.lasproject.bean.CommentMode;
import xm.lasproject.bean.CommunityList;
import xm.lasproject.bean.User;
import xm.lasproject.presentation.adapter.CommentListAdapter;
import xm.lasproject.presentation.contract.ICommunityDetailsContract;
import xm.lasproject.presentation.presenter.CommunityDetailsPresenter;

public class CommunityDetailsActivity extends AppCompatActivity implements ICommunityDetailsContract.View {

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tb_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;
    @BindView(R.id.tv_type)
    TextView mTvType;
    @BindView(R.id.userPhoto)
    ImageView mUserPhoto;
    @BindView(R.id.tv_username)
    TextView mTvUsername;
    @BindView(R.id.tv_time)
    TextView mTvTime;
    @BindView(R.id.tv_content)
    TextView mTvContent;
    @BindView(R.id.edit_msg)
    EditText mEditMsg;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    private CommunityList.ResultsBean mResultsBean;
    private CommunityDetailsPresenter mCommunityDetailsPresenter;
    private CommentInfo mCommentInfo;
    private String mRecordId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_details);
        ButterKnife.bind(this);

        initData();
        initView();
        mCommunityDetailsPresenter = new CommunityDetailsPresenter(this, this);
    }

    private void initData() {
        Intent intent = this.getIntent();
        mResultsBean = (CommunityList.ResultsBean) intent.getSerializableExtra("resultsBean");
        mTvTitle.setText(mResultsBean.getTitle());
        mRecordId = mResultsBean.getObjectId();
        String modeType = mResultsBean.getModeType();

        String modeTypeStr = null;
        switch (modeType) {
            case "1":
                modeTypeStr = "爱情分享";
                break;
            case "2":
                modeTypeStr = "恋爱问答";
                break;
            case "3":
                modeTypeStr = "异地恋爱";
                break;
            case "4":
                modeTypeStr = "失恋救治";
                break;
        }
        mTvType.setText(modeTypeStr);
        mTvContent.setText(mResultsBean.getContent());
        mTvTime.setText(mResultsBean.getTime());
        mTvUsername.setText(mResultsBean.getUsername());
        String sex = mResultsBean.getSex();
        if ("女".equals(sex)) {
            mTvUsername.setTextColor(getResources().getColor(R.color.username_sex_female_color));
        } else {
            mTvUsername.setTextColor(getResources().getColor(R.color.username_sex_male_color));
        }
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

    /**
     * 点击评论
     */
    @OnClick(R.id.btn_send)
    public void onSendClick() {
        if (mEditMsg.getText().toString().isEmpty()) {
            Toast.makeText(this, "请输入评论内容", Toast.LENGTH_SHORT).show();
        } else {
            //保存评论到bmob服务器,要保存的内容：评论内容，记录的Id，评论人的信息
            //获取到当前用户的信息
            User user = BmobUser.getCurrentUser(CommunityDetailsActivity.this, User.class);

            mCommentInfo = new CommentInfo();
            mCommentInfo.setRecordId(mRecordId);
            mCommentInfo.setCommentUserId(user.getObjectId());
            mCommentInfo.setCommentUsername(user.getUsername());
            mCommentInfo.setCommentUserPhoto(user.getPhoto().getFileUrl(this));
            mCommentInfo.setCommentContent(mEditMsg.getText().toString());

            mCommunityDetailsPresenter.addComment(mCommentInfo);

        }

    }

    //点击表情
    @OnClick(R.id.btn_emo)
    public void onEmoClick() {
        Toast.makeText(this, "点击了表情", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.community_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:

                break;
            case R.id.action_add:

                break;
            default:
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void success(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getCommentInfoSuccess(CommentMode response) {
        List<CommentMode.ResultsBean> commentList = new ArrayList<>();
        if (response.getResults().isEmpty()){
            //没有数据

        }else {
            for (CommentMode.ResultsBean c : response.getResults()) {
                commentList.add(c);
            }
            CommentListAdapter comentListAdapter = new CommentListAdapter(this, commentList);
            mRecyclerView.setAdapter(comentListAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            comentListAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void setPresenter(ICommunityDetailsContract.Presenter presenter) {
//        presenter.addComment(mCommentInfo);
        presenter.showComment(mRecordId);
    }
}
