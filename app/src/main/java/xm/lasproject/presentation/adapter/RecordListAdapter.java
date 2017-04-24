package xm.lasproject.presentation.adapter;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import xm.lasproject.R;
import xm.lasproject.bean.RecordMode;

/**
 * <pre>
 *     author : xm
 *     e-mail : wmdkanhai@qq.com
 *     time   : 2017/03/29
 *     desc   : xxx描述
 *     version: 1.0
 * </pre>
 */

public class RecordListAdapter extends BaseRecyclerAdapter<RecordMode.ResultsBean> {

    private Context mContext;
    private List<RecordMode.ResultsBean> data;

    public RecordListAdapter(Context ctx, List<RecordMode.ResultsBean> list) {
        super(ctx, list);
        this.mContext = ctx;
        this.data = list;

    }



    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.item_record_list_view;
    }

    @Override
    public void bindData(RecyclerViewHolder holder, int position, RecordMode.ResultsBean item) {

        ImageView imageView = holder.getImageView(R.id.imageView);
        Glide.with(mContext).load(data.get(position).getUserPhotoUrl()).into(imageView);
        Log.e("-----", "bindData: "+data.get(position).getRecordContent() );
        holder.setText(R.id.tv_content,data.get(position).getRecordContent());
        String createdAt = data.get(position).getCreatedAt();
        //获取到日期后，格式化时间，2017-03-14
        String[] split = createdAt.split(" ");
        holder.setText(R.id.tv_time,split[0]);
    }

}

