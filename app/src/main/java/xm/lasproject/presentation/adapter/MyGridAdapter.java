package xm.lasproject.presentation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import xm.lasproject.R;
import xm.lasproject.bean.CommunityMode;

/**
 * <pre>
 *     author : xm
 *     e-mail : wmdkanhai@qq.com
 *     time   : 2017/03/28
 *     desc   : xxx描述
 *     version: 1.0
 * </pre>
 */

public class MyGridAdapter extends BaseAdapter {
    private List<CommunityMode.ResultsBean> mDataList;
    private Context mContext;

    public MyGridAdapter( Context context,List<CommunityMode.ResultsBean> dataList) {
        mDataList = dataList;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataList.indexOf(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_gridview, null);
        ImageView mImageView = (ImageView) view.findViewById(R.id.imageView);
        TextView mTextView = (TextView) view.findViewById(R.id.textView);
        Glide.with(mContext).load(mDataList.get(position).getModePicture().getUrl()).into(mImageView);
        mTextView.setText(mDataList.get(position).getModeDescribe());
        return view;
    }

}
