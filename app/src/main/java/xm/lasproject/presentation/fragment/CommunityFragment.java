package xm.lasproject.presentation.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import xm.lasproject.R;
import xm.lasproject.bean.CommunityMode;
import xm.lasproject.presentation.activity.CommunityListActivity;
import xm.lasproject.presentation.activity.CommunitySearchActivity;
import xm.lasproject.presentation.adapter.BaseRecyclerAdapter;
import xm.lasproject.presentation.adapter.CommunityModeAdapter;
import xm.lasproject.presentation.adapter.MyPagerAdapter;
import xm.lasproject.presentation.contract.ICommunityModeContract;
import xm.lasproject.presentation.presenter.CommunityModePresenter;

import static xm.lasproject.R.id.viewPager;

/**
 * <pre>
 *     author : xm
 *     e-mail : wmdkanhai@qq.com
 *     time   : 2017/03/22
 *     desc   : 社区Fragment
 *     version: 1.0
 * </pre>
 */

public class CommunityFragment extends Fragment implements ViewPager.OnPageChangeListener, ICommunityModeContract.View {

    @BindView(R.id.tb_toolbar)
    Toolbar mToolbar;
    @BindView(viewPager)
    ViewPager mViewPager;
    @BindView(R.id.tv_describe)
    TextView mTvDescribe;
    @BindView(R.id.ll_point_container)
    LinearLayout mLlPointContainer;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;


    private int[] mImageIds;
    private List<ImageView> mImageViewList;
    private View mPointView;
    private LinearLayout.LayoutParams layoutParams;
    private String[] contentDesc;
    private int previousSelectedPosition = 0;
    private boolean isRunning = false;
    final Handler mHandler = new Handler();
    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
            mHandler.postDelayed(this, 3000);
        }
    };
    private CommunityModePresenter mCommunityModePresenter;

    public CommunityFragment() {
    }

    public static CommunityFragment newInstance() {
        return new CommunityFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_community, container, false);
        ButterKnife.bind(this, rootView);
        mViewPager.addOnPageChangeListener(this);//设置页面更新监听
        initView();
        mCommunityModePresenter = new CommunityModePresenter(this);
        initData();
        initAdapter();
        startpolling();
        return rootView;
    }

    /**
     * 开始轮询
     */
    private void startpolling() {
//        //开始轮询
//        new Thread() {
//            @Override
//            public void run() {
//                isRunning = true;
//                while (isRunning) {
//                    try {
//                        Thread.sleep(5000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
//                        }
//                    });
//                }
//            }
//        }.start();
        // 定时循环
        mHandler.postDelayed(mRunnable, 3000);
    }

    /**
     * 对Toolbar进行初始化设置
     */
    private void initView() {
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        //去掉自带的标题
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.community_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                startActivity(new Intent(getActivity(), CommunitySearchActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * 初始化轮播图的数据，暂时使用的是写死的数据，后期改为数据库数据
     */
    private void initData() {
        //初始化图片
        mImageIds = new int[]{R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.d, R.drawable.e};
        //初始化文本
        contentDesc = new String[]{
                "我就是我，不一样的烟火",
                "阳光下的我，就是这么美丽",
                "树下的我，在想你",
                "我就如同这大地一样狂野",
                "青涩如我，你是否喜欢"
        };
        ImageView mImageView;
        mImageViewList = new ArrayList<>();
        for (int i = 0; i < mImageIds.length; i++) {
            mImageView = new ImageView(getActivity());
            mImageView.setBackgroundResource(mImageIds[i]);
            mImageViewList.add(mImageView);
            //添加小白点
            mPointView = new View(getActivity());
            mPointView.setBackgroundResource(R.drawable.selector_bg_point);
            //给小白点设置大小
            layoutParams = new LinearLayout.LayoutParams(8, 8);
            //给小白点设置间距
            if (i != 0) {
                layoutParams.leftMargin = 14;
            }
            mPointView.setEnabled(false);
            mLlPointContainer.addView(mPointView, layoutParams);
        }

    }

    /**
     * 给轮播图下的小白点进行设置
     */
    private void initAdapter() {
        //给第一个小白点设置显示
        mLlPointContainer.getChildAt(0).setEnabled(true);
        mTvDescribe.setText(contentDesc[0]);
        //设置适配器
        mViewPager.setAdapter(new MyPagerAdapter(mImageViewList));

        //默认设置到中间的某个位置
        int pos = Integer.MAX_VALUE / 2 - (Integer.MAX_VALUE / 2 % mImageViewList.size());
        mViewPager.setCurrentItem(pos);

    }

    //滚动的时候调用
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    //新的条目被选中的时候调用
    @Override
    public void onPageSelected(int position) {
        int newPosition = position % mImageViewList.size();
        mTvDescribe.setText(contentDesc[newPosition]);
        mLlPointContainer.getChildAt(previousSelectedPosition).setEnabled(false);
        mLlPointContainer.getChildAt(newPosition).setEnabled(true);
        previousSelectedPosition = newPosition;
    }

    //滚动状态变化时调用
    @Override
    public void onPageScrollStateChanged(int state) {

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning = false;
        mHandler.removeCallbacks(mRunnable);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError(String message) {

    }

    @Override
    public void success(CommunityMode response) {
        final List<CommunityMode.ResultsBean> dataList = new ArrayList<>();
        for (CommunityMode.ResultsBean resultsBean : response.getResults()) {
            dataList.add(resultsBean);
        }
        CommunityModeAdapter communityModeAdapter = new CommunityModeAdapter(getActivity(), dataList);
        mRecyclerView.setAdapter(communityModeAdapter);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        communityModeAdapter.notifyDataSetChanged();

        communityModeAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int pos) {
                Intent intent = new Intent();
                intent.putExtra("title", dataList.get(pos).getModeDescribe());
                intent.putExtra("modeType", dataList.get(pos).getModeType());
                intent.setClass(getActivity(), CommunityListActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void setPresenter(ICommunityModeContract.Presenter presenter) {
        presenter.getCommunityMode();
    }
}