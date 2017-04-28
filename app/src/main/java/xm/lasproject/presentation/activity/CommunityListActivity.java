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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import xm.lasproject.R;
import xm.lasproject.bean.CommunityList;
import xm.lasproject.presentation.adapter.BaseRecyclerAdapter;
import xm.lasproject.presentation.adapter.CommunityListAdapter;
import xm.lasproject.presentation.contract.ICommunityListContract;
import xm.lasproject.presentation.presenter.CommunityListPresenter;

import static xm.lasproject.R.id.recyclerView;

public class CommunityListActivity extends AppCompatActivity implements ICommunityListContract.View {

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tb_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;
    @BindView(recyclerView)
    RecyclerView mRecyclerView;
    private CommunityListPresenter mCommunityListPresenter;
    private String mModeType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_list);
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        mModeType = intent.getStringExtra("modeType");
        ButterKnife.bind(this);
        mTvTitle.setText(title);
        mCommunityListPresenter = new CommunityListPresenter(this);
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

        mSwipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mCommunityListPresenter.getCommunityList(mModeType);
            }
        });

        showLoading();
    }

    private void refreshData() {
        mCommunityListPresenter.getCommunityList(mModeType);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.community_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.action_search:
//
//                break;
            case R.id.action_add:
                Intent intent = new Intent();
                intent.putExtra("modeType", mModeType);
                intent.setClass(this, CommunityAddActivity.class);
                startActivityForResult(intent,0);
                break;
            default:
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        refreshData();
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

    }

    @Override
    public void success(CommunityList communityList) {
        mSwipeRefresh.setRefreshing(false);
        final List<CommunityList.ResultsBean> dataList = new ArrayList<>();
        for (CommunityList.ResultsBean r : communityList.getResults()) {
            dataList.add(r);
        }

        if (dataList.isEmpty()) {
            //处理没有数据的情况

        } else {
            CommunityListAdapter communityListAdapter = new CommunityListAdapter(this, dataList);
            mRecyclerView.setAdapter(communityListAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            communityListAdapter.notifyDataSetChanged();
            communityListAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View itemView, int pos) {
                    Intent intent = new Intent();
                    CommunityList.ResultsBean resultsBean = dataList.get(pos);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("resultsBean", resultsBean);
                    intent.putExtras(bundle);
                    intent.setClass(CommunityListActivity.this, CommunityDetailsActivity.class);
                    startActivity(intent);
                }
            });
        }

    }


    @Override
    public void setPresenter(ICommunityListContract.Presenter presenter) {
        presenter.getCommunityList(mModeType);
    }
}
