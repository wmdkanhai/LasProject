package xm.lasproject.presentation.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import xm.lasproject.R;
import xm.lasproject.bean.CommentMode;

/**
 * <pre>
 *     author : xm
 *     e-mail : wmdkanhai@qq.com
 *     time   : 2017/03/29
 *     desc   : xxx描述
 *     version: 1.0
 * </pre>
 */

public class CommentListAdapter extends BaseRecyclerAdapter<CommentMode.ResultsBean> {

    private Context mContext;
    private List<CommentMode.ResultsBean> data;

    public CommentListAdapter(Context ctx, List<CommentMode.ResultsBean> list) {
        super(ctx, list);
        this.mContext = ctx;
        this.data = list;
    }

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.item_comment_list_view;
    }

    @Override
    public void bindData(RecyclerViewHolder holder, int position, CommentMode.ResultsBean item) {
        holder.setText(R.id.tv_content,data.get(position).getCommentContent());
        holder.setText(R.id.tv_time,data.get(position).getCreatedAt());
        holder.setText(R.id.tv_name,data.get(position).getCommentUsername());
        ImageView imageView = holder.getImageView(R.id.imageView);
        Glide.with(mContext).load(data.get(position).getCommentUserPhoto().toString()).into(imageView);
    }


}
