package xm.lasproject.presentation.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import xm.lasproject.R;
import xm.lasproject.bean.CommunityMode;

/**
 * <pre>
 *     author : xm
 *     e-mail : wmdkanhai@qq.com
 *     time   : 2017/03/29
 *     desc   : xxx描述
 *     version: 1.0
 * </pre>
 */

public class CommunityModeAdapter extends BaseRecyclerAdapter<CommunityMode.ResultsBean> {

    private Context mContext;
    private List<CommunityMode.ResultsBean> data;

    public CommunityModeAdapter(Context ctx, List<CommunityMode.ResultsBean> list) {
        super(ctx, list);
        this.mContext = ctx;
        this.data = list;
    }

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.item_gridview;
    }

    @Override
    public void bindData(RecyclerViewHolder holder, int position, CommunityMode.ResultsBean item) {
        holder.setText(R.id.textView, data.get(position).getModeDescribe());
        ImageView imageView = holder.getImageView(R.id.imageView);

        if (data.get(position).getModePicture()!=null) {
            Glide.with(mContext).load(data.get(position).getModePicture().getUrl()).into(imageView);
        } else {
            Glide.with(mContext).load(R.mipmap.ic_launcher).into(imageView);
        }
    }


}
