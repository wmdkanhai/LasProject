package xm.lasproject.presentation.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import xm.lasproject.R;
import xm.lasproject.bean.Conversation;
import xm.lasproject.bean.NewFriend;
import xm.lasproject.bean.NewFriendConversation;
import xm.lasproject.bean.NewFriendManager;
import xm.lasproject.bean.PrivateConversation;
import xm.lasproject.presentation.contract.IMutlipleItem;
import xm.lasproject.util.TimeUtil;

/**
 * <pre>
 *     author : xm
 *     e-mail : wmdkanhai@qq.com
 *     time   : 2017/05/09
 *     desc   : 会话fragment ，也就是第二个fragment
 *     version: 1.0
 * </pre>
 */

public class ConversationFragment extends Fragment {

    @BindView(R.id.tb_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;
    @BindView(R.id.imageView)
    ImageView mImageView;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_time)
    TextView mTvTime;
    @BindView(R.id.tv_content)
    TextView mTvContent;
    @BindView(R.id.rl1)
    RelativeLayout mRl1;
    @BindView(R.id.rl2)
    RelativeLayout mRl2;
    private List<Conversation> mConversations;


    public ConversationFragment() {
    }

//    public ConversationFragment(Context c) {
//        this.mContext = c;
//    }

    public static ConversationFragment newInstance() {
        return new ConversationFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_conversation, container, false);
        ButterKnife.bind(this, rootView);
        //单一布局
        IMutlipleItem<Conversation> mutlipleItem = new IMutlipleItem<Conversation>() {

            @Override
            public int getItemViewType(int postion, Conversation c) {
                return 0;
            }

            @Override
            public int getItemLayoutId(int viewtype) {
                return R.layout.item_conversation;
            }

            @Override
            public int getItemCount(List<Conversation> list) {
                return list.size();
            }
        };
        mSwipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mConversations = getConversations();
            }
        });

        mConversations = getConversations();
        if (!mConversations.isEmpty()) {

            mRl1.setVisibility(View.GONE);
            mRl2.setVisibility(View.VISIBLE);

            mTvName.setText(mConversations.get(0).getcName());
            Glide.with(getActivity()).load(mConversations.get(0).getAvatar()).into(mImageView);
            mTvContent.setText(mConversations.get(0).getLastMessageContent());
            mTvTime.setText(TimeUtil.getChatTime(false, mConversations.get(0).getLastMessageTime()));
            mSwipeRefresh.setRefreshing(false);
        }

//        if (!conversations.isEmpty()) {
//            Log.e("--", "getcName: " + conversations.get(0).getcName());
//            Log.e("--", "getLastMessageTime: " + conversations.get(0).getLastMessageTime());
//            Log.e("--", "getLastMessageContent: " + conversations.get(0).getLastMessageContent());
//            Log.e("--", "getAvatar: " + conversations.get(0).getAvatar());
//            Log.e("--", "getUnReadCount: " + conversations.get(0).getUnReadCount());
//            mTvName.setText(conversations.get(0).getcName());
////            mTvName.setText("hahhahah");
////            mTvName.setText();
////            Glide.with(getActivity()).load(conversations.get(0).getAvatar()).into(mIvRecentAvatar);
////
////            mTvRecentMsg.setText(conversations.get(0).getLastMessageContent());
////            mTvRecentTime.setText(TimeUtil.getChatTime(false,conversations.get(0).getLastMessageTime()));
////            long unread = conversations.get(0).getUnReadCount();
////            if(unread>0){
////                mTvRecentUnread.setEnabled(true);
////                mTvRecentUnread.setText(String.valueOf(unread));
////            }else{
////                mTvRecentUnread.setEnabled(false);
////            }
//            mSwipeRefresh.setRefreshing(false);
//        }
        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
//        mSwipeRefresh.setRefreshing(true);
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    /**
     * 获取会话列表的数据：增加新朋友会话
     *
     * @return
     */
    private List<Conversation> getConversations() {
        //添加会话
        List<Conversation> conversationList = new ArrayList<>();
        conversationList.clear();
        List<BmobIMConversation> list = BmobIM.getInstance().loadAllConversation();
        if (list != null && list.size() > 0) {
            for (BmobIMConversation item : list) {
                switch (item.getConversationType()) {
                    case 1://私聊
                        conversationList.add(new PrivateConversation(item));
                        break;
                    default:
                        break;
                }
            }
        }
        //添加新朋友会话-获取好友请求表中最新一条记录
        List<NewFriend> friends = NewFriendManager.getInstance(getActivity()).getAllNewFriend();
        if (friends != null && friends.size() > 0) {
            conversationList.add(new NewFriendConversation(friends.get(0)));
        }
        //重新排序
        Collections.sort(conversationList);
        mSwipeRefresh.setRefreshing(false);
        return conversationList;
    }

    @OnClick(R.id.rl2)
    public void rl2Click(){
        Toast.makeText(getActivity(), "点击了", Toast.LENGTH_SHORT).show();
    }

}
