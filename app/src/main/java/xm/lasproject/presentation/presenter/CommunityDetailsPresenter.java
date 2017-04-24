package xm.lasproject.presentation.presenter;

import android.content.Context;

import com.zhy.http.okhttp.OkHttpUtils;

import cn.bmob.v3.listener.SaveListener;
import okhttp3.Call;
import xm.lasproject.bean.CommentInfo;
import xm.lasproject.bean.CommentMode;
import xm.lasproject.presentation.contract.ICommunityDetailsContract;
import xm.lasproject.repository.network.callback.CommentCallBack;

/**
 * <pre>
 *     author : xm
 *     e-mail : wmdkanhai@qq.com
 *     time   : 2017/03/24
 *     desc   : xxx描述
 *     version: 1.0
 * </pre>
 */

public class CommunityDetailsPresenter implements ICommunityDetailsContract.Presenter {
    private ICommunityDetailsContract.View mView;
    private Context mContext;

    public CommunityDetailsPresenter(Context context, ICommunityDetailsContract.View view) {
        this.mContext = context;
        this.mView = view;
        this.mView.setPresenter(this);
    }


    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }


    @Override
    public void addComment(CommentInfo commentInfo) {
        commentInfo.save(mContext, new SaveListener() {
            @Override
            public void onSuccess() {
                mView.success("成功添加评论");
            }

            @Override
            public void onFailure(int i, String s) {
                mView.showError(s);
            }
        });

    }

    @Override
    public void showComment(String recordId) {
        String url = "http://cloud.bmob.cn/248d651e447d9cc8/getCommentInfo";
        OkHttpUtils.post().addParams("recordId", recordId)
                .url(url).build()
                .execute(new CommentCallBack() {
                             @Override
                             public void onError(Call call, Exception e, int id) {
                                mView.showError(e.getMessage());
                             }

                             @Override
                             public void onResponse(CommentMode response, int id) {
                                 mView.getCommentInfoSuccess(response);
                                 mView.hideLoading();
                             }
                         }

                );
    }
}
