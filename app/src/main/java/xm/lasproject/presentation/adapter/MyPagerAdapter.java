package xm.lasproject.presentation.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * <pre>
 *     author : xm
 *     e-mail : wmdkanhai@qq.com
 *     time   : 2017/03/28
 *     desc   : 社区模块中轮播图的adapter
 *     version: 1.0
 * </pre>
 */

public class MyPagerAdapter extends PagerAdapter {
    private List<ImageView> mImageViewList;

    public MyPagerAdapter(List<ImageView> imageViewList) {
        mImageViewList = imageViewList;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    //1，返回要显示的条目内容，创建条目
    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        //container:容器，也就是ViewPager
        //position：当前要显示的位置

        int newPosition = position % mImageViewList.size();

        final ImageView imageView = mImageViewList.get(newPosition);
        //把View对象添加到container中
        container.addView(imageView);
        //把View对象返回给适配器
        return imageView;
    }

    //2，销毁条目
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //object 要销毁的对象
        container.removeView((View) object);
    }

}
