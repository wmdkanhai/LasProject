package xm.lasproject.presentation.presenter;

import com.zhy.http.okhttp.OkHttpUtils;

import okhttp3.Call;
import xm.lasproject.bean.CommunityList;
import xm.lasproject.repository.network.callback.CommunityListCallBack;
import xm.lasproject.presentation.contract.ICommunityListContract;

/**
 * <pre>
 *     author : xm
 *     e-mail : wmdkanhai@qq.com
 *     time   : 2017/03/24
 *     desc   : xxx描述
 *     version: 1.0
 * </pre>
 */

public class CommunityListPresenter implements ICommunityListContract.Presenter {
    private ICommunityListContract.View mView;

    public CommunityListPresenter(ICommunityListContract.View view) {
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
    public void getCommunityList(String mModeType) {
        String url = "http://cloud.bmob.cn/248d651e447d9cc8/getCommunityThemeInfoByModeType";
        OkHttpUtils.post().addParams("modeType",mModeType)
                .url(url).build()
                .execute(new CommunityListCallBack() {
                             @Override
                             public void onError(Call call, Exception e, int id) {

                             }

                             @Override
                             public void onResponse(CommunityList response, int id) {
                                mView.success(response);
                                 mView.hideLoading();
                             }
                         }

                );
    }
}
