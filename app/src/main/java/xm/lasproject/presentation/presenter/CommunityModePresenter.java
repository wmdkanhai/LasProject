package xm.lasproject.presentation.presenter;

import android.util.Log;

import com.zhy.http.okhttp.OkHttpUtils;

import okhttp3.Call;
import xm.lasproject.bean.CommunityMode;
import xm.lasproject.presentation.contract.ICommunityModeContract;
import xm.lasproject.repository.network.callback.CommunityModeCallBack;

/**
 * <pre>
 *     author : xm
 *     e-mail : wmdkanhai@qq.com
 *     time   : 2017/03/24
 *     desc   : xxx描述
 *     version: 1.0
 * </pre>
 */

public class CommunityModePresenter implements ICommunityModeContract.Presenter {
    private ICommunityModeContract.View mView;

    public CommunityModePresenter(ICommunityModeContract.View view) {
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
    public void getCommunityMode() {
        String url = "http://cloud.bmob.cn/248d651e447d9cc8/getCommunityMode";
        OkHttpUtils.get().url(url).build()
                .execute(new CommunityModeCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("exception", "onError: "+e.getMessage() );
                    }

                    @Override
                    public void onResponse(CommunityMode response, int id) {
                        mView.success(response);
                    }
                });
    }


//    @Override
//    public void getCommunityList() {
//        String url = "http://cloud.bmob.cn/248d651e447d9cc8/getCommunityThemeInfo";
//        OkHttpUtils.get().url(url).build()
//                .execute(new CommunityListCallBack() {
//                             @Override
//                             public void onError(Call call, Exception e, int id) {
//
//                             }
//
//                             @Override
//                             public void onResponse(CommunityList response, int id) {
//                                mView.success(response);
//                             }
//                         }
//
//                );
//    }
}
