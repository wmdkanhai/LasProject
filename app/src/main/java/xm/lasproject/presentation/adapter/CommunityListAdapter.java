package xm.lasproject.presentation.adapter;

import android.content.Context;

import java.util.List;

import xm.lasproject.R;
import xm.lasproject.bean.CommunityList;

/**
 * <pre>
 *     author : xm
 *     e-mail : wmdkanhai@qq.com
 *     time   : 2017/03/29
 *     desc   : xxx描述
 *     version: 1.0
 * </pre>
 */

public class CommunityListAdapter extends BaseRecyclerAdapter<CommunityList.ResultsBean> {

    private Context mContext;
    private List<CommunityList.ResultsBean> data;

    public CommunityListAdapter(Context ctx, List<CommunityList.ResultsBean> list) {
        super(ctx, list);
        this.mContext = ctx;
        this.data = list;
    }

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.item_communitylist_view;
    }

    @Override
    public void bindData(RecyclerViewHolder holder, int position, CommunityList.ResultsBean item) {
        holder.setText(R.id.tv_title,data.get(position).getTitle());
        holder.setText(R.id.tv_content,data.get(position).getContent());
        holder.setText(R.id.tv_username,data.get(position).getUsername());
        holder.setText(R.id.tv_comment,data.get(position).getTime());
        String sex = data.get(position).getSex();
        if ("女".equals(sex)){
            holder.getTextView(R.id.tv_username).setTextColor(0xffe67596);
        }else {
            holder.getTextView(R.id.tv_username).setTextColor(0xff5AC8FA);
        }
    }

}
