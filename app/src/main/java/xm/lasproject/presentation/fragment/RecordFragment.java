package xm.lasproject.presentation.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;
import xm.lasproject.R;
import xm.lasproject.bean.RecordMode;
import xm.lasproject.bean.User;
import xm.lasproject.presentation.activity.AddRecordActivity;
import xm.lasproject.presentation.activity.RecordDetailsActivity;
import xm.lasproject.presentation.adapter.BaseRecyclerAdapter;
import xm.lasproject.presentation.adapter.RecordListAdapter;
import xm.lasproject.presentation.contract.IRecordContract;
import xm.lasproject.presentation.presenter.RecordPresenter;

/**
 * <pre>
 *     author : xm
 *     e-mail : wmdkanhai@qq.com
 *     time   : 2017/03/22
 *     desc   : xxx描述
 *     version: 1.0
 * </pre>
 */

public class RecordFragment extends Fragment implements IRecordContract.View {

    @BindView(R.id.tb_toolbar)
    Toolbar mTbToolbar;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.imageView)
    ImageView mImageView;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbar;
    @BindView(R.id.appBar)
    AppBarLayout mAppBar;
    private RecordPresenter mRecordPresenter;
    private User mUser;
    private RecordListAdapter mRecordListAdapter;
    private String mPairingUserId;

    public RecordFragment() {
    }

    public static RecordFragment newInstance() {
        return new RecordFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_record, container, false);
        ButterKnife.bind(this, rootView);
        mUser = BmobUser.getCurrentUser(getActivity(), User.class);

        String pairingInfo = mUser.getPairingInfo();
        if (pairingInfo != null) {
            String[] split = pairingInfo.split(",");
            mPairingUserId = split[0];
        }

        mRecordPresenter = new RecordPresenter(this);
        initToolbar();
        showLoading();

        return rootView;
    }

    //设置Toolbar
    private void initToolbar() {
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mTbToolbar);
        mCollapsingToolbar.setTitle("我的记录");
        Glide.with(this).load(R.drawable.bg_splash).into(mImageView);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.record_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                Intent intent = new Intent();
                intent.setClass(getActivity(), AddRecordActivity.class);
                startActivityForResult(intent, 100);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100){
            refreshData();
        }
    }

    private void refreshData() {
        mRecordPresenter.getRecordList(mUser.getObjectId());
        if (mPairingUserId != null) {
            mRecordPresenter.getRecordList(mPairingUserId);
        }

    }

    @Override
    public void showLoading() {
//        mSwipeRefresh.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
//        mSwipeRefresh.setRefreshing(false);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(getActivity(), "出错了：" + message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void success(List<RecordMode.ResultsBean> list) {
        ComparatorDate c = new ComparatorDate();
        Collections.sort(list, c);
        setAdapterDatas(list);
    }


    private void setAdapterDatas(final List<RecordMode.ResultsBean> dataList) {
        mRecordListAdapter = new RecordListAdapter(getActivity(), dataList);
        mRecyclerView.setAdapter(mRecordListAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecordListAdapter.notifyDataSetChanged();
        mRecordListAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int pos) {
                Intent intent = new Intent();
                RecordMode.ResultsBean resultsBean = dataList.get(pos);
                Bundle bundle = new Bundle();
                bundle.putSerializable("recordDetails", resultsBean);
                intent.putExtras(bundle);
                intent.setClass(getActivity(), RecordDetailsActivity.class);
                startActivityForResult(intent, 101);
            }
        });
    }

    @Override
    public void setPresenter(IRecordContract.Presenter presenter) {
        presenter.getRecordList(mUser.getObjectId());
//        if (mPairingUserId != null) {
//            presenter.getRecordList(mPairingUserId);
//        }
    }
}


/**
 * 时间比较器，最近的记录显示在前边
 */
class ComparatorDate implements Comparator {
    public int compare(Object obj1, Object obj2) {
        RecordMode.ResultsBean o1 = (RecordMode.ResultsBean) obj1;
        RecordMode.ResultsBean o2 = (RecordMode.ResultsBean) obj1;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-M-d H:mm:ss");
        Date begin = null;
        Date end = null;
        try {
            begin = format.parse(o1.getCreatedAt().toString());
            end = format.parse(o2.getCreatedAt().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (begin.before(end)) {
            return 1;
        } else {
            return -1;
        }
    }
}