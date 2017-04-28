package xm.lasproject.presentation.presenter;

import com.zhy.http.okhttp.OkHttpUtils;

import okhttp3.Call;
import xm.lasproject.bean.RecordMode;
import xm.lasproject.presentation.contract.IRecordContract;
import xm.lasproject.repository.network.callback.RecordListCallBack;

/**
 * <pre>
 *     author : xm
 *     e-mail : wmdkanhai@qq.com
 *     time   : 2017/03/24
 *     desc   : xxx描述
 *     version: 1.0
 * </pre>
 */

public class RecordPresenter implements IRecordContract.Presenter {
    private IRecordContract.View mView;

    public RecordPresenter(IRecordContract.View view) {
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
    public void getRecordList(String userObjectId) {
        String url = "http://cloud.bmob.cn/248d651e447d9cc8/getRecordInfo";
        OkHttpUtils.post().addParams("userObjectId", userObjectId)
                .url(url)
                .build()
                .execute(new RecordListCallBack() {
                             @Override
                             public void onError(Call call, Exception e, int id) {
                                 mView.showError(e.getMessage());
                             }

                             @Override
                             public void onResponse(RecordMode response, int id) {
                                 mView.success(response);
                                 mView.hideLoading();
                             }
                         }
                );
    }
}
