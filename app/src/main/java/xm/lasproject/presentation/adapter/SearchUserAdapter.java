package xm.lasproject.presentation.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import xm.lasproject.R;
import xm.lasproject.bean.User;

/**
 * <pre>
 *     author : xm
 *     e-mail : wmdkanhai@qq.com
 *     time   : 2017/03/31
 *     desc   : xxx描述
 *     version: 1.0
 * </pre>
 */

public class SearchUserAdapter extends BaseRecyclerAdapter<User> {
    private Context mContext;
    private List<User> data;

    public SearchUserAdapter(Context ctx, List list) {
        super(ctx, list);
        this.mContext = ctx;
        this.data = list;
    }


    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.item_search_user;
    }

    @Override
    public void bindData(RecyclerViewHolder holder, int position, User item) {
        final ImageView imageView = holder.getImageView(R.id.imageView);
        if (data.get(position).getPhoto() != null) {
            final String photo = data.get(position).getPhoto();
            Glide.with(mContext).load(photo).into(imageView);
        }else {
            imageView.setImageResource(R.mipmap.ic_launcher);
        }
        holder.setText(R.id.tv_username, data.get(position).getUsername());
    }

}
